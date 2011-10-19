package com.vimukti.accounter.developer.api;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.mortbay.util.UrlEncoded;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Developer;
import com.vimukti.accounter.utils.HibernateUtil;

public class ApiFilter implements Filter {
	public static final String SIGNATURE = "Signature";
	private static final String ALGORITHM = "hmacSHA256";
	private static final String DATE_FORMAT = "yyyy.MM.dd G 'at' HH:mm:ss z";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest req2 = (HttpServletRequest) req;
		String url = req2.getQueryString();
		String signature = req.getParameter(SIGNATURE);
		String signatureProperty = new String("&" + SIGNATURE + "="
				+ new UrlEncoded(signature).encode());
		String remainingUrl = url.replace(signatureProperty, "");
		String apiKey = req.getParameter("ApiKey");
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		try {
			Date expire = format.parse(req.getParameter("Expire"));
			// TODO
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		Company company = null;
		Session session = HibernateUtil.openSession();
		try {
			Developer developer = getDeveloperByApiKey(apiKey);
			if (developer == null) {
				throw new ServletException("Wrong ApiKey.");
			}

			String secretKey = developer.getSecretKey();
			String sighned = doSigning(remainingUrl, secretKey);
			sighned = getURLDecode(new UrlEncoded(sighned).encode());
			if (!sighned.equals(signature)) {
				throw new ServletException("Signature was not matched.");
			}

			Client client = developer.getClient();
			String companyId = req.getParameter("CompanyId");
			long id = 0;
			try {
				id = Long.parseLong(companyId);
			} catch (Exception e) {
				throw new ServletException("Wrong CompanyId");
			}
			company = getCompany(id, client);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		if (company == null) {
			throw new ServletException("You don't have permission.");
		} else {
			req.setAttribute("companyId", company.getID());
			arg2.doFilter(req, resp);
		}
	}

	private Company getCompany(long id, Client client) {
		Session session = HibernateUtil.getCurrentSession();
		Object result = session
				.getNamedQuery("get.ServerCompany.by.companyId.and.client")
				.setLong("id", id).setParameter("client", client)
				.uniqueResult();
		return (Company) ((Object[]) result)[0];
	}

	private String doSigning(String data, String secretKeystr) {
		byte[] secretKey = secretKeystr.getBytes();
		SecretKeySpec keySpec = new SecretKeySpec(secretKey, ALGORITHM);
		try {
			Mac mac = Mac.getInstance(ALGORITHM);
			mac.init(keySpec);
			byte[] doFinal = mac.doFinal(data.getBytes());
			String encode = Base64.encode(doFinal);
			return encode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Developer getDeveloperByApiKey(String apiKey) {
		Session session = HibernateUtil.getCurrentSession();
		return (Developer) session.getNamedQuery("get.developer.by.apiKey")
				.setParameter("apiKey", apiKey).uniqueResult();
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	private String getURLDecode(String string) {
		// try {
		return URLDecoder.decode(string);
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		// return string;
	}
}
