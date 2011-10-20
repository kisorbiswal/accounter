/**
 * 
 */
package com.vimukti.accounter.web.server;

import java.util.Date;
import java.util.List;

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
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.services.IS2SService;
import com.vimukti.accounter.servlets.BaseServlet;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.IAccounterCompanyInitializationService;
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

	/**
	 * Returns User Object From Client HttpSession
	 */

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
}
