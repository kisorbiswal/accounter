package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
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
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;

public class CompaniesServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	private static final String CREATE_SUCCESS = "Your company is created successfully";
	private static final String CREATE_FAIL = "Company creation failed";

	private static final String DELETE_SUCCESS = "Your company is deleted successfully.";
	private static final String DELETE_FAIL = "Company Deletion failed.";
	private static final String MIGRATION_VIEW = "/WEB-INF/companyMigration.jsp";

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

		checkForStatus(req);

		String companyID = req.getParameter(COMPANY_ID);

		if (companyID != null) {
			openCompany(req, resp, Long.parseLong(companyID));
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
				if (httpSession.getAttribute(COMPANY_CREATION_STATUS) == null) {
					req.setAttribute("message",
							"You don't have any companies now.");
				}
			} else {
				List<ServerCompany> list = new ArrayList<ServerCompany>();
				for (ServerCompany serverCompany : companies) {
					list.add(serverCompany);
				}
				Collections.sort(list, new Comparator<ServerCompany>() {

					@Override
					public int compare(ServerCompany company1,
							ServerCompany company2) {
						return company1.getCompanyName().compareTo(
								company2.getCompanyName());
					}

				});
				Set<ServerCompany> sortedCompanies = new HashSet<ServerCompany>();
				for (ServerCompany serverCompany : list) {
					sortedCompanies.add(serverCompany);
				}

				req.setAttribute(ATTR_COMPANY_LIST, list);
			}
			// addUserCookies(resp, client);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		dispatch(req, resp, companiedListView);
	}

	/**
	 * @param httpSession
	 * 
	 */
	private void checkForStatus(HttpServletRequest req) {
		HttpSession httpSession = req.getSession();

		// Checking CreateCompany Status
		String status = (String) httpSession
				.getAttribute(COMPANY_CREATION_STATUS);
		if (status != null) {
			if (status.equals("Success")) {
				req.setAttribute("message", CREATE_SUCCESS);
			} else {
				req.setAttribute("message", CREATE_FAIL);
			}
			httpSession.removeAttribute(COMPANY_CREATION_STATUS);
		}

		// Checking DeleteCompany Status
		String deleteStatus = (String) httpSession
				.getAttribute(COMPANY_DELETION_STATUS);
		if (deleteStatus != null) {
			if (deleteStatus.equals("Success")) {
				req.setAttribute("message", DELETE_SUCCESS);
			} else {
				req.setAttribute(
						"message",
						DELETE_FAIL
								+ " "
								+ httpSession
										.getAttribute("DeletionFailureMessage"));
			}
			httpSession.removeAttribute("DeletionFailureMessage");
			httpSession.removeAttribute(COMPANY_DELETION_STATUS);
		}

	}

	/**
	 * @throws IOException
	 * 
	 */
	private void openCompany(HttpServletRequest req, HttpServletResponse resp,
			long companyID) throws IOException {
		HttpSession httpSession = req.getSession();
		httpSession.setAttribute(COMPANY_ID, companyID);
		addCompanyCookies(resp, companyID);
		addMacAppCookie(req, resp);

		Session session = HibernateUtil.openSession(LOCAL_DATABASE);
		try {
			ServerCompany company = (ServerCompany) session.get(
					ServerCompany.class, companyID);
			if (company != null) {

				if (!company.isActive()) {
					dispatch(req, resp, MIGRATION_VIEW);
					return;
				}
				String url = ACCOUNTER_OLD_URL;
				if (ServerConfiguration.isDebugMode) {
					url = ACCOUNTER_URL;
				}

				redirectExternal(
						req,
						resp,
						buildCompanyServerURL(company.getServer().getAddress(),
								url));
			}
		} finally {
			session.close();
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	private void addCompanyCookies(HttpServletResponse resp, long companyID) {
		Cookie companyCookie = new Cookie(COMPANY_COOKIE,
				String.valueOf(companyID));
		companyCookie.setMaxAge(-1);// Two week
		companyCookie.setPath("/");
		companyCookie.setDomain(ServerConfiguration.getServerCookieDomain());
		resp.addCookie(companyCookie);
	}

	private void addMacAppCookie(HttpServletRequest request,
			HttpServletResponse response) {
		String header = request.getHeader("Nativeapp");
		boolean isNative = (header != null && !header.isEmpty());
		if (isNative) {
			Cookie macAppCookie = new Cookie("Nativeapp", "Mac App");
			macAppCookie.setPath("/");
			response.addCookie(macAppCookie);
		}
	}
}
