package com.vimukti.accounter.web.server.managers;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Activity;
import com.vimukti.accounter.core.ActivityType;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.ServerConvertUtil;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.OperationContext;

public class UserManager extends Manager {
	public long inviteUser(OperationContext context) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			IAccounterCore data = context.getData();
			if (data == null) {
				throw new AccounterException(
						AccounterException.ERROR_ILLEGAL_ARGUMENT,
						"Operation Data Found Null...." + data);
			}
			User user = new User((ClientUser) data);
			String email = user.getEmail();
			Company company = getCompany(context.getCompanyId());
			User userByUserEmail = getUserByUserEmail(email, company);
			if (userByUserEmail != null) {
				if (userByUserEmail.isDeleted()) {
					userByUserEmail.setDeleted(false);
					userByUserEmail.setUserRole(user.getUserRole());
					userByUserEmail.setPermissions(user.getPermissions());
					userByUserEmail.setCanDoUserManagement(user
							.isCanDoUserManagement());
					user = userByUserEmail;
					session.saveOrUpdate(user);
				}
			} else {

				company.addUser(user);
			}
			String userID = context.getUserEmail();

			User inviteduser = getUserByUserEmail(userID, company);
			Activity inviteuserActivity = new Activity(company, inviteduser,
					ActivityType.ADD, user);

			session.save(inviteuserActivity);
			transaction.commit();
			ClientUser clientObject = new ClientConvertUtil().toClientObject(
					user, ClientUser.class);
			ChangeTracker.put(clientObject.toUserInfo());
			return user.getID();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			transaction.rollback();
			if (e instanceof AccounterException) {
				throw (AccounterException) e;
			} else {
				throw new AccounterException(AccounterException.ERROR_INTERNAL,
						e.getMessage());
			}
		}
	}

	public long updateUser(OperationContext updateContext)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		org.hibernate.Transaction hibernateTransaction = session
				.beginTransaction();
		try {
			IAccounterCore data = updateContext.getData();

			ClientUser clientUser = (ClientUser) data;

			User user = (User) session.get(User.class, clientUser.getID());

			String userID = updateContext.getUserEmail();

			Company company = getCompany(updateContext.getCompanyId());
			User user1 = company.getUserByUserEmail(userID);
			new ServerConvertUtil().toServerObject(user,
					(IAccounterCore) clientUser, session);
			canEdit(user, data);

			session.flush();
			session.saveOrUpdate(user);
			Activity userUpdateActivity = new Activity(company, user1,
					ActivityType.EDIT, user);
			session.save(userUpdateActivity);
			hibernateTransaction.commit();
			ChangeTracker.put(clientUser.toUserInfo());
			return user.getID();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			hibernateTransaction.rollback();
			if (e instanceof AccounterException) {
				throw (AccounterException) e;
			} else {
				throw new AccounterException(AccounterException.ERROR_INTERNAL);
			}
		}

	}

	private User getUserByUserEmail(String email, Company company) {
		Session session = HibernateUtil.getCurrentSession();
		return (User) session.getNamedQuery("user.by.emailid")
				.setParameter("emailID", email)
				.setParameter("company", company).uniqueResult();

	}

	public PaginationList<ClientActivity> getUsersActivityLog(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			int startIndex, int length, long companyId) {

		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Timestamp startTime = new Timestamp(startDate.getDateAsObject()
				.getTime());
		Timestamp endTime = new Timestamp(endDate.getDateAsObject().getTime());
		endTime.setHours(23);
		endTime.setMinutes(59);
		endTime.setSeconds(59);
		Query query;
		int count;
		if (startDate.getDate() == 0 || endDate.getDate() == 0) {
			query = session.getNamedQuery("list.Activity").setEntity("company",
					company);
			query.setFirstResult(startIndex);
			query.setMaxResults(length);
			count = ((BigInteger) session.createSQLQuery(
					"SELECT COUNT(*) FROM ACTIVITY").uniqueResult()).intValue();
		} else {
			query = session.getNamedQuery("get.Activities.by.date");
			query.setParameter("fromDate", startTime);
			query.setParameter("endDate", endTime);
			query.setFirstResult(startIndex);
			query.setMaxResults(length);
			query.setEntity("company", company);
			count = ((BigInteger) session
					.createSQLQuery(
							"SELECT COUNT(*) FROM ACTIVITY A WHERE A.TIME_STAMP BETWEEN :fromDate AND :endDate")
					.setParameter("fromDate", startTime)
					.setParameter("endDate", endTime).uniqueResult())
					.intValue();
		}
		List<Activity> activites = query.list();
		PaginationList<ClientActivity> clientActivities = new PaginationList<ClientActivity>();
		for (Activity activity : activites) {
			ClientActivity clientActivity;
			try {
				clientActivity = new ClientConvertUtil().toClientObject(
						activity, ClientActivity.class);
				clientActivities.add(clientActivity);
			} catch (AccounterException e) {
				e.printStackTrace();
			}

		}
		clientActivities.setTotalCount(count);
		return clientActivities;
	}

	public ArrayList<ClientUserInfo> getAllUsers(long companyId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		List<User> financeUsers = session.getNamedQuery("list.User")
				.setEntity("company", company).list();
		List<ClientUserInfo> clientUsers = new ArrayList<ClientUserInfo>();
		for (User user : financeUsers) {
			if (!user.isDeleted()) {
				ClientUser clientUser = new ClientConvertUtil().toClientObject(
						user, ClientUser.class);
				ClientUserInfo userInfo = clientUser.toUserInfo();
				clientUsers.add(userInfo);
			}
		}
		return new ArrayList<ClientUserInfo>(clientUsers);
	}

	public boolean changeMyPassword(String emailId, String oldPassword,
			String newPassword) throws DAOException {

		Session session = HibernateUtil.openSession();
		org.hibernate.Transaction tx = null;

		try {
			tx = session.beginTransaction();
			oldPassword = HexUtil.bytesToHex(Security.makeHash(emailId
					+ oldPassword));
			newPassword = HexUtil.bytesToHex(Security.makeHash(emailId
					+ newPassword));

			Query query = session.getNamedQuery("getEmailIdFromClient")
					.setParameter("emailId", emailId)
					.setParameter("password", oldPassword);
			String emailID = (String) query.uniqueResult();

			if (emailID == null)
				return false;

			query = session.getNamedQuery("updatePasswordForClient");
			query.setParameter("newPassword", newPassword);
			query.setParameter("emailId", emailId);
			query.executeUpdate();
			tx.commit();

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return true;
	}

	public void createAdminUser(ClientUser user, long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		User admin = new User(user);
		admin.setActive(true);
		session.save(admin);
		Company company = getCompany(companyId);
		company.getUsersList().add(admin);
		session.saveOrUpdate(this);
		transaction.commit();
	}

}
