package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.server.FinanceTool;

public class BaseServlet extends HttpServlet {
	public static final String USER_ID = "userID";
	public static final String OUR_COOKIE = "_accounter_01_infinity_2711";

	public static final String COMPANY_NAME = "companyName";

	public static final String EMAIL_ID = "emailId";
	public static final String PASSWORD = "password";

	/**
	 * 
	 */
	protected static final long serialVersionUID = 1L;
	protected static final String LOCAL_DATABASE = "accounter";
	public static final int MAIL_ID = 0;
	public static final int NAME = 1;
	public static final int PHONE_NO = 2;
	

	protected String getCompanyName(HttpServletRequest req) {
		return null;

	}

	protected boolean isCompanyExits(String companyName) {
		if (companyName == null) {
			return false;
		}
		Session openSession = HibernateUtil.openSession(LOCAL_DATABASE);
		try {
			Company uniqueResult = (Company) openSession
					.getNamedQuery("getCompanyName.is.unique")
					.setParameter("companyName", companyName).uniqueResult();
			return uniqueResult == null ? false : true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			openSession.close();
		}
	}

	protected FinanceTool getFinanceTool(HttpServletRequest request) {
		return null;
	}

	protected String getCookie(HttpServletRequest request, String ourCookie) {
		Cookie[] clientCookies = request.getCookies();
		if (clientCookies != null) {
			for (Cookie cookie : clientCookies) {
				if (cookie.getName().equals(ourCookie)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	protected boolean isValidInputs(int inputType, String... values) {
		for (String value : values) {
			switch (inputType) {
			case MAIL_ID:
				return value
						.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}$");

			case NAME:
				return value.matches("^[a-zA-Z]+$");
				
			case PHONE_NO:
				return value.matches("^[0-9][0-9-]+$");

			}
		}
		return false;
	}

	protected void redirect(HttpServletRequest req, HttpServletResponse resp,
			String page) {
		try {
			resp.sendRedirect(page);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void dispatchMessage(String message, HttpServletRequest req,
			HttpServletResponse resp, String page) {
		req.setAttribute("message", message);
		try {
			req.getRequestDispatcher(page).forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void saveEntry(Object object) {
		Session currentSession = HibernateUtil.getCurrentSession();
		currentSession.save(object);
	}

	protected Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Client client = (Client) session.getNamedQuery("getClient.by.mailId")
				.setString(EMAIL_ID, emailId).uniqueResult();
		session.close();
		return client;
	}

}
