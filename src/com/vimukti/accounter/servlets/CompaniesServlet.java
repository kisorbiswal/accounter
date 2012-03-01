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
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.core.SupportedUser;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;

public class CompaniesServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	private static final String CREATE_SUCCESS = "Your company is created successfully";
	private static final String CREATE_FAIL = "Company creation failed";

	private static final String DELETE_SUCCESS = "Your company is deleted successfully.";
	private static final String DELETE_FAIL = "Company Deletion failed.";
	private static final String ACCOUNT_DELETE_FAIL = "Account Deletion failed.";
	private static final String MIGRATION_VIEW = "/WEB-INF/companyMigration.jsp";

	private String companiedListView = "/WEB-INF/companylist.jsp";

	@SuppressWarnings("unchecked")
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
		String create = req.getParameter(CREATE);
		if (create != null && create.equals("true")) {
			createCompany(req, resp);
			return;
		}

		Session session = HibernateUtil.getCurrentSession();
		try {
			Client client = getClient(emailID);
			if (client == null) {
				redirectExternal(req, resp, LOGIN_URL);
				return;
			}
			boolean isSupportedUser = isSupportUser(emailID);
			httpSession.setAttribute(IS_SUPPORTED_USER, isSupportedUser);
			Set<User> users = client.getUsers();
			List<Company> list = new ArrayList<Company>();
			if (isSupportedUser) {
				// get.CompanyId.Tradingname.and.Country.of.supportUser
				List<Object[]> objects = session.getNamedQuery(
						"get.CompanyId.Tradingname.and.Country.of.supportUser")
						.list();
				addCompanies(list, objects);
				req.getSession().setAttribute(SUPPORTED_EMAIL_ID, emailID);
			} else {
				List<Long> userIds = new ArrayList<Long>();
				for (User user : users) {
					if (!user.isDeleted()) {
						userIds.add(user.getID());
					}
				}
				List<Object[]> objects = new ArrayList<Object[]>();
				if (!userIds.isEmpty()) {
					objects = session
							.getNamedQuery(
									"get.CompanyId.Tradingname.and.Country.of.user")
							.setParameterList("userIds", userIds).list();
					addCompanies(list, objects);
				}

				if (!client.getClientSubscription().getSubscription()
						.isPaidUser()) {
					if (list.size() == 0) {
						createCompany(req, resp);
						return;
					}
					if (list.size() == 1) {
						openCompany(req, resp, list.get(0).getId());
						return;
					}
					req.setAttribute("isPaid", false);
				}
				req.setAttribute("isPaid", true);

				if (list.isEmpty()
						&& httpSession.getAttribute(COMPANY_CREATION_STATUS) == null) {
					req.setAttribute("message",
							"You don't have any companies now.");
				}
			}
			req.setAttribute(ATTR_COMPANY_LIST, list);
			req.getSession().removeAttribute(COMPANY_ID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String parameter = req.getParameter("message");
		if (parameter != null && parameter.equals("locked")) {
			req.setAttribute("message", "Your Company has been locked.");
		}
		dispatch(req, resp, companiedListView);
	}

	private void addCompanies(List<Company> list, List<Object[]> objects) {
		for (Object[] obj : objects) {
			Company com = new Company();
			com.setId((Long) obj[0]);
			com.getPreferences().setTradingName((String) obj[1]);
			com.getRegisteredAddress().setCountryOrRegion((String) obj[2]);

			list.add(com);
		}
	}

	private boolean isSupportUser(String emailId) {
		Session currentSession = HibernateUtil.getCurrentSession();
		Object load = currentSession.get(SupportedUser.class, emailId);
		return load != null;
	}

	private void createCompany(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		addMacAppCookie(req, resp);

		String url = ACCOUNTER_OLD_URL;
		if (ServerConfiguration.isDebugMode) {
			url = ACCOUNTER_URL;
		}

		HttpSession httpSession = req.getSession();
		httpSession.setAttribute(CREATE, "true");
		httpSession.removeAttribute(COMPANY_ID);
		req.getSession()
				.setAttribute(IS_TOUCH, "" + req.getParameter(IS_TOUCH));
		redirectExternal(req, resp, url);
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
				Object failureMessage = httpSession
						.getAttribute("DeletionFailureMessage");
				if (failureMessage != null) {
					req.setAttribute("message", DELETE_FAIL + " "
							+ failureMessage);
				} else {
					req.setAttribute("message", DELETE_FAIL);
				}
			}
			httpSession.removeAttribute("DeletionFailureMessage");
			httpSession.removeAttribute(COMPANY_DELETION_STATUS);
		}

		String accountDeleteStatus = (String) httpSession
				.getAttribute(ACCOUNT_DELETION_STATUS);
		if (accountDeleteStatus != null) {
			req.setAttribute("message", ACCOUNT_DELETE_FAIL);
			httpSession.removeAttribute("DeletionFailureMessage");
			httpSession.removeAttribute(ACCOUNT_DELETION_STATUS);
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
		httpSession.setAttribute(IS_TOUCH, req.getParameter(IS_TOUCH));
		addMacAppCookie(req, resp);

		Session session = HibernateUtil.getCurrentSession();

		Company company = (Company) session.get(Company.class, companyID);
		if (company != null) {

			// if (!company.isActive()) {
			// dispatch(req, resp, MIGRATION_VIEW);
			// return;
			// }
			String url = ACCOUNTER_OLD_URL;
			if (ServerConfiguration.isDebugMode) {
				url = ACCOUNTER_URL;
			}

			redirectExternal(req, resp, url);
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	// private void addCompanyCookies(HttpServletResponse resp, long companyID)
	// {
	// Cookie companyCookie = new Cookie(COMPANY_COOKIE,
	// String.valueOf(companyID));
	// companyCookie.setMaxAge(-1);// Two week
	// companyCookie.setPath("/");
	// companyCookie.setDomain(ServerConfiguration.getServerCookieDomain());
	// resp.addCookie(companyCookie);
	// }

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
