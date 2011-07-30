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
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ServerCompany;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.utils.HibernateUtil;

public class CompaniesServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	private String companiedListView = "/WEB_INF/companylist.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession httpSession = req.getSession();
		String emailID = (String) httpSession.getAttribute(EMAIL_ID);
		if (emailID == null) {
			req.setAttribute(ATTR_MESSAGE, "Session Expired.");
			redirectExternal(req, resp, LOGIN_URL);
			return;
		}
		Session session = HibernateUtil.openSession(LOCAL_DATABASE);
		try {
			Client client = getClient(emailID);
			if (client == null) {
				req.setAttribute(ATTR_MESSAGE, "Invalid User.");
				redirectExternal(req, resp, LOGIN_URL);
				return;
			}

			Set<ServerCompany> companies = client.getCompanies();
			if (companies.size() <= 1) {
				redirectToAccounter(req, resp, companies, client);
			} else {
				addUserCookies(resp, client);
				List<String> companyList = new ArrayList<String>();
				for (ServerCompany company : companies) {
					companyList.add(company.getCompanyName());
				}
				req.setAttribute(ATTR_COMPANY_LIST, companyList);
				User user = getUserByEmail(client.getEmailId());
				req.getSession().setAttribute(USER_ID, user.getID());
				dispatch(req, resp, companiedListView);
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void redirectToAccounter(HttpServletRequest req,
			HttpServletResponse resp, Set<ServerCompany> companies,
			Client client) throws IOException {
		Session session = HibernateUtil.getCurrentSession();
		ServerCompany company = null;
		User user = null;
		if (companies.isEmpty()) {
			Transaction transaction = session.beginTransaction();

			company = new ServerCompany();
			session.save(company);
			user = client.toUser();
			session.save(user);
			client.getCompanies().add(company);
			session.save(company);
			try {
				transaction.commit();
			} catch (Exception e) {
				e.printStackTrace();
				transaction.rollback();
			}
		} else {
			company = companies.iterator().next();
			user = getUserByEmail(client.getEmailId());
		}
		setCookies(resp, client, company.getID());
		req.getSession().setAttribute(USER_ID, user.getID());
		redirectExternal(req, resp, ACCOUNTER_URL);
	}

	/**
	 * @param emailId
	 * @return
	 */
	private User getUserByEmail(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		User user = (User) session.getNamedQuery("getuser.by.email")
				.uniqueResult();
		return user;
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String companyID = req.getParameter(COMPANY_ID);

		if (companyID == null) {
			// TODO
		}

		addCompanyCookies(resp, Long.parseLong(companyID));

		redirectExternal(req, resp, ACCOUNTER_URL);
	}

	private void setCookies(HttpServletResponse response, Client client,
			long companyID) {
		addCompanyCookies(response, companyID);
		addUserCookies(response, client);
	}

	private void addCompanyCookies(HttpServletResponse resp, long companyID) {
		Cookie companyCookie = new Cookie(COMPANY_COOKIE,
				String.valueOf(companyID));
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
