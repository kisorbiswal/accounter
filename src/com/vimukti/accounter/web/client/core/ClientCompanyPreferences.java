package com.vimukti.accounter.web.client.core;

import com.vimukti.accounter.web.client.ui.Accounter;

@SuppressWarnings("serial")
public class ClientCompanyPreferences implements IAccounterCore {

	private static int GENERAL_TIME_FORMAT_MINUTES;

	private static int GENERAL_TIME_FORMAT_DECIMAL;

	private static final int SHOW_SUMMARY = 10;

	private static final int SHOW_LIST = 20;

	public static int VAT_REPORTING_PERIOD_MONTHLY = 1;
	public static int VAT_REPORTING_PERIOD_BIMONTHLY = 2;
	public static int VAT_REPORTING_PERIOD_QUARTERLY = 3;
	public static int VAT_REPORTING_PERIOD_ANUALLY = 4;

	public static int VAT_REP_ENDPERIOD_MAR_JUN_SEP_DEC = 1;
	public static int VAT_REP_ENDPERIOD_APR_JUL_OCT_JAN = 2;
	public static int VAT_REP_ENDPERIOD_MAY_AUG_NOV_FEB = 3;

	private static ClientCompanyPreferences preferences;

	boolean useAccountNumbers;

	boolean useClasses;
	private boolean salesOrderEnabled;
	boolean useJobs;

	boolean useChangeLog;

	double logSpaceUsed;

	boolean allowDuplicateDocumentNumbers;

	boolean doYouPaySalesTax;
	public long id;

	boolean isAccuralBasis;// if True then Accural (when customer is Invoiced),
	// if False then Cash Basis(when customer pays
	// Invoice)
	long startOfFiscalYear;

	long endOfFiscalYear;

	long preventPostingBeforeDate;

	long depreciationStartDate;

	long lastDepreciationDate;

	boolean useForeignCurrency;

	boolean isMyAccountantWillrunPayroll;

	// Customer Preferrences

	boolean useCustomerId;

	String defaultShippingTerm;

	int defaultAnnualInterestRate = 0;

	double defaultMinimumFinanceCharge = 0D;

	int graceDays = 0;

	boolean doesCalculateFinanceChargeFromInvoiceDate;

	// If true then finance
	// chagrge will
	// calculate from
	// Invoice date other
	// wise from Due Date.
	private boolean doProductShipMents;
	boolean useVendorId;
	int ageingFromTransactionDateORDueDate = 1;

	boolean useItemNumbers;
	boolean checkForItemQuantityOnHand;
	boolean updateCostAutomatically;

	// VAT Preferences
	boolean enterVATinformationNow = true;
	String VATregistrationNumber;
	int VATreportingPeriod = VAT_REPORTING_PERIOD_QUARTERLY;
	int endingPeriodForVATreporting = VAT_REP_ENDPERIOD_MAR_JUN_SEP_DEC;
	boolean reportVATonAccuralBasis = true;

	private String decimalCharacte = ".";

	// String legalName;

	/**
	 * @return the id
	 */

	/**
	 * @return the useAccountNumbers
	 */
	public boolean getUseAccountNumbers() {
		return useAccountNumbers;
	}

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

	/**
	 * @param useAccountNumbers
	 *            the useAccountNumbers to set
	 */
	public void setUseAccountNumbers(boolean useAccountNumbers) {
		this.useAccountNumbers = useAccountNumbers;
	}

	/**
	 * @return the useClasses
	 */
	public boolean getUseClasses() {
		return useClasses;
	}

	/**
	 * @param useClasses
	 *            the useClasses to set
	 */
	public void setUseClasses(boolean useClasses) {
		this.useClasses = useClasses;
	}

	public String getDefaultShippingTerm() {
		return defaultShippingTerm;
	}

	public void setDefaultShippingTerm(String defaultShippingTerm) {
		this.defaultShippingTerm = defaultShippingTerm;
	}

	public void setStartOfFiscalYear(long startOfFiscalYear) {
		this.startOfFiscalYear = startOfFiscalYear;
	}

	/**
	 * @return the useJobs
	 */
	public boolean getUseJobs() {
		return useJobs;
	}

	/**
	 * @return the endOfFiscalYear
	 */
	public long getEndOfFiscalYear() {
		return endOfFiscalYear;
	}

