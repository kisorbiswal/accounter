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

	// Account prepaidVATaccount;
	// Account ECAcquisitionVATaccount;

	// Account pendingItemReceiptsAccount;

	Set<NominalCodeRange> nominalCodeRange = new HashSet<NominalCodeRange>();
	/**
	 * Name of the Company
	 */
	String name;// Trading name

	/**
	 * Creates new Instance
	 */
	public UKCompanyInitializer(Company company) {
		super(company);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
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

	private void initDefaultUKAccounts() {
		// FinanceDate currentDate = new FinanceDate();
		// FinanceDate fiscalYearStartDate = new FinanceDate(
		// (int) currentDate.getYear(), 0, 1);
		// FinanceDate fiscalYearEndDate = new FinanceDate(
		// (int) currentDate.getYear(), 11, 31);
		//
		// FiscalYear fiscalYear = new FiscalYear(fiscalYearStartDate,
		// fiscalYearEndDate, FiscalYear.STATUS_OPEN, Boolean.TRUE);
		// String dateFormat = AccounterConstants.ddMMyyyy;
		//
		// session.save(fiscalYear);

		// This is the Account created by default for the purpose of UK VAT
		Account vATliabilityAccount = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.SALES_TAX_VAT_UNFILED,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		createAccount(Account.TYPE_OTHER_ASSET,
				AccounterServerConstants.VAT_ON_IMPORTS,
				Account.CASH_FLOW_CATEGORY_INVESTING);

		// This is the Account created by default for the purpose of UK when VAT
		// is Filed

		Account vATFiledLiabilityAccount = createAccount(
				Account.TYPE_OTHER_CURRENT_LIABILITY,
				AccounterServerConstants.SALES_TAX_VAT_FILED,
				Account.CASH_FLOW_CATEGORY_OPERATING);

		// Account VATliabilityAccount = new Account(
		// Account.TYPE_OTHER_CURRENT_LIABILITY, "2699",
		// AccounterConstants.VAT_LIABILITY_ACCOUNT, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalancesAccount, "7", true,
		// this.preferences.getPreventPostingBeforeDate());
		//
		// session.save(VATliabilityAccount);

		// Account salesOutputVAT = new Account(
		// Account.TYPE_OTHER_CURRENT_LIABILITY, "2000",
		// AccounterConstants.SALES_OUTPUT_VAT, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalancesAccount, "4");
		//
		// session.save(salesOutputVAT);
		//
		// Account purchaseInputVAT = new Account(
		// Account.TYPE_OTHER_CURRENT_LIABILITY, "2499",
		// AccounterConstants.PURCHASE_INPUT_VAT, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalancesAccount, "5");
		//
		// session.save(purchaseInputVAT);

		// Account pendingGoodsReceiveNotes = new Account(
		// Account.TYPE_OTHER_CURRENT_LIABILITY, "2500",
		// AccounterConstants.PENDING_GOODS_RECEIVED_NOTES, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalancesAccount, "6");
		//
		// session.save(pendingGoodsReceiveNotes);
		//

		// Account prepaidVATaccount = new Account(
		// Account.TYPE_OTHER_CURRENT_LIABILITY, "2899",
		// AccounterConstants.PREPAID_VAT_ACCOUNT, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalancesAccount, "8");
		//
		// session.save(prepaidVATaccount);
		//
		// Account ECAcquisitionVAT = new Account(
		// Account.TYPE_OTHER_CURRENT_LIABILITY, "2999",
		// AccounterConstants.EC_ACQUISITION_VAT, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalancesAccount, "9");
		//
		// session.save(ECAcquisitionVAT);

		// Account employeePayrollLiabilities = new Account(
		// Account.TYPE_PAYROLL_LIABILITY, "2110",
		// "Employee Payroll Liabilities", true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalancesAccount, "10");
		//
		// session.save(employeePayrollLiabilities);
		//
		// Account retainedEarnings = new Account(Account.TYPE_EQUITY, "3100",
		// "Retained Earnings", true, null,
		// Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalancesAccount, "12");
		//
		// session.save(retainedEarnings);
		//
		// Account earlyPaymentDiscountGiven = new Account(Account.TYPE_INCOME,
		// "4100", AccounterConstants.EARLY_PAYMENT_DISCOUNT_GIVEN, true,
		// null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
		// null, Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true,
		// true, openingBalancesAccount, "13");
		//
		// session.save(earlyPaymentDiscountGiven);
		//
		// Account writeOff = new Account(Account.TYPE_INCOME, "4200",
		// AccounterConstants.WRITE_OFF, true, null,
		// Account.CASH_FLOW_CATEGORY_FINANCING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalancesAccount, "14");
		//
		// session.save(writeOff);
		//
		// Account earlyPaymentDiscountTaken = new Account(
		// Account.TYPE_COST_OF_GOODS_SOLD, "5000",
		// AccounterConstants.EARLY_PAYMENT_DISCOUNT_TAKEN, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalancesAccount, "15");
		//
		// session.save(earlyPaymentDiscountTaken);
		//
		// Account bankCharge = new Account(Account.TYPE_EXPENSE, "5500",
		// AccounterConstants.BANK_CHARGE, true, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true,
		// openingBalancesAccount, "16");
		//
		// session.save(bankCharge);
		//
		// // UK Default InActive Accounts
		// Account payrollLiabilityTax = new
		// Account(Account.TYPE_ACCOUNT_PAYABLE,
		// "2605", AccounterConstants.PAYROLL_LIABILITY_TAX, false, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true,
		// openingBalancesAccount, "17");
		//
		// session.save(payrollLiabilityTax);
		//
		// Account payrollLiabilityOther = new Account(
		// Account.TYPE_ACCOUNT_PAYABLE, "2610",
		// AccounterConstants.PAYROLL_LIABILITY_OTHER, false, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true,
		// openingBalancesAccount, "18");
		//
		// session.save(payrollLiabilityOther);
		//
		// Account payrollLiabilities = new Account(
		// Account.TYPE_PAYROLL_LIABILITY, "2200",
		// AccounterConstants.PAYROLL_LIABILITIES, false, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true,
		// openingBalancesAccount, "19");
		//
		// session.save(payrollLiabilities);
		//
		// Account payrollLiabilitiyNetPay = new Account(
		// Account.TYPE_PAYROLL_LIABILITY, "2240",
		// AccounterConstants.PAYROLL_LIABILITY_NET_PAY, false,
		// payrollLiabilities, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0,
		// false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0,
		// null, false, true, openingBalancesAccount, "19.1");
		//
		// session.save(payrollLiabilitiyNetPay);
		//
		// Account payrollExpenseEmployees = new Account(Account.TYPE_EXPENSE,
		// "7100", AccounterConstants.PAYROLL_EXPENSE_EMPLOYEES, false,
		// null, Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "",
		// null, Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false,
		// true, openingBalancesAccount, "20");
		//
		// session.save(payrollExpenseEmployees);
		//
		// Account payrollExpenseEmployeeSalary = new Account(
		// Account.TYPE_EXPENSE, "7110",
		// AccounterConstants.PAYROLL_EXPENSE_EMPLOYEE_SALARY, false,
		// payrollExpenseEmployees, Account.CASH_FLOW_CATEGORY_OPERATING,
		// 0.0, false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null,
		// 0.0, null, false, true, openingBalancesAccount, "20.1");
		//
		// session.save(payrollExpenseEmployeeSalary);
		//
		// Account payrollExpenseBonus = new Account(Account.TYPE_EXPENSE,
		// "7115",
		// AccounterConstants.PAYROLL_EXPENSE_BONUSES, false,
		// payrollExpenseEmployees, Account.CASH_FLOW_CATEGORY_OPERATING,
		// 0.0, false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null,
		// 0.0, null, false, true, openingBalancesAccount, "20.2");
		//
		// session.save(payrollExpenseBonus);
		//
		// Account payrollExpenseSSP_SMP_SPP_SAP = new Account(
		// Account.TYPE_EXPENSE, "7125",
		// AccounterConstants.PAYROLL_EXPENSE_SSP_SMP_SPP_SAP, false,
		// payrollExpenseEmployees, Account.CASH_FLOW_CATEGORY_OPERATING,
		// 0.0, false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null,
		// 0.0, null, false, true, openingBalancesAccount, "20.3");
		//
		// session.save(payrollExpenseSSP_SMP_SPP_SAP);
		//
		// Account payrollChargeExpense = new Account(Account.TYPE_EXPENSE,
		// "7170", AccounterConstants.PAYROLL_CHARGE_EXPENSE, false, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true,
		// openingBalancesAccount, "21");
		//
		// session.save(payrollChargeExpense);
		//
		// Account payrollChargeExpNIEmployer = new
		// Account(Account.TYPE_EXPENSE,
		// "7175", AccounterConstants.PAYROLL_CHARGE_EXP_NI_EMPLOYER,
		// false, payrollChargeExpense,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, false, true,
		// openingBalancesAccount, "21.1");
		//
		// session.save(payrollChargeExpNIEmployer);
		//
		// Account payrollChargeExpPensionEmployer = new Account(
		// Account.TYPE_EXPENSE, "7180",
		// AccounterConstants.PAYROLL_CHARGE_EXP_PENSION_EMPLOYER, false,
		// payrollChargeExpense, Account.CASH_FLOW_CATEGORY_OPERATING,
		// 0.0, false, "", null, Account.BANK_ACCCOUNT_TYPE_NONE, null,
		// 0.0, null, false, true, openingBalancesAccount, "21.2");
		//
		// session.save(payrollChargeExpPensionEmployer);
		//
		// Account ECSale = new Account(Account.TYPE_OTHER_CURRENT_LIABILITY,
		// "9999", AccounterConstants.EC_SALE, false, null,
		// Account.CASH_FLOW_CATEGORY_OPERATING, 0.0, false, "", null,
		// Account.BANK_ACCCOUNT_TYPE_NONE, null, 0.0, null, true, true,
		// openingBalancesAccount, "22");
		//
		// session.save(ECSale);

		company.setTaxLiabilityAccount(vATliabilityAccount);
		company.setVATFiledLiabilityAccount(vATFiledLiabilityAccount);
		// this.pendingItemReceiptsAccount = pendingItemReceipts;
		// this.prepaidVATaccount = prepaidVATaccount;
		// this.ECAcquisitionVATaccount = ECAcquisitionVAT;

		// setDefaultsUKValues(session);
		createUKDefaultVATCodesAndVATAgency();
		// createNominalCodesRanges(session);
		// createDefaultBrandingTheme(session);

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
				PaymentTerms.DUE_CURRENT_MONTH, 13, true);

		session.save(monthlyPayrollLiability);

		// PaymentTerms quarterlyPayrollLiability = new PaymentTerms(
		// AccounterConstants.PM_QUARTERLY_PAYROLL_LIABILITY,
		// AccounterConstants.PM_QUARTERLY_PAYROLL_LIABILITY, 0, 0,
		// PaymentTerms.DUE_PAYROLL_TAX_QUARTER, 13, true);
		//
		// session.save(quarterlyPayrollLiability);

		// Current Fiscal Year creation

		VendorGroup creditCardCompanies = new VendorGroup();
		creditCardCompanies
				.setName(AccounterServerConstants.CREDIT_CARD_COMPANIES);
		creditCardCompanies.setDefault(true);
		session.save(creditCardCompanies);

	}

	public void createUKDefaultVATCodesAndVATAgency() {
		Session session = HibernateUtil.getCurrentSession();
		try {
			VATReturnBox vt1 = new VATReturnBox(company);
			vt1.setName(AccounterServerConstants.UK_EC_PURCHASES_GOODS);
			vt1.setVatBox(AccounterServerConstants.UK_BOX2_VAT_DUE_ON_ACQUISITIONS);
			vt1.setTotalBox(AccounterServerConstants.UK_BOX9_TOTAL_NET_ACQUISITIONS);
			vt1.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt1);

			VATReturnBox vt3 = new VATReturnBox(company);
			vt3.setName(AccounterServerConstants.UK_EC_SALES_GOODS);
			vt3.setVatBox(AccounterServerConstants.BOX_NONE);
			vt3.setTotalBox(AccounterServerConstants.UK_BOX8_TOTAL_NET_SUPPLIES);
			vt3.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt3);

			VATReturnBox vt4 = new VATReturnBox(company);
			vt4.setName(AccounterServerConstants.UK_EC_SALES_SERVICES);
			vt4.setVatBox(AccounterServerConstants.BOX_NONE);
			vt4.setTotalBox(AccounterServerConstants.UK_BOX6_TOTAL_NET_SALES);
			vt4.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt4);

			VATReturnBox vt5 = new VATReturnBox(company);
			vt5.setName(AccounterServerConstants.UK_DOMESTIC_PURCHASES);
			vt5.setVatBox(AccounterServerConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES);
			vt5.setTotalBox(AccounterServerConstants.UK_BOX7_TOTAL_NET_PURCHASES);
			vt5.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt5);

			VATReturnBox vt6 = new VATReturnBox(company);
			vt6.setName(AccounterServerConstants.UK_DOMESTIC_SALES);
			vt6.setVatBox(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES);
			vt6.setTotalBox(AccounterServerConstants.UK_BOX6_TOTAL_NET_SALES);
			vt6.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt6);

			VATReturnBox vt7 = new VATReturnBox(company);
			vt7.setName(AccounterServerConstants.UK_NOT_REGISTERED_PURCHASES);
			vt7.setVatBox(AccounterServerConstants.UK_BOX4_VAT_RECLAMED_ON_PURCHASES);
			vt7.setTotalBox(AccounterServerConstants.UK_BOX7_TOTAL_NET_PURCHASES);
			vt7.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt7);

			VATReturnBox vt8 = new VATReturnBox(company);
			vt8.setName(AccounterServerConstants.UK_NOT_REGISTERED_SALES);
			vt8.setVatBox(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES);
			vt8.setTotalBox(AccounterServerConstants.UK_BOX6_TOTAL_NET_SALES);
			vt8.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt8);

			VATReturnBox vt11 = new VATReturnBox(company);
			vt11.setName(AccounterServerConstants.UK_REVERSE_CHARGE);
			vt11.setVatBox(AccounterServerConstants.UK_BOX1_VAT_DUE_ON_SALES);
			vt11.setTotalBox(AccounterServerConstants.BOX_NONE);
			vt11.setVatReturnType(TAXAgency.RETURN_TYPE_UK_VAT);
			session.save(vt11);

			// /// For Ireland VAT Return type boxes

			VATReturnBox vt20 = new VATReturnBox(company);
			vt20.setName(AccounterServerConstants.IRELAND_DOMESTIC_SALES);
			vt20.setVatBox(AccounterServerConstants.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES);
			vt20.setTotalBox(AccounterServerConstants.IRELAND_BOX8_TOTAL_NET_SALES);
			vt20.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt20);

			VATReturnBox vt21 = new VATReturnBox(company);
			vt21.setName(AccounterServerConstants.IRELAND_DOMESTIC_PURCHASES);
			vt21.setVatBox(AccounterServerConstants.IRELAND_BOX4_VAT_ON_PURCHASES);
			vt21.setTotalBox(AccounterServerConstants.IRELAND_BOX9_TOTAL_NET_PURCHASES);
			vt21.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt21);

			VATReturnBox vt22 = new VATReturnBox(company);
			vt22.setName(AccounterServerConstants.IRELAND_EC_SALES_GOODS);
			vt22.setVatBox(AccounterServerConstants.BOX_NONE);
			vt22.setTotalBox(AccounterServerConstants.IRELAND_BOX6_E1_GOODS_TO_EU);
			vt22.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt22);

			VATReturnBox vt23 = new VATReturnBox(company);
			vt23.setName(AccounterServerConstants.IRELAND_EC_PURCHASES_GOODS);
			vt23.setVatBox(AccounterServerConstants.IRELAND_BOX2_VAT_DUE_ON_INTRA_EC_ACQUISITIONS);
			vt23.setTotalBox(AccounterServerConstants.IRELAND_BOX7_E2_GOODS_FROM_EU);
			vt23.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt23);

			VATReturnBox vt24 = new VATReturnBox(company);
			vt24.setName(AccounterServerConstants.IRELAND_EXEMPT_SALES);
			vt24.setVatBox(AccounterServerConstants.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES);
			vt24.setTotalBox(AccounterServerConstants.IRELAND_BOX8_TOTAL_NET_SALES);
			vt24.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt24);

			VATReturnBox vt25 = new VATReturnBox(company);
			vt25.setName(AccounterServerConstants.IRELAND_EXEMPT_PURCHASES);
			vt25.setVatBox(AccounterServerConstants.IRELAND_BOX7_E2_GOODS_FROM_EU);
			vt25.setTotalBox(AccounterServerConstants.IRELAND_BOX9_TOTAL_NET_PURCHASES);
			vt25.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt25);

			VATReturnBox vt26 = new VATReturnBox(company);
			vt26.setName(AccounterServerConstants.IRELAND_NOT_REGISTERED_SALES);
			vt26.setVatBox(AccounterServerConstants.IRELAND_BOX1_VAT_CHARGED_ON_SUPPIES);
			vt26.setTotalBox(AccounterServerConstants.IRELAND_BOX8_TOTAL_NET_SALES);
			vt26.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt26);

			VATReturnBox vt27 = new VATReturnBox(company);
			vt27.setName(AccounterServerConstants.IRELAND_NOT_REGISTERED_PURCHASES);
			vt27.setVatBox(AccounterServerConstants.IRELAND_BOX4_VAT_ON_PURCHASES);
			vt27.setTotalBox(AccounterServerConstants.IRELAND_BOX9_TOTAL_NET_PURCHASES);
			vt27.setVatReturnType(TAXAgency.RETURN_TYPE_IRELAND_VAT);
			session.save(vt27);

			// Session session = HibernateUtil.getCurrentSession();

			TAXAgency defaultVATAgency = new TAXAgency();
			defaultVATAgency.setActive(Boolean.TRUE);
			defaultVATAgency.setName(preferences.getVATtaxAgencyName());
			defaultVATAgency.setVATReturn(VATReturn.VAT_RETURN_UK_VAT);

			defaultVATAgency.setSalesLiabilityAccount((Account) session
					.getNamedQuery("unique.name.Account")
					.setString(0,
							AccounterServerConstants.SALES_TAX_VAT_UNFILED)
					.list().get(0));

			defaultVATAgency.setPurchaseLiabilityAccount((Account) session
					.getNamedQuery("unique.name.Account")
					.setString(0,
							AccounterServerConstants.SALES_TAX_VAT_UNFILED)
					.list().get(0));
			defaultVATAgency.setCompany(company);
			defaultVATAgency.setDefault(true);

			session.save(defaultVATAgency);

			TAXItem vatItem1 = new TAXItem(company);
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

			TAXItem vatItem2 = new TAXItem(company);
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

			TAXItem vatItem3 = new TAXItem(company);
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

			TAXItem vatItem4 = new TAXItem(company);
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

			TAXItem vatItem5 = new TAXItem(company);
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

			TAXItem vatItem6 = new TAXItem(company);
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

			TAXItem vatItem7 = new TAXItem(company);
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

			TAXItem vatItem8 = new TAXItem(company);
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

			TAXItem vatItem9 = new TAXItem(company);
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

			TAXItem vatItem10 = new TAXItem(company);
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

			TAXItem vatItem11 = new TAXItem(company);
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

			TAXItem vatItem12 = new TAXItem(company);
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

			TAXItem vatItem13 = new TAXItem(company);
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

			TAXItem vatItem14 = new TAXItem(company);
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

			TAXItem vatItem15 = new TAXItem(company);
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

			TAXItem vatItem16 = new TAXItem(company);
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

			TAXItem vatItem17 = new TAXItem(company);
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

			TAXGroup vatGroup1 = new TAXGroup(company);
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

			TAXGroup vatGroup2 = new TAXGroup(company);
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

			TAXGroup vatGroup3 = new TAXGroup(company);
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

			TAXGroup vatGroup4 = new TAXGroup(company);
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
				true, this.getName(), "(None Added)", "Classic Tempalate",
				"Classic Template");
		session.save(brandingTheme);
	}

	@Override
	public void init() {
		initDefaultUKAccounts();
		// createUKDefaultVATCodesAndVATAgency(session);

	}

	@Override
	String getDateFormat() {
		return AccounterServerConstants.ddMMyyyy;
	}
}
