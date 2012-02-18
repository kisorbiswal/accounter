package com.vimukti.accounter.web.client.ui.core;

import java.util.List;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientStatement;
import com.vimukti.accounter.web.client.core.ClientStatementRecord;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.imports.ImportAction;
import com.vimukti.accounter.web.client.ui.BuildAssemblyAction;
import com.vimukti.accounter.web.client.ui.InventoryAssemblyAction;
import com.vimukti.accounter.web.client.ui.RemindersListAction;
import com.vimukti.accounter.web.client.ui.banking.AccountRegisterAction;
import com.vimukti.accounter.web.client.ui.banking.BankStatementAction;
import com.vimukti.accounter.web.client.ui.banking.BankingHomeAction;
import com.vimukti.accounter.web.client.ui.banking.BuyChecksAndFormsAction;
import com.vimukti.accounter.web.client.ui.banking.ChartsOfAccountsAction;
import com.vimukti.accounter.web.client.ui.banking.CreditCardChargeAction;
import com.vimukti.accounter.web.client.ui.banking.EnterPaymentsAction;
import com.vimukti.accounter.web.client.ui.banking.ImportBankFilesAction;
import com.vimukti.accounter.web.client.ui.banking.MakeDepositAction;
import com.vimukti.accounter.web.client.ui.banking.MatchTrasactionsAction;
import com.vimukti.accounter.web.client.ui.banking.MergeCustomerAction;
import com.vimukti.accounter.web.client.ui.banking.NewBankAccountAction;
import com.vimukti.accounter.web.client.ui.banking.NewReconcileAccountAction;
import com.vimukti.accounter.web.client.ui.banking.PrintChecksAction;
import com.vimukti.accounter.web.client.ui.banking.ReconciliationsListAction;
import com.vimukti.accounter.web.client.ui.banking.ServicesOverviewAction;
import com.vimukti.accounter.web.client.ui.banking.StatementImportViewAction;
import com.vimukti.accounter.web.client.ui.banking.StatementReconcilationAction;
import com.vimukti.accounter.web.client.ui.banking.SyncOnlinePayeesAction;
import com.vimukti.accounter.web.client.ui.banking.WriteChecksAction;
import com.vimukti.accounter.web.client.ui.combo.NewCurrencyAction;
import com.vimukti.accounter.web.client.ui.company.AccounterClassListAction;
import com.vimukti.accounter.web.client.ui.company.AuditHistoryAction;
import com.vimukti.accounter.web.client.ui.company.BudgetAction;
import com.vimukti.accounter.web.client.ui.company.ChalanListViewAction;
import com.vimukti.accounter.web.client.ui.company.ChangePasswordAction;
import com.vimukti.accounter.web.client.ui.company.ChartOfAccountsAction;
import com.vimukti.accounter.web.client.ui.company.CheckPrintSettingAction;
import com.vimukti.accounter.web.client.ui.company.CompanyHomeAction;
import com.vimukti.accounter.web.client.ui.company.CountryRegionListAction;
import com.vimukti.accounter.web.client.ui.company.CreateIRASInformationFileAction;
import com.vimukti.accounter.web.client.ui.company.CreditRatingListAction;
import com.vimukti.accounter.web.client.ui.company.CurrencyGroupListAction;
import com.vimukti.accounter.web.client.ui.company.CustomerCentreAction;
import com.vimukti.accounter.web.client.ui.company.CustomerGroupListAction;
import com.vimukti.accounter.web.client.ui.company.CustomersAction;
import com.vimukti.accounter.web.client.ui.company.DepreciationAction;
import com.vimukti.accounter.web.client.ui.company.EditProfileAction;
import com.vimukti.accounter.web.client.ui.company.FormLayoutsListAction;
import com.vimukti.accounter.web.client.ui.company.IntegrateWithBusinessContactManagerAction;
import com.vimukti.accounter.web.client.ui.company.ItemGroupListAction;
import com.vimukti.accounter.web.client.ui.company.ItemsAction;
import com.vimukti.accounter.web.client.ui.company.JournalEntriesAction;
import com.vimukti.accounter.web.client.ui.company.LocationGroupListAction;
import com.vimukti.accounter.web.client.ui.company.MakeActiveAction;
import com.vimukti.accounter.web.client.ui.company.MakeInActiveAction;
import com.vimukti.accounter.web.client.ui.company.ManageFiscalYearAction;
import com.vimukti.accounter.web.client.ui.company.ManageItemTaxAction;
import com.vimukti.accounter.web.client.ui.company.ManageSalesTaxGroupsAction;
import com.vimukti.accounter.web.client.ui.company.MergeAccountsAction;
import com.vimukti.accounter.web.client.ui.company.MergeFinancialAccountsAction;
import com.vimukti.accounter.web.client.ui.company.MergeItemsAction;
import com.vimukti.accounter.web.client.ui.company.MergeVendorAction;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.company.NewBudgetAction;
import com.vimukti.accounter.web.client.ui.company.NewCashBasisJournalEntryAction;
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
import com.vimukti.accounter.web.client.ui.company.PurchaseItemsAction;
import com.vimukti.accounter.web.client.ui.company.SalesPersonListsAction;
import com.vimukti.accounter.web.client.ui.company.ShippingMethodListAction;
import com.vimukti.accounter.web.client.ui.company.ShippingTermListAction;
import com.vimukti.accounter.web.client.ui.company.TDSResponsiblePersonAction;
import com.vimukti.accounter.web.client.ui.company.TdsDeductorMasterAction;
import com.vimukti.accounter.web.client.ui.company.UserDetailsAction;
import com.vimukti.accounter.web.client.ui.company.UsersActivityListAction;
import com.vimukti.accounter.web.client.ui.company.VendorCenterAction;
import com.vimukti.accounter.web.client.ui.company.VendorGroupListAction;
import com.vimukti.accounter.web.client.ui.customers.CreateStatementAction;
import com.vimukti.accounter.web.client.ui.customers.CustomerPaymentsAction;
import com.vimukti.accounter.web.client.ui.customers.CustomerRefundAction;
import com.vimukti.accounter.web.client.ui.customers.CustomerRefundsAction;
import com.vimukti.accounter.web.client.ui.customers.CustomersHomeAction;
import com.vimukti.accounter.web.client.ui.customers.InvoiceListViewAction;
import com.vimukti.accounter.web.client.ui.customers.InvoicesAction;
import com.vimukti.accounter.web.client.ui.customers.NewCashSaleAction;
import com.vimukti.accounter.web.client.ui.customers.NewCreditsAndRefundsAction;
import com.vimukti.accounter.web.client.ui.customers.NewCustomerAction;
import com.vimukti.accounter.web.client.ui.customers.NewInvoiceAction;
import com.vimukti.accounter.web.client.ui.customers.NewQuoteAction;
import com.vimukti.accounter.web.client.ui.customers.QuotesAction;
import com.vimukti.accounter.web.client.ui.customers.ReceivePaymentAction;
import com.vimukti.accounter.web.client.ui.customers.ReceivedPaymentsAction;
import com.vimukti.accounter.web.client.ui.customers.RecurringsListAction;
import com.vimukti.accounter.web.client.ui.customers.SalesPersonAction;
import com.vimukti.accounter.web.client.ui.customers.TaxDialogAction;
import com.vimukti.accounter.web.client.ui.fixedassets.DisposingRegisteredItemAction;
import com.vimukti.accounter.web.client.ui.fixedassets.HistoryListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.NewFixedAssetAction;
import com.vimukti.accounter.web.client.ui.fixedassets.PendingItemsListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.RegisteredItemsListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.SellingRegisteredItemAction;
import com.vimukti.accounter.web.client.ui.fixedassets.SoldDisposedFixedAssetsListAction;
import com.vimukti.accounter.web.client.ui.reports.APAgingDetailAction;
import com.vimukti.accounter.web.client.ui.reports.APAgingSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.ARAgingDetailAction;
import com.vimukti.accounter.web.client.ui.reports.ARAgingSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.AutomaticTransactionsAction;
import com.vimukti.accounter.web.client.ui.reports.BalanceSheetAction;
import com.vimukti.accounter.web.client.ui.reports.BankCheckDetailReportAction;
import com.vimukti.accounter.web.client.ui.reports.BankDepositDetailReportAction;
import com.vimukti.accounter.web.client.ui.reports.BudgetOverviewReportAction;
import com.vimukti.accounter.web.client.ui.reports.BudgetvsActualsAction;
import com.vimukti.accounter.web.client.ui.reports.CashFlowStatementAction;
import com.vimukti.accounter.web.client.ui.reports.CheckDetailReportAction;
import com.vimukti.accounter.web.client.ui.reports.CustomerTransactionHistoryAction;
import com.vimukti.accounter.web.client.ui.reports.DepreciationSheduleAction;
import com.vimukti.accounter.web.client.ui.reports.ECSalesListAction;
import com.vimukti.accounter.web.client.ui.reports.ECSalesListDetailAction;
import com.vimukti.accounter.web.client.ui.reports.EnterExchangeRatesAction;
import com.vimukti.accounter.web.client.ui.reports.ExpenseReportAction;
import com.vimukti.accounter.web.client.ui.reports.GLReportAction;
import com.vimukti.accounter.web.client.ui.reports.InventoryItemReportAction;
import com.vimukti.accounter.web.client.ui.reports.InventoryStockStatusByItemAction;
import com.vimukti.accounter.web.client.ui.reports.InventoryStockStatusByVendorAction;
import com.vimukti.accounter.web.client.ui.reports.InventoryValuationDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.InventoryValutionSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.MISC1099TransactionDetailAction;
import com.vimukti.accounter.web.client.ui.reports.MissingChecksReportAction;
import com.vimukti.accounter.web.client.ui.reports.MostProfitableCustomersAction;
import com.vimukti.accounter.web.client.ui.reports.ProfitAndLossAction;
import com.vimukti.accounter.web.client.ui.reports.ProfitAndLossByLocationAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByItemDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByItemSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByVendorDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByVendorSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseOpenOrderAction;
import com.vimukti.accounter.web.client.ui.reports.RealisedExchangeLossesAndGainsAction;
import com.vimukti.accounter.web.client.ui.reports.ReconcilationsAction;
import com.vimukti.accounter.web.client.ui.reports.ReconciliationDiscrepancyReportAction;
import com.vimukti.accounter.web.client.ui.reports.ReportsHomeAction;
import com.vimukti.accounter.web.client.ui.reports.ReverseChargeListAction;
import com.vimukti.accounter.web.client.ui.reports.ReverseChargeListDetailAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByCustomerDetailAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByCustomerSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByItemDetailAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByItemSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByLocationDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByLocationSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.SalesOpenOrderAction;
import com.vimukti.accounter.web.client.ui.reports.SalesTaxLiabilityAction;
import com.vimukti.accounter.web.client.ui.reports.StatementReportAction;
import com.vimukti.accounter.web.client.ui.reports.TAXItemExceptionDetailReport;
import com.vimukti.accounter.web.client.ui.reports.TaxItemDetailReportAction;
import com.vimukti.accounter.web.client.ui.reports.TransactionDetailByAccountAction;
import com.vimukti.accounter.web.client.ui.reports.TransactionDetailByTaxItemAction;
import com.vimukti.accounter.web.client.ui.reports.TrialBalanceAction;
import com.vimukti.accounter.web.client.ui.reports.UnRealisedExchangeLossesAndGainsAction;
import com.vimukti.accounter.web.client.ui.reports.VAT100ReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATDetailsReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATItemSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATUncategorisedAmountsReportAction;
import com.vimukti.accounter.web.client.ui.reports.VaTItemDetailAction;
import com.vimukti.accounter.web.client.ui.reports.VatExceptionDetailReportAction;
import com.vimukti.accounter.web.client.ui.reports.VendorTransactionHistoryAction;
import com.vimukti.accounter.web.client.ui.search.SearchInputAction;
import com.vimukti.accounter.web.client.ui.settings.AddMeasurementAction;
import com.vimukti.accounter.web.client.ui.settings.AutomaticSequenceAction;
import com.vimukti.accounter.web.client.ui.settings.ConversionBalancesAction;
import com.vimukti.accounter.web.client.ui.settings.ConversionDateAction;
import com.vimukti.accounter.web.client.ui.settings.CopyThemeAction;
import com.vimukti.accounter.web.client.ui.settings.CustomThemeAction;
import com.vimukti.accounter.web.client.ui.settings.DeleteThemeAction;
import com.vimukti.accounter.web.client.ui.settings.GeneralSettingsAction;
import com.vimukti.accounter.web.client.ui.settings.InventoryItemsAction;
import com.vimukti.accounter.web.client.ui.settings.InviteUserAction;
import com.vimukti.accounter.web.client.ui.settings.InvoiceBrandingAction;
import com.vimukti.accounter.web.client.ui.settings.MeasurementListAction;
import com.vimukti.accounter.web.client.ui.settings.NewBrandThemeAction;
import com.vimukti.accounter.web.client.ui.settings.StockAdjustmentAction;
import com.vimukti.accounter.web.client.ui.settings.StockAdjustmentsListAction;
import com.vimukti.accounter.web.client.ui.settings.StockSettingsAction;
import com.vimukti.accounter.web.client.ui.settings.UsersAction;
import com.vimukti.accounter.web.client.ui.settings.WareHouseItemsListAction;
import com.vimukti.accounter.web.client.ui.settings.WareHouseTransferAction;
import com.vimukti.accounter.web.client.ui.settings.WareHouseViewAction;
import com.vimukti.accounter.web.client.ui.settings.WarehouseListAction;
import com.vimukti.accounter.web.client.ui.settings.WarehouseTransferListAction;
import com.vimukti.accounter.web.client.ui.translation.TranslationAction;
import com.vimukti.accounter.web.client.ui.vat.AdjustTAXAction;
import com.vimukti.accounter.web.client.ui.vat.ETdsFillingAction;
import com.vimukti.accounter.web.client.ui.vat.FileTAXAction;
import com.vimukti.accounter.web.client.ui.vat.ManageTAXCodesListAction;
import com.vimukti.accounter.web.client.ui.vat.NewTAXCodeAction;
import com.vimukti.accounter.web.client.ui.vat.NewVatItemAction;
import com.vimukti.accounter.web.client.ui.vat.PayTAXAction;
import com.vimukti.accounter.web.client.ui.vat.ReceiveVATAction;
import com.vimukti.accounter.web.client.ui.vat.TAXAgencyListAction;
import com.vimukti.accounter.web.client.ui.vat.TDSAcknowledgmentAction;
import com.vimukti.accounter.web.client.ui.vat.TDSAcknowledgmentsReportAction;
import com.vimukti.accounter.web.client.ui.vat.TDSChalanDetailsAction;
import com.vimukti.accounter.web.client.ui.vat.TDSFiledDetailsAction;
import com.vimukti.accounter.web.client.ui.vat.TDSForm16AAction;
import com.vimukti.accounter.web.client.ui.vat.TaxHistoryAction;
import com.vimukti.accounter.web.client.ui.vat.VatItemListAction;
import com.vimukti.accounter.web.client.ui.vendors.AwaitingAuthorisationAction;
import com.vimukti.accounter.web.client.ui.vendors.BillsAction;
import com.vimukti.accounter.web.client.ui.vendors.CashExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.CreditCardExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.DepositAction;
import com.vimukti.accounter.web.client.ui.vendors.EmployeeExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.EnterBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.ExpenseClaimsAction;
import com.vimukti.accounter.web.client.ui.vendors.ExpensesAction;
import com.vimukti.accounter.web.client.ui.vendors.IssuePaymentsAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCashPurchaseAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCheckAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCreditMemoAction;
import com.vimukti.accounter.web.client.ui.vendors.NewItemReceiptAction;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorAction;
import com.vimukti.accounter.web.client.ui.vendors.PayBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.Prepare1099MISCAction;
import com.vimukti.accounter.web.client.ui.vendors.PreviousClaimAction;
import com.vimukti.accounter.web.client.ui.vendors.PurchaseOrderAction;
import com.vimukti.accounter.web.client.ui.vendors.PurchaseOrderListAction;
import com.vimukti.accounter.web.client.ui.vendors.RecordExpensesAction;
import com.vimukti.accounter.web.client.ui.vendors.TDSPayAction;
import com.vimukti.accounter.web.client.ui.vendors.TDSVendorsListAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsListAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorsHomeAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorsListAction;

