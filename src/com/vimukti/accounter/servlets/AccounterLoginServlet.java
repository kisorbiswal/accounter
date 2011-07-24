package com.vimukti.accounter.servlets;

import java.io.File;
import java.io.IOException;
import java.security.Security;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.UserPermissions;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.web.client.data.BizantraConstants;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

public class AccounterLoginServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String view = "/sites/accounterlogin.jsp";
	private UserInfo user;
	private String domainName;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// doPost(req, resp);
		user = (UserInfo) req.getSession().getAttribute("user");
		if (user == null) {
			return;
		}
		domainName = getDomainName();

		if (!isCompanyExitsWithDomian(domainName)) {
			dispatchServlet(req, resp, view);
		} else {
			showAccounterHome(req, resp, false);
		}
	}

	private boolean isCompanyExitsWithDomian(String domainName2) {
		// TODO Auto-generated method stub
		return false;
	}

	private void showAccounterHome(HttpServletRequest req,
			HttpServletResponse resp, boolean isAdmin) {
		String companyName = getCompanyNameForDomain();
		if (companyName != null) {
			// Session session = HibernateUtil.openSession(companyName);
			// Transaction tx = session.beginTransaction();
			// if (session == null) {
			// return;
			// } else {
			createOrLoginUser(req, resp, companyName, isAdmin);
			// }
			// tx.commit();
			// session.close();
		}
	}

	private void createOrLoginUser(HttpServletRequest req,
			HttpServletResponse resp, String companyName, boolean isAdmin) {
		Session session = HibernateUtil.openSession(companyName);
		Transaction tx = session.beginTransaction();
		if (!isUserExistInCompany(session)) {
			createUser(req, resp, session, companyName, isAdmin);
		}
		tx.commit();
		session.close();
		liveLoginUser(req, resp, companyName);
	}

	private void createUser(HttpServletRequest req, HttpServletResponse resp,
			Session session, String companyName, boolean isAdmin) {
		CollaberIdentity identity = (CollaberIdentity) session
				.getNamedQuery("getIDentity.from.emailid")
				.setParameter("emailid", user.getEmail()).uniqueResult();
		if (identity == null) {
			identity = new CollaberIdentity(RolePermissions.BASIC_EMPLOYEE,
					SecureUtils.createID(), companyName);
			identity.setEmailID(user.getEmail());
			identity.setPassword("");
			identity.setEncryptedID(Security.getBytes(Security
					.createSpaceEncKey()));
			createMembers(session, identity);

		}
		User financeUser = new User();
		financeUser.setID(identity.getID());
		financeUser.setFirstName(user.getFirstName());
		financeUser.setLastName(user.getLastName());
		financeUser.setEmailId(user.getEmail());
		UserPermissions permissions = new UserPermissions();
		permissions.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		permissions.setTypeOfInvoices(RolePermissions.TYPE_YES);
		permissions.setTypeOfSystemSettings(RolePermissions.TYPE_YES);
		permissions.setTypeOfViewReports(RolePermissions.TYPE_YES);
		if (isAdmin) {
			identity.setRole(RolePermissions.ADMIN);
			financeUser.setUserRole(RolePermissions.ADMIN);
			permissions.setTypeOfExpences(RolePermissions.TYPE_APPROVE);
			permissions.setTypeOfPublishReports(RolePermissions.TYPE_YES);
			permissions.setTypeOfLockDates(RolePermissions.TYPE_YES);
			financeUser.setCanDoUserManagement(true);

		} else {
			identity.setRole(RolePermissions.BASIC_EMPLOYEE);
			financeUser.setUserRole(RolePermissions.BASIC_EMPLOYEE);
			permissions.setTypeOfExpences(RolePermissions.TYPE_DRAFT_ONLY);
			permissions.setTypeOfPublishReports(RolePermissions.TYPE_NO);
			permissions.setTypeOfLockDates(RolePermissions.TYPE_NO);
			financeUser.setCanDoUserManagement(false);
		}
		financeUser.setCompany(companyName);
		financeUser.setAdmin(isAdmin);
		financeUser.setPermissions(permissions);
		session.save(identity);
		session.save(financeUser);
	}

	private void liveLoginUser(HttpServletRequest req,
			HttpServletResponse resp, String companyName) {

		Session session = null;
		try {
			CollaberIdentity identity = doLogin(req, resp, companyName);
			session = HibernateUtil.openSession(companyName);
			if (identity != null) {
				intializeIdentity(req, identity);

				resp.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
				resp.setHeader("Location", "/accounter");
				return;
			} else {

			}
		} finally {
			if (session != null)
				session.close();
		}

	}

	private void intializeIdentity(HttpServletRequest request,
			CollaberIdentity identity) {
		try {
			identity.incrementLoginCountAndStatus();
			request.getSession().setAttribute(
					BizantraService.SESSION_IDENTITYID, identity.getID());
			request.getSession().setAttribute(BizantraService.COMPANY_NAME,
					identity.getCompanyName());
			request.getSession().setAttribute("isLoggedInFromDomain", true);
			Server.getInstance().addSeesionIdOfIdentity(identity.getID(),
					request.getSession().getID());

			request.setAttribute("success", true);

		} finally {
		}
	}

	private CollaberIdentity doLogin(HttpServletRequest request,
			HttpServletResponse response, String companyName) {
		String emailId = user.getEmail();
		String password = new String("");
		emailId = emailId.trim();
		password = password.trim();

		if (emailId != null && password != null) {
			password = HexUtil
					.bytesToHex(Security.makeHash(emailId + password));
			CollaberIdentity identity = getIDentity(emailId, request,
					companyName);
			return identity;
		}

		return null;
	}

	private CollaberIdentity getIDentity(String emailId,
			HttpServletRequest request, String companyName) {

		Session session = HibernateUtil.openSession(companyName);
		try {
			CollaberIdentity identity = null;
			Query query = session.getNamedQuery("getIDentity.from.emailid");
			query.setParameter("emailid", emailId);
			// query.setParameter("password", password);
			identity = (CollaberIdentity) query.uniqueResult();
			return identity;
		} finally {
			session.close();
		}
	}

	private String getCompanyNameForDomain() {
		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		Object uniqueResult = session
				.getNamedQuery("getCompanyName.using.domainName")
				.setString("domainName", domainName).uniqueResult();
		session.close();
		if (uniqueResult == null) {
			return null;
		}
		BizantraCompany company = (BizantraCompany) uniqueResult;
		return company.getCompanyDomainName();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		BizantraCompany company = doCreateCompany(req);
		if (company != null) {
			initSessionFactory(company, req);
			showAccounterHome(req, resp, true);
		} else {
			dispatchServlet(req, resp, view);
		}

		// if (BizantraService.isCompanyExitsWithDomian(domainName)) {
		//
		// } else {
		// req.setAttribute("firstName", user.getFirstName());
		// req.setAttribute("lastName", user.getLastName());
		// req.setAttribute("emailID", user.getEmail());
		// req.setAttribute("domainName", domainName);
		// dispatchServlet(req, resp, view);
		// }

	}

	private void initSessionFactory(BizantraCompany company,
			HttpServletRequest request) {
		try {
			init(company, request);
		} catch (Throwable e) {
			throw new ExceptionInInitializerError(e);
		}

	}

	private void init(BizantraCompany company, HttpServletRequest request) {
		// HibernateUtil.rebuildSessionFactory();
		Session session = HibernateUtil.openSession(
				company.getCompanyDomainName(), true);
		// Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			session.save(company);
			CollaberIdentity identity = new CollaberIdentity(
					RolePermissions.ADMIN, SecureUtils.createID(),
					company.getCompanyDomainName());
			identity.setEncryptedID(Security.getBytes(Security
					.createSpaceEncKey()));
			identity.setEmailID(user.getEmail().trim());
			identity.setPassword("");
			identity.setPasswordChanged(true);
			// identity.setCompanyName(company.getCompanyDomainName());
			initializeBizantraUserPreference(identity);
			session.save(identity);

			session.saveOrUpdate(identity);
			// UsersMailSendar.sendMailToDefaultUser(identity, company
			// .getCompanyDisplayName());
			try {
				tx.commit();
			} catch (RuntimeException re) {
				try {
					tx.rollback();
				} catch (RuntimeException re2) {

				}
			}

			// Create Attachment Directory for company
			File file = new File(ServerConfiguration.getAttachmentsDir(company
					.getCompanyDomainName()));

			if (!file.exists()) {
				file.mkdir();
			}

		} catch (HibernateException e) {
			e.printStackTrace();
			// deleteCompany(company.getCompanyDomainName());
		} finally {
			session.close();
		}
	}

	private BizantraCompany doCreateCompany(HttpServletRequest request) {
		BizantraCompany company = getCompany(request);
		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);

		Transaction tx = session.beginTransaction();
		if (!validation(request)) {
			return null;
		}
		try {

			// TODO write sql query for creating database in .xml file
			Query query = session.createSQLQuery("CREATE SCHEMA "
					+ company.getCompanyDomainName());
			query.executeUpdate();
			session.save(company);
			tx.commit();
			return company;
		} catch (RuntimeException re) {
			re.printStackTrace();
			tx.rollback();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	private boolean validation(HttpServletRequest request) {
		Boolean flag = true;
		String message = "";
		String companyName = request.getParameter("companyName");
		if (BizantraService.isCompanyExits(companyName)) {
			if (!message.isEmpty())
				message = message + ", Company with this name is already exist";
			else
				message = "Company with this name is already exist";
			flag = false;
		}
		request.setAttribute("errormessage", message);
		return flag;
	}

	private BizantraCompany getCompany(HttpServletRequest request) {
		String companyName = request.getParameter("companyName");
		// String noOfUsers = request.getParameter("nooofusers");
		// String noOfLiteUsers = request.getParameter("noofliteusers");
		String address = request.getParameter("address");
		String city = request.getParameter("city");
		String country = request.getParameter("country");
		String zip = request.getParameter("zip");
		String province = request.getParameter("provence");
		Date expirationDate = new Date();
		expirationDate.setYear(expirationDate.getYear() + 10);
		BizantraCompany company = new BizantraCompany(companyName, companyName,
				expirationDate);
		company.setUserDomain(domainName);
		// company.setTotalNoOfUsers(Integer.parseInt(noOfUsers));
		// company.setTotalNoOfLiteUsers(Integer.parseInt(noOfLiteUsers));
		company.setTotalSize(1024 * 1024 * 1024);
		// company.setID(SecureUtils.createID());
		// company.setCompanyDomainName(name);
		// company.setCompanyDisplayName(name);
		// company.setSubscriptionType("FULL");
		// company.setInitialUsers(Integer.parseInt(noOfUsers));
		// company.setInitialSize(1073741824);
		// company.setTotalNoOfUsers(Integer.parseInt(noOfUsers));
		company.setBizantraVersion(Integer.parseInt(request
				.getParameter("companyType")));
		company.setAddress(address);
		company.setCity(city);
		company.setCountry(country);
		company.setZip(zip);
		company.setProvince(province);
		company.setDateStyle(2);
		if (company.getDateStyle() == 2) {
			company.setCountryCode("United Kingdom");
		} else {
			// TODO HAVE TO BE SET COUNTRY CODE BASE DON DATE STYLE PRESENT
			// GIVING DEFAULT ONE IS 'United Kingdom'
			company.setCountryCode("United Kingdom");
		}
		// company.setTime("Y");
		// company.setPeriod(1);
		// company.setExpirationDate(company.getExpirationDateByDate("Y"));

		StringBuilder holidayStartString = new StringBuilder();
		holidayStartString.append(request.getParameter("startDateDate"));
		holidayStartString.append(", ");
		holidayStartString.append(request.getParameter("startDateMonth"));
		company.setHolidayStartDate(holidayStartString.toString());

		company.setSubsType(BizantraConstants.TRIAL_SUBSCRIPTION);
		company.setSubscriberEmail(user.getEmail().trim());

		return company;
	}

	// @Override
	// protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
	// throws ServletException, IOException {
	// doPost(arg0, arg1);
	// }

	private void dispatchServlet(HttpServletRequest request,
			HttpServletResponse response, String view) {
		try {
			RequestDispatcher dispatcher = getServletContext()
					.getRequestDispatcher(view);
			dispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getDomainName() {
		if (user == null) {
			return null;
		}
		String claimedIdURL[] = user.getClaimedId().split("//");
		claimedIdURL = claimedIdURL[1].split("/");
		claimedIdURL = claimedIdURL[0].split("\\.");
		String domainName = claimedIdURL[0];

		return domainName;
	}

	private boolean isUserExistInCompany(Session session) {
		Object uniqueResult = session.getNamedQuery("unique.emailId.User")
				.setParameter(0, user.getEmail()).uniqueResult();
		if (uniqueResult == null)
			return false;
		else
			return true;
	}

	private void initializeBizantraUserPreference(CollaberIdentity identity) {
		BizantraUserPreferences userPreferences = new BizantraUserPreferences();
		userPreferences.setDefaultPreferences();
		identity.setUserPreferences(userPreferences);

	}
}
