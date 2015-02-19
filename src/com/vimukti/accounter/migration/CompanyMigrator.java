package com.vimukti.accounter.migration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CreatableObject;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerGroup;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.ItemGroup;
import com.vimukti.accounter.core.Job;
import com.vimukti.accounter.core.Location;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.PriceLevel;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.ShippingTerms;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXGroup;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.VendorGroup;
import com.vimukti.accounter.utils.HibernateUtil;

public class CompanyMigrator {
	public static final String ECGINE_URL = "https://www.ecgine.com";
	private static final String ECGINE_REST = "/api/v1/objects/";
	private static final String API_KEY = "api-key";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String EMAIL = "email";
	private static final String PHONE_NUMBER = "phoneNumber";
	private static final String COMPANY_NAME = "companyName";
	private static final String PASSWORD = "password";
	private static final String CONFIRM_PASSWORD = "confirmPassword";
	private static final String USER_NAME = "userName";
	private static final String PASS_WORD = "password";
	private static final String SIGN_UP = "/api/signup";
	private static final String LOG_IN = "/api/login";
	private static final String AUTHORIZATION_KEY = "authorization-barre";

	private static Logger log = Logger.getLogger(CompanyMigrator.class);

	private Company company;

	private HttpClient client;
	private String loginKey;

	public CompanyMigrator(Company company) {
		this.company = company;
	}

	public void migrate(List<String> emails, PickListTypeContext typeContext)
			throws HttpException, IOException, JSONException {
		log.info("***Migrating Company Id: " + company.getId());
		// Organization
		User user = company.getCreatedBy();
		createOrganization(company.getCreatedBy());
		// / login
		loginKey = login(user.getClient().getEmailId(), user.getClient()
				.getPassword());
		MigratorContext context = new MigratorContext();
		context.setCompany(company);
		if (typeContext.isEmpty()) {
			getPicklistObjects(typeContext);
			context.setPickListContext(typeContext);
		}
		// Users Migration
		migrateUsers(emails, context);
		// Accounts
		Map<Long, Long> migratedObjects = migrateObjects("Account",
				Account.class, new AccountMigrator(), context);
		context.put("Account", migratedObjects);
		// salesPersons
		migratedObjects = migrateObjects("SalesPerson", SalesPerson.class,
				new SalesPersonMigrator(), context);
		context.put("SalesPerson", migratedObjects);
		// taxAgencies
		migratedObjects = migrateObjects("TAXAgency", TAXAgency.class,
				new TaxAgencyMigrator(), context);
		context.put("TAXAgency", migratedObjects);
		// taxitems
		migratedObjects = migrateObjects("TAXItem", TAXItem.class,
				new TaxItemMigrator(), context);
		context.put("TAXItem", migratedObjects);
		// taxitems
		migratedObjects = migrateObjects("TAXGroup", TAXGroup.class,
				new TAXGroupMigrator(), context);
		context.put("TAXGroup", migratedObjects);
		// taxCodes
		migratedObjects = migrateObjects("TAXCode", TAXCode.class,
				new TAXCodeMigrator(), context);
		context.put("TAXCode", migratedObjects);
		// Currencies
		migratedObjects = migrateObjects("Currency", Currency.class,
				new CurrencyMigrator(), context);
		context.put("Currency", migratedObjects);
		// paymentTerms
		migratedObjects = migrateObjects("PaymentTerms", PaymentTerms.class,
				new PaymentTermsMigrator(), context);
		context.put("PaymentTerms", migratedObjects);
		// Customer Groups
		migratedObjects = migrateObjects("CustomerGroup", CustomerGroup.class,
				new CustomerGroupMigrator(), context);
		context.put("CustomerGroup", migratedObjects);
		// Shipping Methods
		migratedObjects = migrateObjects("ShippingMethod",
				ShippingMethod.class, new ShippingMethodMigrator(), context);
		context.put("ShippingMethod", migratedObjects);

		// Shipping Methods
		migratedObjects = migrateObjects("ShippingTerms", ShippingTerms.class,
				new ShippingTermsMigrator(), context);
		context.put("ShippingTerms", migratedObjects);
		// PriceLevels
		migratedObjects = migrateObjects("PriceLevel", PriceLevel.class,
				new PriceLevelMigrator(), context);
		context.put("PriceLevel", migratedObjects);
		// Customers
		migratedObjects = migrateObjects("Customer", Customer.class,
				new CustomerMigrator(), context);
		context.put("Customer", migratedObjects);
		// Vendor Groups
		migratedObjects = migrateObjects("VendorGroup", VendorGroup.class,
				new VendorGroupMigrator(), context);
		context.put("VendorGroup", migratedObjects);
		// locations
		migratedObjects = migrateObjects("Location", Location.class,
				new LocationMigrator(), context);
		context.put("Location", migratedObjects);
		// AccounterClasses
		migratedObjects = migrateObjects("AccounterClass",
				AccounterClass.class, new AccounterClassMigrator(), context);
		context.put("AccounterClass", migratedObjects);
		// Jobs
		migratedObjects = migrateObjects("Job", Job.class, new JobMigrator(),
				context);
		context.put("Job", migratedObjects);
		// Item groups
		migratedObjects = migrateObjects("ItemGroup", ItemGroup.class,
				new ItemGroupMigrator(), context);
		context.put("ItemGroup", migratedObjects);
		// Items
		migratedObjects = migrateObjects("Item", Item.class,
				new ItemMigrator(), context);
		context.put("Item", migratedObjects);

		// TODO Transactions

	}