public class ActionFactory {

	public static AccounterMessages messages = Global.get().messages();

	public static GeneralSettingsAction getGeneralSettingsAction() {
		return new GeneralSettingsAction();
	}

	public static InventoryAssemblyAction getInventoryAssemblyAction() {
		return new InventoryAssemblyAction();
	}

	public static NewItemAction getNewInventoryItemAction() {
		NewItemAction action = new NewItemAction(true);
		action.setType(ClientItem.TYPE_INVENTORY_PART);
		return action;
	}

	public static MissingChecksReportAction getMissingChecksReportAction() {
		return new MissingChecksReportAction();
	}

	public static ReconciliationDiscrepancyReportAction getReconciliationDiscrepancyReportAction() {
		return new ReconciliationDiscrepancyReportAction();
	}

	public static InventoryItemsAction getInventoryItemsAction() {
		return new InventoryItemsAction();
	}

	public static BankDepositDetailReportAction getBankDepositDetailReportAction() {
		return new BankDepositDetailReportAction();
	}

	public static BankCheckDetailReportAction getBankCheckDetailReportAction() {
		return new BankCheckDetailReportAction();
	}

	public static InventoryValuationDetailsAction getInventoryValuationDetailsAction() {
		return new InventoryValuationDetailsAction();
	}