	/**
	 * @param endOfFiscalYear
	 *            the endOfFiscalYear to set
	 */
	public void setEndOfFiscalYear(long endOfFiscalYear) {
		this.endOfFiscalYear = endOfFiscalYear;
	}

	/**
	 * @return the enterVATinformationNow
	 */
	public boolean isEnterVATinformationNow() {
		return enterVATinformationNow;
	}

	/**
	 * @param enterVATinformationNow
	 *            the enterVATinformationNow to set
	 */
	public void setEnterVATinformationNow(boolean enterVATinformationNow) {
		this.enterVATinformationNow = enterVATinformationNow;
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
		return reportVATonAccuralBasis;
	}

	/**
	 * @param reportVATonAccuralBasis
	 *            the reportVATonAccuralBasis to set
	 */
	public void setReportVATonAccuralBasis(boolean reportVATonAccuralBasis) {
		this.reportVATonAccuralBasis = reportVATonAccuralBasis;
	}

	/**
	 * @param isAccuralBasis
	 *            the isAccuralBasis to set
	 */
	public void setAccuralBasis(boolean isAccuralBasis) {
		this.isAccuralBasis = isAccuralBasis;
	}

	/**
	 * @param isMyAccountantWillrunPayroll
	 *            the isMyAccountantWillrunPayroll to set
	 */
	public void setMyAccountantWillrunPayroll(
			boolean isMyAccountantWillrunPayroll) {
		this.isMyAccountantWillrunPayroll = isMyAccountantWillrunPayroll;
	}

	/**
	 * @param useJobs
	 *            the useJobs to set
	 */
	public void setUseJobs(boolean useJobs) {
		this.useJobs = useJobs;
	}

	/**
	 * @return the useChangeLog
	 */
	public boolean getUseChangeLog() {
		return useChangeLog;
	}

	/**
	 * @param useChangeLog
	 *            the useChangeLog to set
	 */
	public void setUseChangeLog(boolean useChangeLog) {
		this.useChangeLog = useChangeLog;
	}

	/**
	 * @return the logSpaceUsed
	 */
	public double getLogSpaceUsed() {
		return logSpaceUsed;
	}

	/**
	 * @param logSpaceUsed
	 *            the logSpaceUsed to set
	 */
	public void setLogSpaceUsed(double logSpaceUsed) {
		this.logSpaceUsed = logSpaceUsed;
	}

	/**
	 * @return the allowDuplicateDocumentNumbers
	 */
	public boolean getAllowDuplicateDocumentNumbers() {
		return allowDuplicateDocumentNumbers;
	}

	/**
	 * @param allowDuplicateDocumentNumbers
	 *            the allowDuplicateDocumentNumbers to set
	 */
	public void setAllowDuplicateDocumentNumbers(
			boolean allowDuplicateDocumentNumbers) {
		this.allowDuplicateDocumentNumbers = allowDuplicateDocumentNumbers;
	}

	/**
	 * @return the doYouPaySalesTax
	 */
	public boolean getDoYouPaySalesTax() {
		return doYouPaySalesTax;
	}

	/**
	 * @param doYouPaySalesTax
	 *            the doYouPaySalesTax to set
	 */
	public void setDoYouPaySalesTax(boolean doYouPaySalesTax) {
		this.doYouPaySalesTax = doYouPaySalesTax;
	}

	/**
	 * @return the isAccuralBasis
	 */
	public boolean getIsAccuralBasis() {
		return isAccuralBasis;
	}

	/**
	 * @param isAccuralBasis
	 *            the isAccuralBasis to set
	 */
	public void setIsAccuralBasis(boolean isAccuralBasis) {
		this.isAccuralBasis = isAccuralBasis;
	}

	/**
	 * @return the useForeignCurrency
	 */
	public boolean getUseForeignCurrency() {
		return useForeignCurrency;
	}

	/**
	 * @param useForeignCurrency
	 *            the useForeignCurrency to set
	 */
	public void setUseForeignCurrency(boolean useForeignCurrency) {
		this.useForeignCurrency = useForeignCurrency;
	}

	/**
	 * @return the isMyAccountantWillrunPayroll
	 */
	public boolean getIsMyAccountantWillrunPayroll() {
		return isMyAccountantWillrunPayroll;
	}

