package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.banking.NewPayeeAction;
import com.vimukti.accounter.web.client.ui.company.ChangePasswordAction;
import com.vimukti.accounter.web.client.ui.company.ChartOfAccountsAction;
import com.vimukti.accounter.web.client.ui.company.CompanyHomeAction;
import com.vimukti.accounter.web.client.ui.company.CompanyInfoAction;
import com.vimukti.accounter.web.client.ui.company.CountryRegionListAction;
import com.vimukti.accounter.web.client.ui.company.CreditRatingListAction;
import com.vimukti.accounter.web.client.ui.company.CustomerGroupListAction;
import com.vimukti.accounter.web.client.ui.company.DepreciationAction;
import com.vimukti.accounter.web.client.ui.company.FinanceLogAction;
import com.vimukti.accounter.web.client.ui.company.FormLayoutsListAction;
import com.vimukti.accounter.web.client.ui.company.IntegrateWithBusinessContactManagerAction;
import com.vimukti.accounter.web.client.ui.company.ItemGroupListAction;
import com.vimukti.accounter.web.client.ui.company.JournalEntriesAction;
import com.vimukti.accounter.web.client.ui.company.MakeActiveAction;
import com.vimukti.accounter.web.client.ui.company.MakeInActiveAction;
import com.vimukti.accounter.web.client.ui.company.ManageFiscalYearAction;
import com.vimukti.accounter.web.client.ui.company.ManageItemTaxAction;
import com.vimukti.accounter.web.client.ui.company.ManageSalesTaxGroupsAction;
import com.vimukti.accounter.web.client.ui.company.MergeCustomersAction;
import com.vimukti.accounter.web.client.ui.company.MergeFinancialAccountsAction;
import com.vimukti.accounter.web.client.ui.company.MergeItemsAction;
import com.vimukti.accounter.web.client.ui.company.MergeVendorsAction;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.company.NewCashBasisJournalEntryAction;
import com.vimukti.accounter.web.client.ui.company.NewCompanyAction;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.company.NewJournalEntryAction;
import com.vimukti.accounter.web.client.ui.company.NewSalesperSonAction;
import com.vimukti.accounter.web.client.ui.company.NewTAXAgencyAction;
import com.vimukti.accounter.web.client.ui.company.NewbankAction;
import com.vimukti.accounter.web.client.ui.company.PaySalesTaxAction;
import com.vimukti.accounter.web.client.ui.company.PayTypeListAction;
import com.vimukti.accounter.web.client.ui.company.PaymentTermListAction;
import com.vimukti.accounter.web.client.ui.company.PaymentsAction;
import com.vimukti.accounter.web.client.ui.company.PreferencesAction;
import com.vimukti.accounter.web.client.ui.company.PriceLevelListAction;
import com.vimukti.accounter.web.client.ui.company.SalesPersonListsAction;
import com.vimukti.accounter.web.client.ui.company.ShippingMethodListAction;
import com.vimukti.accounter.web.client.ui.company.ShippingTermListAction;
import com.vimukti.accounter.web.client.ui.company.UserDetailsAction;
import com.vimukti.accounter.web.client.ui.company.VendorGroupListAction;
import com.vimukti.accounter.web.client.ui.reports.SalesTaxLiabilityAction;
import com.vimukti.accounter.web.client.ui.vat.AdjustTAXAction;

/**
 * CompanyActionFactory contains all static methods, each method returns
 * appropriate Action instance from Company Section, use to Get All Company
 * Actions instance.
 * 
 * @author kumar kasimala
 * 
 */
public class CompanyActionFactory extends AbstractActionFactory {

	public static CompanyHomeAction getCompanyHomeAction() {
		return new CompanyHomeAction(actionsConstants.home());
	}

	public static MakeActiveAction getMakeActiveAction() {
		return new MakeActiveAction(actionsConstants.makeActive());

	}

	public static MakeInActiveAction getMakeInActiveAction() {
		return new MakeInActiveAction(actionsConstants.makeInActive());

	}

	public static NewCompanyAction getNewCompanyAction() {
		return new NewCompanyAction(actionsConstants.newCompany(),
				"/images/icons/company/new_company.png");
	}

