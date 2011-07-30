package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;

public class NewLoginServlet extends BaseServlet {

	private static final String LOGIN_VIEW = "/WEB-INF/login.jsp";

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Client client = doLogin(request, response);
			if (client != null) {
				// if valid credentials are there we redirect to <dest> param or
				// /companies
				String destUrl = request.getParameter(DESTINATION);
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
				request.setAttribute("emailId", request.getParameter("emailId"));
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher("/WEB-INF/companysetup");
				dispatcher.forward(request, response);
			}
		} finally {
		}
	}

	private Client doLogin(HttpServletRequest request,
			HttpServletResponse response) {
		String emailId = request.getParameter(EMAIL_ID);
		String password = request.getParameter(PASSWORD);

		Client client = getClient(emailId, password);
		if (client != null && request.getParameter("staySignIn") != null
				&& request.getParameter("staySignIn").equals("on")) {
			setCookies(response, client);
		}
		return client;
	}

	private void setCookies(HttpServletResponse response, Client client) {
		Cookie cookie = new Cookie(OUR_COOKIE, new StringBuffer(
				client.getEmailId()).append(",").append(client.getPassword())
				.append(",").toString());
		cookie.setMaxAge(2 * 7 * 24 * 60 * 60);// Two week
		cookie.setPath("/");
		// this.getThreadLocalResponse().addCookie(cookie);
		response.addCookie(cookie);
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
			query.setParameter("emailid", emailId);
			query.setParameter("password", password);
			client = (Client) query.uniqueResult();
			return client;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// We check if the session is already there, if it is, we check if user
		// have to reset his password(by using a flag on the user object)
		HttpSession httpSession = request.getSession();
		if (httpSession != null) {
			// Getting the mail id of the user from the session
			String emailId = (String) httpSession.getAttribute(EMAIL_ID);

			// Get the Client using the mail id
			Session session = HibernateUtil.openSession(LOCAL_DATABASE);
			try {
				Query query = session.getNamedQuery("getClient.by.mailId");
				query.setParameter("emailId", emailId);

				Client client = (Client) query.uniqueResult();
				if (client != null) {
					if (!client.isActive()) {
						// If session is there and he has to reset the password
						// then do an external redirect to /resetpassword url
						redirectExternal(request, response, RESET_PASSWORD_URL);
					} else {
						// if session is there and no need to reset password
						// then do external redirect to <dest> param or
						// /companies
						String destUrl = request.getParameter(DESTINATION);
						if (destUrl == null || destUrl.isEmpty()) {
							redirectExternal(request, response, COMPANIES_URL);
						} else {
							redirectExternal(request, response, destUrl);
						}

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (session != null)
					session.close();
			}

		} else {
			// if session is not there then we show the form and user fills it
			// which gets submitted to same url
			dispatch(request, response, LOGIN_VIEW);

		}

	}

}
