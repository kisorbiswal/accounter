package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Activation;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.main.Server;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.utils.Security;

public class NewLoginServlet extends BaseServlet {

	private static final String LOGIN_VIEW = "/WEB-INF/login.jsp";

	protected static final Log LOG = LogFactory.getLog(ActivationServlet.class);

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Client client = doLogin(request, response);
		if (client != null) {
			// if valid credentials are there we redirect to <dest> param or
			// /companies

			if (client.isRequirePasswordReset()) {
				client.setRequirePasswordReset(false);
			}

			Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
			try {
				saveEntry(client);
			} finally {
				session.close();
			}

			String destUrl = request.getParameter(PARAM_DESTINATION);
			HttpSession httpSession = request.getSession();
			httpSession.setAttribute(EMAIL_ID, client.getEmailId());
			if (destUrl == null || destUrl.isEmpty()) {
				redirectExternal(request, response, COMPANIES_URL);
			} else {
				redirectExternal(request, response, destUrl);
			}

			return;
		} else {
			request.setAttribute(
					"message",
					"The details that you have are incorrect. If you have forgotten your details, please refer to your invitation or contact the person who invited you to Accounter.");
			dispatch(request, response, LOGIN_VIEW);
		}
	}

	private Client doLogin(HttpServletRequest request,
			HttpServletResponse response) {
		String emailId = request.getParameter(EMAIL_ID);
		String password = request.getParameter(PASSWORD);

		Client client = getClient(emailId, password);
		if (client != null && request.getParameter("staySignIn") != null
				&& request.getParameter("staySignIn").equals("on")) {
			addUserCookies(response, client);
		}
		return client;
	}

	private Client getClient(String emailId, String password) {
		if (emailId == null || password == null) {
			return null;
		}
		emailId = emailId.trim();
		password = HexUtil.bytesToHex(Security.makeHash(emailId
				+ password.trim()));

		Session session = HibernateUtil.openSession(LOCAL_DATABASE);
		try {
			Client client = null;
			Query query = session
					.getNamedQuery("getclient.from.central.db.using.emailid.and.password");
			query.setParameter(EMAIL_ID, emailId);
			query.setParameter(PASSWORD, password);
			client = (Client) query.uniqueResult();
			return client;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		LOG.info(request);
		// We check if the session is already there, if it is, we check if user
		// have to reset his password(by using a flag on the user object)
		HttpSession httpSession = request.getSession();
		String emailID = (String) httpSession.getAttribute(EMAIL_ID);
		if (emailID == null) {
			// if session is not there then we show the form and user fills it
			// which gets submitted to same url
			dispatch(request, response, LOGIN_VIEW);
		} else {

			// Get the Client using the mail id
			Session session = HibernateUtil.openSession(LOCAL_DATABASE);
			try {
				Query query = session.getNamedQuery("getClient.by.mailId");
				query.setParameter(EMAIL_ID, emailID);

				Client client = (Client) query.uniqueResult();
				if (client != null) {
					if (client.isActive()) {
						if (client.isRequirePasswordReset()) {
							// If session is there and he has to reset the
							// password
							// then do an external redirect to /resetpassword
							// url
							redirectExternal(request, response,
									RESET_PASSWORD_URL);
						} else {
							// if session is there and no need to reset password
							// then do external redirect to <dest> param or
							// /companies
							String destUrl = request
									.getParameter(PARAM_DESTINATION);
							if (destUrl == null || destUrl.isEmpty()) {
								redirectExternal(request, response,
										COMPANIES_URL);
							} else {
								redirectExternal(request, response, destUrl);
							}

						}
					} else {
						// not activated so resend the activation mail
						// Generate Token and create Activation and save. then
						// send
						String token = SecureUtils.createID();

						// get activation object by email id

						query = session
								.getNamedQuery("get.activation.by.mailId");
						query.setParameter(EMAIL_ID, emailID);
						Activation activation = (Activation) query
								.uniqueResult();
						// reset the activation code and save it
						activation.setToken(token);
						saveEntry(activation);

						// resend activation mail
						sendActivationEmail(token, client);

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (session != null)
					session.close();
			}

		}

	}

}
