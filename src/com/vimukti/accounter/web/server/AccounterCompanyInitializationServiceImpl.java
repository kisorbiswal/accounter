/**
 * 
 */
package com.vimukti.accounter.web.server;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.ServerConvertUtil;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.UserPermissions;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.servlets.BaseServlet;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.IAccounterCompanyInitializationService;
import com.vimukti.accounter.web.client.core.AccountsTemplate;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.TemplateAccount;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

/**
 * @author Prasanna Kumar G
 * 
 */
public class AccounterCompanyInitializationServiceImpl extends
		RemoteServiceServlet implements IAccounterCompanyInitializationService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {

			if (isValidSession(request)) {
				Session session = HibernateUtil.openSession();
				try {
					if (CheckUserExistanceAndsetAccounterThreadLocal(request)) {
						super.service(request, response);
						Long serverCompanyID = (Long) request.getSession()
								.getAttribute(BaseServlet.COMPANY_ID);
						if (serverCompanyID != null) {
							new FinanceTool()
									.putChangesInCometStream(serverCompanyID);
						}
					} else {
						response.sendError(HttpServletResponse.SC_FORBIDDEN,
								"Could Not Complete the Request!");
					}
				} finally {
					session.close();
				}
			} else {
				response.sendError(HttpServletResponse.SC_FORBIDDEN,
						"Could Not Complete the Request!");
			}
		} catch (Exception e) {

			e.printStackTrace();

			// log.error("Failed to Process Request", e);

			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Could Not Complete the Request!");

		}
	}

	@Override
	public boolean initalizeCompany(ClientCompanyPreferences preferences,
			List<TemplateAccount> accounts) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {
			Client client = getClient(getUserEmail());

			Company company = new Company();
			company.setConfigured(false);
			company.setCreatedDate(new Date());

			User user = new User(getUser(client));
			user.setActive(true);
			user.setClient(client);
			user.setCompany(company);
			session.save(user);

			client.getUsers().add(user);
			session.saveOrUpdate(client);

			AccounterThreadLocal.set(user);
			company.getUsers().add(user);
			company.setCompanyEmail(user.getClient().getEmailId());

			// Updating CompanyPreferences
			CompanyPreferences serverCompanyPreferences = company
					.getPreferences();
			serverCompanyPreferences = new ServerConvertUtil().toServerObject(
					serverCompanyPreferences, preferences, session);
			company.setPreferences(serverCompanyPreferences);

			company.setRegisteredAddress(serverCompanyPreferences
					.getTradingAddress());
			// Initializing Accounts
			company.initialize(accounts);
			company.setConfigured(true);
			session.saveOrUpdate(company);

			transaction.commit();

			getThreadLocalRequest().getSession().setAttribute(
					BaseServlet.COMPANY_ID, company.getId());
			getThreadLocalRequest().getSession().removeAttribute(
					BaseServlet.CREATE);

			UsersMailSendar.sendMailToDefaultUser(user,
					company.getTradingName());
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
			throw new AccounterException(AccounterException.ERROR_INTERNAL);
		}
		return true;
	}

	@Override
	public ClientCompany getCompany() throws AccounterException {
		Long companyID = (Long) getThreadLocalRequest().getSession()
				.getAttribute(BaseServlet.COMPANY_ID);
		if (companyID == null) {
			return null;
		}
		FinanceTool tool = new FinanceTool();
		return tool.getCompanyManager().getClientCompany(getUserEmail(),
				companyID);
	}

	protected String getUserEmail() {
		return (String) getThreadLocalRequest().getSession().getAttribute(
				BaseServlet.EMAIL_ID);
	}

	protected Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter(BaseServlet.EMAIL_ID, emailId);
		Client client = (Client) namedQuery.uniqueResult();
		// session.close();
		return client;
	}

	private ClientUser getUser(Client client) {
		User user = client.toUser();
		// user.setFullName(user.getFirstName() + " " + user.getLastName());
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

	public boolean isValidSession(HttpServletRequest request) {
		return request.getSession().getAttribute(BaseServlet.EMAIL_ID) == null ? false
				: true;
	}

	private boolean CheckUserExistanceAndsetAccounterThreadLocal(
			HttpServletRequest request) {
		Session session = HibernateUtil.getCurrentSession();
		Long serverCompanyID = (Long) request.getSession().getAttribute(
				BaseServlet.COMPANY_ID);
		if (serverCompanyID == null) {
			String create = (String) request.getSession().getAttribute(
					BaseServlet.CREATE);
			if (create != null && create.equals("true")) {
				return true;
			}
			return false;
		}
		Company company = (Company) session.get(Company.class, serverCompanyID);
		if (company == null) {
			return false;
		}
		String userEmail = (String) request.getSession().getAttribute(
				BaseServlet.EMAIL_ID);
		User user = company.getUserByUserEmail(userEmail);
		if (user == null) {
			return false;
		}
		AccounterThreadLocal.set(user);
		return true;
	}

	@Override
	public List<AccountsTemplate> getAccountsTemplate()
			throws AccounterException {
		AccountsTemplateManager manager = new AccountsTemplateManager();
		try {
			return manager.loadAccounts(ServerLocal.get().getLanguage());
		} catch (Exception e) {
			throw new AccounterException(e);
		}
	}
}
