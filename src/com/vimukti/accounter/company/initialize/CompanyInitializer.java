package com.vimukti.accounter.company.initialize;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterConstants;
import com.vimukti.accounter.core.BrandingTheme;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.FiscalYear;
import com.vimukti.accounter.core.NominalCodeRange;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.VendorGroup;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;

public abstract class CompanyInitializer {

	protected Company company;

	// protected Account expense;
	//
	// protected Account income;
	//
	// protected Account liability;
	//
	// protected Account assets;

	/**
	 * Each company have it's own preferences. This will hold all the
	 * preferences related to the company.
	 * 
	 * @see Company
	 */
	protected CompanyPreferences preferences = new CompanyPreferences();

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
	 * Creates new Instance
	 */
	public CompanyInitializer(Company company) {
		this.company = company;
	}

	/**
	 * this is used to get the company type
	 */
	public void init() {
		Session session = HibernateUtil.getCurrentSession();

		intializeCompanyValues();

		openingBalancesAccount = new Account(Account.TYPE_EQUITY, "3040",
				AccounterConstants.OPENING_BALANCE, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, true, null, "4", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(openingBalancesAccount);

		initializeDefaultAssetsAccounts();
		initializeDefaultIncomeAccounts();
		initializeDefaultExpenseAccounts();
		initializeDefaultlLiabilitiesAccounts();
		initializeDefaultEquityAccounts();
	}

	private void intializeCompanyValues() {
		Session session = HibernateUtil.getCurrentSession();

		FinanceDate currentDate = new FinanceDate();
		FinanceDate fiscalYearStartDate = new FinanceDate(
				(int) currentDate.getYear(), 0, 1);
		FinanceDate fiscalYearEndDate = new FinanceDate(
				(int) currentDate.getYear(), 11, 31);

		FiscalYear fiscalYear = new FiscalYear(fiscalYearStartDate,
				fiscalYearEndDate, FiscalYear.STATUS_OPEN, Boolean.TRUE);

		session.save(fiscalYear);

		this.preferences.setUseAccountNumbers(true);
		this.preferences.setUseClasses(false);
		this.preferences.setUseJobs(false);
		this.preferences.setUseChangeLog(false);
		this.preferences.setAllowDuplicateDocumentNumbers(true);
		this.preferences.setDoYouPaySalesTax(false);
		this.preferences.setIsAccuralBasis(true);
		this.preferences.setStartOfFiscalYear(fiscalYearStartDate);
		this.preferences.setEndOfFiscalYear(fiscalYearEndDate);
		this.preferences.setEnableMultiCurrency(false);
		this.preferences.setUseCustomerId(false);
		this.preferences.setDefaultShippingTerm(null);
		this.preferences.setDefaultAnnualInterestRate(0);
		this.preferences.setDefaultMinimumFinanceCharge(0D);
		this.preferences.setGraceDays(3);
		this.preferences.setDoesCalculateFinanceChargeFromInvoiceDate(true);
		this.preferences.setUseVendorId(false);
		this.preferences.setUseItemNumbers(false);
		this.preferences.setCheckForItemQuantityOnHand(true);
		this.preferences.setUpdateCostAutomatically(false);
		this.preferences.setStartDate(fiscalYearStartDate);
		this.preferences.setPreventPostingBeforeDate(fiscalYearStartDate);
		this.preferences.setDateFormat(getDateFormat());
		FinanceDate depreciationStartDateCal = new FinanceDate();
		depreciationStartDateCal.set(fiscalYearStartDate);
		this.preferences.setDepreciationStartDate(depreciationStartDateCal);
		this.company.setPreferences(this.preferences);

		PaymentTerms dueOnReceipt = new PaymentTerms(
				AccounterConstants.PM_DUE_ON_RECEIPT,
				AccounterConstants.DUE_ON_RECEIPT, 0, 0, PaymentTerms.DUE_NONE,
				0, true);

		session.save(dueOnReceipt);

		PaymentTerms netThirty = new PaymentTerms(
				AccounterConstants.PM_NET_THIRTY,
				AccounterConstants.PAY_WITH_IN_THIRTY_DAYS, 0, 0,
				PaymentTerms.DUE_NONE, 30, true);

		session.save(netThirty);

		PaymentTerms netSixty = new PaymentTerms(
				AccounterConstants.PM_NET_SIXTY,
				AccounterConstants.PAY_WITH_IN_SIXTY_DAYS, 0, 0,
				PaymentTerms.DUE_NONE, 60, true);

		session.save(netSixty);

		PaymentTerms monthlyPayrollLiability = new PaymentTerms(
				AccounterConstants.PM_MONTHLY,
				AccounterConstants.PM_MONTHLY_PAYROLL_LIABILITY, 0, 0,
				PaymentTerms.DUE_PAYROLL_TAX_MONTH, 13, true);

		session.save(monthlyPayrollLiability);

		VendorGroup creditCardCompanies = new VendorGroup();
		creditCardCompanies.setName(AccounterConstants.CREDIT_CARD_COMPANIES);
		creditCardCompanies.setDefault(true);
		session.save(creditCardCompanies);

		BrandingTheme brandingTheme = new BrandingTheme("Standard",
				SecureUtils.createID(), 1.35, 1.00, 1.00, "Times New Roman",
				"10pt", "INVOICE", "CREDIT", "STATEMENT", "(None Added)", true,
				"(None Added)", "(None Added)");
		session.save(brandingTheme);

		createNominalCodesRanges();

	}

	private void initializeDefaultEquityAccounts() {
		Session session = HibernateUtil.getCurrentSession();

		Account ordinaryShares = new Account(Account.TYPE_EQUITY, "3001",
				AccounterConstants.ORDINARY_SHARES, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "147", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(ordinaryShares);

		Account reservesRetainedEarnings = new Account(Account.TYPE_EQUITY,
				"3050", AccounterConstants.RESERVES_RETAINED_EARNINGS, true,
				null, Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "148", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(reservesRetainedEarnings);

		Account PAndLBoughtForwardOrYTD = new Account(Account.TYPE_EQUITY,
				"3100", AccounterConstants.P_AND_L_BOUGHT_FORWARD_OR_YTD, true,
				null, Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "149", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(PAndLBoughtForwardOrYTD);

		Account dividends = new Account(Account.TYPE_EQUITY, "3150",
				AccounterConstants.DIVIDENDS, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "150", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(dividends);

		this.retainedEarningsAccount = reservesRetainedEarnings;
	}

	public abstract Company getCompany();

	/**
	 * initialize the default expenses accounts
	 */

	public void initializeDefaultExpenseAccounts() {

		Session session = HibernateUtil.getCurrentSession();
		Account productsOrMaterialsPurchasedTypeA = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD, "5001",
				AccounterConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_A,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "20", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(productsOrMaterialsPurchasedTypeA);

		Account productsOrMaterialsPurchasedTypeB = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD, "5002",
				AccounterConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_B,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "21", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(productsOrMaterialsPurchasedTypeB);

		Account productsOrMaterialsPurchasedTypeC = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD, "5003",
				AccounterConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_C,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "3", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(productsOrMaterialsPurchasedTypeC);

		Account productsOrMaterialsPurchasedTypeD = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD, "5004",
				AccounterConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_D,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "22", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(productsOrMaterialsPurchasedTypeD);

		Account productsOrMaterialsPurchasedTypeE = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD, "5005",
				AccounterConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_E,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "23", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(productsOrMaterialsPurchasedTypeE);

		Account carriage = new Account(Account.TYPE_COST_OF_GOODS_SOLD, "5200",
				AccounterConstants.CARRIAGE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "28", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(carriage);

		Account discountsTaken = new Account(Account.TYPE_COST_OF_GOODS_SOLD,
				"5210", AccounterConstants.DISCOUNTS_TAKEN, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "29", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(discountsTaken);

		Account importDuty = new Account(Account.TYPE_COST_OF_GOODS_SOLD,
				"5220", AccounterConstants.IMPORT_DUTY, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "30", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(importDuty);

		Account openingStock = new Account(Account.TYPE_COST_OF_GOODS_SOLD,
				"5900", AccounterConstants.OPENING_STOCK, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "24", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(openingStock);

		// Account closingStock = new Account(Account.TYPE_COST_OF_GOODS_SOLD,
		// "5910", AccounterConstants.CLOSING_STOCK, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
		// openingBalancesAccount, "25", true, this.preferences
		// .getPreventPostingBeforeDate());
		//
		// session.save(closingStock);

		Account openingFinishedGoods = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD, "5920",
				AccounterConstants.OPEN_FINISHED_GOODS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "26", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(openingFinishedGoods);

		// Account closingFinishedGoods = new Account(
		// Account.TYPE_COST_OF_GOODS_SOLD, "5925",
		// AccounterConstants.CLOSE_FINISHED_GOODS, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
		// openingBalancesAccount, "151", true, this.preferences
		// .getPreventPostingBeforeDate());
		//
		// session.save(closingFinishedGoods);

		Account openingWorkInProgress = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD, "5930",
				AccounterConstants.OPEN_WORK_IN_PROGRESS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "27", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(openingWorkInProgress);
		//
		// Account closingWorkInProgress = new Account(
		// Account.TYPE_COST_OF_GOODS_SOLD, "5935",
		// AccounterConstants.CLOSE_WORK_IN_PROGRESS, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
		// openingBalancesAccount, "152", true, this.preferences
		// .getPreventPostingBeforeDate());
		//
		// session.save(closingWorkInProgress);

		/**
		 * The following are the Default Accounts for the Type of Other Direct
		 * Costs.
		 */

		Account directLabour = new Account(Account.TYPE_OTHER_EXPENSE, "6001",
				AccounterConstants.DIRECT_LABOUR, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "31", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(directLabour);

		Account directEmployersNI = new Account(Account.TYPE_OTHER_EXPENSE,
				"6010", AccounterConstants.DIRECT_EMPLOYERS_NI, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "32", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(directEmployersNI);

		Account otherDirectEmployeeRelatedCosts = new Account(
				Account.TYPE_OTHER_EXPENSE, "6020",
				AccounterConstants.OTHER_DIRECT_EMPLOYEE_RELATED_COSTS, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "33", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(otherDirectEmployeeRelatedCosts);

		Account directExpenses = new Account(Account.TYPE_OTHER_EXPENSE,
				"6100", AccounterConstants.DIRECT_EXPENSES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "34", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(directExpenses);

		Account directTravel = new Account(Account.TYPE_OTHER_EXPENSE, "6150",
				AccounterConstants.DIRECT_TRAVEL, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "35", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(directTravel);

		Account directConsumables = new Account(Account.TYPE_OTHER_EXPENSE,
				"6200", AccounterConstants.DIRECT_CONSUMABLES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "36", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(directConsumables);

		Account merchantAccountFees = new Account(Account.TYPE_OTHER_EXPENSE,
				"6300", AccounterConstants.MERCHANT_ACCOUNT_FEES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "37", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(merchantAccountFees);

		Account commisionsPaid = new Account(Account.TYPE_OTHER_EXPENSE,
				"6310", AccounterConstants.COMMISSIONS_PAID, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "38", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(commisionsPaid);

		Account indirectLabour = new Account(Account.TYPE_EXPENSE, "7001",
				AccounterConstants.INDIRECT_LABOUR, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "39", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(indirectLabour);

		Account indirectEmployersNI = new Account(Account.TYPE_EXPENSE, "7002",
				AccounterConstants.INDIRECT_EMPLOYERS_NI, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "40", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(indirectEmployersNI);

		Account directorsRemunaration = new Account(Account.TYPE_EXPENSE,
				"7003", AccounterConstants.DIRECTORS_REMUNERATION, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "41", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(directorsRemunaration);

		Account casualLabour = new Account(Account.TYPE_EXPENSE, "7004",
				AccounterConstants.CASUAL_LABOUR, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "42", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(casualLabour);

		Account employersPensionContributions = new Account(
				Account.TYPE_EXPENSE, "7010",
				AccounterConstants.EMPLOYERS_PANSION_CONTRIBUTIONS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "43", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(employersPensionContributions);

		Account employeeBenifits = new Account(Account.TYPE_EXPENSE, "7025",
				AccounterConstants.EMPLOYEE_BENIFITS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "46", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(employeeBenifits);

		Account medicalInsurance = new Account(Account.TYPE_EXPENSE, "7030",
				AccounterConstants.MEDICAL_INSURANCE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "47", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(medicalInsurance);

		Account recruitement = new Account(Account.TYPE_EXPENSE, "7040",
				AccounterConstants.RECRUITMENT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "48", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(recruitement);

		Account training = new Account(Account.TYPE_EXPENSE, "7045",
				AccounterConstants.TRAINING, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "49", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(training);

		Account rent = new Account(Account.TYPE_EXPENSE, "7100",
				AccounterConstants.RENT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "50", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(rent);

		Account generalRates = new Account(Account.TYPE_EXPENSE, "7101",
				AccounterConstants.GENERAL_RATES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "51", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(generalRates);

		Account waterRates = new Account(Account.TYPE_EXPENSE, "7102",
				AccounterConstants.WATER_RATES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "52", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(waterRates);

		Account electricity = new Account(Account.TYPE_EXPENSE, "7110",
				AccounterConstants.ELECTRICITY, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "53", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(electricity);

		Account gas = new Account(Account.TYPE_EXPENSE, "7111",
				AccounterConstants.GAS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "54", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(gas);

		Account oil = new Account(Account.TYPE_EXPENSE, "7112",
				AccounterConstants.OIL, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "55", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(oil);

		Account officeCleaning = new Account(Account.TYPE_EXPENSE, "7120",
				AccounterConstants.OFFICE_CLEANING, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "56", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(officeCleaning);

		Account officeMachineMaintanance = new Account(Account.TYPE_EXPENSE,
				"7130", AccounterConstants.OFFICE_MACHINE_MAINTENANCE, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "57", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(officeMachineMaintanance);

		Account repairsAndRenewals = new Account(Account.TYPE_EXPENSE, "7140",
				AccounterConstants.REPAIR_RENEWALS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "58", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(repairsAndRenewals);

		Account officeConsumables = new Account(Account.TYPE_EXPENSE, "7150",
				AccounterConstants.OFFICE_CONSUMABLES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "59", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(officeConsumables);

		Account booksEtc = new Account(Account.TYPE_EXPENSE, "7151",
				AccounterConstants.BOOKS_ETC, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "60", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(booksEtc);

		Account internet = new Account(Account.TYPE_EXPENSE, "7152",
				AccounterConstants.INTERNET, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "61", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(internet);

		Account postage = new Account(Account.TYPE_EXPENSE, "7153",
				AccounterConstants.POSTAGE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "62", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(postage);

		Account printing = new Account(Account.TYPE_EXPENSE, "7154",
				AccounterConstants.PRINTING, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "63", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(printing);

		Account stationary = new Account(Account.TYPE_EXPENSE, "7155",
				AccounterConstants.STATIONERY, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "64", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(stationary);

		Account subscriptions = new Account(Account.TYPE_EXPENSE, "7156",
				AccounterConstants.SUBSCRIPTIONS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "65", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(subscriptions);

		Account telephone = new Account(Account.TYPE_EXPENSE, "7157",
				AccounterConstants.TELEPHONE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "66", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(telephone);

		Account conferenceAndSeminars = new Account(Account.TYPE_EXPENSE,
				"7158", AccounterConstants.CONFERENCES_AND_SEMINARS, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "67", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(conferenceAndSeminars);

		Account charityDonations = new Account(Account.TYPE_EXPENSE, "7160",
				AccounterConstants.CHARITY_DONATIONS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "68", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(charityDonations);

		Account insurencesBusiness = new Account(Account.TYPE_EXPENSE, "7200",
				AccounterConstants.INSURANCES_BUSINESS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "69", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(insurencesBusiness);

		Account advertisiningAndMarketing = new Account(Account.TYPE_EXPENSE,
				"7250", AccounterConstants.ADVERTISING_AND_MARKETING, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "70", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(advertisiningAndMarketing);

		Account localEntertainment = new Account(Account.TYPE_EXPENSE, "7260",
				AccounterConstants.LOCAL_ENTERTAINMENT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "71", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(localEntertainment);

		Account overseasEntertainment = new Account(Account.TYPE_EXPENSE,
				"7261", AccounterConstants.OVERSEAS_ENTERTAINMENT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "72", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(overseasEntertainment);

		Account indirectLocalTravel = new Account(Account.TYPE_EXPENSE, "7270",
				AccounterConstants.INDIRECT_LOCAL_TRAVEL, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "73", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(indirectLocalTravel);

		Account indirectOverseasTravel = new Account(Account.TYPE_EXPENSE,
				"7271", AccounterConstants.INDIRECT_OVERSEAS_TRAVEL, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "74", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(indirectOverseasTravel);

		Account subsitence = new Account(Account.TYPE_EXPENSE, "7280",
				AccounterConstants.SUBSISTENCE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "75", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(subsitence);

		Account vechileExpenses = new Account(Account.TYPE_EXPENSE, "7300",
				AccounterConstants.VECHILE_EXPENSES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "76", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(vechileExpenses);

		Account vechileInsurance = new Account(Account.TYPE_EXPENSE, "7310",
				AccounterConstants.VECHILE_INSURANCE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "77", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(vechileInsurance);

		Account vechileRepairAndServicing = new Account(Account.TYPE_EXPENSE,
				"7320", AccounterConstants.VECHILE_REPAIRS_AND_SERVICING, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "78", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(vechileRepairAndServicing);

		Account professonalFees = new Account(Account.TYPE_EXPENSE, "7350",
				AccounterConstants.PROFESSIONAL_FEES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "79", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(professonalFees);

		Account accountancyFees = new Account(Account.TYPE_EXPENSE, "7360",
				AccounterConstants.ACCOUNTANCY_FEES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "80", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(accountancyFees);

		Account consultancyFees = new Account(Account.TYPE_EXPENSE, "7370",
				AccounterConstants.CONSULTANY_FEES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "81", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(consultancyFees);

		Account legalFees = new Account(Account.TYPE_EXPENSE, "7380",
				AccounterConstants.LEGAL_FEES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "82", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(legalFees);

		Account bankInterestPaid = new Account(Account.TYPE_EXPENSE, "7400",
				AccounterConstants.BANK_INTEREST_PAID, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "83", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(bankInterestPaid);

		Account bankCharges = new Account(Account.TYPE_EXPENSE, "7410",
				AccounterConstants.BANK_CHARGES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "84", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(bankCharges);

		Account creditCharges = new Account(Account.TYPE_EXPENSE, "7420",
				AccounterConstants.CREDIT_CHARGES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "85", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(creditCharges);

		Account leasePayments = new Account(Account.TYPE_EXPENSE, "7430",
				AccounterConstants.LEASE_PAYMENTS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "86", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(leasePayments);

		Account loanInterestPaid = new Account(Account.TYPE_EXPENSE, "7440",
				AccounterConstants.LOAN_INTEREST_PAID, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "87", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(loanInterestPaid);

		Account currencyCharges = new Account(Account.TYPE_EXPENSE, "7450",
				AccounterConstants.CURRENCY_CHARGES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "88", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(currencyCharges);

		Account exchangeRateVariance = new Account(Account.TYPE_EXPENSE,
				"7460", AccounterConstants.EXCHANGE_RATE_VARIANCE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "89", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(exchangeRateVariance);

		Account badDebtProvision = new Account(Account.TYPE_EXPENSE, "7470",
				AccounterConstants.BAD_DEBT_PROVISION, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "90", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(badDebtProvision);

		Account badDebtWriteOff = new Account(Account.TYPE_EXPENSE, "7480",
				AccounterConstants.BAD_DEBT_WRITE_OFF, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "91", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(badDebtWriteOff);

		Account depreciation = new Account(Account.TYPE_EXPENSE, "7500",
				AccounterConstants.DEPRECIATION, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "92", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(depreciation);

		Account officeEquipmentDepreciation = new Account(Account.TYPE_EXPENSE,
				"7510", AccounterConstants.OFFICE_EQUIPMENT_DEPRECIATION, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "93", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(officeEquipmentDepreciation);

		Account itEquipmentDepreciation = new Account(Account.TYPE_EXPENSE,
				"7520", AccounterConstants.IT_EQUIPMENT_DEPRECIATION, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "94", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(itEquipmentDepreciation);

		Account furnituresAndFixuresDepreciation = new Account(
				Account.TYPE_EXPENSE, "7530",
				AccounterConstants.FURNITURE_AND_FIXTURES_DEPRECIARION, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "95", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(furnituresAndFixuresDepreciation);

		Account palntOrMachineryDepreciation = new Account(
				Account.TYPE_EXPENSE, "7540",
				AccounterConstants.PLANT_OR_MACHINERY_DEPRECIATION, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "96", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(palntOrMachineryDepreciation);

		Account vechileDepreciation = new Account(Account.TYPE_EXPENSE, "7550",
				AccounterConstants.VECHILE_DEPRECIATION, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "97", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(vechileDepreciation);

		Account freeHoldBuildingDepreciation = new Account(
				Account.TYPE_EXPENSE, "7560",
				AccounterConstants.FREEHOLD_BUILDING_DEPRECIATION, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "98", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(freeHoldBuildingDepreciation);

		Account leaseHoldPropertyImprvmntsDepreciation = new Account(
				Account.TYPE_EXPENSE,
				"7570",
				AccounterConstants.LEASEHOLD_PROPERTY_IMPROVEMENTS_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "99", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(leaseHoldPropertyImprvmntsDepreciation);
	}

	/**
	 * initialize the default income accounts
	 */

	public void initializeDefaultIncomeAccounts() {

		Session session = HibernateUtil.getCurrentSession();

		Account salesIncomeTypeA = new Account(Account.TYPE_INCOME, "4001",
				AccounterConstants.SALES_INCOME_TYPE_A, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "5", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(salesIncomeTypeA);

		Account salesIncomeTypeB = new Account(Account.TYPE_INCOME, "4002",
				AccounterConstants.SALES_INCOME_TYPE_B, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "6", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(salesIncomeTypeB);

		Account salesIncomeTypeC = new Account(Account.TYPE_INCOME, "4003",
				AccounterConstants.SALES_INCOME_TYPE_C, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "7", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(salesIncomeTypeC);

		Account salesIncomeTypeD = new Account(Account.TYPE_INCOME, "4004",
				AccounterConstants.SALES_INCOME_TYPE_D, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "8", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(salesIncomeTypeD);

		Account salesIncomeTypeE = new Account(Account.TYPE_INCOME, "4005",
				AccounterConstants.SALES_INCOME_TYPE_E, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "9", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(salesIncomeTypeE);

		Account miscellaneousIncome = new Account(Account.TYPE_INCOME, "4100",
				AccounterConstants.MISCELLANEOUS_INCOME, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "10", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(miscellaneousIncome);

		Account distributionAndCarriage = new Account(Account.TYPE_INCOME,
				"4110", AccounterConstants.DISTRIBUTION_AND_CARRIAGE, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "11", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(distributionAndCarriage);

		Account discounts = new Account(Account.TYPE_INCOME, "4120",
				AccounterConstants.DISCOUNTS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "12", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(discounts);

		Account commissionsReceived = new Account(Account.TYPE_INCOME, "4200",
				AccounterConstants.COMMISSION_RECIEVED, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "13", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(commissionsReceived);

		Account creditChargesLatePayment = new Account(Account.TYPE_INCOME,
				"4210", AccounterConstants.CREDIT_CHARGES_LATEPAYMENT, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "14", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(creditChargesLatePayment);

		Account insuranceClaims = new Account(Account.TYPE_INCOME, "4220",
				AccounterConstants.INSURANCE_CLAIMS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "15", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(insuranceClaims);

		Account interestIncome = new Account(Account.TYPE_INCOME, "4230",
				AccounterConstants.INTEREST_INCOME, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "16", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(interestIncome);

		Account rentIncome = new Account(Account.TYPE_INCOME, "4240",
				AccounterConstants.RENT_INCOME, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "17", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(rentIncome);

		Account royltiesReceived = new Account(Account.TYPE_INCOME, "4250",
				AccounterConstants.ROYALTIES_RECIEVED, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "18", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(royltiesReceived);

		Account profitOrLossOnSalesOfAssets = new Account(Account.TYPE_INCOME,
				"4260", AccounterConstants.PROFIT_OR_LOSS_ON_SALES_ASSETS,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "19", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(profitOrLossOnSalesOfAssets);
	}

	/**
	 * initialize the default assets accounts
	 */

	public void initializeDefaultAssetsAccounts() {
		Session session = HibernateUtil.getCurrentSession();

		accountsReceivableAccount = new Account(
				Account.TYPE_OTHER_CURRENT_ASSET, "1001",
				AccounterConstants.ACCOUNTS_RECEIVABLE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, false, false, openingBalancesAccount, "2", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(accountsReceivableAccount);

		accountsPayableAccount = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2001",
				AccounterConstants.ACCOUNTS_PAYABLE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "3", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(accountsPayableAccount);

		Account unDepositedFunds = new Account(
				Account.TYPE_OTHER_CURRENT_ASSET, "1175",
				AccounterConstants.UN_DEPOSITED_FUNDS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, false, true, openingBalancesAccount, "1", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(unDepositedFunds);

		Account deposits = new Account(Account.TYPE_OTHER_CURRENT_ASSET,
				"1003", AccounterConstants.DEPOSITS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "100", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(deposits);

		Account bankCurrentAccount = new Account(
				Account.TYPE_OTHER_CURRENT_ASSET, "1100",
				AccounterConstants.BANK_CURRENT_ACCOUNT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "103", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(bankCurrentAccount);

		Account bankDepositAccount = new Account(
				Account.TYPE_OTHER_CURRENT_ASSET, "1150",
				AccounterConstants.BANK_DEPOSIT_ACCOUNT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "104", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(bankDepositAccount);

		Account pettyCash = new Account(Account.TYPE_OTHER_CURRENT_ASSET,
				"1180", AccounterConstants.PETTY_CASH, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "105", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(pettyCash);

		Account prepayments = new Account(Account.TYPE_OTHER_CURRENT_ASSET,
				"1185", AccounterConstants.PRE_PAYMENTS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "106", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(prepayments);

		Account advancesToEmployees = new Account(
				Account.TYPE_OTHER_CURRENT_ASSET, "1190",
				AccounterConstants.ADVANCES_TO_EMPLOYEES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "107", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(advancesToEmployees);

		Account stock = new Account(Account.TYPE_OTHER_CURRENT_ASSET, "1200",
				AccounterConstants.STOCK, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "108", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(stock);

		Account freeHoldBuildings = new Account(Account.TYPE_FIXED_ASSET,
				"0001", AccounterConstants.FREEHOLD_BUILDINGS, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "122", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(freeHoldBuildings);

		Account acumulatedFreeHoldBuildingDepreciation = new Account(
				Account.TYPE_FIXED_ASSET, "0040",
				AccounterConstants.ACCUMULATED_FREEHOLD_BUILDING_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "123",
				true, this.preferences.getPreventPostingBeforeDate());

		session.save(acumulatedFreeHoldBuildingDepreciation);

		Account leaseHoldPropertyImprovements = new Account(
				Account.TYPE_FIXED_ASSET, "0050",
				AccounterConstants.LEASEHOLD_PROPERTY_IMPROVEMENTS, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "124", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(leaseHoldPropertyImprovements);

		Account accmltdLeaseHoldPropertyImprvmntsDepreciation = new Account(
				Account.TYPE_FIXED_ASSET,
				"0090",
				AccounterConstants.ACCUMULATED_LEASE_HOLD_PROPERTY_IMPROVEMENTS_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "125",
				true, this.preferences.getPreventPostingBeforeDate());

		session.save(accmltdLeaseHoldPropertyImprvmntsDepreciation);

		Account officeEquipment = new Account(Account.TYPE_FIXED_ASSET, "0100",
				AccounterConstants.OFFICE_EQUIPMENT, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "126", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(officeEquipment);

		Account acumltdOfcEqupmntDepreciation = new Account(
				Account.TYPE_FIXED_ASSET, "0115",
				AccounterConstants.ACCUMULATED_OFFICE_EQUIPMENT_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "127",
				true, this.preferences.getPreventPostingBeforeDate());

		session.save(acumltdOfcEqupmntDepreciation);

		Account itEquipment = new Account(Account.TYPE_FIXED_ASSET, "0120",
				AccounterConstants.IT_EQUIPMENT, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "128", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(itEquipment);

		Account accumltdITEquipmentDepreciation = new Account(
				Account.TYPE_FIXED_ASSET, "0135",
				AccounterConstants.ACCUMULATED_IT_EQUIPMENT_DEPRECIATION, true,
				null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "129", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(accumltdITEquipmentDepreciation);

		Account furnitureAndFixtures = new Account(Account.TYPE_FIXED_ASSET,
				"0140", AccounterConstants.FURNITURE_AND_FIXTURES, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "130", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(furnitureAndFixtures);

		Account accumltdFurnitureAndFixturesDepreciation = new Account(
				Account.TYPE_FIXED_ASSET,
				"0155",
				AccounterConstants.ACCUMULATED_FURNITURE_AND_FIXTURES_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "131",
				true, this.preferences.getPreventPostingBeforeDate());

		session.save(accumltdFurnitureAndFixturesDepreciation);

		Account plantAndMachinary = new Account(Account.TYPE_FIXED_ASSET,
				"0160", AccounterConstants.PLANT_AND_MACHINERY, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "132", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(plantAndMachinary);

		Account accumltdPlantAndMachinaryDepreciation = new Account(
				Account.TYPE_FIXED_ASSET,
				"0175",
				AccounterConstants.ACCUMULATED_PLANT_AND_MACHINERY_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "133",
				true, this.preferences.getPreventPostingBeforeDate());

		session.save(accumltdPlantAndMachinaryDepreciation);

		Account vechiles = new Account(Account.TYPE_FIXED_ASSET, "0180",
				AccounterConstants.VECHICLES, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "134", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(vechiles);

		Account accumltdVechilesDepreciation = new Account(
				Account.TYPE_FIXED_ASSET, "0195",
				AccounterConstants.ACCUMULATED_VEHICLES_DEPRECIATION, true,
				null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "135", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(accumltdVechilesDepreciation);

		Account intangibles = new Account(Account.TYPE_FIXED_ASSET, "0200",
				AccounterConstants.INTANGIBLES, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "136", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(intangibles);

		Account bankRevolutions = new Account(Account.TYPE_OTHER_ASSET, "9501",
				AccounterConstants.BANK_REVALUATIONS, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "140", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(bankRevolutions);

		Account historicalAdjustment = new Account(Account.TYPE_OTHER_ASSET,
				"9510", AccounterConstants.HISTORICAL_ADJUSTMENT, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "141", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(historicalAdjustment);

		Account realisedCurrencyGains = new Account(Account.TYPE_OTHER_ASSET,
				"9520", AccounterConstants.REALISED_CURRENCY_GAINS, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "142", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(realisedCurrencyGains);

		Account unrealisedCurrencyGains = new Account(Account.TYPE_OTHER_ASSET,
				"9530", AccounterConstants.UNREALISED_CURRENCY_GAINS, true,
				null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "143", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(unrealisedCurrencyGains);

		Account rounding = new Account(Account.TYPE_OTHER_ASSET, "9540",
				AccounterConstants.ROUNDING, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "144", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(rounding);

		Account suspense = new Account(Account.TYPE_OTHER_ASSET, "9600",
				AccounterConstants.SUSPENSE, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "146", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(suspense);

	}

	/**
	 * initialize the default liabilities accounts
	 */

	public void initializeDefaultlLiabilitiesAccounts() {
		Session session = HibernateUtil.getCurrentSession();
		Account creditCards = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2050", AccounterConstants.CREDIT_CARDS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "110", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(creditCards);

		Account payeeEmploymentTax = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2100",
				AccounterConstants.PAYEE_EMPLOYEMENT_TAX, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "111", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(payeeEmploymentTax);

		Account nationalInsuranceTax = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2110",
				AccounterConstants.NATIONAL_INSURANCE_TAX, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "112", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(nationalInsuranceTax);

		Account corporationTax = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2130",
				AccounterConstants.CORPORATION_RAX, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "114", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(corporationTax);

		Account loans = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2200", AccounterConstants.LOANS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "115", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(loans);

		Account mortagages = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2250", AccounterConstants.MORTGAGES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "116", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(mortagages);

		Account accruals = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2300", AccounterConstants.ACCRUALS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "117", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(accruals);

		Account directorsCurrentAccount = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2400",
				AccounterConstants.DIRECTORS_CURRENT_ACCOUNT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "118", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(directorsCurrentAccount);

		Account netSalaries = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2500", AccounterConstants.NET_SALARIES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "119", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(netSalaries);

		Account pensions = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2510", AccounterConstants.PENSIONS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "120", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(pensions);

		Account unpaidExpenseClaims = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2520",
				AccounterConstants.UNPAID_EXPENSE_CLAIMS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "121", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(unpaidExpenseClaims);

		Account longTermLoans = new Account(Account.TYPE_LONG_TERM_LIABILITY,
				"9001", AccounterConstants.LONG_TERM_LOANS, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "137", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(longTermLoans);

		Account hirePurchaseCreditors = new Account(
				Account.TYPE_LONG_TERM_LIABILITY, "9100",
				AccounterConstants.HIRE_PURCHASE_CREDITORS, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "138", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(hirePurchaseCreditors);

		Account employeePayrollLiabilities = new Account(
				Account.TYPE_PAYROLL_LIABILITY, "2120",
				"Employee Payroll Liabilities", true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, true, openingBalancesAccount, "6", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(employeePayrollLiabilities);
	}

	abstract String getDateFormat();

	private void createNominalCodesRanges() {

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

		this.company.setNominalCodeRange(nominalCodesRangeSet);

	}
}
