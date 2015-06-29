package com.vimukti.accounter.migration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.core.AttendanceOrProductionType;
import com.vimukti.accounter.core.AttendancePayHead;
import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.Budget;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.CashSales;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.ComputionPayHead;
import com.vimukti.accounter.core.CreatableObject;
import com.vimukti.accounter.core.CreditCardCharge;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerCreditMemo;
import com.vimukti.accounter.core.CustomerGroup;
import com.vimukti.accounter.core.CustomerPrePayment;
import com.vimukti.accounter.core.CustomerRefund;
import com.vimukti.accounter.core.Depreciation;
import com.vimukti.accounter.core.Employee;
import com.vimukti.accounter.core.EmployeeGroup;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.FlatRatePayHead;
import com.vimukti.accounter.core.InventoryAssembly;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.ItemGroup;
import com.vimukti.accounter.core.Job;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.Location;
import com.vimukti.accounter.core.MakeDeposit;
import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.core.PayBill;
import com.vimukti.accounter.core.PayStructure;
import com.vimukti.accounter.core.PayTAX;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.PayrollUnit;
import com.vimukti.accounter.core.PriceLevel;
import com.vimukti.accounter.core.PurchaseOrder;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.ReceiveVAT;
import com.vimukti.accounter.core.Reconciliation;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.ShippingTerms;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXGroup;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.TAXReturn;
import com.vimukti.accounter.core.TDSChalanDetail;
import com.vimukti.accounter.core.TDSDeductorMasters;
import com.vimukti.accounter.core.TDSResponsiblePerson;
import com.vimukti.accounter.core.TransferFund;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.UserDefinedPayHead;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.VendorCreditMemo;
import com.vimukti.accounter.core.VendorGroup;
import com.vimukti.accounter.core.VendorPrePayment;
import com.vimukti.accounter.core.Warehouse;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.utils.HibernateUtil;

public class CompanyMigrator {
	public static final String ECGINE_URL = "http://localhost:8080";
	private static final String ECGINE_REST = "/api/v1/objects/";
	private static final String API_KEY = "api_key";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String EMAIL = "email";
	private static final String COMPANY_NAME = "company";
	private static final String USER_NAME = "username";
	private static final String COUNTRY = "country";
	private static final String PASS_WORD = "password";
	private static final String SIGNUP_CREATE_ORG = "/go/accountersignup/";
	private static final String LOG_IN = "/api/login";
	private static final String AUTH_HEADER_NAME = "Authorization";
	private static final String BEARER = "Bearer";
	public static final String USER_ID = "userid";

	private static Logger log = Logger.getLogger(CompanyMigrator.class);

	private Company company;

	private HttpClient client;
	private String apiKey;
	private String password;

	public CompanyMigrator(Company company, String password) {
		this.company = company;
		this.password = password;
		this.client = HttpClientBuilder.create().build();
	}

