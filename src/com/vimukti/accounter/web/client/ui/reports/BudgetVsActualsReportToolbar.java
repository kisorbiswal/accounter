package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class BudgetVsActualsReportToolbar extends ReportToolbar {

	protected SelectCombo monthSelectCombo, quaterlySelectCombo, budgetName,
			dateRangeSelect;
	protected List<String> statusList, dateRangeList, yearRangeList;
	List<String> budgetArray = new ArrayList<String>();
	List<Long> idArray = new ArrayList<Long>();
	long budgetId = 999L;

	public BudgetVsActualsReportToolbar() {
		createData();
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {

		reportview.makeReportRequest(budgetId, startDate, endDate);

	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		setStartDate(startDate);
		setEndDate(endDate);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeChanged(defaultDateRange);
	}

	public void createControls() {

		budgetName = new SelectCombo(Accounter.messages().budget());
		budgetName.setHelpInformation(true);
		statusList = new ArrayList<String>();
		for (String str : budgetArray) {
			statusList.add(str);
		}
		if (budgetArray.size() < 1) {
			statusList.add("");
		}

		budgetName.initCombo(statusList);
		budgetName.setSelected(statusList.get(0));
		budgetName
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						budgetId = idArray.get(budgetName.getSelectedIndex());
						changeDates(startDate, endDate);
					}
				});

		dateRangeSelect = new SelectCombo(Accounter.messages().dateRange());
		dateRangeSelect.setHelpInformation(true);
		dateRangeSelect.initCombo(getDateRangeList());
		dateRangeSelect.setSelectedItem(2);
		dateRangeSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem.equals(messages.accountsVsMonths())) {
							monthSelectCombo.show();
							quaterlySelectCombo.hide();
						} else if (selectItem.equals(messages
								.accountsVsQuaters())) {
							quaterlySelectCombo.show();
							monthSelectCombo.hide();
						}
					}
				});

		monthSelectCombo = new SelectCombo(Accounter.messages().month());
		monthSelectCombo.setHelpInformation(true);
		monthSelectCombo.initCombo(getMonthsList());
		monthSelectCombo.setSelectedItem(0);
		monthSelectCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		quaterlySelectCombo = new SelectCombo(Accounter.messages().quarterly());
		quaterlySelectCombo.setHelpInformation(true);
		quaterlySelectCombo.initCombo(getQuatersList());
		quaterlySelectCombo.setSelectedItem(0);
		quaterlySelectCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
					}
				});

		addItems(budgetName, dateRangeSelect, monthSelectCombo,
				quaterlySelectCombo);
		monthSelectCombo.hide();
		quaterlySelectCombo.hide();

	}

	private List<String> getDateRangeList() {
		ArrayList<String> datesList = new ArrayList<String>();

		datesList.add(messages.accountsVsMonths());
		datesList.add(messages.accountsVsQuaters());
		datesList.add(messages.accountsVsTotal());

		return datesList;
	}

	private List<String> getQuatersList() {

		ArrayList<String> datesList = new ArrayList<String>();

		datesList.add("Q1" + " " + DayAndMonthUtil.jan() + " - "
				+ DayAndMonthUtil.mar());
		datesList.add("Q2" + " " + DayAndMonthUtil.apr() + " - "
				+ DayAndMonthUtil.jun());
		datesList.add("Q3" + " " + DayAndMonthUtil.jul() + " - "
				+ DayAndMonthUtil.sep());
		datesList.add("Q4" + " " + DayAndMonthUtil.oct() + " - "
				+ DayAndMonthUtil.dec());

		return datesList;
	}

	private List<String> getMonthsList() {
		ArrayList<String> datesList = new ArrayList<String>();

		datesList.add(DayAndMonthUtil.january());
		datesList.add(DayAndMonthUtil.february());
		datesList.add(DayAndMonthUtil.march());
		datesList.add(DayAndMonthUtil.april());
		datesList.add(DayAndMonthUtil.may_full());
		datesList.add(DayAndMonthUtil.june());
		datesList.add(DayAndMonthUtil.july());
		datesList.add(DayAndMonthUtil.august());
		datesList.add(DayAndMonthUtil.september());
		datesList.add(DayAndMonthUtil.october());
		datesList.add(DayAndMonthUtil.november());
		datesList.add(DayAndMonthUtil.december());

		return datesList;
	}

	public void createData() {

		Accounter.createHomeService().getBudgetList(
				new AsyncCallback<PaginationList<ClientBudget>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(
							PaginationList<ClientBudget> budgetList) {
						if (budgetList == null) {
							budgetList = new PaginationList<ClientBudget>();
						}
						for (ClientBudget budget : budgetList) {
							budgetArray.add(budget.getBudgetName());
							idArray.add(budget.getID());
						}
						createControls();

						if (idArray.size() > 0) {
							budgetId = idArray.get(0);
						} else {
							budgetId = 999L;
						}
						changeDates(new ClientFinanceDate(),
								new ClientFinanceDate());

					}
				});

	}
}