	public static CompanyInfoAction getCompanyInformationAction() {
		return new CompanyInfoAction(actionsConstants.companyinfo(),
				"/images/icons/company/company_information.png");
	}

	public static PreferencesAction getPreferencesAction() {
		return new PreferencesAction(actionsConstants.companyPreferences(),
				"/images/icons/company/preferences.png");
	}

	public static IntegrateWithBusinessContactManagerAction getIntegrateWithBusinessContactManagerAction() {
		return new IntegrateWithBusinessContactManagerAction(
				actionsConstants.integrateWithBusinessContactManager());
	}

	public static NewJournalEntryAction getNewJournalEntryAction() {
		return new NewJournalEntryAction(actionsConstants.newJournalEntry(),
				"/images/icons/company/new_journal_entry.png");
	}

	public static NewCashBasisJournalEntryAction getNewCashBasisJournalEntryAction() {
		return new NewCashBasisJournalEntryAction(
				actionsConstants.newCashBasisJournalEntry(),
				"/images/icons/company/new_cash_base_journal_entry.png");
	}

	public static NewAccountAction getNewAccountAction() {
		return new NewAccountAction(actionsConstants.newAccount(),
				"/images/icons/company/new_account.png");
	}

	public static PaymentsAction getPaymentsAction() {
		return new PaymentsAction(actionsConstants.payments(), null);
	}

	public static MergeCustomersAction getMergeCustomersAction() {
		return new MergeCustomersAction(actionsConstants.mergeCustomers());
	}

	public static MergeVendorsAction getMergeVendorsAction() {
		return new MergeVendorsAction(actionsConstants.mergeSuppliers());
	}

	public static MergeItemsAction getMergeItemsAction() {
		return new MergeItemsAction(actionsConstants.mergeItems());
	}

	public static MergeFinancialAccountsAction getMergeFinancialAccountsAction() {
		return new MergeFinancialAccountsAction(
				actionsConstants.mergeFinacialAccounts());
	}

	public static ManageSalesTaxGroupsAction getManageSalesTaxGroupsAction() {
		String text;
		if (Accounter.getUser().canDoInvoiceTransactions())
			text = actionsConstants.manageSalesGroups();
		else
			text = actionsConstants.salesTaxGroups();
		return new ManageSalesTaxGroupsAction(text);
	}

	// public static ManageSalesTaxCodesAction getManageSalesTaxCodesAction() {
	// String constant = actionsConstants.manageSalesTaxCodes();
	// return new ManageSalesTaxCodesAction(constant);
	// }

	public static ManageItemTaxAction getManageItemTaxAction() {
		return new ManageItemTaxAction(actionsConstants.manageItemTax());
	}

	public static PaySalesTaxAction getPaySalesTaxAction() {
		String constant = null;
		constant = actionsConstants.paySalesTax();
		return new PaySalesTaxAction(constant);
	}

	public static SalesTaxLiabilityAction getViewSalesTaxLiabilityAction() {
		return new SalesTaxLiabilityAction(actionsConstants.salesTaxLiability());
	}

	// public static NewTaxAgencyAction getNewTaxAgencyAction() {
	// return new NewTaxAgencyAction(actionsConstants.newTaxAgency());
	// }

	public static CustomerGroupListAction getCustomerGroupListAction() {
		return new CustomerGroupListAction(actionsConstants.customerGroupList());
	}

	public static VendorGroupListAction getVendorGroupListAction() {
		return new VendorGroupListAction(UIUtils.getVendorString(
				actionsConstants.supplierGroupList(),
				actionsConstants.vendorGroupList()));
	}

	public static PaymentTermListAction getPaymentTermListAction() {
		return new PaymentTermListAction(actionsConstants.paymentTermList());
	}

	public static ShippingMethodListAction getShippingMethodListAction() {
		return new ShippingMethodListAction(
				actionsConstants.shippingMethodList());
	}

	public static ShippingTermListAction getShippingTermListAction() {
		return new ShippingTermListAction(actionsConstants.shippingTermList());
	}

	public static PriceLevelListAction getPriceLevelListAction() {
		return new PriceLevelListAction(actionsConstants.priceLevelList());
	}

