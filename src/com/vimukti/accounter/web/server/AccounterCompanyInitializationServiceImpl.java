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

import net.zschech.gwt.comet.server.CometServlet;
import net.zschech.gwt.comet.server.CometSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.Currency;
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
		try {
			Client client = getClient(getUserEmail());
			Company company = intializeCompany(preferences, accounts, client);
			getThreadLocalRequest().getSession().setAttribute(
					BaseServlet.COMPANY_ID, company.getId());
			getThreadLocalRequest().getSession().removeAttribute(
					BaseServlet.CREATE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterException(AccounterException.ERROR_INTERNAL);
		}
		return true;
	}

	public static Company intializeCompany(
			ClientCompanyPreferences preferences,
			List<TemplateAccount> accounts, Client client) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {
			Company company = new Company();
			company.setTradingName(preferences.getTradingName());
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
			// EU.initEncryption(company, client.getPassword());
			company.getUsers().add(user);
			company.setCompanyEmail(user.getClient().getEmailId());

			company.setConfigured(true);

			CompanyPreferences serverCompanyPreferences = company
					.getPreferences();

			serverCompanyPreferences = new ServerConvertUtil().toServerObject(
					serverCompanyPreferences, preferences, session);

			Currency primaryCurrency = serverCompanyPreferences
					.getPrimaryCurrency();
			primaryCurrency.setCompany(company);
			session.save(primaryCurrency);

			session.saveOrUpdate(company);

			// Updating CompanyPreferences

			company.getRegisteredAddress().setCountryOrRegion(
					client.getCountry());
			company.setRegisteredAddress(serverCompanyPreferences
					.getTradingAddress());
			// Initializing Accounts
			company.setPreferences(serverCompanyPreferences);
			company.initialize(accounts);

			session.saveOrUpdate(company);
			transaction.commit();

			UsersMailSendar.sendMailToDefaultUser(user,
					company.getTradingName());
			return company;
		} catch (AccounterException e) {
			e.printStackTrace();
			transaction.rollback();
		}
		return null;
	}

	@Override
	public ClientCompany getCompany() throws AccounterException {
		Long companyID = (Long) getThreadLocalRequest().getSession()
				.getAttribute(BaseServlet.COMPANY_ID);
		if (companyID == null) {
			return null;
		}

		FinanceTool tool = new FinanceTool();
		ClientCompany clientCompany = tool.getCompanyManager()
				.getClientCompany(getUserEmail(), companyID);

		CometSession cometSession = CometServlet
				.getCometSession(getThreadLocalRequest().getSession());
		CometManager.initStream(getThreadLocalRequest().getSession().getId(),
				companyID, clientCompany.getLoggedInUser().getEmail(),
				cometSession);

		return clientCompany;
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

	public ClientUser getUser() {
		return getUser(getClient(getUserEmail()));
	}

	private static ClientUser getUser(Client client) {
		User user = client.toUser();
		// user.setFullName(user.getFirstName() + " " + user.getLastName());
		user.setUserRole(RolePermissions.ADMIN);
		user.setAdmin(true);
		user.setCanDoUserManagement(true);
		UserPermissions permissions = new UserPermissions();
		permissions.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		permissions.setTypeOfPayBillsPayments(RolePermissions.TYPE_YES);
		permissions.setTypeOfInvoicesBills(RolePermissions.TYPE_YES);
		permissions.setTypeOfManageAccounts(RolePermissions.TYPE_YES);
		permissions.setTypeOfCompanySettingsLockDates(RolePermissions.TYPE_YES);
		permissions.setTypeOfViewReports(RolePermissions.TYPE_YES);
		permissions.setTypeOfInventoryWarehouse(RolePermissions.TYPE_YES);
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
			return manager.loadAccounts(ServerLocal.get());
		} catch (Exception e) {
			throw new AccounterException(e);
		}
	}

	@Override
	public String getCountry() {
		Client client = getClient(getUserEmail());
		return client.getCountry();
	}

	@Override
	public boolean isCompanyNameExists(String companyName)
			throws AccounterException {
		if (companyName == null) {
			return true;
		}
		companyName = companyName.trim().toLowerCase();
		Session hibernateSession = HibernateUtil.getCurrentSession();
		String email = (String) getThreadLocalRequest().getSession()
				.getAttribute(BaseServlet.EMAIL_ID);
		Number clientId = (Number) hibernateSession
				.getNamedQuery("getClientByCompany")
				.setParameter("clientEmail", email)
				.setParameter("companyName", companyName).uniqueResult();
		return clientId != null;
	}
}
