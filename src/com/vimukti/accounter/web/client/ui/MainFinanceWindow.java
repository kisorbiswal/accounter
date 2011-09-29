package com.vimukti.accounter.web.client.ui;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zschech.gwt.comet.client.CometClient;
import net.zschech.gwt.comet.client.CometListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.comet.AccounterCometSerializer;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.company.HelpItem;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.customers.InvoiceListView;
import com.vimukti.accounter.web.client.ui.reports.ProfitAndLossByLocationAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByLocationDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByLocationSummaryAction;

/**
 * 
 * 
 */

public class MainFinanceWindow extends VerticalPanel {

	private static ViewManager viewManager;
	private Header header;
	private int height;
	private int width;
	private HelpItem item;
	public Map<String, Action> actions;
	private CometClient cometClient;

	public MainFinanceWindow() {
		initializeActionsWithTokens();
		createControls();
		sinkEvents(Event.ONMOUSEOVER);
	}

	private void createControls() {
		viewManager = new ViewManager(this);
		header = new Header();

		ClientCompany company = Accounter.getCompany();
		add(header);
		// If company is configured then show the dashboard
		// if (company.isConfigured()) {
		HorizontalMenuBar hMenuBar = new HorizontalMenuBar();
		if (!Accounter.isMacApp()) {
			add(hMenuBar);
		}

		add(viewManager);
		Label help = new Label(Accounter.constants().helpLinks());
		help.addStyleName("down-panel");
		if (item == null) {
			item = new HelpItem();
		}
		addStyleName(Accounter.constants().financeWindow());

		if (UIUtils.isMSIEBrowser()) {
			this.getElement().getStyle().setPaddingTop(0, Unit.PX);
			this.getElement().getStyle().setPaddingBottom(0, Unit.PX);
		}

		ActionFactory.getCompanyHomeAction().run(null, false);
		// } else {
		// // if company is not configured then show the setupwizard
		// SetupWizard setupWizard = new SetupWizard();
		// add(setupWizard);
		// }

	}

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
		// ClientCometManager.getInstance().initComet();
		//
		// BaseView<?> view = viewManager.getContentPanel();
		// viewManager.fitToSize(height, width);

