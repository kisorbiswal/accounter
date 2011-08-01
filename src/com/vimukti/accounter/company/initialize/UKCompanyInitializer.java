package com.vimukti.accounter.company.initialize;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXGroup;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.VATReturn;
import com.vimukti.accounter.core.VATReturnBox;
import com.vimukti.accounter.core.VendorGroup;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;

public class UKCompanyInitializer extends CompanyInitializer {

	/**
	 * Each company have it's own preferences. This will hold all the
	 * preferences related to the company.
	 * 
	 * @see Company
	 */
	CompanyPreferences preferences = new CompanyPreferences();

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
	 * This is the Account created by default for the purpose of UK VAT
	 */
	Account VATliabilityAccount;
	// Account prepaidVATaccount;
	// Account ECAcquisitionVATaccount;

	Account pendingItemReceiptsAccount;
	/**
	 * This is the Account created by default for the purpose of UK when VAT is
	 * Filed
	 */
	Account VATFiledLiabilityAccount;

	Set<NominalCodeRange> nominalCodeRange = new HashSet<NominalCodeRange>();
	/**
	 * Name of the Company
	 */
	String name;// Trading name

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

	private void initDefaultUKAccounts(Session session) {

		FinanceDate currentDate = new FinanceDate();
		FinanceDate fiscalYearStartDate = new FinanceDate(
				(int) currentDate.getYear(), 0, 1);
		FinanceDate fiscalYearEndDate = new FinanceDate(
				(int) currentDate.getYear(), 11, 31);

		FiscalYear fiscalYear = new FiscalYear(fiscalYearStartDate,
				fiscalYearEndDate, FiscalYear.STATUS_OPEN, Boolean.TRUE);
		String dateFormat = AccounterConstants.ddMMyyyy;

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
			preferences.setDoYouPaySalesTax(false);
			preferences.setIsAccuralBasis(true);
			// preferences.setStartOfFiscalYear(format.parse("2009-01-01"));
			// preferences.setEndOfFiscalYear(format.parse("2009-12-31"));

			preferences.setStartOfFiscalYear(fiscalYearStartDate);
			preferences.setEndOfFiscalYear(fiscalYearEndDate);
			preferences.setUseForeignCurrency(false);
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

		Account openingBalances = new Account(Account.TYPE_EQUITY, "3040",
				AccounterConstants.OPENING_BALANCE, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, true, null, "4", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(openingBalances);

		Account accountsReceivable = new Account(
				Account.TYPE_OTHER_CURRENT_ASSET, "1001",
				AccounterConstants.ACCOUNTS_RECEIVABLE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, false, false, openingBalances, "2", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(accountsReceivable);

		Account deposits = new Account(Account.TYPE_OTHER_CURRENT_ASSET,
				"1003", AccounterConstants.DEPOSITS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "100", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(deposits);

		Account accountsPayable = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2001",
				AccounterConstants.ACCOUNTS_PAYABLE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "3", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(accountsPayable);

		// Account pendingItemReceipts = new Account(
		// Account.TYPE_OTHER_CURRENT_LIABILITY, "2010",
		// AccounterConstants.PENDING_ITEM_RECEIPTS, true, null,
		// Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalances, "4", true, this.preferences
		// .getPreventPostingBeforeDate());
		//
		// session.save(pendingItemReceipts);

		Account salesIncomeTypeA = new Account(Account.TYPE_INCOME, "4001",
				AccounterConstants.SALES_INCOME_TYPE_A, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "5", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(salesIncomeTypeA);

		Account salesIncomeTypeB = new Account(Account.TYPE_INCOME, "4002",
				AccounterConstants.SALES_INCOME_TYPE_B, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "6", true,
				this.preferences.getPreventPostingBeforeDate());
		session.save(salesIncomeTypeB);

		Account salesIncomeTypeC = new Account(Account.TYPE_INCOME, "4003",
				AccounterConstants.SALES_INCOME_TYPE_C, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "7", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(salesIncomeTypeC);

		Account salesIncomeTypeD = new Account(Account.TYPE_INCOME, "4004",
				AccounterConstants.SALES_INCOME_TYPE_D, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "8", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(salesIncomeTypeD);

		Account salesIncomeTypeE = new Account(Account.TYPE_INCOME, "4005",
				AccounterConstants.SALES_INCOME_TYPE_E, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "9", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(salesIncomeTypeE);

		Account miscellaneousIncome = new Account(Account.TYPE_INCOME, "4100",
				AccounterConstants.MISCELLANEOUS_INCOME, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "10", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(miscellaneousIncome);

		Account distributionAndCarriage = new Account(Account.TYPE_INCOME,
				"4110", AccounterConstants.DISTRIBUTION_AND_CARRIAGE, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalances, "11", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(distributionAndCarriage);

		Account discounts = new Account(Account.TYPE_INCOME, "4120",
				AccounterConstants.DISCOUNTS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "12", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(discounts);

		Account commissionsReceived = new Account(Account.TYPE_INCOME, "4200",
				AccounterConstants.COMMISSION_RECIEVED, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "13", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(commissionsReceived);

		Account creditChargesLatePayment = new Account(Account.TYPE_INCOME,
				"4210", AccounterConstants.CREDIT_CHARGES_LATEPAYMENT, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalances, "14", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(creditChargesLatePayment);

		Account insuranceClaims = new Account(Account.TYPE_INCOME, "4220",
				AccounterConstants.INSURANCE_CLAIMS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "15", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(insuranceClaims);

		Account interestIncome = new Account(Account.TYPE_INCOME, "4230",
				AccounterConstants.INTEREST_INCOME, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "16", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(interestIncome);

		Account rentIncome = new Account(Account.TYPE_INCOME, "4240",
				AccounterConstants.RENT_INCOME, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "17", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(rentIncome);

		Account royltiesReceived = new Account(Account.TYPE_INCOME, "4250",
				AccounterConstants.ROYALTIES_RECIEVED, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "18", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(royltiesReceived);

		Account profitOrLossOnSalesOfAssets = new Account(Account.TYPE_INCOME,
				"4260", AccounterConstants.PROFIT_OR_LOSS_ON_SALES_ASSETS,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalances, "19", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(profitOrLossOnSalesOfAssets);

		/**
		 * The following are the Default Accounts for the Type Direct Costs &
		 * Material Products
		 */

		Account productsOrMaterialsPurchasedTypeA = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD, "5001",
				AccounterConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_A,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalances, "20", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(productsOrMaterialsPurchasedTypeA);

		Account productsOrMaterialsPurchasedTypeB = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD, "5002",
				AccounterConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_B,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalances, "21", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(productsOrMaterialsPurchasedTypeB);

		Account productsOrMaterialsPurchasedTypeC = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD, "5003",
				AccounterConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_C,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalances, "3", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(productsOrMaterialsPurchasedTypeC);

		Account productsOrMaterialsPurchasedTypeD = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD, "5004",
				AccounterConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_D,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalances, "22", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(productsOrMaterialsPurchasedTypeD);

		Account productsOrMaterialsPurchasedTypeE = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD, "5005",
				AccounterConstants.PRODUCTS_OR_MATERIALS_PURCHASED_TYPE_E,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalances, "23", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(productsOrMaterialsPurchasedTypeE);

		Account carriage = new Account(Account.TYPE_COST_OF_GOODS_SOLD, "5200",
				AccounterConstants.CARRIAGE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "28", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(carriage);

		Account discountsTaken = new Account(Account.TYPE_COST_OF_GOODS_SOLD,
				"5210", AccounterConstants.DISCOUNTS_TAKEN, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "29", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(discountsTaken);

		Account importDuty = new Account(Account.TYPE_COST_OF_GOODS_SOLD,
				"5220", AccounterConstants.IMPORT_DUTY, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "30", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(importDuty);

		Account openingStock = new Account(Account.TYPE_COST_OF_GOODS_SOLD,
				"5900", AccounterConstants.OPENING_STOCK, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "24", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(openingStock);

		// Account closingStock = new Account(Account.TYPE_COST_OF_GOODS_SOLD,
		// "5910", AccounterConstants.CLOSING_STOCK, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
		// openingBalances, "25", true, this.preferences
		// .getPreventPostingBeforeDate());
		//
		// session.save(closingStock);

		Account openingFinishedGoods = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD, "5920",
				AccounterConstants.OPEN_FINISHED_GOODS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "26", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(openingFinishedGoods);

		// Account closingFinishedGoods = new Account(
		// Account.TYPE_COST_OF_GOODS_SOLD, "5925",
		// AccounterConstants.CLOSE_FINISHED_GOODS, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
		// openingBalances, "151", true, this.preferences
		// .getPreventPostingBeforeDate());
		//
		// session.save(closingFinishedGoods);

		Account openingWorkInProgress = new Account(
				Account.TYPE_COST_OF_GOODS_SOLD, "5930",
				AccounterConstants.OPEN_WORK_IN_PROGRESS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "27", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(openingWorkInProgress);
		//
		// Account closingWorkInProgress = new Account(
		// Account.TYPE_COST_OF_GOODS_SOLD, "5935",
		// AccounterConstants.CLOSE_WORK_IN_PROGRESS, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
		// openingBalances, "152", true, this.preferences
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
				null, true, false, openingBalances, "31", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(directLabour);

		Account directEmployersNI = new Account(Account.TYPE_OTHER_EXPENSE,
				"6010", AccounterConstants.DIRECT_EMPLOYERS_NI, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "32", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(directEmployersNI);

		Account otherDirectEmployeeRelatedCosts = new Account(
				Account.TYPE_OTHER_EXPENSE, "6020",
				AccounterConstants.OTHER_DIRECT_EMPLOYEE_RELATED_COSTS, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalances, "33", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(otherDirectEmployeeRelatedCosts);

		Account directExpenses = new Account(Account.TYPE_OTHER_EXPENSE,
				"6100", AccounterConstants.DIRECT_EXPENSES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "34", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(directExpenses);

		Account directTravel = new Account(Account.TYPE_OTHER_EXPENSE, "6150",
				AccounterConstants.DIRECT_TRAVEL, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "35", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(directTravel);

		Account directConsumables = new Account(Account.TYPE_OTHER_EXPENSE,
				"6200", AccounterConstants.DIRECT_CONSUMABLES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "36", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(directConsumables);

		Account merchantAccountFees = new Account(Account.TYPE_OTHER_EXPENSE,
				"6300", AccounterConstants.MERCHANT_ACCOUNT_FEES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "37", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(merchantAccountFees);

		Account commisionsPaid = new Account(Account.TYPE_OTHER_EXPENSE,
				"6310", AccounterConstants.COMMISSIONS_PAID, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "38", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(commisionsPaid);

		Account indirectLabour = new Account(Account.TYPE_EXPENSE, "7001",
				AccounterConstants.INDIRECT_LABOUR, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "39", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(indirectLabour);

		Account indirectEmployersNI = new Account(Account.TYPE_EXPENSE, "7002",
				AccounterConstants.INDIRECT_EMPLOYERS_NI, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "40", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(indirectEmployersNI);

		Account directorsRemunaration = new Account(Account.TYPE_EXPENSE,
				"7003", AccounterConstants.DIRECTORS_REMUNERATION, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "41", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(directorsRemunaration);

		Account casualLabour = new Account(Account.TYPE_EXPENSE, "7004",
				AccounterConstants.CASUAL_LABOUR, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "42", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(casualLabour);

		Account employersPensionContributions = new Account(
				Account.TYPE_EXPENSE, "7010",
				AccounterConstants.EMPLOYERS_PANSION_CONTRIBUTIONS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "43", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(employersPensionContributions);

		Account sspReclaimed = new Account(Account.TYPE_EXPENSE, "7020",
				AccounterConstants.SSP_RECLAIMED, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "44", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(sspReclaimed);

		Account smpReclaimed = new Account(Account.TYPE_EXPENSE, "7021",
				AccounterConstants.SMP_RECLAIMED, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "45", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(smpReclaimed);

		Account employeeBenifits = new Account(Account.TYPE_EXPENSE, "7025",
				AccounterConstants.EMPLOYEE_BENIFITS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "46", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(employeeBenifits);

		Account medicalInsurance = new Account(Account.TYPE_EXPENSE, "7030",
				AccounterConstants.MEDICAL_INSURANCE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "47", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(medicalInsurance);

		Account recruitement = new Account(Account.TYPE_EXPENSE, "7040",
				AccounterConstants.RECRUITMENT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "48", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(recruitement);

		Account training = new Account(Account.TYPE_EXPENSE, "7045",
				AccounterConstants.TRAINING, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "49", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(training);

		Account rent = new Account(Account.TYPE_EXPENSE, "7100",
				AccounterConstants.RENT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "50", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(rent);

		Account generalRates = new Account(Account.TYPE_EXPENSE, "7101",
				AccounterConstants.GENERAL_RATES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "51", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(generalRates);

		Account waterRates = new Account(Account.TYPE_EXPENSE, "7102",
				AccounterConstants.WATER_RATES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "52", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(waterRates);

		Account electricity = new Account(Account.TYPE_EXPENSE, "7110",
				AccounterConstants.ELECTRICITY, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "53", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(electricity);

		Account gas = new Account(Account.TYPE_EXPENSE, "7111",
				AccounterConstants.GAS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "54", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(gas);

		Account oil = new Account(Account.TYPE_EXPENSE, "7112",
				AccounterConstants.OIL, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "55", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(oil);

		Account officeCleaning = new Account(Account.TYPE_EXPENSE, "7120",
				AccounterConstants.OFFICE_CLEANING, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "56", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(officeCleaning);

		Account officeMachineMaintanance = new Account(Account.TYPE_EXPENSE,
				"7130", AccounterConstants.OFFICE_MACHINE_MAINTENANCE, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalances, "57", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(officeMachineMaintanance);

		Account repairsAndRenewals = new Account(Account.TYPE_EXPENSE, "7140",
				AccounterConstants.REPAIR_RENEWALS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "58", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(repairsAndRenewals);

		Account officeConsumables = new Account(Account.TYPE_EXPENSE, "7150",
				AccounterConstants.OFFICE_CONSUMABLES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "59", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(officeConsumables);

		Account booksEtc = new Account(Account.TYPE_EXPENSE, "7151",
				AccounterConstants.BOOKS_ETC, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "60", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(booksEtc);

		Account internet = new Account(Account.TYPE_EXPENSE, "7152",
				AccounterConstants.INTERNET, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "61", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(internet);

		Account postage = new Account(Account.TYPE_EXPENSE, "7153",
				AccounterConstants.POSTAGE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "62", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(postage);

		Account printing = new Account(Account.TYPE_EXPENSE, "7154",
				AccounterConstants.PRINTING, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "63", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(printing);

		Account stationary = new Account(Account.TYPE_EXPENSE, "7155",
				AccounterConstants.STATIONERY, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "64", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(stationary);

		Account subscriptions = new Account(Account.TYPE_EXPENSE, "7156",
				AccounterConstants.SUBSCRIPTIONS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "65", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(subscriptions);

		Account telephone = new Account(Account.TYPE_EXPENSE, "7157",
				AccounterConstants.TELEPHONE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "66", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(telephone);

		Account conferenceAndSeminars = new Account(Account.TYPE_EXPENSE,
				"7158", AccounterConstants.CONFERENCES_AND_SEMINARS, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalances, "67", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(conferenceAndSeminars);

		Account charityDonations = new Account(Account.TYPE_EXPENSE, "7160",
				AccounterConstants.CHARITY_DONATIONS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "68", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(charityDonations);

		Account insurencesBusiness = new Account(Account.TYPE_EXPENSE, "7200",
				AccounterConstants.INSURANCES_BUSINESS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "69", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(insurencesBusiness);

		Account advertisiningAndMarketing = new Account(Account.TYPE_EXPENSE,
				"7250", AccounterConstants.ADVERTISING_AND_MARKETING, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalances, "70", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(advertisiningAndMarketing);

		Account localEntertainment = new Account(Account.TYPE_EXPENSE, "7260",
				AccounterConstants.LOCAL_ENTERTAINMENT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "71", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(localEntertainment);

		Account overseasEntertainment = new Account(Account.TYPE_EXPENSE,
				"7261", AccounterConstants.OVERSEAS_ENTERTAINMENT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "72", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(overseasEntertainment);

		Account indirectLocalTravel = new Account(Account.TYPE_EXPENSE, "7270",
				AccounterConstants.INDIRECT_LOCAL_TRAVEL, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "73", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(indirectLocalTravel);

		Account indirectOverseasTravel = new Account(Account.TYPE_EXPENSE,
				"7271", AccounterConstants.INDIRECT_OVERSEAS_TRAVEL, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalances, "74", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(indirectOverseasTravel);

		Account subsitence = new Account(Account.TYPE_EXPENSE, "7280",
				AccounterConstants.SUBSISTENCE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "75", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(subsitence);

		Account vechileExpenses = new Account(Account.TYPE_EXPENSE, "7300",
				AccounterConstants.VECHILE_EXPENSES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "76", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(vechileExpenses);

		Account vechileInsurance = new Account(Account.TYPE_EXPENSE, "7310",
				AccounterConstants.VECHILE_INSURANCE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "77", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(vechileInsurance);

		Account vechileRepairAndServicing = new Account(Account.TYPE_EXPENSE,
				"7320", AccounterConstants.VECHILE_REPAIRS_AND_SERVICING, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalances, "78", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(vechileRepairAndServicing);

		Account professonalFees = new Account(Account.TYPE_EXPENSE, "7350",
				AccounterConstants.PROFESSIONAL_FEES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "79", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(professonalFees);

		Account accountancyFees = new Account(Account.TYPE_EXPENSE, "7360",
				AccounterConstants.ACCOUNTANCY_FEES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "80", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(accountancyFees);

		Account consultancyFees = new Account(Account.TYPE_EXPENSE, "7370",
				AccounterConstants.CONSULTANY_FEES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "81", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(consultancyFees);

		Account legalFees = new Account(Account.TYPE_EXPENSE, "7380",
				AccounterConstants.LEGAL_FEES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "82", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(legalFees);

		Account bankInterestPaid = new Account(Account.TYPE_EXPENSE, "7400",
				AccounterConstants.BANK_INTEREST_PAID, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "83", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(bankInterestPaid);

		Account bankCharges = new Account(Account.TYPE_EXPENSE, "7410",
				AccounterConstants.BANK_CHARGES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "84", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(bankCharges);

		Account creditCharges = new Account(Account.TYPE_EXPENSE, "7420",
				AccounterConstants.CREDIT_CHARGES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "85", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(creditCharges);

		Account leasePayments = new Account(Account.TYPE_EXPENSE, "7430",
				AccounterConstants.LEASE_PAYMENTS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "86", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(leasePayments);

		Account loanInterestPaid = new Account(Account.TYPE_EXPENSE, "7440",
				AccounterConstants.LOAN_INTEREST_PAID, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "87", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(loanInterestPaid);

		Account currencyCharges = new Account(Account.TYPE_EXPENSE, "7450",
				AccounterConstants.CURRENCY_CHARGES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "88", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(currencyCharges);

		Account exchangeRateVariance = new Account(Account.TYPE_EXPENSE,
				"7460", AccounterConstants.EXCHANGE_RATE_VARIANCE, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "89", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(exchangeRateVariance);

		Account badDebtProvision = new Account(Account.TYPE_EXPENSE, "7470",
				AccounterConstants.BAD_DEBT_PROVISION, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "90", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(badDebtProvision);

		Account badDebtWriteOff = new Account(Account.TYPE_EXPENSE, "7480",
				AccounterConstants.BAD_DEBT_WRITE_OFF, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "91", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(badDebtWriteOff);

		Account depreciation = new Account(Account.TYPE_EXPENSE, "7500",
				AccounterConstants.DEPRECIATION, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "92", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(depreciation);

		Account officeEquipmentDepreciation = new Account(Account.TYPE_EXPENSE,
				"7510", AccounterConstants.OFFICE_EQUIPMENT_DEPRECIATION, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalances, "93", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(officeEquipmentDepreciation);

		Account itEquipmentDepreciation = new Account(Account.TYPE_EXPENSE,
				"7520", AccounterConstants.IT_EQUIPMENT_DEPRECIATION, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalances, "94", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(itEquipmentDepreciation);

		Account furnituresAndFixuresDepreciation = new Account(
				Account.TYPE_EXPENSE, "7530",
				AccounterConstants.FURNITURE_AND_FIXTURES_DEPRECIARION, true,
				null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
				0.0, null, true, false, openingBalances, "95", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(furnituresAndFixuresDepreciation);

		Account palntOrMachineryDepreciation = new Account(
				Account.TYPE_EXPENSE, "7540",
				AccounterConstants.PLANT_OR_MACHINERY_DEPRECIATION, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "96", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(palntOrMachineryDepreciation);

		Account vechileDepreciation = new Account(Account.TYPE_EXPENSE, "7550",
				AccounterConstants.VECHILE_DEPRECIATION, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "97", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(vechileDepreciation);

		Account freeHoldBuildingDepreciation = new Account(
				Account.TYPE_EXPENSE, "7560",
				AccounterConstants.FREEHOLD_BUILDING_DEPRECIATION, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "98", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(freeHoldBuildingDepreciation);

		Account leaseHoldPropertyImprvmntsDepreciation = new Account(
				Account.TYPE_EXPENSE,
				"7570",
				AccounterConstants.LEASEHOLD_PROPERTY_IMPROVEMENTS_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false,
				"", 0.0, null, true, false, openingBalances, "99", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(leaseHoldPropertyImprvmntsDepreciation);

		// Account debtorsAccountsReceivable = new Account(
		// Account.TYPE_OTHER_CURRENT_ASSET, "1002",
		// AccounterConstants.DEBTORS_ACCOUNTS_RECEIVABLE, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
		// openingBalances, "100", true, this.preferences
		// .getPreventPostingBeforeDate());
		//
		// session.save(debtorsAccountsReceivable);

		Account bankCurrentAccount = new Account(
				Account.TYPE_OTHER_CURRENT_ASSET, "1100",
				AccounterConstants.BANK_CURRENT_ACCOUNT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "103", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(bankCurrentAccount);

		Account bankDepositAccount = new Account(
				Account.TYPE_OTHER_CURRENT_ASSET, "1150",
				AccounterConstants.BANK_DEPOSIT_ACCOUNT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "104", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(bankDepositAccount);

		Account unDepositedFunds = new Account(
				Account.TYPE_OTHER_CURRENT_ASSET, "1175",
				AccounterConstants.UN_DEPOSITED_FUNDS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, false, true, openingBalances, "1", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(unDepositedFunds);

		Account pettyCash = new Account(Account.TYPE_OTHER_CURRENT_ASSET,
				"1180", AccounterConstants.PETTY_CASH, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "105", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(pettyCash);

		Account prepayments = new Account(Account.TYPE_OTHER_CURRENT_ASSET,
				"1185", AccounterConstants.PRE_PAYMENTS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "106", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(prepayments);

		Account advancesToEmployees = new Account(
				Account.TYPE_OTHER_CURRENT_ASSET, "1190",
				AccounterConstants.ADVANCES_TO_EMPLOYEES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "107", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(advancesToEmployees);

		Account stock = new Account(Account.TYPE_OTHER_CURRENT_ASSET, "1200",
				AccounterConstants.STOCK, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "108", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(stock);

		// Account creditorsAccountsPayable = new Account(
		// Account.TYPE_OTHER_CURRENT_LIABILITY, "2002",
		// AccounterConstants.CREDITORS_ACCOUNTS_PAYBLE, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, false,
		// openingBalances, "109", true, this.preferences
		// .getPreventPostingBeforeDate());
		//
		// session.save(creditorsAccountsPayable);

		Account creditCards = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2050", AccounterConstants.CREDIT_CARDS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "110", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(creditCards);

		Account payeeEmploymentTax = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2100",
				AccounterConstants.PAYEE_EMPLOYEMENT_TAX, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "111", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(payeeEmploymentTax);

		Account nationalInsuranceTax = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2110",
				AccounterConstants.NATIONAL_INSURANCE_TAX, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "112", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(nationalInsuranceTax);

		Account saelsTaxVAT = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2120", AccounterConstants.SALES_TAX_VAT_UNFILED, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "113", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(saelsTaxVAT);

		Account corporationTax = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2130",
				AccounterConstants.CORPORATION_RAX, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "114", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(corporationTax);

		Account loans = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2200", AccounterConstants.LOANS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "115", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(loans);

		Account mortagages = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2250", AccounterConstants.MORTGAGES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "116", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(mortagages);

		Account accruals = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2300", AccounterConstants.ACCRUALS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "117", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(accruals);

		Account directorsCurrentAccount = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2400",
				AccounterConstants.DIRECTORS_CURRENT_ACCOUNT, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "118", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(directorsCurrentAccount);

		Account netSalaries = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2500", AccounterConstants.NET_SALARIES, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "119", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(netSalaries);

		Account pensions = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
				"2510", AccounterConstants.PENSIONS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "120", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(pensions);

		Account unpaidExpenseClaims = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2520",
				AccounterConstants.UNPAID_EXPENSE_CLAIMS, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "121", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(unpaidExpenseClaims);

		Account freeHoldBuildings = new Account(Account.TYPE_FIXED_ASSET,
				"0001", AccounterConstants.FREEHOLD_BUILDINGS, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "122", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(freeHoldBuildings);

		Account acumulatedFreeHoldBuildingDepreciation = new Account(
				Account.TYPE_FIXED_ASSET, "0040",
				AccounterConstants.ACCUMULATED_FREEHOLD_BUILDING_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false,
				"", 0.0, null, true, false, openingBalances, "123", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(acumulatedFreeHoldBuildingDepreciation);

		Account leaseHoldPropertyImprovements = new Account(
				Account.TYPE_FIXED_ASSET, "0050",
				AccounterConstants.LEASEHOLD_PROPERTY_IMPROVEMENTS, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "124", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(leaseHoldPropertyImprovements);

		Account accmltdLeaseHoldPropertyImprvmntsDepreciation = new Account(
				Account.TYPE_FIXED_ASSET,
				"0090",
				AccounterConstants.ACCUMULATED_LEASE_HOLD_PROPERTY_IMPROVEMENTS_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false,
				"", 0.0, null, true, false, openingBalances, "125", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(accmltdLeaseHoldPropertyImprvmntsDepreciation);

		Account officeEquipment = new Account(Account.TYPE_FIXED_ASSET, "0100",
				AccounterConstants.OFFICE_EQUIPMENT, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "126", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(officeEquipment);

		Account acumltdOfcEqupmntDepreciation = new Account(
				Account.TYPE_FIXED_ASSET, "0115",
				AccounterConstants.ACCUMULATED_OFFICE_EQUIPMENT_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false,
				"", 0.0, null, true, false, openingBalances, "127", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(acumltdOfcEqupmntDepreciation);

		Account itEquipment = new Account(Account.TYPE_FIXED_ASSET, "0120",
				AccounterConstants.IT_EQUIPMENT, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "128", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(itEquipment);

		Account accumltdITEquipmentDepreciation = new Account(
				Account.TYPE_FIXED_ASSET, "0135",
				AccounterConstants.ACCUMULATED_IT_EQUIPMENT_DEPRECIATION, true,
				null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "",
				0.0, null, true, false, openingBalances, "129", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(accumltdITEquipmentDepreciation);

		Account furnitureAndFixtures = new Account(Account.TYPE_FIXED_ASSET,
				"0140", AccounterConstants.FURNITURE_AND_FIXTURES, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "130", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(furnitureAndFixtures);

		Account accumltdFurnitureAndFixturesDepreciation = new Account(
				Account.TYPE_FIXED_ASSET,
				"0155",
				AccounterConstants.ACCUMULATED_FURNITURE_AND_FIXTURES_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false,
				"", 0.0, null, true, false, openingBalances, "131", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(accumltdFurnitureAndFixturesDepreciation);

		Account plantAndMachinary = new Account(Account.TYPE_FIXED_ASSET,
				"0160", AccounterConstants.PLANT_AND_MACHINERY, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "132", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(plantAndMachinary);

		Account accumltdPlantAndMachinaryDepreciation = new Account(
				Account.TYPE_FIXED_ASSET,
				"0175",
				AccounterConstants.ACCUMULATED_PLANT_AND_MACHINERY_DEPRECIATION,
				true, null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false,
				"", 0.0, null, true, false, openingBalances, "133", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(accumltdPlantAndMachinaryDepreciation);

		Account vechiles = new Account(Account.TYPE_FIXED_ASSET, "0180",
				AccounterConstants.VECHICLES, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "134", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(vechiles);

		Account accumltdVechilesDepreciation = new Account(
				Account.TYPE_FIXED_ASSET, "0195",
				AccounterConstants.ACCUMULATED_VEHICLES_DEPRECIATION, true,
				null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "",
				0.0, null, true, false, openingBalances, "135", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(accumltdVechilesDepreciation);

		Account intangibles = new Account(Account.TYPE_FIXED_ASSET, "0200",
				AccounterConstants.INTANGIBLES, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "136", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(intangibles);

		Account longTermLoans = new Account(Account.TYPE_LONG_TERM_LIABILITY,
				"9001", AccounterConstants.LONG_TERM_LOANS, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "137", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(longTermLoans);

		Account hirePurchaseCreditors = new Account(
				Account.TYPE_LONG_TERM_LIABILITY, "9100",
				AccounterConstants.HIRE_PURCHASE_CREDITORS, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "138", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(hirePurchaseCreditors);

		Account deferredTax = new Account(Account.TYPE_LONG_TERM_LIABILITY,
				"9200", AccounterConstants.DEFERRED_TAX, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "139", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(deferredTax);

		Account bankRevolutions = new Account(Account.TYPE_OTHER_ASSET, "9501",
				AccounterConstants.BANK_REVALUATIONS, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "140", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(bankRevolutions);

		Account historicalAdjustment = new Account(Account.TYPE_OTHER_ASSET,
				"9510", AccounterConstants.HISTORICAL_ADJUSTMENT, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "141", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(historicalAdjustment);

		Account realisedCurrencyGains = new Account(Account.TYPE_OTHER_ASSET,
				"9520", AccounterConstants.REALISED_CURRENCY_GAINS, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "142", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(realisedCurrencyGains);

		Account unrealisedCurrencyGains = new Account(Account.TYPE_OTHER_ASSET,
				"9530", AccounterConstants.UNREALISED_CURRENCY_GAINS, true,
				null, Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "",
				0.0, null, true, false, openingBalances, "143", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(unrealisedCurrencyGains);

		Account rounding = new Account(Account.TYPE_OTHER_ASSET, "9540",
				AccounterConstants.ROUNDING, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "144", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(rounding);

		Account vatOnImports = new Account(Account.TYPE_OTHER_ASSET, "9550",
				AccounterConstants.VAT_ON_IMPORTS, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "145", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(vatOnImports);

		Account suspense = new Account(Account.TYPE_OTHER_ASSET, "9600",
				AccounterConstants.SUSPENSE, true, null,
				Account.CASH_FLOW_CATEGORY_INVESTING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "146", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(suspense);

		Account ordinaryShares = new Account(Account.TYPE_EQUITY, "3001",
				AccounterConstants.ORDINARY_SHARES, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "147", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(ordinaryShares);

		Account reservesRetainedEarnings = new Account(Account.TYPE_EQUITY,
				"3050", AccounterConstants.RESERVES_RETAINED_EARNINGS, true,
				null, Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "",
				0.0, null, true, false, openingBalances, "148", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(reservesRetainedEarnings);

		Account PAndLBoughtForwardOrYTD = new Account(Account.TYPE_EQUITY,
				"3100", AccounterConstants.P_AND_L_BOUGHT_FORWARD_OR_YTD, true,
				null, Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "",
				0.0, null, true, false, openingBalances, "149", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(PAndLBoughtForwardOrYTD);

		Account dividends = new Account(Account.TYPE_EQUITY, "3150",
				AccounterConstants.DIVIDENDS, true, null,
				Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "150", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(dividends);

		Account salesTaxVATFiled = new Account(
				Account.TYPE_OTHER_CURRENT_LIABILITY, "2122",
				AccounterConstants.SALES_TAX_VAT_FILED, true, null,
				Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", 0.0,
				null, true, false, openingBalances, "151", true,
				this.preferences.getPreventPostingBeforeDate());

		session.save(salesTaxVATFiled);

		// Account VATliabilityAccount = new Account(
		// Account.TYPE_OTHER_CURRENT_LIABILITY, "2699",
		// AccounterConstants.VAT_LIABILITY_ACCOUNT, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalances, "7", true,
		// this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(VATliabilityAccount);

		// Account salesOutputVAT = new Account(
		// Account.TYPE_OTHER_CURRENT_LIABILITY, "2000",
		// AccounterConstants.SALES_OUTPUT_VAT, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalances, "4");
		//
		// session.save(salesOutputVAT);
		//
		// Account purchaseInputVAT = new Account(
		// Account.TYPE_OTHER_CURRENT_LIABILITY, "2499",
		// AccounterConstants.PURCHASE_INPUT_VAT, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalances, "5");
		//
		// session.save(purchaseInputVAT);

		// Account pendingGoodsReceiveNotes = new Account(
		// Account.TYPE_OTHER_CURRENT_LIABILITY, "2500",
		// AccounterConstants.PENDING_GOODS_RECEIVED_NOTES, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalances, "6");
		//
		// session.save(pendingGoodsReceiveNotes);
		//

		// Account prepaidVATaccount = new Account(
		// Account.TYPE_OTHER_CURRENT_LIABILITY, "2899",
		// AccounterConstants.PREPAID_VAT_ACCOUNT, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalances, "8");
		//
		// session.save(prepaidVATaccount);
		//
		// Account ECAcquisitionVAT = new Account(
		// Account.TYPE_OTHER_CURRENT_LIABILITY, "2999",
		// AccounterConstants.EC_ACQUISITION_VAT, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalances, "9");
		//
		// session.save(ECAcquisitionVAT);

		// Account employeePayrollLiabilities = new Account(
		// Account.TYPE_PAYROLL_LIABILITY, "2110",
		// "Employee Payroll Liabilities", true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalances, "10");
		//
		// session.save(employeePayrollLiabilities);
		//
		// Account retainedEarnings = new Account(Account.TYPE_EQUITY, "3100",
		// "Retained Earnings", true, null,
		// Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalances, "12");
		//
		// session.save(retainedEarnings);
		//
		// Account earlyPaymentDiscountGiven = new Account(Account.TYPE_INCOME,
		// "4100", AccounterConstants.EARLY_PAYMENT_DISCOUNT_GIVEN, true,
		// null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
		// null, Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true,
		// true, openingBalances, "13");
		//
		// session.save(earlyPaymentDiscountGiven);
		//
		// Account writeOff = new Account(Account.TYPE_INCOME, "4200",
		// AccounterConstants.WRITE_OFF, true, null,
		// Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalances, "14");
		//
		// session.save(writeOff);
		//
		// Account earlyPaymentDiscountTaken = new Account(
		// Account.TYPE_COST_OF_GOODS_SOLD, "5000",
		// AccounterConstants.EARLY_PAYMENT_DISCOUNT_TAKEN, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalances, "15");
		//
		// session.save(earlyPaymentDiscountTaken);
		//
		// Account bankCharge = new Account(Account.TYPE_EXPENSE, "5500",
		// AccounterConstants.BANK_CHARGE, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true,
		// openingBalances, "16");
		//
		// session.save(bankCharge);
		//
		// // UK Default InActive Accounts
		// Account payrollLiabilityTax = new
		// Account(Account.TYPE_ACCOUNT_PAYABLE,
		// "2605", AccounterConstants.PAYROLL_LIABILITY_TAX, false, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true,
		// openingBalances, "17");
		//
		// session.save(payrollLiabilityTax);
		//
		// Account payrollLiabilityOther = new Account(
		// Account.TYPE_ACCOUNT_PAYABLE, "2610",
		// AccounterConstants.PAYROLL_LIABILITY_OTHER, false, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true,
		// openingBalances, "18");
		//
		// session.save(payrollLiabilityOther);
		//
		// Account payrollLiabilities = new Account(
		// Account.TYPE_PAYROLL_LIABILITY, "2200",
		// AccounterConstants.PAYROLL_LIABILITIES, false, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true,
		// openingBalances, "19");
		//
		// session.save(payrollLiabilities);
		//
		// Account payrollLiabilitiyNetPay = new Account(
		// Account.TYPE_PAYROLL_LIABILITY, "2240",
		// AccounterConstants.PAYROLL_LIABILITY_NET_PAY, false,
		// payrollLiabilities, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0,
		// false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0,
		// null, false, true, openingBalances, "19.1");
		//
		// session.save(payrollLiabilitiyNetPay);
		//
		// Account payrollExpenseEmployees = new Account(Account.TYPE_EXPENSE,
		// "7100", AccounterConstants.PAYROLL_EXPENSE_EMPLOYEES, false,
		// null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
		// null, Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false,
		// true, openingBalances, "20");
		//
		// session.save(payrollExpenseEmployees);
		//
		// Account payrollExpenseEmployeeSalary = new Account(
		// Account.TYPE_EXPENSE, "7110",
		// AccounterConstants.PAYROLL_EXPENSE_EMPLOYEE_SALARY, false,
		// payrollExpenseEmployees, Account.CASH_FLOW_CATEGORY_OPERATING,
		// 0.0, false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null,
		// 0.0, null, false, true, openingBalances, "20.1");
		//
		// session.save(payrollExpenseEmployeeSalary);
		//
		// Account payrollExpenseBonus = new Account(Account.TYPE_EXPENSE,
		// "7115",
		// AccounterConstants.PAYROLL_EXPENSE_BONUSES, false,
		// payrollExpenseEmployees, Account.CASH_FLOW_CATEGORY_OPERATING,
		// 0.0, false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null,
		// 0.0, null, false, true, openingBalances, "20.2");
		//
		// session.save(payrollExpenseBonus);
		//
		// Account payrollExpenseSSP_SMP_SPP_SAP = new Account(
		// Account.TYPE_EXPENSE, "7125",
		// AccounterConstants.PAYROLL_EXPENSE_SSP_SMP_SPP_SAP, false,
		// payrollExpenseEmployees, Account.CASH_FLOW_CATEGORY_OPERATING,
		// 0.0, false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null,
		// 0.0, null, false, true, openingBalances, "20.3");
		//
		// session.save(payrollExpenseSSP_SMP_SPP_SAP);
		//
		// Account payrollChargeExpense = new Account(Account.TYPE_EXPENSE,
		// "7170", AccounterConstants.PAYROLL_CHARGE_EXPENSE, false, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true,
		// openingBalances, "21");
		//
		// session.save(payrollChargeExpense);
		//
		// Account payrollChargeExpNIEmployer = new
		// Account(Account.TYPE_EXPENSE,
		// "7175", AccounterConstants.PAYROLL_CHARGE_EXP_NI_EMPLOYER,
		// false, payrollChargeExpense,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true,
		// openingBalances, "21.1");
		//
		// session.save(payrollChargeExpNIEmployer);
		//
		// Account payrollChargeExpPensionEmployer = new Account(
		// Account.TYPE_EXPENSE, "7180",
		// AccounterConstants.PAYROLL_CHARGE_EXP_PENSION_EMPLOYER, false,
		// payrollChargeExpense, Account.CASH_FLOW_CATEGORY_OPERATING,
		// 0.0, false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null,
		// 0.0, null, false, true, openingBalances, "21.2");
		//
		// session.save(payrollChargeExpPensionEmployer);
		//
		// Account ECSale = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
		// "9999", AccounterConstants.EC_SALE, false, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalances, "22");
		//
		// session.save(ECSale);

		this.accountsReceivableAccount = accountsReceivable;
		this.accountsPayableAccount = accountsPayable;
		this.openingBalancesAccount = openingBalances;
		this.retainedEarningsAccount = reservesRetainedEarnings;
		this.VATliabilityAccount = saelsTaxVAT;
		this.VATFiledLiabilityAccount = salesTaxVATFiled;
		// this.pendingItemReceiptsAccount = pendingItemReceipts;
		// this.prepaidVATaccount = prepaidVATaccount;
		// this.ECAcquisitionVATaccount = ECAcquisitionVAT;

		setDefaultsUKValues(session);
		createUKDefaultVATCodesAndVATAgency(session);
		createNominalCodesRanges(session);
		createDefaultBrandingTheme(session);

	}

	/**
	 * Creates all the nominal code ranges for all the default accounts in the
	 * company. It sets the minimum range and maximum range of the nominal codes
	 * 
	 * @param session
	 */

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

	private void setDefaultsUKValues(Session session) {

		// Session session = HibernateUtil.getCurrentSession();
		// Create Default UK Payment Terms

		// PaymentTerms onePercentTenNetThirty = new PaymentTerms(
		// AccounterConstants.PM_ONE_PERCENT_TEN_NET_THIRTY,
		// AccounterConstants.DISCOUNT_ONEPERCENT_IF_PAID_WITHIN_TENDAYS,
		// 10, 1, PaymentTerms.DUE_NONE, 30, true);
		//
		// session.save(onePercentTenNetThirty);
		//
		// PaymentTerms twoPercentTenNetThirty = new PaymentTerms(
		// AccounterConstants.PM_TWO_PERCENT_TEN_NET_THIRTY,
		// AccounterConstants.DISCOUNT_TWOPERCENT_IF_PAID_WITHIN_TENDAYS,
		// 10, 2, PaymentTerms.DUE_NONE, 30, true);
		//
		// session.save(twoPercentTenNetThirty);
		//
		// PaymentTerms netFifteen = new PaymentTerms(
		// AccounterConstants.PM_NET_FIFTEEN,
		// AccounterConstants.PAY_WITH_IN_FIFTEEN_DAYS, 0, 0,
		// PaymentTerms.DUE_NONE, 15, true);
		//
		// session.save(netFifteen);

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

		// PaymentTerms quarterlyPayrollLiability = new PaymentTerms(
		// AccounterConstants.PM_QUARTERLY_PAYROLL_LIABILITY,
		// AccounterConstants.PM_QUARTERLY_PAYROLL_LIABILITY, 0, 0,
		// PaymentTerms.DUE_PAYROLL_TAX_QUARTER, 13, true);
		//
		// session.save(quarterlyPayrollLiability);

		// Current Fiscal Year creation

		VendorGroup creditCardCompanies = new VendorGroup();
		creditCardCompanies.setName(AccounterConstants.CREDIT_CARD_COMPANIES);
		creditCardCompanies.setDefault(true);
		session.save(creditCardCompanies);

	}

	public void createUKDefaultVATCodesAndVATAgency(Session session) {

		try {
			VATReturnBox vt1 = new VATReturnBox();
			vt1.setName(AccounterConstants.UK_EC_PURCHASES_GOODS);
			vt1.setVatBox(AccounterConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS);
			vt1.setTotalBox(AccounterConstants.UK_BOX9_TOTAL_NET_ACQUISITIONS);
			vt1.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt1);

			VATReturnBox vt3 = new VATReturnBox();
			vt3.setName(AccounterConstants.UK_EC_SALES_GOODS);
			vt3.setVatBox(AccounterConstants.BOX_NONE);
			vt3.setTotalBox(AccounterConstants.UK_BOX8_TOTAL_NET_SUPPLIES);
			vt3.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt3);

			VATReturnBox vt4 = new VATReturnBox();
			vt4.setName(AccounterConstants.UK_EC_SALES_SERVICES);
			vt4.setVatBox(AccounterConstants.BOX_NONE);
			vt4.setTotalBox(AccounterConstants.UK_BOX6_TOTAL_NET_SALES);
			vt4.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt4);

			VATReturnBox vt5 = new VATReturnBox();
			vt5.setName(AccounterConstants.UK_DOMESTIC_PURCHASES);
			vt5.setVatBox(AccounterConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES);
			vt5.setTotalBox(AccounterConstants.UK_BOX7_TOTAL_NET_PURCHASES);
			vt5.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt5);

			VATReturnBox vt6 = new VATReturnBox();
			vt6.setName(AccounterConstants.UK_DOMESTIC_SALES);
			vt6.setVatBox(AccounterConstants.UK_BOX1_VAT_DUE_ON_SALES);
			vt6.setTotalBox(AccounterConstants.UK_BOX6_TOTAL_NET_SALES);
			vt6.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt6);

			VATReturnBox vt7 = new VATReturnBox();
			vt7.setName(AccounterConstants.UK_NOT_REGISTERED_PURCHASES);
			vt7.setVatBox(AccounterConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES);
			vt7.setTotalBox(AccounterConstants.UK_BOX7_TOTAL_NET_PURCHASES);
			vt7.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt7);

			VATReturnBox vt8 = new VATReturnBox();
			vt8.setName(AccounterConstants.UK_NOT_REGISTERED_SALES);
			vt8.setVatBox(AccounterConstants.UK_BOX1_VAT_DUE_ON_SALES);
			vt8.setTotalBox(AccounterConstants.UK_BOX6_TOTAL_NET_SALES);
			vt8.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt8);

			VATReturnBox vt11 = new VATReturnBox();
			vt11.setName(AccounterConstants.UK_REVERSE_CHARGE);
			vt11.setVatBox(AccounterConstants.UK_BOX1_VAT_DUE_ON_SALES);
			vt11.setTotalBox(AccounterConstants.BOX_NONE);
			vt11.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt11);

			// /// For Ireland VAT Return type boxes

			VATReturnBox vt20 = new VATReturnBox();
			vt20.setName(AccounterConstants.IRELAND_DOMESTIC_SALES);
			vt20.setVatBox(AccounterConstants.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES);
			vt20.setTotalBox(AccounterConstants.IRELAND_BOX8_TOTAL_NET_SALES);
			vt20.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt20);

			VATReturnBox vt21 = new VATReturnBox();
			vt21.setName(AccounterConstants.IRELAND_DOMESTIC_PURCHASES);
			vt21.setVatBox(AccounterConstants.IRELAND_BOX4_VAT_ON_PURCHASES);
			vt21.setTotalBox(AccounterConstants.IRELAND_BOX9_TOTAL_NET_PURCHASES);
			vt21.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt21);

			VATReturnBox vt22 = new VATReturnBox();
			vt22.setName(AccounterConstants.IRELAND_EC_SALES_GOODS);
			vt22.setVatBox(AccounterConstants.BOX_NONE);
			vt22.setTotalBox(AccounterConstants.IRELAND_BOX6_E1_GOODS_TO_EU);
			vt22.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt22);

			VATReturnBox vt23 = new VATReturnBox();
			vt23.setName(AccounterConstants.IRELAND_EC_PURCHASES_GOODS);
			vt23.setVatBox(AccounterConstants.IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS);
			vt23.setTotalBox(AccounterConstants.IRELAND_BOX7_E2_GOODS_FROM_EU);
			vt23.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt23);

			VATReturnBox vt24 = new VATReturnBox();
			vt24.setName(AccounterConstants.IRELAND_EXEMPT_SALES);
			vt24.setVatBox(AccounterConstants.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES);
			vt24.setTotalBox(AccounterConstants.IRELAND_BOX8_TOTAL_NET_SALES);
			vt24.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt24);

			VATReturnBox vt25 = new VATReturnBox();
			vt25.setName(AccounterConstants.IRELAND_EXEMPT_PURCHASES);
			vt25.setVatBox(AccounterConstants.IRELAND_BOX7_E2_GOODS_FROM_EU);
			vt25.setTotalBox(AccounterConstants.IRELAND_BOX9_TOTAL_NET_PURCHASES);
			vt25.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt25);

			VATReturnBox vt26 = new VATReturnBox();
			vt26.setName(AccounterConstants.IRELAND_NOT_REGISTERED_SALES);
			vt26.setVatBox(AccounterConstants.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES);
			vt26.setTotalBox(AccounterConstants.IRELAND_BOX8_TOTAL_NET_SALES);
			vt26.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt26);

			VATReturnBox vt27 = new VATReturnBox();
			vt27.setName(AccounterConstants.IRELAND_NOT_REGISTERED_PURCHASES);
			vt27.setVatBox(AccounterConstants.IRELAND_BOX4_VAT_ON_PURCHASES);
			vt27.setTotalBox(AccounterConstants.IRELAND_BOX9_TOTAL_NET_PURCHASES);
			vt27.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt27);

			// Session session = HibernateUtil.getCurrentSession();

			TAXAgency defaultVATAgency = new TAXAgency();
			defaultVATAgency.setActive(Boolean.TRUE);
			defaultVATAgency.setName(getPreferences().getVATtaxAgencyName());
			defaultVATAgency.setVATReturn(VATReturn.VAT_RETURN_UK_VAT);

			defaultVATAgency.setSalesLiabilityAccount((Account) session
					.getNamedQuery("unique.name.Account")
					.setString(0, AccounterConstants.SALES_TAX_VAT_UNFILED)
					.list().get(0));

			defaultVATAgency.setPurchaseLiabilityAccount((Account) session
					.getNamedQuery("unique.name.Account")
					.setString(0, AccounterConstants.SALES_TAX_VAT_UNFILED)
					.list().get(0));

			defaultVATAgency.setDefault(true);

			session.save(defaultVATAgency);

			TAXItem vatItem1 = new TAXItem();
			vatItem1.setName("EC Purchases Goods Standard");
			vatItem1.setActive(true);
			vatItem1.setDescription("EC Purchases of Goods Standard");
			vatItem1.setTaxRate(17.5);
			vatItem1.setSalesType(false);
			vatItem1.setTaxAgency(defaultVATAgency);

			vatItem1.setVatReturnBox(vt1);
			vatItem1.setDefault(true);
			vatItem1.setPercentage(true);
			session.save(vatItem1);

			TAXItem vatItem2 = new TAXItem();
			vatItem2.setName("EC Purchases Goods Zero-Rated");
			vatItem2.setActive(true);
			vatItem2.setDescription("EC Purchases of Goods Zero-Rated");
			vatItem2.setTaxRate(0.0);
			vatItem2.setSalesType(false);
			vatItem2.setTaxAgency(defaultVATAgency);
			vatItem2.setPercentage(true);
			vatItem2.setVatReturnBox(vt1);
			vatItem2.setDefault(true);
			session.save(vatItem2);

			TAXItem vatItem3 = new TAXItem();
			vatItem3.setName("EC Sales Goods Standard");
			vatItem3.setActive(true);
			vatItem3.setDescription("EC Sales of Goods Standard");
			vatItem3.setTaxRate(0.0);
			vatItem3.setSalesType(true);
			vatItem3.setTaxAgency(defaultVATAgency);
			vatItem3.setPercentage(true);
			vatItem3.setVatReturnBox(vt3);
			vatItem3.setDefault(true);
			session.save(vatItem3);

			TAXItem vatItem4 = new TAXItem();
			vatItem4.setName("EC Sales Services Standard");
			vatItem4.setActive(true);
			vatItem4.setDescription("EC Sales of Services Standard");
			vatItem4.setTaxRate(0.0);
			vatItem4.setSalesType(true);
			vatItem4.setTaxAgency(defaultVATAgency);
			vatItem4.setVatReturnBox(vt4);
			vatItem4.setDefault(true);
			vatItem4.setPercentage(true);
			session.save(vatItem4);

			TAXItem vatItem5 = new TAXItem();
			vatItem5.setName("Exempt Purchases");
			vatItem5.setActive(true);
			vatItem5.setDescription("Exempt Purchases");
			vatItem5.setTaxRate(0.0);
			vatItem5.setSalesType(false);
			vatItem5.setTaxAgency(defaultVATAgency);
			vatItem5.setVatReturnBox(vt5);
			vatItem5.setDefault(true);
			vatItem5.setPercentage(true);
			session.save(vatItem5);

			TAXItem vatItem6 = new TAXItem();
			vatItem6.setName("Exempt Sales");
			vatItem6.setActive(true);
			vatItem6.setDescription("Exempt Sales");
			vatItem6.setTaxRate(0.0);
			vatItem6.setSalesType(true);
			vatItem6.setTaxAgency(defaultVATAgency);
			vatItem6.setVatReturnBox(vt6);
			vatItem6.setDefault(true);
			vatItem6.setPercentage(true);
			session.save(vatItem6);

			TAXItem vatItem7 = new TAXItem();
			vatItem7.setName("Not Registered Purchases");
			vatItem7.setActive(true);
			vatItem7.setSalesType(false);
			vatItem7.setDescription("Not Registered Purchases");
			vatItem7.setTaxRate(0.0);
			vatItem7.setTaxAgency(defaultVATAgency);
			vatItem7.setPercentage(true);
			// session.save(vt7);
			vatItem7.setVatReturnBox(vt7);
			vatItem7.setDefault(true);
			session.save(vatItem7);

			TAXItem vatItem8 = new TAXItem();
			vatItem8.setName("Not Registered Sales");
			vatItem8.setActive(true);
			vatItem8.setDescription("Not Registered Sales");
			vatItem8.setTaxRate(0.0);
			vatItem8.setSalesType(true);
			vatItem8.setTaxAgency(defaultVATAgency);
			vatItem8.setPercentage(true);
			// session.save(vt8);
			vatItem8.setVatReturnBox(vt8);
			vatItem8.setDefault(true);
			session.save(vatItem8);

			TAXItem vatItem9 = new TAXItem();
			vatItem9.setName("Reduced Purchases");
			vatItem9.setActive(true);
			vatItem9.setSalesType(false);
			vatItem9.setDescription("Reduced Purchases");
			vatItem9.setTaxRate(5.0);
			vatItem9.setTaxAgency(defaultVATAgency);
			vatItem9.setVatReturnBox(vt5);
			vatItem9.setDefault(true);
			vatItem9.setPercentage(true);
			session.save(vatItem9);

			TAXItem vatItem10 = new TAXItem();
			vatItem10.setName("Reduced Sales");
			vatItem10.setActive(true);
			vatItem10.setDescription("Reduced Sales");
			vatItem10.setTaxRate(5.0);
			vatItem10.setSalesType(true);
			vatItem10.setTaxAgency(defaultVATAgency);
			vatItem10.setVatReturnBox(vt6);
			vatItem10.setDefault(true);
			vatItem10.setPercentage(true);
			session.save(vatItem10);

			TAXItem vatItem11 = new TAXItem();
			vatItem11.setName("Reverse Charge Purchases Standard");
			vatItem11.setActive(true);
			vatItem11.setDescription("Reverse Charge Purchases Standard");
			vatItem11.setTaxRate(17.5);
			vatItem11.setSalesType(false);
			vatItem11.setTaxAgency(defaultVATAgency);
			vatItem11.setVatReturnBox(vt11);
			vatItem11.setPercentage(true);
			vatItem11.setDefault(true);
			session.save(vatItem11);

			TAXItem vatItem12 = new TAXItem();
			vatItem12.setName("Standard Purchases");
			vatItem12.setActive(true);
			vatItem12.setDescription("Standard Purchases");
			vatItem12.setTaxRate(17.5);
			vatItem12.setSalesType(false);
			vatItem12.setTaxAgency(defaultVATAgency);
			vatItem12.setVatReturnBox(vt5);
			vatItem12.setDefault(true);
			vatItem12.setPercentage(true);
			session.save(vatItem12);

			TAXItem vatItem13 = new TAXItem();
			vatItem13.setName("Standard Sales");
			vatItem13.setActive(true);
			vatItem13.setDescription("Standard Sales");
			vatItem13.setTaxRate(17.5);
			vatItem13.setSalesType(true);
			vatItem13.setTaxAgency(defaultVATAgency);
			vatItem13.setVatReturnBox(vt6);
			vatItem13.setDefault(true);
			vatItem13.setPercentage(true);
			session.save(vatItem13);

			TAXItem vatItem14 = new TAXItem();
			vatItem14.setName("Zero-Rated Purchases");
			vatItem14.setActive(true);
			vatItem14.setSalesType(false);
			vatItem14.setDescription("Zero-Rated Purchases");
			vatItem14.setTaxRate(0.0);
			vatItem14.setTaxAgency(defaultVATAgency);
			vatItem14.setVatReturnBox(vt5);
			vatItem14.setDefault(true);
			vatItem14.setPercentage(true);
			session.save(vatItem14);

			TAXItem vatItem15 = new TAXItem();
			vatItem15.setName("Zero-Rated Sales");
			vatItem15.setActive(true);
			vatItem15.setDescription("Zero-Rated Sales");
			vatItem15.setTaxRate(0.0);
			vatItem15.setSalesType(true);
			vatItem15.setTaxAgency(defaultVATAgency);
			vatItem15.setVatReturnBox(vt6);
			vatItem15.setDefault(true);
			vatItem15.setPercentage(true);
			session.save(vatItem15);

			TAXItem vatItem16 = new TAXItem();
			vatItem16.setName("New Standard Purchases");
			vatItem16.setActive(true);
			vatItem16.setDescription("New Standard Purchases");
			vatItem16.setTaxRate(20.0);
			vatItem16.setSalesType(false);
			vatItem16.setTaxAgency(defaultVATAgency);
			vatItem16.setVatReturnBox(vt5);
			vatItem16.setDefault(true);
			vatItem16.setPercentage(true);
			session.save(vatItem16);

			TAXItem vatItem17 = new TAXItem();
			vatItem17.setName("New Standard Sales");
			vatItem17.setActive(true);
			vatItem17.setDescription("New Standard Sales");
			vatItem17.setTaxRate(20.0);
			vatItem17.setSalesType(true);
			vatItem17.setTaxAgency(defaultVATAgency);
			vatItem17.setVatReturnBox(vt6);
			vatItem17.setDefault(true);
			vatItem17.setPercentage(true);
			session.save(vatItem17);

			// VATGroup vatGroup1 = new VATGroup();
			// =======
			TAXGroup vatGroup1 = new TAXGroup();
			// >>>>>>> .merge-right.r20318
			vatGroup1.setName("EC Purchases Goods 0% Group");
			vatGroup1.setDescription("EC Purchases of Goods Zero-Rated Group");
			vatGroup1.setActive(true);
			vatGroup1.setSalesType(false);
			vatGroup1.setGroupRate(0.0);
			List<TAXItem> vatItms1 = new ArrayList<TAXItem>();
			vatItms1.add(vatItem2);
			vatItms1.add(vatItem14);
			vatGroup1.setTAXItems(vatItms1);
			vatGroup1.setDefault(true);
			session.save(vatGroup1);

			TAXGroup vatGroup2 = new TAXGroup();
			vatGroup2.setName("EC Purchases Goods 17.5% Group");
			vatGroup2.setDescription("EC Purchases of Goods Group");
			vatGroup2.setActive(true);
			vatGroup2.setGroupRate(17.5);
			vatGroup2.setSalesType(false);
			List<TAXItem> vatItms2 = new ArrayList<TAXItem>();
			vatItms2.add(vatItem12);
			vatItms2.add(vatItem1);
			vatGroup2.setTAXItems(vatItms2);
			vatGroup2.setDefault(true);
			session.save(vatGroup2);

			TAXGroup vatGroup3 = new TAXGroup();
			vatGroup3.setName("EC Sales Goods 0% Group");
			vatGroup3.setDescription("EC Sales of Goods Group");
			vatGroup3.setActive(true);
			vatGroup3.setSalesType(true);
			vatGroup3.setGroupRate(0.0);
			List<TAXItem> vatItms3 = new ArrayList<TAXItem>();
			vatItms3.add(vatItem4);
			vatItms3.add(vatItem3);
			vatGroup3.setTAXItems(vatItms3);
			vatGroup3.setDefault(true);
			vatGroup3.setPercentage((vatItem3.isPercentage() && vatItem4
					.isPercentage()) ? true : false);
			session.save(vatGroup3);

			TAXGroup vatGroup4 = new TAXGroup();
			vatGroup4.setName("Reverse Charge Purchases 17.5% Group");
			vatGroup4.setDescription("Reverse Charge Purchases Group");
			vatGroup4.setActive(true);
			vatGroup4.setSalesType(false);
			vatGroup4.setGroupRate(17.5);
			List<TAXItem> vatItms4 = new ArrayList<TAXItem>();
			vatItms4.add(vatItem12);
			vatItms4.add(vatItem11);
			vatGroup4.setTAXItems(vatItms4);
			vatGroup4.setDefault(true);
			session.save(vatGroup4);

			TAXCode vatCode1 = new TAXCode();
			vatCode1.setName("E");
			vatCode1.setDescription("Exempt");
			vatCode1.setTaxable(true);
			vatCode1.setActive(true);
			vatCode1.setTAXItemGrpForPurchases(vatItem5);
			vatCode1.setTAXItemGrpForSales(vatItem6);
			vatCode1.setDefault(true);
			session.save(vatCode1);

			TAXCode vatCode2 = new TAXCode();
			vatCode2.setName("EGS");
			vatCode2.setDescription("EC Goods Standard (17.5%)");
			vatCode2.setTaxable(true);
			vatCode2.setActive(true);
			vatCode2.setTAXItemGrpForPurchases(vatGroup2);
			vatCode2.setTAXItemGrpForSales(vatGroup3);
			vatCode2.setDefault(true);
			vatCode2.setECSalesEntry(true);
			session.save(vatCode2);

			TAXCode vatCode3 = new TAXCode();
			vatCode3.setName("EGZ");
			vatCode3.setDescription("EC Goods Zero-Rated (0%)");
			vatCode3.setTaxable(true);
			vatCode3.setActive(true);
			vatCode3.setTAXItemGrpForPurchases(vatGroup1);
			vatCode3.setDefault(true);
			vatCode3.setTAXItemGrpForSales(null);

			session.save(vatCode3);

			TAXCode vatCode4 = new TAXCode();
			vatCode4.setName("N");
			vatCode4.setDescription("Not Registered");
			vatCode4.setTaxable(true);
			vatCode4.setActive(true);
			vatCode4.setTAXItemGrpForPurchases(vatItem7);
			vatCode4.setTAXItemGrpForSales(vatItem8);
			vatCode4.setDefault(true);
			session.save(vatCode4);

			TAXCode vatCode5 = new TAXCode();
			vatCode5.setName("R");
			vatCode5.setDescription("Reduced (5%)");
			vatCode5.setTaxable(true);
			vatCode5.setActive(true);
			vatCode5.setTAXItemGrpForPurchases(vatItem9);
			vatCode5.setTAXItemGrpForSales(vatItem10);
			vatCode5.setDefault(true);
			session.save(vatCode5);

			TAXCode vatCode6 = new TAXCode();
			vatCode6.setName("RC");
			vatCode6.setDescription("Reverse Charge");
			vatCode6.setTaxable(true);
			vatCode6.setActive(true);
			vatCode6.setTAXItemGrpForPurchases(vatGroup4);
			// vatCode6.setVATItemGrpForSales(vatItem4);
			vatCode6.setDefault(true);
			vatCode6.setECSalesEntry(true);
			session.save(vatCode6);

			TAXCode vatCode7 = new TAXCode();
			vatCode7.setName("S");
			vatCode7.setDescription("Standard (17.5%)");
			vatCode7.setTaxable(true);
			vatCode7.setActive(true);
			vatCode7.setTAXItemGrpForPurchases(vatItem12);
			vatCode7.setTAXItemGrpForSales(vatItem13);
			vatCode7.setDefault(true);
			session.save(vatCode7);

			TAXCode vatCode8 = new TAXCode();
			vatCode8.setName("Z");
			vatCode8.setDescription("Zero-Rated (0%)");
			vatCode8.setTaxable(true);
			vatCode8.setActive(true);
			vatCode8.setTAXItemGrpForPurchases(vatItem14);
			vatCode8.setTAXItemGrpForSales(vatItem15);
			vatCode8.setDefault(true);
			session.save(vatCode8);

			TAXCode vatCode9 = new TAXCode();
			vatCode9.setName("O");
			vatCode9.setDescription("Outside the Scope of VAT");
			vatCode9.setTaxable(false);
			vatCode9.setActive(true);
			vatCode9.setTAXItemGrpForPurchases(null);
			vatCode9.setTAXItemGrpForSales(null);
			vatCode9.setDefault(true);
			session.save(vatCode9);

			TAXCode vatCode10 = new TAXCode();
			vatCode10.setName("New S");
			vatCode10.setDescription("Standard (20%)");
			vatCode10.setTaxable(true);
			vatCode10.setActive(true);
			vatCode10.setTAXItemGrpForPurchases(vatItem16);
			vatCode10.setTAXItemGrpForSales(vatItem17);
			vatCode10.setDefault(true);
			session.save(vatCode10);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void createDefaultBrandingTheme(Session session) {
		BrandingTheme brandingTheme = new BrandingTheme("Standard",
				SecureUtils.createID(), 1.35, 1.00, 1.00, "Times New Roman",
				"10pt", "INVOICE", "CREDIT", "STATEMENT", "democo@democo.co",
				true, this.getName(), "(None Added)");
		session.save(brandingTheme);
	}

	@Override
	public Company getCompany() {

		return null;
	}

	@Override
	public void init() {
		Session session = HibernateUtil.getCurrentSession();
		initDefaultUKAccounts(session);
		// createUKDefaultVATCodesAndVATAgency(session);

	}

	/*
	 * @Override public void office_expense() {
	 * 
	 * 
	 * }
	 * 
	 * @Override public void motor_veichel_expense() {
	 * 
	 * 
	 * }
	 * 
	 * @Override public void travel_expenses() {
	 * 
	 * 
	 * }
	 * 
	 * @Override public void other_expenses() {
	 * 
	 * 
	 * }
	 * 
	 * @Override public void Cost_of_good_sold() {
	 * 
	 * 
	 * }
	 * 
	 * @Override public void other_direct_cost() {
	 * 
	 * 
	 * }
	 */

}
