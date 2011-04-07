package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.WidgetCreator;
import com.vimukti.accounter.web.client.ui.grids.CompanyFinancialWidgetGrid;
import com.vimukti.accounter.web.client.ui.grids.CustomerWidgetGrid;

public class DashBoard extends BaseHomeView {

	ArrayList<String> addablePortletList = new ArrayList<String>();

	@SuppressWarnings("unused")
	private DashBoard dashboard = null;
	private String dashboardPreference;
	private PortalLayout portalLayout;
	private Portlet[] portlet;
	private WidgetCreator creator;
	private String[] widgetOnSectionPage;
	private CustomerWidgetGrid customerWidgetGrid;
	private CompanyFinancialWidgetGrid grid;

	public static DashBoardPortlet gettingStartedPortlet;
	public DashBoardPortlet bankingPortlet;
	public DashBoardPortlet moneyComingPortlet;
	public DashBoardPortlet moneyGoingPortlet;
	public DashBoardPortlet expenseClaimsPortlet;

	public static VerticalPanel mainLayOut;

	private Timer timer;

	// private String[] secondColumn;
	//
	// private String[] firstColumn;

	public DashBoard() {
		dashboard = this;
		dashboardPreference = FinanceApplication.getCompanyMessages()
				.WELCOMEBANKINGSUMMARY();
		/*
		 * FinanceApplication.getUser().getUserPreferences()
		 * .getDashBoardPreferences();
		 */

	}

	@Override
	public void init() {
		getLeftLayout().add(createControl());
		setSize("99.9%", "100%");

	}

	private VerticalPanel createControl() {
		mainLayOut = new VerticalPanel();
		creator = new WidgetCreator();

		portalLayout = new PortalLayout(this, 1);

		widgetOnSectionPage = dashboardPreference.split(",");
		portlet = new Portlet[widgetOnSectionPage.length];
		for (int i = 0; i < widgetOnSectionPage.length; i++) {
			@SuppressWarnings("unused")
			final int index = i;
			if (widgetOnSectionPage[i].equals("")) {

			} else {
				portlet[i] = creator.getWidgetByName(widgetOnSectionPage[i]);

				portalLayout.add(portlet[i]);
				portlet[i].refreshClicked();
			}
		}

		getAddableWidgets(widgetOnSectionPage);

		gettingStartedPortlet = new GettingStartedPortlet(
				"Getting Started using Accounter");

		bankingPortlet = new BankingPortlet(FinanceApplication
				.getCompanyMessages().bankAccounts());
		moneyComingPortlet = new MoneyComingPortlet(FinanceApplication
				.getCompanyMessages().moneyComingIn());
		moneyGoingPortlet = new MoneyGoingPortlet(FinanceApplication
				.getCompanyMessages().moneyGoingOut());
		expenseClaimsPortlet = new ExpenseClaimPortlet(FinanceApplication
				.getCompanyMessages().expenseClaims());
		FlexTable fTable = new FlexTable();

		fTable.setWidget(0, 0, bankingPortlet);
		fTable.setWidget(0, 1, moneyComingPortlet);
		fTable.setWidget(1, 1, moneyGoingPortlet);
		fTable.setWidget(1, 0, expenseClaimsPortlet);
		fTable.getCellFormatter().setVerticalAlignment(1, 0,
				HasVerticalAlignment.ALIGN_TOP);

		// VerticalPanel leftLayout = new VerticalPanel();
		mainLayOut.setSize("100%", "100%");

		// mainLayOut.add(gettingStartedPortlet);
		mainLayOut.add(fTable);
		if (Header.gettingStartedStatus.equals("Show Getting Started"))
			gettingStartedPortlet.setVisible(false);
		else
			gettingStartedPortlet.setVisible(true);
		// leftLayout.add(addWidgetLinkLayout);
		// leftLayout.add(portalLayout);
		return mainLayOut;

	}

	public void refreshGrids(final IAccounterCore accounterCoreObject) {

		timer = new Timer() {

			@Override
			public void run() {
				IAccounterCore core = accounterCoreObject;
				if (core.getObjectType() == AccounterCoreType.CUSTOMER
						|| ((ClientAccount) core).getType() == ClientAccount.TYPE_INCOME) {
					portlet[1].refreshClicked();
				} else {
					if (core.getObjectType() == AccounterCoreType.ACCOUNT)
						portlet[0].refreshClicked();
				}

			}

		};
		timer.schedule(600);

	}

