package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

	private String companiedListView = "/WEB_INF/companylist.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession httpSession = req.getSession();
		if (httpSession == null) {
			req.setAttribute(ATTR_MESSAGE, "Session Expired.");
			redirectExternal(req, resp, LOGIN_URL);
			return;
		}

		String userID = (String) httpSession.getAttribute(USER_ID);

		if (userID == null) {
			req.setAttribute(ATTR_MESSAGE, "Invalid User.");
			redirectExternal(req, resp, LOGIN_URL);
			return;
		}

		Session session = HibernateUtil.openSession(LOCAL_DATABASE);
		try {
			Client client = getClient(userID);
			if (client == null) {
				req.setAttribute(ATTR_MESSAGE, "Invalid User.");
				redirectExternal(req, resp, LOGIN_URL);
				return;
			}

			Set<ServerCompany> companies = client.getCompanies();
			if (companies.size() <= 1) {
				ServerCompany company = null;
				if (companies.isEmpty()) {
					company = new ServerCompany();
					company = (ServerCompany) session.save(company);
				} else {
					company = companies.iterator().next();
				}
				setCookies(resp, client, company.getCompanyName());
				redirectExternal(req, resp, ACCOUNTER_URL);
			} else {
				addUserCookies(resp, client);
				List<String> companyList = new ArrayList<String>();
				for (ServerCompany company : companies) {
					companyList.add(company.getCompanyName());
				}
				req.setAttribute(ATTR_COMPANY_LIST, companyList);
				dispatch(req, resp, companiedListView);
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String companyName = req.getParameter(COMPANY_NAME);

		if (companyName == null) {
			// TODO
		}

		addCompanyCookies(resp, companyName);

		redirectExternal(req, resp, ACCOUNTER_URL);
	}

	private void createCookiesWithComapnyID(long companyID) {

	}

	private void setCookies(HttpServletResponse response, Client client,
			String companyName) {
		addCompanyCookies(response, companyName);
		addUserCookies(response, client);
	}

	private void addCompanyCookies(HttpServletResponse resp, String companyName) {
		Cookie companyCookie = new Cookie(COMPANY_COOKIE, companyName);
		companyCookie.setMaxAge(2 * 7 * 24 * 60 * 60);// Two week
		companyCookie.setPath("/");
		resp.addCookie(companyCookie);
	}

	private void addUserCookies(HttpServletResponse resp, Client client) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(client.getEmailId());
		buffer.append(",");
		buffer.append(client.getPassword());
		buffer.append("");
		Cookie userCookie = new Cookie(USER_COOKIE, buffer.toString());
		userCookie.setMaxAge(2 * 7 * 24 * 60 * 60);// Two week
		userCookie.setPath("/");
		resp.addCookie(userCookie);
	}

}