	public static InventoryStockStatusByItemAction getInventoryStockStatusByItemAction() {
		return new InventoryStockStatusByItemAction();
	}

	public static InventoryStockStatusByVendorAction getInventoryStockStatusByVendorAction() {
		return new InventoryStockStatusByVendorAction();
	}

	public static ConversionBalancesAction getConversionBalancesAction() {
		return new ConversionBalancesAction();
	}

	public static InvoiceBrandingAction getInvoiceBrandingAction() {
		return new InvoiceBrandingAction();
	}

	public static NewBrandThemeAction getNewBrandThemeAction() {
		return new NewBrandThemeAction();

	}

	public static NewBrandCustomThemeAction getNewBrandCustomThemeAction() {
		return new NewBrandCustomThemeAction();

	}

	public static NewBrandCustomThemeAction getEditBrandCustomThemeAction() {
		return new NewBrandCustomThemeAction();

	}

	public static ConversionDateAction getConversionDateAction() {
		return new ConversionDateAction();
	}

	public static AutomaticSequenceAction getAutomaticSequenceAction() {
		return new AutomaticSequenceAction();
	}

	public static CustomThemeAction getCustomThemeAction() {
		return new CustomThemeAction();
	}

	public static UsersAction getUsersAction() {
		return new UsersAction();
	}

	public static InviteUserAction getInviteUserAction() {
		return new InviteUserAction();
	}

	public static DeleteThemeAction getDeleteThemeAction() {
		return new DeleteThemeAction();
	}

	public static CopyThemeAction getCopyThemeAction() {
		return new CopyThemeAction();
	}

	public static WareHouseViewAction getWareHouseViewAction() {
		return new WareHouseViewAction();
	}

