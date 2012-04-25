package com.vimukti.accounter.web.client.ui;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.imports.UploadCSVFileDialogAction;
import com.vimukti.accounter.web.client.ui.banking.AccountRegisterAction;
import com.vimukti.accounter.web.client.ui.banking.CreditCardChargeAction;
import com.vimukti.accounter.web.client.ui.banking.MakeDepositAction;
import com.vimukti.accounter.web.client.ui.banking.MergeCustomerAction;
import com.vimukti.accounter.web.client.ui.banking.ReconciliationsListAction;
import com.vimukti.accounter.web.client.ui.banking.WriteChecksAction;
import com.vimukti.accounter.web.client.ui.company.AccounterClassListAction;
import com.vimukti.accounter.web.client.ui.company.BudgetAction;
import com.vimukti.accounter.web.client.ui.company.ChalanListViewAction;
import com.vimukti.accounter.web.client.ui.company.ChartOfAccountsAction;
import com.vimukti.accounter.web.client.ui.company.CheckPrintSettingAction;
import com.vimukti.accounter.web.client.ui.company.CompanyHomeAction;
import com.vimukti.accounter.web.client.ui.company.CreateIRASInformationFileAction;
import com.vimukti.accounter.web.client.ui.company.CreditRatingListAction;
import com.vimukti.accounter.web.client.ui.company.CurrencyGroupListAction;
import com.vimukti.accounter.web.client.ui.company.CustomerCentreAction;
import com.vimukti.accounter.web.client.ui.company.CustomerGroupListAction;
import com.vimukti.accounter.web.client.ui.company.CustomersAction;
import com.vimukti.accounter.web.client.ui.company.DeleteCompanyAction;
import com.vimukti.accounter.web.client.ui.company.DepreciationAction;
import com.vimukti.accounter.web.client.ui.company.HelpItem;
import com.vimukti.accounter.web.client.ui.company.ItemGroupListAction;
import com.vimukti.accounter.web.client.ui.company.ItemsAction;
import com.vimukti.accounter.web.client.ui.company.JournalEntriesAction;
import com.vimukti.accounter.web.client.ui.company.LocationGroupListAction;
import com.vimukti.accounter.web.client.ui.company.ManageFiscalYearAction;
import com.vimukti.accounter.web.client.ui.company.ManageSalesTaxGroupsAction;
import com.vimukti.accounter.web.client.ui.company.MergeAccountsAction;
import com.vimukti.accounter.web.client.ui.company.MergeClassAction;
import com.vimukti.accounter.web.client.ui.company.MergeItemsAction;
import com.vimukti.accounter.web.client.ui.company.MergeLocationAction;
import com.vimukti.accounter.web.client.ui.company.MergeVendorAction;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.company.NewJournalEntryAction;
import com.vimukti.accounter.web.client.ui.company.NewTAXAgencyAction;
import com.vimukti.accounter.web.client.ui.company.PaymentTermListAction;
import com.vimukti.accounter.web.client.ui.company.PaymentsAction;
import com.vimukti.accounter.web.client.ui.company.PreferencesAction;
import com.vimukti.accounter.web.client.ui.company.PriceLevelListAction;
import com.vimukti.accounter.web.client.ui.company.ShippingMethodListAction;
import com.vimukti.accounter.web.client.ui.company.ShippingTermListAction;
import com.vimukti.accounter.web.client.ui.company.TDSResponsiblePersonAction;
import com.vimukti.accounter.web.client.ui.company.TdsDeductorMasterAction;
import com.vimukti.accounter.web.client.ui.company.UserDetailsAction;
import com.vimukti.accounter.web.client.ui.company.UsersActivityListAction;
import com.vimukti.accounter.web.client.ui.company.VendorCenterAction;
import com.vimukti.accounter.web.client.ui.company.VendorGroupListAction;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ManageSalesTaxItemsAction;
import com.vimukti.accounter.web.client.ui.core.TransactionsCenterAction;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.customers.CustomerPaymentsAction;
import com.vimukti.accounter.web.client.ui.customers.CustomerRefundAction;
import com.vimukti.accounter.web.client.ui.customers.CustomerRefundsAction;
import com.vimukti.accounter.web.client.ui.customers.CustomersHomeAction;
import com.vimukti.accounter.web.client.ui.customers.InvoicesAction;
import com.vimukti.accounter.web.client.ui.customers.NewCashSaleAction;
import com.vimukti.accounter.web.client.ui.customers.NewCreditsAndRefundsAction;
import com.vimukti.accounter.web.client.ui.customers.NewCustomerAction;
import com.vimukti.accounter.web.client.ui.customers.NewInvoiceAction;
import com.vimukti.accounter.web.client.ui.customers.NewJobAction;
import com.vimukti.accounter.web.client.ui.customers.NewQuoteAction;
import com.vimukti.accounter.web.client.ui.customers.QuotesAction;
import com.vimukti.accounter.web.client.ui.customers.ReceivePaymentAction;
import com.vimukti.accounter.web.client.ui.customers.ReceivedPaymentsAction;
import com.vimukti.accounter.web.client.ui.customers.RecurringsListAction;
import com.vimukti.accounter.web.client.ui.customers.SalesPersonAction;
import com.vimukti.accounter.web.client.ui.fixedassets.NewFixedAssetAction;
import com.vimukti.accounter.web.client.ui.fixedassets.PendingItemsListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.RegisteredItemsListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.SoldDisposedFixedAssetsListAction;
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
import com.vimukti.accounter.web.client.ui.reports.CustomerTransactionHistoryAction;
import com.vimukti.accounter.web.client.ui.reports.DepreciationSheduleAction;
import com.vimukti.accounter.web.client.ui.reports.ECSalesListAction;
import com.vimukti.accounter.web.client.ui.reports.EnterExchangeRatesAction;
import com.vimukti.accounter.web.client.ui.reports.EstimatesByJobAction;
import com.vimukti.accounter.web.client.ui.reports.ExpenseReportAction;
import com.vimukti.accounter.web.client.ui.reports.GLReportAction;
import com.vimukti.accounter.web.client.ui.reports.InventoryItemReportAction;
import com.vimukti.accounter.web.client.ui.reports.InventoryStockStatusByItemAction;
import com.vimukti.accounter.web.client.ui.reports.InventoryStockStatusByVendorAction;
import com.vimukti.accounter.web.client.ui.reports.InventoryValuationDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.InventoryValutionSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.JobProfitabilityDetailReportAction;
import com.vimukti.accounter.web.client.ui.reports.JobProfitabilitySummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.MissingChecksReportAction;
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
import com.vimukti.accounter.web.client.ui.reports.TransactionDetailByAccountAndCategoryAction;
import com.vimukti.accounter.web.client.ui.reports.TransactionDetailByTaxItemAction;
import com.vimukti.accounter.web.client.ui.reports.TrialBalanceAction;
import com.vimukti.accounter.web.client.ui.reports.UnRealisedExchangeLossesAndGainsAction;
import com.vimukti.accounter.web.client.ui.reports.UnbilledCostsByJobAction;
import com.vimukti.accounter.web.client.ui.reports.VAT100ReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATDetailsReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATItemSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATUncategorisedAmountsReportAction;
import com.vimukti.accounter.web.client.ui.reports.VatExceptionDetailReportAction;
import com.vimukti.accounter.web.client.ui.reports.VendorTransactionHistoryAction;
import com.vimukti.accounter.web.client.ui.search.SearchInputAction;
import com.vimukti.accounter.web.client.ui.settings.AddMeasurementAction;
import com.vimukti.accounter.web.client.ui.settings.GeneralSettingsAction;
import com.vimukti.accounter.web.client.ui.settings.InventoryCentreAction;
import com.vimukti.accounter.web.client.ui.settings.InventoryItemsAction;
import com.vimukti.accounter.web.client.ui.settings.InvoiceBrandingAction;
import com.vimukti.accounter.web.client.ui.settings.JobListAction;
import com.vimukti.accounter.web.client.ui.settings.MeasurementListAction;
import com.vimukti.accounter.web.client.ui.settings.StockAdjustmentAction;
import com.vimukti.accounter.web.client.ui.settings.StockAdjustmentsListAction;
import com.vimukti.accounter.web.client.ui.settings.StockSettingsAction;
import com.vimukti.accounter.web.client.ui.settings.UsersAction;
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
import com.vimukti.accounter.web.client.ui.vendors.BillsAction;
import com.vimukti.accounter.web.client.ui.vendors.CashExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.CreditCardExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.DepositAction;
import com.vimukti.accounter.web.client.ui.vendors.EnterBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.ExpenseClaimsAction;
import com.vimukti.accounter.web.client.ui.vendors.ExpensesAction;
import com.vimukti.accounter.web.client.ui.vendors.IssuePaymentsAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCashPurchaseAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCreditMemoAction;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorAction;
import com.vimukti.accounter.web.client.ui.vendors.PayBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.Prepare1099MISCAction;
import com.vimukti.accounter.web.client.ui.vendors.PurchaseOrderAction;
import com.vimukti.accounter.web.client.ui.vendors.PurchaseOrderListAction;
import com.vimukti.accounter.web.client.ui.vendors.RecordExpensesAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorsAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorsHomeAction;

