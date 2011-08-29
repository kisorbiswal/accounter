package com.vimukti.accounter.company.initialize;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterServerConstants;
import com.vimukti.accounter.core.BrandingTheme;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.FiscalYear;
import com.vimukti.accounter.core.NominalCodeRange;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.VendorGroup;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.web.client.ui.Accounter;

public class USCompanyInitializer extends CompanyInitializer {

	/**
	 * This is the Account created by default for the purpose of US Sales Tax
	 */
	Account salesTaxPayable;
	// Account prepaidVATaccount;
	// Account ECAcquisitionVATaccount;

	// Account pendingItemReceiptsAccount;
	/**
	 * This is the Account created by default for the purpose of UK when VAT is
	 * Filed
	 */
	// Account VATFiledLiabilityAccount;

	Set<NominalCodeRange> nominalCodeRange = new HashSet<NominalCodeRange>();
	/**
	 * Name of the Company
	 */
	String name;// Trading name

	/**
	 * Creates new Instance
	 */
	public USCompanyInitializer(Company company) {
		super(company);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
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

	public void setNominalCodeRange(Set<NominalCodeRange> nominalCodeRange) {
		this.nominalCodeRange = nominalCodeRange;
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

	public Set<NominalCodeRange> getNominalCodeRange() {
		return nominalCodeRange;
	}

	/**
	 * Initializes all the US default accounts that are useful in the company
	 * 
	 * @param session
	 */
	public void initDefaultUSAccounts() {
		// setDefaultsUSValues();

		createAccount(Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.PENDING_ITEM_RECEIPTS,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		this.salesTaxPayable = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY, Accounter.constants()
						.salesTaxPayable(),
				Account.CASH_FLOW_CATEGORY_OPERATING);

		// Account retainedEarnings = new Account(Account.TYPE_EQUITY, "3100",
		// "Retained Earnings", true, null,
		// Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
		// null, true, true, openingBalancesAccount, "8", true,
		// this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(retainedEarnings);

		// Account cashDiscountGiven = new Account(Account.TYPE_INCOME, "4100",
		// "Income and Distribution", true, null,
		// Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
		// null, true, true, openingBalancesAccount, "9", true,
		// this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(cashDiscountGiven);

		// Account writeOff = new Account(Account.TYPE_INCOME, "4200",
		// "Write off", true, null, Account.CASH_FLOW_CATEGORY_FINANCING,
		// 0.0, false, "", 0.0, null, true, true, openingBalancesAccount,
		// "10", true, this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(writeOff);

		// Account cashDiscountTaken = new Account(
		// Account.TYPE_COST_OF_GOODS_SOLD, "5100", "Cash Discount taken",
		// true, null, Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false,
		// "", 0.0, null, false, true, openingBalancesAccount, "11", true,
		// this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(cashDiscountTaken);

		// Account bankCharge = new Account(Account.TYPE_EXPENSE, "7250",
		// "Bank Charge", true, null,
		// Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
		// null, false, true, openingBalancesAccount, "12", true,
		// this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(bankCharge);

		// The following two accounts for Cash Basis Journal Entries purpose.
		this.otherCashIncomeAccount = createAccount(Account.TYPE_INCOME,
				AccounterServerConstants.OTHER_CASH_INCOME,
				Account.CASH_FLOW_CATEGORY_FINANCING);

		this.otherCashExpenseAccount = createAccount(Account.TYPE_EXPENSE,
				AccounterServerConstants.OTHER_CASH_EXPENSE,
				Account.CASH_FLOW_CATEGORY_FINANCING);

		// this.accountsReceivableAccount = accountsReceivable;
		// this.accountsPayableAccount = accountsPayable;
		// this.openingBalancesAccount = openingBalancesAccount;
		// this.retainedEarningsAccount = retainedEarnings;
		company.setTaxLiabilityAccount(this.salesTaxPayable);
		// this.pendingItemReceiptsAccount = pendingItemReceipts;

		createUSDefaultTaxGroup();
		// createNominalCodesRanges(session);
		// createDefaultBrandingTheme(session);
	}

	private void setDefaultsUSValues() {

		Session session = HibernateUtil.getCurrentSession();
		// Create Default Payment Terms

		// PaymentTerms onePercentTenNetThirty = new PaymentTerms(
		// AccounterConstants.PM_ONE_PERCENT_TEN_NET_THIRTY,
		// AccounterConstants.DISCOUNT_ONEPERCENT_IF_PAID_WITHIN_TENDAYS,
		// 10, 1, PaymentTerms.DUE_NONE, 30, true);
		//
		// session.save(onePercentTenNetThirty);

		// PaymentTerms twoPercentTenNetThirty = new PaymentTerms(
		// AccounterConstants.PM_TWO_PERCENT_TEN_NET_THIRTY,
		// AccounterConstants.DISCOUNT_TWOPERCENT_IF_PAID_WITHIN_TENDAYS,
		// 10, 2, PaymentTerms.DUE_NONE, 30, true);
		// session.save(twoPercentTenNetThirty);

		//
		// PaymentTerms netFifteen = new PaymentTerms(
		// AccounterConstants.PM_NET_FIFTEEN,
		// AccounterConstants.PAY_WITH_IN_FIFTEEN_DAYS, 0, 0,
		// PaymentTerms.DUE_NONE, 15, true);
		//
		// session.save(netFifteen);

		PaymentTerms dueOnReceipt = new PaymentTerms(
				AccounterServerConstants.PM_DUE_ON_RECEIPT,
				AccounterServerConstants.DUE_ON_RECEIPT, 0, 0,
				PaymentTerms.DUE_NONE, 0, true);

		session.save(dueOnReceipt);

		PaymentTerms netThirty = new PaymentTerms(
				AccounterServerConstants.PM_NET_THIRTY,
				AccounterServerConstants.PAY_WITH_IN_THIRTY_DAYS, 0, 0,
				PaymentTerms.DUE_NONE, 30, true);

		session.save(netThirty);

		PaymentTerms netSixty = new PaymentTerms(
				AccounterServerConstants.PM_NET_SIXTY,
				AccounterServerConstants.PAY_WITH_IN_SIXTY_DAYS, 0, 0,
				PaymentTerms.DUE_NONE, 60, true);

		session.save(netSixty);

		PaymentTerms monthly = new PaymentTerms(
				AccounterServerConstants.PM_MONTHLY,
				AccounterServerConstants.SALES_TAX_PAID_MONTHLY, 0, 0,
				PaymentTerms.DUE_CURRENT_MONTH, 30, true);

		session.save(monthly);

		// PaymentTerms quarterly = new PaymentTerms(
		// AccounterConstants.PM_QUARTERLY,
		// AccounterConstants.SALES_TAX_PAID_QUARTERLY, 0, 0,
		// PaymentTerms.DUE_CURRENT_QUARTER, 30, true);
		//
		// session.save(quarterly);
		//
		// PaymentTerms annually = new PaymentTerms(
		// AccounterConstants.PM_ANNUALLY,
		// AccounterConstants.SALES_TAX_PAID_ANNUALLY, 0, 0,
		// PaymentTerms.DUE_CURRENT_YEAR, 30, true);
		//
		// session.save(annually);

		// Current Fiscal Year creation
		FinanceDate currentDate = new FinanceDate();
		FinanceDate fiscalYearStartDate = new FinanceDate(
				(int) currentDate.getYear(), 0, 1);
		FinanceDate fiscalYearEndDate = new FinanceDate(
				(int) currentDate.getYear(), 11, 31);

		String dateFormat = AccounterServerConstants.MMddyyyy;
		FiscalYear fiscalYear = new FiscalYear(fiscalYearStartDate,
				fiscalYearEndDate, FiscalYear.STATUS_OPEN, Boolean.TRUE);

		session.save(fiscalYear);

		// Create Default PayTypes

		// Set Default Preferences
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		CompanyPreferences preferences = new CompanyPreferences();
		try {
			preferences.setUseAccountNumbers(true);
			preferences.setUseClasses(false);
			preferences.setUseJobs(false);
			preferences.setUseChangeLog(false);
			preferences.setAllowDuplicateDocumentNumbers(true);
			preferences.setDoYouPaySalesTax(true);
			preferences.setIsAccuralBasis(true);
			// preferences.setStartOfFiscalYear(format.parse("2009-01-01"));
			// preferences.setEndOfFiscalYear(format.parse("2009-12-31"));

			preferences.setStartOfFiscalYear(fiscalYearStartDate);
			preferences.setEndOfFiscalYear(fiscalYearEndDate);
			preferences.setEnableMultiCurrency(false);
			preferences.setUseCustomerId(false);
			preferences.setDefaultShippingTerm(null);
			preferences.setDefaultAnnualInterestRate(0);
			preferences.setDefaultMinimumFinanceCharge(0D);
			preferences.setGraceDays(3);
			preferences.setDoesCalculateFinanceChargeFromInvoiceDate(true);
			preferences.setUseVendorId(false);
			preferences.setUseItemNumbers(false);
			preferences.setCheckForItemQuantityOnHand(true);
			preferences.setUpdateCostAutomatically(false);
			preferences.setStartDate(fiscalYearStartDate);
			preferences.setPreventPostingBeforeDate(fiscalYearStartDate);
			preferences.setDateFormat(dateFormat);

			FinanceDate depreciationStartDateCal = new FinanceDate();
			depreciationStartDateCal.set(fiscalYearStartDate);
			// depreciationStartDateCal.set(Calendar.DAY_OF_MONTH, 01);
			preferences.setDepreciationStartDate(depreciationStartDateCal);

			this.setPreferences(preferences);

		} catch (Exception e) {

			e.printStackTrace();
		}

		VendorGroup creditCardCompanies = new VendorGroup();
		creditCardCompanies
				.setName(AccounterServerConstants.CREDIT_CARD_COMPANIES);
		creditCardCompanies.setDefault(true);
		session.save(creditCardCompanies);

	}

	private void createNominalCodesRanges(Session session) {

		Set<NominalCodeRange> nominalCodesRangeSet = new HashSet<NominalCodeRange>();

		NominalCodeRange nominalCodeRange1 = new NominalCodeRange();
		nominalCodeRange1
				.setAccountSubBaseType(Account.SUBBASETYPE_FIXED_ASSET);
		nominalCodeRange1.setMinimum(NominalCodeRange.RANGE_FIXED_ASSET_MIN);
		nominalCodeRange1.setMaximum(NominalCodeRange.RANGE_FIXED_ASSET_MAX);
		nominalCodesRangeSet.add(nominalCodeRange1);

		NominalCodeRange nominalCodeRange2 = new NominalCodeRange();
		nominalCodeRange2
				.setAccountSubBaseType(Account.SUBBASETYPE_CURRENT_ASSET);
		nominalCodeRange2
				.setMinimum(NominalCodeRange.RANGE_OTHER_CURRENT_ASSET_MIN);
		nominalCodeRange2
				.setMaximum(NominalCodeRange.RANGE_OTHER_CURRENT_ASSET_MAX);
		nominalCodesRangeSet.add(nominalCodeRange2);

		NominalCodeRange nominalCodeRange3 = new NominalCodeRange();
		nominalCodeRange3
				.setAccountSubBaseType(Account.SUBBASETYPE_CURRENT_LIABILITY);
		nominalCodeRange3
				.setMinimum(NominalCodeRange.RANGE_OTER_CURRENT_LIABILITY_MIN);
		nominalCodeRange3
				.setMaximum(NominalCodeRange.RANGE_OTER_CURRENT_LIABILITY_MAX);
		nominalCodesRangeSet.add(nominalCodeRange3);

		NominalCodeRange nominalCodeRange4 = new NominalCodeRange();
		nominalCodeRange4.setAccountSubBaseType(Account.SUBBASETYPE_EQUITY);
		nominalCodeRange4.setMinimum(NominalCodeRange.RANGE_EQUITY_MIN);
		nominalCodeRange4.setMaximum(NominalCodeRange.RANGE_EQUITY_MAX);
		nominalCodesRangeSet.add(nominalCodeRange4);

		NominalCodeRange nominalCodeRange5 = new NominalCodeRange();
		nominalCodeRange5.setAccountSubBaseType(Account.SUBBASETYPE_INCOME);
		nominalCodeRange5.setMinimum(NominalCodeRange.RANGE_INCOME_MIN);
		nominalCodeRange5.setMaximum(NominalCodeRange.RANGE_INCOME_MAX);
		nominalCodesRangeSet.add(nominalCodeRange5);

		NominalCodeRange nominalCodeRange6 = new NominalCodeRange();
		nominalCodeRange6
				.setAccountSubBaseType(Account.SUBBASETYPE_COST_OF_GOODS_SOLD);
		nominalCodeRange6
				.setMinimum(NominalCodeRange.RANGE_COST_OF_GOODS_SOLD_MIN);
		nominalCodeRange6
				.setMaximum(NominalCodeRange.RANGE_COST_OF_GOODS_SOLD_MAX);
		nominalCodesRangeSet.add(nominalCodeRange6);

		NominalCodeRange nominalCodeRange7 = new NominalCodeRange();
		nominalCodeRange7
				.setAccountSubBaseType(Account.SUBBASETYPE_OTHER_EXPENSE);
		nominalCodeRange7.setMinimum(NominalCodeRange.RANGE_OTHER_EXPENSE_MIN);
		nominalCodeRange7.setMaximum(NominalCodeRange.RANGE_OTHER_EXPENSE_MAX);
		nominalCodesRangeSet.add(nominalCodeRange7);

		NominalCodeRange nominalCodeRange8 = new NominalCodeRange();
		nominalCodeRange8.setAccountSubBaseType(Account.SUBBASETYPE_EXPENSE);
		nominalCodeRange8.setMinimum(NominalCodeRange.RANGE_EXPENSE_MIN);
		nominalCodeRange8.setMaximum(NominalCodeRange.RANGE_EXPENSE_MAX);
		nominalCodesRangeSet.add(nominalCodeRange8);

		NominalCodeRange nominalCodeRange9 = new NominalCodeRange();
		nominalCodeRange9
				.setAccountSubBaseType(Account.SUBBASETYPE_LONG_TERM_LIABILITY);
		nominalCodeRange9
				.setMinimum(NominalCodeRange.RANGE_LONGTERM_LIABILITY_MIN);
		nominalCodeRange9
				.setMaximum(NominalCodeRange.RANGE_LONGTERM_LIABILITY_MAX);
		nominalCodesRangeSet.add(nominalCodeRange9);

		NominalCodeRange nominalCodeRange10 = new NominalCodeRange();
		nominalCodeRange10
				.setAccountSubBaseType(Account.SUBBASETYPE_OTHER_ASSET);
		nominalCodeRange10.setMinimum(NominalCodeRange.RANGE_OTHER_ASSET_MIN);
		nominalCodeRange10.setMaximum(NominalCodeRange.RANGE_OTHER_ASSET_MAX);
		nominalCodesRangeSet.add(nominalCodeRange10);

		this.setNominalCodeRange(nominalCodesRangeSet);

	}

	private void createDefaultBrandingTheme(Session session) {
		BrandingTheme brandingTheme = new BrandingTheme("Standard",
				SecureUtils.createID(), 1.35, 1.00, 1.00, "Times New Roman",
				"10pt", "INVOICE", "CREDIT", "STATEMENT", "democo@democo.co",
				true, this.getName(), "(None Added)");
		session.save(brandingTheme);
	}

	public void createUSDefaultTaxGroup() {

		try {

			Session session = HibernateUtil.getCurrentSession();

			// Default TaxGroup Creation
			TAXAgency defaultTaxAgency = new TAXAgency();
			defaultTaxAgency.setActive(Boolean.TRUE);
			defaultTaxAgency.setName(Accounter.constants().taxAgency());

			defaultTaxAgency.setPaymentTerm((PaymentTerms) session
					.getNamedQuery("unique.name.PaymentTerms")
					.setString(0, Accounter.constants().netMonthly()).list()
					.get(0));
			defaultTaxAgency.setSalesLiabilityAccount((Account) session
					.getNamedQuery("unique.name.Account")
					.setString(0, Accounter.constants().salesTaxPayable())
					.list().get(0));
			defaultTaxAgency.setDefault(true);
			session.save(defaultTaxAgency);

			// Set<TaxRates> taxRates = new HashSet<TaxRates>();
			//
			// TaxRates taxRate = new TaxRates();
			// taxRate.setRate(0);
			// taxRate.setAsOf(new FinanceDate());
			// taxRate.setID(SecureUtils.createID());
			// taxRates.add(taxRate);

			TAXItem defaultTaxItem = new TAXItem();
			defaultTaxItem.setActive(Boolean.TRUE);
			defaultTaxItem.setName(Accounter.constants().none());
			defaultTaxItem.setTaxAgency(defaultTaxAgency);
			defaultTaxItem.setVatReturnBox(null);
			defaultTaxItem.setTaxRate(0);
			defaultTaxItem.setSalesType(Boolean.TRUE);
			defaultTaxItem.setDefault(true);
			session.save(defaultTaxItem);

			// TAXCode defaultTaxCodeforTaxItem = new TAXCode(
			// (TAXItemGroup) defaultTaxItem);
			// session.save(defaultTaxCodeforTaxItem);

			// TAXGroup defaultTaxGroup = new TAXGroup();
			// defaultTaxGroup.setName("Tax Group");
			// defaultTaxGroup.setID(SecureUtils.createID());
			// defaultTaxGroup.setActive(Boolean.TRUE);
			// defaultTaxGroup.setSalesType(true);
			//
			// List<TAXItem> taxItems = new ArrayList<TAXItem>();
			// taxItems.add(defaultTaxItem);
			// defaultTaxGroup.setTAXItems(taxItems);
			// defaultTaxGroup.setGroupRate(0);
			// defaultTaxGroup.setDefault(true);
			// session.save(defaultTaxGroup);
			// TAXCode defaultTaxCodeforTaxGroup = new TAXCode((TAXItemGroup)
			// defaultTaxGroup);
			// session.save(defaultTaxCodeforTaxGroup);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void init() {
		// super.init();
		initDefaultUSAccounts();
	}

	/*
	 * @Override public void office_expense() { stub
	 * 
	 * }
	 * 
	 * @Override public void motor_veichel_expense() { // TODO Auto-generated
	 * method stub
	 * 
	 * }
	 * 
	 * @Override public void travel_expenses() { stub
	 * 
	 * }
	 * 
	 * @Override public void other_expenses() { stub
	 * 
	 * }
	 * 
	 * @Override public void Cost_of_good_sold() { stub
	 * 
	 * }
	 * 
	 * @Override public void other_direct_cost() { stub
	 * 
	 * }
	 */

	@Override
	String getDateFormat() {
		return AccounterServerConstants.MMddyyyy;
	}

}