	public static WareHouseTransferAction getWareHouseTransferAction() {
		return new WareHouseTransferAction();
	}

	public static WarehouseListAction getWarehouseListAction() {
		return new WarehouseListAction();
	}

	public static WareHouseItemsListAction getWareHouseItemsListAction(
			long wareHouse) {
		return new WareHouseItemsListAction(wareHouse);
	}

	public static WarehouseTransferListAction getWarehouseTransferListAction() {
		return new WarehouseTransferListAction();
	}

	// Banking action factory

	public static BankingHomeAction getBankingHomeAction() {
		return new BankingHomeAction();
	}

	public static NewBankAccountAction getNewBankAccountAction() {
		return new NewBankAccountAction();
	}

	public static AccountRegisterAction getAccountRegisterAction() {
		return new AccountRegisterAction();
	}

	public static WriteChecksAction getWriteChecksAction() {
		return new WriteChecksAction();
	}

	public static BankStatementAction getBankStatementAction(
			ClientAccount account) {
		return new BankStatementAction(account);
	}

	public static WriteChecksAction getWriteChecksAction(
			ClientWriteCheck writeCheck,
			AccounterAsyncCallback<Object> callBackObject) {
		return new WriteChecksAction(writeCheck, callBackObject);
	}

	public static MakeDepositAction getMakeDepositAction() {
		return new MakeDepositAction();
	}

	public static MakeDepositAction getMakeDepositAction(
			ClientTransferFund makeDeposit,
			AccounterAsyncCallback<Object> callBackObject) {
		return new MakeDepositAction(makeDeposit, callBackObject);
	}

	// for reconcilation.
	public static MakeDepositAction getMakeDepositAction(
			ClientAccount reconcilationAccount, double amount,
			ClientStatementRecord statementRecord) {
		return new MakeDepositAction(reconcilationAccount, amount,
				statementRecord);
	}

	public static EnterPaymentsAction getEnterPaymentsAction() {
		return new EnterPaymentsAction();
	}

	public static SyncOnlinePayeesAction getSyncOnlinePayeesAction() {
		return new SyncOnlinePayeesAction();
	}

	public static ImportBankFilesAction getImportBankFilesAction() {
		return new ImportBankFilesAction();
	}

	public static CreditCardChargeAction getCreditCardChargeAction() {
		return new CreditCardChargeAction();
	}

	public static PrintChecksAction getPrintChecksAction() {
		return new PrintChecksAction();
	}

	public static ChartsOfAccountsAction getChartsOfAccountsAction() {
		return new ChartsOfAccountsAction();
	}

	public static PaymentsAction getPaymentsAction(int category) {
		PaymentsAction action = new PaymentsAction(category);
		return action;
	}

	public static MatchTrasactionsAction getMatchTrasactionsAction() {
		return new MatchTrasactionsAction();
	}

	// ActionFactory

	public static CompanyHomeAction getCompanyHomeAction() {
		return new CompanyHomeAction();
	}

	public static MakeActiveAction getMakeActiveAction() {
		return new MakeActiveAction();

	}

	public static MakeInActiveAction getMakeInActiveAction() {
		return new MakeInActiveAction();

	}

	public static PreferencesAction getPreferencesAction(int catagory) {
		return new PreferencesAction(catagory);
	}

	public static IntegrateWithBusinessContactManagerAction getIntegrateWithBusinessContactManagerAction() {
		return new IntegrateWithBusinessContactManagerAction();
	}

	public static NewJournalEntryAction getNewJournalEntryAction() {
		return new NewJournalEntryAction();
	}

	public static NewCashBasisJournalEntryAction getNewCashBasisJournalEntryAction() {
		return new NewCashBasisJournalEntryAction();
	}

	public static NewAccountAction getNewAccountAction() {
		return new NewAccountAction();
	}

	// public static MergeAccountsAction getMergeVendorsAction() {
	// return new MergeAccountsAction(messages.mergeVendors(Global.get()
	// .vendors()));
	// }

	// public static MergeItemsAction getMergeItemsAction() {
	// return new MergeItemsAction(messages.mergeItems());
	// }

	public static MergeFinancialAccountsAction getMergeFinancialAccountsAction() {
		return new MergeFinancialAccountsAction();
	}

	public static ManageSalesTaxGroupsAction getManageSalesTaxGroupsAction() {
		return new ManageSalesTaxGroupsAction();
	}

	// public static ManageSalesTaxCodesAction getManageSalesTaxCodesAction() {
	// String constant = messages.manageSalesTaxCodes();
	// return new ManageSalesTaxCodesAction(constant);
	// }

	public static ManageItemTaxAction getManageItemTaxAction() {
		return new ManageItemTaxAction(messages.manageItemTax());
	}

	public static PaySalesTaxAction getPaySalesTaxAction() {
		return new PaySalesTaxAction();
	}

	public static SalesTaxLiabilityAction getViewSalesTaxLiabilityAction() {
		return new SalesTaxLiabilityAction();
	}

	// public static NewTaxAgencyAction getNewTaxAgencyAction() {
	// return new NewTaxAgencyAction(messages.newTaxAgency());
	// }

	public static CustomerGroupListAction getCustomerGroupListAction() {
		return new CustomerGroupListAction();
	}

	public static VendorGroupListAction getVendorGroupListAction() {
		return new VendorGroupListAction();
	}

	public static PaymentTermListAction getPaymentTermListAction() {
		return new PaymentTermListAction();
	}

	public static ShippingMethodListAction getShippingMethodListAction() {
		return new ShippingMethodListAction();
	}

	public static ShippingTermListAction getShippingTermListAction() {
		return new ShippingTermListAction();
	}

	public static PriceLevelListAction getPriceLevelListAction() {
		return new PriceLevelListAction();
	}

	public static ItemGroupListAction getItemGroupListAction() {
		return new ItemGroupListAction();
	}

	public static CreditRatingListAction getCreditRatingListAction() {
		return new CreditRatingListAction();
	}

	public static CountryRegionListAction getCountryRegionListAction() {
		return new CountryRegionListAction();
	}

	public static CurrencyGroupListAction getCurrencyGroupListAction() {
		return new CurrencyGroupListAction();
	}

	public static FormLayoutsListAction getFormLayoutsListAction() {
		return new FormLayoutsListAction();
	}

	public static PayTypeListAction getPayTypeListAction() {
		return new PayTypeListAction();
	}

	public static ManageFiscalYearAction getManageFiscalYearAction() {
		return new ManageFiscalYearAction();
	}

	public static ChartOfAccountsAction getChartOfAccountsAction() {
		return new ChartOfAccountsAction();
	}

