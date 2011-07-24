package com.vimukti.accounter.main;
//package com.bizantra.server.main;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Set;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//
//import com.bizantra.server.internal.core.BizantraCompany;
//import com.bizantra.server.internal.core.Extension;
//import com.bizantra.server.storage.HibernateUtil;
//
///**
// * @author Sai Karthik K
// * 
// *         This thread is used to check the deletion date of the company daily
// *         and deletes the company if the present day is its deletion date or
// *         even if it crosses the deletion date due to some server crash.
// * 
// */
//public class DeleteCompanyThread extends Thread {
//
//	private static final int ONE_DAY = 1000 * 60 * 60 * 24;
//	private static DeleteCompanyThread deleteCompanyThread;
//
//	public DeleteCompanyThread() {
//		Timer timer = new Timer();
//
//		timer.schedule(new TimerTask() {
//
//			@Override
//			public void run() {
//
//				checkDBForDeletingCompanies();
//			}
//		}, 0, ONE_DAY);
//	}
//
//	protected void checkDBForDeletingCompanies() {
//
//		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
//
//		ArrayList<BizantraCompany> companies = (ArrayList<BizantraCompany>) session
//				.getNamedQuery("get.all.companies").list();
//		session.close();
//		for (BizantraCompany company : companies) {
//
//			session = HibernateUtil.openSession(company.getCompanyDomainName());
//			List companiesList = session.getNamedQuery("get.company.by.name")
//					.setString("name", company.getCompanyDomainName()).list();
//			Set<Extension> companyExtensions = null;
//			if (companiesList.size() != 0) {
//				company = (BizantraCompany) companiesList.get(0);
//				companyExtensions = company.getExtensionsSet();
//				int noOfUsers = 0;
//				int noOfLiteUsers = 0;
//
//				List<Extension> temp = new ArrayList<Extension>();
//				for (Extension companyExtension : companyExtensions) {
//
//					Calendar today = Calendar.getInstance();
//					Calendar expiryDate = companyExtension.getExpiryDate(); // block
//					// users
//					// and
//					// Storage..
//
//					Calendar deletionDate = companyExtension
//							.getDeletionDateByExpiryDate();
//					String expiryType = companyExtension.getExtensionType();
//					noOfUsers = companyExtension.getNoOfUsers();
//					noOfLiteUsers = companyExtension.getNoOfLiteUsers();
//					String refId = companyExtension.getID();
//
//					if ((today.get(Calendar.YEAR) == (deletionDate)
//							.get(Calendar.YEAR)
//							&& today.get(Calendar.MONTH) == (deletionDate)
//									.get(Calendar.MONTH) && today
//							.get(Calendar.DATE) == (deletionDate)
//							.get(Calendar.DATE))
//							|| today.after(deletionDate)) {
//
//						Transaction tx = session.beginTransaction();
//						session.createSQLQuery(
//								"delete from EXTENSION where id = :refId")
//								.setString("refId", refId).executeUpdate();
//						tx.commit();
//					}
//				}
//				session.close();
//				company
//						.setExtendedUsers(company.getExtendedUsers()
//								- noOfUsers);
//				company.setExtendedLiteUsers(company.getExtendedLiteUsers()
//						- noOfLiteUsers);
//				Date today = new Date();
//				Date deletionDate = company.getDeletionDateAfterExpiry();
//				String companyName = company.getCompanyDomainName();
//
//				if (today.equals(deletionDate) || today.after(deletionDate)) {
//					try {
//						session = HibernateUtil
//								.openSession(Server.LOCAL_DATABASE);
//						Transaction tx = session.beginTransaction();
//						session.createSQLQuery("drop schema " + companyName)
//								.executeUpdate();
//						session
//								.createSQLQuery(
//										"delete from BIZANTRA_COMPANY where NAME =:companyName")
//								.setString("companyName", companyName)
//								.executeUpdate();
//						tx.commit();
//						session.close();
//
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//
//	}
//
//	public static DeleteCompanyThread getInstance() {
//
//		if (deleteCompanyThread == null) {
//			deleteCompanyThread = new DeleteCompanyThread();
//		}
//		return deleteCompanyThread;
//	}
// }
