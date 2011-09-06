package com.vimukti.accounter.core;

import java.util.List;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class CompanyPreferences implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6169837986493037863L;

	private static final long USE_ACCOUNT_NO = 0x1L;

	private static final long USE_CLASSES = 0x2L;

	private static final long USE_JOBS = 0x4L;

	private static final long USE_CHANGE_LOG = 0x8L;

	private static final long HAVE_EMPLOYEES = 0x10L;

	private static final long HAVE_W2_EMPLOYEES = 0x20L;

	private static final long HAVE_1099_CONTRACTORS = 0x40L;

	private static final long TRACK_EMPLOYEE_EXPENSES = 0x80L;

	private static final long WANT_ESTIMATES = 0x100L;

	private static final long USE_FOREIGN_CURRENCY = 0x200L;

	private static final long MY_ACCOUNT_WILL_RUN_PAYROLL = 0x400L;

	private static final long ENABLE_MULTI_CURRENCY = 0x800L;

	private static final long KEEP_TRACK_OF_BILLS = 0x1000L;

	private static final long KEEP_TRACK_OF_TIME = 0x2000L;

	private static final long CHARGE_SALES_TAX = 0x4000L;

	/**
	 * whether the Customers in our company pay Sales Tax or not
	 */
	private static final long PAY_SALES_TAX = 0x8000L;

	/**
	 * whether we can use Id's for the Customer while creating them or not.
	 */
	private static final long USE_CUSTOMER_ID = 0x10000L;

	/**
	 * To Confirm whether we can allow duplicate {@link Transaction} Numbers or
	 * not
	 */
	private static final long ALLOW_DUPLICATE_DOCUMENT_NO = 0x20000L;

	private static final long SALES_ORDER_ENABLE = 0x40000L;

	private static final long PURCHASE_ORDER_ENABLE = 0x80000L;

	private static final long USE_VENDOR_ID = 0x100000L;

	private static final long USE_ITEM_NUMBERS = 0x200000L;

	private static final long SALES_PERSON_ENABLE = 0x400000L;

	private static final long CALCULATE_FINANCE_CHARGE_FROM_INVOICE_DATE = 0x800000L;

	private static final long CHECK_ITEM_QUANTITY_ON_HAND = 0x1000000L;

	private static final long UPDATE_COST_AUTOMATIC = 0x2000000L;

	private static final long SELL_SERVICES = 0x4000000L;

	private static final long SELL_PRODUCTS = 0x8000000L;

	private static final long ENTER_VAT_INFORMATION_NOW = 0x10000000L;

	private static final long REPORT_VAT_ON_ACURAL_BASIS = 0x20000000L;

	private static final long TRACK_VAT = 0x40000000L;

	private static final long IS_ACCURAL_BASIS = 0x80000000L;

	private static final long IS_BEGINNING_ON_TODAYS_DATE = 0x100000000L;

	private static final long WANT_STATEMENTS = 0x200000000L;

	public static int VAT_REPORTING_PERIOD_MONTHLY = 1;
	public static int VAT_REPORTING_PERIOD_BIMONTHLY = 2;
	public static int VAT_REPORTING_PERIOD_QUARTERLY = 3;
	public static int VAT_REPORTING_PERIOD_ANUALLY = 4;

	public static int VAT_REP_ENDPERIOD_MAR_JUN_SEP_DEC = 1;
	public static int VAT_REP_ENDPERIOD_APR_JUL_OCT_JAN = 2;
	public static int VAT_REP_ENDPERIOD_MAY_AUG_NOV_FEB = 3;

	// Company Preferrences

	// /**
	// * To confirm whether we can use Account Numbers or not.
	// */
	// boolean useAccountNumbers = true;
	// // property existed from our older QuickBooks Code
	// boolean useClasses = false;
	// // property existed from our older QuickBooks Code
	// boolean useJobs = false;
	// // property existed from our older QuickBooks Code
	// boolean useChangeLog = false;
	// // property existed from our older QuickBooks Code
	double logSpaceUsed = 0D;

	int ageingFromTransactionDateORDueDate = 1;

	FinanceDate startOfFiscalYear = new FinanceDate();
	FinanceDate endOfFiscalYear = new FinanceDate();

	FinanceDate startDate = new FinanceDate();

	private FinanceDate depreciationStartDate = new FinanceDate();

	FinanceDate preventPostingBeforeDate;
	/**
	 * The Shipping Term which can be displayed in the required
	 * {@link Transaction}s.
	 */
	ShippingTerms defaultShippingTerm;

	int defaultAnnualInterestRate = 0;
	double defaultMinimumFinanceCharge = 0D;
	int graceDays = 0;
	String VATregistrationNumber = "";
	int VATreportingPeriod = VAT_REPORTING_PERIOD_QUARTERLY;
	int endingPeriodForVATreporting = VAT_REP_ENDPERIOD_MAR_JUN_SEP_DEC;
	String VATtaxAgencyName = AccounterServerConstants.DEFAULT_VAT_AGENCY_NAME;
	private String dateFormat = AccounterServerConstants.ddMMyyyy;
	public long id;

	// currency related properties
	private Currency primaryCurrency;
	private List<Currency> supportingCurrenciesList;

	// Organization type
	private int organizationType;

	// for tracking employes in setup page
	private int referCustomers;
	private int referVendors;
	private int referAccounts;
	private int industryType;
	// for select fiscal year in setup

	int fiscalYearFirstMonth;
	FinanceDate trackFinanceDate;

	private long preferencesFlag;

	/*
	 * *********Start Location Tracking******
	 */
	private boolean isLocationTrackingEnabled;

	private boolean isClassTrackingEnabled;

	private boolean isClassOnePerTransaction;

	public boolean isLocationTrackingEnabled() {
		return isLocationTrackingEnabled;
	}

	public void setLocationTrackingEnabled(boolean isLocationTrackingEnabled) {
		this.isLocationTrackingEnabled = isLocationTrackingEnabled;
	}

	private long locationTrackingId;

	private int version;

	public long getLocationTrackingId() {
		return locationTrackingId;
	}

	public void setLocationTrackingId(long locationTrackingId) {
		this.locationTrackingId = locationTrackingId;
	}

	/*
	 * *******End of Location Tracking*****
	 */

	public CompanyPreferences() {

		// These Values are Default to TRUE
		/**
		 * if True then Accural (when customer is Invoiced), if False then Cash
		 * Basis(when customer pays Invoice)
		 */
		this.preferencesFlag |= (USE_ACCOUNT_NO | IS_ACCURAL_BASIS
				| SELL_SERVICES | SELL_SERVICES | ENTER_VAT_INFORMATION_NOW
				| REPORT_VAT_ON_ACURAL_BASIS | TRACK_VAT);
	}

	public boolean isPurchaseOrderEnabled() {
		return get(PURCHASE_ORDER_ENABLE);
	}

	public void setPurchaseOrderEnabled(boolean value) {
		set(PURCHASE_ORDER_ENABLE, value);
	}

	public FinanceDate getTrackFinanceDate() {
		return trackFinanceDate;
	}

	public void setTrackFinanceDate(FinanceDate trackFinanceDate) {
		this.trackFinanceDate = trackFinanceDate;
	}

	public boolean isBeginingorTodaysdate() {
		return get(IS_BEGINNING_ON_TODAYS_DATE);
	}

	public void setBeginingorTodaysdate(boolean value) {
		set(IS_BEGINNING_ON_TODAYS_DATE, value);
	}

	public int getFiscalYearFirstMonth() {
		return fiscalYearFirstMonth;
	}

	public void setFiscalYearFirstMonth(int fiscalYearFirstMonth) {
		this.fiscalYearFirstMonth = fiscalYearFirstMonth;
	}

	public boolean isHaveEpmloyees() {
		return get(HAVE_EMPLOYEES);
	}

	public void setHaveEpmloyees(boolean value) {
		set(HAVE_EMPLOYEES, value);
	}

	public boolean isHaveW_2Employees() {
		return get(HAVE_W2_EMPLOYEES);
	}

	public void setHaveW_2Employees(boolean value) {
		set(HAVE_W2_EMPLOYEES, value);
	}

	public boolean isHave1099contractors() {
		return get(HAVE_1099_CONTRACTORS);
	}

	public void setHave1099contractors(boolean value) {
		set(HAVE_1099_CONTRACTORS, value);
	}

	public boolean isTrackEmployeeExpenses() {
		return get(TRACK_EMPLOYEE_EXPENSES);
	}

	public void setTrackEmployeeExpenses(boolean value) {
		set(TRACK_EMPLOYEE_EXPENSES, value);
	}

	public Currency getPrimaryCurrency() {
		return primaryCurrency;
	}

	public void setPrimaryCurrency(Currency primaryCurrency) {
		this.primaryCurrency = primaryCurrency;
	}

	public List<Currency> getSupportingCurrenciesList() {
		return supportingCurrenciesList;
	}

	public void setSupportingCurrenciesList(
			List<Currency> supportingCurrenciesList) {
		this.supportingCurrenciesList = supportingCurrenciesList;
	}

	public boolean isSellServices() {
		return get(SELL_SERVICES);
	}

	public void setSellServices(boolean value) {
		set(SELL_SERVICES, value);
	}

	public boolean isSellProducts() {
		return get(SELL_PRODUCTS);
	}

	public void setSellProducts(boolean value) {
		set(SELL_PRODUCTS, value);
	}

	// String legalName = "";

	/**
	 * @return the legalName
	 */
	// public String getLegalName() {
	// return legalName;
	// }

	/**
	 * @param legalName
	 *            the legalName to set
	 */
	// public void setLegalName(String legalName) {
	// this.legalName = legalName;
	// }

	public String getVATtaxAgencyName() {
		return VATtaxAgencyName;
	}

	public void setVATtaxAgencyName(String ttaxAgencyName) {
		VATtaxAgencyName = ttaxAgencyName;
	}

	/**
	 * @return the enterVATinformationNow
	 */
	public boolean isEnterVATinformationNow() {
		return get(ENTER_VAT_INFORMATION_NOW);
	}

	/**
	 * @param enterVATinformationNow
	 *            the enterVATinformationNow to set
	 */
	public void setEnterVATinformationNow(boolean value) {
		set(ENTER_VAT_INFORMATION_NOW, value);
	}

	/**
	 * @return the vATregistrationNumber
	 */
	public String getVATregistrationNumber() {
		return VATregistrationNumber;
	}

	/**
	 * @param vATregistrationNumber
	 *            the vATregistrationNumber to set
	 */
	public void setVATregistrationNumber(String vATregistrationNumber) {
		VATregistrationNumber = vATregistrationNumber;
	}

	/**
	 * @return the vATreportingPeriod
	 */
	public int getVATreportingPeriod() {
		return VATreportingPeriod;
	}

	/**
	 * @param vATreportingPeriod
	 *            the vATreportingPeriod to set
	 */
	public void setVATreportingPeriod(int vATreportingPeriod) {
		VATreportingPeriod = vATreportingPeriod;
	}

	/**
	 * @return the endingPeriodForVATreporting
	 */
	public int getEndingPeriodForVATreporting() {
		return endingPeriodForVATreporting;
	}

	/**
	 * @param endingPeriodForVATreporting
	 *            the endingPeriodForVATreporting to set
	 */
	public void setEndingPeriodForVATreporting(int endingPeriodForVATreporting) {
		this.endingPeriodForVATreporting = endingPeriodForVATreporting;
	}

	/**
	 * @return the reportVATonAccuralBasis
	 */
	public boolean isReportVATonAccuralBasis() {
		return get(REPORT_VAT_ON_ACURAL_BASIS);
	}

	/**
	 * @param reportVATonAccuralBasis
	 *            the reportVATonAccuralBasis to set
	 */
	public void setReportVATonAccuralBasis(boolean value) {
		set(REPORT_VAT_ON_ACURAL_BASIS, value);
	}

	/**
	 * @return the trackVAT
	 */
	public boolean isTrackVAT() {
		return get(TRACK_VAT);
	}

	/**
	 * @param trackVAT
	 *            the trackVAT to set
	 */
	public void setTrackVAT(boolean value) {
		set(TRACK_VAT, value);
	}

	/**
	 * @param logSpaceUsed
	 *            the logSpaceUsed to set
	 */
	public void setLogSpaceUsed(double logSpaceUsed) {
		this.logSpaceUsed = logSpaceUsed;
	}

	/**
	 * @param isMyAccountantWillrunPayroll
	 *            the isMyAccountantWillrunPayroll to set
	 */
	public void setMyAccountantWillrunPayroll(boolean value) {
		set(MY_ACCOUNT_WILL_RUN_PAYROLL, value);
	}

	/**
	 * @return the useAccountNumbers
	 */
	public boolean getUseAccountNumbers() {
		return get(USE_ACCOUNT_NO);
	}

	/**
	 * @param useAccountNumbers
	 *            the useAccountNumbers to set
	 */
	public void setUseAccountNumbers(boolean value) {
		set(USE_ACCOUNT_NO, value);
	}

	public FinanceDate getStartDate() {
		return startDate;
	}

	public void setStartDate(FinanceDate startDate) {
		this.startDate = startDate;
	}

	public FinanceDate getPreventPostingBeforeDate() {
		return preventPostingBeforeDate;
	}

	public void setPreventPostingBeforeDate(FinanceDate preventPostingBeforeDate) {
		this.preventPostingBeforeDate = preventPostingBeforeDate;
	}

	/**
	 * @return the useClasses
	 */
	public boolean getUseClasses() {
		return get(USE_CLASSES);
	}

	/**
	 * @param useClasses
	 *            the useClasses to set
	 */
	public void setUseClasses(boolean value) {
		set(USE_CLASSES, value);
	}

	/**
	 * @return the useJobs
	 */
	public boolean getUseJobs() {
		return get(USE_JOBS);
	}

	/**
	 * @param useJobs
	 *            the useJobs to set
	 */
	public void setUseJobs(boolean value) {
		set(USE_JOBS, value);
	}

	/**
	 * @return the useChangeLog
	 */
	public boolean getUseChangeLog() {
		return get(USE_CHANGE_LOG);
	}

	/**
	 * @param useChangeLog
	 *            the useChangeLog to set
	 */
	public void setUseChangeLog(boolean value) {
		set(USE_CHANGE_LOG, value);
	}

	/**
	 * @return the logSpaceUsed
	 */
	public double getLogSpaceUsed() {
		return logSpaceUsed;
	}

	/**
	 * @return the allowDuplicateDocumentNumbers
	 */
	public boolean getAllowDuplicateDocumentNumbers() {
		return get(ALLOW_DUPLICATE_DOCUMENT_NO);
	}

	/**
	 * @param allowDuplicateDocumentNumbers
	 *            the allowDuplicateDocumentNumbers to set
	 */
	public void setAllowDuplicateDocumentNumbers(boolean value) {
		set(ALLOW_DUPLICATE_DOCUMENT_NO, value);
	}

	/**
	 * @return the doYouPaySalesTax
	 */
	public boolean getDoYouPaySalesTax() {
		return get(PAY_SALES_TAX);
	}

	/**
	 * @param doYouPaySalesTax
	 *            the doYouPaySalesTax to set
	 */
	public void setDoYouPaySalesTax(boolean value) {
		set(PAY_SALES_TAX, value);
	}

	public boolean isAccuralBasis() {
		return get(IS_ACCURAL_BASIS);
	}

	/**
	 * @param isAccuralBasis
	 *            the isAccuralBasis to set
	 */
	public void setIsAccuralBasis(boolean value) {
		set(IS_ACCURAL_BASIS, value);
	}

	/**
	 * @return the startOfFiscalYear
	 */
	public FinanceDate getStartOfFiscalYear() {
		return startOfFiscalYear;
	}

	/**
	 * @param startOfFiscalYear
	 *            the startOfFiscalYear to set
	 */
	public void setStartOfFiscalYear(FinanceDate startOfFiscalYear) {
		this.startOfFiscalYear = startOfFiscalYear;
	}

	/**
	 * @return the endOfFiscalYear
	 */
	public FinanceDate getEndOfFiscalYear() {
		return endOfFiscalYear;
	}

	/**
	 * @param endOfFiscalYear
	 *            the endOfFiscalYear to set
	 */
	public void setEndOfFiscalYear(FinanceDate endOfFiscalYear) {
		this.endOfFiscalYear = endOfFiscalYear;
	}

	/**
	 * @return the isMyAccountantWillrunPayroll
	 */
	public boolean getIsMyAccountantWillrunPayroll() {
		return get(MY_ACCOUNT_WILL_RUN_PAYROLL);
	}

	/**
	 * @return the useCustomerId
	 */
	public boolean getUseCustomerId() {
		return get(USE_CUSTOMER_ID);
	}

	/**
	 * @param useCustomerId
	 *            the useCustomerId to set
	 */
	public void setUseCustomerId(boolean value) {
		set(USE_CUSTOMER_ID, value);
	}

	/**
	 * @return the defaultShippingTerm
	 */
	public ShippingTerms getDefaultShippingTerm() {
		return defaultShippingTerm;
	}

	/**
	 * @param defaultShippingTerm
	 *            the defaultShippingTerm to set
	 */
	public void setDefaultShippingTerm(ShippingTerms defaultShippingTerm) {
		this.defaultShippingTerm = defaultShippingTerm;
	}

	/**
	 * @return the defaultAnnualInterestRate
	 */
	public int getDefaultAnnualInterestRate() {
		return defaultAnnualInterestRate;
	}

	/**
	 * @param defaultAnnualInterestRate
	 *            the defaultAnnualInterestRate to set
	 */
	public void setDefaultAnnualInterestRate(int defaultAnnualInterestRate) {
		this.defaultAnnualInterestRate = defaultAnnualInterestRate;
	}

	/**
	 * @return the defaultMinimumFinanceCharge
	 */
	public double getDefaultMinimumFinanceCharge() {
		return defaultMinimumFinanceCharge;
	}

	/**
	 * @param defaultMinimumFinanceCharge
	 *            the defaultMinimumFinanceCharge to set
	 */
	public void setDefaultMinimumFinanceCharge(
			double defaultMinimumFinanceCharge) {
		this.defaultMinimumFinanceCharge = defaultMinimumFinanceCharge;
	}

	/**
	 * @return the graceDays
	 */
	public int getGraceDays() {
		return graceDays;
	}

	/**
	 * @param graceDays
	 *            the graceDays to set
	 */
	public void setGraceDays(int graceDays) {
		this.graceDays = graceDays;
	}

	/**
	 * @return the ageing
	 */
	public int getAgeingFromTransactionDateORDueDate() {
		return ageingFromTransactionDateORDueDate;
	}

	/**
	 * @param ageing
	 *            the ageing to set
	 */
	public void setAgeingFromTransactionDateORDueDate(
			int ageingFromTransactionDateORDueDate) {
		this.ageingFromTransactionDateORDueDate = ageingFromTransactionDateORDueDate;
	}

	/**
	 * @return the doesCalculateFinanceChargeFromInvoiceDate
	 */
	public boolean getDoesCalculateFinanceChargeFromInvoiceDate() {
		return get(CALCULATE_FINANCE_CHARGE_FROM_INVOICE_DATE);
	}

	/**
	 * @param doesCalculateFinanceChargeFromInvoiceDate
	 *            the doesCalculateFinanceChargeFromInvoiceDate to set
	 */
	public void setDoesCalculateFinanceChargeFromInvoiceDate(boolean value) {
		set(CALCULATE_FINANCE_CHARGE_FROM_INVOICE_DATE, value);
	}

	/**
	 * @return the useVendorId
	 */
	public boolean getUseVendorId() {
		return get(USE_VENDOR_ID);
	}

	/**
	 * @param useVendorId
	 *            the useVendorId to set
	 */
	public void setUseVendorId(boolean value) {
		set(USE_VENDOR_ID, value);
	}

	/**
	 * @return the useItemNumbers
	 */
	public boolean getUseItemNumbers() {
		return get(USE_ITEM_NUMBERS);
	}

	/**
	 * @param useItemNumbers
	 *            the useItemNumbers to set
	 */
	public void setUseItemNumbers(boolean value) {
		set(USE_ITEM_NUMBERS, value);
	}

	/**
	 * @return the checkForItemQuantityOnHand
	 */
	public boolean getCheckForItemQuantityOnHand() {
		return get(CHECK_ITEM_QUANTITY_ON_HAND);
	}

	/**
	 * @param checkForItemQuantityOnHand
	 *            the checkForItemQuantityOnHand to set
	 */
	public void setCheckForItemQuantityOnHand(boolean value) {
		set(CHECK_ITEM_QUANTITY_ON_HAND, value);
	}

	/**
	 * @return the updateCostAutomatically
	 */
	public boolean getUpdateCostAutomatically() {
		return get(UPDATE_COST_AUTOMATIC);
	}

	/**
	 * @param updateCostAutomatically
	 *            the updateCostAutomatically to set
	 */
	public void setUpdateCostAutomatically(boolean value) {
		set(UPDATE_COST_AUTOMATIC, value);
	}

	@Override
	public long getID() {
		return this.id;
	}

	public FinanceDate getDepreciationStartDate() {
		return depreciationStartDate;
	}

	public void setDepreciationStartDate(FinanceDate depreciationStartDate) {
		this.depreciationStartDate = depreciationStartDate;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		return true;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public boolean isSalesOrderEnabled() {
		return get(SALES_ORDER_ENABLE);
	}

	public void setSalesOrderEnabled(boolean value) {
		set(SALES_ORDER_ENABLE, value);
	}

	public void setEnableMultiCurrency(boolean value) {
		set(ENABLE_MULTI_CURRENCY, value);
	}

	public boolean isSalesPersonEnabled() {
		return get(SALES_PERSON_ENABLE);
	}

	public void setSalesPerson(boolean value) {
		set(SALES_PERSON_ENABLE, value);
	}

	public int getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(int organizationType) {
		this.organizationType = organizationType;
	}

	public boolean isDoYouChargesalesTax() {
		return get(CHARGE_SALES_TAX);
	}

	public void setDoYouChargesalesTax(boolean value) {
		set(CHARGE_SALES_TAX, value);
	}

	public boolean isDoyouKeepTrackofBills() {
		return get(KEEP_TRACK_OF_BILLS);
	}

	public void setDoyouKeepTrackofBills(boolean value) {
		set(KEEP_TRACK_OF_BILLS, value);
	}

	public boolean isDoYouKeepTrackOfTime() {
		return get(KEEP_TRACK_OF_TIME);
	}

	public void setDoYouKeepTrackOfTime(boolean value) {
		set(KEEP_TRACK_OF_TIME, value);
	}

	public int getReferCustomers() {
		return referCustomers;
	}

	public void setReferCustomers(int referCustomers) {
		this.referCustomers = referCustomers;
	}

	public int getReferVendors() {
		return referVendors;
	}

	public void setReferVendors(int referSuplliers) {
		this.referVendors = referSuplliers;
	}

	public int getReferAccounts() {
		return referAccounts;
	}

	public void setReferAccounts(int referAccounts) {
		this.referAccounts = referAccounts;
	}

	public boolean isDoyouwantEstimates() {
		return get(WANT_ESTIMATES);
	}

	public void setDoyouwantEstimates(boolean value) {
		set(WANT_ESTIMATES, value);
	}

	public boolean isDoyouwantstatements() {
		return get(WANT_STATEMENTS);
	}

	public void setDoyouwantstatements(boolean value) {
		set(WANT_STATEMENTS, value);
	}

	public int getIndustryType() {
		return industryType;
	}

	public void setIndustryType(int industryType) {
		this.industryType = industryType;
	}

	/**
	 * @return the preferencesFlag
	 */
	public long getPreferencesFlag() {
		return preferencesFlag;
	}

	/**
	 * @param preferencesFlag
	 *            the preferencesFlag to set
	 */
	public void setPreferencesFlag(long preferencesFlag) {
		this.preferencesFlag = preferencesFlag;
	}

	/*
	 * // Accounting Preferences
	 * 
	 * 
	 * boolean requireAccounts; boolean showLowestSubAccountOnly; Boolean
	 * useClassTracking; boolean promptToAssignClasses; Boolean
	 * automaticallyAssignGeneralJournalEntryNumber = Boolean.TRUE; Boolean
	 * warnWhenPostingATransactionToRetainedEarnings = Boolean.TRUE; Boolean
	 * warnIfTransactionsAreDaysPast = Boolean.TRUE; Boolean
	 * warnIfTransactionsAreDaysFuture = Boolean.TRUE;
	 * 
	 * int deafaultPastDays = 90; int defaultFuturedays = 30;
	 * 
	 * Date closingDate;
	 * 
	 * String closingDatePassword; String closingDatePasswordConfirmation;
	 * 
	 * // Bills Preferences int defaultBillsDueDate = 10; Boolean
	 * warnAboutDuplicateBillNumbersFromSameVendors; Boolean
	 * automaticallyUseDiscountsAndCredits; Account defaultDiscountAccount;
	 * 
	 * // Checking Preferences
	 * 
	 * boolean printAccountNamesOnVoucher; Boolean
	 * changeCheckDateWhenCheckIsPrinted; boolean startWithPayeeFieldOnCheck;
	 * boolean warnAboutDuplicateCheckNumbers = Boolean.TRUE; Boolean
	 * autofillPayeeAccountNumberinCheckMemo = Boolean.TRUE; Boolean
	 * enableCreatePayChecksWithDefaultAccount; Boolean
	 * enablePayOfPayRollLiablitiesWithDefaultAccount; Account
	 * defaultCreatePayCheckAccount; Account defaultPay_PayRollLiabalities;
	 * 
	 * // DeskTop View Preferences
	 * 
	 * boolean enableInvoices = Boolean.TRUE; boolean enableSalesReciepts =
	 * Boolean.TRUE; boolean enableStatementsAndStatementCharges = Boolean.TRUE;
	 * boolean enterBillsAndPayBills = Boolean.TRUE;
	 * 
	 * // Finance Charge Preferences double annualIntrestRateinPercent; Double
	 * minimumFinanceCharge; Integer minimumGracePeriodinDays; Account
	 * financeChargeAccount; Boolean
	 * accessFinanceChargesOnOverDueFinancialCharges; Date calculateChargesFrom;
	 * boolean markFinanceChargeInvoices; int timeFormat =
	 * GENERAL_TIME_FORMAT_MINUTES; boolean alwaysShowYearsAsFourDigit =
	 * Boolean.TRUE; boolean neverUpdateNameinformationWhenSavingTransactions;
	 * boolean saveTransactionsBeforePrinting = Boolean.TRUE;
	 * 
	 * // Integrated Applications
	 * 
	 * boolean allowApplicationsToAccessCompanyFile; Boolean
	 * notifyTheUserBeforeRunningTheApplicationWhoseCertificateHasExpired;
	 * 
	 * // Items And InventoryPreferences
	 * 
	 * boolean inventoryAndPurchaseOrdersAreActive; Boolean
	 * warnAboutDuplicatePurchaseNumbers; boolean deductQuantityOnSalesOrders;
	 * boolean warnIfNotInventoryToSale; int warnType; Boolean
	 * enableUnitOfMeasure;
	 * 
	 * // Jobs And Estimates
	 * 
	 * String pendingStatus; String awarded; String inProgress; String closed;
	 * String notAwarded; boolean enableCreateEstimates; Boolean
	 * enableProgressInvoicing; boolean warnAboutDuplicateEstimateNumbers;
	 * boolean printZeroAmount;
	 * 
	 * // Multiple Currencies
	 * 
	 * boolean singleCurrency; Set<Currency> currencies;
	 * 
	 * // PayRoll & Employee Preferences
	 * 
	 * int payRollFeatureType;
	 * 
	 * PayRollPrintingPreferences payrollPrintingOptions;
	 * 
	 * // Reminders Preferences
	 * 
	 * int checksToPrint; int payChecksToPrint; int
	 * invoicesOrCreditMemosToPrint; int overdueInvoices; int
	 * salesRecieptsToPrint; int salesOrdersToPrint; int inventoryToReorder; int
	 * assemblyItemsToBuild; int billsToPay; int memorizedTransactionsDue; int
	 * moneyToDeposit; int purchaseOrdersToPrint; int todoNotes;
	 *//**
	 * 
	 */
	/*
	 * int openAuthorizationsToCapture;
	 * 
	 * // Reports And Graph Preferences
	 * 
	 * int summaryBasedOn; int showAccountBy; int ageingReportsType;
	 * 
	 * // Sales And Customers Preferences
	 * 
	 * int usualShippingMethod; boolean usePriceLevels; Boolean
	 * warnAboutDuplicateInvoiceNumbers; Boolean
	 * dontPrintWithZeroAmountsWhenConvertingToInvoice;
	 * 
	 * boolean automaticallyApplyPayments; Boolean
	 * automaticallyCalculatePayments; Boolean
	 * useUndepositedFundAsADefaultDepositToCode;
	 * 
	 * // Sales Tax Preferences
	 * 
	 * boolean chargeSalesTax;
	 * 
	 * boolean enableProductsAndServicesForIncomeAccounts;
	 * 
	 * boolean enableLocationTracking;
	 * 
	 * int locationType;
	 * 
	 * boolean enableClassTracking;
	 * 
	 * boolean enableClassWarning;
	 * 
	 * int typeOfClassesOnSales; // One Per Transaction or One Per Detail line
	 * 
	 * boolean enableCustomTransactionNumbes;
	 * 
	 * boolean enableDelayedCharges;
	 * 
	 * boolean enableDeposits;
	 * 
	 * boolean enableEstimates;
	 * 
	 * int estimatesBillingMethod;
	 * 
	 * String messageToCustomersOnEstimates;
	 * 
	 * boolean enableQuantityAndRate;
	 * 
	 * boolean enableSalesTax;
	 * 
	 * boolean makeAllExistingProductsAndServiesTaxable;
	 * 
	 * boolean makeAllExistingCustomersTaxable;
	 * 
	 * boolean newCustomersAreTaxable;
	 * 
	 * boolean newProductsAndServicesAreTaxable;
	 * 
	 * Account salesTaxAccount;
	 * 
	 * double salesTaxRate;
	 * 
	 * boolean enableServiceDates;
	 * 
	 * boolean enableShipping;
	 * 
	 * Account shippingAccount;
	 * 
	 * int defaultInvoiceTerms;
	 * 
	 * boolean automaticallyApplyCredits;
	 * 
	 * int salesFormDefaultDeliveryMethod;
	 * 
	 * boolean insertAGreetingBeforeMessageText;
	 * 
	 * int greetingFormat;
	 * 
	 * int messageFor;
	 * 
	 * String emailSubject;
	 * 
	 * String emailMessage;
	 * 
	 * boolean copyMeOnAllFormsSentByEmail;
	 * 
	 * boolean printACopyOnAllFormsSebtByEmail;
	 * 
	 * boolean enableEmailSalesFormAsAttachment;
	 * 
	 * int emailBodyAttachmentType;
	 * 
	 * boolean showAgingInformation;
	 * 
	 * boolean showSummary;
	 * 
	 * int invoiceAutomationMethod;
	 * 
	 * boolean manageBillsToPayLater;
	 * 
	 * boolean enableExpenseTrackingByCustomer;
	 * 
	 * boolean useBillableExpenses;
	 * 
	 * double defaultMarkUpRate;
	 * 
	 * int typeOfBillableExpenseIncomeAccount;
	 * 
	 * Account incomeAccount;
	 * 
	 * boolean enableMultipleSpliLines;
	 * 
	 * boolean duplicateCheckWarning;
	 * 
	 * boolean duplicateBillWarning;
	 * 
	 * int defaultBillTerms;
	 * 
	 * boolean automaticallyApplyBillPayments;
	 * 
	 * boolean useServiesForTimeTracking;
	 * 
	 * boolean billCustomerForTime;
	 * 
	 * boolean showBillingRateToEmployeesAndSubContractors;
	 */

	private boolean get(long flag) {
		return (this.preferencesFlag & flag) == flag;
	}

	private void set(long flag, boolean isSet) {
		if (isSet) {
			this.preferencesFlag |= flag;
		} else {
			this.preferencesFlag &= ~flag;
		}
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	public void setisClassTrackingEnabled(boolean isClassTrackingEnabled) {
		this.isClassTrackingEnabled = isClassTrackingEnabled;
	}

	public boolean isClassTrackingEnabled() {
		return isClassTrackingEnabled;
	}

	public boolean isClassOnePerTransaction() {
		return isClassOnePerTransaction;
	}

	public void setClassOnePerTransaction(boolean isClassOnePerTransaction) {
		this.isClassOnePerTransaction = isClassOnePerTransaction;
	}
}
