package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;

public abstract class TransactionsListView<T> extends BaseListView<T> {
	protected ClientFinanceDate startDate;
	protected ClientFinanceDate endDate;
	public static final int VIEW_ALL = 0;
	public static final int VIEW_OPEN = 1;
	public static final int VIEW_OVERDUE = 2;
	public static final int VIEW_VOIDED = 3;
	public static final int VIEW_DRAFT = 4;
	public static final int TYPE_ALL = 1000;

	private String viewType;

	public TransactionsListView() {
		super();
		this.startDate = Accounter.getStartDate();
		this.endDate = getCompany().getCurrentFiscalYearEndDate();
	}

	public TransactionsListView(String viewType) {
		super();
		this.startDate = Accounter.getStartDate();
		this.endDate = getCompany().getCurrentFiscalYearEndDate();
		this.setViewType(viewType);
		this.setDateRange(messages.all());
	}

	@Override
	protected boolean isTransactionListView() {
		return true;
	}

	@Override
	protected SelectCombo getSelectItem() {
		viewSelect = new SelectCombo(messages.currentView());
		viewSelect.setHelpInformation(true);
		// listOfTypes.add(DRAFT);
		viewSelect.initCombo(getViewSelectTypes());

		// if (UIUtils.isMSIEBrowser())
		// viewSelect.setWidth("105px");

		viewSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (viewSelect.getSelectedValue() != null) {
							grid.setViewType(viewSelect.getSelectedValue());
							setViewType(selectItem);
							filterList(viewSelect.getSelectedValue());
						}
					}
				});
		viewSelect.addStyleName("invoiceListCombo");
		viewSelect.setComboItem(getViewType());
		return viewSelect;
	}

	@Override
	protected SelectCombo getDateRangeSelectItem() {
		dateRangeSelector = new SelectCombo(messages.date());
		String[] dateRangeArray = { messages.all(), messages.thisWeek(),
				messages.thisMonth(), messages.lastWeek(),
				messages.lastMonth(), messages.thisFinancialYear(),
				messages.lastFinancialYear(), messages.thisFinancialQuarter(),
				messages.lastFinancialQuarter(), messages.financialYearToDate() };
		final List<String> dateRanges = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRanges.add(dateRangeArray[i]);
		}
		dateRangeSelector.initCombo(dateRanges);

		dateRangeSelector.setComboItem(messages.all());
		setDateRange(messages.all());
		// if (UIUtils.isMSIEBrowser())
		// dateRangeSelector.setWidth("105px");

		dateRangeSelector
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem != null
								&& !selectItem.equals(messages.custom())) {
							dateRangeChanged(dateRangeSelector
									.getSelectedValue());
							setDateRange(dateRangeSelector.getSelectedValue());
							grid.setViewType(dateRangeSelector
									.getSelectedValue());
							dateRangeSelector.removeComboItem(messages.custom());
							updateButton.setEnabled(false);
						}
					}
				});
		dateRangeSelector.addStyleName("invoiceListCombo");

		return dateRangeSelector;
	}

	public void dateRangeChanged(String dateRange) {
		ClientFinanceDate date = new ClientFinanceDate();
		startDate = Accounter.getStartDate();
		endDate = getCompany().getCurrentFiscalYearEndDate();
		// getLastandOpenedFiscalYearEndDate();
		if (dateRange.equals(messages.thisWeek())) {
			startDate = getWeekStartDate();
			endDate.setDay(startDate.getDay() + 6);
			endDate.setMonth(startDate.getMonth());
			endDate.setYear(startDate.getYear());
		}
		if (dateRange.equals(messages.thisMonth())) {
			startDate = new ClientFinanceDate(date.getYear(), date.getMonth(),
					1);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(new ClientFinanceDate().getDateAsObject());
			endCal.set(Calendar.DAY_OF_MONTH,
					endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = new ClientFinanceDate(endCal.getTime());

		}
		if (dateRange.equals(messages.lastWeek())) {
			endDate = getWeekStartDate();
			endDate.setDay(endDate.getDay() - 1);
			startDate = new ClientFinanceDate(endDate.getDate());
			startDate.setDay(startDate.getDay() - 6);

		}
		if (dateRange.equals(messages.lastMonth())) {
			int day;
			if (date.getMonth() == 0) {
				day = getMonthLastDate(11, date.getYear() - 1);
				startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
			} else {
				day = getMonthLastDate(date.getMonth() - 1, date.getYear());
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth() - 1, 1);
				endDate = new ClientFinanceDate(date.getYear(),
						date.getMonth() - 1, day);
			}
		}
		if (dateRange.equals(messages.thisFinancialYear())) {
			startDate = getCompany().getCurrentFiscalYearStartDate();
			endDate = getCompany().getCurrentFiscalYearEndDate();
		}
		if (dateRange.equals(messages.lastFinancialYear())) {

			startDate = Accounter.getCompany().getCurrentFiscalYearStartDate();
			startDate.setYear(startDate.getYear() - 1);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(Accounter.getCompany().getCurrentFiscalYearEndDate()
					.getDateAsObject());
			endCal.set(Calendar.DAY_OF_MONTH,
					endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			endDate = new ClientFinanceDate(endCal.getTime());
			endDate.setYear(endDate.getYear() - 1);

		}
		if (dateRange.equals(messages.thisFinancialQuarter())) {
			startDate = new ClientFinanceDate();
			endDate = getCompany().getCurrentFiscalYearEndDate();
			// getLastandOpenedFiscalYearEndDate();
			getCurrentQuarter();
		}
		if (dateRange.equals(messages.lastFinancialQuarter())) {
			startDate = new ClientFinanceDate();
			endDate = getCompany().getCurrentFiscalYearEndDate();
			// getLastandOpenedFiscalYearEndDate();
			getCurrentQuarter();
			startDate.setYear(startDate.getYear() - 1);
			endDate.setYear(endDate.getYear() - 1);
		}
		if (dateRange.equals(messages.financialYearToDate())) {
			startDate = getCompany().getCurrentFiscalYearStartDate();
			endDate = new ClientFinanceDate();
		}
		setStartDate(startDate);
		setEndDate(endDate);
		changeDates(startDate, endDate);
	}

	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		initListCallback();
	}

	public ClientFinanceDate getStartDate() {
		return startDate == null ? ClientFinanceDate.emptyDate() : startDate;
	}

	public void setStartDate(ClientFinanceDate startDate) {
		this.startDate = startDate;
	}

	public ClientFinanceDate getEndDate() {
		return UIUtils.toDate(endDate);
	}

	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
	}

	public ClientFinanceDate getWeekStartDate() {
		ClientFinanceDate date = new ClientFinanceDate();
		int day = date.getDay() % 6;
		ClientFinanceDate newDate = new ClientFinanceDate();
		if (day != 1) {
			newDate.setDay(date.getDay() - day);
		} else {
			newDate.setDay(date.getDay());
		}
		return newDate;
	}

	// public native double getWeekStartDate()/*-{
	// var date= new Date();
	// var day=date.getDay();
	// var newDate=new Date();
	// newDate.setDate(date.getDate()-day);
	// var tmp=newDate.getTime();
	// return tmp;
	// }-*/;

	public int getMonthLastDate(int month, int year) {
		int lastDay;
		switch (month) {
		case 0:
		case 2:
		case 4:
		case 6:
		case 7:
		case 9:
		case 11:
			lastDay = 31;
			break;
		case 1:
			if (year % 4 == 0 && year % 100 == 0)
				lastDay = 29;
			else
				lastDay = 28;
			break;

		default:
			lastDay = 30;
			break;
		}
		return lastDay;
	}

	public void getCurrentQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear(), 0, 1);
			endDate = new ClientFinanceDate(date.getYear(), 2, 31);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear(), 9, 1);
			endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			break;
		}
	}

	public void getPreviousQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
			endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 0, 1);
			endDate = new ClientFinanceDate(date.getYear(), 2, 31);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;
		}
	}

	public void filterList(String selectedValue) {

	}

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currentView", viewSelect.getValue().toString());
		map.put("dateRange", dateRangeSelector.getValue().toString());
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return;
		}
		String currentView = (String) map.get("currentView");
		viewSelect.setComboItem(currentView);
		this.setViewType(currentView);
		String dateRange1 = (String) map.get("dateRange");
		dateRangeSelector.setComboItem(dateRange1);
		dateRangeChanged(dateRange1);
		ClientFinanceDate startDate1 = (ClientFinanceDate) map.get("startDate");
		setStartDate(startDate1);
		ClientFinanceDate endDate1 = (ClientFinanceDate) map.get("endDate");
		setEndDate(endDate1);
		start = (Integer) map.get("start");
	}

	@Override
	public void customManage() {
		setStartDate(fromItem.getDate());
		setEndDate(toItem.getDate());
		changeDates(startDate, endDate);
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	@Override
	public String getDateRange() {
		return super.getDateRange();
	}

	@Override
	public void setDateRange(String selectedValue) {
		super.setDateRange(selectedValue);
	}

	@Override
	public void restoreView(String currentView, String dateRange) {
		if (currentView != null) {
			viewSelect.setComboItem(currentView);
			setViewType(currentView);
		}
		if (dateRange != null) {
			dateRangeSelector.setComboItem(dateRange);
			setDateRange(dateRange);
		}
		filterList(currentView);
	}
}
