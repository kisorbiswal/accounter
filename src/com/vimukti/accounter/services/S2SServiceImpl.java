package com.vimukti.accounter.services;

import java.io.File;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.main.Server;
import com.vimukti.accounter.main.ServerConfiguration;
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
	public void deleteUserFromCompany(String deletableEmail, String senderEmail) {
		// TODO Auto-generated method stub

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
	public void deleteClientFromCompany(String email) {
		// TODO Auto-generated method stub
		
	}
}
