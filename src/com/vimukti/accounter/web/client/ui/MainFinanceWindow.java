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
import com.vimukti.accounter.web.client.ui.company.HelpItem;
import com.vimukti.accounter.web.client.ui.company.ItemsAction;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.company.PaymentsAction;
import com.vimukti.accounter.web.client.ui.company.PreferencesAction;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.payroll.AttendanceProductionTypeListAction;
import com.vimukti.accounter.web.client.ui.reports.PayHeadDetailReportAction;
import com.vimukti.accounter.web.client.ui.reports.PayHeadSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.PaySheetReportAction;
import com.vimukti.accounter.web.client.ui.reports.PaySlipDetailReportAction;
import com.vimukti.accounter.web.client.ui.reports.PaySlipSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.ProfitAndLossByLocationAction;
import com.vimukti.accounter.web.client.ui.reports.ReconcilationsAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByLocationDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByLocationSummaryAction;

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

		viewManager = new ViewManager(this);
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

		ActionFactory.getCompanyHomeAction().run(null, false);
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
				String historyToken = ActionFactory.getCompanyHomeAction()
						.getHistoryToken();
				if (!History.getToken().equals(historyToken)) {
					ActionFactory.getExpensesAction(null).run(null, true);
				}
				ActionFactory.getCompanyHomeAction().run(null, false);
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
		actions.put(ActionFactory.getCompanyHomeAction().getHistoryToken(),
				ActionFactory.getCompanyHomeAction());
		actions.put(ActionFactory.getNewJournalEntryAction().getHistoryToken(),
				ActionFactory.getNewJournalEntryAction());
		actions.put(ActionFactory.getNewAccountAction().getHistoryToken(),
				ActionFactory.getNewAccountAction());
		PreferencesAction preferencesAction = ActionFactory
				.getPreferencesAction(PreferencesAction.COMPANY);
		actions.put(preferencesAction.getHistoryToken(), preferencesAction);
		preferencesAction = ActionFactory
				.getPreferencesAction(PreferencesAction.SETTINGS);
		actions.put(preferencesAction.getHistoryToken(), preferencesAction);
		actions.put(ActionFactory.getManageSalesTaxGroupsAction()
				.getHistoryToken(), ActionFactory
				.getManageSalesTaxGroupsAction());
		actions.put(ActionFactory.getManageSalesTaxItemsAction()
				.getHistoryToken(), ActionFactory
				.getManageSalesTaxItemsAction());
		actions.put(ActionFactory.getPaySalesTaxAction().getHistoryToken(),
				ActionFactory.getPaySalesTaxAction());
		actions.put(ActionFactory.getAdjustTaxAction().getHistoryToken(),
				ActionFactory.getAdjustTaxAction());
		actions.put(ActionFactory.getNewTAXAgencyAction().getHistoryToken(),
				ActionFactory.getNewTAXAgencyAction());
		actions.put(ActionFactory.getCustomerGroupListAction()
				.getHistoryToken(), ActionFactory.getCustomerGroupListAction());
		actions.put(ActionFactory.getVendorGroupListAction().getHistoryToken(),
				ActionFactory.getVendorGroupListAction());
		actions.put(ActionFactory.getPaymentTermListAction().getHistoryToken(),
				ActionFactory.getPaymentTermListAction());
		actions.put(ActionFactory.getShippingMethodListAction()
				.getHistoryToken(), ActionFactory.getShippingMethodListAction());
		actions.put(
				ActionFactory.getShippingTermListAction().getHistoryToken(),
				ActionFactory.getShippingTermListAction());
		actions.put(ActionFactory.getPriceLevelListAction().getHistoryToken(),
				ActionFactory.getPriceLevelListAction());
		actions.put(ActionFactory.getItemGroupListAction().getHistoryToken(),
				ActionFactory.getItemGroupListAction());
		actions.put(ActionFactory.getAccounterClassGroupListAction()
				.getHistoryToken(), ActionFactory
				.getAccounterClassGroupListAction());
		actions.put(
				ActionFactory.getCreditRatingListAction().getHistoryToken(),
				ActionFactory.getCreditRatingListAction());
		actions.put(
				ActionFactory.getManageFiscalYearAction().getHistoryToken(),
				ActionFactory.getManageFiscalYearAction());
		actions.put(ActionFactory.getChartOfAccountsAction().getHistoryToken(),
				ActionFactory.getChartOfAccountsAction());
		actions.put(ActionFactory.getJournalEntriesAction().getHistoryToken(),
				ActionFactory.getJournalEntriesAction());

		// actions.put(ActionFactory.getItemsAction().getHistoryToken(),
		// ActionFactory.getItemsAction());
		actions.put(ActionFactory.getCustomersAction().getHistoryToken(),
				ActionFactory.getCustomersAction());
		actions.put(ActionFactory.getVendorsAction().getHistoryToken(),
				ActionFactory.getVendorsAction());
		actions.put(ActionFactory.getPaymentsAction(PaymentsAction.COMPANY)
				.getHistoryToken(), ActionFactory
				.getPaymentsAction(PaymentsAction.COMPANY));
		actions.put(ActionFactory.getJobListAction().getHistoryToken(),
				ActionFactory.getJobListAction());
		//
		// actions.put(ActionFactory.getImportBankStatementAction()
		// .getHistoryToken(), ActionFactory
		// .getImportBankStatementAction());

		actions.put(ActionFactory.getSalesPersonAction().getHistoryToken(),
				ActionFactory.getSalesPersonAction());

		// tax related items menus
		actions.put(ActionFactory.getNewVatItemAction().getHistoryToken(),
				ActionFactory.getNewVatItemAction());
		actions.put(ActionFactory.getNewTAXCodeAction().getHistoryToken(),
				ActionFactory.getNewTAXCodeAction());
		actions.put(ActionFactory.getNewTAXAgencyAction().getHistoryToken(),
				ActionFactory.getNewTAXAgencyAction());
		actions.put(ActionFactory.getAdjustTaxAction().getHistoryToken(),
				ActionFactory.getAdjustTaxAction());
		actions.put(ActionFactory.getFileTAXAction().getHistoryToken(),
				ActionFactory.getFileTAXAction());
		actions.put(ActionFactory.getpayTAXAction().getHistoryToken(),
				ActionFactory.getpayTAXAction());
		actions.put(ActionFactory.getreceiveVATAction().getHistoryToken(),
				ActionFactory.getreceiveVATAction());
		actions.put(ActionFactory.getVatItemListAction().getHistoryToken(),
				ActionFactory.getVatItemListAction());
		actions.put(ActionFactory.getTAXCodeListAction().getHistoryToken(),
				ActionFactory.getTAXCodeListAction());
		actions.put(ActionFactory.getTaxHistoryAction().getHistoryToken(),
				ActionFactory.getTaxHistoryAction());

		actions.put(ActionFactory.getCustomersHomeAction().getHistoryToken(),
				ActionFactory.getCustomersHomeAction());
		actions.put(ActionFactory.getNewCustomerAction().getHistoryToken(),
				ActionFactory.getNewCustomerAction());

		actions.put(ActionFactory.getNewQuoteAction(ClientEstimate.QUOTES)
				.getHistoryToken(), ActionFactory
				.getNewQuoteAction(ClientEstimate.QUOTES));
		actions.put(ActionFactory.getNewQuoteAction(ClientEstimate.CHARGES)
				.getHistoryToken(), ActionFactory
				.getNewQuoteAction(ClientEstimate.CHARGES));
		actions.put(ActionFactory.getNewQuoteAction(ClientEstimate.CREDITS)
				.getHistoryToken(), ActionFactory
				.getNewQuoteAction(ClientEstimate.CREDITS));

		actions.put(ActionFactory.getNewInvoiceAction().getHistoryToken(),
				ActionFactory.getNewInvoiceAction());
		actions.put(ActionFactory.getInvoiceBrandingAction().getHistoryToken(),
				ActionFactory.getInvoiceBrandingAction());
		actions.put(ActionFactory.getNewCashSaleAction().getHistoryToken(),
				ActionFactory.getNewCashSaleAction());
		actions.put(ActionFactory.getNewCreditsAndRefundsAction()
				.getHistoryToken(), ActionFactory
				.getNewCreditsAndRefundsAction());
		actions.put(ActionFactory.getNewCustomerPaymentAction()
				.getHistoryToken(), ActionFactory.getNewCustomerPaymentAction());
		actions.put(ActionFactory.getReceivePaymentAction().getHistoryToken(),
				ActionFactory.getReceivePaymentAction());
		actions.put(ActionFactory.getCustomerRefundAction().getHistoryToken(),
				ActionFactory.getCustomerRefundAction());
		actions.put(ActionFactory.getCustomersAction().getHistoryToken(),
				ActionFactory.getCustomersAction());
		// actions.put(ActionFactory.getItemsAction().getHistoryToken(),
		// ActionFactory.getItemsAction());

		actions.put(ActionFactory.getMissingChecksReportAction()
				.getHistoryToken(), ActionFactory
				.getMissingChecksReportAction());
		actions.put(ActionFactory.getReconciliationDiscrepancyReportAction()
				.getHistoryToken(), ActionFactory
				.getReconciliationDiscrepancyReportAction());

		actions.put(ActionFactory.getInventoryAssemblyAction()
				.getHistoryToken(), ActionFactory.getInventoryAssemblyAction());
		actions.put(
				ActionFactory.getNewInventoryItemAction().getHistoryToken(),
				ActionFactory.getNewInventoryItemAction());

		actions.put(ActionFactory.getQuotesAction(ClientEstimate.QUOTES)
				.getHistoryToken(), ActionFactory
				.getQuotesAction(ClientEstimate.QUOTES));
		actions.put(ActionFactory.getQuotesAction(ClientEstimate.CHARGES)
				.getHistoryToken(), ActionFactory
				.getQuotesAction(ClientEstimate.CHARGES));
		actions.put(ActionFactory.getQuotesAction(ClientEstimate.CREDITS)
				.getHistoryToken(), ActionFactory
				.getQuotesAction(ClientEstimate.CREDITS));
		actions.put(ActionFactory.getInvoicesAction(null).getHistoryToken(),
				ActionFactory.getInvoicesAction(null));
		actions.put(ActionFactory.getTransactionCenterAction()
				.getHistoryToken(), ActionFactory.getTransactionCenterAction());

		actions.put(
				ActionFactory.getReceivedPaymentsAction().getHistoryToken(),
				ActionFactory.getReceivedPaymentsAction());
		actions.put(ActionFactory.getCustomerRefundsAction().getHistoryToken(),
				ActionFactory.getCustomerRefundsAction());
		actions.put(ActionFactory.getTransactionCenterAction()
				.getHistoryToken(), ActionFactory.getTransactionCenterAction());
		actions.put(ActionFactory.getVendorsHomeAction().getHistoryToken(),
				ActionFactory.getVendorsHomeAction());
		actions.put(ActionFactory.getNewVendorAction().getHistoryToken(),
				ActionFactory.getNewVendorAction());
		actions.put(ActionFactory.getNewCashPurchaseAction().getHistoryToken(),
				ActionFactory.getNewCashPurchaseAction());
		actions.put(ActionFactory.getNewCreditMemoAction().getHistoryToken(),
				ActionFactory.getNewCreditMemoAction());
		actions.put(ActionFactory.getNewCheckAction().getHistoryToken(),
				ActionFactory.getNewCheckAction());
		actions.put(ActionFactory.getEnterBillsAction().getHistoryToken(),
				ActionFactory.getEnterBillsAction());
		actions.put(ActionFactory.getPayBillsAction().getHistoryToken(),
				ActionFactory.getPayBillsAction());
		actions.put(ActionFactory.getIssuePaymentsAction().getHistoryToken(),
				ActionFactory.getIssuePaymentsAction());
		actions.put(
				ActionFactory.getNewVendorPaymentAction().getHistoryToken(),
				ActionFactory.getNewVendorPaymentAction());
		actions.put(ActionFactory.getExpensesAction("").getHistoryToken(),
				ActionFactory.getExpensesAction(""));
		actions.put(ActionFactory.getExpenseClaimsAction(0).getHistoryToken(),
				ActionFactory.getExpenseClaimsAction(0));

		actions.put(ActionFactory.getVendorsAction().getHistoryToken(),
				ActionFactory.getVendorsAction());
		// actions.put(ActionFactory.getItemsAction().getHistoryToken(),
		// ActionFactory.getItemsAction());
		actions.put(ActionFactory.getBillsAction().getHistoryToken(),
				ActionFactory.getBillsAction());
		actions.put(ActionFactory.getVendorPaymentsAction().getHistoryToken(),
				ActionFactory.getVendorPaymentsAction());

		actions.put(ActionFactory.getNewBankAccountAction().getHistoryToken(),
				ActionFactory.getNewBankAccountAction());
		actions.put(ActionFactory.getWriteChecksAction().getHistoryToken(),
				ActionFactory.getWriteChecksAction());
		// actions.put(ActionFactory.getBankStatementAction().getHistoryToken(),
		// ActionFactory.getBankStatementAction());
		actions.put(ActionFactory.getMakeDepositAction().getHistoryToken(),
				ActionFactory.getMakeDepositAction());
		actions.put(ActionFactory.getPayBillsAction().getHistoryToken(),
				ActionFactory.getPayBillsAction());
		actions.put(
				ActionFactory.getCreditCardChargeAction().getHistoryToken(),
				ActionFactory.getCreditCardChargeAction());
		actions.put(ActionFactory.getPaymentsAction(PaymentsAction.BANKING)
				.getHistoryToken(), ActionFactory
				.getPaymentsAction(PaymentsAction.BANKING));

		actions.put(ActionFactory.getNewQuoteAction(ClientEstimate.SALES_ORDER)
				.getHistoryToken(), ActionFactory
				.getNewQuoteAction(ClientEstimate.SALES_ORDER));
		actions.put(ActionFactory.getQuotesAction(ClientEstimate.SALES_ORDER)
				.getHistoryToken(), ActionFactory
				.getQuotesAction(ClientEstimate.SALES_ORDER));
		actions.put(ActionFactory.getSalesOpenOrderAction().getHistoryToken(),
				ActionFactory.getSalesOpenOrderAction());

		actions.put(ActionFactory.getPurchaseOrderAction().getHistoryToken(),
				ActionFactory.getPurchaseOrderAction());
		actions.put(ActionFactory.getPurchaseOrderListAction()
				.getHistoryToken(), ActionFactory.getPurchaseOrderListAction());
		actions.put(ActionFactory.getPurchaseOpenOrderListAction()
				.getHistoryToken(), ActionFactory
				.getPurchaseOpenOrderListAction());

		actions.put(ActionFactory.getProfitAndLossAction().getHistoryToken(),
				ActionFactory.getProfitAndLossAction());
		actions.put(ActionFactory.getBalanceSheetAction().getHistoryToken(),
				ActionFactory.getBalanceSheetAction());
		actions.put(ActionFactory.getCashFlowStatementAction()
				.getHistoryToken(), ActionFactory.getCashFlowStatementAction());
		actions.put(ActionFactory.getTrialBalanceAction().getHistoryToken(),
				ActionFactory.getTrialBalanceAction());

		actions.put(ActionFactory.getTransactionDetailByAccountAction()
				.getHistoryToken(), ActionFactory
				.getTransactionDetailByAccountAction());
		actions.put(ActionFactory
				.getTransactionDetailByAccountAndCategoryAction()
				.getHistoryToken(), ActionFactory
				.getTransactionDetailByAccountAndCategoryAction());
		actions.put(ActionFactory.getGlReportAction().getHistoryToken(),
				ActionFactory.getGlReportAction());
		actions.put(ActionFactory.getExpenseReportAction().getHistoryToken(),
				ActionFactory.getExpenseReportAction());

		actions.put(ActionFactory.getSalesTaxLiabilityAction()
				.getHistoryToken(), ActionFactory.getSalesTaxLiabilityAction());
		actions.put(ActionFactory.getTransactionDetailByTaxItemAction()
				.getHistoryToken(), ActionFactory
				.getTransactionDetailByTaxItemAction());
		actions.put(ActionFactory.getArAgingSummaryReportAction()
				.getHistoryToken(), ActionFactory
				.getArAgingSummaryReportAction());
		actions.put(ActionFactory.getArAgingDetailAction().getHistoryToken(),
				ActionFactory.getArAgingDetailAction());
		actions.put(ActionFactory.getStatementReport(false, 0)
				.getHistoryToken(), ActionFactory.getStatementReport(false, 0));
		actions.put(ActionFactory.getCustomerTransactionHistoryAction()
				.getHistoryToken(), ActionFactory
				.getCustomerTransactionHistoryAction());
		actions.put(ActionFactory.getSalesByCustomerSummaryAction()
				.getHistoryToken(), ActionFactory
				.getSalesByCustomerSummaryAction());
		actions.put(ActionFactory.getEstimatesByJobAction().getHistoryToken(),
				ActionFactory.getEstimatesByJobAction());
		actions.put(ActionFactory.getUnbilledCostsByJobAction()
				.getHistoryToken(), ActionFactory.getUnbilledCostsByJobAction());
		actions.put(ActionFactory.getSalesByCustomerDetailAction()
				.getHistoryToken(), ActionFactory
				.getSalesByCustomerDetailAction());
		actions.put(ActionFactory.getSalesByItemSummaryAction()
				.getHistoryToken(), ActionFactory.getSalesByItemSummaryAction());
		actions.put(ActionFactory.getSalesByItemDetailAction()
				.getHistoryToken(), ActionFactory.getSalesByItemDetailAction());
		actions.put(ActionFactory.getSalesOpenOrderAction().getHistoryToken(),
				ActionFactory.getSalesOpenOrderAction());

		actions.put(ActionFactory.getAorpAgingSummaryReportAction()
				.getHistoryToken(), ActionFactory
				.getAorpAgingSummaryReportAction());
		actions.put(ActionFactory.getAorpAgingDetailAction().getHistoryToken(),
				ActionFactory.getAorpAgingDetailAction());
		actions.put(ActionFactory.getVendorTransactionHistoryAction()
				.getHistoryToken(), ActionFactory
				.getVendorTransactionHistoryAction());

		actions.put(ActionFactory.getPurchaseByVendorSummaryAction()
				.getHistoryToken(), ActionFactory
				.getPurchaseByVendorSummaryAction());
		actions.put(ActionFactory.getPurchaseByVendorDetailAction()
				.getHistoryToken(), ActionFactory
				.getPurchaseByVendorDetailAction());
		actions.put(ActionFactory.getPurchaseByItemSummaryAction()
				.getHistoryToken(), ActionFactory
				.getPurchaseByItemSummaryAction());
		actions.put(ActionFactory.getPurchaseByItemAction().getHistoryToken(),
				ActionFactory.getPurchaseByItemAction());
		actions.put(ActionFactory.getPurchaseOpenOrderAction()
				.getHistoryToken(), ActionFactory.getPurchaseOpenOrderAction());

		actions.put(
				ActionFactory.getStatementReport(true, 0).getHistoryToken(),
				ActionFactory.getStatementReport(true, 0));

		actions.put(
				ActionFactory.getVATSummaryReportAction().getHistoryToken(),
				ActionFactory.getVATSummaryReportAction());
		actions.put(
				ActionFactory.getVATDetailsReportAction().getHistoryToken(),
				ActionFactory.getVATDetailsReportAction());
		actions.put(ActionFactory.getVAT100ReportAction().getHistoryToken(),
				ActionFactory.getVAT100ReportAction());

		actions.put(ActionFactory.getVATUncategorisedAmountsReportAction()
				.getHistoryToken(), ActionFactory
				.getVATUncategorisedAmountsReportAction());

		actions.put(ActionFactory.getVATExceptionDetailsReportAction()
				.getHistoryToken(), ActionFactory
				.getVATExceptionDetailsReportAction());

		actions.put(ActionFactory.getTaxItemExceptionDetailReportAction()
				.getHistoryToken(), ActionFactory
				.getTaxItemExceptionDetailReportAction());

		actions.put(ActionFactory.getTaxItemDetailReportAction()
				.getHistoryToken(), ActionFactory
				.getTaxItemDetailReportAction());

		actions.put(ActionFactory.getVATItemSummaryReportAction()
				.getHistoryToken(), ActionFactory
				.getVATItemSummaryReportAction());
		actions.put(ActionFactory.getECSalesListAction().getHistoryToken(),
				ActionFactory.getECSalesListAction());

		actions.put(ActionFactory.getGeneralSettingsAction().getHistoryToken(),
				ActionFactory.getGeneralSettingsAction());
		actions.put(ActionFactory.getNewJobAction(null).getHistoryToken(),
				ActionFactory.getNewJobAction(null));
		actions.put("bankAccounts",
				ActionFactory.getChartOfAccountsAction(ClientAccount.TYPE_BANK));
		actions.put(ActionFactory.CashExpenseAction().getHistoryToken(),
				ActionFactory.CashExpenseAction());
		actions.put(ActionFactory.CreditCardExpenseAction().getHistoryToken(),
				ActionFactory.CreditCardExpenseAction());
		actions.put("employeeExpenses",
				ActionFactory.getExpensesAction(messages.employee()));
		actions.put(ActionFactory.getAccountRegisterAction().getHistoryToken(),
				ActionFactory.getAccountRegisterAction());
		actions.put("overDueInvoices",
				ActionFactory.getInvoicesAction(messages.overDue()));
		actions.put(ActionFactory.getUserDetailsAction().getHistoryToken(),
				ActionFactory.getUserDetailsAction());
		actions.put(ActionFactory.getPrepare1099MISCAction().getHistoryToken(),
				ActionFactory.getPrepare1099MISCAction());

		actions.put(ActionFactory.getReconciliationsListAction()
				.getHistoryToken(), ActionFactory
				.getReconciliationsListAction());

		actions.put(ActionFactory.getReportsHomeAction().getHistoryToken(),
				ActionFactory.getReportsHomeAction());

		ProfitAndLossByLocationAction profitAndLossByLocationActionLocation = ActionFactory
				.getProfitAndLossByLocationAction(LOCATION);

		ProfitAndLossByLocationAction profitAndLossByLocationActionClass = ActionFactory
				.getProfitAndLossByLocationAction(CLASS);

		ProfitAndLossByLocationAction profitAndLossByLocationActionJob = ActionFactory
				.getProfitAndLossByLocationAction(JOB);

		actions.put(profitAndLossByLocationActionLocation.getHistoryToken(),
				profitAndLossByLocationActionLocation);
		actions.put(profitAndLossByLocationActionJob.getHistoryToken(),
				profitAndLossByLocationActionJob);

		actions.put(profitAndLossByLocationActionClass.getHistoryToken(),
				profitAndLossByLocationActionClass);
		ReconcilationsAction reconcilationsAction = ActionFactory
				.getReconcilationsAction();
		actions.put(reconcilationsAction.getHistoryToken(),
				reconcilationsAction);
		// Location,class Summary Reports
		SalesByLocationSummaryAction salesByLocationSummaryActionTrue = ActionFactory
				.getSalesByLocationSummaryAction(true, true);
		SalesByLocationSummaryAction salesByLocationSummaryActionFalse = ActionFactory
				.getSalesByLocationSummaryAction(false, true);
		SalesByLocationSummaryAction purchaseByLocationSummaryActionTrue = ActionFactory
				.getSalesByLocationSummaryAction(true, false);
		SalesByLocationSummaryAction purchaseByLocationSummaryActionFalse = ActionFactory
				.getSalesByLocationSummaryAction(false, false);
		actions.put(purchaseByLocationSummaryActionTrue.getHistoryToken(),
				purchaseByLocationSummaryActionTrue);
		actions.put(purchaseByLocationSummaryActionFalse.getHistoryToken(),
				purchaseByLocationSummaryActionFalse);
		actions.put(salesByLocationSummaryActionTrue.getHistoryToken(),
				salesByLocationSummaryActionTrue);
		actions.put(salesByLocationSummaryActionFalse.getHistoryToken(),
				salesByLocationSummaryActionFalse);

		// CLASS TRACKING
		SalesByLocationDetailsAction salesByLocationDetailActionTrue = ActionFactory
				.getSalesByLocationDetailsAction(true, true);
		SalesByLocationDetailsAction salesByLocationDetailActionFalse = ActionFactory
				.getSalesByLocationDetailsAction(false, true);
		SalesByLocationDetailsAction purchaseByLocationDetailActiontrue = ActionFactory
				.getSalesByLocationDetailsAction(true, false);
		SalesByLocationDetailsAction purchaseByLocationDetailActionFalse = ActionFactory
				.getSalesByLocationDetailsAction(false, false);
		actions.put(salesByLocationDetailActionTrue.getHistoryToken(),
				salesByLocationDetailActionTrue);
		actions.put(purchaseByLocationDetailActiontrue.getHistoryToken(),
				purchaseByLocationDetailActiontrue);
		actions.put(purchaseByLocationDetailActionFalse.getHistoryToken(),
				purchaseByLocationDetailActionFalse);
		actions.put(salesByLocationDetailActionFalse.getHistoryToken(),
				salesByLocationDetailActionFalse);

		actions.put(ActionFactory.getUsersActivityListAction()
				.getHistoryToken(), ActionFactory.getUsersActivityListAction());

		actions.put(ActionFactory.getRecurringsListAction().getHistoryToken(),
				ActionFactory.getRecurringsListAction());

		actions.put(ActionFactory.getRemindersListAction().getHistoryToken(),
				ActionFactory.getRemindersListAction());

		actions.put(ActionFactory.getAutomaticTransactionsAction()
				.getHistoryToken(), ActionFactory
				.getAutomaticTransactionsAction());

		NewItemAction supplierItem = ActionFactory.getNewItemAction(false);
		actions.put(supplierItem.getHistoryToken(), supplierItem);

		NewItemAction customerItem = ActionFactory.getNewItemAction(true);
		actions.put(customerItem.getHistoryToken(), customerItem);

		actions.put(ActionFactory.getLocationGroupListAction()
				.getHistoryToken(), ActionFactory.getLocationGroupListAction());
		actions.put(ActionFactory.getAccounterClassGroupListAction()
				.getHistoryToken(), ActionFactory
				.getAccounterClassGroupListAction());

		ItemsAction customerItemAction = ActionFactory.getItemsAction(true,
				false);
		actions.put(customerItemAction.getHistoryToken(), customerItemAction);

		ItemsAction vendorItemAction = ActionFactory
				.getItemsAction(false, true);
		actions.put(vendorItemAction.getHistoryToken(), vendorItemAction);

		ItemsAction allItemAction = ActionFactory.getItemsAction(false, false);
		actions.put(allItemAction.getHistoryToken(), allItemAction);

		// adding actions for inventory

		actions.put(ActionFactory.getWareHouseViewAction().getHistoryToken(),
				ActionFactory.getWareHouseViewAction());
		actions.put(ActionFactory.getWareHouseTransferAction()
				.getHistoryToken(), ActionFactory.getWareHouseTransferAction());
		actions.put(ActionFactory.getAddMeasurementAction().getHistoryToken(),
				ActionFactory.getAddMeasurementAction());

		actions.put(
				ActionFactory.getInventoryItemsAction(
						ClientItem.TYPE_INVENTORY_PART).getHistoryToken(),
				ActionFactory
						.getInventoryItemsAction(ClientItem.TYPE_INVENTORY_PART));

		actions.put(
				ActionFactory.getInventoryItemsAction(
						ClientItem.TYPE_INVENTORY_ASSEMBLY).getHistoryToken(),
				ActionFactory
						.getInventoryItemsAction(ClientItem.TYPE_INVENTORY_ASSEMBLY));

		actions.put(ActionFactory.getWarehouseListAction().getHistoryToken(),
				ActionFactory.getWarehouseListAction());
		actions.put(ActionFactory.getWarehouseTransferListAction()
				.getHistoryToken(), ActionFactory
				.getWarehouseTransferListAction());
		actions.put(ActionFactory.getMeasurementsAction().getHistoryToken(),
				ActionFactory.getMeasurementsAction());
		actions.put(ActionFactory.getStockAdjustmentAction().getHistoryToken(),
				ActionFactory.getStockAdjustmentAction());

		actions.put(ActionFactory.getStockSettingsAction().getHistoryToken(),
				ActionFactory.getStockSettingsAction());

		actions.put(ActionFactory.getStockAdjustmentsListAction()
				.getHistoryToken(), ActionFactory
				.getStockAdjustmentsListAction());

		actions.put(ActionFactory.getCurrencyGroupListAction()
				.getHistoryToken(), ActionFactory.getCurrencyGroupListAction());

		actions.put(ActionFactory.getTranslationAction().getHistoryToken(),
				ActionFactory.getTranslationAction());

		actions.put(ActionFactory.getTAXAgencyListAction().getHistoryToken(),
				ActionFactory.getTAXAgencyListAction());

		// for inventory report actions
		actions.put(ActionFactory.getInventoryItemReportAction()
				.getHistoryToken(), ActionFactory
				.getInventoryItemReportAction());
		actions.put(ActionFactory.getInventoryValuationDetailsAction()
				.getHistoryToken(), ActionFactory
				.getInventoryValuationDetailsAction());
		actions.put(ActionFactory.getInventoryValutionSummaryReportAction()
				.getHistoryToken(), ActionFactory
				.getInventoryValutionSummaryReportAction());
		actions.put(ActionFactory.getInventoryStockStatusByItemAction()
				.getHistoryToken(), ActionFactory
				.getInventoryStockStatusByItemAction());
		actions.put(ActionFactory.getInventoryStockStatusByVendorAction()
				.getHistoryToken(), ActionFactory
				.getInventoryStockStatusByVendorAction());
		// merge actions
		actions.put(ActionFactory.getCustomerMergeAction().getHistoryToken(),
				ActionFactory.getCustomerMergeAction());

		actions.put(ActionFactory.getVendorMergeAction().getHistoryToken(),
				ActionFactory.getVendorMergeAction());

		actions.put(ActionFactory.getAccountMergeAction().getHistoryToken(),
				ActionFactory.getAccountMergeAction());

		actions.put(ActionFactory.getItemMergeAction().getHistoryToken(),
				ActionFactory.getItemMergeAction());

		actions.put(ActionFactory.getBudgetActions().getHistoryToken(),
				ActionFactory.getBudgetActions());

		actions.put(ActionFactory.getClassMergeAction().getHistoryToken(),
				ActionFactory.getClassMergeAction());

		actions.put(ActionFactory.getLocationMergeAction().getHistoryToken(),
				ActionFactory.getLocationMergeAction());

		/**
		 * budget report actions
		 */
		actions.put(ActionFactory.getBudgetOverView().getHistoryToken(),
				ActionFactory.getBudgetOverView());
		actions.put(ActionFactory.getBudgetVsActionReport().getHistoryToken(),
				ActionFactory.getBudgetVsActionReport());

		actions.put(ActionFactory.getRecordExpensesAction().getHistoryToken(),
				ActionFactory.getRecordExpensesAction());

		actions.put(ActionFactory.getNewFixedAssetAction().getHistoryToken(),
				ActionFactory.getNewFixedAssetAction());

		actions.put(ActionFactory.getDepreciationSheduleAction()
				.getHistoryToken(), ActionFactory
				.getDepreciationSheduleAction());

		actions.put(ActionFactory.getDepriciationAction().getHistoryToken(),
				ActionFactory.getDepriciationAction());
		actions.put(ActionFactory.getRegisteredItemsListAction()
				.getHistoryToken(), ActionFactory
				.getRegisteredItemsListAction());
		actions.put(
				ActionFactory.getPendingItemsListAction().getHistoryToken(),
				ActionFactory.getPendingItemsListAction());
		actions.put(
				ActionFactory.getSoldDisposedListAction().getHistoryToken(),
				ActionFactory.getSoldDisposedListAction());

		actions.put(ActionFactory.getSearchInputAction().getHistoryToken(),
				ActionFactory.getSearchInputAction());

		actions.put(ActionFactory.getCustomerCentre().getHistoryToken(),
				ActionFactory.getCustomerCentre());

		actions.put(ActionFactory.getCheckPrintSettingAction()
				.getHistoryToken(), ActionFactory.getCheckPrintSettingAction());

		actions.put(ActionFactory.getDeleteCompanyAction().getHistoryToken(),
				ActionFactory.getDeleteCompanyAction());

		// actions.put(ActionFactory.getDepositAction().getHistoryToken(),
		// ActionFactory.getDepositAction());

		actions.put(ActionFactory.getTDSChalanDetailsView().getHistoryToken(),
				ActionFactory.getTDSChalanDetailsView());

		actions.put(ActionFactory.getTDSChalanListView().getHistoryToken(),
				ActionFactory.getTDSChalanListView());

		actions.put(ActionFactory.getTdsDeductorMasters().getHistoryToken(),
				ActionFactory.getTdsDeductorMasters());

		actions.put(ActionFactory.getTDSResponsiblePerson().getHistoryToken(),
				ActionFactory.getTDSResponsiblePerson());

		actions.put(ActionFactory.getVendorCentreAction().getHistoryToken(),
				ActionFactory.getVendorCentreAction());

		actions.put(ActionFactory.getUsersAction().getHistoryToken(),
				ActionFactory.getUsersAction());

		actions.put(ActionFactory.getInvoiceBrandingAction().getHistoryToken(),
				ActionFactory.getInvoiceBrandingAction());

		actions.put(ActionFactory.getETdsFillingView().getHistoryToken(),
				ActionFactory.getETdsFillingView());

		actions.put(ActionFactory.getTDSAcknowledgmentAction()
				.getHistoryToken(), ActionFactory.getTDSAcknowledgmentAction());

		actions.put(ActionFactory.getTDSForm16AAction().getHistoryToken(),
				ActionFactory.getTDSForm16AAction());

		actions.put(ActionFactory.getTDSAcknowledgmentsReportAction()
				.getHistoryToken(), ActionFactory
				.getTDSAcknowledgmentsReportAction());

		actions.put(ActionFactory.getTDSFiledDetailsAction().getHistoryToken(),
				ActionFactory.getTDSFiledDetailsAction());

		actions.put(ActionFactory.getIARSInformationAction().getHistoryToken(),
				ActionFactory.getIARSInformationAction());
		actions.put(ActionFactory.getRealisedExchangeLossesAndGainsAction()
				.getHistoryToken(), ActionFactory
				.getRealisedExchangeLossesAndGainsAction());
		actions.put(ActionFactory.getUnRealisedExchangeLossesAndGainsAction()
				.getHistoryToken(), ActionFactory
				.getUnRealisedExchangeLossesAndGainsAction());
		actions.put(ActionFactory.getEnterExchangeRatesAction()
				.getHistoryToken(), ActionFactory.getEnterExchangeRatesAction());

		actions.put(ActionFactory.getDepositAction().getHistoryToken(),
				ActionFactory.getDepositAction());

		actions.put(ActionFactory.getUploadCSVFileDialog().getHistoryToken(),
				ActionFactory.getUploadCSVFileDialog());

		actions.put(ActionFactory.getBuildAssemblyAction().getHistoryToken(),
				ActionFactory.getBuildAssemblyAction());

		actions.put(ActionFactory.getInventoryCentreAction().getHistoryToken(),
				ActionFactory.getInventoryCentreAction());

		// for job reports
		actions.put(ActionFactory.getJobProfitabilitySummaryReportAction()
				.getHistoryToken(), ActionFactory
				.getJobProfitabilitySummaryReportAction());
		actions.put(ActionFactory.getJobProfitabilityDetailReportAction()
				.getHistoryToken(), ActionFactory
				.getJobProfitabilityDetailReportAction());
		// for banking reports
		actions.put(ActionFactory.getBankDepositDetailReportAction()
				.getHistoryToken(), ActionFactory
				.getBankDepositDetailReportAction());
		actions.put(ActionFactory.getBankCheckDetailReportAction()
				.getHistoryToken(), ActionFactory
				.getBankCheckDetailReportAction());

		actions.put(ActionFactory.getNewEmployeeAction().getHistoryToken(),
				ActionFactory.getNewEmployeeAction());

		actions.put(ActionFactory.getNewPayHeadAction().getHistoryToken(),
				ActionFactory.getNewPayHeadAction());

		actions.put(
				ActionFactory.getNewEmployeeGroupAction().getHistoryToken(),
				ActionFactory.getNewEmployeeGroupAction());

		actions.put(ActionFactory.getNewPayrollUnitAction().getHistoryToken(),
				ActionFactory.getNewPayrollUnitAction());

		actions.put(ActionFactory.getEmployeeListAction().getHistoryToken(),
				ActionFactory.getEmployeeListAction());

		actions.put(ActionFactory.getPayheadListAction().getHistoryToken(),
				ActionFactory.getPayheadListAction());

		actions.put(ActionFactory.getPayrollUnitListAction().getHistoryToken(),
				ActionFactory.getPayrollUnitListAction());

		actions.put(ActionFactory.getEmployeeGroupListAction()
				.getHistoryToken(), ActionFactory.getEmployeeGroupListAction());

		actions.put(ActionFactory.getPayStructureAction().getHistoryToken(),
				ActionFactory.getPayStructureAction());

		actions.put(ActionFactory.getPayRunAction().getHistoryToken(),
				ActionFactory.getPayRunAction());

		actions.put(
				ActionFactory.getPayStructureListAction().getHistoryToken(),
				ActionFactory.getPayStructureListAction());

		actions.put(ActionFactory.getNewAttendanceProductionTypeAction()
				.getHistoryToken(), ActionFactory
				.getNewAttendanceProductionTypeAction());

		actions.put(ActionFactory.getAttendanceManagementAction()
				.getHistoryToken(), ActionFactory
				.getAttendanceManagementAction());

		actions.put(new AttendanceProductionTypeListAction().getHistoryToken(),
				new AttendanceProductionTypeListAction());

		actions.put(new PaySlipSummaryReportAction().getHistoryToken(),
				new PaySlipSummaryReportAction());

		actions.put(new PaySlipDetailReportAction().getHistoryToken(),
				new PaySlipDetailReportAction());

		actions.put(new PaySheetReportAction().getHistoryToken(),
				new PaySheetReportAction());

		actions.put(new PayHeadSummaryReportAction().getHistoryToken(),
				new PayHeadSummaryReportAction());

		actions.put(new PayHeadDetailReportAction().getHistoryToken(),
				new PayHeadDetailReportAction());

		actions.put(new PayHeadSummaryReportAction().getHistoryToken(),
				new PayHeadSummaryReportAction());

	}

	public ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	public void onSessionExpired() {
		// Accounter.getComet().stopComet();
	}
}
