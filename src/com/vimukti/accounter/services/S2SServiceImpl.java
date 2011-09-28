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
import com.vimukti.accounter.core.Server;
import com.vimukti.accounter.core.ServerCompany;
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

public class S2SServiceImpl extends RemoteServiceServlet implements IS2SService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void createComapny(long companyID, String companyName,
			int companyType, ClientUser user) throws AccounterException {
		Company company = new Company(companyType);
		company.setFullName(companyName);
		init(company, companyID, user);
	}

	@Override
	public boolean isAdmin(long companyID, String emailID) {
		String schemaName = Server.COMPANY + companyID;
		Session session = HibernateUtil.openSession(schemaName);
		try {
			User user = (User) session.getNamedQuery("adminUserForEmailId")
					.setParameter("emailid", emailID).uniqueResult();
			if (user != null) {
				return true;
			}
			return false;
		} finally {
			session.close();
		}
	}

	@Override
	public void deleteUserFromCompany(long companyID, String email) {
		String schema = Server.COMPANY + companyID;
		Session session = HibernateUtil.openSession(schema);
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Company company = (Company) session.get(Company.class, 1l);
			User user = company.getUserByUserEmail(email);
			company.getUsers().remove(user);
			session.saveOrUpdate(company);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			session.close();
		}

	}

	private void init(Company company, long serverCompnayId,
			ClientUser clientUser) throws AccounterException {

		String schemaName = Server.COMPANY + serverCompnayId;

		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		Transaction serverTransaction = null;
		try {
			serverTransaction = session.beginTransaction();
			Query query = session.createSQLQuery("CREATE SCHEMA " + schemaName);
			query.executeUpdate();
			serverTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (serverTransaction != null) {
				serverTransaction.rollback();
			}
			throw new AccounterException(e);
		} finally {
			session.close();
		}
		Session companySession = HibernateUtil.openSession(schemaName, true);
		Transaction transaction = null;
		try {
			transaction = companySession.beginTransaction();
			// Creating User
			User user = new User(clientUser);
			user.setActive(true);
			companySession.save(user);

			AccounterThreadLocal.set(user);

			company.getUsers().add(user);
			company.setCompanyEmail(user.getEmail());

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

			FinanceTool.createView();

			transaction.commit();
			UsersMailSendar.sendMailToDefaultUser(user, company.getFullName());

		} catch (Exception e) {
			e.printStackTrace();
			dropSchema(schemaName);
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

	private void dropSchema(String schemaName) throws AccounterException {
		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		Transaction serverTransaction = null;
		try {
			serverTransaction = session.beginTransaction();
			Query query = session.createSQLQuery("DROP SCHEMA " + schemaName);
			query.executeUpdate();
			serverTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (serverTransaction != null) {
				serverTransaction.rollback();
			}
			throw new AccounterException(e);
		} finally {
			session.close();
		}
	}

	@Override
	public void deleteClientFromCompany(long serverCompanyId,
			String deletableEmail) throws AccounterException {

		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Client deletingClient = getClient(deletableEmail);

			ServerCompany serverCompany = null;
			serverCompany = (ServerCompany) session.load(ServerCompany.class,
					serverCompanyId);
			// serverCompany.getClients().remove(deletingClient);
			// session.saveOrUpdate(serverCompany);
			deletingClient.getCompanies().remove(serverCompany);
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

		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);

		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();
			Client inviter = getClient(senderEmailId);

			ServerCompany serverCompany = (ServerCompany) session.load(
					ServerCompany.class, companyId);

			String invitedUserEmailID = userInfo.getEmail();
			Client invitedClient = getClient(invitedUserEmailID);
			boolean userExists = false;
			String randomString = HexUtil.getRandomString();
			if (invitedClient == null) {
				invitedClient = new Client();
				invitedClient.setActive(true);
				Set<ServerCompany> servercompanies = new HashSet<ServerCompany>();
				servercompanies.add(serverCompany);
				invitedClient.setCompanies(servercompanies);
				invitedClient.setCountry(inviter.getCountry());
				invitedClient.setEmailId(invitedUserEmailID);
				invitedClient.setFirstName(userInfo.getFirstName());
				invitedClient.setLastName(userInfo.getLastName());
				invitedClient.setPassword(HexUtil.bytesToHex(Security
						.makeHash(invitedUserEmailID + randomString)));
				// invitedClient.setRequirePasswordReset(true);
			} else {
				userExists = true;
				Set<ServerCompany> invitedClientCompanies = invitedClient
						.getCompanies();
				for (ServerCompany invitedClientCompany : invitedClientCompanies) {
					if (serverCompany == invitedClientCompany) {
						// req.setAttribute("message",
						// "Invited user already exists in your company.");
						return true;
					}
				}
				invitedClient.getCompanies().add(serverCompany);
			}

			session.save(invitedClient);
			transaction.commit();
			if (userExists) {
				UsersMailSendar.sendMailToOtherCompanyUser(invitedClient,
						serverCompany.getCompanyName(), inviter);
			} else {
				UsersMailSendar.sendMailToInvitedUser(invitedClient,
						randomString, serverCompany.getCompanyName());
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
		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Query dropSchema = session.createSQLQuery("DROP SCHEMA "
					+ Server.COMPANY + serverCompanyID);
			dropSchema.executeUpdate();
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

	@Override
	public void updateServerCompany(long serverCompanyID, String fullName)
			throws AccounterException {
		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			ServerCompany company = (ServerCompany) session.get(
					ServerCompany.class, serverCompanyID);
			company.setCompanyName(fullName);
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