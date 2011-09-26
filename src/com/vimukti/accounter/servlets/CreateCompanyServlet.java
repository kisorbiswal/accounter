package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Server;
import com.vimukti.accounter.core.ServerCompany;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.UserPermissions;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.main.ServerAllocationFactory;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;
import com.vimukti.accounter.web.server.FinanceTool;

public class CreateCompanyServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String view = "/WEB-INF/CreateCompany.jsp";
	private static final String CREATING = "Creating Company Please wait...";

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control",
				"max-age=0,no-store, no-cache, must-revalidate");

		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");

		HttpSession session = request.getSession();
		String emailID = (String) session.getAttribute(EMAIL_ID);
		if (emailID == null) {
			redirectExternal(request, response, LOGIN_URL);
			return;
		}
		String status = (String) session.getAttribute(COMPANY_CREATION_STATUS);
		if (status != null) {
			request.setAttribute("message", CREATING);
			return;
		}
		doCreateCompany(request, response, emailID);
	}

	private void doCreateCompany(HttpServletRequest request,
			HttpServletResponse response, String emailID) throws IOException {
		String companyName = request.getParameter("name");
		String companyTypeStr = request.getParameter("companyType");
		if (!isValidCompanyName(companyName)) {
			request.setAttribute("errormessage",
					"Invalid Company Name. Name Should be grater than 5 characters");
			dispatch(request, response, view);
			return;
		}
		int companyType = getCompanyType(companyTypeStr);
		if (companyType < 0) {
			request.setAttribute("errormessage", "Invalid Company Type.");
			dispatch(request, response, view);
			return;
		}
		ServerCompany serverCompany = null;
		Client client = null;
		Session session = HibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			serverCompany = getServerCompany(companyName);
			if (serverCompany != null) {
				request.setAttribute("errormessage",
						"Company exist with the same name.");
				dispatch(request, response, view);
				return;
			}
			client = getClient(emailID);
			if (client == null) {
				request.setAttribute("errormessage",
						"Your session was expired. please login again.");
				dispatch(request, response, view);
				return;
			}

			serverCompany = new ServerCompany();
			serverCompany.getClients().add(client);
			serverCompany.setActive(true);
			serverCompany.setCompanyName(companyName);
			serverCompany.setCompanyType(companyType);
			serverCompany.setConfigured(false);
			serverCompany.setCreatedDate(new Date());
			serverCompany.setServer(getPreferredServer(companyType,
					companyName, request.getRemoteAddr()));
			session.saveOrUpdate(serverCompany);

			client.getCompanies().add(serverCompany);
			session.saveOrUpdate(client);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
		long serverId = serverCompany.getId();
		String serverCompanyName = serverCompany.getCompanyName();
		int serverCompanyType = serverCompany.getCompanyType();
		ClientUser user = getUser(client);
		final HttpSession httpSession = request.getSession(true);
		redirectExternal(request, response, COMPANIES_URL);
		try {
			createComapny(serverId, serverCompanyName, serverCompanyType, user);
			httpSession.setAttribute(COMPANY_CREATION_STATUS, "Success");
			updateServers(serverCompany.getServer(), true);
		} catch (Exception e) {
			e.printStackTrace();
			rollback(serverId, user.getID());
			httpSession.setAttribute(COMPANY_CREATION_STATUS, "Fail");
		}
		return;
	}

	private void createComapny(long companyID, String companyName,
			int companyType, ClientUser user) throws Exception {
		Company company = new Company(companyType);
		company.setId(companyID);
		company.setFullName(companyName);
		init(company, companyID, user);
	}

	private void init(Company company, long serverCompnayId,
			ClientUser clientUser) throws Exception {
		Session companySession = HibernateUtil.openSession();
		Transaction transaction = companySession.beginTransaction();
		try {
			// Creating User
			User user = new User(clientUser);
			user.setActive(true);
			companySession.save(user);

			AccounterThreadLocal.set(user);
			company.getUsers().add(user);
			company.setCompanyEmail(user.getEmail());

			companySession.save(company);

			FinanceTool.createView(company.getID());

			transaction.commit();
			UsersMailSendar.sendMailToDefaultUser(user, company.getFullName());

		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
			throw e;
		} finally {
			if (companySession.isOpen()) {
				companySession.close();
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	private Server getPreferredServer(int companyType, String companyName,
			String sourceAddr) {
		return ServerAllocationFactory.getServerAllocator().allocateServer(
				companyType, companyName, sourceAddr);
	}

	private ClientUser getUser(Client client) {
		User user = client.toUser();
		user.setFullName(user.getFirstName() + " " + user.getLastName());
		user.setUserRole(RolePermissions.ADMIN);
		user.setAdmin(true);
		user.setCanDoUserManagement(true);
		UserPermissions permissions = new UserPermissions();
		permissions.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		permissions.setTypeOfExpences(RolePermissions.TYPE_APPROVE);
		permissions.setTypeOfInvoices(RolePermissions.TYPE_YES);
		permissions.setTypeOfLockDates(RolePermissions.TYPE_YES);
		permissions.setTypeOfPublishReports(RolePermissions.TYPE_YES);
		permissions.setTypeOfSystemSettings(RolePermissions.TYPE_YES);
		permissions.setTypeOfViewReports(RolePermissions.TYPE_YES);
		user.setPermissions(permissions);
		return user.getClientUser();
	}

	private ServerCompany getServerCompany(String companyName) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getServerCompany.by.name")
				.setParameter("name", companyName);
		return (ServerCompany) query.uniqueResult();
	}

	private void rollback(long serverId, long clientId) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Object object = session.get(ServerCompany.class, serverId);
			Client client = (Client) session.get(Client.class, clientId);
			client.getCompanies().remove((ServerCompany) object);
			session.saveOrUpdate(client);
			session.delete(object);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	private int getCompanyType(String companyType) {
		if (companyType != null) {
			int type = Integer.parseInt(companyType);
			if (type == Company.ACCOUNTING_TYPE_UK
					|| type == Company.ACCOUNTING_TYPE_US
					|| type == Company.ACCOUNTING_TYPE_INDIA
					|| type == Company.ACCOUNTING_TYPE_OTHER) {
				return type;
			}
		}
		return -1;
	}

	private boolean isValidCompanyName(String companyId) {
		return companyId.trim().length() > 5;
		// return companyId.matches("^[A-Za-z0-9]{5,}$");
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String emailID = (String) session.getAttribute(EMAIL_ID);
		if (emailID == null) {
			redirectExternal(request, response, LOGIN_URL);
		}

		String status = (String) session.getAttribute(COMPANY_CREATION_STATUS);
		if (status != null) {
			request.setAttribute("message", CREATING);
			return;
		}
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control",
				"max-age=0,no-store, no-cache, must-revalidate");

		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		dispatch(request, response, view);
	}
}
