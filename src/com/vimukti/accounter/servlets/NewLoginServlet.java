package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;

public class NewLoginServlet extends BaseServlet {
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Client client = doLogin(request, response);
			if (client != null) {
				intializeClient(request, client);

				response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
				response.setHeader("Location", "/WEB-INF/companysetup");
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

	private void intializeClient(HttpServletRequest request, Client client) {
		// TODO Auto-generated method stub

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
		redirect(request, response, "/WEB-INF/login.jsp");
	}

}