	public static ItemGroupListAction getItemGroupListAction() {
		return new ItemGroupListAction(actionsConstants.itemGroupList());
	}

	public static CreditRatingListAction getCreditRatingListAction() {
		return new CreditRatingListAction(actionsConstants.creditRatingList());
	}

	public static CountryRegionListAction getCountryRegionListAction() {
		return new CountryRegionListAction(actionsConstants.countryRegionList());
	}

	public static FormLayoutsListAction getFormLayoutsListAction() {
		return new FormLayoutsListAction(actionsConstants.formLayoutsList());
	}

	public static PayTypeListAction getPayTypeListAction() {
		return new PayTypeListAction(actionsConstants.payTypeList());
	}

	public static ManageFiscalYearAction getManageFiscalYearAction() {
		return new ManageFiscalYearAction(actionsConstants.manageFiscalYear());
	}

	public static ChartOfAccountsAction getChartOfAccountsAction() {
		return new ChartOfAccountsAction(
				actionsConstants.accounterCategoryList(),
				"/images/icons/banking/chart_of_accounts.png");
	}

	public static ChartOfAccountsAction getChartOfAccountsAction(int accountType) {
		return new ChartOfAccountsAction(
				actionsConstants.accounterCategoryList(),
				"/images/icons/banking/chart_of_accounts.png", accountType);
	}

	public static SalesPersonListsAction getSalesPersonListAction() {
		return new SalesPersonListsAction(actionsConstants.SalesPersons(),
				"/images/icons/vendors/pay_bills.png");
	}

	public static JournalEntriesAction getJournalEntriesAction() {
		return new JournalEntriesAction(actionsConstants.journalEntries());
	}

	public static NewbankAction getNewbankAction() {
		return new NewbankAction(actionsConstants.newBank(), "");
	}

	public static ManageItemTaxAction getNewItemTaxAction() {
		return new ManageItemTaxAction(actionsConstants.newItemTax(), "");
	}

	public static NewSalesperSonAction getNewSalesperSonAction() {
		return new NewSalesperSonAction(actionsConstants.newSalesperson(),
				"/images/icons/company/new_sales_person.png");
	}

	public static NewItemAction getNewItemAction() {
		return new NewItemAction(actionsConstants.newItem(),
				"/images/icons/vendors/new_item.png");
	}

	// public static NewItemAction getNewItemAction(ClientItem item,
	// AsyncCallback<Object> callback) {
	// return new NewItemAction(actionsConstants.newItem(),
	// "/images/icons/vendors/new_item.png", item, callback);
	// }

	public static NewPayeeAction getNewPayeeAction() {
		return new NewPayeeAction("", null);

	}

	public static DepreciationAction getDepriciationAction() {
		return new DepreciationAction(Accounter.constants()
				.depreciation(), null);

	}

	public static NewTAXAgencyAction getNewTAXAgencyAction() {
		String flag;
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			flag = Accounter.constants().newVATAgency();

		else
			flag = Accounter.constants().newTAXAgency();

		return new NewTAXAgencyAction(flag);
	}

	public static FinanceLogAction getFinanceLogAction() {
		return new FinanceLogAction(Accounter.constants().showLog());
	}

	public static ManageSalesTaxItemsAction getManageSalesTaxItemsAction() {
		String constant;
		if (Accounter.getUser().canDoInvoiceTransactions())
			constant = actionsConstants.manageSalesItems();
		else
			constant = actionsConstants.salesTaxItems();
		return new ManageSalesTaxItemsAction(constant);
	}

	// public static NewTaxItemAction getTaxItemAction() {
	// return new NewTaxItemAction(FinanceApplication.constants()
	// .newTaxItem());
	// }

	public static AdjustTAXAction getAdjustTaxAction() {
		return new AdjustTAXAction(actionsConstants.taxAdjustment());
	}

	public static ChangePasswordAction getChangePasswordAction() {
		return new ChangePasswordAction(actionsConstants.changePassword());

	}

	public static UserDetailsAction getUserDetailsAction() {
		return new UserDetailsAction(actionsConstants.userDetails());
	}

	// public static ForgetPasswordAction getForgetPasswordAction(){
	// return new ForgetPasswordAction(actionsConstants.forgetPassword());
	//
	// }

}
