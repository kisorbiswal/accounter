package com.vimukti.accounter.developer.api;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

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
	public void doFilter(ServletRequest req2, ServletResponse resp2,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) req2;
		HttpServletResponse resp = (HttpServletResponse) resp2;

		String url = req.getQueryString();
		String signature = req.getParameter(SIGNATURE);
		if (signature == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Signature must be present");
			return;
		}
		String signatureProperty = new String("&" + SIGNATURE + "="
				+ URLEncoder.encode(signature, "utf8"));
		String remainingUrl = url.replace(signatureProperty, "");
		String apiKey = req.getParameter("ApiKey");
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		try {
			Date expire = format.parse(req.getParameter("Expire"));
			// TODO
		} catch (ParseException e1) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Wrong expire date formate");
			return;
		}
		Company company = null;
		Session session = HibernateUtil.openSession();
		try {
			Developer developer = getDeveloperByApiKey(apiKey);
			if (developer == null) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Wrong API key.");
				return;
			}

			String secretKey = developer.getSecretKey();
			String sighned = doSigning(remainingUrl, secretKey);
			if (!sighned.equals(signature)) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Signature not matched");
				return;
			}

			Client client = developer.getClient();
			String companyId = req.getParameter("CompanyId");
			long id = 0;
			try {
				id = Long.parseLong(companyId);
			} catch (Exception e) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Company Id should be long");
				return;
			}
			company = getCompany(id, client);
			if (company == null) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Wrong company id");
				return;
			}

			req.setAttribute("companyId", company.getID());
			req.setAttribute("emailId", client.getEmailId());
			arg2.doFilter(req2, resp2);
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Internal Error.");
		} finally {
			if (session != null) {
				session.close();
			}
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

	private String doSigning(String url, String secretKeystr) {
		byte[] secretKeyBytes = secretKeystr.getBytes();
		SecretKeySpec keySpec = new SecretKeySpec(secretKeyBytes, ALGORITHM);
		try {
			Mac mac = Mac.getInstance(ALGORITHM);
			mac.init(keySpec);
			byte[] doFinal = mac.doFinal(url.getBytes());
			String string = new String(doFinal);
			return getURLDecode(URLEncoder.encode(string, "utf8"));
		} catch (Exception e) {
		}
		return url;
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

	private String getURLDecode(String string) throws Exception {
		return URLDecoder.decode(string, "utf8");
	}
}