	@SuppressWarnings("unchecked")
	private void migrateUsers(List<String> migratedEmails,
			MigratorContext context) throws HttpException, JSONException,
			IOException {
		Session session = HibernateUtil.getCurrentSession();
		List<User> users = session.createCriteria(User.class, "user")
				.add(Restrictions.eq("company", company.getId())).list();
		for (User user : users) {
			String emailId = user.getClient().getEmailId();
			if (migratedEmails.contains(emailId)) {
				emailId = migrateEmail(1, emailId, migratedEmails);
			}
			migratedEmails.add(emailId);
			Map<Long, Long> migrateObjects = migrateObjects("User", User.class,
					new UserMigrator(), context);
			context.put("User", migrateObjects);
		}
	}

	private String migrateEmail(int count, String emailId,
			List<String> migratedEmails) {
		String migratedEmail = emailId + count;
		if (migratedEmails.contains(migratedEmail)) {
			return migrateEmail(++count, migratedEmail, migratedEmails);
		}
		return migratedEmail;
	}

	private void getPicklistObjects(PickListTypeContext typeContext)
			throws HttpException, IOException, JSONException {
		String[] picklists = new String[] { "AccountBaseType", "AccountType",
				"CashFlowCategory", "ClassTrackingType", "DayOfWeek",
				"DepreciationFor", "DepreciationMethod", "DepreciationStatus",
				"DiscountInTransactions", "EmailPreference",
				"FixedAssetStatus", "Gender", "IntervelType", "Month",
				"PaymentMethod", "PaymentStatus", "PriorityType",
				"RecuringType", "RecurrenceInstance", "RelationshipType",
				"TaxItemInTransactions", "TransactionStatus",
				"TransactionType", "InventoryScheme", "InvoiceStatus",
				"ItemType", "ProjectStatus", "QuotationType",
				"SalesOrderStatus", "AttendanceProductionType",
				"AttendanceProductionTypePeriod", "CalculationType",
				"ComputationSlabType", "EmployeeAttendanceManagementItemType",
				"NamePrefix", "PayeeType", "PayEmployeeType",
				"PayHeadCalculationPeriod", "PayHeadCalculationType",
				"PayHeadComputeOn", "PayHeadEarningOrDeductionOn",
				"PayHeadFormulaFunctionType", "PayHeadPerDayCalculationBasis",
				"PayHeadType", "PayRunType", "PayStructureType", "SlabType",
				"TransportationMode", "DeductorMastersStatus", "DeductorType",
				"DepreciationPeriods", "FormType", "MinistryDeptName",
				"NatureOfPayment", "RetutnType", "TAXAccountType",
				"TaxAdjustmentType", "BillStatus", "DiscountInTransactions",
				"PurchaseOrderStatus" };
		for (String identity : picklists) {
			get(identity, typeContext);
		}
	}

	private void get(String identity, PickListTypeContext typeContext)
			throws HttpException, IOException, JSONException {
		HttpMethod request = new GetMethod(ECGINE_URL + ECGINE_REST + identity);
		request.addRequestHeader(AUTHORIZATION_KEY, loginKey);
		int statusCode = client.executeMethod(request);
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException(HttpStatus.getStatusText(statusCode));
		}
		JSONArray array = new JSONArray(request.getResponseBodyAsString());
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
			String instanceIdentity = object.getString("identity");
			long instanceId = object.getLong("id");
			typeContext.put(identity, instanceIdentity, instanceId);
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends CreatableObject> Map<Long, Long> migrateObjects(
			String identity, Class<T> clazz, IMigrator<T> migrator,
			MigratorContext context) throws JSONException, HttpException,
			IOException {
		List<Long> ids = new ArrayList<Long>();
		Session session = HibernateUtil.getCurrentSession();
		JSONArray accountsJSON = new JSONArray();
		List<T> accounts = session.createCriteria(clazz, "obj")
				.add(Restrictions.eq("company", company.getId())).list();
		for (T obj : accounts) {
			accountsJSON.put(migrator.migrate(obj, context));
			ids.add(obj.getID());
		}
		// Send Request TO REST API
		Map<Long, Long> newAndOldIds = new HashMap<Long, Long>();
		PostMethod post = new PostMethod(ECGINE_URL + ECGINE_REST + identity);
		post.addRequestHeader(AUTHORIZATION_KEY, loginKey);
		StringRequestEntity requestEntity = new StringRequestEntity(
				accountsJSON.toString(), "application/json", "UTF-8");
		post.setRequestEntity(requestEntity);
		int statusCode = client.executeMethod(post);
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException(HttpStatus.getStatusText(statusCode));
		}
		JSONArray array = new JSONArray(post.getResponseBodyAsString());
		for (int i = 0; i < array.length(); i++) {
			JSONObject json = array.getJSONObject(i);
			newAndOldIds.put(ids.get(i), json.getLong("id"));
		}
		return newAndOldIds;
	}

	private String login(String userName, String password)
			throws JSONException, IOException {
		HttpMethod executeMethod = new PostMethod(ECGINE_URL + LOG_IN);
		HttpMethodParams params = new HttpMethodParams();
		params.setParameter(USER_NAME, userName);
		params.setParameter(PASS_WORD, password);
		int statusCode = client.executeMethod(executeMethod);
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException(HttpStatus.getStatusText(statusCode));
		}
		JSONObject responseResult = new JSONObject(
				executeMethod.getResponseBodyAsString());
		return responseResult.getString(API_KEY);
	}

	private String createOrganization(User user) throws HttpException,
			IOException, JSONException {
		// TODO
		return null;
	}
}