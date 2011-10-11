package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.company.initialize.CompanyInitializedFactory;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.TemplateAccount;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class Company implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2342334234608152532L;

	public static final int ACCOUNTING_TYPE_US = 0;
	public static final int ACCOUNTING_TYPE_UK = 1;
	public static final int ACCOUNTING_TYPE_INDIA = 2;
	public static final int ACCOUNTING_TYPE_OTHER = 3;

	public static final String UK = "UK";

	public static final String US = "US";

	public static final String INDIA = "India";

	public static final String OTHER = "Other";

	private long id;
	private int version;
	int accountingType = 0;

	String companyID;

	private boolean isConfigured;

	Address registeredAddress;

	String companyEmail;

	// don't know the purpose

	String companyEmailForCustomers;

	// don't know the purpose
	Contact contact = new Contact();

	// don't know the purpose
	String ein;

	// not used yet
	int firstMonthOfFiscalYear;

	// not used yet
	int firstMonthOfIncomeTaxYear;

	int taxForm;

	FinanceDate booksClosingDate;

	int closingDateWarningType;

	boolean enableAccountNumbers;

	int customerType;

	boolean enableAutoRecall;

	boolean restartSetupInterviews;

	int fiscalYearStarting;

	int industry;

	private Set<Location> locations = new HashSet<Location>();
	/**
	 * Each company have it's own preferences. This will hold all the
	 * preferences related to the company.
	 * 
	 * @see Company
	 */
	CompanyPreferences preferences = new CompanyPreferences();

	/**
	 * Each Company has different Users This variable has the Set of
	 * {@link User}
	 */
	private Set<User> users = new HashSet<User>();

	private Set<VATReturnBox> vatReturnBoxes = new HashSet<VATReturnBox>();

	private String registrationNumber;

	public void setID(long id) {
		this.id = id;
	}

	/**
	 * This is the direct references to the Accounts Receivable Account for the
	 * purpose of the Transactions.
	 */
	Account accountsReceivableAccount;
	/**
	 * This is the direct references to the Accounts Payable Account for the
	 * purpose of the Transactions.
	 */
	Account accountsPayableAccount;
	/**
	 * This is the direct references to the Opening Balances Account for the
	 * purpose of the Transactions.
	 */
	Account openingBalancesAccount;
	/**
	 * This is the direct references to the Retained Earnings Account for the
	 * purpose of the Transactions.
	 */
	Account retainedEarningsAccount;
	/**
	 * This is the direct references to the Other Cash Income Account for the
	 * purpose of the Cash Basis Journal Entry.
	 */
	Account otherCashIncomeAccount;

	/**
	 * This is the direct references to the Other Cash Expense Account for the
	 * purpose of the Cash Basis Journal Entry.
	 */
	Account otherCashExpenseAccount;

	/**
	 * This is the direct references to Cash Discounts Given to track the
	 * discounts given.
	 */
	private Account cashDiscountsGiven;

	/**
	 * This is the direct references to Cash Discounts Given to track the
	 * discounts taken.
	 */
	private Account cashDiscountsTaken;

	/**
	 * This is the Account created by default for the purpose of UK VAT
	 */
	private Account taxLiabilityAccount;
	// Account prepaidVATaccount;
	// Account ECAcquisitionVATaccount;

	Account pendingItemReceiptsAccount;

	/**
	 * Each company have a set of {@link Account} This will hold all the
	 * Accounts created in this company.
	 */
	private Set<Account> accounts = new HashSet<Account>();

	/**
	 * Each company has a set of PaymentTerms. This property can hold a Set of
	 * {@link PaymentTerms}
	 */
	private Set<PaymentTerms> paymentTerms = new HashSet<PaymentTerms>();
	/**
	 * This represents a Set of {@link FiscalYear} We can create any number of
	 * Fiscal years and close them.
	 */
	private Set<FiscalYear> fiscalYears = new HashSet<FiscalYear>();

	// Set<PayType> payTypes = new HashSet<PayType>();

	/**
	 * Each company can have a Set of {@link Payee}
	 * 
	 * @see Vendor
	 * @see Customer
	 * @see TaxAgency
	 * @see SalesPerson
	 */
	private Set<Payee> payees = new HashSet<Payee>();

	private Set<TAXItem> taxItems = new HashSet<TAXItem>();

	private Set<TAXAgency> taxAgencies = new HashSet<TAXAgency>();

	private Set<NominalCodeRange> nominalCodeRange = new HashSet<NominalCodeRange>();

	/**
	 * This is the Account created by default for the purpose of UK when VAT is
	 * Filed
	 */
	Account VATFiledLiabilityAccount;

	private Set<BrandingTheme> brandingTheme = new HashSet<BrandingTheme>();

	private Set<User> usersList = new HashSet<User>();

	private Set<VATReturn> vatReturns = new HashSet<VATReturn>();

	private Set<Currency> currencies = new HashSet<Currency>();

	private Set<AccounterClass> accounterClasses = new HashSet<AccounterClass>();

	private Set<TAXGroup> taxGroups = new HashSet<TAXGroup>();

	private Set<Customer> customers = new HashSet<Customer>();

	private Set<Vendor> vendors = new HashSet<Vendor>();

	private Set<Item> items = new HashSet<Item>();

	private Set<CustomerGroup> customerGroups = new HashSet<CustomerGroup>();

	private Set<VendorGroup> vendorGroups = new HashSet<VendorGroup>();

	private Set<ShippingTerms> shippingTerms = new HashSet<ShippingTerms>();

	private Set<ShippingMethod> shippingMethods = new HashSet<ShippingMethod>();

	private Set<PriceLevel> priceLevels = new HashSet<PriceLevel>();

	private Set<ItemGroup> itemGroups = new HashSet<ItemGroup>();

	private Set<PaySalesTax> paySalesTaxs = new HashSet<PaySalesTax>();

	private Set<CreditRating> creditRatings = new HashSet<CreditRating>();

	private Set<SalesPerson> salesPersons = new HashSet<SalesPerson>();

	private Set<Bank> banks = new HashSet<Bank>();

	private Set<TaxRates> taxrates = new HashSet<TaxRates>();

	private Set<FixedAsset> fixedAssets = new HashSet<FixedAsset>();

	private Set<TAXAdjustment> taxAdjustments = new HashSet<TAXAdjustment>();

	private Set<TAXCode> taxCodes = new HashSet<TAXCode>();

	private Set<TAXItemGroup> taxItemGroups = new HashSet<TAXItemGroup>();

	private Set<Box> vatBoxes = new HashSet<Box>();
	
	private Set<Transaction> transactions = new HashSet<Transaction>();
	
	private Set<Activity> activities = new HashSet<Activity>();

	String bankAccountNo;

	String sortCode;

	boolean doupaySalesChecBox;

	String vatRegNumber;

	String serviceItemDefaultIncomeAccount = AccounterServerConstants.CASH_DISCOUNT_GIVEN;

	String serviceItemDefaultExpenseAccount = AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_A;

	String nonInventoryItemDefaultIncomeAccount = AccounterServerConstants.CASH_DISCOUNT_GIVEN;

	String nonInventoryItemDefaultExpenseAccount = AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_A;

	private String ukServiceItemDefaultIncomeAccount = AccounterServerConstants.SALES_INCOME_TYPE_A;

	private String ukServiceItemDefaultExpenseAccount = AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_A;

	private String ukNonInventoryItemDefaultIncomeAccount = AccounterServerConstants.SALES_INCOME_TYPE_A;

	private String ukNonInventoryItemDefaultExpenseAccount = AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_A;

	public Account getRetainedEarningsAccount() {
		return retainedEarningsAccount;
	}

	public void setRetainedEarningsAccount(Account retainedEarningsAccount) {
		this.retainedEarningsAccount = retainedEarningsAccount;
	}

	public void setAccountsReceivableAccount(Account accountsReceivableAccount) {
		this.accountsReceivableAccount = accountsReceivableAccount;
	}

	public void setAccountsPayableAccount(Account accountsPayableAccount) {
		this.accountsPayableAccount = accountsPayableAccount;
	}

	public void setOpeningBalancesAccount(Account openingBalancesAccount) {
		this.openingBalancesAccount = openingBalancesAccount;
	}

	public Account getOtherCashIncomeAccount() {
		return otherCashIncomeAccount;
	}

	public void setOtherCashIncomeAccount(Account otherCashIncomeAccount) {
		this.otherCashIncomeAccount = otherCashIncomeAccount;
	}

	public Account getOtherCashExpenseAccount() {
		return otherCashExpenseAccount;
	}

	public void setOtherCashExpenseAccount(Account otherCashExpenseAccount) {
		this.otherCashExpenseAccount = otherCashExpenseAccount;
	}

	/**
	 * Creates new Instance
	 */
	public Company() {
		// TODO Auto-generated constructor stub
	}

	public Company(int accountingType) {
		this.accountingType = accountingType;

		this.preferences = new CompanyPreferences();
		Address tradingAddress = new Address();
		tradingAddress.type = Address.TYPE_COMPANY;
		preferences.setTradingAddress(tradingAddress);
		registeredAddress = new Address();
		registeredAddress.type = Address.TYPE_COMPANY_REGISTRATION;

		if (accountingType == ACCOUNTING_TYPE_UK) {
			preferences.setReferVendors(ClientVendor.SUPPLIER);
		}
		initPrimaryCurrency();
	}

	/**
	 * 
	 */
	private void initPrimaryCurrency() {
		Currency currency = new Currency();
		switch (accountingType) {
		case ACCOUNTING_TYPE_US:
			currency.setName("USD");
			break;
		case ACCOUNTING_TYPE_UK:
			currency.setName("GBP");
			break;
		case ACCOUNTING_TYPE_INDIA:
			currency.setName("INR");
			break;
		default:
			currency.setName("INR");
			break;
		}
		preferences.setPrimaryCurrency(currency);
	}

	public void initialize(List<TemplateAccount> accounts) {

		CompanyInitializedFactory.getInitializer(this).init(accounts);
	}

	/**
	 * Return the list of Nominal code ranges for the given sub base type of an
	 * Account
	 * 
	 * @param accountSubBaseType
	 * @return Integer[]
	 */
	public Integer[] getNominalCodeRange(int accountSubBaseType) {

		for (NominalCodeRange nomincalCode : this.getNominalCodeRange()) {
			if (nomincalCode.getAccountSubBaseType() == accountSubBaseType) {
				return new Integer[] { nomincalCode.getMinimum(),
						nomincalCode.getMaximum() };
			}
		}

		return null;
	}

	public static ArrayList<Integer> getAccountingTypes() {
		ArrayList<Integer> accountingTypes = new ArrayList<Integer>();
		accountingTypes.add(ACCOUNTING_TYPE_US);
		accountingTypes.add(ACCOUNTING_TYPE_UK);
		accountingTypes.add(ACCOUNTING_TYPE_INDIA);
		accountingTypes.add(ACCOUNTING_TYPE_OTHER);
		return accountingTypes;
	}

	/**
	 * @return the name
	 */
	public String getFullName() {
		return getPreferences().getFullName();
	}

	/**
	 * @return the legalName
	 */
	public String getTradingName() {
		return getPreferences().getLegalName();
	}

	public void setTradingName(String legalName) {
		getPreferences().setLegalName(legalName);
	}

	public String gettimezone() {
		return getPreferences().getTimezone();
	}

	public void settimezone(String timezone) {
		getPreferences().setTimezone(timezone);
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	/**
	 * @return the companyEmail
	 */
	public String getCompanyEmail() {
		return companyEmail;
	}

	/**
	 * @return the companyEmailForCustomers
	 */
	public String getCompanyEmailForCustomers() {
		return companyEmailForCustomers;
	}

	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * @return the ein
	 */
	public String getEin() {
		return ein;
	}

	/**
	 * @return the firstMonthOfFiscalYear
	 */
	public int getFirstMonthOfFiscalYear() {
		return firstMonthOfFiscalYear;
	}

	/**
	 * @return the firstMonthOfIncomeTaxYear
	 */
	public int getFirstMonthOfIncomeTaxYear() {
		return firstMonthOfIncomeTaxYear;
	}

	/**
	 * @return the taxForm
	 */
	public int getTaxForm() {
		return taxForm;
	}

	/**
	 * @return the booksClosingDate
	 */
	public FinanceDate getBooksClosingDate() {
		return booksClosingDate;
	}

	/**
	 * @return the closingDateWarningType
	 */
	public int getClosingDateWarningType() {
		return closingDateWarningType;
	}

	/**
	 * @return the enableAccountNumbers
	 */
	public boolean getEnableAccountNumbers() {
		return enableAccountNumbers;
	}

	/**
	 * @return the customerType
	 */
	public int getCustomerType() {
		return customerType;
	}

	/**
	 * @return the enableAutoRecall
	 */
	public boolean getEnableAutoRecall() {
		return enableAutoRecall;
	}

	/**
	 * @return the restartSetupInterviews
	 */
	public boolean getRestartSetupInterviews() {
		return restartSetupInterviews;
	}

	/**
	 * @return the taxId
	 */
	public String getTaxId() {
		return getPreferences().getTaxId();
	}

	/**
	 * @return the fiscalYearStarting
	 */
	public int getFiscalYearStarting() {
		return fiscalYearStarting;
	}

	/**
	 * @return the industry
	 */
	public int getIndustry() {
		return industry;
	}

	/**
	 * @return the preferences
	 */
	public CompanyPreferences getPreferences() {
		return preferences;
	}

	/**
	 * @param preferences
	 *            the preferences to set
	 */
	public void setPreferences(CompanyPreferences preferences) {
		this.preferences = preferences;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getSortCode() {
		return sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public boolean isDoupaySalesChecBox() {
		return doupaySalesChecBox;
	}

	public void setDoupaySalesChecBox(boolean doupaySalesChecBox) {
		this.doupaySalesChecBox = doupaySalesChecBox;
	}

	public String getVatRegNumber() {
		return vatRegNumber;
	}

	public void setVatRegNumber(String vatRegNumber) {
		this.vatRegNumber = vatRegNumber;
	}

	/**
	 * @return the accountsReceivableAccount
	 */
	public Account getAccountsReceivableAccount() {
		return accountsReceivableAccount;
	}

	/**
	 * @return the accountsPayableAccount
	 */
	public Account getAccountsPayableAccount() {
		return accountsPayableAccount;
	}

	/**
	 * @return the openingBalancesAccount
	 */
	public Account getOpeningBalancesAccount() {
		return openingBalancesAccount;
	}

	public Account getPendingItemReceiptsAccount() {
		return pendingItemReceiptsAccount;
	}

	public void setPendingItemReceiptsAccount(Account pendingItemReceiptsAccount) {
		this.pendingItemReceiptsAccount = pendingItemReceiptsAccount;
	}

	public Account getVATFiledLiabilityAccount() {
		return VATFiledLiabilityAccount;
	}

	public void setVATFiledLiabilityAccount(Account vATFiledLiabilityAccount) {
		VATFiledLiabilityAccount = vATFiledLiabilityAccount;
	}

	@Override
	public String toString() {

		return getFullName() + " " + companyEmail;

	}

	public void setFullName(String name) {
		this.getPreferences().setFullName(name);

	}

	/**
	 * @return the accountingType
	 */
	public int getAccountingType() {
		return accountingType;
	}

	public boolean isConfigured() {
		return isConfigured;
	}

	public void setConfigured(boolean isConfigured) {
		this.isConfigured = isConfigured;
	}

	/**
	 * @param accountingType
	 *            the accountingType to set
	 */
	public void setAccountingType(int accountingType) {
		this.accountingType = accountingType;
	}

	public String getServiceItemDefaultIncomeAccount() {
		return serviceItemDefaultIncomeAccount;
	}

	public void setServiceItemDefaultIncomeAccount(
			String serviceItemDefaultIncomeAccount) {
		this.serviceItemDefaultIncomeAccount = serviceItemDefaultIncomeAccount;
	}

	public String getServiceItemDefaultExpenseAccount() {
		return serviceItemDefaultExpenseAccount;
	}

	public void setServiceItemDefaultExpenseAccount(
			String serviceItemDefaultExpenseAccount) {
		this.serviceItemDefaultExpenseAccount = serviceItemDefaultExpenseAccount;
	}

	public String getNonInventoryItemDefaultIncomeAccount() {
		return nonInventoryItemDefaultIncomeAccount;
	}

	public void setNonInventoryItemDefaultIncomeAccount(
			String nonInventoryItemDefaultIncomeAccount) {
		this.nonInventoryItemDefaultIncomeAccount = nonInventoryItemDefaultIncomeAccount;
	}

	public String getNonInventoryItemDefaultExpenseAccount() {
		return nonInventoryItemDefaultExpenseAccount;
	}

	public void setNonInventoryItemDefaultExpenseAccount(
			String nonInventoryItemDefaultExpenseAccount) {
		this.nonInventoryItemDefaultExpenseAccount = nonInventoryItemDefaultExpenseAccount;
	}

	/**
	 * Creates an entry in the VATRateCalculation entry for a transactionItem in
	 * the UK company. This makes us to track all the VAT related amount to pay
	 * while making the PayVAT.
	 * 
	 * @param transactionItem
	 * @param session
	 * @return boolean
	 */
	public static boolean setTAXRateCalculation(
			TransactionItem transactionItem, Session session) {

		if (transactionItem.getTaxCode() != null) {

			TAXCode code = transactionItem.getTaxCode();

			if (transactionItem.transaction.getTransactionCategory() == Transaction.CATEGORY_CUSTOMER) {

				setVRCForCustomer(code, transactionItem, session);

			} else if (transactionItem.transaction.getTransactionCategory() == Transaction.CATEGORY_VENDOR) {

				setVRCForVendor(code, transactionItem, session);

			}

		}

		return false;
	}

	/**
	 * Creates the VATRateCalculation entry for the Customer type transaction.
	 * 
	 * @param code
	 * @param transactionItem
	 * @param session
	 */
	private static void setVRCForCustomer(TAXCode code,
			TransactionItem transactionItem, Session session) {

		if (code.getTAXItemGrpForSales() != null) {

			if (code.getTAXItemGrpForSales() instanceof TAXItem) {

				TAXItem vatItem = ((TAXItem) code.getTAXItemGrpForSales());

				setVatItemVRC(vatItem, transactionItem, session);

			} else {

				TAXGroup vatGroup = (TAXGroup) code.getTAXItemGrpForSales();
				for (int i = 0; i < vatGroup.getTAXItems().size(); i++) {

					TAXItem vatItem = vatGroup.getTAXItems().get(i);

					setVatGroupVRC(vatItem, transactionItem, session);

				}
			}
		}

	}

	/**
	 * Creates the VATRateCalculation entry for the Vendor type transaction.
	 * 
	 * @param code
	 * @param transactionItem
	 * @param session
	 */
	private static void setVRCForVendor(TAXCode code,
			TransactionItem transactionItem, Session session) {
		if (code.getTAXItemGrpForPurchases() != null) {

			if (code.getTAXItemGrpForPurchases() instanceof TAXItem) {

				TAXItem vatItem = (TAXItem) code.getTAXItemGrpForPurchases();

				setVatItemVRC(vatItem, transactionItem, session);

			} else {

				TAXGroup vatGroup = (TAXGroup) code.getTAXItemGrpForPurchases();
				for (int i = 0; i < vatGroup.getTAXItems().size(); i++) {

					TAXItem vatItem = vatGroup.getTAXItems().get(i);

					setVatGroupVRC(vatItem, transactionItem, session);

				}
			}
		}

	}

	/**
	 * Set the VATItem entry in the VATRateCalculation. This is used to differ
	 * this entry from the VATGroup type entry.
	 * 
	 * @param vatItem
	 * @param transactionItem
	 * @param session
	 */
	private static void setVatItemVRC(TAXItem vatItem,
			TransactionItem transactionItem, Session session) {

		TAXRateCalculation vc = null;
		vc = new TAXRateCalculation(vatItem, transactionItem);

		vc.setVATGroupEntry(false);

		setVRC(vatItem, transactionItem, session, vc);

	}

	/**
	 * Set the VATGroup entry in the VATRateCalculation. This is used to differ
	 * this entry from the VATItem type entry.
	 * 
	 * @param vatItem
	 * @param transactionItem
	 * @param session
	 */
	private static void setVatGroupVRC(TAXItem vatItem,
			TransactionItem transactionItem, Session session) {

		TAXRateCalculation vc = null;
		vc = new TAXRateCalculation(vatItem, transactionItem);

		vc.setVATGroupEntry(true);

		/*
		 * Here updating vatAmount value for vat group entries
		 */
		if (vatItem.getName().equals(
				AccounterServerConstants.VAT_ITEM_EC_SALES_SERVICES_STANDARD)
				|| vatItem.getName().equals(
						AccounterServerConstants.VAT_ITEM_STANDARD_PURCHASES)
				|| vatItem.getName().equals(
						AccounterServerConstants.VAT_ITEM_ZERO_RATED_PURCHASES)) {

			vc.vatAmount = -1 * vc.vatAmount;
		}

		setVRC(vatItem, transactionItem, session, vc);

	}

	/**
	 * Creates the entry in the VATRateCalculation with the given values passed
	 * to the constructor.
	 * 
	 * @param vatItem
	 * @param transactionItem
	 * @param session
	 * @param vc
	 */
	private static void setVRC(TAXItem vatItem,
			TransactionItem transactionItem, Session session,
			TAXRateCalculation vc) {

		vatItem.getTaxAgency().updateVATAgencyAccount(session,
				transactionItem.transaction,
				transactionItem.transaction.getTransactionCategory(),
				vc.getVatAmount());

		transactionItem.taxRateCalculationEntriesList.add(vc);

		// setPayVatEntries(transactionItem, vatItem);

	}

	public void setUkServiceItemDefaultIncomeAccount(
			String ukServiceItemDefaultIncomeAccount) {
		this.ukServiceItemDefaultIncomeAccount = ukServiceItemDefaultIncomeAccount;
	}

	public String getUkServiceItemDefaultIncomeAccount() {
		return ukServiceItemDefaultIncomeAccount;
	}

	public void setUkServiceItemDefaultExpenseAccount(
			String ukServiceItemDefaultExpenseAccount) {
		this.ukServiceItemDefaultExpenseAccount = ukServiceItemDefaultExpenseAccount;
	}

	public String getUkServiceItemDefaultExpenseAccount() {
		return ukServiceItemDefaultExpenseAccount;
	}

	public void setUkNonInventoryItemDefaultIncomeAccount(
			String ukNonInventoryItemDefaultIncomeAccount) {
		this.ukNonInventoryItemDefaultIncomeAccount = ukNonInventoryItemDefaultIncomeAccount;
	}

	public String getUkNonInventoryItemDefaultIncomeAccount() {
		return ukNonInventoryItemDefaultIncomeAccount;
	}

	public void setUkNonInventoryItemDefaultExpenseAccount(
			String ukNonInventoryItemDefaultExpenseAccount) {
		this.ukNonInventoryItemDefaultExpenseAccount = ukNonInventoryItemDefaultExpenseAccount;
	}

	public String getUkNonInventoryItemDefaultExpenseAccount() {
		return ukNonInventoryItemDefaultExpenseAccount;
	}

	public Company toCompany(Company cmp) {

		cmp.accountingType = this.getAccountingType();
		cmp.id = this.getID();
		cmp.accounts = this.getAccounts();

		cmp.companyEmail = this.getCompanyEmail();
		cmp.registeredAddress = this.getTradingAddress();
		cmp.companyEmailForCustomers = this.getCompanyEmailForCustomers();
		cmp.contact = this.getContact();
		cmp.ein = this.getEin();
		cmp.firstMonthOfFiscalYear = this.getFirstMonthOfFiscalYear();
		cmp.firstMonthOfIncomeTaxYear = this.getFirstMonthOfIncomeTaxYear();
		cmp.taxForm = this.getTaxForm();
		cmp.booksClosingDate = this.getBooksClosingDate();
		cmp.closingDateWarningType = this.getClosingDateWarningType();
		cmp.enableAccountNumbers = this.getEnableAccountNumbers();

		cmp.customerType = this.getCustomerType();
		cmp.enableAutoRecall = this.getEnableAutoRecall();
		cmp.restartSetupInterviews = this.getRestartSetupInterviews();
		cmp.setTaxId(this.getTaxId());
		cmp.fiscalYearStarting = this.getFiscalYearStarting();
		cmp.industry = this.getIndustry();
		cmp.users = this.getUsers();
		cmp.vatReturnBoxes = this.getVatReturnBoxes();
		cmp.preferences = this.getPreferences();
		cmp.id = this.getID();

		cmp.accountsReceivableAccount = this.getAccountsReceivableAccount();
		cmp.accountsPayableAccount = this.getAccountsPayableAccount();
		cmp.openingBalancesAccount = this.getOpeningBalancesAccount();
		cmp.accountsPayableAccount = this.getAccountsPayableAccount();
		cmp.pendingItemReceiptsAccount = this.getPendingItemReceiptsAccount();
		cmp.retainedEarningsAccount = this.getRetainedEarningsAccount();
		cmp.otherCashIncomeAccount = this.getOtherCashIncomeAccount();
		cmp.otherCashExpenseAccount = this.getOtherCashExpenseAccount();

		// cmp.VATliabilityAccount = this.getvagetvat;

		cmp.paymentTerms = this.getPaymentTerms();

		cmp.fiscalYears = this.getFiscalYears();

		cmp.payees = this.getPayees();

		cmp.nominalCodeRange = this.getNominalCodeRange();

		cmp.customers = this.getCustomers();

		cmp.vendors = this.getVendors();

		cmp.taxAgencies = this.getTaxAgencies();

		cmp.taxItemGroups = this.getTaxItemGroups();

		cmp.items = this.getItems();

		cmp.customerGroups = this.getCustomerGroups();

		cmp.vendorGroups = this.getVendorGroups();

		cmp.shippingTerms = this.getShippingTerms();

		cmp.shippingMethods = this.getShippingMethods();

		cmp.priceLevels = this.getPriceLevels();

		cmp.itemGroups = this.getItemGroups();

		cmp.taxGroups = this.getTaxGroups();

		cmp.paySalesTaxs = this.getPaySalesTaxs();

		cmp.creditRatings = this.getCreditRatings();

		cmp.salesPersons = this.getSalesPersons();

		cmp.taxCodes = this.getTaxCodes();

		cmp.taxItems = this.getTaxItems();

		cmp.banks = this.getBanks();

		cmp.taxrates = this.getTaxrates();

		cmp.fixedAssets = this.getFixedAssets();

		cmp.taxAdjustments = this.getTaxAdjustments();

		cmp.vatBoxes = this.getVatBoxes();

		cmp.vatReturns = this.getVatReturns();

		cmp.registrationNumber = this.getRegistrationNumber();

		cmp.bankAccountNo = this.getBankAccountNo();

		cmp.sortCode = this.getSortCode();

		cmp.brandingTheme = this.getBrandingTheme();

		cmp.usersList = this.getUsersList();

		cmp.accounterClasses = this.getAccounterClasses();

		cmp.locations = this.getLocations();

		return cmp;
	}

	public Set<BrandingTheme> getBrandingTheme() {
		return brandingTheme;
	}

	public Set<Vendor> getVendors() {
		return vendors;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setTaxId(String taxId) {
		getPreferences().setTaxId(taxId);
	}

	public void updatePreferences(ClientCompany clientCompany)
			throws AccounterException {

		ServerConvertUtil serverConvertUtil = new ServerConvertUtil();
		this.setFullName(clientCompany.getName());

		this.setRegisteredAddress(serverConvertUtil.toServerObject(
				this.registeredAddress, clientCompany.getRegisteredAddress(),
				HibernateUtil.getCurrentSession()));

		this.setCompanyEmail(clientCompany.getCompanyEmail());
		// RegisteredName=legalName
		this.setTradingName(clientCompany.getTradingName());
		this.setRegistrationNumber(clientCompany.getRegistrationNumber());
		this.setTaxId(clientCompany.getTaxId());
		this.setBankAccountNo(clientCompany.getBankAccountNo());
		this.setSortCode(clientCompany.getSortCode());
		this.setPreferences(serverConvertUtil.toServerObject(this.preferences,
				clientCompany.getPreferences(),
				HibernateUtil.getCurrentSession()));
	}

	public ClientCompany toClientCompany() throws AccounterException {
		ClientCompany clientCompany = new ClientCompany();
		clientCompany.setID(this.id);
		clientCompany.setCompanyEmail(this.companyEmail);
		clientCompany.setRegistrationNumber(this.registrationNumber);
		clientCompany.setBankAccountNo(this.bankAccountNo);
		clientCompany.setSortCode(this.sortCode);
		if (this.preferences != null) {
			clientCompany.setPreferences(new ClientConvertUtil()
					.toClientObject(this.preferences,
							ClientCompanyPreferences.class));
		}

		return clientCompany;
	}

	/**
	 * Returns User By UserID
	 * 
	 * @param userID
	 * @return
	 */
	public User getUserByUserId(long userID) {
		for (User user : users) {
			if (user.getID() == userID) {
				return user;
			}
		}
		return null;

	}

	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCompanyID() {
		return companyID;
	}

	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

	/**
	 * @return the tradingAddress
	 */
	public Address getTradingAddress() {
		return this.preferences.getTradingAddress();
	}

	/**
	 * @param tradingAddress
	 *            the tradingAddress to set
	 */
	public void setTradingAddress(Address tradingAddress) {
		this.preferences.setTradingAddress(tradingAddress);
	}

	/**
	 * @return the registeredAddress
	 */
	public Address getRegisteredAddress() {
		return registeredAddress;
	}

	/**
	 * @param registeredAddress
	 *            the registeredAddress to set
	 */
	public void setRegisteredAddress(Address registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

	/**
	 * Returns User By EmailID
	 * 
	 * @param email
	 * @return
	 */
	public User getUserByUserEmail(String email) {
		Set<User> users = getUsers();
		for (User user : users) {
			if (user.getEmail().equals(email)) {
				return user;
			}
		}
		return null;
	}

	/**
	 * @param user
	 */
	public void addUser(User user) {
		this.users.add(user);
	}

	public Account getTaxLiabilityAccount() {
		return taxLiabilityAccount;
	}

	public void setTaxLiabilityAccount(Account vATliabilityAccount) {
		taxLiabilityAccount = vATliabilityAccount;
	}

	public Account getCashDiscountsGiven() {
		return cashDiscountsGiven;
	}

	public void setCashDiscountsGiven(Account cashDiscountsGiven) {
		this.cashDiscountsGiven = cashDiscountsGiven;
	}

	public Account getCashDiscountsTaken() {
		return cashDiscountsTaken;
	}

	public void setCashDiscountsTaken(Account cashDiscountsTaken) {
		this.cashDiscountsTaken = cashDiscountsTaken;
	}

	public Set<Location> getLocations() {
		return locations;
	}

	public void setLocations(Set<Location> arrayList) {
		this.locations = arrayList;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<VATReturnBox> getVatReturnBoxes() {
		return vatReturnBoxes;
	}

	public void setVatReturnBoxes(Set<VATReturnBox> vatReturnBoxes) {
		this.vatReturnBoxes = vatReturnBoxes;
	}

	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	public Set<PaymentTerms> getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(Set<PaymentTerms> paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public Set<FiscalYear> getFiscalYears() {
		return fiscalYears;
	}

	public void setFiscalYears(Set<FiscalYear> fiscalYears) {
		this.fiscalYears = fiscalYears;
	}

	public Set<Payee> getPayees() {
		return payees;
	}

	public void setPayees(Set<Payee> payees) {
		this.payees = payees;
		customers = new HashSet<Customer>();
		vendors = new HashSet<Vendor>();
		taxAgencies = new HashSet<TAXAgency>();
		salesPersons = new HashSet<SalesPerson>();

		for (Payee payee : payees) {
			// Iterate and check which type it is and add to that list
			if (payee instanceof Customer) {
				customers.add((Customer) payee);
			} else if (payee instanceof Vendor) {
				vendors.add((Vendor) payee);
			} else if (payee instanceof TAXAgency) {
				taxAgencies.add((TAXAgency) payee);
			} else {
				taxAgencies.add((TAXAgency) payee);
			}
		}
		this.payees = payees;
	}

	public Set<TAXItem> getTaxItems() {
		return taxItems;
	}

	public void setTaxItems(Set<TAXItem> taxItems) {
		this.taxItems = taxItems;
	}

	public Set<TAXAgency> getTaxAgencies() {
		return taxAgencies;
	}

	public void setTaxAgencies(Set<TAXAgency> taxAgencies) {
		this.taxAgencies = taxAgencies;
	}

	public Set<NominalCodeRange> getNominalCodeRange() {
		return nominalCodeRange;
	}

	public void setNominalCodeRange(Set<NominalCodeRange> nominalCodeRange) {
		this.nominalCodeRange = nominalCodeRange;
	}

	public Set<User> getUsersList() {
		return usersList;
	}

	public void setUsersList(Set<User> usersList) {
		this.usersList = usersList;
	}

	public Set<VATReturn> getVatReturns() {
		return vatReturns;
	}

	public void setVatReturns(Set<VATReturn> vatReturns) {
		this.vatReturns = vatReturns;
	}

	public Set<Currency> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Set<Currency> currencies) {
		this.currencies = currencies;
	}

	public Set<AccounterClass> getAccounterClasses() {
		return accounterClasses;
	}

	public void setAccounterClasses(Set<AccounterClass> accounterClasses) {
		this.accounterClasses = accounterClasses;
	}

	public Set<TAXGroup> getTaxGroups() {
		return taxGroups;
	}

	public void setTaxGroups(Set<TAXGroup> taxGroups) {
		this.taxGroups = taxGroups;
	}

	public Set<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public Set<CustomerGroup> getCustomerGroups() {
		return customerGroups;
	}

	public void setCustomerGroups(Set<CustomerGroup> customerGroups) {
		this.customerGroups = customerGroups;
	}

	public Set<VendorGroup> getVendorGroups() {
		return vendorGroups;
	}

	public void setVendorGroups(Set<VendorGroup> vendorGroups) {
		this.vendorGroups = vendorGroups;
	}

	public Set<ShippingTerms> getShippingTerms() {
		return shippingTerms;
	}

	public void setShippingTerms(Set<ShippingTerms> shippingTerms) {
		this.shippingTerms = shippingTerms;
	}

	public Set<ShippingMethod> getShippingMethods() {
		return shippingMethods;
	}

	public void setShippingMethods(Set<ShippingMethod> shippingMethods) {
		this.shippingMethods = shippingMethods;
	}

	public Set<PriceLevel> getPriceLevels() {
		return priceLevels;
	}

	public void setPriceLevels(Set<PriceLevel> priceLevels) {
		this.priceLevels = priceLevels;
	}

	public Set<ItemGroup> getItemGroups() {
		return itemGroups;
	}

	public void setItemGroups(Set<ItemGroup> itemGroups) {
		this.itemGroups = itemGroups;
	}

	public Set<PaySalesTax> getPaySalesTaxs() {
		return paySalesTaxs;
	}

	public void setPaySalesTaxs(Set<PaySalesTax> paySalesTaxs) {
		this.paySalesTaxs = paySalesTaxs;
	}

	public Set<CreditRating> getCreditRatings() {
		return creditRatings;
	}

	public void setCreditRatings(Set<CreditRating> creditRatings) {
		this.creditRatings = creditRatings;
	}

	public Set<SalesPerson> getSalesPersons() {
		return salesPersons;
	}

	public void setSalesPersons(Set<SalesPerson> salesPersons) {
		this.salesPersons = salesPersons;
	}

	public Set<Bank> getBanks() {
		return banks;
	}

	public void setBanks(Set<Bank> banks) {
		this.banks = banks;
	}

	public Set<TaxRates> getTaxrates() {
		return taxrates;
	}

	public void setTaxrates(Set<TaxRates> taxrates) {
		this.taxrates = taxrates;
	}

	public Set<FixedAsset> getFixedAssets() {
		return fixedAssets;
	}

	public void setFixedAssets(Set<FixedAsset> fixedAssets) {
		this.fixedAssets = fixedAssets;
	}

	public Set<TAXAdjustment> getTaxAdjustments() {
		return taxAdjustments;
	}

	public void setTaxAdjustments(Set<TAXAdjustment> taxAdjustments) {
		this.taxAdjustments = taxAdjustments;
	}

	public Set<TAXCode> getTaxCodes() {
		return taxCodes;
	}

	public void setTaxCodes(Set<TAXCode> taxCodes) {
		this.taxCodes = taxCodes;
	}

	public Set<TAXItemGroup> getTaxItemGroups() {
		return taxItemGroups;
	}

	public void setTaxItemGroups(Set<TAXItemGroup> taxItemGroups) {
		this.taxItemGroups = taxItemGroups;
		taxItems = new HashSet<TAXItem>();

		taxGroups = new HashSet<TAXGroup>();

		for (TAXItemGroup vatItemGroup : taxItemGroups) {

			if (vatItemGroup instanceof TAXItem) {
				taxItems.add((TAXItem) vatItemGroup);
			} else if (vatItemGroup instanceof TAXGroup) {
				taxGroups.add((TAXGroup) vatItemGroup);
			}
		}
	}

	public Set<Box> getVatBoxes() {
		return vatBoxes;
	}

	public void setVatBoxes(Set<Box> vatBoxes) {
		this.vatBoxes = vatBoxes;
	}

	public void setRestartSetupInterviews(boolean restartSetupInterviews) {
		this.restartSetupInterviews = restartSetupInterviews;
	}

	public void setVendors(Set<Vendor> vendors) {
		this.vendors = vendors;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public long getID() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void onDelete(Session session) {
		delete(transactions, session);
		delete(accounts, session);
		delete(customers, session);
		delete(vendors, session);
		delete(currencies, session);
		delete(fiscalYears, session);
		delete(paymentTerms, session);
		delete(taxCodes, session);
		delete(taxItems, session);
		delete(taxAgencies, session);
		delete(payees, session);
//		delete(nominalCodeRange, session);
		delete(brandingTheme, session);
		delete(usersList, session);
		delete(vatReturns, session);
		delete(currencies, session);
		delete(accounterClasses, session);
		delete(taxGroups, session);
		delete(items, session);
		delete(customerGroups, session);
		delete(vendorGroups, session);
		delete(shippingMethods, session);
		delete(shippingTerms, session);
		delete(priceLevels, session);
		delete(itemGroups, session);
		delete(paySalesTaxs, session);
		delete(creditRatings, session);
		delete(salesPersons, session);
		delete(banks, session);
		delete(taxrates, session);
		delete(fixedAssets, session);
		delete(taxAdjustments, session);
		delete(taxCodes, session);
		delete(taxItemGroups, session);
		delete(vatBoxes, session);
		delete(activities, session);
	}

	private static <T> void delete(Collection<T> list, Session session) {
		for (T acc : list) {
			session.delete(acc);
		}
	}
}