/**
 * 
 * 
 */

public class MainFinanceWindow extends FlowPanel {

	private static ViewManager viewManager;
	private Header header;
	private int height;
	private int width;
	private HelpItem item;
	public static Map<String, Action> actions;
	private static final int CLASS = 1;
	private static final int LOCATION = 2;
	private static final int JOB = 3;

	protected AccounterMessages messages = Global.get().messages();

	public MainFinanceWindow() {
		initializeActionsWithTokens();
		createControls();
		sinkEvents(Event.ONMOUSEOVER);
	}

	private void createControls() {

		viewManager = (ViewManager) GWT.create(ViewManager.class);
		viewManager.createView(this);

		header = new Header(Accounter.getCompany());

		add(header);

		WebMenu menubar = GWT.create(WebMenu.class);
		menubar.initialize(isTouch());
		add(menubar.asWidget());

		add(viewManager);

		Label help = new Label(messages.helpLinks());
		help.addStyleName("down-panel");
		if (item == null) {
			item = new HelpItem();
		}
		addStyleName("financeWindow");

		new CompanyHomeAction().run(null, false);
		// // } else {
		// // // if company is not configured then show the setupwizard
		// // SetupWizard setupWizard = new SetupWizard();
		// // add(setupWizard);
		// // }
		// Window.addWindowClosingHandler(new ClosingHandler() {
		//
		// @Override
		// public void onWindowClosing(ClosingEvent event) {
		// Accounter.setShutdown(true);
		// }
		// });
	}