	public static ChartOfAccountsAction getChartOfAccountsAction(int accountType) {
		return new ChartOfAccountsAction(accountType);
	}

	public static SalesPersonListsAction getSalesPersonListAction() {
		return new SalesPersonListsAction();
	}

	public static JournalEntriesAction getJournalEntriesAction() {
		return new JournalEntriesAction();
	}

	public static NewbankAction getNewbankAction() {
		return new NewbankAction();
	}

	public static ManageItemTaxAction getNewItemTaxAction() {
		return new ManageItemTaxAction(messages.newItemTax());
	}

	public static NewItemAction getNewItemAction(boolean forCustomer) {
		return new NewItemAction(forCustomer);
	}

	// public static NewItemAction getNewItemAction(ClientItem item,
	// AccounterAsyncCallback<Object> callback) {
	// return new NewItemAction(messages.newItem(),
	// }

	public static DepreciationAction getDepriciationAction() {
		return new DepreciationAction();

	}

	public static NewTAXAgencyAction getNewTAXAgencyAction() {
		return new NewTAXAgencyAction();
	}

	public static ManageSalesTaxItemsAction getManageSalesTaxItemsAction() {
		return new ManageSalesTaxItemsAction();
	}

	// public static NewTaxItemAction getTaxItemAction() {
	// return new NewTaxItemAction(FinanceApplication.constants()
	// .newTaxItem());
	// }

	public static AdjustTAXAction getAdjustTaxAction() {
		return new AdjustTAXAction(2);
	}

	public static ChangePasswordAction getChangePasswordAction() {
		return new ChangePasswordAction();

	}

	public static UserDetailsAction getUserDetailsAction() {
		return new UserDetailsAction();
	}

	// public static ForgetPasswordAction getForgetPasswordAction(){
	// return new ForgetPasswordAction(messages.forgetPassword());
	//
	// }

	// Customers actions Factory

	public static NewCustomerAction getNewCustomerAction() {
		return new NewCustomerAction();
	}

	public static NewCustomerAction getNewCustomerAction(String quickAddText) {
		return new NewCustomerAction(quickAddText);
	}

	public static NewVendorAction getNewVendorAction(String quickAddText) {
		return new NewVendorAction(quickAddText);
	}

	public static NewCustomerAction getNewCustomerAction(
			ClientCustomer customer,
			AccounterAsyncCallback<Object> callBackObject) {
		return new NewCustomerAction(customer, callBackObject);
	}

	public static CustomersHomeAction getCustomersHomeAction() {
		return new CustomersHomeAction();
	}

	public static NewQuoteAction getNewQuoteAction(int type) {
		return new NewQuoteAction(type);
	}

	// public static NewQuoteAction getNewQuoteAction(ClientEstimate quote,
	// AccounterAsyncCallback<Object> callBackObject, int type) {
	// return new NewQuoteAction(quote, callBackObject,
	// type);
	// }

	// public static NewItemAction getNewItemAction(ClientItem item,
	// AccounterAsyncCallback<Object> callBackObject) {
	// return new NewItemAction(messages.newItem(),
	// }

	public static TaxDialogAction getTaxAction() {
		return new TaxDialogAction();
	}

	public static NewInvoiceAction getNewInvoiceAction() {
		return new NewInvoiceAction();
	}

	public static NewInvoiceAction getNewInvoiceAction(ClientInvoice invoice,
			AccounterAsyncCallback<Object> callBackObject) {
		return new NewInvoiceAction(invoice, callBackObject);
	}

	public static NewCashSaleAction getNewCashSaleAction() {
		return new NewCashSaleAction();
	}

	public static NewCashSaleAction getNewCashSaleAction(
			ClientCashSales cashSales,
			AccounterAsyncCallback<Object> callBackObject) {
		return new NewCashSaleAction(cashSales, callBackObject);
	}

	public static NewCreditsAndRefundsAction getNewCreditsAndRefundsAction() {
		return new NewCreditsAndRefundsAction();
	}

	// public static NewCreditsAndRefundsAction getNewCreditsAndRefundsAction(
	// ClientCustomerCreditMemo creditMemo,
	// AccounterAsyncCallback<Object> callBackObject) {
	// return new NewCreditsAndRefundsAction(messages.newCreditsAndRefunds(),
	// creditMemo, callBackObject);
	// }

	public static ReceivePaymentAction getReceivePaymentAction() {
		return new ReceivePaymentAction();
	}

	public static ReceivePaymentAction getReceivePaymentAction(
			ClientReceivePayment receivePayment,
			AccounterAsyncCallback<Object> callBackObject) {
		return new ReceivePaymentAction(messages.receivePayment(),
				receivePayment, callBackObject);
	}

	public static CustomerRefundAction getCustomerRefundAction() {
		return new CustomerRefundAction();
	}

	// public static CustomerRefundAction getCustomerRefundAction(
	// ClientCustomerRefund customerRefund,
	// AccounterAsyncCallback<Object> callBackObject) {
	// return new CustomerRefundAction(messages.customerRefund(),
	// callBackObject);
	// }

	public static CreateStatementAction getCreateStatementAction() {
		return new CreateStatementAction();
	}

	public static CustomersAction getCustomersAction() {
		return new CustomersAction();
	}

	public static ItemsAction getItemsAction(boolean customer, boolean vendor) {
		if (customer) {
			return new ItemsAction(Global.get().customer());
		} else if (vendor) {
			return new ItemsAction(Global.get().vendor());
		} else
			return new ItemsAction(messages.bothCustomerAndVendor(Global.get()
					.Customer(), Global.get().Vendor()));
	}

	public static QuotesAction getQuotesAction(int type) {
		return new QuotesAction(type);
	}

	public static ReceivedPaymentsAction getReceivedPaymentsAction() {
		return new ReceivedPaymentsAction();
	}

	public static InvoicesAction getInvoicesAction(String viewType) {
		return new InvoicesAction(viewType);
	}

	public static CustomerRefundsAction getCustomerRefundsAction() {
		return new CustomerRefundsAction();
	}

	public static NewSalesperSonAction getNewSalesperSonAction() {
		return new NewSalesperSonAction();
	}

	public static SalesPersonAction getSalesPersonAction() {
		return new SalesPersonAction();
	}

	public static CustomerPaymentsAction getNewCustomerPaymentAction() {
		return new CustomerPaymentsAction();
	}

	public static BrandingThemeComboAction getBrandingThemeComboAction() {
		return new BrandingThemeComboAction();
	}

	public static EmailThemeComboAction getEmailThemeComboAction() {
		return new EmailThemeComboAction();
	}