	public void getAddableWidgets(String[] widgetOnSectionPage) {
		String[] totalWidget = {
				FinanceApplication.getFinanceUIConstants().welcome(),
				FinanceApplication.getFinanceUIConstants().bankingSummary(),
				FinanceApplication.getFinanceUIConstants().profitAndLoss(),
				FinanceApplication.getFinanceUIConstants().creditOverview(),
				FinanceApplication.getFinanceUIConstants().debitOverview(),
				FinanceApplication.getFinanceUIConstants().latestQuote(),
				FinanceApplication.getFinanceUIConstants().expenses() };

		boolean isAvailable = false;

		for (int i = 0; i < totalWidget.length; i++) {
			for (int k = 0; k < widgetOnSectionPage.length; k++) {
				if (totalWidget[i].equals(widgetOnSectionPage[k])) {
					isAvailable = true;
					break;
				} else {
					isAvailable = false;
				}

			}
			if (!isAvailable) {
				addablePortletList.add(totalWidget[i]);
				isAvailable = false;
			}
		}

	}

	public PortalLayout getPortalLayout() {
		return portalLayout;
	}

	public void setPortalLayout(PortalLayout portalLayout) {
		this.portalLayout = portalLayout;
	}

	public ArrayList<String> getAddablePortletList() {
		return addablePortletList;
	}

	public void setAddablePortletList(ArrayList<String> addablePortletList) {
		this.addablePortletList = addablePortletList;
	}

	@Override
	public boolean shouldSaveInHistory() {
		// Company Home Should always be in History so shouldSaveInHistory
		// should true
		return true;
	}

	@Override
	public void fitToSize(int height, int width) {

		// if (height > 0) {
		// if (height - 205 > 220) {
		// creator.setCustomerWidgetHeight(height - 305);
		// creator.setCompanyFinancialWidgetHeight(205);
		// } else {
		// creator.setCustomerWidgetHeight(height - 260);
		// creator.setCompanyFinancialWidgetHeight(205);
		// }
		//
		// }
		if (height > 0) {
			creator.setCompanyFinancialWidgetHeight(205);
			creator.setCustomerWidgetHeight(height - 200);
		}
	}

	@Override
	protected void onAttach() {
		creator.setContinueRequest(true);
		// timer.scheduleRepeating(60000);
		super.onAttach();
	}

	@Override
	protected void onUnload() {
		creator.setContinueRequest(false);
		if (timer != null)
			timer.cancel();
		super.onUnload();
	}

	public void refreshWidgetData(IAccounterCore accounterCoreObject) {
		int transactionType = 0;
		if (accounterCoreObject == null) {
			bankingPortlet.refreshWidget();
			moneyComingPortlet.refreshWidget();
		}

		if (accounterCoreObject instanceof ClientTransaction)
			transactionType = ((ClientTransaction) accounterCoreObject)
					.getType();
		else if (accounterCoreObject instanceof ClientAccount
				&& ((ClientAccount) accounterCoreObject).getType() == ClientAccount.TYPE_BANK)
			bankingPortlet.refreshWidget();

		if (transactionType == ClientTransaction.TYPE_CASH_EXPENSE
				|| transactionType == ClientTransaction.TYPE_EMPLOYEE_EXPENSE
				|| transactionType == ClientTransaction.TYPE_CREDIT_CARD_EXPENSE) {
			expenseClaimsPortlet.refreshWidget();
		}
		if (transactionType == ClientTransaction.TYPE_INVOICE
				|| transactionType == ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO
				|| transactionType == ClientTransaction.TYPE_CUSTOMER_PREPAYMENT
				|| transactionType == ClientTransaction.TYPE_CUSTOMER_REFUNDS
				|| transactionType == ClientTransaction.TYPE_RECEIVE_PAYMENT) {
			moneyComingPortlet.refreshWidget();
		}
		if (transactionType == ClientTransaction.TYPE_CREDIT_CARD_CHARGE
				|| transactionType == ClientTransaction.TYPE_ENTER_BILL
				|| transactionType == ClientTransaction.TYPE_PAY_BILL
				|| transactionType == ClientTransaction.TYPE_VENDOR_CREDIT_MEMO
				|| transactionType == ClientTransaction.TYPE_VENDOR_PAYMENT
				|| transactionType == ClientTransaction.TYPE_WRITE_CHECK) {
			moneyGoingPortlet.refreshWidget();
			bankingPortlet.refreshWidget();
			moneyComingPortlet.refreshWidget();
		}
	}

	public static void showGettingStarted() {
		// mainLayOut.insert(gettingStartedPortlet, 1);
		gettingStartedPortlet.setVisible(true);
	}

	public static void hideGettingStarted() {
		gettingStartedPortlet.setVisible(false);
	}
}