	private native boolean isTouch() /*-{
		return $wnd.isTouch;
	}-*/;

	public HelpItem getHelpItem() {
		return item;

	}

	private Command getDashBoardCommand() {
		Command dashBoardcmd = new Command() {

			@Override
			public void execute() {
				String historyToken = new CompanyHomeAction().getHistoryToken();
				if (!History.getToken().equals(historyToken)) {
					new ExpensesAction(null).run(null, true);
				}
				new CompanyHomeAction().run(null, false);
			}
		};
		return dashBoardcmd;
	}

	public static ViewManager getViewManager() {
		return viewManager;
	}

	public void fitToSize(int height, int width) {
		this.height = height;
		this.width = width - 20;
		viewManager.fitToSize(height, width - 10 - 15);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		this.getElement().getParentElement()
				.addClassName("main-finance-window");
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEOVER:

			break;

		default:
			break;
		}
		super.onBrowserEvent(event);
	}

	/**
	 * Give this MenuBar focus.
	 */
	public void focus() {
		FocusImpl.getFocusImplForPanel().focus(getElement());
	}

	@Override
	protected void onAttach() {
		this.getParent().addStyleName("noScroll");
		this.setStyleName("finance_window");
		super.onAttach();
	}

	@Override
	protected void onDetach() {
		Accounter.getComet().stopComet();
		this.getParent().removeStyleName("noScroll");
		super.onDetach();

	}

	@Override
	protected void onUnload() {
		clearMacMenu();
		super.onUnload();
		// TODO
		// AccounterCometClient.cometStop();
	}

	private native static void clearMacMenu() /*-{
		$wnd.ClearMacMenu();
	}-*/;

	public <T extends IAccounterCore> void historyChanged(String value) {
		if (actions == null || value == null)
			return;

		HistoryToken historyToken;
		try {
			historyToken = new HistoryToken(value);
		} catch (Exception e) {
			return;
		}

		final Action action = actions.get(historyToken.getToken());
		if (action != null) {

			if (historyToken.getType() != null && historyToken.getValue() != 0) {
				AccounterAsyncCallback<T> callback = new AccounterAsyncCallback<T>() {

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(messages.unableToshowtheview());
					}

					@Override
					public void onResultSuccess(T result) {
						if (result != null) {
							action.setInput(result);
							action.execute();
						}
					}

				};

				Accounter.createGETService().getObjectById(
						historyToken.getType(), historyToken.getValue(),
						callback);

			} else {
				action.run(null, false);
			}
		}
	}

	public static Map<String, Action> getActions() {
		return actions;
	}

	private void initializeActionsWithTokens() {
		actions = new HashMap<String, Action>();
		actions.put(new CompanyHomeAction().getHistoryToken(),
				new CompanyHomeAction());
		actions.put(new NewJournalEntryAction().getHistoryToken(),
				new NewJournalEntryAction());
		actions.put(new NewAccountAction().getHistoryToken(),
				new NewAccountAction());
		PreferencesAction preferencesAction = new PreferencesAction(
				PreferencesAction.COMPANY);
		actions.put(preferencesAction.getHistoryToken(), preferencesAction);
		preferencesAction = new PreferencesAction(PreferencesAction.SETTINGS);
		actions.put(preferencesAction.getHistoryToken(), preferencesAction);
		actions.put(new ManageSalesTaxGroupsAction().getHistoryToken(),
				new ManageSalesTaxGroupsAction());
		actions.put(new ManageSalesTaxItemsAction().getHistoryToken(),
				new ManageSalesTaxItemsAction());
		actions.put(new AdjustTAXAction(2).getHistoryToken(),
				new AdjustTAXAction(2));
		actions.put(new NewTAXAgencyAction().getHistoryToken(),
				new NewTAXAgencyAction());
		actions.put(new CustomerGroupListAction().getHistoryToken(),
				new CustomerGroupListAction());
		actions.put(new VendorGroupListAction().getHistoryToken(),
				new VendorGroupListAction());
		actions.put(new PaymentTermListAction().getHistoryToken(),
				new PaymentTermListAction());
		actions.put(new ShippingMethodListAction().getHistoryToken(),
				new ShippingMethodListAction());
		actions.put(new ShippingTermListAction().getHistoryToken(),
				new ShippingTermListAction());
		actions.put(new PriceLevelListAction().getHistoryToken(),
				new PriceLevelListAction());
		actions.put(new ItemGroupListAction().getHistoryToken(),
				new ItemGroupListAction());
		actions.put(new AccounterClassListAction().getHistoryToken(),
				new AccounterClassListAction());
		actions.put(new CreditRatingListAction().getHistoryToken(),
				new CreditRatingListAction());
		actions.put(new ManageFiscalYearAction().getHistoryToken(),
				new ManageFiscalYearAction());
		actions.put(new ChartOfAccountsAction().getHistoryToken(),
				new ChartOfAccountsAction());
		actions.put(new JournalEntriesAction().getHistoryToken(),
				new JournalEntriesAction());

		// actions.put(ActionFactory.getItemsAction().getHistoryToken(),
		// ActionFactory.getItemsAction());
		actions.put(new CustomersAction().getHistoryToken(),
				new CustomersAction());
		actions.put(new VendorsAction().getHistoryToken(), new VendorsAction());
		actions.put(
				new PaymentsAction(PaymentsAction.COMPANY).getHistoryToken(),
				new PaymentsAction(PaymentsAction.COMPANY));
		actions.put(new JobListAction().getHistoryToken(), new JobListAction());
		//
		// actions.put(ActionFactory.getImportBankStatementAction()
		// .getHistoryToken(), ActionFactory
		// .getImportBankStatementAction());

		actions.put(new SalesPersonAction().getHistoryToken(),
				new SalesPersonAction());

		// tax related items menus
		actions.put(new NewVatItemAction().getHistoryToken(),
				new NewVatItemAction());
		actions.put(new NewTAXCodeAction().getHistoryToken(),
				new NewTAXCodeAction());
		actions.put(new NewTAXAgencyAction().getHistoryToken(),
				new NewTAXAgencyAction());
		actions.put(new AdjustTAXAction(2).getHistoryToken(),
				new AdjustTAXAction(2));
		actions.put(new FileTAXAction().getHistoryToken(), new FileTAXAction());
		actions.put(new PayTAXAction().getHistoryToken(), new PayTAXAction());
		actions.put(new ReceiveVATAction().getHistoryToken(),
				new ReceiveVATAction());
		actions.put(new VatItemListAction().getHistoryToken(),
				new VatItemListAction());
		actions.put(new ManageTAXCodesListAction().getHistoryToken(),
				new ManageTAXCodesListAction());
		actions.put(new TaxHistoryAction().getHistoryToken(),
				new TaxHistoryAction());

		actions.put(new CustomersHomeAction().getHistoryToken(),
				new CustomersHomeAction());
		actions.put(new NewCustomerAction().getHistoryToken(),
				new NewCustomerAction());

		actions.put(
				new NewQuoteAction(ClientEstimate.QUOTES).getHistoryToken(),
				new NewQuoteAction(ClientEstimate.QUOTES));
		actions.put(
				new NewQuoteAction(ClientEstimate.CHARGES).getHistoryToken(),
				new NewQuoteAction(ClientEstimate.CHARGES));
		actions.put(
				new NewQuoteAction(ClientEstimate.CREDITS).getHistoryToken(),
				new NewQuoteAction(ClientEstimate.CREDITS));

		actions.put(new NewInvoiceAction().getHistoryToken(),
				new NewInvoiceAction());
		actions.put(new InvoiceBrandingAction().getHistoryToken(),
				new InvoiceBrandingAction());
		actions.put(new NewCashSaleAction().getHistoryToken(),
				new NewCashSaleAction());
		actions.put(new NewCreditsAndRefundsAction().getHistoryToken(),
				new NewCreditsAndRefundsAction());
		actions.put(new CustomerPaymentsAction().getHistoryToken(),
				new CustomerPaymentsAction());
		actions.put(new ReceivePaymentAction().getHistoryToken(),
				new ReceivePaymentAction());
		actions.put(new CustomerRefundAction().getHistoryToken(),
				new CustomerRefundAction());
		actions.put(new CustomersAction().getHistoryToken(),
				new CustomersAction());
		// actions.put(ActionFactory.getItemsAction().getHistoryToken(),
		// ActionFactory.getItemsAction());

		actions.put(new MissingChecksReportAction().getHistoryToken(),
				new MissingChecksReportAction());
		actions.put(
				new ReconciliationDiscrepancyReportAction().getHistoryToken(),
				new ReconciliationDiscrepancyReportAction());

		actions.put(new InventoryAssemblyAction().getHistoryToken(),
				new InventoryAssemblyAction());

		NewItemAction newItemAction = new NewItemAction(true);
		newItemAction.setType(ClientItem.TYPE_INVENTORY_PART);
		actions.put(newItemAction.getHistoryToken(), newItemAction);

		actions.put(new QuotesAction(ClientEstimate.QUOTES).getHistoryToken(),
				new QuotesAction(ClientEstimate.QUOTES));
		actions.put(new QuotesAction(ClientEstimate.CHARGES).getHistoryToken(),
				new QuotesAction(ClientEstimate.CHARGES));
		actions.put(new QuotesAction(ClientEstimate.CREDITS).getHistoryToken(),
				new QuotesAction(ClientEstimate.CREDITS));
		actions.put(new InvoicesAction(null).getHistoryToken(),
				new InvoicesAction(null));
		actions.put(new TransactionsCenterAction().getHistoryToken(),
				new TransactionsCenterAction());

		actions.put(new ReceivedPaymentsAction().getHistoryToken(),
				new ReceivedPaymentsAction());
		actions.put(new CustomerRefundsAction().getHistoryToken(),
				new CustomerRefundsAction());
		actions.put(new TransactionsCenterAction().getHistoryToken(),
				new TransactionsCenterAction());
		actions.put(new VendorsHomeAction().getHistoryToken(),
				new VendorsHomeAction());
		actions.put(new NewVendorAction().getHistoryToken(),
				new NewVendorAction());
		actions.put(new NewCashPurchaseAction().getHistoryToken(),
				new NewCashPurchaseAction());
		actions.put(new NewCreditMemoAction().getHistoryToken(),
				new NewCreditMemoAction());
		actions.put(new EnterBillsAction().getHistoryToken(),
				new EnterBillsAction());
		actions.put(new PayBillsAction().getHistoryToken(),
				new PayBillsAction());
		actions.put(new IssuePaymentsAction().getHistoryToken(),
				new IssuePaymentsAction());
		actions.put(new VendorPaymentsAction().getHistoryToken(),
				new VendorPaymentsAction());
		actions.put(new ExpensesAction("").getHistoryToken(),
				new ExpensesAction(""));
		actions.put(new ExpenseClaimsAction(0).getHistoryToken(),
				new ExpenseClaimsAction(0));

		actions.put(new VendorsAction().getHistoryToken(), new VendorsAction());
		// actions.put(ActionFactory.getItemsAction().getHistoryToken(),
		// ActionFactory.getItemsAction());
		actions.put(new BillsAction().getHistoryToken(), new BillsAction());
		actions.put(new VendorPaymentsAction().getHistoryToken(),
				new VendorPaymentsAction());

		actions.put(
				new NewAccountAction(ClientAccount.TYPE_BANK).getHistoryToken(),
				new NewAccountAction(ClientAccount.TYPE_BANK));
		actions.put(new WriteChecksAction().getHistoryToken(),
				new WriteChecksAction());
		// actions.put(ActionFactory.getBankStatementAction().getHistoryToken(),
		// ActionFactory.getBankStatementAction());
		actions.put(new MakeDepositAction().getHistoryToken(),
				new MakeDepositAction());
		actions.put(new PayBillsAction().getHistoryToken(),
				new PayBillsAction());
		actions.put(new CreditCardChargeAction().getHistoryToken(),
				new CreditCardChargeAction());
		actions.put(
				new PaymentsAction(PaymentsAction.BANKING).getHistoryToken(),
				new PaymentsAction(PaymentsAction.BANKING));

		actions.put(new NewQuoteAction(ClientEstimate.SALES_ORDER)
				.getHistoryToken(), new NewQuoteAction(
				ClientEstimate.SALES_ORDER));
		actions.put(
				new QuotesAction(ClientEstimate.SALES_ORDER).getHistoryToken(),
				new QuotesAction(ClientEstimate.SALES_ORDER));
		actions.put(new SalesOpenOrderAction().getHistoryToken(),
				new SalesOpenOrderAction());

		actions.put(new PurchaseOrderAction().getHistoryToken(),
				new PurchaseOrderAction());
		actions.put(new PurchaseOrderListAction().getHistoryToken(),
				new PurchaseOrderListAction());
		actions.put(new PurchaseOpenOrderAction().getHistoryToken(),
				new PurchaseOpenOrderAction());

		actions.put(new ProfitAndLossAction().getHistoryToken(),
				new ProfitAndLossAction());
		actions.put(new BalanceSheetAction().getHistoryToken(),
				new BalanceSheetAction());
		actions.put(new CashFlowStatementAction().getHistoryToken(),
				new CashFlowStatementAction());
		actions.put(new TrialBalanceAction().getHistoryToken(),
				new TrialBalanceAction());

		actions.put(new TransactionDetailByAccountAction().getHistoryToken(),
				new TransactionDetailByAccountAction());
		actions.put(new TransactionDetailByAccountAndCategoryAction()
				.getHistoryToken(),
				new TransactionDetailByAccountAndCategoryAction());
		actions.put(new GLReportAction().getHistoryToken(),
				new GLReportAction());
		actions.put(new ExpenseReportAction().getHistoryToken(),
				new ExpenseReportAction());

		actions.put(new SalesTaxLiabilityAction().getHistoryToken(),
				new SalesTaxLiabilityAction());
		actions.put(new TransactionDetailByTaxItemAction().getHistoryToken(),
				new TransactionDetailByTaxItemAction());
		actions.put(new ARAgingSummaryReportAction().getHistoryToken(),
				new ARAgingSummaryReportAction());
		actions.put(new ARAgingDetailAction().getHistoryToken(),
				new ARAgingDetailAction());
		actions.put(new StatementReportAction(0, false).getHistoryToken(),
				new StatementReportAction(0, false));
		actions.put(new CustomerTransactionHistoryAction().getHistoryToken(),
				new CustomerTransactionHistoryAction());
		actions.put(new SalesByCustomerSummaryAction().getHistoryToken(),
				new SalesByCustomerSummaryAction());
		actions.put(new EstimatesByJobAction().getHistoryToken(),
				new EstimatesByJobAction());
		actions.put(new UnbilledCostsByJobAction().getHistoryToken(),
				new UnbilledCostsByJobAction());
		actions.put(new SalesByCustomerDetailAction().getHistoryToken(),
				new SalesByCustomerDetailAction());
		actions.put(new SalesByItemSummaryAction().getHistoryToken(),
				new SalesByItemSummaryAction());
		actions.put(new SalesByItemDetailAction().getHistoryToken(),
				new SalesByItemDetailAction());
		actions.put(new SalesOpenOrderAction().getHistoryToken(),
				new SalesOpenOrderAction());

		actions.put(new APAgingSummaryReportAction().getHistoryToken(),
				new APAgingSummaryReportAction());
		actions.put(new ARAgingDetailAction().getHistoryToken(),
				new ARAgingDetailAction());
		actions.put(new VendorTransactionHistoryAction().getHistoryToken(),
				new VendorTransactionHistoryAction());

		actions.put(new PurchaseByVendorSummaryAction().getHistoryToken(),
				new PurchaseByVendorSummaryAction());
		actions.put(new PurchaseByVendorDetailsAction().getHistoryToken(),
				new PurchaseByVendorDetailsAction());
		actions.put(new PurchaseByItemSummaryAction().getHistoryToken(),
				new PurchaseByItemSummaryAction());
		actions.put(new PurchaseByItemDetailsAction().getHistoryToken(),
				new PurchaseByItemDetailsAction());
		actions.put(new PurchaseOpenOrderAction().getHistoryToken(),
				new PurchaseOpenOrderAction());

		actions.put(new StatementReportAction(0, true).getHistoryToken(),
				new StatementReportAction(0, true));

		actions.put(new VATSummaryReportAction().getHistoryToken(),
				new VATSummaryReportAction());
		actions.put(new VATDetailsReportAction().getHistoryToken(),
				new VATDetailsReportAction());
		actions.put(new VAT100ReportAction().getHistoryToken(),
				new VAT100ReportAction());

		actions.put(
				new VATUncategorisedAmountsReportAction().getHistoryToken(),
				new VATUncategorisedAmountsReportAction());

		actions.put(new VatExceptionDetailReportAction().getHistoryToken(),
				new VatExceptionDetailReportAction());

		actions.put(new TAXItemExceptionDetailReport().getHistoryToken(),
				new TAXItemExceptionDetailReport());

		actions.put(new TaxItemDetailReportAction().getHistoryToken(),
				new TaxItemDetailReportAction());

		actions.put(new VATItemSummaryReportAction().getHistoryToken(),
				new VATItemSummaryReportAction());
		actions.put(new ECSalesListAction().getHistoryToken(),
				new ECSalesListAction());

		actions.put(new GeneralSettingsAction().getHistoryToken(),
				new GeneralSettingsAction());
		actions.put(new NewJobAction(null).getHistoryToken(), new NewJobAction(
				null));
		actions.put("bankAccounts", new ChartOfAccountsAction(
				ClientAccount.TYPE_BANK));
		actions.put(new CashExpenseAction().getHistoryToken(),
				new CashExpenseAction());
		actions.put(new CreditCardExpenseAction().getHistoryToken(),
				new CreditCardExpenseAction());
		actions.put("employeeExpenses", new ExpensesAction(messages.employee()));
		actions.put(new AccountRegisterAction().getHistoryToken(),
				new AccountRegisterAction());
		actions.put("overDueInvoices", new InvoicesAction(messages.overDue()));
		actions.put(new UserDetailsAction().getHistoryToken(),
				new UserDetailsAction());
		actions.put(new Prepare1099MISCAction().getHistoryToken(),
				new Prepare1099MISCAction());

		actions.put(new ReconciliationsListAction().getHistoryToken(),
				new ReconciliationsListAction());

		actions.put(new ReportsHomeAction().getHistoryToken(),
				new ReportsHomeAction());

		ProfitAndLossByLocationAction profitAndLossByLocationActionLocation = new ProfitAndLossByLocationAction(
				LOCATION);

		ProfitAndLossByLocationAction profitAndLossByLocationActionClass = new ProfitAndLossByLocationAction(
				CLASS);

		ProfitAndLossByLocationAction profitAndLossByLocationActionJob = new ProfitAndLossByLocationAction(
				JOB);

		actions.put(profitAndLossByLocationActionLocation.getHistoryToken(),
				profitAndLossByLocationActionLocation);
		actions.put(profitAndLossByLocationActionJob.getHistoryToken(),
				profitAndLossByLocationActionJob);

		actions.put(profitAndLossByLocationActionClass.getHistoryToken(),
				profitAndLossByLocationActionClass);
		ReconcilationsAction reconcilationsAction = new ReconcilationsAction();
		actions.put(reconcilationsAction.getHistoryToken(),
				reconcilationsAction);
		// Location,class Summary Reports
		SalesByLocationSummaryAction salesByLocationSummaryActionTrue = new SalesByLocationSummaryAction(
				true, true);
		SalesByLocationSummaryAction salesByLocationSummaryActionFalse = new SalesByLocationSummaryAction(
				false, true);
		SalesByLocationSummaryAction purchaseByLocationSummaryActionTrue = new SalesByLocationSummaryAction(
				true, false);
		SalesByLocationSummaryAction purchaseByLocationSummaryActionFalse = new SalesByLocationSummaryAction(
				false, false);
		actions.put(purchaseByLocationSummaryActionTrue.getHistoryToken(),
				purchaseByLocationSummaryActionTrue);
		actions.put(purchaseByLocationSummaryActionFalse.getHistoryToken(),
				purchaseByLocationSummaryActionFalse);
		actions.put(salesByLocationSummaryActionTrue.getHistoryToken(),
				salesByLocationSummaryActionTrue);
		actions.put(salesByLocationSummaryActionFalse.getHistoryToken(),
				salesByLocationSummaryActionFalse);

		// CLASS TRACKING
		SalesByLocationDetailsAction salesByLocationDetailActionTrue = new SalesByLocationDetailsAction(
				true, true);
		SalesByLocationDetailsAction salesByLocationDetailActionFalse = new SalesByLocationDetailsAction(
				false, true);
		SalesByLocationDetailsAction purchaseByLocationDetailActiontrue = new SalesByLocationDetailsAction(
				true, false);
		SalesByLocationDetailsAction purchaseByLocationDetailActionFalse = new SalesByLocationDetailsAction(
				false, false);
		actions.put(salesByLocationDetailActionTrue.getHistoryToken(),
				salesByLocationDetailActionTrue);
		actions.put(purchaseByLocationDetailActiontrue.getHistoryToken(),
				purchaseByLocationDetailActiontrue);
		actions.put(purchaseByLocationDetailActionFalse.getHistoryToken(),
				purchaseByLocationDetailActionFalse);
		actions.put(salesByLocationDetailActionFalse.getHistoryToken(),
				salesByLocationDetailActionFalse);

		actions.put(new UsersActivityListAction().getHistoryToken(),
				new UsersActivityListAction());

		actions.put(new RecurringsListAction().getHistoryToken(),
				new RecurringsListAction());

		actions.put(new RemindersListAction().getHistoryToken(),
				new RemindersListAction());

		actions.put(new AutomaticTransactionsAction().getHistoryToken(),
				new AutomaticTransactionsAction());

		NewItemAction supplierItem = new NewItemAction(false);
		actions.put(supplierItem.getHistoryToken(), supplierItem);

		NewItemAction customerItem = new NewItemAction(true);
		actions.put(customerItem.getHistoryToken(), customerItem);

		actions.put(new LocationGroupListAction().getHistoryToken(),
				new LocationGroupListAction());
		actions.put(new AccounterClassListAction().getHistoryToken(),
				new AccounterClassListAction());

		ItemsAction customerItemAction = new ItemsAction(Global.get()
				.customer());
		actions.put(customerItemAction.getHistoryToken(), customerItemAction);

		ItemsAction vendorItemAction = new ItemsAction(Global.get().vendor());
		actions.put(vendorItemAction.getHistoryToken(), vendorItemAction);

		ItemsAction allItemAction = new ItemsAction(
				messages.bothCustomerAndVendor(Global.get().Customer(), Global
						.get().Vendor()));
		actions.put(allItemAction.getHistoryToken(), allItemAction);

		// adding actions for inventory

		actions.put(new WareHouseViewAction().getHistoryToken(),
				new WareHouseViewAction());
		actions.put(new WareHouseTransferAction().getHistoryToken(),
				new WareHouseTransferAction());
		actions.put(new AddMeasurementAction().getHistoryToken(),
				new AddMeasurementAction());

		actions.put(new InventoryItemsAction(ClientItem.TYPE_INVENTORY_PART)
				.getHistoryToken(), new InventoryItemsAction(
				ClientItem.TYPE_INVENTORY_PART));

		actions.put(
				new InventoryItemsAction(ClientItem.TYPE_INVENTORY_ASSEMBLY)
						.getHistoryToken(), new InventoryItemsAction(
						ClientItem.TYPE_INVENTORY_ASSEMBLY));

		actions.put(new WarehouseListAction().getHistoryToken(),
				new WarehouseListAction());
		actions.put(new WarehouseTransferListAction().getHistoryToken(),
				new WarehouseTransferListAction());
		actions.put(new MeasurementListAction().getHistoryToken(),
				new MeasurementListAction());
		actions.put(new StockAdjustmentAction().getHistoryToken(),
				new StockAdjustmentAction());

		actions.put(new StockSettingsAction().getHistoryToken(),
				new StockSettingsAction());

		actions.put(new StockAdjustmentsListAction().getHistoryToken(),
				new StockAdjustmentsListAction());

		actions.put(new CurrencyGroupListAction().getHistoryToken(),
				new CurrencyGroupListAction());

		actions.put(new TranslationAction().getHistoryToken(),
				new TranslationAction());

		actions.put(new TAXAgencyListAction().getHistoryToken(),
				new TAXAgencyListAction());

		// for inventory report actions
		actions.put(new InventoryItemReportAction().getHistoryToken(),
				new InventoryItemReportAction());
		actions.put(new InventoryValuationDetailsAction().getHistoryToken(),
				new InventoryValuationDetailsAction());
		actions.put(
				new InventoryValutionSummaryReportAction().getHistoryToken(),
				new InventoryValutionSummaryReportAction());
		actions.put(new InventoryStockStatusByItemAction().getHistoryToken(),
				new InventoryStockStatusByItemAction());
		actions.put(new InventoryStockStatusByVendorAction().getHistoryToken(),
				new InventoryStockStatusByVendorAction());
		// merge actions
		actions.put(new MergeCustomerAction().getHistoryToken(),
				new MergeCustomerAction());

		actions.put(new MergeVendorAction().getHistoryToken(),
				new MergeVendorAction());

		actions.put(new MergeAccountsAction().getHistoryToken(),
				new MergeAccountsAction());

		actions.put(new MergeItemsAction().getHistoryToken(),
				new MergeItemsAction());

		actions.put(new BudgetAction().getHistoryToken(), new BudgetAction());

		actions.put(new MergeClassAction().getHistoryToken(),
				new MergeClassAction());

		actions.put(new MergeLocationAction().getHistoryToken(),
				new MergeLocationAction());

		/**
		 * budget report actions
		 */
		actions.put(new BudgetOverviewReportAction().getHistoryToken(),
				new BudgetOverviewReportAction());
		actions.put(new BudgetvsActualsAction().getHistoryToken(),
				new BudgetvsActualsAction());

		actions.put(new RecordExpensesAction().getHistoryToken(),
				new RecordExpensesAction());

		actions.put(new NewFixedAssetAction().getHistoryToken(),
				new NewFixedAssetAction());

		actions.put(new DepreciationSheduleAction().getHistoryToken(),
				new DepreciationSheduleAction());

		actions.put(new DepreciationAction().getHistoryToken(),
				new DepreciationAction());
		actions.put(new RegisteredItemsListAction().getHistoryToken(),
				new RegisteredItemsListAction());
		actions.put(new PendingItemsListAction().getHistoryToken(),
				new PendingItemsListAction());
		actions.put(new SoldDisposedFixedAssetsListAction().getHistoryToken(),
				new SoldDisposedFixedAssetsListAction());

		actions.put(new SearchInputAction().getHistoryToken(),
				new SearchInputAction());

		actions.put(new CustomerCentreAction().getHistoryToken(),
				new CustomerCentreAction());

		actions.put(new CheckPrintSettingAction().getHistoryToken(),
				new CheckPrintSettingAction());

		actions.put(new DeleteCompanyAction().getHistoryToken(),
				new DeleteCompanyAction());

		// actions.put(ActionFactory.getDepositAction().getHistoryToken(),
		// ActionFactory.getDepositAction());

		actions.put(new TDSChalanDetailsAction().getHistoryToken(),
				new TDSChalanDetailsAction());

		actions.put(new ChalanListViewAction().getHistoryToken(),
				new ChalanListViewAction());

		actions.put(new TdsDeductorMasterAction().getHistoryToken(),
				new TdsDeductorMasterAction());

		actions.put(new TDSResponsiblePersonAction().getHistoryToken(),
				new TDSResponsiblePersonAction());

		actions.put(new VendorCenterAction().getHistoryToken(),
				new VendorCenterAction());

		actions.put(new UsersAction().getHistoryToken(), new UsersAction());

		actions.put(new InvoiceBrandingAction().getHistoryToken(),
				new InvoiceBrandingAction());

		actions.put(new ETdsFillingAction().getHistoryToken(),
				new ETdsFillingAction());

		actions.put(new TDSAcknowledgmentAction().getHistoryToken(),
				new TDSAcknowledgmentAction());

		actions.put(new TDSForm16AAction().getHistoryToken(),
				new TDSForm16AAction());

		actions.put(new TDSAcknowledgmentsReportAction().getHistoryToken(),
				new TDSAcknowledgmentsReportAction());

		actions.put(new TDSFiledDetailsAction().getHistoryToken(),
				new TDSFiledDetailsAction());

		actions.put(new CreateIRASInformationFileAction().getHistoryToken(),
				new CreateIRASInformationFileAction());
		actions.put(
				new RealisedExchangeLossesAndGainsAction().getHistoryToken(),
				new RealisedExchangeLossesAndGainsAction());
		actions.put(
				new UnRealisedExchangeLossesAndGainsAction().getHistoryToken(),
				new UnRealisedExchangeLossesAndGainsAction());
		actions.put(new EnterExchangeRatesAction().getHistoryToken(),
				new EnterExchangeRatesAction());

		actions.put(new DepositAction().getHistoryToken(), new DepositAction());

		actions.put(new UploadCSVFileDialogAction().getHistoryToken(),
				new UploadCSVFileDialogAction());

		actions.put(new BuildAssemblyAction().getHistoryToken(),
				new BuildAssemblyAction());

		actions.put(new InventoryCentreAction().getHistoryToken(),
				new InventoryCentreAction());

		// for job reports
		actions.put(
				new JobProfitabilitySummaryReportAction().getHistoryToken(),
				new JobProfitabilitySummaryReportAction());
		actions.put(new JobProfitabilityDetailReportAction().getHistoryToken(),
				new JobProfitabilityDetailReportAction());
		// for banking reports
		actions.put(new BankDepositDetailReportAction().getHistoryToken(),
				new BankDepositDetailReportAction());
		actions.put(new BankCheckDetailReportAction().getHistoryToken(),
				new BankCheckDetailReportAction());

	}

	public ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	public void onSessionExpired() {
		// Accounter.getComet().stopComet();
	}
}