	public static EmailViewAction getEmailViewAction() {
		return new EmailViewAction();
	}

	public static InvoiceListViewAction getInvoiceListViewAction() {
		return new InvoiceListViewAction();
	}

	public static PaymentDialogAction getPaymentDialogAction() {
		return new PaymentDialogAction();
	}

	// Fixed Assests action factory

	public static NewFixedAssetAction getNewFixedAssetAction() {
		return new NewFixedAssetAction();
	}

	public static SellingRegisteredItemAction getSellingRegisteredItemAction() {
		return new SellingRegisteredItemAction();
	}

	public static DisposingRegisteredItemAction getDiposingRegisteredItemAction() {
		return new DisposingRegisteredItemAction();
	}

	public static PendingItemsListAction getPendingItemsListAction() {
		return new PendingItemsListAction();

	}

	public static RegisteredItemsListAction getRegisteredItemsListAction() {
		return new RegisteredItemsListAction();
	}

	public static SoldDisposedFixedAssetsListAction getSoldDisposedListAction() {
		return new SoldDisposedFixedAssetsListAction();
	}

	public static HistoryListAction getHistoryListAction() {
		return new HistoryListAction();
	}

	public static PurchaseOpenOrderAction getPurchaseOpenOrderListAction() {
		return new PurchaseOpenOrderAction();
	}

	public static ReportsHomeAction getReportsHomeAction() {
		return new ReportsHomeAction();
	}

	public static TAXAgencyListAction getTAXAgencyListAction() {
		return new TAXAgencyListAction();
	}

	public static BalanceSheetAction getBalanceSheetAction() {
		return new BalanceSheetAction();
	}

	public static CashFlowStatementAction getCashFlowStatementAction() {
		return new CashFlowStatementAction();
	}

	public static TrialBalanceAction getTrialBalanceAction() {
		return new TrialBalanceAction();
	}

	public static TransactionDetailByAccountAction getTransactionDetailByAccountAction() {
		return new TransactionDetailByAccountAction();
	}

	public static AutomaticTransactionsAction getAutomaticTransactionsAction() {
		return new AutomaticTransactionsAction();
	}

	public static GLReportAction getGlReportAction() {
		return new GLReportAction();
	}

	public static SalesTaxLiabilityAction getSalesTaxLiabilityAction() {
		return new SalesTaxLiabilityAction();
	}

	public static TransactionDetailByTaxItemAction getTransactionDetailByTaxItemAction() {
		return new TransactionDetailByTaxItemAction();
	}

	public static ARAgingDetailAction getArAgingDetailAction() {
		return new ARAgingDetailAction();
	}

	public static CustomerTransactionHistoryAction getCustomerTransactionHistoryAction() {
		return new CustomerTransactionHistoryAction();
	}

	public static MostProfitableCustomersAction getMostProfitableCustomersAction() {
		return new MostProfitableCustomersAction();
	}

	public static SalesByCustomerSummaryAction getSalesByCustomerSummaryAction() {
		return new SalesByCustomerSummaryAction();
	}

	public static SalesByCustomerDetailAction getSalesByCustomerDetailAction() {
		return new SalesByCustomerDetailAction();
	}

	public static SalesByItemSummaryAction getSalesByItemSummmaryAction() {
		return new SalesByItemSummaryAction();
	}

	public static SalesByItemDetailAction getSalesByItemDetailAction() {
		return new SalesByItemDetailAction();
	}

	public static APAgingDetailAction getAorpAgingDetailAction() {
		return new APAgingDetailAction();
	}

	public static VendorTransactionHistoryAction getVendorTransactionHistoryAction() {
		return new VendorTransactionHistoryAction();
	}

	public static ProfitAndLossAction getProfitAndLossAction() {
		return new ProfitAndLossAction();
	}

	public static InventoryItemReportAction getInventoryItemReportAction() {
		return new InventoryItemReportAction();
	}

	public static SalesByItemSummaryAction getSalesByItemSummaryAction() {
		return new SalesByItemSummaryAction();
	}

	public static PurchaseByVendorSummaryAction getPurchaseByVendorSummaryAction() {
		return new PurchaseByVendorSummaryAction();
	}

	public static PurchaseByVendorDetailsAction getPurchaseByVendorDetailAction() {
		return new PurchaseByVendorDetailsAction();
	}

	public static PurchaseByItemSummaryAction getPurchaseByItemSummaryAction() {
		return new PurchaseByItemSummaryAction();
	}

	public static PurchaseByItemDetailsAction getPurchaseByItemAction() {
		return new PurchaseByItemDetailsAction();
	}

	public static PurchaseOpenOrderAction getPurchaseOpenOrderAction() {
		return new PurchaseOpenOrderAction();
	}

	//
	// public static PurchaseClosedOrderAction getPurchaseClosedOrderAction() {
	// return new PurchaseClosedOrderAction();
	// }
	//
	public static SalesOpenOrderAction getSalesOpenOrderAction() {
		return new SalesOpenOrderAction();
	}

	//
	// public static SalesClosedOrderAction getSalesCloseOrderAction() {
	// return new SalesClosedOrderAction();
	// }

	public static VATDetailsReportAction getVATDetailsReportAction() {
		return new VATDetailsReportAction();
	}

	public static VATSummaryReportAction getVATSummaryReportAction() {
		return new VATSummaryReportAction();
	}

	public static VAT100ReportAction getVAT100ReportAction() {
		return new VAT100ReportAction();
	}

	public static VATUncategorisedAmountsReportAction getVATUncategorisedAmountsReportAction() {
		return new VATUncategorisedAmountsReportAction();
	}

	public static VATItemSummaryReportAction getVATItemSummaryReportAction() {
		return new VATItemSummaryReportAction();
	}

	public static ECSalesListAction getECSalesListAction() {
		return new ECSalesListAction();
	}

	public static ECSalesListDetailAction getECSalesListDetailAction() {
		return new ECSalesListDetailAction();
	}

	public static ReverseChargeListAction getReverseChargeListAction() {
		return new ReverseChargeListAction();
	}

	public static ReverseChargeListDetailAction getReverseChargeListDetailAction() {
		return new ReverseChargeListDetailAction();
	}

	public static VaTItemDetailAction getVaTItemDetailAction() {
		return new VaTItemDetailAction();
	}

	public static ARAgingSummaryReportAction getArAgingSummaryReportAction() {
		return new ARAgingSummaryReportAction();
	}

	public static APAgingSummaryReportAction getAorpAgingSummaryReportAction() {
		return new APAgingSummaryReportAction();
	}

