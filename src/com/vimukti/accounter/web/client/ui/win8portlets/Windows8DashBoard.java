package com.vimukti.accounter.web.client.ui.win8portlets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.portlet.PortletPage;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.BaseHomeView;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class Windows8DashBoard extends BaseHomeView {

	private PortletPage page;
	private StyledPanel mainPanel;
	private StyledPanel portletsPanel;
	private StyledPanel quicklinksPanel;
	private Map<String, String> quickLinksMap;
	private Button moreButton;

	public Windows8DashBoard() {

	}

	@Override
	public void init() {
		super.add(new HTML("<h2>" + Accounter.getMessages().dashBoard() + ""));
		// super.init();
		add(createControl());

	}

	private Widget createControl() {
		mainPanel = new StyledPanel("dashBoard");
		page = new PortletPage(PortletPage.DASHBOARD);
		quicklinksPanel = new StyledPanel("quickLinks-panel");

		portletsPanel = new StyledPanel("portlets-panel");

		createQuickLinksPanel();

		moreButton = new Button();
		moreButton.setText("More...");
		moreButton.getElement().setId("moreButton");
		moreButton.setTitle("click here to go to menu");
		moreButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				// ActionFactory.getAccounterMenuAction().run(null, false);
			}
		});
		quicklinksPanel.add(moreButton);
		// TODO add portlets to windows8
		// portletsPanel.add(page);
		// mainPanel.add(portletsPanel);

		mainPanel.add(quicklinksPanel);
		return mainPanel;
	}

	private void createQuickLinksPanel() {
		quickLinksMap = new HashMap<String, String>();
		// initializing links...
		quickLinksMap.put(messages.newInvoice(), ActionFactory
				.getNewInvoiceAction().getHistoryToken());

		quickLinksMap.put(messages.expenses(),
				ActionFactory.getExpensesAction("").getHistoryToken());

		quickLinksMap.put(messages.payees(Global.get().Customers()),
				ActionFactory.getCustomerCentre().getHistoryToken());

		quickLinksMap.put(messages.writeCheck(), ActionFactory
				.getWriteChecksAction().getHistoryToken());

		// quickLinksMap.put(messages.depositTransferFunds(), ActionFactory
		// .getTransferFundsAction().getHistoryToken());

		quickLinksMap.put(messages.bills(), ActionFactory.getBillsAction()
				.getHistoryToken());

		if (Accounter.getCompany().getPreferences().isKeepTrackofBills()) {
			quickLinksMap.put(messages.enterBill(), ActionFactory
					.getEnterBillsAction().getHistoryToken());
		}

		quickLinksMap.put(messages.cashExpense(), ActionFactory
				.CashExpenseAction().getHistoryToken());

		quickLinksMap.put(messages.chartOfAccounts(), ActionFactory
				.getChartOfAccountsAction().getHistoryToken());

		quickLinksMap.put(messages.creditCardExpense(), ActionFactory
				.CreditCardExpenseAction().getHistoryToken());

		quickLinksMap.put(messages.newQuote(),
				ActionFactory.getNewQuoteAction(ClientEstimate.QUOTES)
						.getHistoryToken());
		if (Accounter.getCompany().getPreferences().isDelayedchargesEnabled()) {
			quickLinksMap.put(messages.newCharge(), ActionFactory
					.getNewQuoteAction(ClientEstimate.CHARGES)
					.getHistoryToken());
		}
		quickLinksMap.put(messages.newJournalEntry(), ActionFactory
				.getNewJournalEntryAction().getHistoryToken());

		quickLinksMap.put(messages.receivePayment(), ActionFactory
				.getReceivePaymentAction().getHistoryToken());

		quickLinksMap.put(messages.Reconcile(), ActionFactory
				.getReconciliationsListAction().getHistoryToken());

		quickLinksMap.put(messages.payees(Global.get().Vendor()), ActionFactory
				.getVendorsAction().getHistoryToken());

		quickLinksMap.put(messages.reports(), ActionFactory
				.getReportsHomeAction().getHistoryToken());

		// adding to flex table...
		Iterator<String> iterator = quickLinksMap.keySet().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			if (i == 3) {
				i = 0;
			} else {
				final String linkName = iterator.next();
				Anchor link = new Anchor(linkName);
				link.setStyleName("quickLinkBox");
				link.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						historyChanged(quickLinksMap.get(linkName));
					}
				});
				quicklinksPanel.add(link);
				i++;
			}
		}
	}

	protected void historyChanged(String value) {
		Action<?> action = MainFinanceWindow.actions.get(value);
		action.run();
	}

	@Override
	protected void onAttach() {
		getPage().refreshWidgets();
		super.onAttach();
	}

	@Override
	protected void onUnload() {
		super.onUnload();
	}

	public void refreshWidgetData() {
	}

	public void showGettingStarted() {
	}

	public void hideGettingStarted() {
	}

	public PortletPage getPage() {
		return page;
	}

	public void setPage(PortletPage page) {
		this.page = page;
	}

}
