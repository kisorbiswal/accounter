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

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.ServerCompany;
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
		long companyID = 0;
		if (companies.isEmpty()) {
			companyID = createNewCompany(client);
		} else {
			companyID = companies.iterator().next().getID();
		}
		setCookies(resp, client, companyID);
		redirectExternal(req, resp, ACCOUNTER_URL);
	}

	private long createNewCompany(Client client) {
		ServerCompany serverCompany = new ServerCompany();
		String companyName = client.getEmailId().replaceAll("@", "_")
				.replace(".com", "").replaceAll("\\.", "_");
		serverCompany.setCompanyName(companyName);
		serverCompany.setConfigured(true);
		Session session = HibernateUtil.getCurrentSession();

		Transaction transaction = session.beginTransaction();

		session.save(serverCompany);
		client.getCompanies().add(serverCompany);
		session.save(serverCompany);
		session.saveOrUpdate(client);
		Query query = session.createSQLQuery("CREATE SCHEMA "
				+ serverCompany.getCompanyName());
		query.executeUpdate();
		try {
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}

		Session companySession = HibernateUtil.openSession(
				serverCompany.getCompanyName(), true);
		Transaction companyTrans = companySession.beginTransaction();
		Company company = new Company();
		company.setCompanyID(serverCompany.getCompanyName());
		company.setCompanyEmail(client.getEmailId());
		company.setFullName(serverCompany.getCompanyName());
		company.setTradingAddress(new Address());
		company.setRegisteredAddress(new Address());
		companySession.save(company);
		try {
			companyTrans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			companyTrans.rollback();
		}
		return serverCompany.getID();
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
