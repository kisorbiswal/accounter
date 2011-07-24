package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.security.Security;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;

import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.vimukti.accounter.main.LiveServer;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;

public class SubscriptionAuthenticationServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String AUTHENTICATION_SUCCEEDED = "101";
	private String AUTHENTICATION_FAILED = "102";
	private String COMPANY_EXPIRED = "103";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String emailId = null;
		String password = null;
		String domainName = null;
		try {
			JSONObject jsonObject = new JSONObject(req
					.getParameter("jsonObject"));
			emailId = jsonObject.getString("emailId");
			password = jsonObject.getString("password");
			domainName = jsonObject.getString("domainName");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Session session = null;
		try {
			session = HibernateUtil.openSession(domainName);
			CollaberIdentity identity = doLogin(req, resp, emailId, password,
					domainName, session);
			if (identity == null) {
				dispatchView(req, resp, AUTHENTICATION_FAILED);
			} else if (identity.getClientIdentity().getCompany().deletionDate
					.before(new Date())) {
				dispatchView(req, resp, COMPANY_EXPIRED);
				// TODO Karthik
			} else {
				dispatchView(req, resp, AUTHENTICATION_SUCCEEDED + ":"
						+ identity.getID());
			}
		} finally {
			session.close();
		}
	}

	public void dispatchView(HttpServletRequest request,
			HttpServletResponse response, String view) {
		try {
			response.getWriter().write(view);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private CollaberIdentity doLogin(HttpServletRequest request,
			HttpServletResponse response, String emailId, String password,
			String domainName, Session session) {

		if (emailId != null && password != null) {
			password = HexUtil
					.bytesToHex(Security.makeHash(emailId + password));
			CollaberIdentity identity = getIDentity(emailId, password,
					domainName, session);
			return identity;
		}
		return null;
	}

	private CollaberIdentity getIDentity(String emailId, String password,
			String domainName, Session session) {

		if (domainName == null) {
			return null;
		}
		if (!LiveServer.getInstance().isCompanyExists(domainName)) {
			return null;
		}

		CollaberIdentity identity = null;
		Query query = session
				.getNamedQuery("getIDentity.from.emailid.and.password");
		query.setParameter("emailid", emailId);
		query.setParameter("password", password);
		identity = (CollaberIdentity) query.uniqueResult();
		return identity;
	}
}