	/**
	 * @param isMyAccountantWillrunPayroll
	 *            the isMyAccountantWillrunPayroll to set
	 */
	public void setIsMyAccountantWillrunPayroll(
			boolean isMyAccountantWillrunPayroll) {
		this.isMyAccountantWillrunPayroll = isMyAccountantWillrunPayroll;
	}

	/**
	 * @return the useCustomerId
	 */
	public boolean getUseCustomerId() {
		return useCustomerId;
	}

	public long getPreventPostingBeforeDate() {
		return preventPostingBeforeDate;
	}

	public void setPreventPostingBeforeDate(long preventPostingBeforeDate) {
		this.preventPostingBeforeDate = preventPostingBeforeDate;
	}

	/**
	 * @param useCustomerId
	 *            the useCustomerId to set
	 */
	public void setUseCustomerId(boolean useCustomerId) {
		this.useCustomerId = useCustomerId;
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
		return doesCalculateFinanceChargeFromInvoiceDate;
	}

	/**
	 * @param doesCalculateFinanceChargeFromInvoiceDate
	 *            the doesCalculateFinanceChargeFromInvoiceDate to set
	 */
	public void setDoesCalculateFinanceChargeFromInvoiceDate(
			boolean doesCalculateFinanceChargeFromInvoiceDate) {
		this.doesCalculateFinanceChargeFromInvoiceDate = doesCalculateFinanceChargeFromInvoiceDate;
	}

	/**
	 * @return the useVendorId
	 */
	public boolean getUseVendorId() {
		return useVendorId;
	}

	/**
	 * @param useVendorId
	 *            the useVendorId to set
	 */
	public void setUseVendorId(boolean useVendorId) {
		this.useVendorId = useVendorId;
	}

	/**
	 * @return the useItemNumbers
	 */
	public boolean getUseItemNumbers() {
		return useItemNumbers;
	}

	/**
	 * @param useItemNumbers
	 *            the useItemNumbers to set
	 */
	public void setUseItemNumbers(boolean useItemNumbers) {
		this.useItemNumbers = useItemNumbers;
	}

	/**
	 * @return the checkForItemQuantityOnHand
	 */
	public boolean getCheckForItemQuantityOnHand() {
		return checkForItemQuantityOnHand;
	}

	/**
	 * @param checkForItemQuantityOnHand
	 *            the checkForItemQuantityOnHand to set
	 */
	public void setCheckForItemQuantityOnHand(boolean checkForItemQuantityOnHand) {
		this.checkForItemQuantityOnHand = checkForItemQuantityOnHand;
	}

	/**
	 * @return the updateCostAutomatically
	 */
	public boolean getUpdateCostAutomatically() {
		return updateCostAutomatically;
	}

	/**
	 * @param updateCostAutomatically
	 *            the updateCostAutomatically to set
	 */
	public void setUpdateCostAutomatically(boolean updateCostAutomatically) {
		this.updateCostAutomatically = updateCostAutomatically;
	}

	public long getStartOfFiscalYear() {
		return this.startOfFiscalYear;
	}

	public void setEndOfFiscalYear(ClientFinanceDate value) {
		this.endOfFiscalYear = value.getDate();
	}

	public long getDepreciationStartDate() {
		return depreciationStartDate;
	}

	public void setDepreciationStartDate(long depreciationStartDate) {
		this.depreciationStartDate = depreciationStartDate;
	}

	public long getLastDepreciationDate() {
		return lastDepreciationDate;
	}

	public void setLastDepreciationDate(long lastDepreciationDate) {
		this.lastDepreciationDate = lastDepreciationDate;
	}

	public String getDecimalCharacter() {
		return decimalCharacte;

	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.COMPANY_PREFERENCES;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		// this.id=id;

	}

	@Override
	public String getClientClassSimpleName() {
		return "ClientCompanyPreferences";
	}

	public static ClientCompanyPreferences get() {
		if (preferences == null) {
			preferences = Accounter.getCompany().getPreferences();
		}
		return preferences;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isDoProductShipMents() {
		return doProductShipMents;
	}

	/**
	 * 
	 * @param doProductShipMents
	 */
	public void setDoProductShipMents(boolean doProductShipMents) {
		this.doProductShipMents = doProductShipMents;
	}

	public boolean isSalesOrderEnabled() {
		return salesOrderEnabled;
	}

	public void setSalesOrderEnabled(boolean salesOrderEnabled) {
		this.salesOrderEnabled = salesOrderEnabled;
	}
}
