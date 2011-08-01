package com.vimukti.accounter.developer.api;

import java.io.IOException;

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

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Developer;
import com.vimukti.accounter.core.ServerCompany;
import com.vimukti.accounter.servlets.BaseServlet;
import com.vimukti.accounter.utils.HibernateUtil;

public class ApiFilter implements Filter {
	public static final String SIGNATURE = "signature";
	private static final String ALGORITHM = "hmacSHA256";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest req2 = (HttpServletRequest) req;
		String url = req2.getRequestURI();
		String signature = req.getParameter(SIGNATURE);
		String remainingUrl = url.replace(SIGNATURE + "=" + signature, "");
		String apiKey = req.getParameter("apiKey");
		ServerCompany company = null;
		Session session = HibernateUtil.openSession(BaseServlet.LOCAL_DATABASE);
		try {
			Developer developer = getDeveloperByApiKey(apiKey);
			if (developer == null) {
				throw new ServletException("Wrong ApiKey.");
			}

			String secretKey = developer.getSecretKey();
			String sighned = doSigning(remainingUrl, secretKey);
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
			req.setAttribute("companyName", company.getCompanyName());
		}

	}

	private ServerCompany getCompany(long id, Client client) {
		Session session = HibernateUtil.getCurrentSession();
		return (ServerCompany) session
				.getNamedQuery("get.ServerCompany.by.companyId.and.client")
				.setParameter(0, id).setParameter(1, client).uniqueResult();
	}

	private String doSigning(String data, String secretKeystr) {
		byte[] secretKey = secretKeystr.getBytes();
		SecretKeySpec keySpec = new SecretKeySpec(secretKey, ALGORITHM);
		try {
			Mac mac = Mac.getInstance(ALGORITHM);
			mac.init(keySpec);
			byte[] doFinal = mac.doFinal(data.getBytes());
			return new String(doFinal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Developer getDeveloperByApiKey(String apiKey) {
		Session session = HibernateUtil.getCurrentSession();
		return (Developer) session.getNamedQuery("get.developer.by.apikey")
				.setParameter(0, apiKey).uniqueResult();
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
