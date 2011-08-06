package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.WidgetCreator;
import com.vimukti.accounter.web.client.ui.grids.CompanyFinancialWidgetGrid;
import com.vimukti.accounter.web.client.ui.grids.CustomerWidgetGrid;

public class DashBoardView extends BaseHomeView {

	ArrayList<String> addablePortletList = new ArrayList<String>();

	private String dashboardPreference;
	private PortalLayout portalLayout;
	private Portlet[] portlet;
	private WidgetCreator creator = new WidgetCreator();
	private String[] widgetOnSectionPage;
	private CustomerWidgetGrid customerWidgetGrid;
	private CompanyFinancialWidgetGrid grid;

	public DashBoardPortlet gettingStartedPortlet;
	public DashBoardPortlet bankingPortlet;
	public DashBoardPortlet moneyComingPortlet;
	public DashBoardPortlet moneyGoingPortlet;
	public DashBoardPortlet expenseClaimsPortlet;

	private Timer timer;

	// private String[] secondColumn;
	//
	// private String[] firstColumn;

	public DashBoardView() {
		dashboardPreference = Accounter.constants().welcomeBankingSummary();
		/*
		 * FinanceApplication.getUser().getUserPreferences()
		 * .getDashBoardPreferences();
		 */

	}

	@Override
	public void init() {
		super.init();
		getLeftLayout().add(createControl());
		setSize("100%", "100%");

	}

	private Widget createControl() {
		portalLayout = new PortalLayout(this, 1);

		widgetOnSectionPage = dashboardPreference.split(",");
		portlet = new Portlet[widgetOnSectionPage.length];
		for (int i = 0; i < widgetOnSectionPage.length; i++) {

			final int index = i;
			if (widgetOnSectionPage[i].equals("")) {

			} else {
				portlet[i] = creator.getWidgetByName(widgetOnSectionPage[i]);

				portalLayout.add(portlet[i]);
				portlet[i].refreshClicked();
			}
		}

		getAddableWidgets(widgetOnSectionPage);

		gettingStartedPortlet = new GettingStartedPortlet(Accounter.constants()
				.gettingStartedusingAccounter());

		bankingPortlet = new BankingPortlet(Accounter.constants()
				.bankAccounts());
		moneyComingPortlet = new MoneyComingPortlet(Accounter.constants()
				.moneyComingIn());
		moneyGoingPortlet = new MoneyGoingPortlet(Accounter.constants()
				.moneyGoingOut());
		expenseClaimsPortlet = new ExpenseClaimPortlet(Accounter.constants()
				.expenseClaims());
		FlexTable fTable = new FlexTable();

		fTable.setWidget(0, 0, bankingPortlet);
		fTable.setWidget(0, 1, moneyComingPortlet);
		fTable.setWidget(1, 1, moneyGoingPortlet);
		fTable.setWidget(1, 0, expenseClaimsPortlet);

		try {
			bankingPortlet.getElement().getParentElement()
					.setClassName("banking-portlet");
			expenseClaimsPortlet.getElement().getParentElement()
					.setClassName("expense_claim_portlet");
		} catch (Exception e) {
			System.err.println("Exception :" + e);
		}

		fTable.getCellFormatter().setVerticalAlignment(1, 0,
				HasVerticalAlignment.ALIGN_TOP);


		// mainLayOut.add(gettingStartedPortlet);
		fTable.setWidth("100%");
		gettingStartedPortlet.setVisible(true);
		// leftLayout.add(addWidgetLinkLayout);
		// leftLayout.add(portalLayout);
		return fTable;

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
		String[] totalWidget = { Accounter.constants().welcome(),
				Accounter.constants().bankingSummary(),
				Accounter.constants().profitAndLoss(),
				Accounter.constants().creditOverview(),
				Accounter.constants().debitOverview(),
				Accounter.constants().latestQuote(),
				Accounter.constants().expenses() };

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
	protected void onAttach() {
		creator.setContinueRequest(true);
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
		bankingPortlet.refreshWidget();
		moneyComingPortlet.refreshWidget();
		moneyGoingPortlet.refreshWidget();
		expenseClaimsPortlet.refreshWidget();
	}

	public void showGettingStarted() {
		gettingStartedPortlet.setVisible(true);
	}

	public void hideGettingStarted() {
		gettingStartedPortlet.setVisible(false);
	}

}