		// setHeight(this.height + "px");
		// if (view == null)
		// viewManager.fitToSize(width - 10, height);
		// else {
		// view.setHeightForCanvas((height * 80.6 / 100) + "");
		// view.getButtonPanel().setHeight("30px");
		// }
		super.onLoad();
		viewManager.fitToSize(this.getOffsetHeight(), 960);
		// if (GWT.isScript())
		startCometService();
		this.getElement().getParentElement()
				.addClassName("main-finance-window");
	}

	private void startCometService() {
		AccounterCometSerializer serializer = GWT
				.create(AccounterCometSerializer.class);
		this.cometClient = new CometClient("/do/comet", serializer,
				new CometListener() {

					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onMessage(List<? extends Serializable> messages) {
						for (Serializable serializableObj : messages) {
							Accounter.getCompany().processCommand(
									(IAccounterCore) serializableObj);
						}
					}

					@Override
					public void onHeartbeat() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onError(Throwable exception, boolean connected) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onDisconnected() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onConnected(int heartbeat) {
						// TODO Auto-generated method stub

					}
				});
		cometClient.start();
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
		if (cometClient != null) {
			this.cometClient.stop();
		}
		this.getParent().removeStyleName("noScroll");
		super.onDetach();

	}

	@Override
	protected void onUnload() {
		super.onUnload();
		// TODO
		// AccounterCometClient.cometStop();
	}

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

					public void onException(AccounterException caught) {
						Accounter.showError(Accounter.constants()
								.unableToshowtheview());
					}

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

	private void initializeActionsWithTokens() {
		actions = new HashMap<String, Action>();
		actions.put(ActionFactory.getCompanyHomeAction().getHistoryToken(),
				ActionFactory.getCompanyHomeAction());
		actions.put(ActionFactory.getNewJournalEntryAction().getHistoryToken(),
				ActionFactory.getNewJournalEntryAction());
		actions.put(ActionFactory.getNewAccountAction().getHistoryToken(),
				ActionFactory.getNewAccountAction());
		actions.put(ActionFactory.getPreferencesAction().getHistoryToken(),
				ActionFactory.getPreferencesAction());
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

		actions.put(ActionFactory.getItemsAction().getHistoryToken(),
				ActionFactory.getItemsAction());
		actions.put(ActionFactory.getCustomersAction().getHistoryToken(),
				ActionFactory.getCustomersAction());
		actions.put(ActionFactory.getVendorsAction().getHistoryToken(),
				ActionFactory.getVendorsAction());
		actions.put(ActionFactory.getPaymentsAction().getHistoryToken(),
				ActionFactory.getPaymentsAction());

		actions.put(ActionFactory.getSalesPersonAction().getHistoryToken(),
				ActionFactory.getSalesPersonAction());
		actions.put(ActionFactory.getNewVatItemAction().getHistoryToken(),
				ActionFactory.getNewVatItemAction());
		actions.put(ActionFactory.getNewTAXCodeAction().getHistoryToken(),
				ActionFactory.getNewTAXCodeAction());
		actions.put(ActionFactory.getNewTAXAgencyAction().getHistoryToken(),
				ActionFactory.getNewTAXAgencyAction());
		actions.put(ActionFactory.getAdjustTaxAction().getHistoryToken(),
				ActionFactory.getAdjustTaxAction());
		actions.put(ActionFactory.getFileVatAction().getHistoryToken(),
				ActionFactory.getFileVatAction());
		actions.put(ActionFactory.getpayVATAction().getHistoryToken(),
				ActionFactory.getpayVATAction());
		actions.put(ActionFactory.getreceiveVATAction().getHistoryToken(),
				ActionFactory.getreceiveVATAction());
		actions.put(ActionFactory.getVatItemListAction().getHistoryToken(),
				ActionFactory.getVatItemListAction());

		actions.put(ActionFactory.getTAXCodeListAction().getHistoryToken(),
				ActionFactory.getTAXCodeListAction());

		actions.put(ActionFactory.getCustomersHomeAction().getHistoryToken(),
				ActionFactory.getCustomersHomeAction());
		actions.put(ActionFactory.getNewCustomerAction().getHistoryToken(),
				ActionFactory.getNewCustomerAction());

		actions.put(ActionFactory.getNewQuoteAction().getHistoryToken(),
				ActionFactory.getNewQuoteAction());
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
		actions.put(ActionFactory.getItemsAction().getHistoryToken(),
				ActionFactory.getItemsAction());
		actions.put(ActionFactory.getQuotesAction().getHistoryToken(),
				ActionFactory.getQuotesAction());
		actions.put(ActionFactory.getInvoicesAction(null).getHistoryToken(),
				ActionFactory.getInvoicesAction(null));
		actions.put(
				ActionFactory.getReceivedPaymentsAction().getHistoryToken(),
				ActionFactory.getReceivedPaymentsAction());
		actions.put(ActionFactory.getCustomerRefundsAction().getHistoryToken(),
				ActionFactory.getCustomerRefundsAction());

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
		actions.put(ActionFactory.getRecordExpensesAction().getHistoryToken(),
				ActionFactory.getRecordExpensesAction());
		actions.put(ActionFactory.getExpenseClaimsAction(0).getHistoryToken(),
				ActionFactory.getExpenseClaimsAction(0));

		actions.put(ActionFactory.getVendorsAction().getHistoryToken(),
				ActionFactory.getVendorsAction());
		actions.put(ActionFactory.getItemsAction().getHistoryToken(),
				ActionFactory.getItemsAction());
		actions.put(ActionFactory.getBillsAction().getHistoryToken(),
				ActionFactory.getBillsAction());
		actions.put(ActionFactory.getVendorPaymentsAction().getHistoryToken(),
				ActionFactory.getVendorPaymentsAction());

		actions.put(ActionFactory.getNewBankAccountAction().getHistoryToken(),
				ActionFactory.getNewBankAccountAction());
		actions.put(ActionFactory.getWriteChecksAction().getHistoryToken(),
				ActionFactory.getWriteChecksAction());
		actions.put(ActionFactory.getMakeDepositAction().getHistoryToken(),
				ActionFactory.getMakeDepositAction());
		actions.put(ActionFactory.getPayBillsAction().getHistoryToken(),
				ActionFactory.getPayBillsAction());
		actions.put(
				ActionFactory.getCreditCardChargeAction().getHistoryToken(),
				ActionFactory.getCreditCardChargeAction());
		actions.put(ActionFactory.getPaymentsAction().getHistoryToken(),
				ActionFactory.getPaymentsAction());

		actions.put(ActionFactory.getSalesOrderAction().getHistoryToken(),
				ActionFactory.getSalesOrderAction());
		actions.put(ActionFactory.getSalesOrderListAction().getHistoryToken(),
				ActionFactory.getSalesOrderListAction());
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
		actions.put(ActionFactory.getStatementReport().getHistoryToken(),
				ActionFactory.getStatementReport());
		actions.put(ActionFactory.getCustomerTransactionHistoryAction()
				.getHistoryToken(), ActionFactory
				.getCustomerTransactionHistoryAction());
		actions.put(ActionFactory.getSalesByCustomerSummaryAction()
				.getHistoryToken(), ActionFactory
				.getSalesByCustomerSummaryAction());
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
		actions.put(ActionFactory.getVATItemSummaryReportAction()
				.getHistoryToken(), ActionFactory
				.getVATItemSummaryReportAction());
		actions.put(ActionFactory.getECSalesListAction().getHistoryToken(),
				ActionFactory.getECSalesListAction());

		actions.put(ActionFactory.getGeneralSettingsAction().getHistoryToken(),
				ActionFactory.getGeneralSettingsAction());

		actions.put("bankAccounts",
				ActionFactory.getChartOfAccountsAction(ClientAccount.TYPE_BANK));
		actions.put("cashExpenses",
				ActionFactory.getExpensesAction(Accounter.constants().cash()));
		actions.put("creditCardExpenses", ActionFactory
				.getExpensesAction(Accounter.constants().creditCard()));
		actions.put("employeeExpenses", ActionFactory
				.getExpensesAction(Accounter.constants().employee()));
		actions.put(ActionFactory.getAccountRegisterAction().getHistoryToken(),
				ActionFactory.getAccountRegisterAction());
		actions.put("overDueInvoices",
				ActionFactory.getInvoicesAction(InvoiceListView.OVER_DUE));
		actions.put(ActionFactory.getUserDetailsAction().getHistoryToken(),
				ActionFactory.getUserDetailsAction());
		actions.put(ActionFactory.getPrepare1099MISCAction().getHistoryToken(),
				ActionFactory.getPrepare1099MISCAction());

		actions.put(ActionFactory.getReconciliationsListAction()
				.getHistoryToken(), ActionFactory
				.getReconciliationsListAction());

		actions.put(ActionFactory.getReportsHomeAction().getHistoryToken(),
				ActionFactory.getReportsHomeAction());

		ProfitAndLossByLocationAction profitAndLossByLocationActionTrue = ActionFactory
				.getProfitAndLossByLocationAction(true);

		ProfitAndLossByLocationAction profitAndLossByLocationActionFalse = ActionFactory
				.getProfitAndLossByLocationAction(false);

		actions.put(profitAndLossByLocationActionTrue.getHistoryToken(),
				profitAndLossByLocationActionTrue);

		actions.put(profitAndLossByLocationActionFalse.getHistoryToken(),
				profitAndLossByLocationActionFalse);

		SalesByLocationDetailsAction salesByLocationDetailActionTrue = ActionFactory
				.getSalesByLocationDetailsAction(true);
		SalesByLocationSummaryAction salesByLocationSummaryActionTrue = ActionFactory
				.getSalesByLocationSummaryAction(true);

		actions.put(salesByLocationDetailActionTrue.getHistoryToken(),
				salesByLocationDetailActionTrue);

		actions.put(salesByLocationSummaryActionTrue.getHistoryToken(),
				salesByLocationSummaryActionTrue);

		SalesByLocationDetailsAction salesByLocationDetailActionFalse = ActionFactory
				.getSalesByLocationDetailsAction(false);
		SalesByLocationSummaryAction salesByLocationSummaryActionFalse = ActionFactory
				.getSalesByLocationSummaryAction(false);

		actions.put(salesByLocationDetailActionFalse.getHistoryToken(),
				salesByLocationDetailActionFalse);

		actions.put(salesByLocationSummaryActionFalse.getHistoryToken(),
				salesByLocationSummaryActionFalse);

		actions.put(ActionFactory.getUsersActivityListAction()
				.getHistoryToken(), ActionFactory.getUsersActivityListAction());

		NewItemAction supplierItem = ActionFactory.getNewItemAction(false);
		actions.put(supplierItem.getHistoryToken(), supplierItem);

		NewItemAction customerItem = ActionFactory.getNewItemAction(true);
		actions.put(customerItem.getHistoryToken(), customerItem);

	}

	public ClientCompany getCompany() {
		return Accounter.getCompany();
	}
}
