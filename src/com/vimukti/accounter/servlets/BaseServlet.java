package com.vimukti.accounter.servlets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

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

	protected boolean isValidInputs(String... inputs) {
		// TODO Auto-generated method stub
		return false;
	}

	protected void redirect(HttpServletRequest req, HttpServletResponse resp,
			String page) {
		// TODO Auto-generated method stub

	}

	protected void dispatchMessage(String string, HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub

	}
}
