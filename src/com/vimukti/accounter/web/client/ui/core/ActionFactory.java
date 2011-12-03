package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.banking.AccountRegisterAction;
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
import com.vimukti.accounter.web.client.ui.banking.SyncOnlinePayeesAction;
import com.vimukti.accounter.web.client.ui.banking.TransferFundsAction;
import com.vimukti.accounter.web.client.ui.banking.WriteChecksAction;
import com.vimukti.accounter.web.client.ui.combo.NewCurrencyAction;
import com.vimukti.accounter.web.client.ui.company.AccounterClassListAction;
import com.vimukti.accounter.web.client.ui.company.AuditHistoryAction;
import com.vimukti.accounter.web.client.ui.company.BudgetAction;
import com.vimukti.accounter.web.client.ui.company.ChangePasswordAction;
import com.vimukti.accounter.web.client.ui.company.ChartOfAccountsAction;
import com.vimukti.accounter.web.client.ui.company.CompanyHomeAction;
import com.vimukti.accounter.web.client.ui.company.CountryRegionListAction;
import com.vimukti.accounter.web.client.ui.company.CreditRatingListAction;
import com.vimukti.accounter.web.client.ui.company.CurrencyGroupListAction;
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
import com.vimukti.accounter.web.client.ui.company.UserDetailsAction;
import com.vimukti.accounter.web.client.ui.company.UsersActivityListAction;
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
import com.vimukti.accounter.web.client.ui.customers.SalesOrderAction;
import com.vimukti.accounter.web.client.ui.customers.SalesOrderListAction;
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
import com.vimukti.accounter.web.client.ui.reports.BalanceSheetAction;
import com.vimukti.accounter.web.client.ui.reports.BudgetOverviewReportAction;
import com.vimukti.accounter.web.client.ui.reports.CashFlowStatementAction;
import com.vimukti.accounter.web.client.ui.reports.CheckDetailReportAction;
import com.vimukti.accounter.web.client.ui.reports.CustomerTransactionHistoryAction;
import com.vimukti.accounter.web.client.ui.reports.ECSalesListAction;
import com.vimukti.accounter.web.client.ui.reports.ECSalesListDetailAction;
import com.vimukti.accounter.web.client.ui.reports.ExpenseReportAction;
import com.vimukti.accounter.web.client.ui.reports.GLReportAction;
import com.vimukti.accounter.web.client.ui.reports.MISC1099TransactionDetailAction;
import com.vimukti.accounter.web.client.ui.reports.MostProfitableCustomersAction;
import com.vimukti.accounter.web.client.ui.reports.ProfitAndLossAction;
import com.vimukti.accounter.web.client.ui.reports.ProfitAndLossByLocationAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByItemDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByItemSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByVendorDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByVendorSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseClosedOrderAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseOpenOrderAction;
import com.vimukti.accounter.web.client.ui.reports.ReportsHomeAction;
import com.vimukti.accounter.web.client.ui.reports.ReverseChargeListAction;
import com.vimukti.accounter.web.client.ui.reports.ReverseChargeListDetailAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByCustomerDetailAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByCustomerSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByItemDetailAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByItemSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByLocationDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByLocationSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.SalesClosedOrderAction;
import com.vimukti.accounter.web.client.ui.reports.SalesOpenOrderAction;
import com.vimukti.accounter.web.client.ui.reports.SalesTaxLiabilityAction;
import com.vimukti.accounter.web.client.ui.reports.StatementReportAction;
import com.vimukti.accounter.web.client.ui.reports.TAXItemExceptionDetailReport;
import com.vimukti.accounter.web.client.ui.reports.TaxItemDetailReportAction;
import com.vimukti.accounter.web.client.ui.reports.TransactionDetailByAccountAction;
import com.vimukti.accounter.web.client.ui.reports.TransactionDetailByTaxItemAction;
import com.vimukti.accounter.web.client.ui.reports.TrialBalanceAction;
import com.vimukti.accounter.web.client.ui.reports.VAT100ReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATDetailsReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATItemSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATUncategorisedAmountsReportAction;
import com.vimukti.accounter.web.client.ui.reports.VaTItemDetailAction;
import com.vimukti.accounter.web.client.ui.reports.VatExceptionDetailReportAction;
import com.vimukti.accounter.web.client.ui.reports.VendorTransactionHistoryAction;
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
import com.vimukti.accounter.web.client.ui.vat.FileTAXAction;
import com.vimukti.accounter.web.client.ui.vat.ManageTAXCodesListAction;
import com.vimukti.accounter.web.client.ui.vat.NewTAXCodeAction;
import com.vimukti.accounter.web.client.ui.vat.NewVatItemAction;
import com.vimukti.accounter.web.client.ui.vat.PayTAXAction;
import com.vimukti.accounter.web.client.ui.vat.ReceiveVATAction;
import com.vimukti.accounter.web.client.ui.vat.TAXAgencyListAction;
import com.vimukti.accounter.web.client.ui.vat.TaxHistoryAction;
import com.vimukti.accounter.web.client.ui.vat.VatItemListAction;
import com.vimukti.accounter.web.client.ui.vendors.AwaitingAuthorisationAction;
import com.vimukti.accounter.web.client.ui.vendors.BillsAction;
import com.vimukti.accounter.web.client.ui.vendors.CashExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.CreditCardExpenseAction;
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
	public static AccounterMessages messages = Accounter.messages();

	public static GeneralSettingsAction getGeneralSettingsAction() {
		return new GeneralSettingsAction(messages.generalSettings());
	}

	public static InventoryItemsAction getInventoryItemsAction() {
		return new InventoryItemsAction(messages.inventoryItems());
	}

	public static ConversionBalancesAction getConversionBalancesAction() {
		return new ConversionBalancesAction(messages.conversionBalance());
	}

	public static InvoiceBrandingAction getInvoiceBrandingAction() {
		return new InvoiceBrandingAction(messages.invoiceBranding());
	}

	public static NewBrandThemeAction getNewBrandThemeAction() {
		return new NewBrandThemeAction(messages.newBrandThemeLabel());

	}

	public static ConversionDateAction getConversionDateAction() {
		return new ConversionDateAction(messages.conversionDate());
	}

	public static AutomaticSequenceAction getAutomaticSequenceAction() {
		return new AutomaticSequenceAction(messages.automaticSequencing());
	}

	public static CustomThemeAction getCustomThemeAction() {
		return new CustomThemeAction(messages.newBrandThemeLabel());
	}

	public static UsersAction getUsersAction() {
		return new UsersAction(messages.users());
	}

	public static InviteUserAction getInviteUserAction() {
		return new InviteUserAction(messages.inviteUser());
	}

	public static DeleteThemeAction getDeleteThemeAction() {
		return new DeleteThemeAction(messages.users());
	}

	public static CopyThemeAction getCopyThemeAction() {
		return new CopyThemeAction(messages.copyTheme());
	}

	public static WareHouseViewAction getWareHouseViewAction() {
		return new WareHouseViewAction(messages.wareHouse());
	}

	public static WareHouseTransferAction getWareHouseTransferAction() {
		return new WareHouseTransferAction(messages.wareHouseTransfer());
	}

	public static WarehouseListAction getWarehouseListAction() {
		return new WarehouseListAction(messages.warehouseList());
	}

	public static WareHouseItemsListAction getWareHouseItemsListAction(
			long wareHouse) {
		return new WareHouseItemsListAction(wareHouse,
				messages.wareHouseItems());
	}

	public static WarehouseTransferListAction getWarehouseTransferListAction() {
		return new WarehouseTransferListAction(messages.warehouseTransferList());
	}

	// Banking action factory

	public static BankingHomeAction getBankingHomeAction() {
		return new BankingHomeAction(messages.bankingHome());
	}

	public static NewBankAccountAction getNewBankAccountAction() {
		return new NewBankAccountAction(messages.newAccount());
	}

	public static AccountRegisterAction getAccountRegisterAction() {
		return new AccountRegisterAction(messages.accountRegister());

	}

	public static WriteChecksAction getWriteChecksAction() {
		return new WriteChecksAction(messages.writeCheck());
	}

	public static WriteChecksAction getWriteChecksAction(
			ClientWriteCheck writeCheck,
			AccounterAsyncCallback<Object> callBackObject) {
		return new WriteChecksAction(messages.writeCheck(), writeCheck,
				callBackObject);
	}

	public static MakeDepositAction getMakeDepositAction() {
		return new MakeDepositAction(messages.makeDeposit());
	}

	public static MakeDepositAction getMakeDepositAction(
			ClientMakeDeposit makeDeposit,
			AccounterAsyncCallback<Object> callBackObject) {
		return new MakeDepositAction(messages.makeDeposit(), makeDeposit,
				callBackObject);
	}

	public static TransferFundsAction getTransferFundsAction() {
		return new TransferFundsAction(messages.transferFunds());
	}

	public static EnterPaymentsAction getEnterPaymentsAction() {
		return new EnterPaymentsAction(messages.enterPayments());
	}

	public static SyncOnlinePayeesAction getSyncOnlinePayeesAction() {
		return new SyncOnlinePayeesAction(messages.syncOnlinePayees());
	}

	public static ImportBankFilesAction getImportBankFilesAction() {
		return new ImportBankFilesAction(messages.importBankFiles());
	}

	public static CreditCardChargeAction getCreditCardChargeAction() {
		return new CreditCardChargeAction(messages.creditCardCharge());
	}

	public static PrintChecksAction getPrintChecksAction() {
		return new PrintChecksAction(messages.printChecks());
	}

	public static ChartsOfAccountsAction getChartsOfAccountsAction() {
		return new ChartsOfAccountsAction(messages.chartOfAccounts());
	}

	public static PaymentsAction getPaymentsAction(int category) {
		PaymentsAction action = new PaymentsAction(messages.payments(),
				category);
		return action;
	}

	public static MatchTrasactionsAction getMatchTrasactionsAction() {
		return new MatchTrasactionsAction(messages.matchTrasactions());
	}

	// ActionFactory

	public static CompanyHomeAction getCompanyHomeAction() {
		return new CompanyHomeAction(messages.home());
	}

	public static MakeActiveAction getMakeActiveAction() {
		return new MakeActiveAction(messages.makeActive());

	}

	public static MakeInActiveAction getMakeInActiveAction() {
		return new MakeInActiveAction(messages.makeInActive());

	}

	public static PreferencesAction getPreferencesAction() {
		return new PreferencesAction(messages.companyPreferences());
	}

	public static IntegrateWithBusinessContactManagerAction getIntegrateWithBusinessContactManagerAction() {
		return new IntegrateWithBusinessContactManagerAction(
				messages.integrateWithBusinessContactManager());
	}

	public static NewJournalEntryAction getNewJournalEntryAction() {
		return new NewJournalEntryAction(messages.newJournalEntry());
	}

	public static NewCashBasisJournalEntryAction getNewCashBasisJournalEntryAction() {
		return new NewCashBasisJournalEntryAction(
				messages.newCashBasisJournalEntry());
	}

	public static NewAccountAction getNewAccountAction() {
		return new NewAccountAction(messages.newAccount());
	}

	public static MergeAccountsAction getMergeVendorsAction() {
		return new MergeAccountsAction(messages.mergeVendors(Global.get()
				.vendor()));
	}

	public static MergeItemsAction getMergeItemsAction() {
		return new MergeItemsAction(messages.mergeItems());
	}

	public static MergeFinancialAccountsAction getMergeFinancialAccountsAction() {
		return new MergeFinancialAccountsAction(
				messages.mergeFinancialAccounts());
	}

	public static ManageSalesTaxGroupsAction getManageSalesTaxGroupsAction() {
		String text;
		if (Accounter.getUser().canDoInvoiceTransactions())
			text = messages.manageSalesGroups();
		else
			text = messages.salesTaxGroups();
		return new ManageSalesTaxGroupsAction(text);
	}

	// public static ManageSalesTaxCodesAction getManageSalesTaxCodesAction() {
	// String constant = messages.manageSalesTaxCodes();
	// return new ManageSalesTaxCodesAction(constant);
	// }

	public static ManageItemTaxAction getManageItemTaxAction() {
		return new ManageItemTaxAction(messages.manageItemTax());
	}

	public static PaySalesTaxAction getPaySalesTaxAction() {
		String constant = null;
		constant = messages.payTax();
		return new PaySalesTaxAction(constant);
	}

	public static SalesTaxLiabilityAction getViewSalesTaxLiabilityAction() {
		return new SalesTaxLiabilityAction(messages.salesTaxLiability());
	}

	// public static NewTaxAgencyAction getNewTaxAgencyAction() {
	// return new NewTaxAgencyAction(messages.newTaxAgency());
	// }

	public static CustomerGroupListAction getCustomerGroupListAction() {
		return new CustomerGroupListAction(messages.payeeGroupList(Global.get()
				.Customer()));
	}

	public static VendorGroupListAction getVendorGroupListAction() {
		return new VendorGroupListAction(Global.get().messages()
				.payeeGroupList(Global.get().Vendor()));
	}

	public static PaymentTermListAction getPaymentTermListAction() {
		return new PaymentTermListAction(messages.paymentTermList());
	}

	public static ShippingMethodListAction getShippingMethodListAction() {
		return new ShippingMethodListAction(messages.shippingMethodList());
	}

	public static ShippingTermListAction getShippingTermListAction() {
		return new ShippingTermListAction(messages.shippingTermList());
	}

	public static PriceLevelListAction getPriceLevelListAction() {
		return new PriceLevelListAction(messages.priceLevelList());
	}

	public static ItemGroupListAction getItemGroupListAction() {
		return new ItemGroupListAction(messages.itemGroupList());
	}

	public static CreditRatingListAction getCreditRatingListAction() {
		return new CreditRatingListAction(messages.creditRatingList());
	}

	public static CountryRegionListAction getCountryRegionListAction() {
		return new CountryRegionListAction(messages.countryRegionList());
	}

	public static CurrencyGroupListAction getCurrencyGroupListAction() {
		return new CurrencyGroupListAction(messages.currencyList());
	}

	public static FormLayoutsListAction getFormLayoutsListAction() {
		return new FormLayoutsListAction(messages.formLayoutsList());
	}

	public static PayTypeListAction getPayTypeListAction() {
		return new PayTypeListAction(messages.payTypeList());
	}

	public static ManageFiscalYearAction getManageFiscalYearAction() {
		return new ManageFiscalYearAction(messages.manageFiscalYear());
	}

	public static ChartOfAccountsAction getChartOfAccountsAction() {
		return new ChartOfAccountsAction(messages.accounterCategoryList());
	}

	public static ChartOfAccountsAction getChartOfAccountsAction(int accountType) {
		return new ChartOfAccountsAction(messages.accounterCategoryList(),
				accountType);
	}

	public static SalesPersonListsAction getSalesPersonListAction() {
		return new SalesPersonListsAction(messages.salesPersons());
	}

	public static JournalEntriesAction getJournalEntriesAction() {
		return new JournalEntriesAction(messages.journalEntries());
	}

	public static NewbankAction getNewbankAction() {
		return new NewbankAction(messages.newBank());
	}

	public static ManageItemTaxAction getNewItemTaxAction() {
		return new ManageItemTaxAction(messages.newItemTax());
	}

	public static NewItemAction getNewItemAction(boolean forCustomer) {
		return new NewItemAction(messages.newItem(), forCustomer);
	}

	// public static NewItemAction getNewItemAction(ClientItem item,
	// AccounterAsyncCallback<Object> callback) {
	// return new NewItemAction(messages.newItem(),
	// }

	public static DepreciationAction getDepriciationAction() {
		return new DepreciationAction(messages.depreciation());

	}

	public static NewTAXAgencyAction getNewTAXAgencyAction() {
		String flag = messages.newTAXAgency();

		return new NewTAXAgencyAction(flag);
	}

	public static ManageSalesTaxItemsAction getManageSalesTaxItemsAction() {
		String constant;
		if (Accounter.getUser().canDoInvoiceTransactions())
			constant = messages.manageSalesItems();
		else
			constant = messages.salesTaxItems();
		return new ManageSalesTaxItemsAction(constant);
	}

	// public static NewTaxItemAction getTaxItemAction() {
	// return new NewTaxItemAction(FinanceApplication.constants()
	// .newTaxItem());
	// }

	public static AdjustTAXAction getAdjustTaxAction() {
		String flag = messages.taxAdjustment();
		return new AdjustTAXAction(flag);
	}

	public static ChangePasswordAction getChangePasswordAction() {
		return new ChangePasswordAction(messages.changePassword());

	}

	public static UserDetailsAction getUserDetailsAction() {
		return new UserDetailsAction(messages.userDetails());
	}

	// public static ForgetPasswordAction getForgetPasswordAction(){
	// return new ForgetPasswordAction(messages.forgetPassword());
	//
	// }

	// Customers actions Factory

	public static NewCustomerAction getNewCustomerAction() {
		return new NewCustomerAction(messages.newPayee(Global.get().Customer()));
	}

	public static NewCustomerAction getNewCustomerAction(String quickAddText) {
		return new NewCustomerAction(
				messages.newPayee(Global.get().Customer()), quickAddText);
	}

	public static NewVendorAction getNewVendorAction(String quickAddText) {
		return new NewVendorAction(messages.newPayee(Global.get().vendor()),
				quickAddText);
	}

	public static NewCustomerAction getNewCustomerAction(
			ClientCustomer customer,
			AccounterAsyncCallback<Object> callBackObject) {
		return new NewCustomerAction(
				messages.newPayee(Global.get().Customer()), customer,
				callBackObject);
	}

	public static CustomersHomeAction getCustomersHomeAction() {
		return new CustomersHomeAction(messages.payeesHome(Global.get()
				.Customer().trim()));
	}

	public static NewQuoteAction getNewQuoteAction(int type, String title) {
		return new NewQuoteAction(title, type);
	}

	public static NewQuoteAction getNewQuoteAction(ClientEstimate quote,
			AccounterAsyncCallback<Object> callBackObject, int type) {
		return new NewQuoteAction(messages.newQuote(), quote, callBackObject,
				type);
	}

	// public static NewItemAction getNewItemAction(ClientItem item,
	// AccounterAsyncCallback<Object> callBackObject) {
	// return new NewItemAction(messages.newItem(),
	// }

	public static TaxDialogAction getTaxAction() {
		return new TaxDialogAction(messages.tax());
	}

	public static NewInvoiceAction getNewInvoiceAction() {
		return new NewInvoiceAction(messages.newInvoice());
	}

	public static NewInvoiceAction getNewInvoiceAction(ClientInvoice invoice,
			AccounterAsyncCallback<Object> callBackObject) {
		return new NewInvoiceAction(messages.newInvoice(), invoice,
				callBackObject);
	}

	public static NewCashSaleAction getNewCashSaleAction() {
		return new NewCashSaleAction(messages.newCashSale());
	}

	public static NewCashSaleAction getNewCashSaleAction(
			ClientCashSales cashSales,
			AccounterAsyncCallback<Object> callBackObject) {
		return new NewCashSaleAction(messages.newCashSale(), cashSales,
				callBackObject);
	}

	public static NewCreditsAndRefundsAction getNewCreditsAndRefundsAction() {
		return new NewCreditsAndRefundsAction(messages.newCreditNotes());
	}

	public static NewCreditsAndRefundsAction getNewCreditsAndRefundsAction(
			ClientCustomerCreditMemo creditMemo,
			AccounterAsyncCallback<Object> callBackObject) {
		return new NewCreditsAndRefundsAction(messages.newCreditsAndRefunds(),
				creditMemo, callBackObject);
	}

	public static ReceivePaymentAction getReceivePaymentAction() {
		return new ReceivePaymentAction(messages.receivePayment());
	}

	public static ReceivePaymentAction getReceivePaymentAction(
			ClientReceivePayment receivePayment,
			AccounterAsyncCallback<Object> callBackObject) {
		return new ReceivePaymentAction(messages.receivePayment(),
				receivePayment, callBackObject);
	}

	public static CustomerRefundAction getCustomerRefundAction() {
		return new CustomerRefundAction(messages.customerRefund(Global.get()
				.Customer()));
	}

	// public static CustomerRefundAction getCustomerRefundAction(
	// ClientCustomerRefund customerRefund,
	// AccounterAsyncCallback<Object> callBackObject) {
	// return new CustomerRefundAction(messages.customerRefund(),
	// callBackObject);
	// }

	public static CreateStatementAction getCreateStatementAction() {
		return new CreateStatementAction(messages.createStatement());
	}

	public static CustomersAction getCustomersAction() {
		return new CustomersAction(messages.payees(Global.get().Customer()));
	}

	public static ItemsAction getItemsAction(boolean customer, boolean vendor) {
		if (customer) {
			return new ItemsAction(messages.items(), Global.get().customer());
		} else if (vendor) {
			return new ItemsAction(messages.items(), Global.get().vendor());
		} else
			return new ItemsAction(messages.items(), Accounter.messages()
					.bothCustomerAndVendor(Global.get().Customer(),
							Global.get().Vendor()));
	}

	public static QuotesAction getQuotesAction(String title, int type) {
		return new QuotesAction(title, type);
	}

	public static ReceivedPaymentsAction getReceivedPaymentsAction() {
		return new ReceivedPaymentsAction(messages.receivedPayments());
	}

	public static InvoicesAction getInvoicesAction(String viewType) {
		return new InvoicesAction(messages.invoices(), viewType);
	}

	public static CustomerRefundsAction getCustomerRefundsAction() {
		return new CustomerRefundsAction(messages.customerRefunds(Global.get()
				.Customer()));
	}

	public static SalesOrderAction getSalesOrderAction() {
		return new SalesOrderAction(messages.newSalesOrder());
	}

	public static SalesOrderListAction getSalesOrderListAction() {
		return new SalesOrderListAction(messages.salesOrderList());
	}

	public static NewSalesperSonAction getNewSalesperSonAction() {
		return new NewSalesperSonAction(messages.newSalesPerson());
	}

	public static SalesPersonAction getSalesPersonAction() {
		return new SalesPersonAction(messages.salesPersons());
	}

	public static CustomerPaymentsAction getNewCustomerPaymentAction() {
		return new CustomerPaymentsAction(messages.payeePrePayment(Global.get()
				.Customer()));
	}

	public static BrandingThemeComboAction getBrandingThemeComboAction() {
		return new BrandingThemeComboAction(messages.brandingThemeCombo());
	}

	public static EmailViewAction getEmailViewAction() {
		return new EmailViewAction(messages.email());
	}

	public static InvoiceListViewAction getInvoiceListViewAction() {
		return new InvoiceListViewAction("");
	}

	public static PaymentDialogAction getPaymentDialogAction() {
		return new PaymentDialogAction(messages.payments());
	}

	// Fixed Assests action factory

	public static NewFixedAssetAction getNewFixedAssetAction() {
		return new NewFixedAssetAction(messages.newFixedAsset());
	}

	public static SellingRegisteredItemAction getSellingRegisteredItemAction() {
		return new SellingRegisteredItemAction(messages.sellingRegisteredItem());
	}

	public static DisposingRegisteredItemAction getDiposingRegisteredItemAction() {
		return new DisposingRegisteredItemAction(
				messages.disposingRegisteredItem());
	}

	public static PendingItemsListAction getPendingItemsListAction() {
		return new PendingItemsListAction(messages.pendingItemsList());

	}

	public static RegisteredItemsListAction getRegisteredItemsListAction() {
		return new RegisteredItemsListAction(messages.registeredItemsList());
	}

	public static SoldDisposedFixedAssetsListAction getSoldDisposedListAction() {
		return new SoldDisposedFixedAssetsListAction(
				messages.soldDisposedItems());
	}

	public static HistoryListAction getHistoryListAction() {
		return new HistoryListAction(messages.history());
	}

	// Purchase order action

	// public static PurchaseOrderAction getPurchaseOrderAction() {
	// return new PurchaseOrderAction("Purchase Order",
	// }

	// public static PurchaseOrderListAction getPurchaseOrderListAction() {
	// return new PurchaseOrderListAction("PurchaseOrder List",
	// }

	public static PurchaseOpenOrderAction getPurchaseOpenOrderListAction() {
		return new PurchaseOpenOrderAction(messages.purchaseOrderReport());
	}

	// reports action factory

	public static ReportsHomeAction getReportsHomeAction() {
		return new ReportsHomeAction(messages.reportsHome());
	}

	public static VendorsListAction getVendorListAction() {
		return new VendorsListAction(Global.get().messages()
				.payeesList(Global.get().Vendor()));
	}

	public static TAXAgencyListAction getTAXAgencyListAction() {
		return new TAXAgencyListAction(messages.payeesList(messages
				.taxAgencie()));
	}

	public static BalanceSheetAction getBalanceSheetAction() {
		return new BalanceSheetAction(messages.balanceSheet());
	}

	public static CashFlowStatementAction getCashFlowStatementAction() {
		return new CashFlowStatementAction(messages.cashFlowReport());
	}

	public static TrialBalanceAction getTrialBalanceAction() {
		return new TrialBalanceAction(messages.trialBalance());
	}

	public static TransactionDetailByAccountAction getTransactionDetailByAccountAction() {
		return new TransactionDetailByAccountAction(
				messages.transactionDetailByAccount());
	}

	public static GLReportAction getGlReportAction() {
		return new GLReportAction(messages.generalLedgerReport());
	}

	public static SalesTaxLiabilityAction getSalesTaxLiabilityAction() {
		return new SalesTaxLiabilityAction(messages.salesTaxLiability());
	}

	public static TransactionDetailByTaxItemAction getTransactionDetailByTaxItemAction() {
		return new TransactionDetailByTaxItemAction(
				messages.transactionDetailByTaxItem());
	}

	// public static YtdProfitComparedToLastYearAction
	// getYtdProfitComparedToLastYearAction() {
	// return new YtdProfitComparedToLastYearAction(
	// }

	public static ARAgingDetailAction getArAgingDetailAction() {
		return new ARAgingDetailAction(messages.arAgeingDetail());
	}

	public static CustomerTransactionHistoryAction getCustomerTransactionHistoryAction() {
		return new CustomerTransactionHistoryAction(
				messages.payeeTransactionHistory(Global.get().Customer()));
	}

	public static MostProfitableCustomersAction getMostProfitableCustomersAction() {
		return new MostProfitableCustomersAction(
				messages.mostProfitableCustomers(Global.get().customer()));
	}

	public static SalesByCustomerSummaryAction getSalesByCustomerSummaryAction() {
		return new SalesByCustomerSummaryAction(
				messages.salesByCustomerSummary(Global.get().customer()));
	}

	public static SalesByCustomerDetailAction getSalesByCustomerDetailAction() {
		return new SalesByCustomerDetailAction(
				messages.salesByCustomerDetail(Global.get().Customer()));
	}

	public static SalesByItemSummaryAction getSalesByItemSummmaryAction() {
		return new SalesByItemSummaryAction(messages.salesByItemSummary());
	}

	public static SalesByItemDetailAction getSalesByItemDetailAction() {
		return new SalesByItemDetailAction(messages.salesByItemDetail());
	}

	// public static YtdSalesComparedToLastYearAction
	// getYtdSalesComparedToLastYearAction() {
	// return new YtdSalesComparedToLastYearAction(
	// }

	public static APAgingDetailAction getAorpAgingDetailAction() {
		return new APAgingDetailAction(messages.apAgeingDetail());
	}

	public static VendorTransactionHistoryAction getVendorTransactionHistoryAction() {
		return new VendorTransactionHistoryAction(Global.get().messages()
				.payeeTransactionHistory(Global.get().Vendor()));
	}

	// public static AmountsDueToVendorsAction getAmountsDueToVendorsAction() {
	// return new AmountsDueToVendorsAction("Amounts Due To"
	// + UIUtils.getVendorString("Suppliers", "Vendors"),
	// }

	public static ProfitAndLossAction getProfitAndLossAction() {
		return new ProfitAndLossAction(messages.profitAndLoss());
	}

	public static SalesByItemSummaryAction getSalesByItemSummaryAction() {
		return new SalesByItemSummaryAction(messages.salesByItemSummary());
	}

	public static PurchaseByVendorSummaryAction getPurchaseByVendorSummaryAction() {
		return new PurchaseByVendorSummaryAction(Global.get().messages()
				.purchaseByVendorSummary(Global.get().Vendor()));
	}

	public static PurchaseByVendorDetailsAction getPurchaseByVendorDetailAction() {
		return new PurchaseByVendorDetailsAction(Global.get().messages()
				.purchaseByVendorDetail(Global.get().Vendor()));
	}

	public static PurchaseByItemSummaryAction getPurchaseByItemSummaryAction() {
		return new PurchaseByItemSummaryAction(messages.purchaseByItemSummary());
	}

	public static PurchaseByItemDetailsAction getPurchaseByItemAction() {
		return new PurchaseByItemDetailsAction(messages.purchaseByItemDetail());
	}

	public static PurchaseOpenOrderAction getPurchaseOpenOrderAction() {
		return new PurchaseOpenOrderAction(messages.purchaseOrderReport());
	}

	public static PurchaseClosedOrderAction getPurchaseClosedOrderAction() {
		return new PurchaseClosedOrderAction(messages.purchaseClosedOrder());
	}

	public static SalesOpenOrderAction getSalesOpenOrderAction() {
		return new SalesOpenOrderAction(messages.salesOrderReport());
	}

	public static SalesClosedOrderAction getSalesCloseOrderAction() {
		return new SalesClosedOrderAction(messages.salesCloseOrder());
	}

	public static VATDetailsReportAction getVATDetailsReportAction() {
		return new VATDetailsReportAction(messages.vatDetail());
	}

	public static VATSummaryReportAction getVATSummaryReportAction() {
		return new VATSummaryReportAction(messages.priorVATReturns());
	}

	public static VAT100ReportAction getVAT100ReportAction() {
		return new VAT100ReportAction(messages.vat100());
	}

	public static VATUncategorisedAmountsReportAction getVATUncategorisedAmountsReportAction() {
		return new VATUncategorisedAmountsReportAction(
				messages.uncategorisedVATAmounts());
	}

	public static VATItemSummaryReportAction getVATItemSummaryReportAction() {
		return new VATItemSummaryReportAction(messages.vatItemSummary());
	}

	public static ECSalesListAction getECSalesListAction() {
		return new ECSalesListAction(messages.ecSalesList());
	}

	public static ECSalesListDetailAction getECSalesListDetailAction() {
		return new ECSalesListDetailAction(messages.ecSalesListDetailReport());
	}

	public static ReverseChargeListAction getReverseChargeListAction() {
		return new ReverseChargeListAction(messages.reverseChargeList());
	}

	public static ReverseChargeListDetailAction getReverseChargeListDetailAction() {
		return new ReverseChargeListDetailAction(
				messages.reverseChargeListDetailReport());
	}

	public static VaTItemDetailAction getVaTItemDetailAction() {
		return new VaTItemDetailAction(messages.vatItemDetailReport());
	}

	public static ARAgingSummaryReportAction getArAgingSummaryReportAction() {
		return new ARAgingSummaryReportAction(messages.arAgeingSummary());
	}

	public static APAgingSummaryReportAction getAorpAgingSummaryReportAction() {
		return new APAgingSummaryReportAction(messages.apAgeingSummary());
	}

	public static ExpenseReportAction getExpenseReportAction() {
		return new ExpenseReportAction(messages.expenseReport());
	}

	public static DepositDetailAction getDetailReportAction() {
		return new DepositDetailAction(messages.depositDetail());
	}

	public static CheckDetailReportAction getCheckDetailReport() {
		return new CheckDetailReportAction(messages.checkDetail(), "");
	}

	public static StatementReportAction getStatementReport(boolean isVendor,
			long payeeId) {
		try {
			if (isVendor) {
				return new StatementReportAction(payeeId,
						messages.payeeStatement(Global.get().Vendor()), true);
			} else {
				return new StatementReportAction(payeeId,
						messages.payeeStatement(Global.get().Customer()), false);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;
	}

	// sales order action factory

	// public static SalesOrderAction getSalesOrderAction() {
	// return new SalesOrderAction("Sales Order",
	// "/images/Sales-order.png");
	// }

	// public static SalesOrderListAction getSalesOrderListAction() {
	// return new SalesOrderListAction("SalesOrder List",
	// "/images/Sales-order-list.png");
	// }

	// public static SalesOpenOrderAction getSalesOpenOrderAction() {
	// return new SalesOpenOrderAction("SalesOrder Report",
	// "/images/icons/report/reports.png");
	// }

	// vat action factory

	public static NewVatItemAction getNewVatItemAction() {
		String flag = messages.newTaxItem();
		return new NewVatItemAction(flag);
	}

	public static FileTAXAction getFileTAXAction() {
		return new FileTAXAction(messages.fileTAX());
	}

	public static VatItemListAction getVatItemListAction() {
		return new VatItemListAction(messages.taxItemsList());
	}

	// public static VatGroupAction getVatGroupAction() {
	// return new VatGroupAction(messages.newVATGroup());
	// }

	public static ManageTAXCodesListAction getTAXCodeListAction() {
		return new ManageTAXCodesListAction(messages.taxCodesList());
	}

	public static NewTAXCodeAction getNewTAXCodeAction() {
		return new NewTAXCodeAction(messages.newTaxCode());
	}

	// public static Action getManageVATCodeAction() {
	// return new ManageVATCodeAction("Manage VAT");
	// }

	// public static NewTAXAgencyAction getNewTAXAgencyAction() {
	// return new NewTAXAgencyAction(messages.newVATAgency());
	// }

	// public static ManageVATGroupListAction getManageVATGroupListAction() {
	// return new ManageVATGroupListAction(messages.vatGroupList());
	// }

	public static AdjustTAXAction getVatAdjustmentAction() {
		return new AdjustTAXAction(messages.vatAdjustment());

	}

	public static PayTAXAction getpayTAXAction() {
		return new PayTAXAction(messages.payTax());

	}

	public static ReceiveVATAction getreceiveVATAction() {
		return new ReceiveVATAction(messages.tAXRefund());
	}

	public static VendorsHomeAction getVendorsHomeAction() {
		return new VendorsHomeAction(Global.get().messages()
				.payeesHome(Global.get().Vendor().trim()));
	}

	public static NewVendorAction getNewVendorAction() {
		return new NewVendorAction(Global.get().messages()
				.newPayee(Global.get().Vendor()));
	}

	public static NewVendorAction getNewVendorAction(ClientVendor vendor,
			AccounterAsyncCallback<Object> callback) {
		return new NewVendorAction(Global.get().messages()
				.newPayee(Global.get().Vendor()));
	}

	public static PurchaseItemsAction getPurchaseItemsAction() {
		return new PurchaseItemsAction(messages.items(), Global.get().Vendor());
	}

	// public static NewItemAction getNewItemAction() {
	// return new NewItemAction(messages.newItem(),
	// "/images/icons/vendors/new_item.png", null);
	// }
	//
	// public static NewItemAction getNewItemAction(Item item,
	// AccounterAsyncCallback<Object> callback, AbstractBaseView view) {
	// return new NewItemAction(messages.newItem(),
	// "/images/icons/vendors/new_item.png", view, item, callback);
	// }

	public static NewCashPurchaseAction getNewCashPurchaseAction() {
		return new NewCashPurchaseAction(messages.newCashPurchase());
	}

	public static NewCashPurchaseAction getNewCashPurchaseAction(
			ClientCashPurchase cashPurchase,
			AccounterAsyncCallback<Object> callback) {
		return new NewCashPurchaseAction(messages.newCashPurchase(),
				cashPurchase, callback);
	}

	public static NewCreditMemoAction getNewCreditMemoAction() {
		return new NewCreditMemoAction(Global.get().messages()
				.payeeCredit(Global.get().Vendor()));
	}

	public static NewCreditMemoAction getNewCreditMemoAction(
			ClientVendorCreditMemo vendorCreditMemo,
			AccounterAsyncCallback<Object> callBack) {
		return new NewCreditMemoAction(messages.newCreditMemo(),
				vendorCreditMemo, callBack);
	}

	public static NewCheckAction getNewCheckAction() {
		return new NewCheckAction(messages.newCheck());
	}

	public static EnterBillsAction getEnterBillsAction() {
		return new EnterBillsAction(messages.enterBill());
	}

	public static PayBillsAction getPayBillsAction() {
		return new PayBillsAction(messages.payBill());
	}

	public static IssuePaymentsAction getIssuePaymentsAction() {
		return new IssuePaymentsAction(messages.issuePayment());
	}

	public static VendorPaymentsAction getNewVendorPaymentAction() {
		return new VendorPaymentsAction(Global.get().messages()
				.payeePrePayment(Global.get().Vendor()));
	}

	public static RecordExpensesAction getRecordExpensesAction() {
		return new RecordExpensesAction(messages.recordExpenses());
	}

	public static ServicesOverviewAction getServicesOverviewAction() {
		return new ServicesOverviewAction(messages.servicesOverview());
	}

	public static BuyChecksAndFormsAction getBuyChecksAndFormsAction() {
		return new BuyChecksAndFormsAction(messages.buyChecksAndForms(), "");
	}

	public static VendorsListAction getVendorsAction() {
		return new VendorsListAction(Global.get().messages()
				.payees(Global.get().Vendor()));
	}

	// public static Item getItemAction() {
	// return new ItemListAction(messages.items());
	// }

	public static BillsAction getBillsAction() {
		return new BillsAction(messages.billsAndItemReceipts());
	}

	public static ExpensesAction getExpensesAction(String viewType) {
		return new ExpensesAction(messages.recordExpenses(), viewType);
	}

	public static VendorPaymentsListAction getVendorPaymentsAction() {

		return new VendorPaymentsListAction(Global.get().messages()
				.payeePayment(Global.get().Vendor()));
	}

	public static PurchaseOrderAction getPurchaseOrderAction() {
		return new PurchaseOrderAction(messages.purchaseOrder());
	}

	public static PurchaseOrderListAction getPurchaseOrderListAction() {
		return new PurchaseOrderListAction(messages.purchaseOrderList());
	}

	public static NewItemReceiptAction getItemReceiptAction() {
		return new NewItemReceiptAction(messages.itemReceipt());
	}

	public static CashExpenseAction CashExpenseAction() {
		return new CashExpenseAction(messages.cashExpense());
	}

	public static EmployeeExpenseAction EmployeeExpenseAction() {
		return new EmployeeExpenseAction(messages.employeeExpense());
	}

	public static CreditCardExpenseAction CreditCardExpenseAction() {
		return new CreditCardExpenseAction(messages.creditCardCharge());
	}

	public static AwaitingAuthorisationAction getAwaitingAuthorisationAction() {
		return new AwaitingAuthorisationAction(messages.awaitingAuthorisation());

	}

	public static PreviousClaimAction getPreviousClaimAction() {
		return new PreviousClaimAction(messages.previousClaims());

	}

	public static ExpenseClaimsAction getExpenseClaimsAction(int selectedTab) {
		return new ExpenseClaimsAction(messages.expenseClaims(), selectedTab);
	}

	public static MeasurementListAction getMeasurementsAction() {
		return new MeasurementListAction(messages.measurement());
	}

	public static AddMeasurementAction getAddMeasurementAction() {
		return new AddMeasurementAction(messages.addMeasurementName());
	}

	public static NewCurrencyAction getNewCurrencyAction() {
		return new NewCurrencyAction(messages.newCurrency());
	}

	public static EditProfileAction getEditProfileAction() {
		return new EditProfileAction(messages.editProfile());
	}

	public static RecurringsListAction getRecurringsListAction() {
		return new RecurringsListAction("Recurrings List action");
	}

	public static Prepare1099MISCAction getPrepare1099MISCAction() {
		return new Prepare1099MISCAction(messages.prepare1099MiscForms());
	}

	public static SalesByLocationDetailsAction getSalesByLocationDetailsAction(
			boolean isLocation) {
		String actionsting = messages.getSalesByLocationDetails(Global.get()
				.Location());
		if (!isLocation) {
			actionsting = messages.salesByClassDetails();
		}
		return new SalesByLocationDetailsAction(actionsting, isLocation);
	}

	public static SalesByLocationSummaryAction getSalesByLocationSummaryAction(
			boolean isLocation) {
		String actionsting = messages.salesByLocationSummary(Global.get()
				.Location());
		if (!isLocation) {
			actionsting = messages.salesByClassSummary();
		}
		return new SalesByLocationSummaryAction(actionsting, isLocation);
	}

	public static ProfitAndLossByLocationAction getProfitAndLossByLocationAction(
			boolean isLocation) {
		String actionstring = messages.profitAndLoss() + " By "
				+ Global.get().Location();
		if (!isLocation) {
			actionstring = messages.profitAndLossbyClass();
		}
		return new ProfitAndLossByLocationAction(actionstring, isLocation);
	}

	public static BudgetAction getBudgetActions() {
		return new BudgetAction(messages.budgetView());
	}

	public static NewBudgetAction getNewBudgetAction() {
		return new NewBudgetAction(messages.newBudget());
	}

	public static UsersActivityListAction getUsersActivityListAction() {
		return new UsersActivityListAction(messages.usersActivityLogTitle());
	}

	public static TDSVendorsListAction getTDSVendorsAction(boolean isTDSView) {
		return new TDSVendorsListAction(messages.tdsVendorsList(), isTDSView);
	}

	public static NewReconcileAccountAction getNewReconciliationAction() {
		return new NewReconcileAccountAction(messages.Reconciliation());
	}

	public static ReconciliationsListAction getReconciliationsListAction() {
		return new ReconciliationsListAction(messages.ReconciliationsList());
	}

	public static LocationGroupListAction getLocationGroupListAction() {
		return new LocationGroupListAction(messages.locationGroupList(Global
				.get().Location()));
	}

	public static AccounterClassListAction getAccounterClassGroupListAction() {
		return new AccounterClassListAction(messages.accounterClassList());
	}

	public static TDSPayAction getpayTDSAction() {
		return new TDSPayAction(messages.payTDS());
	}

	public static MISC1099TransactionDetailAction getMisc1099TransactionDetailAction() {
		return new MISC1099TransactionDetailAction(
				messages.MISC1099TransactionDetailByVendor(Global.get()
						.Vendor()));
	}

	public static StockSettingsAction getStockSettingsAction() {
		return new StockSettingsAction(messages.stockSettings());
	}

	public static StockAdjustmentAction getStockAdjustmentAction() {
		return new StockAdjustmentAction(messages.stockAdjustment());
	}

	public static StockAdjustmentsListAction getStockAdjustmentsListAction() {
		return new StockAdjustmentsListAction(messages.stockAdjustments());
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
		return new TaxHistoryAction(messages.taxHistory());
	}

	public static VatExceptionDetailReportAction getVATExceptionDetailsReportAction() {
		return new VatExceptionDetailReportAction(messages.vatExceptionDetail());
	}

	public static TaxItemDetailReportAction getTaxItemDetailReportAction() {
		return new TaxItemDetailReportAction(messages.taxItemDetailReport());
	}

	public static TAXItemExceptionDetailReport getTaxItemExceptionDetailReportAction() {
		return new TAXItemExceptionDetailReport(
				messages.taxItemExceptionDetailReport());
	}

	public static TranslationAction getTranslationAction() {
		return new TranslationAction(messages.translation());
	}

	public static MergeCustomerAction getCustomerMergeAction() {
		return new MergeCustomerAction(messages.mergeCustomers(Global.get()
				.Customer()));
	}

	public static MergeVendorAction getVendorMergeAction() {
		return new MergeVendorAction(messages.mergeVendors(Global.get()
				.Vendor()));
	}

	public static MergeAccountsAction getAccountMergeAction() {
		return new MergeAccountsAction(messages.mergeAccounts());
	}

	public static MergeItemsAction getItemMergeAction() {
		return new MergeItemsAction(messages.mergeItems());
	}

	public static BudgetOverviewReportAction getBudgetOverView() {
		return new BudgetOverviewReportAction(messages.budgetOverview());
	}

	public static AuditHistoryAction getAuditHistory(ClientActivity object) {
		return new AuditHistoryAction(messages.history(), object);

	}
}
