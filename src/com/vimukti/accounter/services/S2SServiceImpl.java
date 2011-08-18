package com.vimukti.accounter.services;

import java.io.File;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.ServerCompany;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.main.Server;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.servlets.BaseServlet;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class S2SServiceImpl implements IS2SService {

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
		User user = (User) session.getNamedQuery("adminUserForEmailId")
				.setParameter("emailid", emailID).uniqueResult();
		if (user != null) {
			return true;
		}
		return false;
	}

	@Override
	public void deleteUserFromCompany(long companyID, String email) {
		String schema = Server.COMPANY + companyID;
		Session session = HibernateUtil.openSession(schema);
		Transaction transaction = session.beginTransaction();
		try {
			Company company = (Company) session.get(Company.class, 1l);
			User user = company.getUserByUserEmail(email);
			session.delete(user);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			session.close();
		}

	}

	private void init(Company company, long serverCompnayId,
			ClientUser clientUser) {

		String schemaName = Server.COMPANY + serverCompnayId;

		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		Transaction serverTransaction = session.beginTransaction();
		try {
			Query query = session.createSQLQuery("CREATE SCHEMA " + schemaName);
			query.executeUpdate();
			serverTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			serverTransaction.rollback();
			return;
		}
		Session companySession = HibernateUtil.openSession(schemaName, true);
		Transaction transaction = companySession.beginTransaction();
		try {
			// Creating User
			User user = new User(clientUser);
			companySession.save(user);

			AccounterThreadLocal.set(user);

			company.getUsers().add(user);
			company.setCompanyEmail(user.getEmail());
			companySession.save(company);

			// Create Attachment Directory for company
			File file = new File(ServerConfiguration.getAttachmentsDir(company
					.getFullName()));

			if (!file.exists()) {
				file.mkdir();
			}

			FinanceTool.createView();

			transaction.commit();
			UsersMailSendar.sendMailToDefaultUser(user, company.getFullName());

		} catch (Exception e) {
			e.printStackTrace();
			dropSchema(schemaName);
			transaction.rollback();
			return;
		} finally {
			if (companySession.isOpen()) {
				companySession.close();
			}
		}
	}

	private void dropSchema(String schemaName) {
		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		Transaction serverTransaction = session.beginTransaction();
		try {
			Query query = session.createSQLQuery("DROP SCHEMA " + schemaName);
			query.executeUpdate();
			serverTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			serverTransaction.rollback();
		}
	}

	@Override
	public void deleteClientFromCompany(long serverCompanyId,
			String deletableEmail) {

		Session openSession = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		Transaction beginTransaction = openSession.beginTransaction();

		Client deletableClient = getClient(deletableEmail);

		ServerCompany serverCompany = null;
		try {
			serverCompany = (ServerCompany) openSession.load(
					ServerCompany.class, serverCompanyId);
			serverCompany.getClients().remove(deletableClient);
			openSession.saveOrUpdate(serverCompany);
			deletableClient.getCompanies().remove(serverCompany);
			openSession.saveOrUpdate(deletableClient);
			beginTransaction.commit();
		} catch (HibernateException e) {
			beginTransaction.rollback();
			return;
		} catch (Exception e) {
			beginTransaction.rollback();
			return;
		} finally {
			if (openSession != null && openSession.isOpen()) {
				openSession.close();
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
}