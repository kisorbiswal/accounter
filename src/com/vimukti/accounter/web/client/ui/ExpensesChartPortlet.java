package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.reports.ExpensePortletToolBar;
import com.vimukti.accounter.web.client.ui.reports.PortletToolBar;

public class ExpensesChartPortlet extends GraphPointsPortlet {

	private PortletToolBar toolBar;
	private VerticalPanel graphPanel;

	public ExpensesChartPortlet(ClientPortletConfiguration configuration) {
		super(configuration, messages.expenses(), "");
		this.setConfiguration(configuration);
	}

	@Override
	public void createBody() {
		toolBarInitilization();
		graphPanel = new VerticalPanel();
		this.body.add(toolBar);
		this.body.add(graphPanel);

	}

	private void toolBarInitilization() {
		toolBar = new ExpensePortletToolBar() {
			@Override
			protected void initData() {
				if (ExpensesChartPortlet.this.getConfiguration()
						.getPortletKey() != null) {
					setDefaultDateRange(ExpensesChartPortlet.this
							.getConfiguration().getPortletKey());
				} else {
					setDefaultDateRange(messages.thisMonth());
				}
			}

			@Override
			protected void refreshPortletData(String selectItem) {
				ExpensesChartPortlet.this.clearGraph();
				dateRangeItemCombo.setSelected(selectItem);
				ExpensesChartPortlet.this.getConfiguration().setPortletKey(
						selectItem);
				dateRangeChanged(selectItem);
				ExpensesChartPortlet.this.updateData(startDate.getDate(),
						endDate.getDate());
			}

			@Override
			public void setDefaultDateRange(String defaultDateRange) {
				dateRangeItemCombo.setSelected(defaultDateRange);
				dateRangeChanged(defaultDateRange);
				ExpensesChartPortlet.this.updateData(startDate.getDate(),
						endDate.getDate());
			}
		};
	}

	protected void refreshPortletData(String selectItem) {

	}

	public void updateData(long startDate, long endDate) {

		AccounterAsyncCallback<Map<String, Double>> callback = new AccounterAsyncCallback<Map<String, Double>>() {

			@Override
			public void onResultSuccess(Map<String, Double> result) {
				if (result != null && (!result.isEmpty())) {
					final List<String> accountsNames = new ArrayList<String>();
					final List<Double> accountBalances = new ArrayList<Double>();
					Iterator<String> iterator = result.keySet().iterator();
					int i = 0;
					double amount = 0;
					while (iterator.hasNext()) {
						String accountName = iterator.next();
						Double accountBalance = result.get(accountName);
						if (i < 11) {
							accountsNames.add(accountName);
							accountBalances.add(accountBalance);
						} else {
							amount = amount + accountBalance;
							if (i == result.keySet().size() - 2) {
								accountsNames.add(messages.other());
								accountBalances.add(amount);
							}
						}
						i++;
					}
					Runnable runnable = new Runnable() {

						@Override
						public void run() {
							GraphChart chart = new GraphChart();
							chart.expenseAccountNames = accountsNames;
							graphPanel
									.add(chart
											.createAccountExpenseChart(accountBalances));
							completeInitialization();
						}
					};
					VisualizationUtils.loadVisualizationApi(runnable,
							AnnotatedTimeLine.PACKAGE);
				} else {
					Label label = new Label(messages.noRecordsToShow());
					graphPanel.add(label);
					label.addStyleName("no_records_label");
					graphPanel.setCellHorizontalAlignment(label,
							HasAlignment.ALIGN_CENTER);
				}
			}

			@Override
			public void onException(AccounterException exception) {
				completeInitialization();
			}
		};

		Accounter.createHomeService().getAccountsAndValues(startDate, endDate,
				callback);
	}

	public void clearGraph() {
		graphPanel.clear();
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}

}
