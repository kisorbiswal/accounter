package com.vimukti.accounter.web.client.ui.win8portlets;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.core.ClientPortletPageConfiguration;
import com.vimukti.accounter.web.client.portlet.PortletColumn;
import com.vimukti.accounter.web.client.portlet.PortletFactory;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Portlet;
import com.vimukti.accounter.web.client.ui.StyledPanel;

public class PortletPage extends FlowPanel {

	public static final String DASHBOARD = "dashboard";

	private String name;
	public ClientPortletPageConfiguration config;
	private PortletColumn[] columns;
	// private PickupDragController dragController;
	public boolean haveToRefresh = true;
	private DashboardPortletsData portletsData;
	private final DateRangeUtil dateRangeUtil;
	private ScrollPanel graphPortletsScrollPanel;
	private StyledPanel graphPortletsPanel;

	public PortletPage(String pageName) {
		dateRangeUtil = new DateRangeUtil();
		this.name = pageName;
		refreshPage();
	}

	public void refreshPage() {

		config = Accounter.getCompany().getPortletPageConfiguration(name);
		setup();
		refreshWidgets();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	private void setup() {
		StyledPanel panel = new StyledPanel("panel");
		this.add(panel);
		graphPortletsScrollPanel = new ScrollPanel();
		graphPortletsPanel = new StyledPanel("graphPortletsPanel");
		// create columns
		columns = new PortletColumn[config.getPortletConfigurations().size()];
		for (int x = 0; x < config.getPortletConfigurations().size(); x++) {
			columns[x] = new PortletColumn(x);
			panel.add(columns[x]);
			columns[x].getElement().addClassName("portlet");
		}
		// create the portlets in them
		// sendRequestForPortletData();
		graphPortletsScrollPanel.add(graphPortletsPanel);
	}

	/*
	 * private void sendRequestForPortletData() { if (config != null) {
	 * List<ClientPortletConfiguration> configurations = config
	 * .getPortletConfigurations(); DashboardPortletDataRequest request = new
	 * DashboardPortletDataRequest(); for (ClientPortletConfiguration cpc :
	 * configurations) { setRequestData(cpc, request); }
	 * Accounter.createHomeService().getDashbardData(request, new
	 * AsyncCallback<DashboardPortletsData>() {
	 * 
	 * @Override public void onSuccess(DashboardPortletsData result) {
	 * portletsData = result; for (ClientPortletConfiguration pc : config
	 * .getPortletConfigurations()) { addPortletToPage(pc); } }
	 * 
	 * @Override public void onFailure(Throwable caught) {
	 * System.out.println(caught.getMessage()); } }); } }
	 */

	private void setRequestData(ClientPortletConfiguration cpc,
			DashboardPortletDataRequest request) {
		if (cpc.getName().equals(PortletFactory.ACCOUNTS_BALANCES_PORTLET)) {
			if (cpc.getPortletMap() != null
					&& cpc.getPortletMap().get(Portlet.ACCOUNT_TYPE) != null) {
				request.setAccountBalances_AccountType(cpc.getPortletMap().get(
						Portlet.ACCOUNT_TYPE));
			}
		} else if (cpc.getName().equals(PortletFactory.EXPENSES_CLAIM)) {
			if (cpc.getPortletMap() != null
					&& cpc.getPortletMap().get(Portlet.DATE_RANGE) != null) {
				dateRangeUtil.dateRangeChanged(cpc.getPortletMap().get(
						Portlet.DATE_RANGE));
				request.setExpenseClaims_StartDate(dateRangeUtil.getStartDate());
				request.setExpenseClaims_EndDate(dateRangeUtil.getEndDate());
			}
		} else if (cpc.getName()
				.equals(PortletFactory.INCOME_BREAKDOWN_PORTLET)) {
			if (cpc.getPortletMap() != null
					&& cpc.getPortletMap().get(Portlet.DATE_RANGE) != null) {
				dateRangeUtil.dateRangeChanged(cpc.getPortletMap().get(
						Portlet.DATE_RANGE));
				request.setIncomeBreakdown_StartDate(dateRangeUtil
						.getStartDate());
				request.setIncomeBreakdown_EndDate(dateRangeUtil.getEndDate());
			}
		} else if (cpc.getName().equals(
				PortletFactory.INCOME_EXPENSE_BREAKDOWN_PORTLET)) {
			if (cpc.getPortletMap() != null
					&& cpc.getPortletMap().get(Portlet.DATE_RANGE) != null) {
				dateRangeUtil.dateRangeChanged(cpc.getPortletMap().get(
						Portlet.DATE_RANGE));
				request.setIncomeAndExpense_dateRangeType(dateRangeUtil
						.getDateRangeType(cpc.getPortletMap().get(
								Portlet.DATE_RANGE)));
				request.setIncomeAndExpense_StartDate(dateRangeUtil
						.getStartDate());
				request.setIncomeAndExpense_EndDate(dateRangeUtil.getEndDate());
			}
		} else if (cpc.getName().equals(PortletFactory.RECENT_TRANSACTIONS)) {
			if (cpc.getPortletMap() != null
					&& cpc.getPortletMap().get(Portlet.LIMIT) != null) {
				request.setRecentTransactions_Limit(Integer.parseInt(cpc
						.getPortletMap().get(Portlet.LIMIT)));
			}
		} else if (cpc.getName().equals(
				PortletFactory.TOP_CUSTOMERS_BY_SALES_PORTLET)) {
			if (cpc.getPortletMap() != null) {
				if (cpc.getPortletMap().get(Portlet.DATE_RANGE) != null) {
					dateRangeUtil.dateRangeChanged(cpc.getPortletMap().get(
							Portlet.DATE_RANGE));
					request.setTopCustomers_StartDate(dateRangeUtil
							.getStartDate());
					request.setTopCustomers_EndDate(dateRangeUtil.getEndDate());
				}
				if (cpc.getPortletMap().get(Portlet.LIMIT) != null) {
					request.setTopCustomers_Limit(Integer.parseInt(cpc
							.getPortletMap().get(Portlet.LIMIT)));
				}
			}
		} else if (cpc.getName().equals(
				PortletFactory.TOP_ITEMS_BY_SALES_PORTLET)) {
			if (cpc.getPortletMap() != null) {
				if (cpc.getPortletMap().get(Portlet.DATE_RANGE) != null) {
					dateRangeUtil.dateRangeChanged(cpc.getPortletMap().get(
							Portlet.DATE_RANGE));
					request.setTopItems_StartDate(dateRangeUtil.getStartDate());
					request.setTopItems_EndDate(dateRangeUtil.getEndDate());
				}
				if (cpc.getPortletMap().get(Portlet.LIMIT) != null) {
					request.setTopItems_Limit(Integer.parseInt(cpc
							.getPortletMap().get(Portlet.LIMIT)));
				}
			}
		} else if (cpc.getName().equals(
				PortletFactory.TOP_VENDORS_BY_EXPENSES_PORTLET)) {
			if (cpc.getPortletMap() != null) {
				if (cpc.getPortletMap().get(Portlet.DATE_RANGE) != null) {
					dateRangeUtil.dateRangeChanged(cpc.getPortletMap().get(
							Portlet.DATE_RANGE));
					request.setTopVendors_StartDate(dateRangeUtil
							.getStartDate());
					request.setTopVendors_EndDate(dateRangeUtil.getEndDate());
				}
				if (cpc.getPortletMap().get(Portlet.LIMIT) != null) {
					request.setTopVendors_Limit(Integer.parseInt(cpc
							.getPortletMap().get(Portlet.LIMIT)));
				}
			}
		} else if (cpc.getName().equals(
				PortletFactory.YEAR_OVER_YEAR_EXPENSE_PORTLET)) {
			if (cpc.getPortletMap() != null) {
				if (cpc.getPortletMap().get(Portlet.DATE_RANGE) != null) {
					dateRangeUtil.dateRangeChanged(cpc.getPortletMap().get(
							Portlet.DATE_RANGE));
					request.setYearOverYearExpense_StartDate(dateRangeUtil
							.getStartDate());
					request.setYearOverYearExpense_EndDate(dateRangeUtil
							.getEndDate());
					request.setYearOverYearExpense_dateRangeType(dateRangeUtil
							.getDateRangeType(cpc.getPortletMap().get(
									Portlet.DATE_RANGE)));
				}
				if (cpc.getPortletMap().get(Portlet.LIMIT) != null) {
					request.setYearOverYearExpense_AccountId(Long.parseLong(cpc
							.getPortletMap().get(Portlet.LIMIT)));
				}

			}
		} else if (cpc.getName().equals(
				PortletFactory.YEAR_OVER_YEAR_INCOME_PORTLET)) {
			if (cpc.getPortletMap() != null) {
				if (cpc.getPortletMap().get(Portlet.DATE_RANGE) != null) {
					dateRangeUtil.dateRangeChanged(cpc.getPortletMap().get(
							Portlet.DATE_RANGE));
					request.setYearOverYearIncome_StartDate(dateRangeUtil
							.getStartDate());
					request.setYearOverYearIncome_EndDate(dateRangeUtil
							.getEndDate());
					request.setYearOverYearIncome_dateRangeType(dateRangeUtil
							.getDateRangeType(cpc.getPortletMap().get(
									Portlet.DATE_RANGE)));
				}
				if (cpc.getPortletMap().get(Portlet.LIMIT) != null) {
					request.setYearOverYearIncome_AccountId(Long.parseLong(cpc
							.getPortletMap().get(Portlet.LIMIT)));
				}
			}
		}

	}

	/*
	 * private void addPortletToPage(ClientPortletConfiguration pc) { Portlet
	 * portlet = createPortlet(pc); if (portlet != null) { //
	 * portlet.setPortletPage(this); columns[pc.column].addPortlet(portlet);
	 * portlet.refreshWidget(); setPortletData(pc, portlet); final
	 * GraphPointsPortlet graphPointsPortlet = portlet .getGraphPointdPortlet();
	 * if (graphPointsPortlet != null) {
	 * graphPortletsPanel.add(graphPointsPortlet); portlet.addHandler(new
	 * ClickHandler() {
	 * 
	 * @Override public void onClick(ClickEvent event) {
	 * graphPortletsScrollPanel .ensureVisible(graphPointsPortlet); } },
	 * ClickEvent.getType()); }
	 * 
	 * } }
	 */
	private void setPortletData(ClientPortletConfiguration pc, Portlet portlet) {
		PortletData data = new PortletData();
		if (pc.getName().equals(PortletFactory.BANK_ACCOUNT)) {
			// data = portletsData.getBankAccountsPortletData();
		} else if (pc.getName()
				.equals(PortletFactory.ACCOUNTS_BALANCES_PORTLET)) {
			// data = portletsData.getAccountBalancesPortletData();
		} else if (pc.getName().equals(PortletFactory.EXPENSES_CLAIM)) {
			// data = portletsData.getExpensePortletData();
		} else if (pc.getName().equals(PortletFactory.INCOME_BREAKDOWN_PORTLET)) {
			// data = portletsData.getIncomePortletData();
		} else if (pc.getName().equals(
				PortletFactory.INCOME_EXPENSE_BREAKDOWN_PORTLET)) {
			data = portletsData.getIncomeExpensePortletData();
		} else if (pc.getName().equals(PortletFactory.MONEY_COMING)) {
			data = portletsData.getMoneyComingData();
		} else if (pc.getName().equals(PortletFactory.MONEY_GOING)) {
			data = portletsData.getMoneyGoingData();
		} else if (pc.getName().equals(PortletFactory.RECENT_TRANSACTIONS)) {
			// data = portletsData.getRecentTransactionsPortletData();
		} else if (pc.getName().equals(
				PortletFactory.TOP_CUSTOMERS_BY_SALES_PORTLET)) {
			// data = portletsData.getCustomersBySalesPortletData();
		} else if (pc.getName().equals(
				PortletFactory.TOP_ITEMS_BY_SALES_PORTLET)) {
			// data = portletsData.getItemsBySalesPortletData();
		} else if (pc.getName().equals(
				PortletFactory.TOP_VENDORS_BY_EXPENSES_PORTLET)) {
			// data = portletsData.getVendorsByExpensesPortletData();
		} else if (pc.getName().equals(PortletFactory.WHO_I_OWE)) {
			data = portletsData.getWhoIowePortletData();
		} else if (pc.getName().equals(PortletFactory.WHO_OWES_ME)) {
			data = portletsData.getWhoOwesMePortletData();
		} else if (pc.getName().equals(
				PortletFactory.YEAR_OVER_YEAR_EXPENSE_PORTLET)) {
			// data = portletsData.getYearOverYearExpensePortletData();
		} else if (pc.getName().equals(
				PortletFactory.YEAR_OVER_YEAR_INCOME_PORTLET)) {
			// data = portletsData.getYearOverYearIncomePortletData();
		}
		// portlet.setData(data);
	}

	// public PortletPageConfigureDialog createSettingsDialog() {
	// updateConfiguration();
	// PortletPageConfigureDialog configureDialog = new
	// PortletPageConfigureDialog(
	// Global.get().messages().configurePortlets(), config, this);
	// return configureDialog;
	// }

	// private Portlet createPortlet(ClientPortletConfiguration pc) {
	// return PortletFactory.get().createPortlet(pc, this);
	// }

	public void updatePortletPage() {
		if (config.getPageName() == null) {
			config.setPageName(name);
		}
		Accounter.createHomeService().savePortletPageConfig(config,
				new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean arg0) {
						Accounter.getCompany().setPortletConfiguration(config);
						if (arg0 && haveToRefresh) {
							clear();
							refreshPage();
						}
					}

					@Override
					public void onFailure(Throwable arg0) {
						System.err.println(arg0.toString());
					}
				});
	}

	public void updateConfiguration() {
		ArrayList<ClientPortletConfiguration> configs = new ArrayList<ClientPortletConfiguration>();
		for (PortletColumn column : columns) {
			for (Portlet portlet : column.getPortlets()) {
				configs.add(portlet.getConfiguration());
			}
		}
		config.setPortletsConfiguration(configs);
		config.setPageName(name);
	}

	public void refreshWidgets() {
		if (columns != null)
			for (PortletColumn column : this.columns) {
				column.refreshWidgets();
			}
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		haveToRefresh = false;
		updatePortletPage();
	}

	public DashboardPortletsData getPortletsData() {
		return portletsData;
	}

	public void setPortletsData(DashboardPortletsData portletsData) {
		this.portletsData = portletsData;
	}

}