	public void migrate() throws HttpException, IOException, JSONException {
		log.info("***Migrating Company  :" + company.getTradingName()
				+ " @@ID : " + company.getId());
		// Create context
		MigratorContext context = new MigratorContext();
		context.setCompany(company);
		// Organization
		User user = company.getCreatedBy();
		long adminId = signup(user);
		context.setAdmin("Admin", adminId);
		// / login
		apiKey = login("test1@vimukti.com", this.password);

		// Users Migration
		// migrateUsers(emails, context);

		// Measurements
		Map<Long, Long> migratedObjects = migrateObjects("Measurement",
				Measurement.class, new MeasurementMigrator(), context);
		context.put("Measurement", migratedObjects);
		// Accounts
		migratedObjects = migrateObjects("Account", Account.class,
				new AccountMigrator(), context);
		context.put("Account", migratedObjects);
		// BankAccount
		migratedObjects = migrateObjects("BankAccount", BankAccount.class,
				new BankAccountMigrator(), context);
		context.put("BankAccount", migratedObjects);
		// salesPersons
		migratedObjects = migrateObjects("SalesPerson", SalesPerson.class,
				new SalesPersonMigrator(), context);
		context.put("SalesPerson", migratedObjects);
		// // Warehouse
		migratedObjects = migrateObjects("Warehouse", Warehouse.class,
				new WarehouseMigrator(), context);
		context.put("Warehouse", migratedObjects);
		// paymentTerms
		migratedObjects = migrateObjects("PaymentTerm", PaymentTerms.class,
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
		// locations
		migratedObjects = migrateObjects("Location", Location.class,
				new LocationMigrator(), context);
		context.put("Location", migratedObjects);
		// AccounterClasses
		migratedObjects = migrateObjects("AccountClass", AccounterClass.class,
				new AccounterClassMigrator(), context);
		context.put("AccounterClass", migratedObjects);
		// Item groups
		migratedObjects = migrateObjects("ItemGroup", ItemGroup.class,
				new ItemGroupMigrator(), context);
		context.put("ItemGroup", migratedObjects);
		// taxAgencies
		migratedObjects = migrateObjects("TaxAgency", TAXAgency.class,
				new TaxAgencyMigrator(), context);
		context.put("TaxAgency", migratedObjects);
		// taxitems
		migratedObjects = migrateObjects("TaxItem", TAXItem.class,
				new TaxItemMigrator(), context);
		context.put("Tax", migratedObjects);
		// taxitems
		migratedObjects = migrateObjects("TaxGroup", TAXGroup.class,
				new TAXGroupMigrator(), context);
		context.put("Tax", migratedObjects);
		// taxCodes
		migratedObjects = migrateObjects("TaxCode", TAXCode.class,
				new TAXCodeMigrator(), context);
		context.put("TaxCode", migratedObjects);
		// Customers
		migratedObjects = migrateObjects("Customer", Customer.class,
				new CustomerMigrator(), context);
		context.put("Customer", migratedObjects);
		// CustomerPrepayment
		migratedObjects = migrateObjects("CustomerPrepayment",
				CustomerPrePayment.class, new CustomerPrepaymentMigrator(),
				context);
		context.put("CustomerPrepayment", migratedObjects);
		// Vendor Groups
		migratedObjects = migrateObjects("VendorGroup", VendorGroup.class,
				new VendorGroupMigrator(), context);
		context.put("VendorGroup", migratedObjects);
		// Jobs
		migratedObjects = migrateObjects("Project", Job.class,
				new JobMigrator(), context);
		context.put("Job", migratedObjects);
		// JournalEntries
		migratedObjects = migrateObjects("JournalEntry", JournalEntry.class,
				new JournalEntryMigrator(), context);
		context.put("JournalEntry", migratedObjects);
		// Items
		migratedObjects = migrateObjects("ServiceItem", Item.class,
				new ServiceItemMigrator(), context);
		context.put("Item", migratedObjects);
		// Items
		migratedObjects = migrateObjects("ProductItem", Item.class,
				new ProductItemMigrator(), context);
		context.put("Item", migratedObjects);
		// Items
		migratedObjects = migrateObjects("InventoryItem", Item.class,
				new InventoryItemMigrator(), context);
		context.put("Item", migratedObjects);
		// Items
		migratedObjects = migrateObjects("InventoryAssembly",
				InventoryAssembly.class, new InventoryAssemblyMigrator(),
				context);
		context.put("Item", migratedObjects);
		// SalesQuotation
		migratedObjects = migrateObjects("SalesQuotation", Estimate.class,
				new SalesQuotationMigrator(), context);
		context.put("SalesQuotation", migratedObjects);
		// SalesOrder
		migratedObjects = migrateObjects("SalesOrder", Estimate.class,
				new SalesOrderMigrator(), context);
		context.put("SalesOrder", migratedObjects);
		// Credits
		migratedObjects = migrateObjects("Credit", Estimate.class,
				new CreditsMigrator(), context);
		context.put("Credit", migratedObjects);
		// Charges
		migratedObjects = migrateObjects("Charge", Estimate.class,
				new ChargesMigrator(), context);
		context.put("Charge", migratedObjects);
		// Invoice
		migratedObjects = migrateObjects("Invoice", Invoice.class,
				new InvoiceMigrator(), context);
		context.put("Invoice", migratedObjects);
		// ReceivePayment
		migratedObjects = migrateObjects("ReceivePayment",
				ReceivePayment.class, new ReceivePaymentMigrator(), context);
		context.put("ReceivePayment", migratedObjects);
		// CashSale
		migratedObjects = migrateObjects("CashSale", CashSales.class,
				new CashSaleMigrator(), context);
		context.put("CashSale", migratedObjects);
		// CustomerCreditMemo
		migratedObjects = migrateObjects("CreditMemo",
				CustomerCreditMemo.class, new CreditMemoMigrator(), context);
		context.put("CreditMemo", migratedObjects);
		// Vendor
		migratedObjects = migrateObjects("Vendor", Vendor.class,
				new VendorMigrator(), context);
		context.put("Vendor", migratedObjects);
		// VendorPrepayment
		migratedObjects = migrateObjects("VendorPrepayment",
				VendorPrePayment.class, new VendorPrepaymentMigrator(), context);
		context.put("VendorPrepayment", migratedObjects);
		// PurchaseOrder
		migratedObjects = migrateObjects("PurchaseOrder", PurchaseOrder.class,
				new PurchaseOrderMigrator(), context);
		context.put("PurchaseOrder", migratedObjects);
		// CashPurchase
		migratedObjects = migrateObjects("CashPurchase", CashPurchase.class,
				new CashPurchaseMigrator(), context);
		context.put("CashPurchase", migratedObjects);
		// Credit Card Expense(Record Expense Type)
		migratedObjects = migrateObjects("PurchaseExpense",
				CreditCardCharge.class, new CreditCardExpenseMigrator(),
				context);
		context.put("PurchaseExpense", migratedObjects);
		// CashExpense(Record Expense Type)
		migratedObjects = migrateObjects("PurchaseExpense", CashPurchase.class,
				new CashExpenseMigrator(), context);
		context.put("PurchaseExpense", migratedObjects);
		// EnterBill
		migratedObjects = migrateObjects("EnterBill", EnterBill.class,
				new EnterBillMigrator(), context);
		context.put("EnterBill", migratedObjects);
		// DebitNote
		migratedObjects = migrateObjects("DebitNote", VendorCreditMemo.class,
				new DebitNoteMigrator(), context);
		context.put("DebitNote", migratedObjects);
		// PayBill
		migratedObjects = migrateObjects("PayBill", PayBill.class,
				new PayBillMigrator(), context);
		context.put("PayBill", migratedObjects);
		// CustomerRefund
		migratedObjects = migrateObjects("CustomerRefund",
				CustomerRefund.class, new CustomerRefundMigrator(), context);
		context.put("CustomerRefund", migratedObjects);
		// MakeDeposit
		migratedObjects = migrateObjects("MakeDeposit", MakeDeposit.class,
				new MakeDepositMigrator(), context);
		context.put("MakeDeposit", migratedObjects);
		// TransferFund
		migratedObjects = migrateObjects("TransferFund", TransferFund.class,
				new TransferFundMigrator(), context);
		context.put("TransferFund", migratedObjects);
		// WriteCheck
		migratedObjects = migrateObjects("WriteCheck", WriteCheck.class,
				new WriteCheckMigrator(), context);
		context.put("WriteCheck", migratedObjects);
		// Budget
		migratedObjects = migrateObjects("Budget", Budget.class,
				new BudgetMigrator(), context);
		context.put("WriteCheck", migratedObjects);
		// Depreciation
		migratedObjects = migrateObjects("Depreciation", Depreciation.class,
				new DepreciationMigrator(), context);
		context.put("Depreciation", migratedObjects);
		// PayrollUnit
		migratedObjects = migrateObjects("PayRollUnit", PayrollUnit.class,
				new PayrollUnitMigrator(), context);
		context.put("PayrollUnit", migratedObjects);
		// AttendanceOrProductionType
		migratedObjects = migrateObjects("AttendanceOrProductionType",
				AttendanceOrProductionType.class,
				new AttendanceOrProductionTypeMigrator(), context);
		context.put("AttendanceOrProductionType", migratedObjects);
		// PayHead
		migratedObjects = migrateObjects("PayHead", AttendancePayHead.class,
				new AttendancePayHeadMigrator(), context);
		context.put("PayHead", migratedObjects);
		migratedObjects = migrateObjects("PayHead", ComputionPayHead.class,
				new ComputionPayHeadMigrator(), context);
		context.put("PayHead", migratedObjects);
		migratedObjects = migrateObjects("PayHead", FlatRatePayHead.class,
				new FlatRatePayHeadMigrator(), context);
		context.put("PayHead", migratedObjects);
		migratedObjects = migrateObjects("PayHead", UserDefinedPayHead.class,
				new UserDefinedPayHeadMigrator(), context);
		context.put("PayHead", migratedObjects);
		// EmployeeGroup
		migratedObjects = migrateObjects("EmployeeGroup", EmployeeGroup.class,
				new EmployeeGroupMigrator(), context);
		context.put("EmployeeGroup", migratedObjects);
		// Employee
		migratedObjects = migrateObjects("Employee", Employee.class,
				new EmployeeMigrator(), context);
		context.put("Employee", migratedObjects);
		// PayStructure
		migratedObjects = migrateObjects("PayStructure", PayStructure.class,
				new PayStructureMigrator(), context);
		context.put("PayStructure", migratedObjects);
		// Reconciliation
		migratedObjects = migrateObjects("Reconciliation",
				Reconciliation.class, new ReconciliationMigrator(), context);
		context.put("Reconciliation", migratedObjects);
		// TDSDeductorMasters
		migratedObjects = migrateObjects("TDSDeductorMasters",
				TDSDeductorMasters.class, new TDSDeductorMastersMigrator(),
				context);
		context.put("TDSDeductorMasters", migratedObjects);
		// TDSResponsiblePerson
		migratedObjects = migrateObjects("TDSResponsiblePerson",
				TDSResponsiblePerson.class, new TDSResponsiblePersonMigrator(),
				context);
		context.put("TDSResponsiblePerson", migratedObjects);
		// TaxRefund
		migratedObjects = migrateObjects("TaxRefund", ReceiveVAT.class,
				new TaxRefundMigrator(), context);
		context.put("TaxRefund", migratedObjects);
		// TDSChalan
		migratedObjects = migrateObjects("TdsChallan", TDSChalanDetail.class,
				new TdsChallanMigrator(), context);
		context.put("TdsChallan", migratedObjects);
		// PayTAX
		migratedObjects = migrateObjects("PayTAX", PayTAX.class,
				new PayTaxMigrator(), context);
		context.put("PayTAX", migratedObjects);
		// TDSChalan
		migratedObjects = migrateObjects("FileTax", TAXReturn.class,
				new FileTaxMigrator(), context);
		context.put("FileTax", migratedObjects);

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

	@SuppressWarnings("unchecked")
	private <T extends CreatableObject> Map<Long, Long> migrateObjects(
			String identity, Class<T> clazz, IMigrator<T> migrator,
			MigratorContext context) throws JSONException, HttpException,
			IOException {
		List<Long> ids = new ArrayList<Long>();
		Session session = HibernateUtil.getCurrentSession();
		JSONArray objectArray = new JSONArray();
		Criteria criteria = session.createCriteria(clazz, "obj");
		migrator.addRestrictions(criteria);
		List<T> objects = criteria.add(Restrictions.eq("company", company))
				.list();
		if (objects.isEmpty()) {
			return new HashMap<Long, Long>();
		}
		// Map<fieldName-Identity,List<OldId>
		Map<String, List<Long>> accounterMap = new HashMap<String, List<Long>>();
		context.setChildrenMap(accounterMap);

		for (T obj : objects) {
			JSONObject migrate = migrator.migrate(obj, context);
			if (migrate != null) {
				objectArray.put(migrate);
				ids.add(obj.getID());
			}
		}
		// Send Request TO REST API
		Map<Long, Long> newAndOldIds = new HashMap<Long, Long>();
		HttpPost post = new HttpPost(ECGINE_URL + ECGINE_REST + identity);
		addAuthenticationParameters(post, this.apiKey);
		post.setHeader("Content-type", "application/json");
		post.setEntity(new StringEntity(objectArray.toString()));
		HttpResponse response = client.execute(post);
		StatusLine status = response.getStatusLine();
		HttpEntity entity = response.getEntity();
		if (status.getStatusCode() != HttpStatus.SC_OK) {
			if (entity != null) {
				EntityUtils.consume(entity);
			}
			return newAndOldIds;
			// throw new RuntimeException(status.toString());
		}
		String content = IOUtils.toString(entity.getContent());
		JSONArray array = new JSONArray(content);
		Map<String, List<Long>> ecgineMap = new HashMap<String, List<Long>>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject json = array.getJSONObject(i);
			newAndOldIds.put(ids.get(i), json.getLong("id"));
			JSONObject jsonObject = json.getJSONObject("object");
			createEcgineChildrenMap(accounterMap, ecgineMap, jsonObject);
			log.info("Migrated " + identity + "  Accounter ID :" + ids.get(i)
					+ " Ecgine ID :" + json.getLong("id"));
		}
		putChildrenInContext(accounterMap, ecgineMap, context);
		return newAndOldIds;
	}

	private void putChildrenInContext(Map<String, List<Long>> accounterMap,
			Map<String, List<Long>> ecgineMap, MigratorContext context) {
		for (Entry<String, List<Long>> e : accounterMap.entrySet()) {
			String key = e.getKey();
			List<Long> accList = e.getValue();
			List<Long> ecgineList = ecgineMap.get(key);
			String identity = key.substring(key.indexOf("-") + 1);
			context.put(identity, makeMap(accList, ecgineList));
		}
	}

	private Map<Long, Long> makeMap(List<Long> accList, List<Long> ecgineList) {
		Map<Long, Long> map = new HashMap<Long, Long>();
		for (int i = 0; i < accList.size(); i++) {
			map.put(accList.get(i), ecgineList.get(i));
		}
		return map;
	}

	private void createEcgineChildrenMap(Map<String, List<Long>> accounterMap,
			Map<String, List<Long>> ecgineMap, JSONObject jsonObject)
			throws JSONException {
		for (Entry<String, List<Long>> e : accounterMap.entrySet()) {
			String key = e.getKey();
			List<Long> list = ecgineMap.get(key);
			if (list == null) {
				list = new ArrayList<Long>();
				ecgineMap.put(key, list);
			}
			String prop = key.substring(0, key.indexOf("-"));
			JSONObject obj = jsonObject.getJSONObject(prop);
			JSONArray jsonArray = obj.getJSONArray("value");
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(jsonArray.getJSONObject(i).getLong("id"));
			}
		}
	}

