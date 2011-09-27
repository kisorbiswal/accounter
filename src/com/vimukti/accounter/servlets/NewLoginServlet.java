package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Activation;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.RememberMeKey;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.utils.Security;

public class NewLoginServlet extends BaseServlet {

	private static final String LOGIN_VIEW = "/WEB-INF/login.jsp";
	private static final String ACTIVATION_VIEW = "/WEB-INF/activation.jsp";

	protected static final Log LOG = LogFactory.getLog(ActivationServlet.class);

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Session session = HibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Client client = doLogin(request, response);
			if (client != null) {
				// if valid credentials are there we redirect to <dest> param or
				// /companies

				if (!client.isActive()) {
					// TODO send the ResetPAssword page
					// client.setRequirePasswordReset(false);
					// saveEntry(client);
					request.setAttribute(
							"successmessage",
							"Your account is not yet activated. Please enter the activation code below to activate your account or click on Resend activate code to get the activation code again.");
					dispatch(request, response, ACTIVATION_VIEW);

				} else {

					if (client.isRequirePasswordReset()) {
						client.setRequirePasswordReset(false);
						session.saveOrUpdate(client);
					}

					String destUrl = request.getParameter(PARAM_DESTINATION);
					HttpSession httpSession = request.getSession();
					httpSession.setAttribute(EMAIL_ID, client.getEmailId());
					if (destUrl == null || destUrl.isEmpty()) {
						client.setLoginCount(client.getLoginCount() + 1);
						client.setLastLoginTime(System.currentTimeMillis());
						session.saveOrUpdate(client);
						redirectExternal(request, response, COMPANIES_URL);
					} else {
						redirectExternal(request, response, destUrl);
					}

				}
			} else {
				request.setAttribute(
						"message",
						"The details that you have are incorrect. If you have forgotten your details, please refer to your invitation or contact the person who invited you to Accounter.");
				dispatch(request, response, LOGIN_VIEW);
			}
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	private Client doLogin(HttpServletRequest request,
			HttpServletResponse response) throws NoSuchAlgorithmException {
		String emailId = request.getParameter(EMAIL_ID);
		String password = request.getParameter(PASSWORD);

		Client client = getClient(emailId, password);
		if (client != null && request.getParameter("staySignIn") != null
				&& request.getParameter("staySignIn").equals("on")) {
			// Inserting RememberMeKey
			Session session = HibernateUtil.getCurrentSession();

			byte[] makeHash = Security.makeHash(client.getEmailId()
					+ Security.makeHash(client.getPassword()));
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(makeHash);
			String rememberMeKey = HexUtil.bytesToHex(digest);
			RememberMeKey rememberMe = new RememberMeKey(client.getEmailId(),
					rememberMeKey);
			session.save(rememberMe);
			addUserCookies(response, rememberMeKey);
		}
		return client;
	}

	protected void addUserCookies(HttpServletResponse resp, String key) {
		Cookie userCookie = new Cookie(OUR_COOKIE, key);
		userCookie.setMaxAge(2 * 7 * 24 * 60 * 60);// Two week
		userCookie.setPath("/");
		userCookie.setDomain(ServerConfiguration.getServerCookieDomain());
		resp.addCookie(userCookie);
	}

	private Client getClient(String emailId, String password) {
		if (emailId == null || password == null) {
			return null;
		}
		emailId = emailId.trim();
		password = HexUtil.bytesToHex(Security.makeHash(emailId
				+ password.trim()));

		Session session = HibernateUtil.getCurrentSession();
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
		}
		return null;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		LOG.info(request);
		// We check if the session is already there, if it is, we check if user
		// have to reset his password(by using a flag on the user object)
		HttpSession httpSession = request.getSession(false);
		if (httpSession == null) {
			dispatch(request, response, LOGIN_VIEW);
			return;
		}
		String activationType = (String) httpSession
				.getAttribute(ACTIVATION_TYPE);
		if (activationType != null && activationType.equals("resetpassword")) {
			httpSession.removeAttribute(ACTIVATION_TYPE);
			httpSession.removeAttribute(EMAIL_ID);
			redirectExternal(request, response, LOGIN_URL);
			return;
		}
		String emailID = (String) httpSession.getAttribute(EMAIL_ID);
		if (emailID == null) {
			// if session is not there then we show the form and user fills it
			// which gets submitted to same url
			String userCookie = getCookie(request, OUR_COOKIE);
			if (userCookie == null) {
				dispatch(request, response, LOGIN_VIEW);
				return;
			}

			Session session = HibernateUtil.openSession();
			Query query = session.getNamedQuery("get.remembermeKey");
			query.setParameter("key", userCookie);
			RememberMeKey rememberMeKey = (RememberMeKey) query.uniqueResult();
			
			if (rememberMeKey == null) {
				dispatch(request, response, LOGIN_VIEW);
				return;
			}
			Client client = getClient(rememberMeKey.getEmailID());
			if (client == null) {
				dispatch(request, response, LOGIN_VIEW);
				return;
			}
			httpSession.setAttribute(EMAIL_ID, rememberMeKey.getEmailID());

			Transaction transaction = session.beginTransaction();
			try {
				client.setLoginCount(client.getLoginCount() + 1);
				client.setLastLoginTime(System.currentTimeMillis());
				session.saveOrUpdate(client);
				transaction.commit();
			} catch (Exception e) {
				e.printStackTrace();
				transaction.rollback();
			}
			redirectExternal(request, response, COMPANIES_URL);

		} else {

			// Get the Client using the mail id
			Session session = HibernateUtil.openSession();
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
							return;
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
								.getNamedQuery("get.activation.by.emailid");
						query.setParameter(EMAIL_ID, emailID);
						Activation activation = (Activation) query
								.uniqueResult();
						Transaction transaction = session.beginTransaction();
						// reset the activation code and save it
						activation.setToken(token);
						try {
							saveEntry(activation);

							// resend activation mail
							sendActivationEmail(token, client);
							transaction.commit();
						} catch (Exception e) {
							e.printStackTrace();
							transaction.rollback();
						}

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
