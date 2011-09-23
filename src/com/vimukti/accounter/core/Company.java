package com.vimukti.accounter.core;

import java.util.ArrayList;
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

public class Company extends CreatableObject implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2342334234608152532L;

	private static final int TYPE_SOLE_PROPRIETORSHIP = 1;

	private static final int TYPE_PARTNERSHIP_OR_LLP = 2;

	private static final int TYPE_LLC_SINGLE_MEMBER = 3;

	private static final int TYPE_LLC_MULTI_MEMBER = 4;

	private static final int TYPE_CORPORATION = 5;

	private static final int TYPE_S_CORPORATION = 6;

	private static final int TYPE_NON_PROFIT = 7;
	public static final int TYPE_BASIC = 8;

	private static final int TYPE_OTHER_NONE = 99999;

	private static final int INDUSTRY_ACCOUNTING_OR_BOOKKEEPING = 11;

	private static final int ADVERTISING_OR_private_RELATIONS = 12;

	private static final int AGRICULTURE_RANCHING_OR_FARMING = 13;

	private static final int ARTWRITING_OR_PHOTOGRAPHY = 14;

	private static final int AUTOMOTICE_SALES_OR_REPAIR = 15;

	private static final int CUSTOMER_TYPE_CLIENTS = 45;

	private static final int CUSTOMER_TYPE_CUSTOMERS = 46;

	private static final int CUSTOMER_TYPE_DONORS = 47;

	private static final int CUSTOMER_TYPE_GUESTS = 48;

	private static final int CUSTOMER_TYPE_MEMBERS = 49;

	private static final int CUSTOMER_TYPE_PATIENTS = 50;

	private static final int CUSTOMER_TYPE_TENANTS = 51;

	public static final int ACCOUNTING_TYPE_US = 0;
	public static final int ACCOUNTING_TYPE_UK = 1;
	public static final int ACCOUNTING_TYPE_INDIA = 2;
	public static final int ACCOUNTING_TYPE_OTHER = 3;

	public static final String UK = "UK";

	public static final String US = "US";

	public static final String INDIA = "India";

	public static final String OTHER = "Other";

	int accountingType = 0;

	String companyID;

	private boolean isConfigured;
	/**
	 * this can hold a Set of {@link Address}
	 */
	// Set<Address> addresses = new HashSet<Address>();

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

	private ArrayList<Location> locations;
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
	Set<User> users = new HashSet<User>();

	Set<VATReturnBox> vatReturnBoxes = new HashSet<VATReturnBox>();

	private String registrationNumber;

	/**
	 * Each company have a set of {@link Account} This will hold all the
	 * Accounts created in this company.
	 */
	ArrayList<Account> accounts = new ArrayList<Account>();

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
	 * Each company has a set of PaymentTerms. This property can hold a Set of
	 * {@link PaymentTerms}
	 */
	ArrayList<PaymentTerms> paymentTerms = new ArrayList<PaymentTerms>();
	/**
	 * This represents a Set of {@link FiscalYear} We can create any number of
	 * Fiscal years and close them.
	 */
	ArrayList<FiscalYear> fiscalYears = new ArrayList<FiscalYear>();

	// Set<PayType> payTypes = new HashSet<PayType>();

	/**
	 * Each company can have a Set of {@link Payee}
	 * 
	 * @see Vendor
	 * @see Customer
	 * @see TaxAgency
	 * @see SalesPerson
	 */
	private ArrayList<Payee> payees = new ArrayList<Payee>();

	private ArrayList<TAXItem> taxItems = new ArrayList<TAXItem>();

	private ArrayList<TAXAgency> taxAgencies = new ArrayList<TAXAgency>();

	Set<NominalCodeRange> nominalCodeRange = new HashSet<NominalCodeRange>();

	/**
	 * This is the Account created by default for the purpose of UK when VAT is
	 * Filed
	 */
	Account VATFiledLiabilityAccount;

	ArrayList<BrandingTheme> brandingTheme = new ArrayList<BrandingTheme>();

	private ArrayList<User> usersList = new ArrayList<User>();

	// Set<PaySalesTaxEntries> paySalesTaxEntriesList = new
	// HashSet<PaySalesTaxEntries>();
	//
	// public Set<PaySalesTaxEntries> getPaySalesTaxEntriesList() {
	// return paySalesTaxEntriesList;
	// }
	//
	// public void setPaySalesTaxEntriesList(
	// Set<PaySalesTaxEntries> paySalesTaxEntriesList) {
	// this.paySalesTaxEntriesList = paySalesTaxEntriesList;
	// }

	// <<<<<<< .working
	// public void setVatItems(ArrayList<VATItem> vatItems) {
	// this.vatItems = vatItems;
	// }

	// public ArrayList<VATItem> getVatItems() {
	// FinanceDate presentDate = new FinanceDate();
	// if (vatItems != null) {
	// if (presentDate.before(new FinanceDate(111, 0, 4))) {
	// for (int i = 0; i < vatItems.size(); i++) {
	// VATItem vatItem = vatItems.get(i);
	// if (vatItem != null
	// && vatItem.getName().startsWith("New Standard")) {
	// vatItems.remove(i);
	// i--;
	// }
	// }
	// } else {
	// for (int i = 0; i < vatItems.size(); i++) {
	// VATItem vatItem = vatItems.get(i);
	// if (vatItem != null
	// && vatItem.getName().startsWith("Standard")) {
	// vatItems.get(i).setName("Old " + vatItem.getName());
	// } else if (vatItem != null
	// && vatItem.getName().equals(
	// "New Standard Purchases")) {
	// vatItems.get(i).setName("Standard Purchases");
	// } else if (vatItem != null
	// && vatItem.getName().equals("New Standard Sales")) {
	// vatItems.get(i).setName("Standard Sales");
	// }
	// }
	// }
	// }
	// return vatItems;
	// }

	private ArrayList<VATReturn> vatReturns = new ArrayList<VATReturn>();

	private Set<Currency> currencies = new HashSet<Currency>();

	private ArrayList<AccounterClass> accounterClasses = new ArrayList<AccounterClass>();

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	private String countryCode = "United Kingdom";

	public void setVatReturns(ArrayList<VATReturn> vatReturns) {
		this.vatReturns = vatReturns;
	}

	public ArrayList<VATReturn> getVatReturns() {
		return vatReturns;
	}

	ArrayList<TAXGroup> taxGroups = new ArrayList<TAXGroup>();

	/**
	 * @return the paymentTerms
	 */
	public ArrayList<PaymentTerms> getPaymentTerms() {
		return paymentTerms;
	}

	/**
	 * @param paymentTerms
	 *            the paymentTerms to set
	 */
	public void setPaymentTerms(ArrayList<PaymentTerms> paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	/**
	 * @return the customers
	 */
	public ArrayList<Customer> getCustomers() {
		return customers;
	}

	/**
	 * @param customers
	 *            the customers to set
	 */
	public void setCustomers(ArrayList<Customer> customers) {
		this.customers = customers;
	}

	/**
	 * @return the banks
	 */
	public ArrayList<Bank> getBanks() {
		return banks;
	}

	/**
	 * @param banks
	 *            the banks to set
	 */
	public void setBanks(ArrayList<Bank> banks) {
		this.banks = banks;
	}

	/**
	 * @return the taxrates
	 */
	public ArrayList<TaxRates> getTaxrates() {
		return taxrates;
	}

	/**
	 * @param taxrates
	 *            the taxrates to set
	 */
	public void setTaxrates(ArrayList<TaxRates> taxrates) {
		this.taxrates = taxrates;
	}

	/**
	 * @param taxAgencies
	 *            the taxAgencies to set
	 */
	public void setTaxAgencies(ArrayList<TAXAgency> taxAgencies) {
		this.taxAgencies = taxAgencies;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	/**
	 * @param customerGroups
	 *            the customerGroups to set
	 */
	public void setCustomerGroups(ArrayList<CustomerGroup> customerGroups) {
		this.customerGroups = customerGroups;
	}

	/**
	 * @param vendorGroups
	 *            the vendorGroups to set
	 */
	public void setVendorGroups(ArrayList<VendorGroup> vendorGroups) {
		this.vendorGroups = vendorGroups;
	}

	/**
	 * @param shippingTerms
	 *            the shippingTerms to set
	 */
	public void setShippingTerms(ArrayList<ShippingTerms> shippingTerms) {
		this.shippingTerms = shippingTerms;
	}

	/**
	 * @param shippingMethods
	 *            the shippingMethods to set
	 */
	public void setShippingMethods(ArrayList<ShippingMethod> shippingMethods) {
		this.shippingMethods = shippingMethods;
	}

	/**
	 * @param priceLevels
	 *            the priceLevels to set
	 */
	public void setPriceLevels(ArrayList<PriceLevel> priceLevels) {
		this.priceLevels = priceLevels;
	}

	/**
	 * @param itemGroups
	 *            the itemGroups to set
	 */
	public void setItemGroups(ArrayList<ItemGroup> itemGroups) {
		ItemGroups = itemGroups;
	}

	/**
	 * @param taxGroups
	 *            the taxGroups to set
	 */
	public void setTaxGroups(ArrayList<TAXGroup> taxGroups) {
		this.taxGroups = taxGroups;
	}

	/**
	 * @param paySalesTaxs
	 *            the paySalesTaxs to set
	 */
	public void setPaySalesTaxs(ArrayList<PaySalesTax> paySalesTaxs) {
		this.paySalesTaxs = paySalesTaxs;
	}

	/**
	 * @param creditRatings
	 *            the creditRatings to set
	 */
	public void setCreditRatings(ArrayList<CreditRating> creditRatings) {
		this.creditRatings = creditRatings;
	}

	/**
	 * @param salesPersons
	 *            the salesPersons to set
	 */
	public void setSalesPersons(ArrayList<SalesPerson> salesPersons) {
		this.salesPersons = salesPersons;
	}

	/**
	 * @param taxCodes
	 *            the taxCodes to set
	 */
	public void setTaxCodes(ArrayList<TAXCode> taxCodes) {
		this.taxCodes = taxCodes;
	}

	public void setTaxItems(ArrayList<TAXItem> taxItems) {
		this.taxItems = taxItems;
	}

	public Set<NominalCodeRange> getNominalCodeRange() {
		return nominalCodeRange;
	}

	public void setNominalCodeRange(Set<NominalCodeRange> nominalCodeRange) {
		this.nominalCodeRange = nominalCodeRange;
	}

	private ArrayList<Customer> customers;

	private ArrayList<Vendor> vendors;

	private ArrayList<Item> items;

	private ArrayList<CustomerGroup> customerGroups;

	private ArrayList<VendorGroup> vendorGroups;

	private ArrayList<ShippingTerms> shippingTerms;

	private ArrayList<ShippingMethod> shippingMethods;

	private ArrayList<PriceLevel> priceLevels;

	private ArrayList<ItemGroup> ItemGroups;

	private ArrayList<PaySalesTax> paySalesTaxs;

	private ArrayList<CreditRating> creditRatings;

	private ArrayList<SalesPerson> salesPersons;

	private ArrayList<Bank> banks;

	private ArrayList<TaxRates> taxrates;

	private ArrayList<FixedAsset> fixedAssets;

	private ArrayList<TAXAdjustment> taxAdjustments;

	private ArrayList<TAXCode> taxCodes;

	private ArrayList<TAXItemGroup> taxItemGroups;

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

	private Set<Box> vatBoxes;

	public ArrayList<FiscalYear> getFiscalYears() {
		return fiscalYears;
	}

	public void setFiscalYears(ArrayList<FiscalYear> fiscalYears) {
		this.fiscalYears = fiscalYears;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public ArrayList<TAXCode> getTaxCodes() {
		return taxCodes;
	}

	public ArrayList<TAXItem> getTaxItems() {
		return taxItems;
	}

	// public void setTaxCodes(Set<TaxCode> taxCodes) {
	// this.taxCodes = taxCodes;
	// }

	/**
	 * Called Later at Process Command
	 * 
	 * @param accounts
	 */

	public ArrayList<Item> getItems() {
		return items;
	}

	// public void setItems(Set<Item> items) {
	// this.items = items;
	// }

	public ArrayList<CustomerGroup> getCustomerGroups() {
		return customerGroups;
	}

	// public void setCustomerGroups(Set<CustomerGroup> customerGroups) {
	// this.customerGroups = customerGroups;
	// }

	public ArrayList<TAXGroup> getTaxGroups() {
		return taxGroups;
	}

	public void setTaxAdjustments(ArrayList<TAXAdjustment> taxAdjustments) {
		this.taxAdjustments = taxAdjustments;
	}

	public ArrayList<TAXAdjustment> getTaxAdjustments() {
		return taxAdjustments;
	}

	// public void setTaxGroups(Set<TaxGroup> taxGroups) {
	// this.taxGroups = taxGroups;
	// }

	public ArrayList<PaySalesTax> getPaySalesTaxs() {
		return paySalesTaxs;
	}

	// public void setPaySalesTaxs(Set<PaySalesTax> paySalesTaxs) {
	// this.paySalesTaxs = paySalesTaxs;
	// }

	public ArrayList<VendorGroup> getVendorGroups() {
		return vendorGroups;
	}

	// public void setVendorGroups(Set<VendorGroup> vendorGroups) {
	// this.vendorGroups = vendorGroups;
	// }

	public ArrayList<ShippingTerms> getShippingTerms() {
		return shippingTerms;
	}

	// public void setShippingTerms(Set<ShippingTerms> shippingTerms) {
	// this.shippingTerms = shippingTerms;
	// }

	public ArrayList<ShippingMethod> getShippingMethods() {
		return shippingMethods;
	}

	// public void setShippingMethods(Set<ShippingMethod> shippingMethods) {
	// this.shippingMethods = shippingMethods;
	// }

	public ArrayList<PriceLevel> getPriceLevels() {
		return priceLevels;
	}

	// public void setPriceLevels(Set<PriceLevel> priceLevels) {
	// this.priceLevels = priceLevels;
	// }

	public ArrayList<ItemGroup> getItemGroups() {
		return ItemGroups;
	}

	public ArrayList<CreditRating> getCreditRatings() {
		return creditRatings;
	}

	public ArrayList<SalesPerson> getSalesPersons() {
		return salesPersons;
	}

	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;

	}

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
		/*
		 * Session session = HibernateUtil.getCurrentSession(); switch
		 * (accountingType) { case ACCOUNTING_TYPE_US:
		 * 
		 * //initDefaultUSAccounts(session); break; case ACCOUNTING_TYPE_UK:
		 * //initDefaultUKAccounts(session); break; case ACCOUNTING_TYPE_INDIA:
		 * break; }
		 */
	}

	/*
	 * public Company(int accountingType, Session session) {
	 * 
	 * this.accountingType = accountingType; switch (accountingType) { case
	 * ACCOUNTING_TYPE_US: //initDefaultUSAccounts(session); break; case
	 * ACCOUNTING_TYPE_UK: new
	 * UKCompanyInitializer().createUKDefaultVATCodesAndVATAgency(session);
	 * //initDefaultUKAccounts(session); break; case ACCOUNTING_TYPE_INDIA:
	 * break; } }
	 */

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

	/*
	 * private void initDefaultUKAccounts(Session session) {
	 * 
	 * FinanceDate currentDate = new FinanceDate(); FinanceDate
	 * fiscalYearStartDate = new FinanceDate( (int) currentDate.getYear(), 0,
	 * 1); FinanceDate fiscalYearEndDate = new FinanceDate( (int)
	 * currentDate.getYear(), 11, 31);
	 * 
	 * FiscalYear fiscalYear = new FiscalYear(fiscalYearStartDate,
	 * fiscalYearEndDate, FiscalYear.STATUS_OPEN, Boolean.TRUE);
	 * 
	 * session.save(fiscalYear);
	 * 
	 * // Create Default PayTypes
	 * 
	 * // Set Default Preferences // SimpleDateFormat format = new
	 * SimpleDateFormat("yyyy-MM-dd"); CompanyPreferences preferences = new
	 * CompanyPreferences(); try { preferences.setUseAccountNumbers(true);
	 * preferences.setUseClasses(false); preferences.setUseJobs(false);
	 * preferences.setUseChangeLog(false);
	 * preferences.setAllowDuplicateDocumentNumbers(true);
	 * preferences.setDoYouPaySalesTax(false);
	 * preferences.setIsAccuralBasis(true); //
	 * preferences.setStartOfFiscalYear(format.parse("2009-01-01")); //
	 * preferences.setEndOfFiscalYear(format.parse("2009-12-31"));
	 * 
	 * preferences.setStartOfFiscalYear(fiscalYearStartDate);
	 * preferences.setEndOfFiscalYear(fiscalYearEndDate);
	 * preferences.setUseForeignCurrency(false);
	 * preferences.setUseCustomerId(false);
	 * preferences.setDefaultShippingTerm(null);
	 * preferences.setDefaultAnnualInterestRate(0);
	 * preferences.setDefaultMinimumFinanceCharge(0D);
	 * preferences.setGraceDays(3);
	 * preferences.setDoesCalculateFinanceChargeFromInvoiceDate(true);
	 * preferences.setUseVendorId(false); preferences.setUseItemNumbers(false);
	 * preferences.setCheckForItemQuantityOnHand(true);
	 * preferences.setUpdateCostAutomatically(false);
	 * preferences.setStartDate(fiscalYearStartDate);
	 * preferences.setPreventPostingBeforeDate(fiscalYearStartDate);
	 * 
	 * FinanceDate depreciationStartDateCal = new FinanceDate();
	 * depreciationStartDateCal.setTime(fiscalYearStartDate); //
	 * depreciationStartDateCal.set(Calendar.DAY_OF_MONTH, 01);
	 * preferences.setDepreciationStartDate(depreciationStartDateCal);
	 * 
	 * this.setPreferences(preferences);
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace(); }
	 * 
	 * Account openingBalances = new Account(Account.TYPE_EQUITY, "3040",
	 * AccounterConstants.OPENING_BALANCE, true, null,
	 * Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true, null, "4",
	 * true, this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(openingBalances);
	 * 
	 * Account accountsReceivable = new Account(
	 * Account.TYPE_OTHER_CURRENT_ASSET, "1001",
	 * AccounterConstants.ACCOUNTS_RECEIVABLE, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, false,
	 * openingBalances, "2", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(accountsReceivable);
	 * 
	 * Account deposits = new Account(Account.TYPE_OTHER_CURRENT_ASSET, "1003",
	 * AccounterConstants.DEPOSITS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "100", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(deposits);
	 * 
	 * Account accountsPayable = new Account(
	 * Account.TYPE_OTHER_CURRENT_LIABILITY, "2001",
	 * AccounterConstants.ACCOUNTS_PAYABLE, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "3", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(accountsPayable);
	 * 
	 * // Account pendingItemReceipts = new Account( //
	 * Account.TYPE_OTHER_CURRENT_LIABILITY, "2010", //
	 * AccounterConstants.PENDING_ITEM_RECEIPTS, true, null, //
	 * Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true, //
	 * openingBalances, "4", true, this.preferences //
	 * .getPreventPostingBeforeDate()); // // session.save(pendingItemReceipts);
	 * 
	 * Account salesIncomeTypeA = new Account(Account.TYPE_INCOME, "4001",
	 * AccounterConstants.SALES_INCOME_TYPE_A, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "5", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(salesIncomeTypeA);
	 * 
	 * Account salesIncomeTypeB = new Account(Account.TYPE_INCOME, "4002",
	 * AccounterConstants.SALES_INCOME_TYPE_B, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "6", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * session.save(salesIncomeTypeB);
	 * 
	 * Account salesIncomeTypeC = new Account(Account.TYPE_INCOME, "4003",
	 * AccounterConstants.SALES_INCOME_TYPE_C, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "7", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(salesIncomeTypeC);
	 * 
	 * Account salesIncomeTypeD = new Account(Account.TYPE_INCOME, "4004",
	 * AccounterConstants.SALES_INCOME_TYPE_D, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "8", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(salesIncomeTypeD);
	 * 
	 * Account salesIncomeTypeE = new Account(Account.TYPE_INCOME, "4005",
	 * AccounterConstants.SALES_INCOME_TYPE_E, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "9", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(salesIncomeTypeE);
	 * 
	 * Account miscellaneousIncome = new Account(Account.TYPE_INCOME, "4100",
	 * AccounterConstants.MISCELLANEOUS_INCOME, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "10", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(miscellaneousIncome);
	 * 
	 * Account distributionAndCarriage = new Account(Account.TYPE_INCOME,
	 * "4110", AccounterConstants.DISTRIBUTION_AND_CARRIAGE, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "11", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(distributionAndCarriage);
	 * 
	 * Account discounts = new Account(Account.TYPE_INCOME, "4120",
	 * AccounterConstants.DISCOUNTS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "12", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(discounts);
	 * 
	 * Account commissionsReceived = new Account(Account.TYPE_INCOME, "4200",
	 * AccounterConstants.COMMISSION_RECIEVED, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "13", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(commissionsReceived);
	 * 
	 * Account creditChargesLatePayment = new Account(Account.TYPE_INCOME,
	 * "4210", AccounterConstants.CREDIT_CHARGES_LATEPAYMENT, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "14", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(creditChargesLatePayment);
	 * 
	 * Account insuranceClaims = new Account(Account.TYPE_INCOME, "4220",
	 * AccounterConstants.INSURANCE_CLAIMS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "15", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(insuranceClaims);
	 * 
	 * Account interestIncome = new Account(Account.TYPE_INCOME, "4230",
	 * AccounterConstants.INTEREST_INCOME, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "16", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(interestIncome);
	 * 
	 * Account rentIncome = new Account(Account.TYPE_INCOME, "4240",
	 * AccounterConstants.RENT_INCOME, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "17", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(rentIncome);
	 * 
	 * Account royltiesReceived = new Account(Account.TYPE_INCOME, "4250",
	 * AccounterConstants.ROYALTIES_RECIEVED, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "18", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(royltiesReceived);
	 * 
	 * Account profitOrLossOnSalesOfAssets = new Account(Account.TYPE_INCOME,
	 * "4260", AccounterConstants.PROFIT_OR_LOSS_ON_SALES_ASSETS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "19", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(profitOrLossOnSalesOfAssets);
	 * 
	 * 
	 * 
	 * Account productsOrMaterialsPurchasedTypeA = new Account(
	 * Account.TYPE_COST_OF_GOODS_SOLD, "5001",
	 * AccounterConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_A, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "20", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(productsOrMaterialsPurchasedTypeA);
	 * 
	 * Account productsOrMaterialsPurchasedTypeB = new Account(
	 * Account.TYPE_COST_OF_GOODS_SOLD, "5002",
	 * AccounterConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_B, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "21", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(productsOrMaterialsPurchasedTypeB);
	 * 
	 * Account productsOrMaterialsPurchasedTypeC = new Account(
	 * Account.TYPE_COST_OF_GOODS_SOLD, "5003",
	 * AccounterConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_C, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "3", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(productsOrMaterialsPurchasedTypeC);
	 * 
	 * Account productsOrMaterialsPurchasedTypeD = new Account(
	 * Account.TYPE_COST_OF_GOODS_SOLD, "5004",
	 * AccounterConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_D, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "22", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(productsOrMaterialsPurchasedTypeD);
	 * 
	 * Account productsOrMaterialsPurchasedTypeE = new Account(
	 * Account.TYPE_COST_OF_GOODS_SOLD, "5005",
	 * AccounterConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_E, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "23", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(productsOrMaterialsPurchasedTypeE);
	 * 
	 * Account carriage = new Account(Account.TYPE_COST_OF_GOODS_SOLD, "5200",
	 * AccounterConstants.CARRIAGE, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "28", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(carriage);
	 * 
	 * Account discountsTaken = new Account(Account.TYPE_COST_OF_GOODS_SOLD,
	 * "5210", AccounterConstants.DISCOUNTS_TAKEN, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "29", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(discountsTaken);
	 * 
	 * Account importDuty = new Account(Account.TYPE_COST_OF_GOODS_SOLD, "5220",
	 * AccounterConstants.IMPORT_DUTY, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "30", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(importDuty);
	 * 
	 * Account openingStock = new Account(Account.TYPE_COST_OF_GOODS_SOLD,
	 * "5900", AccounterConstants.OPENING_STOCK, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "24", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(openingStock);
	 * 
	 * // Account closingStock = new Account(Account.TYPE_COST_OF_GOODS_SOLD, //
	 * "5910", AccounterConstants.CLOSING_STOCK, true, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false, //
	 * openingBalances, "25", true, this.preferences //
	 * .getPreventPostingBeforeDate()); // // session.save(closingStock);
	 * 
	 * Account openingFinishedGoods = new Account(
	 * Account.TYPE_COST_OF_GOODS_SOLD, "5920",
	 * AccounterConstants.OPEN_FINISHED_GOODS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "26", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(openingFinishedGoods);
	 * 
	 * // Account closingFinishedGoods = new Account( //
	 * Account.TYPE_COST_OF_GOODS_SOLD, "5925", //
	 * AccounterConstants.CLOSE_FINISHED_GOODS, true, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false, //
	 * openingBalances, "151", true, this.preferences //
	 * .getPreventPostingBeforeDate()); // //
	 * session.save(closingFinishedGoods);
	 * 
	 * Account openingWorkInProgress = new Account(
	 * Account.TYPE_COST_OF_GOODS_SOLD, "5930",
	 * AccounterConstants.OPEN_WORK_IN_PROGRESS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "27", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(openingWorkInProgress); // // Account closingWorkInProgress
	 * = new Account( // Account.TYPE_COST_OF_GOODS_SOLD, "5935", //
	 * AccounterConstants.CLOSE_WORK_IN_PROGRESS, true, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false, //
	 * openingBalances, "152", true, this.preferences //
	 * .getPreventPostingBeforeDate()); // //
	 * session.save(closingWorkInProgress);
	 * 
	 * 
	 * 
	 * Account directLabour = new Account(Account.TYPE_OTHER_EXPENSE, "6001",
	 * AccounterConstants.DIRECT_LABOUR, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "31", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(directLabour);
	 * 
	 * Account directEmployersNI = new Account(Account.TYPE_OTHER_EXPENSE,
	 * "6010", AccounterConstants.DIRECT_EMPLOYERS_NI, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "32", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(directEmployersNI);
	 * 
	 * Account otherDirectEmployeeRelatedCosts = new Account(
	 * Account.TYPE_OTHER_EXPENSE, "6020",
	 * AccounterConstants.OTHER_DIRECT_EMPLOYEE_RELATED_COSTS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "33", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(otherDirectEmployeeRelatedCosts);
	 * 
	 * Account directExpenses = new Account(Account.TYPE_OTHER_EXPENSE, "6100",
	 * AccounterConstants.DIRECT_EXPENSES, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "34", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(directExpenses);
	 * 
	 * Account directTravel = new Account(Account.TYPE_OTHER_EXPENSE, "6150",
	 * AccounterConstants.DIRECT_TRAVEL, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "35", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(directTravel);
	 * 
	 * Account directConsumables = new Account(Account.TYPE_OTHER_EXPENSE,
	 * "6200", AccounterConstants.DIRECT_CONSUMABLES, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "36", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(directConsumables);
	 * 
	 * Account merchantAccountFees = new Account(Account.TYPE_OTHER_EXPENSE,
	 * "6300", AccounterConstants.MERCHANT_ACCOUNT_FEES, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "37", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(merchantAccountFees);
	 * 
	 * Account commisionsPaid = new Account(Account.TYPE_OTHER_EXPENSE, "6310",
	 * AccounterConstants.COMMISSIONS_PAID, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "38", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(commisionsPaid);
	 * 
	 * Account indirectLabour = new Account(Account.TYPE_EXPENSE, "7001",
	 * AccounterConstants.INDIRECT_LABOUR, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "39", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(indirectLabour);
	 * 
	 * Account indirectEmployersNI = new Account(Account.TYPE_EXPENSE, "7002",
	 * AccounterConstants.INDIRECT_EMPLOYERS_NI, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "40", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(indirectEmployersNI);
	 * 
	 * Account directorsRemunaration = new Account(Account.TYPE_EXPENSE, "7003",
	 * AccounterConstants.DIRECTORS_REMUNERATION, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "41", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(directorsRemunaration);
	 * 
	 * Account casualLabour = new Account(Account.TYPE_EXPENSE, "7004",
	 * AccounterConstants.CASUAL_LABOUR, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "42", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(casualLabour);
	 * 
	 * Account employersPensionContributions = new Account(
	 * Account.TYPE_EXPENSE, "7010",
	 * AccounterConstants.EMPLOYERS_PANSION_CONTRIBUTIONS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "43", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(employersPensionContributions);
	 * 
	 * Account sspReclaimed = new Account(Account.TYPE_EXPENSE, "7020",
	 * AccounterConstants.SSP_RECLAIMED, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "44", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(sspReclaimed);
	 * 
	 * Account smpReclaimed = new Account(Account.TYPE_EXPENSE, "7021",
	 * AccounterConstants.SMP_RECLAIMED, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "45", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(smpReclaimed);
	 * 
	 * Account employeeBenifits = new Account(Account.TYPE_EXPENSE, "7025",
	 * AccounterConstants.EMPLOYEE_BENIFITS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "46", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(employeeBenifits);
	 * 
	 * Account medicalInsurance = new Account(Account.TYPE_EXPENSE, "7030",
	 * AccounterConstants.MEDICAL_INSURANCE, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "47", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(medicalInsurance);
	 * 
	 * Account recruitement = new Account(Account.TYPE_EXPENSE, "7040",
	 * AccounterConstants.RECRUITMENT, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "48", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(recruitement);
	 * 
	 * Account training = new Account(Account.TYPE_EXPENSE, "7045",
	 * AccounterConstants.TRAINING, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "49", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(training);
	 * 
	 * Account rent = new Account(Account.TYPE_EXPENSE, "7100",
	 * AccounterConstants.RENT, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "50", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(rent);
	 * 
	 * Account generalRates = new Account(Account.TYPE_EXPENSE, "7101",
	 * AccounterConstants.GENERAL_RATES, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "51", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(generalRates);
	 * 
	 * Account waterRates = new Account(Account.TYPE_EXPENSE, "7102",
	 * AccounterConstants.WATER_RATES, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "52", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(waterRates);
	 * 
	 * Account electricity = new Account(Account.TYPE_EXPENSE, "7110",
	 * AccounterConstants.ELECTRICITY, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "53", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(electricity);
	 * 
	 * Account gas = new Account(Account.TYPE_EXPENSE, "7111",
	 * AccounterConstants.GAS, true, null, Account.CASH_FLOW_CATEGORY_OPERATING,
	 * 0.0, false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null,
	 * true, false, openingBalances, "54", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(gas);
	 * 
	 * Account oil = new Account(Account.TYPE_EXPENSE, "7112",
	 * AccounterConstants.OIL, true, null, Account.CASH_FLOW_CATEGORY_OPERATING,
	 * 0.0, false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null,
	 * true, false, openingBalances, "55", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(oil);
	 * 
	 * Account officeCleaning = new Account(Account.TYPE_EXPENSE, "7120",
	 * AccounterConstants.OFFICE_CLEANING, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "56", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(officeCleaning);
	 * 
	 * Account officeMachineMaintanance = new Account(Account.TYPE_EXPENSE,
	 * "7130", AccounterConstants.OFFICE_MACHINE_MAINTENANCE, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "57", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(officeMachineMaintanance);
	 * 
	 * Account repairsAndRenewals = new Account(Account.TYPE_EXPENSE, "7140",
	 * AccounterConstants.REPAIR_RENEWALS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "58", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(repairsAndRenewals);
	 * 
	 * Account officeConsumables = new Account(Account.TYPE_EXPENSE, "7150",
	 * AccounterConstants.OFFICE_CONSUMABLES, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "59", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(officeConsumables);
	 * 
	 * Account booksEtc = new Account(Account.TYPE_EXPENSE, "7151",
	 * AccounterConstants.BOOKS_ETC, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "60", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(booksEtc);
	 * 
	 * Account internet = new Account(Account.TYPE_EXPENSE, "7152",
	 * AccounterConstants.INTERNET, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "61", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(internet);
	 * 
	 * Account postage = new Account(Account.TYPE_EXPENSE, "7153",
	 * AccounterConstants.POSTAGE, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "62", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(postage);
	 * 
	 * Account printing = new Account(Account.TYPE_EXPENSE, "7154",
	 * AccounterConstants.PRINTING, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "63", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(printing);
	 * 
	 * Account stationary = new Account(Account.TYPE_EXPENSE, "7155",
	 * AccounterConstants.STATIONERY, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "64", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(stationary);
	 * 
	 * Account subscriptions = new Account(Account.TYPE_EXPENSE, "7156",
	 * AccounterConstants.SUBSCRIPTIONS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "65", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(subscriptions);
	 * 
	 * Account telephone = new Account(Account.TYPE_EXPENSE, "7157",
	 * AccounterConstants.TELEPHONE, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "66", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(telephone);
	 * 
	 * Account conferenceAndSeminars = new Account(Account.TYPE_EXPENSE, "7158",
	 * AccounterConstants.CONFERENCES_AND_SEMINARS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "67", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(conferenceAndSeminars);
	 * 
	 * Account charityDonations = new Account(Account.TYPE_EXPENSE, "7160",
	 * AccounterConstants.CHARITY_DONATIONS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "68", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(charityDonations);
	 * 
	 * Account insurencesBusiness = new Account(Account.TYPE_EXPENSE, "7200",
	 * AccounterConstants.INSURANCES_BUSINESS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "69", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(insurencesBusiness);
	 * 
	 * Account advertisiningAndMarketing = new Account(Account.TYPE_EXPENSE,
	 * "7250", AccounterConstants.ADVERTISING_AND_MARKETING, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "70", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(advertisiningAndMarketing);
	 * 
	 * Account localEntertainment = new Account(Account.TYPE_EXPENSE, "7260",
	 * AccounterConstants.LOCAL_ENTERTAINMENT, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "71", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(localEntertainment);
	 * 
	 * Account overseasEntertainment = new Account(Account.TYPE_EXPENSE, "7261",
	 * AccounterConstants.OVERSEAS_ENTERTAINMENT, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "72", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(overseasEntertainment);
	 * 
	 * Account indirectLocalTravel = new Account(Account.TYPE_EXPENSE, "7270",
	 * AccounterConstants.INDIRECT_LOCAL_TRAVEL, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "73", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(indirectLocalTravel);
	 * 
	 * Account indirectOverseasTravel = new Account(Account.TYPE_EXPENSE,
	 * "7271", AccounterConstants.INDIRECT_OVERSEAS_TRAVEL, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "74", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(indirectOverseasTravel);
	 * 
	 * Account subsitence = new Account(Account.TYPE_EXPENSE, "7280",
	 * AccounterConstants.SUBSISTENCE, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "75", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(subsitence);
	 * 
	 * Account vechileExpenses = new Account(Account.TYPE_EXPENSE, "7300",
	 * AccounterConstants.VECHILE_EXPENSES, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "76", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(vechileExpenses);
	 * 
	 * Account vechileInsurance = new Account(Account.TYPE_EXPENSE, "7310",
	 * AccounterConstants.VECHILE_INSURANCE, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "77", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(vechileInsurance);
	 * 
	 * Account vechileRepairAndServicing = new Account(Account.TYPE_EXPENSE,
	 * "7320", AccounterConstants.VECHILE_REPAIRS_AND_SERVICING, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "78", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(vechileRepairAndServicing);
	 * 
	 * Account professonalFees = new Account(Account.TYPE_EXPENSE, "7350",
	 * AccounterConstants.PROFESSIONAL_FEES, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "79", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(professonalFees);
	 * 
	 * Account accountancyFees = new Account(Account.TYPE_EXPENSE, "7360",
	 * AccounterConstants.ACCOUNTANCY_FEES, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "80", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(accountancyFees);
	 * 
	 * Account consultancyFees = new Account(Account.TYPE_EXPENSE, "7370",
	 * AccounterConstants.CONSULTANY_FEES, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "81", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(consultancyFees);
	 * 
	 * Account legalFees = new Account(Account.TYPE_EXPENSE, "7380",
	 * AccounterConstants.LEGAL_FEES, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "82", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(legalFees);
	 * 
	 * Account bankInterestPaid = new Account(Account.TYPE_EXPENSE, "7400",
	 * AccounterConstants.BANK_INTEREST_PAID, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "83", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(bankInterestPaid);
	 * 
	 * Account bankCharges = new Account(Account.TYPE_EXPENSE, "7410",
	 * AccounterConstants.BANK_CHARGES, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "84", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(bankCharges);
	 * 
	 * Account creditCharges = new Account(Account.TYPE_EXPENSE, "7420",
	 * AccounterConstants.CREDIT_CHARGES, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "85", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(creditCharges);
	 * 
	 * Account leasePayments = new Account(Account.TYPE_EXPENSE, "7430",
	 * AccounterConstants.LEASE_PAYMENTS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "86", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(leasePayments);
	 * 
	 * Account loanInterestPaid = new Account(Account.TYPE_EXPENSE, "7440",
	 * AccounterConstants.LOAN_INTEREST_PAID, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "87", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(loanInterestPaid);
	 * 
	 * Account currencyCharges = new Account(Account.TYPE_EXPENSE, "7450",
	 * AccounterConstants.CURRENCY_CHARGES, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "88", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(currencyCharges);
	 * 
	 * Account exchangeRateVariance = new Account(Account.TYPE_EXPENSE, "7460",
	 * AccounterConstants.EXCHANGE_RATE_VARIANCE, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "89", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(exchangeRateVariance);
	 * 
	 * Account badDebtProvision = new Account(Account.TYPE_EXPENSE, "7470",
	 * AccounterConstants.BAD_DEBT_PROVISION, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "90", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(badDebtProvision);
	 * 
	 * Account badDebtWriteOff = new Account(Account.TYPE_EXPENSE, "7480",
	 * AccounterConstants.BAD_DEBT_WRITE_OFF, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "91", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(badDebtWriteOff);
	 * 
	 * Account depreciation = new Account(Account.TYPE_EXPENSE, "7500",
	 * AccounterConstants.DEPRECIATION, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "92", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(depreciation);
	 * 
	 * Account officeEquipmentDepreciation = new Account(Account.TYPE_EXPENSE,
	 * "7510", AccounterConstants.OFFICE_EQUIPMENT_DEPRECIATION, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "93", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(officeEquipmentDepreciation);
	 * 
	 * Account itEquipmentDepreciation = new Account(Account.TYPE_EXPENSE,
	 * "7520", AccounterConstants.IT_EQUIPMENT_DEPRECIATION, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "94", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(itEquipmentDepreciation);
	 * 
	 * Account furnituresAndFixuresDepreciation = new Account(
	 * Account.TYPE_EXPENSE, "7530",
	 * AccounterConstants.FURNITURE_AND_FIXTURES_DEPRECIARION, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "95", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(furnituresAndFixuresDepreciation);
	 * 
	 * Account palntOrMachineryDepreciation = new Account( Account.TYPE_EXPENSE,
	 * "7540", AccounterConstants.PLANT_OR_MACHINERY_DEPRECIATION, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "96", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(palntOrMachineryDepreciation);
	 * 
	 * Account vechileDepreciation = new Account(Account.TYPE_EXPENSE, "7550",
	 * AccounterConstants.VECHILE_DEPRECIATION, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "97", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(vechileDepreciation);
	 * 
	 * Account freeHoldBuildingDepreciation = new Account( Account.TYPE_EXPENSE,
	 * "7560", AccounterConstants.FREEHOLD_BUILDING_DEPRECIATION, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "98", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(freeHoldBuildingDepreciation);
	 * 
	 * Account leaseHoldPropertyImprvmntsDepreciation = new Account(
	 * Account.TYPE_EXPENSE, "7570",
	 * AccounterConstants.LEASEHOLD_PROPERTY_IMPROVEMENTS_DEPRECIATION, true,
	 * null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "99", true, this.preferences
	 * .getPreventPostingBeforeDate());
	 * 
	 * session.save(leaseHoldPropertyImprvmntsDepreciation);
	 * 
	 * // Account debtorsAccountsReceivable = new Account( //
	 * Account.TYPE_OTHER_CURRENT_ASSET, "1002", //
	 * AccounterConstants.DEBTORS_ACCOUNTS_RECEIVABLE, true, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false, //
	 * openingBalances, "100", true, this.preferences //
	 * .getPreventPostingBeforeDate()); // //
	 * session.save(debtorsAccountsReceivable);
	 * 
	 * Account bankCurrentAccount = new Account(
	 * Account.TYPE_OTHER_CURRENT_ASSET, "1100",
	 * AccounterConstants.BANK_CURRENT_ACCOUNT, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "103", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(bankCurrentAccount);
	 * 
	 * Account bankDepositAccount = new Account(
	 * Account.TYPE_OTHER_CURRENT_ASSET, "1150",
	 * AccounterConstants.BANK_DEPOSIT_ACCOUNT, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "104", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(bankDepositAccount);
	 * 
	 * Account unDepositedFunds = new Account( Account.TYPE_OTHER_CURRENT_ASSET,
	 * "1175", AccounterConstants.UN_DEPOSITED_FUNDS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true,
	 * openingBalances, "1", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(unDepositedFunds);
	 * 
	 * Account pettyCash = new Account(Account.TYPE_OTHER_CURRENT_ASSET, "1180",
	 * AccounterConstants.PETTY_CASH, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "105", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(pettyCash);
	 * 
	 * Account prepayments = new Account(Account.TYPE_OTHER_CURRENT_ASSET,
	 * "1185", AccounterConstants.PRE_PAYMENTS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "106", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(prepayments);
	 * 
	 * Account advancesToEmployees = new Account(
	 * Account.TYPE_OTHER_CURRENT_ASSET, "1190",
	 * AccounterConstants.ADVANCES_TO_EMPLOYEES, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "107", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(advancesToEmployees);
	 * 
	 * Account stock = new Account(Account.TYPE_OTHER_CURRENT_ASSET, "1200",
	 * AccounterConstants.STOCK, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "108", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(stock);
	 * 
	 * // Account creditorsAccountsPayable = new Account( //
	 * Account.TYPE_OTHER_CURRENT_LIABILITY, "2002", //
	 * AccounterConstants.CREDITORS_ACCOUNTS_PAYBLE, true, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false, //
	 * openingBalances, "109", true, this.preferences //
	 * .getPreventPostingBeforeDate()); // //
	 * session.save(creditorsAccountsPayable);
	 * 
	 * Account creditCards = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
	 * "2050", AccounterConstants.CREDIT_CARDS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "110", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(creditCards);
	 * 
	 * Account payeeEmploymentTax = new Account(
	 * Account.TYPE_OTHER_CURRENT_LIABILITY, "2100",
	 * AccounterConstants.PAYEE_EMPLOYEMENT_TAX, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "111", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(payeeEmploymentTax);
	 * 
	 * Account nationalInsuranceTax = new Account(
	 * Account.TYPE_OTHER_CURRENT_LIABILITY, "2110",
	 * AccounterConstants.NATIONAL_INSURANCE_TAX, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "112", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(nationalInsuranceTax);
	 * 
	 * Account saelsTaxVAT = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
	 * "2120", AccounterConstants.SALES_TAX_VAT_UNFILED, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "113", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(saelsTaxVAT);
	 * 
	 * Account corporationTax = new Account(
	 * Account.TYPE_OTHER_CURRENT_LIABILITY, "2130",
	 * AccounterConstants.CORPORATION_RAX, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "114", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(corporationTax);
	 * 
	 * Account loans = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY, "2200",
	 * AccounterConstants.LOANS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "115", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(loans);
	 * 
	 * Account mortagages = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
	 * "2250", AccounterConstants.MORTGAGES, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "116", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(mortagages);
	 * 
	 * Account accruals = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
	 * "2300", AccounterConstants.ACCRUALS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "117", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(accruals);
	 * 
	 * Account directorsCurrentAccount = new Account(
	 * Account.TYPE_OTHER_CURRENT_LIABILITY, "2400",
	 * AccounterConstants.DIRECTORS_CURRENT_ACCOUNT, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "118", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(directorsCurrentAccount);
	 * 
	 * Account netSalaries = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
	 * "2500", AccounterConstants.NET_SALARIES, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "119", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(netSalaries);
	 * 
	 * Account pensions = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
	 * "2510", AccounterConstants.PENSIONS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "120", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(pensions);
	 * 
	 * Account unpaidExpenseClaims = new Account(
	 * Account.TYPE_OTHER_CURRENT_LIABILITY, "2520",
	 * AccounterConstants.UNPAID_EXPENSE_CLAIMS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "121", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(unpaidExpenseClaims);
	 * 
	 * Account freeHoldBuildings = new Account(Account.TYPE_FIXED_ASSET, "0001",
	 * AccounterConstants.FREEHOLD_BUILDINGS, true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "122", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(freeHoldBuildings);
	 * 
	 * Account acumulatedFreeHoldBuildingDepreciation = new Account(
	 * Account.TYPE_FIXED_ASSET, "0040",
	 * AccounterConstants.ACCUMULATED_FREEHOLD_BUILDING_DEPRECIATION, true,
	 * null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "123", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(acumulatedFreeHoldBuildingDepreciation);
	 * 
	 * Account leaseHoldPropertyImprovements = new Account(
	 * Account.TYPE_FIXED_ASSET, "0050",
	 * AccounterConstants.LEASEHOLD_PROPERTY_IMPROVEMENTS, true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "124", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(leaseHoldPropertyImprovements);
	 * 
	 * Account accmltdLeaseHoldPropertyImprvmntsDepreciation = new Account(
	 * Account.TYPE_FIXED_ASSET, "0090",
	 * AccounterConstants.ACCUMULATED_LEASE_HOLD_PROPERTY_IMPROVEMENTS_DEPRECIATION
	 * , true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "125", true, this.preferences
	 * .getPreventPostingBeforeDate());
	 * 
	 * session.save(accmltdLeaseHoldPropertyImprvmntsDepreciation);
	 * 
	 * Account officeEquipment = new Account(Account.TYPE_FIXED_ASSET, "0100",
	 * AccounterConstants.OFFICE_EQUIPMENT, true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "126", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(officeEquipment);
	 * 
	 * Account acumltdOfcEqupmntDepreciation = new Account(
	 * Account.TYPE_FIXED_ASSET, "0115",
	 * AccounterConstants.ACCUMULATED_OFFICE_EQUIPMENT_DEPRECIATION, true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "127", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(acumltdOfcEqupmntDepreciation);
	 * 
	 * Account itEquipment = new Account(Account.TYPE_FIXED_ASSET, "0120",
	 * AccounterConstants.IT_EQUIPMENT, true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "128", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(itEquipment);
	 * 
	 * Account accumltdITEquipmentDepreciation = new Account(
	 * Account.TYPE_FIXED_ASSET, "0135",
	 * AccounterConstants.ACCUMULATED_IT_EQUIPMENT_DEPRECIATION, true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "129", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(accumltdITEquipmentDepreciation);
	 * 
	 * Account furnitureAndFixtures = new Account(Account.TYPE_FIXED_ASSET,
	 * "0140", AccounterConstants.FURNITURE_AND_FIXTURES, true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "130", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(furnitureAndFixtures);
	 * 
	 * Account accumltdFurnitureAndFixturesDepreciation = new Account(
	 * Account.TYPE_FIXED_ASSET, "0155",
	 * AccounterConstants.ACCUMULATED_FURNITURE_AND_FIXTURES_DEPRECIATION, true,
	 * null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "131", true, this.preferences
	 * .getPreventPostingBeforeDate());
	 * 
	 * session.save(accumltdFurnitureAndFixturesDepreciation);
	 * 
	 * Account plantAndMachinary = new Account(Account.TYPE_FIXED_ASSET, "0160",
	 * AccounterConstants.PLANT_AND_MACHINERY, true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "132", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(plantAndMachinary);
	 * 
	 * Account accumltdPlantAndMachinaryDepreciation = new Account(
	 * Account.TYPE_FIXED_ASSET, "0175",
	 * AccounterConstants.ACCUMULATED_PLANT_AND_MACHINERY_DEPRECIATION, true,
	 * null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "133", true, this.preferences
	 * .getPreventPostingBeforeDate());
	 * 
	 * session.save(accumltdPlantAndMachinaryDepreciation);
	 * 
	 * Account vechiles = new Account(Account.TYPE_FIXED_ASSET, "0180",
	 * AccounterConstants.VECHICLES, true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "134", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(vechiles);
	 * 
	 * Account accumltdVechilesDepreciation = new Account(
	 * Account.TYPE_FIXED_ASSET, "0195",
	 * AccounterConstants.ACCUMULATED_VEHICLES_DEPRECIATION, true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "135", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(accumltdVechilesDepreciation);
	 * 
	 * Account intangibles = new Account(Account.TYPE_FIXED_ASSET, "0200",
	 * AccounterConstants.INTANGIBLES, true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "136", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(intangibles);
	 * 
	 * Account longTermLoans = new Account(Account.TYPE_LONG_TERM_LIABILITY,
	 * "9001", AccounterConstants.LONG_TERM_LOANS, true, null,
	 * Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "137", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(longTermLoans);
	 * 
	 * Account hirePurchaseCreditors = new Account(
	 * Account.TYPE_LONG_TERM_LIABILITY, "9100",
	 * AccounterConstants.HIRE_PURCHASE_CREDITORS, true, null,
	 * Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "138", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(hirePurchaseCreditors);
	 * 
	 * Account deferredTax = new Account(Account.TYPE_LONG_TERM_LIABILITY,
	 * "9200", AccounterConstants.DEFERRED_TAX, true, null,
	 * Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "139", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(deferredTax);
	 * 
	 * Account bankRevolutions = new Account(Account.TYPE_OTHER_ASSET, "9501",
	 * AccounterConstants.BANK_REVALUATIONS, true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "140", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(bankRevolutions);
	 * 
	 * Account historicalAdjustment = new Account(Account.TYPE_OTHER_ASSET,
	 * "9510", AccounterConstants.HISTORICAL_ADJUSTMENT, true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "141", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(historicalAdjustment);
	 * 
	 * Account realisedCurrencyGains = new Account(Account.TYPE_OTHER_ASSET,
	 * "9520", AccounterConstants.REALISED_CURRENCY_GAINS, true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "142", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(realisedCurrencyGains);
	 * 
	 * Account unrealisedCurrencyGains = new Account(Account.TYPE_OTHER_ASSET,
	 * "9530", AccounterConstants.UNREALISED_CURRENCY_GAINS, true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "143", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(unrealisedCurrencyGains);
	 * 
	 * Account rounding = new Account(Account.TYPE_OTHER_ASSET, "9540",
	 * AccounterConstants.ROUNDING, true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "144", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(rounding);
	 * 
	 * Account vatOnImports = new Account(Account.TYPE_OTHER_ASSET, "9550",
	 * AccounterConstants.VAT_ON_IMPORTS, true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "145", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(vatOnImports);
	 * 
	 * Account suspense = new Account(Account.TYPE_OTHER_ASSET, "9600",
	 * AccounterConstants.SUSPENSE, true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "146", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(suspense);
	 * 
	 * Account ordinaryShares = new Account(Account.TYPE_EQUITY, "3001",
	 * AccounterConstants.ORDINARY_SHARES, true, null,
	 * Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "147", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(ordinaryShares);
	 * 
	 * Account reservesRetainedEarnings = new Account(Account.TYPE_EQUITY,
	 * "3050", AccounterConstants.RESERVES_RETAINED_EARNINGS, true, null,
	 * Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "148", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(reservesRetainedEarnings);
	 * 
	 * Account PAndLBoughtForwardOrYTD = new Account(Account.TYPE_EQUITY,
	 * "3100", AccounterConstants.P_AND_L_BOUGHT_FORWARD_OR_YTD, true, null,
	 * Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "149", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(PAndLBoughtForwardOrYTD);
	 * 
	 * Account dividends = new Account(Account.TYPE_EQUITY, "3150",
	 * AccounterConstants.DIVIDENDS, true, null,
	 * Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "150", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(dividends);
	 * 
	 * Account salesTaxVATFiled = new Account(
	 * Account.TYPE_OTHER_CURRENT_LIABILITY, "2122",
	 * AccounterConstants.SALES_TAX_VAT_FILED, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "151", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(salesTaxVATFiled);
	 * 
	 * // Account VATliabilityAccount = new Account( //
	 * Account.TYPE_OTHER_CURRENT_LIABILITY, "2699", //
	 * AccounterConstants.VAT_LIABILITY_ACCOUNT, true, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true, //
	 * openingBalances, "7", true, //
	 * this.preferences.getPreventPostingBeforeDate()); // //
	 * session.save(VATliabilityAccount);
	 * 
	 * // Account salesOutputVAT = new Account( //
	 * Account.TYPE_OTHER_CURRENT_LIABILITY, "2000", //
	 * AccounterConstants.SALES_OUTPUT_VAT, true, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true, //
	 * openingBalances, "4"); // // session.save(salesOutputVAT); // // Account
	 * purchaseInputVAT = new Account( // Account.TYPE_OTHER_CURRENT_LIABILITY,
	 * "2499", // AccounterConstants.PURCHASE_INPUT_VAT, true, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true, //
	 * openingBalances, "5"); // // session.save(purchaseInputVAT);
	 * 
	 * // Account pendingGoodsReceiveNotes = new Account( //
	 * Account.TYPE_OTHER_CURRENT_LIABILITY, "2500", //
	 * AccounterConstants.PENDING_GOODS_RECEIVED_NOTES, true, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true, //
	 * openingBalances, "6"); // // session.save(pendingGoodsReceiveNotes); //
	 * 
	 * // Account prepaidVATaccount = new Account( //
	 * Account.TYPE_OTHER_CURRENT_LIABILITY, "2899", //
	 * AccounterConstants.PREPAID_VAT_ACCOUNT, true, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true, //
	 * openingBalances, "8"); // // session.save(prepaidVATaccount); // //
	 * Account ECAcquisitionVAT = new Account( //
	 * Account.TYPE_OTHER_CURRENT_LIABILITY, "2999", //
	 * AccounterConstants.EC_ACQUISITION_VAT, true, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true, //
	 * openingBalances, "9"); // // session.save(ECAcquisitionVAT);
	 * 
	 * // Account employeePayrollLiabilities = new Account( //
	 * Account.TYPE_PAYROLL_LIABILITY, "2110", //
	 * "Employee Payroll Liabilities", true, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true, //
	 * openingBalances, "10"); // // session.save(employeePayrollLiabilities);
	 * // // Account retainedEarnings = new Account(Account.TYPE_EQUITY, "3100",
	 * // "Retained Earnings", true, null, //
	 * Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true, //
	 * openingBalances, "12"); // // session.save(retainedEarnings); // //
	 * Account earlyPaymentDiscountGiven = new Account(Account.TYPE_INCOME, //
	 * "4100", AccounterConstants.EARLY_PAYMENT_DISCOUNT_GIVEN, true, // null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", // null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, // true,
	 * openingBalances, "13"); // // session.save(earlyPaymentDiscountGiven); //
	 * // Account writeOff = new Account(Account.TYPE_INCOME, "4200", //
	 * AccounterConstants.WRITE_OFF, true, null, //
	 * Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true, //
	 * openingBalances, "14"); // // session.save(writeOff); // // Account
	 * earlyPaymentDiscountTaken = new Account( //
	 * Account.TYPE_COST_OF_GOODS_SOLD, "5000", //
	 * AccounterConstants.EARLY_PAYMENT_DISCOUNT_TAKEN, true, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true, //
	 * openingBalances, "15"); // // session.save(earlyPaymentDiscountTaken); //
	 * // Account bankCharge = new Account(Account.TYPE_EXPENSE, "5500", //
	 * AccounterConstants.BANK_CHARGE, true, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true, //
	 * openingBalances, "16"); // // session.save(bankCharge); // // // UK
	 * Default InActive Accounts // Account payrollLiabilityTax = new //
	 * Account(Account.TYPE_ACCOUNT_PAYABLE, // "2605",
	 * AccounterConstants.PAYROLL_LIABILITY_TAX, false, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true, //
	 * openingBalances, "17"); // // session.save(payrollLiabilityTax); // //
	 * Account payrollLiabilityOther = new Account( //
	 * Account.TYPE_ACCOUNT_PAYABLE, "2610", //
	 * AccounterConstants.PAYROLL_LIABILITY_OTHER, false, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true, //
	 * openingBalances, "18"); // // session.save(payrollLiabilityOther); // //
	 * Account payrollLiabilities = new Account( //
	 * Account.TYPE_PAYROLL_LIABILITY, "2200", //
	 * AccounterConstants.PAYROLL_LIABILITIES, false, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true, //
	 * openingBalances, "19"); // // session.save(payrollLiabilities); // //
	 * Account payrollLiabilitiyNetPay = new Account( //
	 * Account.TYPE_PAYROLL_LIABILITY, "2240", //
	 * AccounterConstants.PAYROLL_LIABILITY_NET_PAY, false, //
	 * payrollLiabilities, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, // false,
	 * "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, // null, false,
	 * true, openingBalances, "19.1"); // //
	 * session.save(payrollLiabilitiyNetPay); // // Account
	 * payrollExpenseEmployees = new Account(Account.TYPE_EXPENSE, // "7100",
	 * AccounterConstants.PAYROLL_EXPENSE_EMPLOYEES, false, // null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", // null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, // true,
	 * openingBalances, "20"); // // session.save(payrollExpenseEmployees); //
	 * // Account payrollExpenseEmployeeSalary = new Account( //
	 * Account.TYPE_EXPENSE, "7110", //
	 * AccounterConstants.PAYROLL_EXPENSE_EMPLOYEE_SALARY, false, //
	 * payrollExpenseEmployees, Account.CASH_FLOW_CATEGORY_OPERATING, // 0.0,
	 * false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null, // 0.0, null,
	 * false, true, openingBalances, "20.1"); // //
	 * session.save(payrollExpenseEmployeeSalary); // // Account
	 * payrollExpenseBonus = new Account(Account.TYPE_EXPENSE, // "7115", //
	 * AccounterConstants.PAYROLL_EXPENSE_BONUSES, false, //
	 * payrollExpenseEmployees, Account.CASH_FLOW_CATEGORY_OPERATING, // 0.0,
	 * false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null, // 0.0, null,
	 * false, true, openingBalances, "20.2"); // //
	 * session.save(payrollExpenseBonus); // // Account
	 * payrollExpenseSSP_SMP_SPP_SAP = new Account( // Account.TYPE_EXPENSE,
	 * "7125", // AccounterConstants.PAYROLL_EXPENSE_SSP_SMP_SPP_SAP, false, //
	 * payrollExpenseEmployees, Account.CASH_FLOW_CATEGORY_OPERATING, // 0.0,
	 * false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null, // 0.0, null,
	 * false, true, openingBalances, "20.3"); // //
	 * session.save(payrollExpenseSSP_SMP_SPP_SAP); // // Account
	 * payrollChargeExpense = new Account(Account.TYPE_EXPENSE, // "7170",
	 * AccounterConstants.PAYROLL_CHARGE_EXPENSE, false, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true, //
	 * openingBalances, "21"); // // session.save(payrollChargeExpense); // //
	 * Account payrollChargeExpNIEmployer = new // Account(Account.TYPE_EXPENSE,
	 * // "7175", AccounterConstants.PAYROLL_CHARGE_EXP_NI_EMPLOYER, // false,
	 * payrollChargeExpense, // Account.CASH_FLOW_CATEGORY_OPERATING, 0.0,
	 * false, "", null, // Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null,
	 * false, true, // openingBalances, "21.1"); // //
	 * session.save(payrollChargeExpNIEmployer); // // Account
	 * payrollChargeExpPensionEmployer = new Account( // Account.TYPE_EXPENSE,
	 * "7180", // AccounterConstants.PAYROLL_CHARGE_EXP_PENSION_EMPLOYER, false,
	 * // payrollChargeExpense, Account.CASH_FLOW_CATEGORY_OPERATING, // 0.0,
	 * false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null, // 0.0, null,
	 * false, true, openingBalances, "21.2"); // //
	 * session.save(payrollChargeExpPensionEmployer); // // Account ECSale = new
	 * Account(Account.TYPE_OTHER_CURRENT_LIABILITY, // "9999",
	 * AccounterConstants.EC_SALE, false, null, //
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null, //
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true, //
	 * openingBalances, "22"); // // session.save(ECSale);
	 * 
	 * this.accountsReceivableAccount = accountsReceivable;
	 * this.accountsPayableAccount = accountsPayable;
	 * this.openingBalancesAccount = openingBalances;
	 * this.retainedEarningsAccount = reservesRetainedEarnings;
	 * this.VATliabilityAccount = saelsTaxVAT; this.VATFiledLiabilityAccount =
	 * salesTaxVATFiled; // this.pendingItemReceiptsAccount =
	 * pendingItemReceipts; // this.prepaidVATaccount = prepaidVATaccount; //
	 * this.ECAcquisitionVATaccount = ECAcquisitionVAT;
	 * 
	 * setDefaultsUKValues(session);
	 * createUKDefaultVATCodesAndVATAgency(session);
	 * createNominalCodesRanges(session); createDefaultBrandingTheme(session);
	 * 
	 * }
	 */

	/**
	 * Creates all the nominal code ranges for all the default accounts in the
	 * company. It sets the minimum range and maximum range of the nominal codes
	 * 
	 * @param session
	 */
	/*
	 * private void createNominalCodesRanges(Session session) {
	 * 
	 * Set<NominalCodeRange> nominalCodesRangeSet = new
	 * HashSet<NominalCodeRange>();
	 * 
	 * NominalCodeRange nominalCodeRange1 = new NominalCodeRange();
	 * nominalCodeRange1
	 * .setAccountSubBaseType(Account.SUBBASETYPE_FIXED_ASSET);
	 * nominalCodeRange1.setMinimum(NominalCodeRange.RANGE_FIXED_ASSET_MIN);
	 * nominalCodeRange1.setMaximum(NominalCodeRange.RANGE_FIXED_ASSET_MAX);
	 * nominalCodesRangeSet.add(nominalCodeRange1);
	 * 
	 * NominalCodeRange nominalCodeRange2 = new NominalCodeRange();
	 * nominalCodeRange2
	 * .setAccountSubBaseType(Account.SUBBASETYPE_CURRENT_ASSET);
	 * nominalCodeRange2
	 * .setMinimum(NominalCodeRange.RANGE_OTHER_CURRENT_ASSET_MIN);
	 * nominalCodeRange2
	 * .setMaximum(NominalCodeRange.RANGE_OTHER_CURRENT_ASSET_MAX);
	 * nominalCodesRangeSet.add(nominalCodeRange2);
	 * 
	 * NominalCodeRange nominalCodeRange3 = new NominalCodeRange();
	 * nominalCodeRange3
	 * .setAccountSubBaseType(Account.SUBBASETYPE_CURRENT_LIABILITY);
	 * nominalCodeRange3
	 * .setMinimum(NominalCodeRange.RANGE_OTER_CURRENT_LIABILITY_MIN);
	 * nominalCodeRange3
	 * .setMaximum(NominalCodeRange.RANGE_OTER_CURRENT_LIABILITY_MAX);
	 * nominalCodesRangeSet.add(nominalCodeRange3);
	 * 
	 * NominalCodeRange nominalCodeRange4 = new NominalCodeRange();
	 * nominalCodeRange4.setAccountSubBaseType(Account.SUBBASETYPE_EQUITY);
	 * nominalCodeRange4.setMinimum(NominalCodeRange.RANGE_EQUITY_MIN);
	 * nominalCodeRange4.setMaximum(NominalCodeRange.RANGE_EQUITY_MAX);
	 * nominalCodesRangeSet.add(nominalCodeRange4);
	 * 
	 * NominalCodeRange nominalCodeRange5 = new NominalCodeRange();
	 * nominalCodeRange5.setAccountSubBaseType(Account.SUBBASETYPE_INCOME);
	 * nominalCodeRange5.setMinimum(NominalCodeRange.RANGE_INCOME_MIN);
	 * nominalCodeRange5.setMaximum(NominalCodeRange.RANGE_INCOME_MAX);
	 * nominalCodesRangeSet.add(nominalCodeRange5);
	 * 
	 * NominalCodeRange nominalCodeRange6 = new NominalCodeRange();
	 * nominalCodeRange6
	 * .setAccountSubBaseType(Account.SUBBASETYPE_COST_OF_GOODS_SOLD);
	 * nominalCodeRange6
	 * .setMinimum(NominalCodeRange.RANGE_COST_OF_GOODS_SOLD_MIN);
	 * nominalCodeRange6
	 * .setMaximum(NominalCodeRange.RANGE_COST_OF_GOODS_SOLD_MAX);
	 * nominalCodesRangeSet.add(nominalCodeRange6);
	 * 
	 * NominalCodeRange nominalCodeRange7 = new NominalCodeRange();
	 * nominalCodeRange7
	 * .setAccountSubBaseType(Account.SUBBASETYPE_OTHER_EXPENSE);
	 * nominalCodeRange7.setMinimum(NominalCodeRange.RANGE_OTHER_EXPENSE_MIN);
	 * nominalCodeRange7.setMaximum(NominalCodeRange.RANGE_OTHER_EXPENSE_MAX);
	 * nominalCodesRangeSet.add(nominalCodeRange7);
	 * 
	 * NominalCodeRange nominalCodeRange8 = new NominalCodeRange();
	 * nominalCodeRange8.setAccountSubBaseType(Account.SUBBASETYPE_EXPENSE);
	 * nominalCodeRange8.setMinimum(NominalCodeRange.RANGE_EXPENSE_MIN);
	 * nominalCodeRange8.setMaximum(NominalCodeRange.RANGE_EXPENSE_MAX);
	 * nominalCodesRangeSet.add(nominalCodeRange8);
	 * 
	 * NominalCodeRange nominalCodeRange9 = new NominalCodeRange();
	 * nominalCodeRange9
	 * .setAccountSubBaseType(Account.SUBBASETYPE_LONG_TERM_LIABILITY);
	 * nominalCodeRange9
	 * .setMinimum(NominalCodeRange.RANGE_LONGTERM_LIABILITY_MIN);
	 * nominalCodeRange9
	 * .setMaximum(NominalCodeRange.RANGE_LONGTERM_LIABILITY_MAX);
	 * nominalCodesRangeSet.add(nominalCodeRange9);
	 * 
	 * NominalCodeRange nominalCodeRange10 = new NominalCodeRange();
	 * nominalCodeRange10
	 * .setAccountSubBaseType(Account.SUBBASETYPE_OTHER_ASSET);
	 * nominalCodeRange10.setMinimum(NominalCodeRange.RANGE_OTHER_ASSET_MIN);
	 * nominalCodeRange10.setMaximum(NominalCodeRange.RANGE_OTHER_ASSET_MAX);
	 * nominalCodesRangeSet.add(nominalCodeRange10);
	 * 
	 * this.setNominalCodeRange(nominalCodesRangeSet);
	 * 
	 * }
	 */

	/*
	 * private void setDefaultsUKValues(Session session) {
	 * 
	 * // Session session = HibernateUtil.getCurrentSession(); // Create Default
	 * UK Payment Terms
	 * 
	 * // PaymentTerms onePercentTenNetThirty = new PaymentTerms( //
	 * AccounterConstants.PM_ONE_PERCENT_TEN_NET_THIRTY, //
	 * AccounterConstants.DISCOUNT_ONEPERCENT_IF_PAID_WITHIN_TENDAYS, // 10, 1,
	 * PaymentTerms.DUE_NONE, 30, true); // //
	 * session.save(onePercentTenNetThirty); // // PaymentTerms
	 * twoPercentTenNetThirty = new PaymentTerms( //
	 * AccounterConstants.PM_TWO_PERCENT_TEN_NET_THIRTY, //
	 * AccounterConstants.DISCOUNT_TWOPERCENT_IF_PAID_WITHIN_TENDAYS, // 10, 2,
	 * PaymentTerms.DUE_NONE, 30, true); // //
	 * session.save(twoPercentTenNetThirty); // // PaymentTerms netFifteen = new
	 * PaymentTerms( // AccounterConstants.PM_NET_FIFTEEN, //
	 * AccounterConstants.PAY_WITH_IN_FIFTEEN_DAYS, 0, 0, //
	 * PaymentTerms.DUE_NONE, 15, true); // // session.save(netFifteen);
	 * 
	 * PaymentTerms dueOnReceipt = new PaymentTerms(
	 * AccounterConstants.PM_DUE_ON_RECEIPT, AccounterConstants.DUE_ON_RECEIPT,
	 * 0, 0, PaymentTerms.DUE_NONE, 0, true);
	 * 
	 * session.save(dueOnReceipt);
	 * 
	 * PaymentTerms netThirty = new PaymentTerms(
	 * AccounterConstants.PM_NET_THIRTY,
	 * AccounterConstants.PAY_WITH_IN_THIRTY_DAYS, 0, 0, PaymentTerms.DUE_NONE,
	 * 30, true);
	 * 
	 * session.save(netThirty);
	 * 
	 * PaymentTerms netSixty = new PaymentTerms(
	 * AccounterConstants.PM_NET_SIXTY,
	 * AccounterConstants.PAY_WITH_IN_SIXTY_DAYS, 0, 0, PaymentTerms.DUE_NONE,
	 * 60, true);
	 * 
	 * session.save(netSixty);
	 * 
	 * PaymentTerms monthlyPayrollLiability = new PaymentTerms(
	 * AccounterConstants.PM_MONTHLY,
	 * AccounterConstants.PM_MONTHLY_PAYROLL_LIABILITY, 0, 0,
	 * PaymentTerms.DUE_PAYROLL_TAX_MONTH, 13, true);
	 * 
	 * session.save(monthlyPayrollLiability);
	 * 
	 * // PaymentTerms quarterlyPayrollLiability = new PaymentTerms( //
	 * AccounterConstants.PM_QUARTERLY_PAYROLL_LIABILITY, //
	 * AccounterConstants.PM_QUARTERLY_PAYROLL_LIABILITY, 0, 0, //
	 * PaymentTerms.DUE_PAYROLL_TAX_QUARTER, 13, true); // //
	 * session.save(quarterlyPayrollLiability);
	 * 
	 * // Current Fiscal Year creation
	 * 
	 * VendorGroup creditCardCompanies = new VendorGroup();
	 * creditCardCompanies.setName(AccounterConstants.CREDIT_CARD_COMPANIES);
	 * creditCardCompanies.setDefault(true); session.save(creditCardCompanies);
	 * 
	 * }
	 */
	/*
	 * public void createUSDefaultTaxGroup(Session session) {
	 * 
	 * try {
	 * 
	 * // Session session = HibernateUtil.getCurrentSession();
	 * 
	 * // Default TaxGroup Creation TAXAgency defaultTaxAgency = new
	 * TAXAgency(); defaultTaxAgency.setActive(Boolean.TRUE);
	 * defaultTaxAgency.setName("Tax Agency");
	 * 
	 * defaultTaxAgency.setPaymentTerm((PaymentTerms) session
	 * .getNamedQuery("unique.name.PaymentTerms") .setString(0,
	 * "Net Monthly").list().get(0));
	 * defaultTaxAgency.setSalesLiabilityAccount((Account) session
	 * .getNamedQuery("unique.name.Account") .setString(0,
	 * "Sales Tax Payable").list().get(0)); defaultTaxAgency.setDefault(true);
	 * session.save(defaultTaxAgency);
	 * 
	 * // Set<TaxRates> taxRates = new HashSet<TaxRates>(); // // TaxRates
	 * taxRate = new TaxRates(); // taxRate.setRate(0); // taxRate.setAsOf(new
	 * FinanceDate()); // taxRate.setID(SecureUtils.createID()); //
	 * taxRates.add(taxRate);
	 * 
	 * TAXItem defaultTaxItem = new TAXItem();
	 * defaultTaxItem.setActive(Boolean.TRUE); defaultTaxItem.setName("None");
	 * defaultTaxItem.setTaxAgency(defaultTaxAgency);
	 * defaultTaxItem.setVatReturnBox(null); defaultTaxItem.setTaxRate(0);
	 * defaultTaxItem.setSalesType(Boolean.TRUE);
	 * defaultTaxItem.setDefault(true); session.save(defaultTaxItem); TAXCode
	 * defaultTaxCodeforTaxItem = new TAXCode( (TAXItemGroup) defaultTaxItem);
	 * session.save(defaultTaxCodeforTaxItem);
	 * 
	 * // TAXGroup defaultTaxGroup = new TAXGroup(); //
	 * defaultTaxGroup.setName("Tax Group"); //
	 * defaultTaxGroup.setID(SecureUtils.createID()); //
	 * defaultTaxGroup.setActive(Boolean.TRUE); //
	 * defaultTaxGroup.setSalesType(true); // // ArrayList<TAXItem> taxItems =
	 * new ArrayList<TAXItem>(); // taxItems.add(defaultTaxItem); //
	 * defaultTaxGroup.setTAXItems(taxItems); //
	 * defaultTaxGroup.setGroupRate(0); // defaultTaxGroup.setDefault(true); //
	 * session.save(defaultTaxGroup); // TAXCode defaultTaxCodeforTaxGroup = new
	 * TAXCode((TAXItemGroup) // defaultTaxGroup); //
	 * session.save(defaultTaxCodeforTaxGroup);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * }
	 */
	/*
	 * public void createUKDefaultVATCodesAndVATAgency(Session session) {
	 * 
	 * try { VATReturnBox vt1 = new VATReturnBox();
	 * vt1.setName(AccounterConstants.UK_EC_PURCHASES_GOODS);
	 * vt1.setVatBox(AccounterConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS);
	 * vt1.setTotalBox(AccounterConstants.UK_BOX9_TOTAL_NET_ACQUISITIONS);
	 * vt1.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT); session.save(vt1);
	 * 
	 * VATReturnBox vt3 = new VATReturnBox();
	 * vt3.setName(AccounterConstants.UK_EC_SALES_GOODS);
	 * vt3.setVatBox(AccounterConstants.BOX_NONE);
	 * vt3.setTotalBox(AccounterConstants.UK_BOX8_TOTAL_NET_SUPPLIES);
	 * vt3.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT); session.save(vt3);
	 * 
	 * VATReturnBox vt4 = new VATReturnBox();
	 * vt4.setName(AccounterConstants.UK_EC_SALES_SERVICES);
	 * vt4.setVatBox(AccounterConstants.BOX_NONE);
	 * vt4.setTotalBox(AccounterConstants.UK_BOX6_TOTAL_NET_SALES);
	 * vt4.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT); session.save(vt4);
	 * 
	 * VATReturnBox vt5 = new VATReturnBox();
	 * vt5.setName(AccounterConstants.UK_DOMESTIC_PURCHASES);
	 * vt5.setVatBox(AccounterConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES);
	 * vt5.setTotalBox(AccounterConstants.UK_BOX7_TOTAL_NET_PURCHASES);
	 * vt5.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT); session.save(vt5);
	 * 
	 * VATReturnBox vt6 = new VATReturnBox();
	 * vt6.setName(AccounterConstants.UK_DOMESTIC_SALES);
	 * vt6.setVatBox(AccounterConstants.UK_BOX1_VAT_DUE_ON_SALES);
	 * vt6.setTotalBox(AccounterConstants.UK_BOX6_TOTAL_NET_SALES);
	 * vt6.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT); session.save(vt6);
	 * 
	 * VATReturnBox vt7 = new VATReturnBox();
	 * vt7.setName(AccounterConstants.UK_NOT_REGISTERED_PURCHASES);
	 * vt7.setVatBox(AccounterConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES);
	 * vt7.setTotalBox(AccounterConstants.UK_BOX7_TOTAL_NET_PURCHASES);
	 * vt7.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT); session.save(vt7);
	 * 
	 * VATReturnBox vt8 = new VATReturnBox();
	 * vt8.setName(AccounterConstants.UK_NOT_REGISTERED_SALES);
	 * vt8.setVatBox(AccounterConstants.UK_BOX1_VAT_DUE_ON_SALES);
	 * vt8.setTotalBox(AccounterConstants.UK_BOX6_TOTAL_NET_SALES);
	 * vt8.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT); session.save(vt8);
	 * 
	 * VATReturnBox vt11 = new VATReturnBox();
	 * vt11.setName(AccounterConstants.UK_REVERSE_CHARGE);
	 * vt11.setVatBox(AccounterConstants.UK_BOX1_VAT_DUE_ON_SALES);
	 * vt11.setTotalBox(AccounterConstants.BOX_NONE);
	 * vt11.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT); session.save(vt11);
	 * 
	 * // /// For Ireland VAT Return type boxes
	 * 
	 * VATReturnBox vt20 = new VATReturnBox();
	 * vt20.setName(AccounterConstants.IRELAND_DOMESTIC_SALES);
	 * vt20.setVatBox(AccounterConstants.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES);
	 * vt20.setTotalBox(AccounterConstants.IRELAND_BOX8_TOTAL_NET_SALES);
	 * vt20.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
	 * session.save(vt20);
	 * 
	 * VATReturnBox vt21 = new VATReturnBox();
	 * vt21.setName(AccounterConstants.IRELAND_DOMESTIC_PURCHASES);
	 * vt21.setVatBox(AccounterConstants.IRELAND_BOX4_VAT_ON_PURCHASES);
	 * vt21.setTotalBox(AccounterConstants.IRELAND_BOX9_TOTAL_NET_PURCHASES);
	 * vt21.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
	 * session.save(vt21);
	 * 
	 * VATReturnBox vt22 = new VATReturnBox();
	 * vt22.setName(AccounterConstants.IRELAND_EC_SALES_GOODS);
	 * vt22.setVatBox(AccounterConstants.BOX_NONE);
	 * vt22.setTotalBox(AccounterConstants.IRELAND_BOX6_E1_GOODS_TO_EU);
	 * vt22.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
	 * session.save(vt22);
	 * 
	 * VATReturnBox vt23 = new VATReturnBox();
	 * vt23.setName(AccounterConstants.IRELAND_EC_PURCHASES_GOODS);
	 * vt23.setVatBox
	 * (AccounterConstants.IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS);
	 * vt23.setTotalBox(AccounterConstants.IRELAND_BOX7_E2_GOODS_FROM_EU);
	 * vt23.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
	 * session.save(vt23);
	 * 
	 * VATReturnBox vt24 = new VATReturnBox();
	 * vt24.setName(AccounterConstants.IRELAND_EXEMPT_SALES);
	 * vt24.setVatBox(AccounterConstants.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES);
	 * vt24.setTotalBox(AccounterConstants.IRELAND_BOX8_TOTAL_NET_SALES);
	 * vt24.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
	 * session.save(vt24);
	 * 
	 * VATReturnBox vt25 = new VATReturnBox();
	 * vt25.setName(AccounterConstants.IRELAND_EXEMPT_PURCHASES);
	 * vt25.setVatBox(AccounterConstants.IRELAND_BOX7_E2_GOODS_FROM_EU);
	 * vt25.setTotalBox(AccounterConstants.IRELAND_BOX9_TOTAL_NET_PURCHASES);
	 * vt25.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
	 * session.save(vt25);
	 * 
	 * VATReturnBox vt26 = new VATReturnBox();
	 * vt26.setName(AccounterConstants.IRELAND_NOT_REGISTERED_SALES);
	 * vt26.setVatBox(AccounterConstants.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES);
	 * vt26.setTotalBox(AccounterConstants.IRELAND_BOX8_TOTAL_NET_SALES);
	 * vt26.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
	 * session.save(vt26);
	 * 
	 * VATReturnBox vt27 = new VATReturnBox();
	 * vt27.setName(AccounterConstants.IRELAND_NOT_REGISTERED_PURCHASES);
	 * vt27.setVatBox(AccounterConstants.IRELAND_BOX4_VAT_ON_PURCHASES);
	 * vt27.setTotalBox(AccounterConstants.IRELAND_BOX9_TOTAL_NET_PURCHASES);
	 * vt27.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
	 * session.save(vt27);
	 * 
	 * // Session session = HibernateUtil.getCurrentSession();
	 * 
	 * TAXAgency defaultVATAgency = new TAXAgency();
	 * defaultVATAgency.setActive(Boolean.TRUE);
	 * defaultVATAgency.setName(getPreferences().getVATtaxAgencyName());
	 * defaultVATAgency.setVATReturn(VATReturn.VAT_RETURN_UK_VAT);
	 * 
	 * defaultVATAgency.setSalesLiabilityAccount((Account) session
	 * .getNamedQuery("unique.name.Account") .setString(0,
	 * AccounterConstants.SALES_TAX_VAT_UNFILED) .list().get(0));
	 * 
	 * defaultVATAgency.setPurchaseLiabilityAccount((Account) session
	 * .getNamedQuery("unique.name.Account") .setString(0,
	 * AccounterConstants.SALES_TAX_VAT_UNFILED) .list().get(0));
	 * 
	 * defaultVATAgency.setDefault(true);
	 * 
	 * session.save(defaultVATAgency);
	 * 
	 * TAXItem vatItem1 = new TAXItem();
	 * vatItem1.setName("EC Purchases Goods Standard");
	 * vatItem1.setActive(true);
	 * vatItem1.setDescription("EC Purchases of Goods Standard");
	 * vatItem1.setTaxRate(17.5); vatItem1.setSalesType(false);
	 * vatItem1.setTaxAgency(defaultVATAgency);
	 * 
	 * vatItem1.setVatReturnBox(vt1); vatItem1.setDefault(true);
	 * vatItem1.setPercentage(true); session.save(vatItem1);
	 * 
	 * TAXItem vatItem2 = new TAXItem();
	 * vatItem2.setName("EC Purchases Goods Zero-Rated");
	 * vatItem2.setActive(true);
	 * vatItem2.setDescription("EC Purchases of Goods Zero-Rated");
	 * vatItem2.setTaxRate(0.0); vatItem2.setSalesType(false);
	 * vatItem2.setTaxAgency(defaultVATAgency); vatItem2.setPercentage(true);
	 * vatItem2.setVatReturnBox(vt1); vatItem2.setDefault(true);
	 * session.save(vatItem2);
	 * 
	 * TAXItem vatItem3 = new TAXItem();
	 * vatItem3.setName("EC Sales Goods Standard"); vatItem3.setActive(true);
	 * vatItem3.setDescription("EC Sales of Goods Standard");
	 * vatItem3.setTaxRate(0.0); vatItem3.setSalesType(true);
	 * vatItem3.setTaxAgency(defaultVATAgency); vatItem3.setPercentage(true);
	 * vatItem3.setVatReturnBox(vt3); vatItem3.setDefault(true);
	 * session.save(vatItem3);
	 * 
	 * TAXItem vatItem4 = new TAXItem();
	 * vatItem4.setName("EC Sales Services Standard"); vatItem4.setActive(true);
	 * vatItem4.setDescription("EC Sales of Services Standard");
	 * vatItem4.setTaxRate(0.0); vatItem4.setSalesType(true);
	 * vatItem4.setTaxAgency(defaultVATAgency); vatItem4.setVatReturnBox(vt4);
	 * vatItem4.setDefault(true); vatItem4.setPercentage(true);
	 * session.save(vatItem4);
	 * 
	 * TAXItem vatItem5 = new TAXItem(); vatItem5.setName("Exempt Purchases");
	 * vatItem5.setActive(true); vatItem5.setDescription("Exempt Purchases");
	 * vatItem5.setTaxRate(0.0); vatItem5.setSalesType(false);
	 * vatItem5.setTaxAgency(defaultVATAgency); vatItem5.setVatReturnBox(vt5);
	 * vatItem5.setDefault(true); vatItem5.setPercentage(true);
	 * session.save(vatItem5);
	 * 
	 * TAXItem vatItem6 = new TAXItem(); vatItem6.setName("Exempt Sales");
	 * vatItem6.setActive(true); vatItem6.setDescription("Exempt Sales");
	 * vatItem6.setTaxRate(0.0); vatItem6.setSalesType(true);
	 * vatItem6.setTaxAgency(defaultVATAgency); vatItem6.setVatReturnBox(vt6);
	 * vatItem6.setDefault(true); vatItem6.setPercentage(true);
	 * session.save(vatItem6);
	 * 
	 * TAXItem vatItem7 = new TAXItem();
	 * vatItem7.setName("Not Registered Purchases"); vatItem7.setActive(true);
	 * vatItem7.setSalesType(false);
	 * vatItem7.setDescription("Not Registered Purchases");
	 * vatItem7.setTaxRate(0.0); vatItem7.setTaxAgency(defaultVATAgency);
	 * vatItem7.setPercentage(true); // session.save(vt7);
	 * vatItem7.setVatReturnBox(vt7); vatItem7.setDefault(true);
	 * session.save(vatItem7);
	 * 
	 * TAXItem vatItem8 = new TAXItem();
	 * vatItem8.setName("Not Registered Sales"); vatItem8.setActive(true);
	 * vatItem8.setDescription("Not Registered Sales");
	 * vatItem8.setTaxRate(0.0); vatItem8.setSalesType(true);
	 * vatItem8.setTaxAgency(defaultVATAgency); vatItem8.setPercentage(true); //
	 * session.save(vt8); vatItem8.setVatReturnBox(vt8);
	 * vatItem8.setDefault(true); session.save(vatItem8);
	 * 
	 * TAXItem vatItem9 = new TAXItem(); vatItem9.setName("Reduced Purchases");
	 * vatItem9.setActive(true); vatItem9.setSalesType(false);
	 * vatItem9.setDescription("Reduced Purchases"); vatItem9.setTaxRate(5.0);
	 * vatItem9.setTaxAgency(defaultVATAgency); vatItem9.setVatReturnBox(vt5);
	 * vatItem9.setDefault(true); vatItem9.setPercentage(true);
	 * session.save(vatItem9);
	 * 
	 * TAXItem vatItem10 = new TAXItem(); vatItem10.setName("Reduced Sales");
	 * vatItem10.setActive(true); vatItem10.setDescription("Reduced Sales");
	 * vatItem10.setTaxRate(5.0); vatItem10.setSalesType(true);
	 * vatItem10.setTaxAgency(defaultVATAgency); vatItem10.setVatReturnBox(vt6);
	 * vatItem10.setDefault(true); vatItem10.setPercentage(true);
	 * session.save(vatItem10);
	 * 
	 * TAXItem vatItem11 = new TAXItem();
	 * vatItem11.setName("Reverse Charge Purchases Standard");
	 * vatItem11.setActive(true);
	 * vatItem11.setDescription("Reverse Charge Purchases Standard");
	 * vatItem11.setTaxRate(17.5); vatItem11.setSalesType(false);
	 * vatItem11.setTaxAgency(defaultVATAgency);
	 * vatItem11.setVatReturnBox(vt11); vatItem11.setPercentage(true);
	 * vatItem11.setDefault(true); session.save(vatItem11);
	 * 
	 * TAXItem vatItem12 = new TAXItem();
	 * vatItem12.setName("Standard Purchases"); vatItem12.setActive(true);
	 * vatItem12.setDescription("Standard Purchases");
	 * vatItem12.setTaxRate(17.5); vatItem12.setSalesType(false);
	 * vatItem12.setTaxAgency(defaultVATAgency); vatItem12.setVatReturnBox(vt5);
	 * vatItem12.setDefault(true); vatItem12.setPercentage(true);
	 * session.save(vatItem12);
	 * 
	 * TAXItem vatItem13 = new TAXItem(); vatItem13.setName("Standard Sales");
	 * vatItem13.setActive(true); vatItem13.setDescription("Standard Sales");
	 * vatItem13.setTaxRate(17.5); vatItem13.setSalesType(true);
	 * vatItem13.setTaxAgency(defaultVATAgency); vatItem13.setVatReturnBox(vt6);
	 * vatItem13.setDefault(true); vatItem13.setPercentage(true);
	 * session.save(vatItem13);
	 * 
	 * TAXItem vatItem14 = new TAXItem();
	 * vatItem14.setName("Zero-Rated Purchases"); vatItem14.setActive(true);
	 * vatItem14.setSalesType(false);
	 * vatItem14.setDescription("Zero-Rated Purchases");
	 * vatItem14.setTaxRate(0.0); vatItem14.setTaxAgency(defaultVATAgency);
	 * vatItem14.setVatReturnBox(vt5); vatItem14.setDefault(true);
	 * vatItem14.setPercentage(true); session.save(vatItem14);
	 * 
	 * TAXItem vatItem15 = new TAXItem(); vatItem15.setName("Zero-Rated Sales");
	 * vatItem15.setActive(true); vatItem15.setDescription("Zero-Rated Sales");
	 * vatItem15.setTaxRate(0.0); vatItem15.setSalesType(true);
	 * vatItem15.setTaxAgency(defaultVATAgency); vatItem15.setVatReturnBox(vt6);
	 * vatItem15.setDefault(true); vatItem15.setPercentage(true);
	 * session.save(vatItem15);
	 * 
	 * TAXItem vatItem16 = new TAXItem();
	 * vatItem16.setName("New Standard Purchases"); vatItem16.setActive(true);
	 * vatItem16.setDescription("New Standard Purchases");
	 * vatItem16.setTaxRate(20.0); vatItem16.setSalesType(false);
	 * vatItem16.setTaxAgency(defaultVATAgency); vatItem16.setVatReturnBox(vt5);
	 * vatItem16.setDefault(true); vatItem16.setPercentage(true);
	 * session.save(vatItem16);
	 * 
	 * TAXItem vatItem17 = new TAXItem();
	 * vatItem17.setName("New Standard Sales"); vatItem17.setActive(true);
	 * vatItem17.setDescription("New Standard Sales");
	 * vatItem17.setTaxRate(20.0); vatItem17.setSalesType(true);
	 * vatItem17.setTaxAgency(defaultVATAgency); vatItem17.setVatReturnBox(vt6);
	 * vatItem17.setDefault(true); vatItem17.setPercentage(true);
	 * session.save(vatItem17);
	 * 
	 * // VATGroup vatGroup1 = new VATGroup(); // ======= TAXGroup vatGroup1 =
	 * new TAXGroup(); // >>>>>>> .merge-right.r20318
	 * vatGroup1.setName("EC Purchases Goods 0% Group");
	 * vatGroup1.setDescription("EC Purchases of Goods Zero-Rated Group");
	 * vatGroup1.setActive(true); vatGroup1.setSalesType(false);
	 * vatGroup1.setGroupRate(0.0); ArrayList<TAXItem> vatItms1 = new
	 * ArrayList<TAXItem>(); vatItms1.add(vatItem2); vatItms1.add(vatItem14);
	 * vatGroup1.setTAXItems(vatItms1); vatGroup1.setDefault(true);
	 * session.save(vatGroup1);
	 * 
	 * TAXGroup vatGroup2 = new TAXGroup();
	 * vatGroup2.setName("EC Purchases Goods 17.5% Group");
	 * vatGroup2.setDescription("EC Purchases of Goods Group");
	 * vatGroup2.setActive(true); vatGroup2.setGroupRate(17.5);
	 * vatGroup2.setSalesType(false); ArrayList<TAXItem> vatItms2 = new
	 * ArrayList<TAXItem>(); vatItms2.add(vatItem12); vatItms2.add(vatItem1);
	 * vatGroup2.setTAXItems(vatItms2); vatGroup2.setDefault(true);
	 * session.save(vatGroup2);
	 * 
	 * TAXGroup vatGroup3 = new TAXGroup();
	 * vatGroup3.setName("EC Sales Goods 0% Group");
	 * vatGroup3.setDescription("EC Sales of Goods Group");
	 * vatGroup3.setActive(true); vatGroup3.setSalesType(true);
	 * vatGroup3.setGroupRate(0.0); ArrayList<TAXItem> vatItms3 = new
	 * ArrayList<TAXItem>(); vatItms3.add(vatItem4); vatItms3.add(vatItem3);
	 * vatGroup3.setTAXItems(vatItms3); vatGroup3.setDefault(true);
	 * vatGroup3.setPercentage((vatItem3.isPercentage() && vatItem4
	 * .isPercentage()) ? true : false); session.save(vatGroup3);
	 * 
	 * TAXGroup vatGroup4 = new TAXGroup();
	 * vatGroup4.setName("Reverse Charge Purchases 17.5% Group");
	 * vatGroup4.setDescription("Reverse Charge Purchases Group");
	 * vatGroup4.setActive(true); vatGroup4.setSalesType(false);
	 * vatGroup4.setGroupRate(17.5); ArrayList<TAXItem> vatItms4 = new
	 * ArrayList<TAXItem>(); vatItms4.add(vatItem12); vatItms4.add(vatItem11);
	 * vatGroup4.setTAXItems(vatItms4); vatGroup4.setDefault(true);
	 * session.save(vatGroup4);
	 * 
	 * TAXCode vatCode1 = new TAXCode(); vatCode1.setName("E");
	 * vatCode1.setDescription("Exempt"); vatCode1.setTaxable(true);
	 * vatCode1.setActive(true); vatCode1.setTAXItemGrpForPurchases(vatItem5);
	 * vatCode1.setTAXItemGrpForSales(vatItem6); vatCode1.setDefault(true);
	 * session.save(vatCode1);
	 * 
	 * TAXCode vatCode2 = new TAXCode(); vatCode2.setName("EGS");
	 * vatCode2.setDescription("EC Goods Standard (17.5%)");
	 * vatCode2.setTaxable(true); vatCode2.setActive(true);
	 * vatCode2.setTAXItemGrpForPurchases(vatGroup2);
	 * vatCode2.setTAXItemGrpForSales(vatGroup3); vatCode2.setDefault(true);
	 * vatCode2.setECSalesEntry(true); session.save(vatCode2);
	 * 
	 * TAXCode vatCode3 = new TAXCode(); vatCode3.setName("EGZ");
	 * vatCode3.setDescription("EC Goods Zero-Rated (0%)");
	 * vatCode3.setTaxable(true); vatCode3.setActive(true);
	 * vatCode3.setTAXItemGrpForPurchases(vatGroup1); vatCode3.setDefault(true);
	 * vatCode3.setTAXItemGrpForSales(null);
	 * 
	 * session.save(vatCode3);
	 * 
	 * TAXCode vatCode4 = new TAXCode(); vatCode4.setName("N");
	 * vatCode4.setDescription("Not Registered"); vatCode4.setTaxable(true);
	 * vatCode4.setActive(true); vatCode4.setTAXItemGrpForPurchases(vatItem7);
	 * vatCode4.setTAXItemGrpForSales(vatItem8); vatCode4.setDefault(true);
	 * session.save(vatCode4);
	 * 
	 * TAXCode vatCode5 = new TAXCode(); vatCode5.setName("R");
	 * vatCode5.setDescription("Reduced (5%)"); vatCode5.setTaxable(true);
	 * vatCode5.setActive(true); vatCode5.setTAXItemGrpForPurchases(vatItem9);
	 * vatCode5.setTAXItemGrpForSales(vatItem10); vatCode5.setDefault(true);
	 * session.save(vatCode5);
	 * 
	 * TAXCode vatCode6 = new TAXCode(); vatCode6.setName("RC");
	 * vatCode6.setDescription("Reverse Charge"); vatCode6.setTaxable(true);
	 * vatCode6.setActive(true); vatCode6.setTAXItemGrpForPurchases(vatGroup4);
	 * // vatCode6.setVATItemGrpForSales(vatItem4); vatCode6.setDefault(true);
	 * vatCode6.setECSalesEntry(true); session.save(vatCode6);
	 * 
	 * TAXCode vatCode7 = new TAXCode(); vatCode7.setName("S");
	 * vatCode7.setDescription("Standard (17.5%)"); vatCode7.setTaxable(true);
	 * vatCode7.setActive(true); vatCode7.setTAXItemGrpForPurchases(vatItem12);
	 * vatCode7.setTAXItemGrpForSales(vatItem13); vatCode7.setDefault(true);
	 * session.save(vatCode7);
	 * 
	 * TAXCode vatCode8 = new TAXCode(); vatCode8.setName("Z");
	 * vatCode8.setDescription("Zero-Rated (0%)"); vatCode8.setTaxable(true);
	 * vatCode8.setActive(true); vatCode8.setTAXItemGrpForPurchases(vatItem14);
	 * vatCode8.setTAXItemGrpForSales(vatItem15); vatCode8.setDefault(true);
	 * session.save(vatCode8);
	 * 
	 * TAXCode vatCode9 = new TAXCode(); vatCode9.setName("O");
	 * vatCode9.setDescription("Outside the Scope of VAT");
	 * vatCode9.setTaxable(false); vatCode9.setActive(true);
	 * vatCode9.setTAXItemGrpForPurchases(null);
	 * vatCode9.setTAXItemGrpForSales(null); vatCode9.setDefault(true);
	 * session.save(vatCode9);
	 * 
	 * TAXCode vatCode10 = new TAXCode(); vatCode10.setName("New S");
	 * vatCode10.setDescription("Standard (20%)"); vatCode10.setTaxable(true);
	 * vatCode10.setActive(true);
	 * vatCode10.setTAXItemGrpForPurchases(vatItem16);
	 * vatCode10.setTAXItemGrpForSales(vatItem17); vatCode10.setDefault(true);
	 * session.save(vatCode10);
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace(); }
	 * 
	 * }
	 */
	public ArrayList<Payee> getPayees() {
		return payees;
	}

	public void setPayees(ArrayList<Payee> payees) {

		customers = new ArrayList<Customer>();
		vendors = new ArrayList<Vendor>();
		taxAgencies = new ArrayList<TAXAgency>();
		salesPersons = new ArrayList<SalesPerson>();

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

	public ArrayList<Vendor> getVendors() {
		return vendors;
	}

	public ArrayList<TAXAgency> getTaxAgencies() {
		return taxAgencies;
	}

	/**
	 * It initialises all the default values of the US company.
	 * 
	 * @param session
	 */

	/*
	 * private void setDefaultsUSValues(Session session) {
	 * 
	 * // Session session = HibernateUtil.getCurrentSession(); // Create Default
	 * Payment Terms
	 * 
	 * // PaymentTerms onePercentTenNetThirty = new PaymentTerms( //
	 * AccounterConstants.PM_ONE_PERCENT_TEN_NET_THIRTY, //
	 * AccounterConstants.DISCOUNT_ONEPERCENT_IF_PAID_WITHIN_TENDAYS, // 10, 1,
	 * PaymentTerms.DUE_NONE, 30, true); // //
	 * session.save(onePercentTenNetThirty);
	 * 
	 * // PaymentTerms twoPercentTenNetThirty = new PaymentTerms( //
	 * AccounterConstants.PM_TWO_PERCENT_TEN_NET_THIRTY, //
	 * AccounterConstants.DISCOUNT_TWOPERCENT_IF_PAID_WITHIN_TENDAYS, // 10, 2,
	 * PaymentTerms.DUE_NONE, 30, true); //
	 * session.save(twoPercentTenNetThirty);
	 * 
	 * // // PaymentTerms netFifteen = new PaymentTerms( //
	 * AccounterConstants.PM_NET_FIFTEEN, //
	 * AccounterConstants.PAY_WITH_IN_FIFTEEN_DAYS, 0, 0, //
	 * PaymentTerms.DUE_NONE, 15, true); // // session.save(netFifteen);
	 * 
	 * PaymentTerms dueOnReceipt = new PaymentTerms(
	 * AccounterConstants.PM_DUE_ON_RECEIPT, AccounterConstants.DUE_ON_RECEIPT,
	 * 0, 0, PaymentTerms.DUE_NONE, 0, true);
	 * 
	 * session.save(dueOnReceipt);
	 * 
	 * PaymentTerms netThirty = new PaymentTerms(
	 * AccounterConstants.PM_NET_THIRTY,
	 * AccounterConstants.PAY_WITH_IN_THIRTY_DAYS, 0, 0, PaymentTerms.DUE_NONE,
	 * 30, true);
	 * 
	 * session.save(netThirty);
	 * 
	 * PaymentTerms netSixty = new PaymentTerms(
	 * AccounterConstants.PM_NET_SIXTY,
	 * AccounterConstants.PAY_WITH_IN_SIXTY_DAYS, 0, 0, PaymentTerms.DUE_NONE,
	 * 60, true);
	 * 
	 * session.save(netSixty);
	 * 
	 * PaymentTerms monthly = new PaymentTerms(AccounterConstants.PM_MONTHLY,
	 * AccounterConstants.SALES_TAX_PAID_MONTHLY, 0, 0,
	 * PaymentTerms.DUE_CURRENT_MONTH, 30, true);
	 * 
	 * session.save(monthly);
	 * 
	 * // PaymentTerms quarterly = new PaymentTerms( //
	 * AccounterConstants.PM_QUARTERLY, //
	 * AccounterConstants.SALES_TAX_PAID_QUARTERLY, 0, 0, //
	 * PaymentTerms.DUE_CURRENT_QUARTER, 30, true); // //
	 * session.save(quarterly); // // PaymentTerms annually = new PaymentTerms(
	 * // AccounterConstants.PM_ANNUALLY, //
	 * AccounterConstants.SALES_TAX_PAID_ANNUALLY, 0, 0, //
	 * PaymentTerms.DUE_CURRENT_YEAR, 30, true); // // session.save(annually);
	 * 
	 * // Current Fiscal Year creation FinanceDate currentDate = new
	 * FinanceDate(); FinanceDate fiscalYearStartDate = new FinanceDate( (int)
	 * currentDate.getYear(), 0, 1); FinanceDate fiscalYearEndDate = new
	 * FinanceDate( (int) currentDate.getYear(), 11, 31);
	 * 
	 * FiscalYear fiscalYear = new FiscalYear(fiscalYearStartDate,
	 * fiscalYearEndDate, FiscalYear.STATUS_OPEN, Boolean.TRUE);
	 * 
	 * session.save(fiscalYear);
	 * 
	 * // Create Default PayTypes
	 * 
	 * // Set Default Preferences // SimpleDateFormat format = new
	 * SimpleDateFormat("yyyy-MM-dd"); CompanyPreferences preferences = new
	 * CompanyPreferences(); try { preferences.setUseAccountNumbers(true);
	 * preferences.setUseClasses(false); preferences.setUseJobs(false);
	 * preferences.setUseChangeLog(false);
	 * preferences.setAllowDuplicateDocumentNumbers(true);
	 * preferences.setDoYouPaySalesTax(false);
	 * preferences.setIsAccuralBasis(true); //
	 * preferences.setStartOfFiscalYear(format.parse("2009-01-01")); //
	 * preferences.setEndOfFiscalYear(format.parse("2009-12-31"));
	 * 
	 * preferences.setStartOfFiscalYear(fiscalYearStartDate);
	 * preferences.setEndOfFiscalYear(fiscalYearEndDate);
	 * preferences.setUseForeignCurrency(false);
	 * preferences.setUseCustomerId(false);
	 * preferences.setDefaultShippingTerm(null);
	 * preferences.setDefaultAnnualInterestRate(0);
	 * preferences.setDefaultMinimumFinanceCharge(0D);
	 * preferences.setGraceDays(3);
	 * preferences.setDoesCalculateFinanceChargeFromInvoiceDate(true);
	 * preferences.setUseVendorId(false); preferences.setUseItemNumbers(false);
	 * preferences.setCheckForItemQuantityOnHand(true);
	 * preferences.setUpdateCostAutomatically(false);
	 * preferences.setStartDate(fiscalYearStartDate);
	 * preferences.setPreventPostingBeforeDate(fiscalYearStartDate);
	 * 
	 * FinanceDate depreciationStartDateCal = new FinanceDate();
	 * depreciationStartDateCal.setTime(fiscalYearStartDate); //
	 * depreciationStartDateCal.set(Calendar.DAY_OF_MONTH, 01);
	 * preferences.setDepreciationStartDate(depreciationStartDateCal);
	 * 
	 * this.setPreferences(preferences);
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace(); }
	 * 
	 * VendorGroup creditCardCompanies = new VendorGroup();
	 * creditCardCompanies.setName(AccounterConstants.CREDIT_CARD_COMPANIES);
	 * creditCardCompanies.setDefault(true); session.save(creditCardCompanies);
	 * 
	 * }
	 */
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

	/**
	 * Initialises all the US default accounts that are useful in the company
	 * 
	 * @param session
	 */
	/*
	 * public void initDefaultUSAccounts(Session session) {
	 * 
	 * setDefaultsUSValues(session);
	 * 
	 * Account openingBalances = new Account(Account.TYPE_EQUITY, "3040",
	 * "Opening Balances", true, null, Account.CASH_FLOW_CATEGORY_FINANCING,
	 * 0.0, false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null,
	 * true, true, null, "7", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(openingBalances);
	 * 
	 * Account unDepositedFunds = new Account( Account.TYPE_OTHER_CURRENT_ASSET,
	 * "1175", "Un Deposited Funds", true, null,
	 * Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true,
	 * openingBalances, "1", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(unDepositedFunds);
	 * 
	 * Account accountsReceivable = new Account(
	 * Account.TYPE_OTHER_CURRENT_ASSET, "1001", "Debtors", true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, false,
	 * openingBalances, "2", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(accountsReceivable);
	 * 
	 * Account accountsPayable = new Account(
	 * Account.TYPE_OTHER_CURRENT_LIABILITY, "2001", "Creditors", true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
	 * openingBalances, "3", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(accountsPayable);
	 * 
	 * Account pendingItemReceipts = new Account(
	 * Account.TYPE_OTHER_CURRENT_LIABILITY, "2010",
	 * AccounterConstants.PENDING_ITEM_RECEIPTS, true, null,
	 * Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
	 * openingBalances, "4", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(pendingItemReceipts);
	 * 
	 * Account salesTaxPayable = new Account(
	 * Account.TYPE_OTHER_CURRENT_LIABILITY, "2050", "Sales Tax Payable", true,
	 * null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
	 * openingBalances, "5", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(salesTaxPayable);
	 * 
	 * Account employeePayrollLiabilities = new Account(
	 * Account.TYPE_PAYROLL_LIABILITY, "2110", "Employee Payroll Liabilities",
	 * true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
	 * openingBalances, "6", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(employeePayrollLiabilities);
	 * 
	 * Account retainedEarnings = new Account(Account.TYPE_EQUITY, "3100",
	 * "Retained Earnings", true, null, Account.CASH_FLOW_CATEGORY_FINANCING,
	 * 0.0, false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null,
	 * true, true, openingBalances, "8", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(retainedEarnings);
	 * 
	 * Account cashDiscountGiven = new Account(Account.TYPE_INCOME, "4100",
	 * "Income and Distribution", true, null,
	 * Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
	 * openingBalances, "9", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(cashDiscountGiven);
	 * 
	 * Account writeOff = new Account(Account.TYPE_INCOME, "4200", "Write off",
	 * true, null, Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
	 * openingBalances, "10", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(writeOff);
	 * 
	 * Account cashDiscountTaken = new Account( Account.TYPE_COST_OF_GOODS_SOLD,
	 * "5100", "Cash Discount taken", true, null,
	 * Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true,
	 * openingBalances, "11", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(cashDiscountTaken);
	 * 
	 * Account bankCharge = new Account(Account.TYPE_EXPENSE, "7250",
	 * "Bank Charge", true, null, Account.CASH_FLOW_CATEGORY_FINANCING, 0.0,
	 * false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false,
	 * true, openingBalances, "12", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(bankCharge);
	 * 
	 * // The following two accounts for Cash Basis Journal Entries purpose.
	 * Account otherCashIncome = new Account(Account.TYPE_INCOME, "4030",
	 * AccounterConstants.OTHER_CASH_INCOME, false, null,
	 * Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, true, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
	 * openingBalances, "13", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(otherCashIncome);
	 * 
	 * Account otherCashExpense = new Account(Account.TYPE_EXPENSE, "7900",
	 * AccounterConstants.OTHER_CASH_EXPENSE, false, null,
	 * Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, true, "", null,
	 * Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true,
	 * openingBalances, "14", true,
	 * this.preferences.getPreventPostingBeforeDate());
	 * 
	 * session.save(otherCashExpense);
	 * 
	 * this.accountsReceivableAccount = accountsReceivable;
	 * this.accountsPayableAccount = accountsPayable;
	 * this.openingBalancesAccount = openingBalances;
	 * this.retainedEarningsAccount = retainedEarnings;
	 * this.otherCashIncomeAccount = otherCashIncome;
	 * this.otherCashExpenseAccount = otherCashExpense;
	 * this.pendingItemReceiptsAccount = pendingItemReceipts;
	 * 
	 * createUSDefaultTaxGroup(session); createNominalCodesRanges(session);
	 * createDefaultBrandingTheme(session); }
	 */

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

	// public static Company getCompany() {
	// Company company = HibernateUtil.getCurrentSession() != null ? (Company)
	// HibernateUtil
	// .getCurrentSession().get(Company.class, 1L) : (Company) Utility
	// .getCurrentSession().get(Company.class, 1L);
	//
	// if (company == null)
	// return null;
	//
	// company.toCompany(company);
	// return company;
	// }

	/**
	 * @return the fixedAssets
	 */
	public ArrayList<FixedAsset> getFixedAssets() {
		return fixedAssets;
	}

	/**
	 * @param fixedAssets
	 *            the fixedAssets to set
	 */
	public void setFixedAssets(ArrayList<FixedAsset> fixedAssets) {
		this.fixedAssets = fixedAssets;
	}

	/**
	 * Creates entries in the TaxRateCalculation for the transctionItem entry in
	 * the transaction.
	 * 
	 * @param transactionItem
	 * @param session
	 * @param amount
	 * @return
	 */
	// public static boolean setTaxRateCalculation(
	// TransactionItem transactionItem, Session session, double amount) {
	//
	// if (!transactionItem.isBecameVoid()) {
	// if (transactionItem.getType() == TransactionItem.TYPE_ACCOUNT
	// || transactionItem.getType() == TransactionItem.TYPE_ITEM) {
	// if (transactionItem.taxGroup != null) {
	// for (TAXItem item : transactionItem.taxGroup.getTAXItems()) {
	//
	// prepareTaxRateCalculation(session, transactionItem,
	// item, amount);
	// }
	// } else if (transactionItem.taxItem != null) {
	//
	// TAXItem item = transactionItem.taxItem;
	// prepareTaxRateCalculation(session, transactionItem, item,
	// amount);
	// }
	// } else {
	// if (transactionItem.taxGroup != null) {
	// for (TAXItem taxItem : transactionItem.taxGroup
	// .getTAXItems()) {
	//
	// TaxRateCalculation rc = new TaxRateCalculation(amount,
	// taxItem.getTaxRate(), taxItem, transactionItem);
	// transactionItem.taxRateCalculationEntriesList.add(rc);
	//
	// TAXAgency taxAgency = taxItem.getTaxAgency();
	// if (taxAgency != null) {
	// taxAgency.updateBalance(session,
	// transactionItem.transaction,
	// rc.taxCollected);
	//
	// session.saveOrUpdate(taxAgency);
	// }
	//
	// preparePaySalesTaxEntries(transactionItem, taxItem
	// .getTaxRate(), taxItem);
	// }
	//
	// } else if (transactionItem.taxItem != null) {
	//
	// TaxRateCalculation rc = new TaxRateCalculation(amount,
	// transactionItem.taxItem.getTaxRate(),
	// transactionItem.taxItem, transactionItem);
	// transactionItem.taxRateCalculationEntriesList.add(rc);
	//
	// TAXAgency taxAgency = transactionItem.taxItem
	// .getTaxAgency();
	// if (taxAgency != null) {
	// taxAgency.updateBalance(session,
	// transactionItem.transaction, rc.taxCollected);
	//
	// session.saveOrUpdate(taxAgency);
	// }
	//
	// preparePaySalesTaxEntries(transactionItem,
	// transactionItem.taxItem.getTaxRate(),
	// transactionItem.taxItem);
	// }
	// }
	// } else {
	// if (transactionItem.getType() == TransactionItem.TYPE_ACCOUNT
	// || transactionItem.getType() == TransactionItem.TYPE_ITEM) {
	// // for (TaxCode code : transactionItem.taxGroup.getTaxCodes()) {
	// //
	// // TaxRateCalculation rc = getTaxRateCalculation(
	// // transactionItem, code);
	// // if (rc != null) {
	// // rc.getTaxAgency().updateBalance(session,
	// // transactionItem.transaction, -rc.taxCollected,
	// // rc);
	// // session.update(rc.taxAgency);
	// // }
	// //
	// // }
	//
	// for (TaxRateCalculation rc : getTaxRateCalculation(transactionItem)) {
	//
	// if (rc != null) {
	// rc.getTaxAgency().updateBalance(session,
	// transactionItem.transaction, -rc.taxCollected,
	// rc);
	// session.update(rc.taxAgency);
	// }
	//
	// }
	//
	// preparePaySalesTaxEntries(transactionItem, 0,
	// transactionItem.taxItem);
	// } else {
	//
	// TaxRateCalculation rc = getTaxRateCalculation(transactionItem,
	// transactionItem.taxItem);
	//
	// TAXAgency taxAgency = rc.getTaxAgency();
	// if (taxAgency != null) {
	// taxAgency.updateBalance(session,
	// transactionItem.transaction, amount, rc);
	//
	// session.saveOrUpdate(taxAgency);
	// }
	//
	// preparePaySalesTaxEntries(transactionItem, 0,
	// transactionItem.taxItem);
	// }
	// }
	//
	// return false;
	// }

	// private static ArrayList<TaxRateCalculation> getTaxRateCalculation(
	// TransactionItem transactionItem) {
	//
	// ArrayList<TaxRateCalculation> list = new ArrayList<TaxRateCalculation>();
	//
	// for (TaxRateCalculation trc :
	// transactionItem.taxRateCalculationEntriesList) {
	//
	// if (trc.transactionItem == transactionItem) {
	// list.add(trc);
	// }
	//
	// }
	//
	// return list;
	// }

	// private static TaxRateCalculation getTaxRateCalculation(
	// TransactionItem transactionItem, TAXItem item) {
	//
	// TaxRateCalculation result = null;
	//
	// for (TaxRateCalculation trc :
	// transactionItem.taxRateCalculationEntriesList) {
	//
	// if (trc.transactionItem == transactionItem && trc.taxItem == item) {
	// result = trc;
	// break;
	// }
	//
	// }
	//
	// return result;
	// }

	// private static void prepareTaxRateCalculation(Session session,
	// TransactionItem transactionItem, TAXItem taxItem, double amount) {
	//
	// double rate = taxItem.getTaxRate();
	// TaxRateCalculation rc = new TaxRateCalculation(amount, rate, taxItem,
	// transactionItem);
	// transactionItem.taxRateCalculationEntriesList.add(rc);
	//
	// double taxCollected = rc.taxCollected;
	//
	// updateTaxAgency(session, transactionItem, taxItem, taxCollected);
	//
	// preparePaySalesTaxEntries(transactionItem, rate, taxItem);
	// }

	// private static void updateTaxAgency(Session session,
	// TransactionItem transactionItem, TAXItem taxItem,
	// Double taxCollected) {
	//
	// taxItem.getTaxAgency().updateBalance(session,
	// transactionItem.transaction, taxCollected);
	//
	// session.saveOrUpdate(taxItem.getTaxAgency());
	// }

	/**
	 * For every entry in the TaxRateCalculation class we need to update the
	 * PaySAlesTax class. This class will be used to get the entries in to
	 * TransactionPaySalesTax while making the PaySalesTax.
	 * 
	 * @param transactionItem
	 * @param rate
	 * @param code
	 */
	// private static void preparePaySalesTaxEntries(
	// TransactionItem transactionItem, double rate, TAXItem item) {
	//
	// /**
	// * Here rate will be 100 for Transaction Item type SalesTax
	// */
	//
	// Session session = HibernateUtil.getCurrentSession() != null ?
	// HibernateUtil
	// .getCurrentSession()
	// : Utility.getCurrentSession();
	// boolean flag = false;
	// if (!transactionItem.isBecameVoid()) {
	//
	// for (PaySalesTaxEntries paySalesTaxEntries :
	// transactionItem.transaction.paySalesTaxEntriesList) {
	//
	// if (item.name.equalsIgnoreCase(paySalesTaxEntries.taxItem.name)) {
	// paySalesTaxEntries
	// .updateAmountAndBalane(transactionItem.lineTotal);
	//
	// session.saveOrUpdate(paySalesTaxEntries);
	// flag = true;
	// break;
	//
	// }
	//
	// }
	// if (!flag) {
	//
	// PaySalesTaxEntries paySalesTaxEntries = new PaySalesTaxEntries(
	// transactionItem.transaction, transactionItem.lineTotal,
	// rate, item);
	// // transactionItem.transaction.paySalesTaxEntriesList
	// // .add(paySalesTaxEntries);
	//
	// session.save(paySalesTaxEntries);
	//
	// }
	//
	// } else {
	// for (PaySalesTaxEntries paySalesTaxEntries :
	// transactionItem.transaction.paySalesTaxEntriesList) {
	//
	// if (transactionItem.taxItem.name
	// .equalsIgnoreCase(paySalesTaxEntries.taxItem.name)) {
	// paySalesTaxEntries.updateAmountAndBalane(-1
	// * transactionItem.lineTotal);
	// session.saveOrUpdate(paySalesTaxEntries);
	// break;
	//
	// }
	//
	// }
	//
	// }
	//
	// }

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

		} else {

			TAXItem vatItem = transactionItem.getTaxItem();
			if (vatItem != null) {
				setVatItemVRC(vatItem, transactionItem, session);
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
		if (code.getVATItemGrpForPurchases() != null) {

			if (code.getVATItemGrpForPurchases() instanceof TAXItem) {

				TAXItem vatItem = (TAXItem) code.getVATItemGrpForPurchases();

				setVatItemVRC(vatItem, transactionItem, session);

			} else {

				TAXGroup vatGroup = (TAXGroup) code.getVATItemGrpForPurchases();
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

	// private static void setPayVatEntries(TransactionItem transactionItem,
	// VATItem vatItem) {
	//
	// // No need to create separate PayVATEntries. All the entries in the
	// PayVAT can be get from VATReturns.
	//
	// VATCode code = transactionItem.getVatCode();
	//
	//
	//
	// PayVATEntries pt = getPaySalesTaxEntry(code);
	//
	// if(pt!=null)
	// {
	// pt.updateAmountAndBalane(transactionItem.getVATfraction());
	// }
	// else
	// {
	// pt=new PayVATEntries(transactionItem.getLineTotal(),
	// vatItem.getVatRate(), code, vatItem.getVatAgency(),
	// transactionItem.getTransaction());
	// }
	// transactionItem.transaction.payVATEntriesList.add(pt);
	//
	// }

	// private static PayVATEntries getPaySalesTaxEntry(VATCode code) {
	// return null;
	//
	// }

	// <<<<<<< .working
	// public void setVatCodes(ArrayList<VATCode> vatCodes) {
	// this.vatCodes = vatCodes;
	// }

	// public ArrayList<VATCode> getVatCodes() {
	// FinanceDate presentDate = new FinanceDate();
	// if (vatCodes != null) {
	// if (presentDate.before(new FinanceDate(111, 0, 4))) {
	// for (int i = 0; i < vatCodes.size(); i++) {
	// VATCode vatCode = vatCodes.get(i);
	// if (vatCode != null && vatCode.getName().equals("New S")) {
	// vatCodes.remove(i);
	// i--;
	// break;
	// }
	// }
	// } else {
	// for (int i = 0; i < vatCodes.size(); i++) {
	// VATCode vatCode = vatCodes.get(i);
	// if (vatCode != null && vatCode.getName().equals("S"))
	// vatCodes.get(i).setName("Old S");
	// else if (vatCode != null
	// && vatCode.getName().equals("New S"))
	// vatCodes.get(i).setName("S");
	// }
	// }
	// }
	// return vatCodes;
	// }

	// public void setVatItemGroups(ArrayList<VATItemGroup> vatItemGroups) {
	// =======
	public void setTaxItemGroups(ArrayList<TAXItemGroup> vatItemGroups) {
		// >>>>>>> .merge-right.r20318
		if (vatItemGroups == null) {
			return;
		}

		taxItems = new ArrayList<TAXItem>();

		taxGroups = new ArrayList<TAXGroup>();

		for (TAXItemGroup vatItemGroup : vatItemGroups) {

			if (vatItemGroup instanceof TAXItem) {
				taxItems.add((TAXItem) vatItemGroup);
			} else if (vatItemGroup instanceof TAXGroup) {
				taxGroups.add((TAXGroup) vatItemGroup);
			}
		}

		this.taxItemGroups = vatItemGroups;
	}

	public ArrayList<TAXItemGroup> getTaxItemGroups() {
		return taxItemGroups;
	}

	public void setVatReturnBoxes(Set<VATReturnBox> vatReturnBoxes) {
		this.vatReturnBoxes = vatReturnBoxes;
	}

	public Set<VATReturnBox> getVatReturnBoxes() {
		return vatReturnBoxes;
	}

	public Set<Box> getVatBoxes() {
		return vatBoxes;
	}

	public void setVatBoxes(Set<Box> vatBoxes) {
		this.vatBoxes = vatBoxes;
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
		cmp.createdBy = this.getCreatedBy();
		cmp.lastModifier = this.getLastModifier();
		cmp.createdDate = this.getCreatedDate();
		cmp.lastModifiedDate = this.getLastModifiedDate();
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

		cmp.ItemGroups = this.getItemGroups();

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

	/*
	 * private void createDefaultBrandingTheme(Session session) { BrandingTheme
	 * brandingTheme = new BrandingTheme("Standard", SecureUtils.createID(),
	 * 1.35, 1.00, 1.00, "Times New Roman", "10pt", "INVOICE", "CREDIT",
	 * "STATEMENT", "democo@democo.co", true, this.getName(), "(None Added)");
	 * session.save(brandingTheme); }
	 */

	public ArrayList<BrandingTheme> getBrandingTheme() {
		return brandingTheme;
	}

	public void setBrandingTheme(ArrayList<BrandingTheme> brandingTheme) {
		this.brandingTheme = brandingTheme;
	}

	public void setUsersList(ArrayList<User> usersList) {
		this.usersList = usersList;
	}

	public ArrayList<User> getUsersList() {
		return usersList;
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
		// Session session = HibernateUtil.getCurrentSession();
		// User user = (User) session.getNamedQuery("getuser.by.email")
		// .setParameter(0, email).uniqueResult();
		// return user;
	}

	public void setCurrencies(Set<Currency> currencies) {
		this.currencies = currencies;
	}

	public Set<Currency> getCurrencies() {
		return currencies;
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

	public ArrayList<AccounterClass> getAccounterClasses() {
		return accounterClasses;
	}

	public void setAccounterClasses(ArrayList<AccounterClass> accounterClasses) {
		this.accounterClasses = accounterClasses;
	}

	public ArrayList<Location> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<Location> arrayList) {
		this.locations = arrayList;
	}
}
