package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.User;
import com.vimukti.accounter.main.Server;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;

public class LiveLoginServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2062165649089031647L;

	protected static final Log logger = LogFactory
			.getLog(LiveLoginServlet.class);

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		User user = null;
		try {
			// Check if user is already logged in
			if (getCompanyName(request) != null) {
				response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
				response.setHeader("Location", "/accounter");
				return;
			}
			// Now try to login using cookies
			user = getUser(request);
			if (user == null) {
				redirectLogin(request, response);
			} else {

				// && !identity.getUser(identity.getID()).isLocked()) {
				logger.info("User Logged in : " + user.getEmailId());

				intializeUser(request, user);
				response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
				response.setHeader("Location", "/accounter");
			}
		} finally {
		}
	}

	/**
	 * Try to login using cookies
	 * 
	 * @param request
	 * @return
	 */
	private User getUser(HttpServletRequest request) {
		String cookie = getCookie(request, OUR_COOKIE);
		String[] params = cookie.split(",");
		if (params == null || params.length != 3) {
			return null;
		}
		String id = params[0];
		String password = params[1];
		String companyName = params[2];
		// Log.info("CheckCookiesLogin Method Called");
		User user = null;
		Session session = null;
		if (isCompanyExits(companyName))
			session = HibernateUtil.openSession(companyName);
		else
			return null;
		if (id != null && password != null) {

			user = (User) session
					.getNamedQuery("getIDentity.from.id.and.password")
					.setParameter("id", id)
					.setParameter("password", password).uniqueResult();
			if (user == null)
				return null;
			return user;
		}
		return null;
	}

	private void redirectLogin(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher("/sites/login.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Session session = null;
		try {
			User user = doLogin(request, response);
			if (user != null) {
				intializeUser(request, user);

				response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
				response.setHeader("Location", "/accounter");
				return;
			} else {
				request.setAttribute(
						"message",
						"The details that you have are incorrect. If you have forgotten your details, please refer to your invitation or contact the person who invited you to Accounter.");
				request.setAttribute("emailId", request.getParameter("emailId"));
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher("/sites/login.jsp");
				dispatcher.forward(request, response);
			}
		} finally {
			if (session != null)
				session.close();
		}
	}

	private void intializeUser(HttpServletRequest request, User user) {
		try {
			// user.incrementLoginCountAndStatus();
			request.getSession().setAttribute(USER_ID,
					String.valueOf(user.getID()));
			request.getSession().setAttribute(COMPANY_NAME, user.getCompany());
			Server.getInstance().addSeesionIdOfIdentity(
					String.valueOf(user.getID()), request.getSession().getID());

			request.setAttribute("success", true);

			// identity.sendMyStatusToUsers(true);

		} finally {
		}
	}

	private User doLogin(HttpServletRequest request,
			HttpServletResponse response) {
		String emailId = request.getParameter("emailId").trim();
		String password = request.getParameter("password").trim();
		String companyName = request.getParameter(COMPANY_NAME).trim();

		if (emailId != null && password != null && !password.isEmpty()) {
			password = HexUtil
					.bytesToHex(Security.makeHash(emailId + password));
			User user = getUser(emailId, password, companyName);
			if (user != null && request.getParameter("staySignIn") != null
					&& request.getParameter("staySignIn").equals("on")) {
				setCookies(response, user);
			}
			return user;
		}

		return null;
	}

	private User getUser(String emailId, String password, String companyName) {

		Session session = HibernateUtil.openSession(companyName);
		try {
			User user = null;
			Query query = session
					.getNamedQuery("getIDentity.from.emailid.and.password");
			query.setParameter("emailid", emailId);
			query.setParameter("password", password);
			user = (User) query.uniqueResult();
			return user;
		} finally {
			session.close();
		}
	}

	private void setCookies(HttpServletResponse response, User user) {
		Cookie cookie = new Cookie("_accounter_01_infinity_2711",
				new StringBuffer(user.getEmailId()).append(",")
						.append(user.getPasswordSha1Hash()).append(",")
						.append(user.getCompany()).toString());
		cookie.setMaxAge(2 * 7 * 24 * 60 * 60);// Two week
		cookie.setPath("/");
		// this.getThreadLocalResponse().addCookie(cookie);
		response.addCookie(cookie);
	}

}