	private String login(String userName, String password)
			throws JSONException, IOException {

		HttpPost post = new HttpPost(ECGINE_URL + LOG_IN);
		List<NameValuePair> postParameters = new ArrayList<>();
		postParameters.add(new BasicNameValuePair(USER_NAME, userName));
		postParameters.add(new BasicNameValuePair(PASS_WORD, password));
		post.setEntity(new UrlEncodedFormEntity(postParameters));

		HttpResponse response = client.execute(post);

		StatusLine status = response.getStatusLine();
		if (status.getStatusCode() != HttpStatus.SC_OK) {
			throw new RuntimeException(status.toString());
		}

		HttpEntity entity = response.getEntity();
		String responseContent = IOUtils.toString(entity.getContent());

		JSONObject responseResult = new JSONObject(responseContent);
		log.info("Login success for: " + userName);
		return responseResult.getString(API_KEY);
	}

	private long signup(User user) throws HttpException, IOException,
			JSONException {
		HttpPost post = new HttpPost(ECGINE_URL + SIGNUP_CREATE_ORG);
		Client accClient = user.getClient();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(EMAIL, accClient.getEmailId()));
		params.add(new BasicNameValuePair(FIRST_NAME, accClient.getFirstName()));
		params.add(new BasicNameValuePair(LAST_NAME, accClient.getLastName()));
		params.add(new BasicNameValuePair(COUNTRY, accClient.getCountry()));
		params.add(new BasicNameValuePair(COMPANY_NAME, user.getCompany()
				.getTradingName()));
		params.add(new BasicNameValuePair(PASS_WORD, this.password));
		post.setEntity(new UrlEncodedFormEntity(params));
		HttpResponse response = client.execute(post);
		StatusLine status = response.getStatusLine();
		if (status.getStatusCode() != HttpStatus.SC_OK) {
			throw new RuntimeException(status.toString());
		}
		log.info("***Signup Sucessfully with " + accClient.getEmailId());
		HttpEntity content = response.getEntity();
		String responseContent = IOUtils.toString(content.getContent());
		JSONObject responseResult = new JSONObject(responseContent);
		return responseResult.getLong(USER_ID);
	}

	private void addAuthenticationParameters(HttpRequest executeMethod,
			String apiKey) {
		String encodedKey = Base64.getEncoder().encodeToString(
				apiKey.getBytes());
		executeMethod.addHeader(AUTH_HEADER_NAME, BEARER + " " + encodedKey);
	}
}
