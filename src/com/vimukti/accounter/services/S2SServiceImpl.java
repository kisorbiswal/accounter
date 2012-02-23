package com.vimukti.accounter.services;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.servlets.BaseServlet;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.OperationContext;

public class S2SServiceImpl extends RemoteServiceServlet implements IS2SService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void createComapny(long companyID, String companyName,
			int companyType, ClientUser user) throws AccounterException {
		Company company = new Company();
		company.setID(companyID);
		company.setTradingName(companyName);
		init(company, companyID, user);
	}

	@Override
	public boolean isAdmin(long companyID, String emailID) {
		Session session = HibernateUtil.openSession();
		try {
			User user = (User) session
					.getNamedQuery("adminUserForEmailId")
					.setParameter("emailid", emailID)
					.setParameter("company",
							session.get(Company.class, companyID))
					.uniqueResult();
			if (user != null) {
				return true;
			}
			return false;
		} finally {
			session.close();
		}
	}

	@Override
	public void deleteUserFromCompany(long companyID, ClientUser user) {
		Session session = HibernateUtil.openSession();
		try {
			String clientClassSimpleName = user.getObjectType()
					.getClientClassSimpleName();
			FinanceTool financeTool = new FinanceTool();
			OperationContext context = new OperationContext(companyID, user,
					user.getEmail(), String.valueOf(user.getID()),
					clientClassSimpleName);
			financeTool.delete(context);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	private void init(Company company, long serverCompnayId,
			ClientUser clientUser) throws AccounterException {

		// String schemaName = Server.COMPANY + serverCompnayId;
		//
		// Session session = HibernateUtil.openSession();
		// Transaction serverTransaction = session.beginTransaction();
		// try {
		// Query query = session.createSQLQuery("CREATE SCHEMA " + schemaName);
		// query.executeUpdate();
		// serverTransaction.commit();
		// } catch (Exception e) {
		// e.printStackTrace();
		// serverTransaction.rollback();
		// throw new AccounterException(e);
		// }
		Session companySession = HibernateUtil.openSession();
		Transaction transaction = null;
		try {
			transaction = companySession.beginTransaction();
			// Creating User
			User user = new User(clientUser);
			user.setActive(true);
			companySession.save(user);

			AccounterThreadLocal.set(user);
			company.getUsers().add(user);
			company.setCompanyEmail(user.getClient().getEmailId());

			// Comment these 4 Lines If you want Company Setup
			CompanyPreferences preferences = company.getPreferences();
			// if (preferences == null) {
			// preferences = new CompanyPreferences();
			// }
			// preferences.setSellProducts(true);
			// preferences.setSellServices(true);
			// preferences.setPurchaseOrderEnabled(true);
			// preferences.setDoyouwantEstimates(true);
			// company.setConfigured(true);

			companySession.save(company);

			// Comment this Line If You want CompantSetUp
			// company.initialize(null);

			transaction.commit();
			UsersMailSendar.sendMailToDefaultUser(user,
					company.getTradingName());

		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
			throw new AccounterException(e);
		} finally {
			if (companySession.isOpen()) {
				companySession.close();
			}
		}
	}

	// private void dropSchema(String schemaName) throws AccounterException {
	// Session session = HibernateUtil.openSession();
	// Transaction serverTransaction = session.beginTransaction();
	// try {
	// Query query = session.createSQLQuery("DROP SCHEMA " + schemaName);
	// query.executeUpdate();
	// serverTransaction.commit();
	// } catch (Exception e) {
	// e.printStackTrace();
	// serverTransaction.rollback();
	// throw new AccounterException(e);
	// }
	// }

	@Override
	public void deleteClientFromCompany(long serverCompanyId,
			String deletableEmail) throws AccounterException {

		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Client deletingClient = getClient(deletableEmail);

			Company company = null;
			company = (Company) session.load(Company.class, serverCompanyId);
			// serverCompany.getClients().remove(deletingClient);
			// session.saveOrUpdate(serverCompany);
			deletingClient.getUsers().remove(company);
			session.saveOrUpdate(deletingClient);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw new AccounterException(AccounterException.ERROR_INTERNAL);
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	protected Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter(BaseServlet.EMAIL_ID, emailId);
		Client client = (Client) namedQuery.uniqueResult();
		return client;
	}

	@Override
	public boolean inviteUser(long companyId, ClientUserInfo userInfo,
			String senderEmailId) throws AccounterException {

		Session session = HibernateUtil.openSession();

		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();
			Client inviter = getClient(senderEmailId);

			Company serverCompany = (Company) session.load(Company.class,
					companyId);

			String invitedUserEmailID = userInfo.getEmail();
			Client invitedClient = getClient(invitedUserEmailID);
			boolean userExists = false;
			String randomString = HexUtil.getRandomString();
			if (invitedClient == null) {
				invitedClient = new Client();
				invitedClient.setActive(true);
				Set<Company> servercompanies = new HashSet<Company>();
				servercompanies.add(serverCompany);
				// invitedClient.setUsers(servercompanies);
				invitedClient.setCountry(inviter.getCountry());
				invitedClient.setEmailId(invitedUserEmailID);
				invitedClient.setFirstName(userInfo.getFirstName());
				invitedClient.setLastName(userInfo.getLastName());
				invitedClient.setPassword(HexUtil.bytesToHex(Security
						.makeHash(invitedUserEmailID
								+ Client.PASSWORD_HASH_STRING + randomString)));
				// invitedClient.setRequirePasswordReset(true);
			} else {
				userExists = true;
				// Set<Company> invitedClientCompanies =
				// invitedClient.getUsers();
				// for (Company invitedClientCompany : invitedClientCompanies) {
				// if (serverCompany == invitedClientCompany) {
				// req.setAttribute("message",
				// "Invited user already exists in your company.");
				// return true;
				// }
				// }
				// invitedClient.getUsers().add(serverCompany);
			}

			session.save(invitedClient);
			transaction.commit();
			if (userExists) {
				UsersMailSendar.sendMailToOtherCompanyUser(invitedClient,
						serverCompany.getTradingName(), inviter);
			} else {
				UsersMailSendar.sendMailToInvitedUser(invitedClient,
						randomString, serverCompany.getTradingName());
			}

			return userExists;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw new AccounterException(AccounterException.ERROR_INTERNAL, e);
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public void deleteCompany(long serverCompanyID) throws AccounterException {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Company object = (Company) session.get(Company.class,
					serverCompanyID);
			object.onDelete(session);
			session.delete(object);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
			throw new AccounterException(AccounterException.ERROR_INTERNAL, e);
		} finally {
			session.close();
		}
	}

	@Override
	public void updateServerCompany(long serverCompanyID, String fullName)
			throws AccounterException {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Company company = (Company) session.get(Company.class,
					serverCompanyID);
			company.setTradingName(fullName);
			session.saveOrUpdate(company);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw new AccounterException(AccounterException.ERROR_INTERNAL, e);
		} finally {
			session.close();
		}
	}
}