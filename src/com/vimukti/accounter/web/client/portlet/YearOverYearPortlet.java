package com.vimukti.accounter.web.client.portlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.GraphChart;
import com.vimukti.accounter.web.client.ui.GraphPointsPortlet;
import com.vimukti.accounter.web.client.ui.YearOverYearPortletData;

public class YearOverYearPortlet extends GraphPointsPortlet {

	public static final int YEAR_OVER_YEAR_INCOME = 1;
	public static final int YEAR_OVER_YEAR_EXPENSE = 2;
	private YearOverYearToolBar toolBar;
	private VerticalPanel graphPanel;
	private int chartType;

	public YearOverYearPortlet(ClientPortletConfiguration pc, int chartType) {
		super(pc, "", "", "100%");
		this.chartType = chartType;
		setPortletTitle();
	}

	private void setPortletTitle() {
		if (chartType == YEAR_OVER_YEAR_INCOME) {
			setPortletTitle(messages.yearOverYearIncome());
		} else {
			setPortletTitle(messages.yearOverYearExpense());
		}
	}

	@Override
	public void createBody() {
		graphPanel = new VerticalPanel();
		createToolBar();
		this.body.add(graphPanel);
	}

	private void createToolBar() {
		toolBar = new YearOverYearToolBar(chartType) {

			@Override
			protected void refreshPortletData(String selectItem, long accountId) {
				YearOverYearPortlet.this.clearGrid();
				dateRangeItemCombo.setSelected(selectItem);
				if (accountId != 0) {
					accountCombo.setSelected(Accounter.getCompany()
							.getAccount(accountId).getName());
				}
				Map<String, String> portletMap = new HashMap<String, String>();
				portletMap.put(DATE_RANGE, selectItem);
				portletMap.put(ACCOUNT_ID, String.valueOf(accountId));
				YearOverYearPortlet.this.getConfiguration().setPortletMap(
						portletMap);
				dateRangeChanged(selectItem);
				YearOverYearPortlet.this.updateData(
						getDateRangeType(selectItem), toolBar.getStartDate(),
						toolBar.getEndDate(), accountId);
			}

			@Override
			protected List<String> getSelectedItem() {
				List<String> list = new ArrayList<String>();
				if (YearOverYearPortlet.this.getConfiguration().getPortletMap()
						.get(DATE_RANGE) != null) {
					list.add(YearOverYearPortlet.this.getConfiguration()
							.getPortletMap().get(DATE_RANGE));
				} else {
					list.add(messages.thisMonth());
				}
				if (YearOverYearPortlet.this.getConfiguration().getPortletMap()
						.get(ACCOUNT_ID) != null) {
					accountId = Long
							.parseLong(YearOverYearPortlet.this
									.getConfiguration().getPortletMap()
									.get(ACCOUNT_ID));
					list.add(String.valueOf(accountId));
				}
				return list;
			}

			@Override
			public void setDefaultDateRange(String defaultDateRange) {
				dateRangeItemCombo.setSelected(defaultDateRange);
				dateRangeChanged(defaultDateRange);
				YearOverYearPortlet.this.updateData(
						getDateRangeType(defaultDateRange), getStartDate(),
						getEndDate(), accountId);
			}
		};
		this.body.add(toolBar);
	}

	protected void updateData(int dateRangeType, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long accountId) {
		AsyncCallback<ArrayList<YearOverYearPortletData>> callback = new AsyncCallback<ArrayList<YearOverYearPortletData>>() {

			@Override
			public void onSuccess(ArrayList<YearOverYearPortletData> result) {
				if (result != null && (!result.isEmpty()))
					updateGraphData(result);
			}

			@Override
			public void onFailure(Throwable caught) {

			}
		};
		if (accountId != 0) {
			Accounter.createHomeService().getAccountsBalancesByDate(startDate,
					endDate, accountId, dateRangeType, callback);
		} else {
			Label label = new Label(messages.noRecordsToShow());
			graphPanel.add(label);
			graphPanel.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		}
	}

	protected void updateGraphData(ArrayList<YearOverYearPortletData> result) {
		final List<Double> balances = new ArrayList<Double>();
		final List<String> monthNames = new ArrayList<String>();
		Iterator<YearOverYearPortletData> iterator = result.iterator();
		while (iterator.hasNext()) {
			YearOverYearPortletData accountDetail = iterator.next();
			balances.add(accountDetail.getAmount());
			monthNames.add(String.valueOf(accountDetail.getMonth()));
		}
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				GraphChart chart = new GraphChart();
				chart.setTitle(toolBar.accountCombo.getSelectedValue()
						.getName());
				chart.accountNames = monthNames;
				graphPanel.add(chart.createYearOverYearChart(balances));
				completeInitialization();
			}

		};
		VisualizationUtils.loadVisualizationApi(runnable,
				AnnotatedTimeLine.PACKAGE);
	}

	protected void clearGrid() {
		graphPanel.clear();
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}
}
