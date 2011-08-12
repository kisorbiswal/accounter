package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ServerCompany;
import com.vimukti.accounter.utils.HibernateUtil;

public class CompaniesServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	private static final String SUCCESS = "Your company is created successfully";
	private static final String FAIL = "Company creation failed";

	private String companiedListView = "/WEB-INF/companylist.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession httpSession = req.getSession();
		String emailID = (String) httpSession.getAttribute(EMAIL_ID);
		if (emailID == null) {
			redirectExternal(req, resp, LOGIN_URL);
			return;
		}
		String status = (String) httpSession
				.getAttribute(COMPANY_CREATION_STATUS);
		if (status != null) {
			if (status.equals("Success")) {
				req.setAttribute("message", SUCCESS);
			} else {
				req.setAttribute("message", FAIL);
			}
			httpSession.removeAttribute(COMPANY_CREATION_STATUS);
		}
		String companyID = req.getParameter(COMPANY_ID);

		if (companyID != null) {
			addCompanyCookies(resp, Long.parseLong(companyID));
			redirectExternal(req, resp, ACCOUNTER_URL);
			return;
		}

		Session session = HibernateUtil.openSession(LOCAL_DATABASE);
		try {
			Client client = getClient(emailID);
			if (client == null) {
				redirectExternal(req, resp, LOGIN_URL);
				return;
			}

			Set<ServerCompany> companies = client.getCompanies();
			if (companies.isEmpty()) {
				req.setAttribute("message", "You Don't Have any Companies Now.");
			} else {
				req.setAttribute(ATTR_COMPANY_LIST, companies);
			}
			addUserCookies(resp, client);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		dispatch(req, resp, companiedListView);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	private void addCompanyCookies(HttpServletResponse resp, long companyID) {
		Cookie companyCookie = new Cookie(COMPANY_COOKIE,
				String.valueOf(companyID));
		companyCookie.setMaxAge(2 * 7 * 24 * 60 * 60);// Two week
		companyCookie.setPath("/");
		resp.addCookie(companyCookie);
	}

}
