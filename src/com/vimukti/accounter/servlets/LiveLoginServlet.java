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
				logger.info("User Logged in : " + user.getEmail());

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
		if (cookie == null) {
			return null;
		}
		String[] params = cookie.split(",");
		if (params == null || params.length != 3) {
			return null;
		}
		String id = params[0];
		String password = params[1];
		String companyName = params[2];

		return getUser(id, password, companyName);

	}

	private void redirectLogin(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher("/WEB-INF/login.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
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
						.getRequestDispatcher("/WEB-INF/login.jsp");
				dispatcher.forward(request, response);
			}
		} finally {
		}
	}

	private void intializeUser(HttpServletRequest request, User user) {
		try {
			// user.incrementLoginCountAndStatus();
			request.getSession().setAttribute(USER_ID,
					String.valueOf(user.getID()));
			request.getSession().setAttribute("userName",
					String.valueOf(user.getName()));
			request.getSession().setAttribute(COMPANY_NAME, user.getCompany());
			Server.addSeesionIdOfIdentity(String.valueOf(user.getID()), request
					.getSession().getId());

			request.setAttribute("success", true);

		} finally {
		}
	}

	private User doLogin(HttpServletRequest request,
			HttpServletResponse response) {
		String emailId = request.getParameter(EMAIL_ID);
		String password = request.getParameter(PASSWORD);
		String companyName = request.getParameter(COMPANY_NAME);

		User user = getUser(emailId, password, companyName);
		if (user != null && request.getParameter("staySignIn") != null
				&& request.getParameter("staySignIn").equals("on")) {
			setCookies(response, user);
		}
		return user;
	}

	private User getUser(String emailId, String password, String companyName) {
		if (emailId == null || password == null || companyName == null) {
			return null;
		}
		emailId = emailId.trim();
		password = HexUtil.bytesToHex(Security.makeHash(emailId
				+ password.trim()));
		companyName = companyName.trim();
		Session session = HibernateUtil.openSession(companyName);
		try {
			User user = null;
			Query query = session
					.getNamedQuery("getuser.from.emailid.and.password");
			query.setParameter("emailid", emailId);
			query.setParameter("password", password);
			user = (User) query.uniqueResult();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	private void setCookies(HttpServletResponse response, User user) {
		Cookie cookie = new Cookie(OUR_COOKIE,
				new StringBuffer(user.getEmail()).append(",")
						.append(user.getPasswordSha1Hash()).append(",")
						.append(user.getCompany()).toString());
		cookie.setMaxAge(2 * 7 * 24 * 60 * 60);// Two week
		cookie.setPath("/");
		// this.getThreadLocalResponse().addCookie(cookie);
		response.addCookie(cookie);
	}

}