	public static ExpenseReportAction getExpenseReportAction() {
		return new ExpenseReportAction();
	}

	public static DepositDetailAction getDetailReportAction() {
		return new DepositDetailAction();
	}

	public static CheckDetailReportAction getCheckDetailReport() {
		return new CheckDetailReportAction("");
	}

	public static StatementReportAction getStatementReport(boolean isVendor,
			long payeeId) {
		try {
			return new StatementReportAction(payeeId, isVendor);
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;
	}

	public static NewVatItemAction getNewVatItemAction() {
		return new NewVatItemAction();
	}

	public static FileTAXAction getFileTAXAction() {
		return new FileTAXAction();
	}

	public static VatItemListAction getVatItemListAction() {
		return new VatItemListAction();
	}

	public static ManageTAXCodesListAction getTAXCodeListAction() {
		return new ManageTAXCodesListAction();
	}

	public static NewTAXCodeAction getNewTAXCodeAction() {
		return new NewTAXCodeAction();
	}

	public static AdjustTAXAction getVatAdjustmentAction() {
		return new AdjustTAXAction(1);

	}

	public static PayTAXAction getpayTAXAction() {
		return new PayTAXAction();

	}

	public static ReceiveVATAction getreceiveVATAction() {
		return new ReceiveVATAction();
	}

	public static VendorsHomeAction getVendorsHomeAction() {
		return new VendorsHomeAction();
	}

	public static NewVendorAction getNewVendorAction() {
		return new NewVendorAction();
	}

	public static NewVendorAction getNewVendorAction(ClientVendor vendor,
			AccounterAsyncCallback<Object> callback) {
		return new NewVendorAction();
	}

	public static PurchaseItemsAction getPurchaseItemsAction() {
		return new PurchaseItemsAction(Global.get().Vendor());
	}

	public static NewCashPurchaseAction getNewCashPurchaseAction() {
		return new NewCashPurchaseAction();
	}

	public static NewCashPurchaseAction getNewCashPurchaseAction(
			ClientCashPurchase cashPurchase,
			AccounterAsyncCallback<Object> callback) {
		return new NewCashPurchaseAction(cashPurchase, callback);
	}

	public static NewCreditMemoAction getNewCreditMemoAction() {
		return new NewCreditMemoAction();
	}

	public static NewCheckAction getNewCheckAction() {
		return new NewCheckAction();
	}

	public static EnterBillsAction getEnterBillsAction() {
		return new EnterBillsAction();
	}

	public static PayBillsAction getPayBillsAction() {
		return new PayBillsAction();
	}

	public static IssuePaymentsAction getIssuePaymentsAction() {
		return new IssuePaymentsAction();
	}

	public static VendorPaymentsAction getNewVendorPaymentAction() {
		return new VendorPaymentsAction();
	}

	public static RecordExpensesAction getRecordExpensesAction() {
		return new RecordExpensesAction();
	}

	public static ServicesOverviewAction getServicesOverviewAction() {
		return new ServicesOverviewAction();
	}

	public static BuyChecksAndFormsAction getBuyChecksAndFormsAction() {
		return new BuyChecksAndFormsAction("");
	}

	public static VendorsListAction getVendorsAction() {
		return new VendorsListAction();
	}

	public static BillsAction getBillsAction() {
		return new BillsAction();
	}

	public static ExpensesAction getExpensesAction(String viewType) {
		return new ExpensesAction(viewType);
	}

	public static VendorPaymentsListAction getVendorPaymentsAction() {
		return new VendorPaymentsListAction();
	}

	public static PurchaseOrderAction getPurchaseOrderAction() {
		return new PurchaseOrderAction();
	}

	public static PurchaseOrderListAction getPurchaseOrderListAction() {
		return new PurchaseOrderListAction();
	}

	public static NewItemReceiptAction getItemReceiptAction() {
		return new NewItemReceiptAction();
	}

	public static CashExpenseAction CashExpenseAction() {
		return new CashExpenseAction();
	}

	public static EmployeeExpenseAction EmployeeExpenseAction() {
		return new EmployeeExpenseAction();
	}

	public static CreditCardExpenseAction CreditCardExpenseAction() {
		return new CreditCardExpenseAction();
	}

	public static AwaitingAuthorisationAction getAwaitingAuthorisationAction() {
		return new AwaitingAuthorisationAction();

	}

	public static PreviousClaimAction getPreviousClaimAction() {
		return new PreviousClaimAction();

	}

	public static ExpenseClaimsAction getExpenseClaimsAction(int selectedTab) {
		return new ExpenseClaimsAction(selectedTab);
	}

	public static MeasurementListAction getMeasurementsAction() {
		return new MeasurementListAction();
	}

	public static AddMeasurementAction getAddMeasurementAction() {
		return new AddMeasurementAction();
	}

	public static NewCurrencyAction getNewCurrencyAction() {
		return new NewCurrencyAction();
	}

	public static EditProfileAction getEditProfileAction() {
		return new EditProfileAction();
	}

	public static RecurringsListAction getRecurringsListAction() {
		return new RecurringsListAction();
	}

	public static RemindersListAction getRemindersListAction() {
		return new RemindersListAction();
	}

	public static Prepare1099MISCAction getPrepare1099MISCAction() {
		return new Prepare1099MISCAction();
	}

	public static SalesByLocationDetailsAction getSalesByLocationDetailsAction(
			boolean isLocation) {
		return new SalesByLocationDetailsAction(isLocation);
	}

	public static SalesByLocationSummaryAction getSalesByLocationSummaryAction(
			boolean isLocation) {

		return new SalesByLocationSummaryAction(isLocation);
	}

	public static ProfitAndLossByLocationAction getProfitAndLossByLocationAction(
			boolean isLocation) {

		return new ProfitAndLossByLocationAction(isLocation);
	}

	public static BudgetAction getBudgetActions() {
		return new BudgetAction();
	}

	public static NewBudgetAction getNewBudgetAction() {
		return new NewBudgetAction();
	}

	public static UsersActivityListAction getUsersActivityListAction() {
		return new UsersActivityListAction();
	}

	public static TDSVendorsListAction getTDSVendorsAction(boolean isTDSView) {
		return new TDSVendorsListAction(isTDSView);
	}

	public static NewReconcileAccountAction getNewReconciliationAction() {
		return new NewReconcileAccountAction();
	}

	public static ReconciliationsListAction getReconciliationsListAction() {
		return new ReconciliationsListAction();
	}

	public static LocationGroupListAction getLocationGroupListAction() {
		return new LocationGroupListAction();
	}

	public static AccounterClassListAction getAccounterClassGroupListAction() {
		return new AccounterClassListAction();
	}

	public static TDSPayAction getpayTDSAction() {
		return new TDSPayAction();
	}

	public static MISC1099TransactionDetailAction getMisc1099TransactionDetailAction() {
		return new MISC1099TransactionDetailAction();
	}

	public static StockSettingsAction getStockSettingsAction() {
		return new StockSettingsAction();
	}

	public static StockAdjustmentAction getStockAdjustmentAction() {
		return new StockAdjustmentAction();
	}

	public static StockAdjustmentsListAction getStockAdjustmentsListAction() {
		return new StockAdjustmentsListAction();
	}

	/*
	 * public static BudgetReportAction getBudgetReportsAction(int i) { switch
	 * (i) { case 1: return new BudgetReportAction(messages.accountVScustom(),
	 * i); case 2: return new BudgetReportAction(messages.accountVSmonths(), i);
	 * case 3: return new BudgetReportAction(messages.accountVSquaters(), i);
	 * case 4: return new BudgetReportAction(messages.accountVSyears(), i);
	 * default: return new BudgetReportAction(messages.accountVScustom(), i); }
	 * 
	 * }
	 */

	public static TaxHistoryAction getTaxHistoryAction() {
		return new TaxHistoryAction();
	}

	public static VatExceptionDetailReportAction getVATExceptionDetailsReportAction() {
		return new VatExceptionDetailReportAction();
	}

	public static TaxItemDetailReportAction getTaxItemDetailReportAction() {
		return new TaxItemDetailReportAction();
	}

	public static TAXItemExceptionDetailReport getTaxItemExceptionDetailReportAction() {
		return new TAXItemExceptionDetailReport();
	}

	public static TranslationAction getTranslationAction() {
		return new TranslationAction();
	}

	public static SearchInputAction getSearchInputAction() {
		return new SearchInputAction();
	}

	public static MergeCustomerAction getCustomerMergeAction() {
		return new MergeCustomerAction();
	}

	public static MergeVendorAction getVendorMergeAction() {
		return new MergeVendorAction();
	}

	public static MergeAccountsAction getAccountMergeAction() {
		return new MergeAccountsAction();
	}

	public static MergeItemsAction getItemMergeAction() {
		return new MergeItemsAction();
	}

	public static BudgetOverviewReportAction getBudgetOverView() {
		return new BudgetOverviewReportAction();
	}

	public static BudgetvsActualsAction getBudgetVsActionReport() {
		return new BudgetvsActualsAction();
	}

	public static AuditHistoryAction getAuditHistory(ClientActivity object) {
		return new AuditHistoryAction(object);

	}

	public static TransactionsCenterAction getTransactionCenterAction() {
		return new TransactionsCenterAction();
	}

	public static DepreciationSheduleAction getDepreciationSheduleAction() {
		return new DepreciationSheduleAction();
	}

	public static CheckPrintSettingAction getCheckPrintSettingAction() {
		return new CheckPrintSettingAction();
	}

	public static ReconcilationDetailByAccountAction getReconcilationDetailByAccount(
			long id) {
		return new ReconcilationDetailByAccountAction(id);
	}

	public static CustomerCentreAction getCustomerCentre() {
		return new CustomerCentreAction();
	}

	public static ReconcilationsAction getReconcilationsAction() {
		return new ReconcilationsAction();
	}

	public static TDSChalanDetailsAction getTDSChalanDetailsView() {
		return new TDSChalanDetailsAction();
	}

	public static TdsDeductorMasterAction getTdsDeductorMasters() {
		return new TdsDeductorMasterAction();
	}

	public static TDSResponsiblePersonAction getTDSResponsiblePerson() {
		return new TDSResponsiblePersonAction();
	}

	public static ChalanListViewAction getTDSChalanListView() {
		return new ChalanListViewAction();
	}

	public static ETdsFillingAction getETdsFillingView() {
		return new ETdsFillingAction();
	}

	public static TDSAcknowledgmentAction getTDSAcknowledgmentAction() {
		return new TDSAcknowledgmentAction();
	}

	public static TDSAcknowledgmentsReportAction getTDSAcknowledgmentsReportAction() {
		return new TDSAcknowledgmentsReportAction();
	}

	public static TDSFiledDetailsAction getTDSFiledDetailsAction() {
		return new TDSFiledDetailsAction();
	}

	public static VendorCenterAction getVendorCentreAction() {
		return new VendorCenterAction();
	}

	public static TDSForm16AAction getTDSForm16AAction() {
		return new TDSForm16AAction();
	}

	public static CreateIRASInformationFileAction getIARSInformationAction() {
		return new CreateIRASInformationFileAction();
	}

	// /**
	// * For Reconcilation
	// *
	// * @param accountid
	// *
	// * @return
	// */
	//
	// public static BankStatementAction getImportBankStatementAction() {
	// return new BankStatementAction(new ClientAccount());
	// }

	public static StatementReconcilationAction getStatementReconcilationAction(
			long accountid, ClientStatement statementId) {
		return new StatementReconcilationAction(accountid, statementId);
	}

	public static StatementImportViewAction getStatementImportViewAction(
			List<String[]> data, long accountId) {
		return new StatementImportViewAction(data, accountId);
	}

	public static Action<?> getBankFeesAction() {
		return null;
	}

	public static Action<?> getMinorAdjustmentsAction() {
		return null;
	}

	public static RealisedExchangeLossesAndGainsAction getRealisedExchangeLossesAndGainsAction() {
		return new RealisedExchangeLossesAndGainsAction();
	}

	public static UnRealisedExchangeLossesAndGainsAction getUnRealisedExchangeLossesAndGainsAction() {
		return new UnRealisedExchangeLossesAndGainsAction();
	}

	public static EnterExchangeRatesAction getEnterExchangeRatesAction() {
		return new EnterExchangeRatesAction();
	}

	public static DepositAction getDepositAction() {
		return new DepositAction();
	}

	public static BuildAssemblyAction getBuildAssemblyAction() {
		return new BuildAssemblyAction();
	}

	public static InventoryValuationDetailsAction getInventoryValuationDetailsAction(
			Long id) {
		return new InventoryValuationDetailsAction(id);
	}

	public static InventoryValutionSummaryReportAction getInventoryValutionSummaryReportAction() {
		return new InventoryValutionSummaryReportAction();
	}

	public static RecurringTransactionDialogAction getRecurringTransactionDialogAction() {
		return new RecurringTransactionDialogAction();
	}

	public static ImportAction getImportAction() {
		return new ImportAction();
	}

}
