package com.vimukti.accounter.web.client.portlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.AccountDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ExpensePortletData;
import com.vimukti.accounter.web.client.ui.GraphChart;
import com.vimukti.accounter.web.client.ui.GraphPointsPortlet;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.reports.DateRangePortletToolBar;
import com.vimukti.accounter.web.client.ui.reports.PortletToolBar;

public class IncomeBreakdownPortlet extends GraphPointsPortlet {

	private PortletToolBar toolBar;
	private VerticalPanel graphPanel;

	public IncomeBreakdownPortlet(ClientPortletConfiguration pc) {
		super(pc, messages.incomes(), "", "100%");
		this.setConfiguration(pc);
	}

	@Override
	public void helpClicked() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void goToClicked() {
		ActionFactory.getExpensesAction(null).run(null, true);
	}

	@Override
	public void createBody() {
		VerticalPanel vPanel = new VerticalPanel();
		graphPanel = new VerticalPanel();
		initToolBar();
		this.body.add(toolBar);
		this.body.add(graphPanel);
		this.body.add(vPanel);
	}

	private void initToolBar() {
		toolBar = new DateRangePortletToolBar() {
			@Override
			protected String getSelectedItem() {
				if (IncomeBreakdownPortlet.this.getConfiguration()
						.getPortletMap().get(DATE_RANGE) != null) {
					return IncomeBreakdownPortlet.this.getConfiguration()
							.getPortletMap().get(DATE_RANGE);
				} else {
					return messages.financialYearToDate();
				}
			}

			@Override
			protected void refreshPortletData(String selectItem) {
				IncomeBreakdownPortlet.this.clearGraph();
				dateRangeItemCombo.setSelected(selectItem);
				Map<String, String> portletMap = new HashMap<String, String>();
				portletMap.put(DATE_RANGE, selectItem);
				IncomeBreakdownPortlet.this.getConfiguration().setPortletMap(
						portletMap);
				dateRangeChanged(selectItem);
				IncomeBreakdownPortlet.this.updateData(toolBar.getStartDate(),
						toolBar.getEndDate());
			}

			@Override
			public void setDefaultDateRange(String defaultDateRange) {
				dateRangeItemCombo.setSelected(defaultDateRange);
				dateRangeChanged(defaultDateRange);
				IncomeBreakdownPortlet.this.updateData(getStartDate(),
						getEndDate());
			}
		};
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}

	public void updateData(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {

		AccounterAsyncCallback<ExpensePortletData> callback = new AccounterAsyncCallback<ExpensePortletData>() {

			@Override
			public void onResultSuccess(ExpensePortletData result) {
				if (result != null && (!result.getAccountDetails().isEmpty())) {
					updateGraphData(result);
				} else {
					Label label = new Label(messages.noRecordsToShow());
					graphPanel.add(label);
					label.addStyleName("no_records_label");
					graphPanel.setCellHorizontalAlignment(label,
							HasAlignment.ALIGN_CENTER);
				}
				completeInitialization();
			}

			@Override
			public void onException(AccounterException exception) {
				completeInitialization();
			}
		};

		Accounter.createHomeService().getIncomeBreakdownPortletData(startDate,
				endDate, callback);
	}

	protected void updateGraphData(ExpensePortletData result) {
		final List<String> accountsNames = new ArrayList<String>();
		final List<Double> accountBalances = new ArrayList<Double>();
		Iterator<AccountDetail> iterator = result.getAccountDetails()
				.iterator();
		int i = 0;
		double amount = 0;
		while (iterator.hasNext()) {
			AccountDetail accountDetail = iterator.next();
			Double accountBalance = accountDetail.getAmount();
			if (i < 11) {
				accountsNames.add(accountDetail.getName());
				accountBalances.add(accountBalance);
			} else {
				amount = amount + accountBalance;
				if (i == result.getAccountDetails().size() - 2) {
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
				chart.accountNames = accountsNames;
				graphPanel
						.add(chart.createAccountExpenseChart(accountBalances));
			}
		};
		VisualizationUtils.loadVisualizationApi(runnable,
				AnnotatedTimeLine.PACKAGE);
	}

	public void clearGraph() {
		graphPanel.clear();
	}

	@Override
	public void refreshClicked() {
		// TODO Auto-generated method stub

	}

}