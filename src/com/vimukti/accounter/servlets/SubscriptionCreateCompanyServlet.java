package com.vimukti.accounter.servlets;

import java.io.File;
import java.io.IOException;
import java.security.Security;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.vimukti.accounter.main.LiveServer;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.web.client.core.ClientUser;

public class SubscriptionCreateCompanyServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String view = "/CreateCompany.jsp";
	private boolean isCompanyUpated = false;
	private boolean isPendingStatusRemoved;

	Logger Log = Logger.getLogger(SubscriptionCreateCompanyServlet.class);
	private String COMPANY_CREATED = "101";
	private String COMPANY_UPDATED = "102";
	private String PENDING_STATUS_REMOVED = "103";
	private String COMPANY_NOT_CREATED = "104";

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Log.info("Request received from Subscription Management to create company.");
		BizantraCompany company = null;
		try {
			company = doCreateCompany(request);
			if (company != null) {
				String password = initSessionFactory(company, request);
				Log.info("password:" + password);
				request.setAttribute("message",
						"Company has been created sucessfully");
				dispatchView(request, response, COMPANY_CREATED + ":"
						+ password);
				// JSONObject jsonObject = new JSONObject(request
				// .getParameter("jsonObject"));
				// String content = setBodyContent(jsonObject, company
				// .getCreatedDate(), company.getExpirationDate(),
				// password);
				// String recipientEmail = jsonObject
				// .getString("option_selection8");
				// UsersMailSendar.sendCompanyCreatedMail(company
				// .getCompanyDomainName(), content, recipientEmail);
			} else if (isCompanyUpated) {
				Log.info("Company Updated");
				isCompanyUpated = false;
				dispatchView(request, response, COMPANY_UPDATED);
			} else if (isPendingStatusRemoved) {
				Log.info("Pending Status removed");
				isPendingStatusRemoved = false;
				dispatchView(request, response, PENDING_STATUS_REMOVED);
			} else {
				Log.info("Company isn't created");
				dispatchView(request, response, COMPANY_NOT_CREATED);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// @SuppressWarnings("deprecation")
	// public String setBodyContent(JSONObject jsonObject, Date createdDate,
	// Date expirationDate, String password) {
	// String content = null;
	// try {
	//
	// content =
	// "<div style=\"background: #D8DCE0;\"><div id=\"body\"	style=\"line-height:20px;border: 1px solid #C3D9FF; margin: 15px 50px; padding:20px; font-family: Verdana; font-size: 15px;background: #FFF\">Dear "
	// + jsonObject.getString("option_selection9")
	// + jsonObject.getString("option_selection10")
	// +
	// ", <br /><br />Welcome to Bizantra<br/><br/><div>you have been successfully registered with Bizantra,Your login information is as follows:<div style=\"padding:20px;\"><strong>Your BizantraId: <strong>"
	// + jsonObject.getString("option_selection8")
	// +
	// "</strong></strong><br /><strong>Your password: <strong style=\"color: red\">"
	// + password
	// +
	// "</strong></strong><br /><strong>Company Created Date: <strong style=\"color: red\">"
	// + String.valueOf(createdDate.getDate())
	// + "/"
	// + String.valueOf(createdDate.getMonth())
	// + "/"
	// + String.valueOf(createdDate.getYear() + 1900)
	// +
	// "</strong></strong><br /><strong>Company Expiration Date: <strong style=\"color: red\">"
	// + String.valueOf(expirationDate.getDate())
	// + "/"
	// + String.valueOf(expirationDate.getMonth())
	// + "/"
	// + String.valueOf(expirationDate.getYear() + 1900)
	// +
	// "</strong></strong><br/>Your Role: <strong style=\"color: #765c3b\">Admin</strong><br />Your Company Name: <strong>"
	// + jsonObject.getString("option_selection2")
	// +
	// "</strong><br/>Your Bizantra Application URL: <strong>http://173.203.206.73:8892</strong></div><div>You will be asked to log into your account. <br />Be sure to log in with this BizantraId & password. <br /><p style=\"font-size:11px;padding-top:8px;\">This is an auto generated message. This has been generated for Password Assistence Activity.<br/>We are requesting you to change your password in regular interval and keep secure your account.<br/>For any further assitence or query please contact with Admin.</p><div>Best Regards, <br /><div style=\"font-size:18px\">BIZANTRA Team. <br /><a href=\"http://bizantra.com\" target=\"_blank\">www.bizantra.com</a></div></div></div><div>";
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// return content;
	//
	// }

	private String initSessionFactory(BizantraCompany company,
			HttpServletRequest request) {
		try {
			return init(company, request);
		} catch (Throwable e) {
			throw new ExceptionInInitializerError(e);
		}

	}

	private String init(BizantraCompany company, HttpServletRequest request)
			throws JSONException {
		HibernateUtil.rebuildSessionFactory();
		JSONObject jsonObject = new JSONObject(
				request.getParameter("jsonObject"));
		Session session = HibernateUtil.openSession(
				company.getCompanyDomainName(), true);
		Log.info("Session opened with schema: "
				+ company.getCompanyDomainName());
		// Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		String password = null;
		try {
			session.save(company);
			CollaberIdentity identity = new CollaberIdentity(
					BizantraConstants.SUPER_USER_ROLE, SecureUtils.createID(),
					company.getCompanyDomainName());
			identity.setEncryptedID(Security.getBytes(Security
					.createSpaceEncKey()));
			identity.setEmailID(jsonObject.getString("emailAddress"));
			password = HexUtil.getRandomString();
			identity.setPassword(password);
			// identity.setCompanyName(company.getCompanyDomainName());
			initializeBizantraUserPreference(identity);
			session.save(identity);

			createDefaultSpace(identity, session, false, jsonObject, company);

			createFinanceSpace(identity, session, jsonObject);

			session.saveOrUpdate(identity);
			try {
				tx.commit();
			} catch (RuntimeException re) {
				try {
					Log.info("re");
					tx.rollback();
					re.printStackTrace();
				} catch (RuntimeException re2) {
					Log.info("re2");
					re2.printStackTrace();

				}
			}
			// Create default templates.
			// Create Attachment Directory for company
			File file = new File(ServerConfiguration.getAttachmentsDir(company
					.getCompanyDomainName()));

			if (!file.exists()) {
				file.mkdir();
			}

		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return password;
	}

	private ClientUser createDefaultUser(JSONObject jsonObject,
			String identityID) throws JSONException {

		String subscriptionType = jsonObject.getString("subscriptionType")
				.split(":")[0].trim();

		ClientUser admin = new ClientUser();
		if (!(subscriptionType.equalsIgnoreCase("trial"))) {
			admin.setAddress1(jsonObject.getString("address_state") + ", "
					+ jsonObject.getString("address_street"));
			admin.setCity(jsonObject.getString("address_city"));
			admin.setZipCode(jsonObject.getString("address_zip"));
			admin.setCountry(jsonObject.getString("address_country"));
		}
		admin.id = identityID;
		admin.setFullName(jsonObject.getString("firstName") + " "
				+ jsonObject.getString("lastName"));
		admin.setEmailID(jsonObject.getString("emailAddress"));
		admin.setRole(BizantraConstants.SUPER_USER_ROLE);

		admin.setCompany(jsonObject.getString("domainName"));

		admin.setProvince("");
		admin.setMobile("");
		admin.setWorkphone("");
		admin.setAcessPermissions(BizantraConstants.SUPER_USER_ROLE);
		Log.info("Created admin account");
		return admin;
	}

	private WorkSpace createFinanceSpace(CollaberIdentity identity,
			Session session, JSONObject jsonObject)
			throws NumberFormatException, JSONException {
		ClientWorkspace wsi = new ClientWorkspace();

		wsi.name = CollaberIdentity.FINANCE_WORKSPACE;
		wsi.description = "Finance Workspace description";
		wsi.spaceId = SecureUtils.createID();

		wsi.createdDate = new Date();
		WorkSpace space = new WorkSpace(identity, wsi, true);
		// space.setAccountingType(Integer.valueOf(request
		// .getParameter("companyType")));

		int financeCompanyAccountingType = 0;

		financeCompanyAccountingType = jsonObject.getInt("companyType");

		space.createFinanceTool(financeCompanyAccountingType);
		// Save it
		session.save(space);

		// Add to the list of spaces
		identity.getSpaces().add(space);
		space.setIdentity(identity);
		Log.info("Created Finance workspace");
		return space;
	}

	private WorkSpace createDefaultSpace(CollaberIdentity identity,
			Session session, boolean isPersonal, JSONObject jsonObject,
			BizantraCompany company) throws JSONException {
		// Create a new workspace
		ClientWorkspace wsi = new ClientWorkspace();
		wsi.name = company.getCompanyDomainName();
		wsi.description = "All Full Users Shared Space ";
		wsi.spaceId = SecureUtils.createID();
		wsi.createdDate = new Date();
		WorkSpace space = new WorkSpace(identity, wsi, true);
		UsersTool tool = space.createUserTool(company);
		ClientUser defaultUser = this.createDefaultUser(jsonObject,
				identity.getID());
		tool.createAdminUser(defaultUser);
		// Save it
		Member member = space.getMember(identity.getID());
		member.fullname = defaultUser.fullName;
		session.saveOrUpdate(member);
		session.save(space);

		identity.getSpaces().add(space);
		// session.saveOrUpdate(group);
		space.setIdentity(identity);
		Log.info("Created Default workspace");
		LiveServer.getInstance().addCompany(company.getCompanyDomainName());
		return space;

	}

	private BizantraCompany doCreateCompany(HttpServletRequest request)
			throws JSONException {
		BizantraCompany company = getCompany(request);
		if (company == null) {
			return null;
		}
		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);

		Transaction tx = session.beginTransaction();
		// if (!validation(company)) {
		// return null;
		// }
		try {

			// TODO write sql query for creating database in .xml file
			Query query = session.createSQLQuery("CREATE SCHEMA "
					+ company.getCompanyDomainName());
			query.executeUpdate();
			session.save(company);
			try {
				tx.commit();
			} catch (RuntimeException re) {
				try {
					tx.rollback();
				} catch (RuntimeException re2) {

				}
			}
			return company;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	private BizantraCompany getCompany(HttpServletRequest request)
			throws JSONException {
		Log.info("Processing Create Company : getCompany()");
		JSONObject jsonObject = new JSONObject(
				request.getParameter("jsonObject"));
		// boolean isPending = false;
		String holidayDate = null;
		String holidayMonth = null;
		String business = null;
		String address = null;
		String city = null;
		String country = null;
		String zip = null;
		// if (jsonObject.getString("payment_status").equals("Pending")) {
		// Log.info("isPending True");
		// isPending = true;
		// }
		String subscriptionType = jsonObject.getString("subscriptionType")
				.split(":")[0].trim();
		String name = null;
		// if (option != null && option.equals("companyExtension")) {
		// name = jsonObject.getString("option_selection2");
		// } else {
		name = jsonObject.getString("domainName");
		// }
		String displayName = jsonObject.getString("companyName");
		if (!(subscriptionType.equalsIgnoreCase("trial"))) {

			// if (!jsonObject.getString("option_selection4").equals("null")) {
			// holidayDate = jsonObject.getString("option_selection4");
			// }
			// if (!jsonObject.getString("option_selection5").equals("null")) {
			// holidayMonth = jsonObject.getString("option_selection5");
			// }
			if (!jsonObject.getString("business").equals("null")) {
				business = jsonObject.getString("business");
			}
			if (!jsonObject.getString("address_street").equals("null")) {
				address = jsonObject.getString("address_street");
			}
			if (!jsonObject.getString("address_city").equals("null")) {
				city = jsonObject.getString("address_city");
			}
			if (!jsonObject.getString("address_zip").equals("null")) {
				zip = jsonObject.getString("address_zip");
			}
			if (!jsonObject.getString("address_country").equals("null")) {
				country = jsonObject.getString("address_country");
			}

		}

		// String province = jsonObject.getString("provence");

		int subscriptionPeriod = Integer.parseInt(jsonObject
				.getString("subscriptionPeriod"));
		String subscriptionTime = jsonObject.getString("subscriptionTime");
		int subscriptionTotalUsers = Integer.parseInt(jsonObject
				.getString("subscriptionTotalUsers"));
		int subscriptionTotalLiteUsers = Integer.parseInt(jsonObject
				.getString("subscriptionTotalLiteUsers"));
		long subscriptionTotalSize = Long.parseLong(jsonObject
				.getString("subscriptionTotalSize"));
		DateFormat formatter;
		formatter = new SimpleDateFormat("dd-MMM-yy");
		Date creationDate = null;
		Date expiryDate = null;
		Date deletionDate = null;

		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		Query query = session.getNamedQuery("get.company.by.name");
		query.setString("name", name);
		if (query.list().size() > 0) {
			session.close();
			session = HibernateUtil.openSession(name);
			query = session.getNamedQuery("get.company.by.name");
			query.setString("name", name);

			BizantraCompany company = (BizantraCompany) query.list().get(0);
			try {
				expiryDate = (Date) formatter.parse(jsonObject
						.getString("expirationDate"));
				deletionDate = (Date) formatter.parse(jsonObject
						.getString("deletionDate"));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Log.info("Request came for renewing company..");
			Date today = new Date();
			// Date deletionDate = new Date();
			// deletionDate.setDate(company.getExpirationDate().getDate() + 60);
			if (deletionDate.after(today)) {
				company.renewCompany(expiryDate, deletionDate,
						subscriptionTotalUsers, subscriptionTotalLiteUsers,
						subscriptionTotalSize);
				updateCompany(session, company);
				session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
				company = (BizantraCompany) session
						.getNamedQuery("get.company.by.name")
						.setString("name", name).list().get(0);
				company.renewCompany(expiryDate, deletionDate,
						subscriptionTotalUsers, subscriptionTotalLiteUsers,
						subscriptionTotalSize);
				updateCompany(session, company);
				isCompanyUpated = true;
				Log.info("isCompanyUpdate made true");
				return null;
			}
		}
		session.close();
		BizantraCompany company = new BizantraCompany();
		// company.setId(id);
		try {
			creationDate = (Date) formatter.parse(jsonObject
					.getString("createdDate"));
			expiryDate = (Date) formatter.parse(jsonObject
					.getString("expirationDate"));
			deletionDate = (Date) formatter.parse(jsonObject
					.getString("deletionDate"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		company.setCompanyDomainName(name);
		company.setCompanyDisplayName(displayName);
		company.setAddress(address);
		company.setCity(city);
		company.setCountry(country);
		company.setZip(zip);
		company.setCreatedDate(creationDate);
		company.setExpirationDate(expiryDate);
		company.setDeletionDate(deletionDate);
		company.setTotalNoOfUsers(subscriptionTotalUsers);
		company.setTotalNoOfLiteUsers(subscriptionTotalLiteUsers);
		company.setTotalSize(subscriptionTotalSize);
		company.setDateStyle(2);
		company.setBizantraVersion(jsonObject.getInt("companyType"));
		// company.setProvince(province);
		StringBuilder holidayStartString = new StringBuilder();
		holidayStartString.append(holidayMonth);
		holidayStartString.append(",");
		holidayStartString.append(holidayDate);
		// company.setHolidayStartDate(holidayStartString.toString());
		company.setHolidayStartDate("1,0");
		Log.info(" Returning: " + jsonObject.getString("domainName"));

		company.setSubsType(subscriptionType);
		company.setSubscriberEmail(jsonObject.getString("emailAddress"));

		return company;
	}

	private void updateCompany(Session session, BizantraCompany company) {
		Transaction tx = session.beginTransaction();
		session.update(company);
		tx.commit();
		session.close();
	}

	public void dispatchView(HttpServletRequest request,
			HttpServletResponse response, String view) {
		try {
			response.getWriter().write(view);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean validation(BizantraCompany company) {
		Boolean flag = true;
		String message = "";
		String companyName = company.getCompanyDomainName();
		if (companyName == null || companyName == "") {
			message = "Company name is not valid.";
			flag = false;
		}
		if (BizantraService.isCompanyExits(companyName)) {
			message = "Company with this name already exist.";
			flag = false;
		}
		// if (request.getParameter("nooofusers") == null
		// || request.getParameter("nooofusers") == "") {
		// message = "Company must have a number of User";
		// flag = false;
		// }
		// if (!request.getParameter("nooofusers").matches("[0-9]+")) {
		// message = "Company user number must be a positive numaric number";
		// flag = false;
		// }
		// String emailId = company.getParameter("emailid");
		// if (emailId == null
		// || emailId == ""
		// || !emailId
		// .matches("^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$"))
		// {
		// message = "Invalid Email Id for default user";
		// flag = false;
		// }
		// String password = company.getParameter("password");
		// if (password == null || password == "" || password.length() < 6) {
		// message = "Invalid Password for default user";
		// flag = false;
		// }
		// company.setAttribute("message", message);
		return flag;
	}

	private void initializeBizantraUserPreference(CollaberIdentity identity) {
		BizantraUserPreferences userPreferences = new BizantraUserPreferences();
		userPreferences.setDefaultPreferences();
		identity.setUserPreferences(userPreferences);

	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(view);
	}

}
