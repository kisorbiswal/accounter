package com.vimukti.accounter.company.initialize;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import com.vimukti.accounter.core.VendorGroup;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.web.client.core.TemplateAccount;
import com.vimukti.accounter.web.client.ui.core.Calendar;

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
	protected CompanyPreferences preferences;

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
	Account cashDiscountsGiven;

	/**
	 * This is the direct references to Cash Discounts Given to track the
	 * discounts taken.
	 */
	Account cashDiscountsTaken;

	/**
	 * Creates new Instance
	 */
	public CompanyInitializer(Company company) {
		this.company = company;
		preferences = company.getPreferences();
	}

	/**
	 * this is used to get the company type
	 */
	public void init(List<TemplateAccount> defaultAccounts) {
		Session session = HibernateUtil.getCurrentSession();

		intializeCompanyValues();
		if (defaultAccounts != null) {
			ArrayList<Account> accounts = new ArrayList<Account>();
			for (int accountId = 1; accountId <= defaultAccounts.size(); accountId++) {
				TemplateAccount account = defaultAccounts.get(accountId);
				Account defaultAccount = new Account(account.getTypeAsInt(),
						"", account.getName(), true, null,
						account.getCashFlowAsInt(), 0.0, false, "", 0.0, null,
						true, true, null, String.valueOf(accountId), true,
						this.preferences.getPreventPostingBeforeDate());

				session.save(defaultAccount);
				accounts.add(defaultAccount);
			}
			company.setAccounts(accounts);
		}

		openingBalancesAccount = new Account(Account.TYPE_EQUITY, "3040",
				AccounterServerConstants.OPENING_BALANCE, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, true, null, "4", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(openingBalancesAccount);
		company.setOpeningBalancesAccount(openingBalancesAccount);

		initializeDefaultAssetsAccounts();
		initializeDefaultIncomeAccounts();
		initializeDefaultExpenseAccounts();
		initializeDefaultlLiabilitiesAccounts();
		initializeDefaultEquityAccounts();

		session.saveOrUpdate(company);

		init();

	}

	protected abstract void init();

	public Company getCompany() {
		return company;
	}

	private void intializeCompanyValues() {
		Session session = HibernateUtil.getCurrentSession();

		FinanceDate currentDate = new FinanceDate();
		int fiscalYearFirstMonth = this.company.getPreferences()
				.getFiscalYearFirstMonth();
		FinanceDate fiscalYearStartDate = new FinanceDate(
				(int) currentDate.getYear(), fiscalYearFirstMonth, 1);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(fiscalYearStartDate.getAsDateObject());
		endCal.set(Calendar.MONTH, endCal.get(Calendar.MONTH) + 11);
		endCal.set(Calendar.DATE,
				endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
		FinanceDate fiscalYearEndDate = new FinanceDate(endCal.getTime());

		FiscalYear fiscalYear = new FiscalYear(fiscalYearStartDate,
				fiscalYearEndDate, FiscalYear.STATUS_OPEN, Boolean.TRUE);

		session.save(fiscalYear);

		this.preferences.setUseAccountNumbers(true);
		this.preferences.setUseClasses(false);
		this.preferences.setUseJobs(false);
		this.preferences.setUseChangeLog(false);
		this.preferences.setAllowDuplicateDocumentNumbers(true);
		this.preferences.setDoYouPaySalesTax(true);
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
		this.preferences.setSalesOrderEnabled(true);
		FinanceDate depreciationStartDateCal = new FinanceDate();
		depreciationStartDateCal.set(fiscalYearStartDate);
		this.preferences.setDepreciationStartDate(depreciationStartDateCal);
		this.company.setPreferences(this.preferences);

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

		PaymentTerms monthlyPayrollLiability = new PaymentTerms(
				AccounterServerConstants.PM_MONTHLY,
				AccounterServerConstants.PM_MONTHLY_PAYROLL_LIABILITY, 0, 0,
				PaymentTerms.DUE_PAYROLL_TAX_MONTH, 13, true);

		session.save(monthlyPayrollLiability);

		VendorGroup creditCardCompanies = new VendorGroup();
		creditCardCompanies
				.setName(AccounterServerConstants.CREDIT_CARD_COMPANIES);
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

		Account retainedEarnings = new Account(Account.TYPE_EQUITY, "3051",
				AccounterServerConstants.RETAINED_EARNINGS, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "151", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(retainedEarnings);

		Account ownerAShareCapital = new Account(Account.TYPE_EQUITY, "3052",
				AccounterServerConstants.EQUITY_OWNER_SHARE, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "152", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(ownerAShareCapital);

		Account openingBalanceOffset = new Account(Account.TYPE_EQUITY, "3053",
				AccounterServerConstants.EQUITY_OPENING_BALANCE_OFFSET, true,
				null, Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "153", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(openingBalanceOffset);

		Account gainLossOnExchange = new Account(Account.TYPE_EQUITY, "3054",
				AccounterServerConstants.EQUITY_GAIN_LOSS_EXCHANGE, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "154", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(gainLossOnExchange);

		Account ordinaryShares = new Account(Account.TYPE_EQUITY, "3001",
				AccounterServerConstants.ORDINARY_SHARES, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "147", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(ordinaryShares);

		// Opening is done

		Account reservesRetainedEarnings = new Account(Account.TYPE_EQUITY,
				"3050", AccounterServerConstants.RESERVES_RETAINED_EARNINGS,
				true, null, Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "148",
				true, this.preferences.getPreventPostingBeforeDate());
		session.save(reservesRetainedEarnings);
		this.retainedEarningsAccount = reservesRetainedEarnings;

		Account PAndLBoughtForwardOrYTD = new Account(Account.TYPE_EQUITY,
				"3100", AccounterServerConstants.P_AND_L_BOUGHT_FORWARD_OR_YTD,
				true, null, Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "149",
				true, this.preferences.getPreventPostingBeforeDate());
		session.save(PAndLBoughtForwardOrYTD);

		Account dividends = new Account(Account.TYPE_EQUITY, "3150",
				AccounterServerConstants.DIVIDENDS, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "150", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(dividends);

	}

	public void initializeDefaultExpenseAccounts() {

		Session session = HibernateUtil.getCurrentSession();

		// Cost of Goods Sold
		Account productsOrMaterialsPurchasedTypeA = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD,
				"5001",
				AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_A,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "20", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(productsOrMaterialsPurchasedTypeA);

		Account productsOrMaterialsPurchasedTypeB = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD,
				"5002",
				AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_B,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "21", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(productsOrMaterialsPurchasedTypeB);

		Account productsOrMaterialsPurchasedTypeC = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD,
				"5003",
				AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_C,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "3", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(productsOrMaterialsPurchasedTypeC);

		Account productsOrMaterialsPurchasedTypeD = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD,
				"5004",
				AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_D,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "22", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(productsOrMaterialsPurchasedTypeD);

		Account productsOrMaterialsPurchasedTypeE = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD,
				"5005",
				AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_E,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "23", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(productsOrMaterialsPurchasedTypeE);

		Account productsOrMaterialsPurchasedTypeF = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD,
				"5006",
				AccounterServerConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_F,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "25", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(productsOrMaterialsPurchasedTypeF);

		Account carriage = new Account(Account.TYPE_COST_OF_GOODS_SOLD, "5200",
				AccounterServerConstants.CARRIAGE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "28", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(carriage);

		cashDiscountsTaken = new Account(Account.TYPE_COST_OF_GOODS_SOLD,
				"5210", AccounterServerConstants.CASH_DISCOUNT_TAKEN, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "29", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(cashDiscountsTaken);
		company.setCashDiscountsTaken(this.cashDiscountsTaken);

		Account importDuty = new Account(Account.TYPE_COST_OF_GOODS_SOLD,
				"5220", AccounterServerConstants.IMPORT_DUTY, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "30", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(importDuty);

		Account openingStock = new Account(Account.TYPE_COST_OF_GOODS_SOLD,
				"5900", AccounterServerConstants.OPENING_STOCK, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "24", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(openingStock);

		Account openingFinishedGoods = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD, "5920",
				AccounterServerConstants.OPEN_FINISHED_GOODS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "26", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(openingFinishedGoods);

		Account openingWorkInProgress = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD, "5930",
				AccounterServerConstants.OPEN_WORK_IN_PROGRESS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "27", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(openingWorkInProgress);
		// ---------------

		// Other Direct Costs
		Account directLabour = new Account(Account.TYPE_OTHER_EXPENSE, "6001",
				AccounterServerConstants.DIRECT_LABOUR, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "31", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(directLabour);

		Account directEmployersNI = new Account(Account.TYPE_OTHER_EXPENSE,
				"6010", AccounterServerConstants.DIRECT_EMPLOYERS_NI, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "32", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(directEmployersNI);

		Account otherDirectEmployeeRelatedCosts = new Account(
				Account.TYPE_OTHER_EXPENSE, "6020",
				AccounterServerConstants.OTHER_DIRECT_EMPLOYEE_RELATED_COSTS,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "33", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(otherDirectEmployeeRelatedCosts);

		Account directExpenses = new Account(Account.TYPE_OTHER_EXPENSE,
				"6100", AccounterServerConstants.DIRECT_EXPENSES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "34", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(directExpenses);

		Account directTravel = new Account(Account.TYPE_OTHER_EXPENSE, "6150",
				AccounterServerConstants.DIRECT_TRAVEL, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "35", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(directTravel);

		Account directConsumables = new Account(Account.TYPE_OTHER_EXPENSE,
				"6200", AccounterServerConstants.DIRECT_CONSUMABLES, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "36", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(directConsumables);

		Account merchantAccountFees = new Account(Account.TYPE_OTHER_EXPENSE,
				"6300", AccounterServerConstants.MERCHANT_ACCOUNT_FEES, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "37", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(merchantAccountFees);

		Account commisionsPaid = new Account(Account.TYPE_OTHER_EXPENSE,
				"6310", AccounterServerConstants.COMMISSIONS_PAID, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "38", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(commisionsPaid);
		// --------------

		// Indirect Costs
		Account indirectLabour = new Account(Account.TYPE_EXPENSE, "7001",
				AccounterServerConstants.INDIRECT_LABOUR, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "39", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(indirectLabour);

		Account indirectEmployersNI = new Account(Account.TYPE_EXPENSE, "7002",
				AccounterServerConstants.INDIRECT_EMPLOYERS_NI, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "40", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(indirectEmployersNI);

		Account directorsRemunaration = new Account(Account.TYPE_EXPENSE,
				"7003", AccounterServerConstants.DIRECTORS_REMUNERATION, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "41", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(directorsRemunaration);

		Account casualLabour = new Account(Account.TYPE_EXPENSE, "7004",
				AccounterServerConstants.CASUAL_LABOUR, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "42", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(casualLabour);

		Account employersPensionContributions = new Account(
				Account.TYPE_EXPENSE, "7010",
				AccounterServerConstants.EMPLOYERS_PANSION_CONTRIBUTIONS, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "43", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(employersPensionContributions);

		Account sspReclaimed = new Account(Account.TYPE_EXPENSE, "7011",
				AccounterServerConstants.SSP_RECLAIMED, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "44", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(sspReclaimed);

		Account smpReclaimed = new Account(Account.TYPE_EXPENSE, "7012",
				AccounterServerConstants.SMP_RECLAIMED, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "45", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(smpReclaimed);

		Account employeeBenifits = new Account(Account.TYPE_EXPENSE, "7025",
				AccounterServerConstants.EMPLOYEE_BENIFITS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "46", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(employeeBenifits);

		Account medicalInsurance = new Account(Account.TYPE_EXPENSE, "7030",
				AccounterServerConstants.MEDICAL_INSURANCE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "47", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(medicalInsurance);

		Account recruitement = new Account(Account.TYPE_EXPENSE, "7040",
				AccounterServerConstants.RECRUITMENT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "48", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(recruitement);

		Account training = new Account(Account.TYPE_EXPENSE, "7045",
				AccounterServerConstants.TRAINING, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "49", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(training);

		Account rent = new Account(Account.TYPE_EXPENSE, "7100",
				AccounterServerConstants.RENT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "50", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(rent);

		Account generalRates = new Account(Account.TYPE_EXPENSE, "7101",
				AccounterServerConstants.GENERAL_RATES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "51", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(generalRates);

		Account waterRates = new Account(Account.TYPE_EXPENSE, "7102",
				AccounterServerConstants.WATER_RATES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "52", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(waterRates);

		Account electricity = new Account(Account.TYPE_EXPENSE, "7110",
				AccounterServerConstants.ELECTRICITY, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "53", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(electricity);

		Account gas = new Account(Account.TYPE_EXPENSE, "7111",
				AccounterServerConstants.GAS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "54", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(gas);

		Account oil = new Account(Account.TYPE_EXPENSE, "7112",
				AccounterServerConstants.OIL, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "55", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(oil);

		Account officeCleaning = new Account(Account.TYPE_EXPENSE, "7120",
				AccounterServerConstants.OFFICE_CLEANING, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "56", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(officeCleaning);

		Account officeMachineMaintanance = new Account(Account.TYPE_EXPENSE,
				"7130", AccounterServerConstants.OFFICE_MACHINE_MAINTENANCE,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "57", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(officeMachineMaintanance);

		Account repairsAndRenewals = new Account(Account.TYPE_EXPENSE, "7140",
				AccounterServerConstants.REPAIR_RENEWALS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "58", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(repairsAndRenewals);

		Account officeConsumables = new Account(Account.TYPE_EXPENSE, "7150",
				AccounterServerConstants.OFFICE_CONSUMABLES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "59", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(officeConsumables);

		Account booksEtc = new Account(Account.TYPE_EXPENSE, "7151",
				AccounterServerConstants.BOOKS_ETC, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "60", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(booksEtc);

		Account internet = new Account(Account.TYPE_EXPENSE, "7152",
				AccounterServerConstants.INTERNET, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "61", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(internet);

		Account postage = new Account(Account.TYPE_EXPENSE, "7153",
				AccounterServerConstants.POSTAGE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "62", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(postage);

		Account printing = new Account(Account.TYPE_EXPENSE, "7154",
				AccounterServerConstants.PRINTING, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "63", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(printing);

		Account stationary = new Account(Account.TYPE_EXPENSE, "7155",
				AccounterServerConstants.STATIONERY, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "64", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(stationary);

		Account subscriptions = new Account(Account.TYPE_EXPENSE, "7156",
				AccounterServerConstants.SUBSCRIPTIONS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "65", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(subscriptions);

		Account telephone = new Account(Account.TYPE_EXPENSE, "7157",
				AccounterServerConstants.TELEPHONE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "66", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(telephone);

		Account conferenceAndSeminars = new Account(Account.TYPE_EXPENSE,
				"7158", AccounterServerConstants.CONFERENCES_AND_SEMINARS,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "67", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(conferenceAndSeminars);

		Account charityDonations = new Account(Account.TYPE_EXPENSE, "7160",
				AccounterServerConstants.CHARITY_DONATIONS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "68", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(charityDonations);

		Account insurencesBusiness = new Account(Account.TYPE_EXPENSE, "7200",
				AccounterServerConstants.INSURANCES_BUSINESS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "69", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(insurencesBusiness);

		Account advertisiningAndMarketing = new Account(Account.TYPE_EXPENSE,
				"7250", AccounterServerConstants.ADVERTISING_AND_MARKETING,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "70", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(advertisiningAndMarketing);

		Account localEntertainment = new Account(Account.TYPE_EXPENSE, "7260",
				AccounterServerConstants.LOCAL_ENTERTAINMENT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "71", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(localEntertainment);

		Account overseasEntertainment = new Account(Account.TYPE_EXPENSE,
				"7261", AccounterServerConstants.OVERSEAS_ENTERTAINMENT, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "72", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(overseasEntertainment);

		Account indirectLocalTravel = new Account(Account.TYPE_EXPENSE, "7270",
				AccounterServerConstants.INDIRECT_LOCAL_TRAVEL, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "73", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(indirectLocalTravel);

		Account indirectOverseasTravel = new Account(Account.TYPE_EXPENSE,
				"7271", AccounterServerConstants.INDIRECT_OVERSEAS_TRAVEL,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "74", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(indirectOverseasTravel);

		Account subsitence = new Account(Account.TYPE_EXPENSE, "7280",
				AccounterServerConstants.SUBSISTENCE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "75", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(subsitence);

		Account vechileExpenses = new Account(Account.TYPE_EXPENSE, "7300",
				AccounterServerConstants.VECHILE_EXPENSES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "76", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(vechileExpenses);

		Account vechileInsurance = new Account(Account.TYPE_EXPENSE, "7310",
				AccounterServerConstants.VECHILE_INSURANCE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "77", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(vechileInsurance);

		Account vechileRepairAndServicing = new Account(Account.TYPE_EXPENSE,
				"7320", AccounterServerConstants.VECHILE_REPAIRS_AND_SERVICING,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "78", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(vechileRepairAndServicing);

		Account professonalFees = new Account(Account.TYPE_EXPENSE, "7350",
				AccounterServerConstants.PROFESSIONAL_FEES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "79", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(professonalFees);

		Account accountancyFees = new Account(Account.TYPE_EXPENSE, "7360",
				AccounterServerConstants.ACCOUNTANCY_FEES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "80", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(accountancyFees);

		Account consultancyFees = new Account(Account.TYPE_EXPENSE, "7370",
				AccounterServerConstants.CONSULTANY_FEES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "81", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(consultancyFees);

		Account legalFees = new Account(Account.TYPE_EXPENSE, "7380",
				AccounterServerConstants.LEGAL_FEES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "82", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(legalFees);

		Account bankInterestPaid = new Account(Account.TYPE_EXPENSE, "7400",
				AccounterServerConstants.BANK_INTEREST_PAID, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "83", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(bankInterestPaid);

		Account bankCharges = new Account(Account.TYPE_EXPENSE, "7410",
				AccounterServerConstants.BANK_CHARGES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "84", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(bankCharges);

		Account creditCharges = new Account(Account.TYPE_EXPENSE, "7420",
				AccounterServerConstants.CREDIT_CHARGES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "85", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(creditCharges);

		Account leasePayments = new Account(Account.TYPE_EXPENSE, "7430",
				AccounterServerConstants.LEASE_PAYMENTS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "86", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(leasePayments);

		Account loanInterestPaid = new Account(Account.TYPE_EXPENSE, "7440",
				AccounterServerConstants.LOAN_INTEREST_PAID, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "87", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(loanInterestPaid);

		Account currencyCharges = new Account(Account.TYPE_EXPENSE, "7450",
				AccounterServerConstants.CURRENCY_CHARGES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "88", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(currencyCharges);

		Account exchangeRateVariance = new Account(Account.TYPE_EXPENSE,
				"7460", AccounterServerConstants.EXCHANGE_RATE_VARIANCE, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "89", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(exchangeRateVariance);

		Account badDebtProvision = new Account(Account.TYPE_EXPENSE, "7470",
				AccounterServerConstants.BAD_DEBT_PROVISION, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "90", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(badDebtProvision);

		Account badDebtWriteOff = new Account(Account.TYPE_EXPENSE, "7480",
				AccounterServerConstants.BAD_DEBT_WRITE_OFF, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "91", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(badDebtWriteOff);

		Account depreciation = new Account(Account.TYPE_EXPENSE, "7500",
				AccounterServerConstants.DEPRECIATION, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "92", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(depreciation);

		Account officeEquipmentDepreciation = new Account(Account.TYPE_EXPENSE,
				"7510", AccounterServerConstants.OFFICE_EQUIPMENT_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "93", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(officeEquipmentDepreciation);

		Account itEquipmentDepreciation = new Account(Account.TYPE_EXPENSE,
				"7520", AccounterServerConstants.IT_EQUIPMENT_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "94", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(itEquipmentDepreciation);

		Account furnituresAndFixuresDepreciation = new Account(
				Account.TYPE_EXPENSE, "7530",
				AccounterServerConstants.FURNITURE_AND_FIXTURES_DEPRECIARION,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "95", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(furnituresAndFixuresDepreciation);

		Account palntOrMachineryDepreciation = new Account(
				Account.TYPE_EXPENSE, "7540",
				AccounterServerConstants.PLANT_OR_MACHINERY_DEPRECIATION, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "96", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(palntOrMachineryDepreciation);

		Account vechileDepreciation = new Account(Account.TYPE_EXPENSE, "7550",
				AccounterServerConstants.VECHILE_DEPRECIATION, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "97", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(vechileDepreciation);

		Account freeHoldBuildingDepreciation = new Account(
				Account.TYPE_EXPENSE, "7560",
				AccounterServerConstants.FREEHOLD_BUILDING_DEPRECIATION, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "98", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(freeHoldBuildingDepreciation);

		Account leaseHoldPropertyImprvmntsDepreciation = new Account(
				Account.TYPE_EXPENSE,
				"7570",
				AccounterServerConstants.LEASEHOLD_PROPERTY_IMPROVEMENTS_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "99", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(leaseHoldPropertyImprvmntsDepreciation);
	}

	public void initializeDefaultIncomeAccounts() {

		Session session = HibernateUtil.getCurrentSession();

		Account salesIncomeTypeA = new Account(Account.TYPE_INCOME, "4001",
				AccounterServerConstants.SALES_INCOME_TYPE_A, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "5", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(salesIncomeTypeA);

		// TODO 8 accounts

		Account salesIncomeTypeB = new Account(Account.TYPE_INCOME, "4002",
				AccounterServerConstants.SALES_INCOME_TYPE_B, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "6", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(salesIncomeTypeB);

		Account salesIncomeTypeC = new Account(Account.TYPE_INCOME, "4003",
				AccounterServerConstants.SALES_INCOME_TYPE_C, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "7", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(salesIncomeTypeC);

		Account salesIncomeTypeD = new Account(Account.TYPE_INCOME, "4004",
				AccounterServerConstants.SALES_INCOME_TYPE_D, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "8", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(salesIncomeTypeD);

		Account salesIncomeTypeE = new Account(Account.TYPE_INCOME, "4005",
				AccounterServerConstants.SALES_INCOME_TYPE_E, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "9", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(salesIncomeTypeE);

		Account miscellaneousIncome = new Account(Account.TYPE_INCOME, "4100",
				AccounterServerConstants.MISCELLANEOUS_INCOME, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "10", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(miscellaneousIncome);

		Account distributionAndCarriage = new Account(Account.TYPE_INCOME,
				"4110", AccounterServerConstants.DISTRIBUTION_AND_CARRIAGE,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "11", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(distributionAndCarriage);

		cashDiscountsGiven = new Account(Account.TYPE_INCOME, "4120",
				AccounterServerConstants.CASH_DISCOUNT_GIVEN, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "12", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(cashDiscountsGiven);
		company.setCashDiscountsGiven(cashDiscountsGiven);

		Account commissionsReceived = new Account(Account.TYPE_INCOME, "4200",
				AccounterServerConstants.COMMISSION_RECIEVED, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "13", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(commissionsReceived);

		Account creditChargesLatePayment = new Account(Account.TYPE_INCOME,
				"4210", AccounterServerConstants.CREDIT_CHARGES_LATEPAYMENT,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "14", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(creditChargesLatePayment);

		Account insuranceClaims = new Account(Account.TYPE_INCOME, "4220",
				AccounterServerConstants.INSURANCE_CLAIMS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "15", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(insuranceClaims);

		Account interestIncome = new Account(Account.TYPE_INCOME, "4230",
				AccounterServerConstants.INTEREST_INCOME, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "16", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(interestIncome);

		Account rentIncome = new Account(Account.TYPE_INCOME, "4240",
				AccounterServerConstants.RENT_INCOME, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "17", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(rentIncome);

		Account royaltiesRecieved = new Account(Account.TYPE_INCOME, "4251",
				AccounterServerConstants.ROYALTIES_RECIEVED, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "19", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(royaltiesRecieved);

		Account profitOrLossOnSalesOfAssets = new Account(Account.TYPE_INCOME,
				"4260",
				AccounterServerConstants.PROFIT_OR_LOSS_ON_SALES_ASSETS, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "20", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(profitOrLossOnSalesOfAssets);
	}

	public void initializeDefaultAssetsAccounts() {
		Session session = HibernateUtil.getCurrentSession();

		// Current Accounts
		accountsReceivableAccount = new Account(
				Account.TYPE_OTHER_CURRENT_ASSET, "1001",
				AccounterServerConstants.ACCOUNTS_RECEIVABLE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, false, false, openingBalancesAccount, "2", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(accountsReceivableAccount);
		company.setAccountsReceivableAccount(accountsReceivableAccount);

		Account debitors = new Account(Account.TYPE_OTHER_CURRENT_ASSET,
				"1002", AccounterServerConstants.DEBTORS_ACCOUNTS_RECEIVABLE,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "100",
				true, this.preferences.getPreventPostingBeforeDate());
		session.save(debitors);

		Account deposits = new Account(Account.TYPE_OTHER_CURRENT_ASSET,
				"1003", AccounterServerConstants.DEPOSITS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "101", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(deposits);

		Account bankCurrentAccount = new Account(
				Account.TYPE_OTHER_CURRENT_ASSET, "1100",
				AccounterServerConstants.BANK_CURRENT_ACCOUNT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "103", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(bankCurrentAccount);

		Account bankDepositAccount = new Account(
				Account.TYPE_OTHER_CURRENT_ASSET, "1150",
				AccounterServerConstants.BANK_DEPOSIT_ACCOUNT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "104", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(bankDepositAccount);

		Account unDepositedFunds = new Account(
				Account.TYPE_OTHER_CURRENT_ASSET, "1175",
				AccounterServerConstants.UN_DEPOSITED_FUNDS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, false, true, openingBalancesAccount, "1", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(unDepositedFunds);

		Account pettyCash = new Account(Account.TYPE_OTHER_CURRENT_ASSET,
				"1180", AccounterServerConstants.PETTY_CASH, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "105", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(pettyCash);

		Account prepayments = new Account(Account.TYPE_OTHER_CURRENT_ASSET,
				"1185", AccounterServerConstants.PRE_PAYMENTS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "106", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(prepayments);

		Account advancesToEmployees = new Account(
				Account.TYPE_OTHER_CURRENT_ASSET, "1190",
				AccounterServerConstants.ADVANCES_TO_EMPLOYEES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "107", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(advancesToEmployees);

		Account stock = new Account(Account.TYPE_OTHER_CURRENT_ASSET, "1200",
				AccounterServerConstants.STOCK, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "108", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(stock);

		// Fixed Accounts
		Account officeEquipment = new Account(Account.TYPE_FIXED_ASSET, "0100",
				AccounterServerConstants.OFFICE_EQUIPMENT, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "126", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(officeEquipment);

		// TODO Less Acumulation Deprication on office Equipment
		Account computerEquipment = new Account(Account.TYPE_FIXED_ASSET,
				"0003", AccounterServerConstants.ASSETS_COMPUTER_EQUIPMENTS,
				true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "139",
				true, this.preferences.getPreventPostingBeforeDate());
		session.save(computerEquipment);

		Account lessAcumulationDepricationOnComputerEquipement = new Account(
				Account.TYPE_FIXED_ASSET, "0002",
				AccounterServerConstants.ASSETS_LAD_COMPUTER_EQUIPMENTS, true,
				null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "109", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(lessAcumulationDepricationOnComputerEquipement);

		Account freeHoldBuildings = new Account(Account.TYPE_FIXED_ASSET,
				"0001", AccounterServerConstants.FREEHOLD_BUILDINGS, true,
				null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "122", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(freeHoldBuildings);

		Account acumulatedFreeHoldBuildingDepreciation = new Account(
				Account.TYPE_FIXED_ASSET,
				"0040",
				AccounterServerConstants.ACCUMULATED_FREEHOLD_BUILDING_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "123",
				true, this.preferences.getPreventPostingBeforeDate());
		session.save(acumulatedFreeHoldBuildingDepreciation);

		Account leaseHoldPropertyImprovements = new Account(
				Account.TYPE_FIXED_ASSET, "0050",
				AccounterServerConstants.LEASEHOLD_PROPERTY_IMPROVEMENTS, true,
				null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "124", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(leaseHoldPropertyImprovements);

		Account accmltdLeaseHoldPropertyImprvmntsDepreciation = new Account(
				Account.TYPE_FIXED_ASSET,
				"0090",
				AccounterServerConstants.ACCUMULATED_LEASE_HOLD_PROPERTY_IMPROVEMENTS_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "125",
				true, this.preferences.getPreventPostingBeforeDate());
		session.save(accmltdLeaseHoldPropertyImprvmntsDepreciation);

		Account itEquipment = new Account(Account.TYPE_FIXED_ASSET, "0120",
				AccounterServerConstants.IT_EQUIPMENT, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "128", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(itEquipment);

		Account acumltdOfcEqupmntDepreciation = new Account(
				Account.TYPE_FIXED_ASSET,
				"0115",
				AccounterServerConstants.ACCUMULATED_OFFICE_EQUIPMENT_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "127",
				true, this.preferences.getPreventPostingBeforeDate());
		session.save(acumltdOfcEqupmntDepreciation);

		Account accumltdITEquipmentDepreciation = new Account(
				Account.TYPE_FIXED_ASSET, "0135",
				AccounterServerConstants.ACCUMULATED_IT_EQUIPMENT_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "129",
				true, this.preferences.getPreventPostingBeforeDate());
		session.save(accumltdITEquipmentDepreciation);

		Account furnitureAndFixtures = new Account(Account.TYPE_FIXED_ASSET,
				"0140", AccounterServerConstants.FURNITURE_AND_FIXTURES, true,
				null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "130", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(furnitureAndFixtures);

		Account accumltdFurnitureAndFixturesDepreciation = new Account(
				Account.TYPE_FIXED_ASSET,
				"0155",
				AccounterServerConstants.ACCUMULATED_FURNITURE_AND_FIXTURES_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "131",
				true, this.preferences.getPreventPostingBeforeDate());
		session.save(accumltdFurnitureAndFixturesDepreciation);

		Account plantAndMachinary = new Account(Account.TYPE_FIXED_ASSET,
				"0160", AccounterServerConstants.PLANT_AND_MACHINERY, true,
				null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "",
				0.0, null, true, false, openingBalancesAccount, "132", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(plantAndMachinary);

		Account accumltdPlantAndMachinaryDepreciation = new Account(
				Account.TYPE_FIXED_ASSET,
				"0175",
				AccounterServerConstants.ACCUMULATED_PLANT_AND_MACHINERY_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "133",
				true, this.preferences.getPreventPostingBeforeDate());
		session.save(accumltdPlantAndMachinaryDepreciation);

		Account vechiles = new Account(Account.TYPE_FIXED_ASSET, "0180",
				AccounterServerConstants.VECHICLES, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "134", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(vechiles);

		Account accumltdVechilesDepreciation = new Account(
				Account.TYPE_FIXED_ASSET, "0195",
				AccounterServerConstants.ACCUMULATED_VEHICLES_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false,
				"", 0.0, null, true, false, openingBalancesAccount, "135",
				true, this.preferences.getPreventPostingBeforeDate());
		session.save(accumltdVechilesDepreciation);

		Account intangibles = new Account(Account.TYPE_FIXED_ASSET, "0200",
				AccounterServerConstants.INTANGIBLES, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "136", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(intangibles);

		// Other Current Assets
		// TODO Advance Tax
		// TODO Inventary Assests
		// TODO prepaid
		// TODO Undeposited
		// TODO saves tax incurred on Expenses

		// Cash
		// TODO Un deposited funds
		// TODO Pretty cash
		// TODO

		// Bank

		//
		// Account bankRevolutions = new Account(Account.TYPE_OTHER_ASSET,
		// "9501",
		// AccounterConstants.BANK_REVALUATIONS, true, null,
		// Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
		// null, true, false, openingBalancesAccount, "140", true,
		// this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(bankRevolutions);
		//
		// Account historicalAdjustment = new Account(Account.TYPE_OTHER_ASSET,
		// "9510", AccounterConstants.HISTORICAL_ADJUSTMENT, true, null,
		// Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
		// null, true, false, openingBalancesAccount, "141", true,
		// this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(historicalAdjustment);
		//
		// Account realisedCurrencyGains = new Account(Account.TYPE_OTHER_ASSET,
		// "9520", AccounterConstants.REALISED_CURRENCY_GAINS, true, null,
		// Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
		// null, true, false, openingBalancesAccount, "142", true,
		// this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(realisedCurrencyGains);
		//
		// Account unrealisedCurrencyGains = new
		// Account(Account.TYPE_OTHER_ASSET,
		// "9530", AccounterConstants.UNREALISED_CURRENCY_GAINS, true,
		// null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "",
		// 0.0, null, true, false, openingBalancesAccount, "143", true,
		// this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(unrealisedCurrencyGains);
		//
		// Account rounding = new Account(Account.TYPE_OTHER_ASSET, "9540",
		// AccounterConstants.ROUNDING, true, null,
		// Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
		// null, true, false, openingBalancesAccount, "144", true,
		// this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(rounding);
		//
		// Account suspense = new Account(Account.TYPE_OTHER_ASSET, "9600",
		// AccounterConstants.SUSPENSE, true, null,
		// Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
		// null, true, false, openingBalancesAccount, "146", true,
		// this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(suspense);

	}

	public void initializeDefaultlLiabilitiesAccounts() {
		Session session = HibernateUtil.getCurrentSession();

		// Current Liabilities
		// TODO Remaining 16

		accountsPayableAccount = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2001",
				AccounterServerConstants.ACCOUNTS_PAYABLE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "3", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(accountsPayableAccount);
		company.setAccountsPayableAccount(accountsPayableAccount);

		Account creditCards = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2099", AccounterServerConstants.CREDIT_CARDS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "110", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(creditCards);

		Account payeeEmploymentTax = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2100",
				AccounterServerConstants.PAYEE_EMPLOYEMENT_TAX, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "111", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(payeeEmploymentTax);

		Account nationalInsuranceTax = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2110",
				AccounterServerConstants.NATIONAL_INSURANCE_TAX, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "112", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(nationalInsuranceTax);

		// TODO Sales Tax (VAT) Un filed
		// TODO Sales Tax (VAT) Filed

		Account corporationTax = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2130",
				AccounterServerConstants.CORPORATION_RAX, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "114", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(corporationTax);

		Account loans = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2200", AccounterServerConstants.LOANS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "115", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(loans);

		Account mortagages = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2250", AccounterServerConstants.MORTGAGES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "116", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(mortagages);

		Account accruals = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2300", AccounterServerConstants.ACCRUALS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "117", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(accruals);

		Account directorsCurrentAccount = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2400",
				AccounterServerConstants.DIRECTORS_CURRENT_ACCOUNT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "118", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(directorsCurrentAccount);

		Account netSalaries = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2500", AccounterServerConstants.NET_SALARIES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "119", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(netSalaries);

		Account pensions = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2510", AccounterServerConstants.PENSIONS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "120", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(pensions);

		Account unpaidExpenseClaims = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2520",
				AccounterServerConstants.UNPAID_EXPENSE_CLAIMS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "121", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(unpaidExpenseClaims);
		// -----------

		// Long term liabilities
		Account longTermLoans = new Account(Account.TYPE_LONG_TERM_LIABILITY,
				"9001", AccounterServerConstants.LONG_TERM_LOANS, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "137", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(longTermLoans);

		Account hirePurchaseCreditors = new Account(
				Account.TYPE_LONG_TERM_LIABILITY, "9100",
				AccounterServerConstants.HIRE_PURCHASE_CREDITORS, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, false, openingBalancesAccount, "138", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(hirePurchaseCreditors);
		// TODO defered tax
		// ----------
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
