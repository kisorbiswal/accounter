package com.vimukti.accounter.migration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

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
import org.apache.http.client.methods.HttpGet;
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
import com.vimukti.accounter.core.BuildAssembly;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.CashSales;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.ComputionPayHead;
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
import com.vimukti.accounter.core.FixedAsset;
import com.vimukti.accounter.core.FlatRatePayHead;
import com.vimukti.accounter.core.IAccounterServerCore;
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
	private static final String API_KEY = "apikey";
	private static final String FIRST_NAME = "firstName";
	public static final String API_BASE_URL = "/api/v1";
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
	public static final String ECGINE_REST_PACKAGE_INSTALL = "/api/v1/package/install";
	private static final String PKG_NAME = "pkgName";
	private static final String PKG_VERSION = "pkgVersion";
	private static final long DELAY_BETWEEN_TASKS = 10 * 1000;
	private static final String ECGINE_REST_JOB_STATUS = "/api/v1/job/status";
	private static final String JOBID = "jobId";
	public static final String ORGANIZATION_ID = "orgid";
	public static final String ECGINE_LIST = "/lists/";
	private static final String CURRENCY = "currency";
	private MigratorContext context;
	public static final long COMMON_SETTINGS_OLD_ID = 1;
	public static final long FEATURES_OLD_ID = 2;
	public static final long CUSTOMER_AND_SALES_SETTINGS_OLD_ID = 3;
	public static final String COMMON_SETTINGS = "CommonSettings";
	public static final String FEATURES = "Features";
	public static final String CUSTOMER_AND_SALES_SETTINGS = "CustomerAndSalesSettings";
	private static Logger log = Logger.getLogger(CompanyMigrator.class);

	private Company company;

	private HttpClient client;
	private String apiKey;
	private Map<String, JSONObject> inactiveObjects = new HashMap<String, JSONObject>();

	public CompanyMigrator(Company company) {
		this.company = company;
		this.client = HttpClientBuilder.create().build();
	}

	public void migrate() throws Exception {
		log.info("***Migrating Company  :" + company.getTradingName()
				+ " @@ID : " + company.getId());
		// Create context
		context = new MigratorContext();
		context.setCompany(company);
		// Organization
		User user = company.getCreatedBy();
		JSONObject json = signup(user);
		context.setAdmin("Admin", json.getLong(USER_ID));
		// Users Migration
		// migrateUsers(emails, context);
		apiKey = json.getString(API_KEY);
		installPackage("accounter", "accounter10");

	}

	void migrateAllObjects() throws HttpException, JSONException, IOException {
		// DefaultAccounts
		Map<Long, Long> migratedObjects = migrateObjects("Account",
				Account.class, new DefaultAccountsMigrator(), context);
		context.put("Account", migratedObjects);
		// CommonSettings
		migratedObjects = migrateObjects(COMMON_SETTINGS,
				CompanyPreferences.class, new DefaultCommonSettingsMigrator(),
				context);
		// MultiCurrencyMigrator
		if (company.getPreferences().isEnabledMultiCurrency()) {
			migratedObjects = migrateObjects(COMMON_SETTINGS,
					CompanyPreferences.class,
					new AccountingCurrenciesMigrator(), context);
		}
		// Measurements
		migratedObjects = migrateObjects("Measurement", Measurement.class,
				new MeasurementMigrator(), context);
		context.put("Measurement", migratedObjects);
		// Accounts
		migratedObjects = migrateObjects("Account", Account.class,
				new AccountMigrator(), context);
		context.put("Account", migratedObjects);
		// BankAccount
		migratedObjects = migrateObjects("BankAccount", BankAccount.class,
				new BankAccountMigrator(), context);
		context.put("Account", migratedObjects);
		// Warehouse
		migratedObjects = migrateObjects("Warehouse", Warehouse.class,
				new WarehouseMigrator(), context);
		context.put("Warehouse", migratedObjects);
		// paymentTerms
		migratedObjects = migrateObjects("PaymentTerm", PaymentTerms.class,
				new PaymentTermsMigrator(), context);
		context.put("PaymentTerms", migratedObjects);
		// taxAgencies
		migratedObjects = migrateObjects("TaxAgency", TAXAgency.class,
				new TaxAgencyMigrator(), context);
		context.put("TaxAgency", migratedObjects);

		// salesPersons
		migratedObjects = migrateObjects("SalesPerson", SalesPerson.class,
				new SalesPersonMigrator(), context);
		context.put("SalesPerson", migratedObjects);
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
		// taxitems
		migratedObjects = migrateObjects("TaxItem", TAXItem.class,
				new TaxItemMigrator(), context);
		context.put("Tax", migratedObjects);
		// TaxGroups
		migratedObjects = migrateObjects("TaxGroup", TAXGroup.class,
				new TAXGroupMigrator(), context);
		context.put("Tax", migratedObjects);
		// taxCodes
		migratedObjects = migrateObjects("TaxCode", TAXCode.class,
				new TAXCodeMigrator(), context);
		context.put("TaxCode", migratedObjects);

		// CompanyDefatultTaxCodeMigration
		migratedObjects = migrateObjects(COMMON_SETTINGS,
				CompanyPreferences.class, new CompanyDefatultTaxCodeMigrator(),
				context);

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
		// CustomerAndSalesSettingsMigrator
		migratedObjects = migrateObjects(CUSTOMER_AND_SALES_SETTINGS,
				CompanyPreferences.class,
				new DefaultCustomerAndSalesSettingsMigrator(), context);
		// FeaturesMigrator
		migratedObjects = migrateObjects(FEATURES, CompanyPreferences.class,
				new DefaultFeaturesMigrator(), context);
		// Items
		migratedObjects = migrateObjects("InventoryItem", Item.class,
				new InventoryItemMigrator(), context);
		context.put("Item", migratedObjects);
		// Items
		migratedObjects = migrateObjects("InventoryAssembly",
				InventoryAssembly.class, new InventoryAssemblyMigrator(),
				context);
		context.put("Item", migratedObjects);
		// Build Assembly
		migratedObjects = migrateObjects("BuildAssembly", BuildAssembly.class,
				new BuildAssemblyMigrator(), context);
		context.put("BuildAssembly", migratedObjects);
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
		// // Charges this is not using in new accounter
		// migratedObjects = migrateObjects("Charge", Estimate.class,
		// new ChargesMigrator(), context,null);
		// context.put("Charge", migratedObjects);
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
		// migratedObjects = migrateObjects("Budget", Budget.class,
		// new BudgetMigrator(), context,null);
		// context.put("WriteCheck", migratedObjects);
		// FixedAsset
		migratedObjects = migrateObjects("FixedAsset", FixedAsset.class,
				new FixedAssetMigrator(), context);
		context.put("FixedAsset", migratedObjects);
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
		// // PayRunMigrator
		// migratedObjects = migrateObjects("PayRun", PayRun.class,
		// new PayRunMigrator(), context,null);
		// context.put("PayRun", migratedObjects);
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
		migratedObjects = migrateObjects("PermissionSet", User.class,
				new PermissionSetMigrator(), context);
		context.put("PermissionSet", migratedObjects);
		migratedObjects = migrateObjects("Profile", User.class,
				new ProfileMigrator(), context);
		context.put("Profile", migratedObjects);
		migratedObjects = migrateObjects("User", User.class,
				new UserMigrator(), context);
		context.put("User", migratedObjects);
		// Updating all inactive objects
		Set<String> keySet = inactiveObjects.keySet();
		for (String key : keySet) {
			setRequestToRestApi(key, context,
					new JSONArray().put(inactiveObjects.get(key)), null);
		}
		// CommonSettings
		migratedObjects = migrateObjects(COMMON_SETTINGS,
				CompanyPreferences.class, new CommonSettingsMigrator(), context);
		// CustomerAndSalesSettingsMigrator
		migratedObjects = migrateObjects(CUSTOMER_AND_SALES_SETTINGS,
				CompanyPreferences.class,
				new CustomerAndSalesSettingsMigrator(), context);
		// FeaturesMigrator
		migratedObjects = migrateObjects(FEATURES, CompanyPreferences.class,
				new FeaturesMigrator(), context);
	}

	public void installPackage(String pkgName, String pkgVersion)
			throws Exception {
		// Creating HTTP request post method.

		HttpPost post = new HttpPost(ECGINE_URL + ECGINE_REST_PACKAGE_INSTALL);

		List<NameValuePair> postParameters = new ArrayList<>();
		postParameters.add(new BasicNameValuePair(PKG_NAME, pkgName));
		postParameters.add(new BasicNameValuePair(PKG_VERSION, pkgVersion));
		post.setEntity(new UrlEncodedFormEntity(postParameters));

		// Adding authentication parameters
		addAuthenticationParameters(post);

		// Executing method
		HttpResponse response = client.execute(post);

		StatusLine status = response.getStatusLine();

		if (status.getStatusCode() != HttpStatus.SC_OK) {
			throw new RuntimeException(status.toString());
		}
		HttpEntity entity = response.getEntity();
		// Read Response
		JSONObject jsonResult = new JSONObject(IOUtils.toString(entity
				.getContent()));
		showPolloingStatus(jsonResult.getLong("jobId"), "Install Package");
		migrateAllObjects();
	}

	private void showPolloingStatus(long jobId, String note) {
		Timer pollingTimer = new Timer();
		JobPollingTask task = new JobPollingTask(jobId, note, pollingTimer);
		pollingTimer.scheduleAtFixedRate(task, new Date(), DELAY_BETWEEN_TASKS);
		try {
			synchronized (pollingTimer) {
				pollingTimer.wait();
			}
		} catch (InterruptedException e) {
			log.error("Error while waiting to install accounter package", e);
			throw new RuntimeException(e);
		}
	}

	class JobPollingTask extends TimerTask {
		private long jobId;
		private String note;
		private Timer pollingTimer;

		public JobPollingTask(long jobId, String note, Timer pollingTimer) {
			this.jobId = jobId;
			this.note = note;
			this.pollingTimer = pollingTimer;
		}

		@Override
		public void run() {
			try {
				JSONObject jsonResult = showStatus(jobId);
				String status = (String) jsonResult.get("status");
				if (jsonResult.has("extStatus")) {
					String extStatus = (String) jsonResult.get("extStatus");
					log.info("Status of " + note + " - " + status + ";"
							+ extStatus);
				} else {
					log.info("Status of " + note + " - " + status);
				}
				if ("Aborted".equals(status) || "Failed".equals(status)
						|| "Completed".equals(status)) {
					log.info("Job " + note + " Completed!!");
					synchronized (pollingTimer) {
						pollingTimer.notifyAll();
					}
					pollingTimer.cancel();
				}
			} catch (Exception e) {
				log.error("Error while polling job status with jobId " + jobId,
						e);
			}
		}
	}

	private JSONObject showStatus(long jobId) throws Exception {
		// Creating HTTP request post method.
		HttpPost post = new HttpPost(ECGINE_URL + ECGINE_REST_JOB_STATUS);

		List<NameValuePair> postParameters = new ArrayList<>();
		postParameters
				.add(new BasicNameValuePair(JOBID, String.valueOf(jobId)));
		post.setEntity(new UrlEncodedFormEntity(postParameters));

		// Adding authentication parameters
		addAuthenticationParameters(post);

		// Executing method
		HttpResponse response = client.execute(post);

		StatusLine result = response.getStatusLine();

		if (result.getStatusCode() != HttpStatus.SC_OK) {
			throw new RuntimeException(result.toString());
		}

		HttpEntity entity = response.getEntity();
		// Read Response
		JSONObject jsonResult = new JSONObject(IOUtils.toString(entity
				.getContent()));
		return jsonResult;

	}

	private void addAuthenticationParameters(HttpRequest executeMethod) {
		String encodedKey = Base64.getEncoder().encodeToString(
				apiKey.getBytes());
		executeMethod.addHeader(AUTH_HEADER_NAME, BEARER + " " + encodedKey);
	}

	@SuppressWarnings("unchecked")
	private <T extends IAccounterServerCore> Map<Long, Long> migrateObjects(
			String identity, Class<T> clazz, IMigrator<T> migrator,
			MigratorContext context) throws JSONException, HttpException,
			IOException {
		List<Long> ids = new ArrayList<Long>();
		Session session = HibernateUtil.getCurrentSession();
		JSONArray objectArray = new JSONArray();
		Criteria criteria = session.createCriteria(clazz, "obj");
		migrator.addRestrictions(criteria);
		List<T> objects = new ArrayList<T>();
		if (clazz.equals(CompanyPreferences.class)) {
			objects.add((T) company.getPreferences());
		} else {
			objects = criteria.add(Restrictions.eq("company", company)).list();
		}
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
		return setRequestToRestApi(identity, context, objectArray, ids);
	}

	// here if we are sending already inserted objects to update then ids should
	// be null.Because those are old database id's. So no need to insert them in
	// context again
	private Map<Long, Long> setRequestToRestApi(String identity,
			MigratorContext context, JSONArray objectArray, List<Long> ids)
			throws JSONException, HttpException, IOException {

		Map<Long, Long> newAndOldIds = new HashMap<Long, Long>();
		// Setting SingleTon id's
		if (identity.equals(COMMON_SETTINGS)
				&& context.get(COMMON_SETTINGS, COMMON_SETTINGS_OLD_ID) == null) {
			if (!setSingleTonId(identity, newAndOldIds, objectArray,
					COMMON_SETTINGS_OLD_ID)) {
				return newAndOldIds;
			}
		} else if (identity.equals(CUSTOMER_AND_SALES_SETTINGS)
				&& context.get(CUSTOMER_AND_SALES_SETTINGS,
						CUSTOMER_AND_SALES_SETTINGS_OLD_ID) == null) {
			if (!setSingleTonId(identity, newAndOldIds, objectArray,
					CUSTOMER_AND_SALES_SETTINGS_OLD_ID)) {
				return newAndOldIds;
			}
		} else if (identity.equals(FEATURES)
				&& context.get(FEATURES, FEATURES_OLD_ID) == null) {
			if (!setSingleTonId(identity, newAndOldIds, objectArray,
					FEATURES_OLD_ID)) {
				return newAndOldIds;
			}
		}
		HttpPost post = new HttpPost(ECGINE_URL + ECGINE_REST + identity);
		addAuthenticationParameters(post);
		post.setHeader("Content-type", "application/json");
		post.setEntity(new StringEntity(objectArray.toString()));
		HttpResponse response = client.execute(post);
		StatusLine status = response.getStatusLine();
		HttpEntity entity = response.getEntity();

		if (status.getStatusCode() != HttpStatus.SC_OK) {
			if (entity != null) {
				String content = IOUtils.toString(entity.getContent());
				try {
					JSONArray array = new JSONArray(content);
					for (int i = 0; i < array.length(); i++) {
						JSONObject json = array.getJSONObject(i);
						boolean success = json.getBoolean("success");
						if (success) {
							Long id = json.getLong("id");
							Long oldId = ids.get(i);
							newAndOldIds.put(oldId, id);
							log.info("Migrated "
									+ identity
									+ (ids != null ? "  Accounter ID :" + oldId
											: "") + " Ecgine ID :" + id);
						} else {
							if (json.has("errors")) {
								String error = json.getJSONArray("errors")
										.toString();
								if (!error.equals("[]")) {
									log.info("\n" + "Found Errors In "
											+ identity + error);
								}
							}
						}
					}
				} catch (Exception e) {
					log.error("Error Occurred in server while saving "
							+ identity);
				}
			}
			return newAndOldIds;
			// throw new RuntimeException(status.toString());
		}
		String content = IOUtils.toString(entity.getContent());
		JSONArray array = new JSONArray(content);
		Map<String, List<Long>> ecgineMap = new HashMap<String, List<Long>>();
		Map<String, List<Long>> accounterMap = context.getChildrenMap();
		for (int i = 0; i < array.length(); i++) {
			JSONObject json = array.getJSONObject(i);
			long id = json.getLong("id");

			if (json.has("inactive") && json.getBoolean("inactive")) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", id);
				jsonObject.put("inactive", false);
				inactiveObjects.put(identity, jsonObject);
			}
			if (json.has("inActive") && json.getBoolean("inActive")) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", id);
				jsonObject.put("inActive", false);
				inactiveObjects.put(identity, jsonObject);
			}
			if (json.has("isInactive") && json.getBoolean("isInactive")) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", id);
				jsonObject.put("isInactive", false);
				inactiveObjects.put(identity, jsonObject);
			}
			if (ids != null) {
				newAndOldIds.put(ids.get(i), id);
			}
			JSONObject jsonObject = json.getJSONObject("object");

			createEcgineChildrenMap(accounterMap, ecgineMap, jsonObject);
			log.info("Migrated " + identity
					+ (ids != null ? "  Accounter ID :" + ids.get(i) : "")
					+ " Ecgine ID :" + json.getLong("id"));
		}
		putChildrenInContext(accounterMap, ecgineMap, context);
		return newAndOldIds;
	}

	private boolean setSingleTonId(String identity,
			Map<Long, Long> newAndOldIds, JSONArray objectArray, long oldId) {
		HttpGet get = new HttpGet(ECGINE_URL + API_BASE_URL + ECGINE_LIST
				+ identity);
		addAuthenticationParameters(get);
		get.setHeader("Content-type", "application/json");
		try {
			HttpResponse response = client.execute(get);
			StatusLine status = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				if (entity != null) {
					EntityUtils.consume(entity);
				}
				return false;
			}
			String content = IOUtils.toString(entity.getContent());
			JSONObject json = new JSONObject(content);
			JSONObject jsonValue = (JSONObject) json.get("value");
			long singleTonId = jsonValue.getLong("id");
			HashMap<Long, Long> map = new HashMap<Long, Long>();
			map.put(oldId, singleTonId);
			context.put(identity, map);
			JSONObject jsonObject = objectArray.getJSONObject(0);
			jsonObject.put("id", singleTonId);
			objectArray.remove(0);
			objectArray.put(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
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

	private JSONObject signup(User user) throws HttpException, IOException,
			JSONException {
		HttpPost post = new HttpPost(ECGINE_URL + SIGNUP_CREATE_ORG);
		Client accClient = user.getClient();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(EMAIL, accClient.getEmailId()));
		params.add(new BasicNameValuePair(FIRST_NAME, accClient.getFirstName()));
		params.add(new BasicNameValuePair(LAST_NAME, accClient.getLastName()));
		params.add(new BasicNameValuePair(COUNTRY, removeSpaces(accClient
				.getCountry())));
		Company company2 = user.getCompany();
		params.add(new BasicNameValuePair(COMPANY_NAME, company2
				.getTradingName()));
		params.add(new BasicNameValuePair(CURRENCY, company2
				.getPrimaryCurrency().getFormalName()));
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
		return responseResult;
	}

	String removeSpaces(String country) {
		return country.replace(" ", "");
	}
}
