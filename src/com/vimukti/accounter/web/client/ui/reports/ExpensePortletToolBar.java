package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;

public class ExpensePortletToolBar extends PortletToolBar {
	protected SelectCombo dateRangeItemCombo;
	private List<String> dateRangesList;

	public ExpensePortletToolBar() {
		createControls();
	}

	private void createControls() {
		dateRangesList = new ArrayList<String>();
		String[] dateRangeArray = { messages.all(), messages.thisWeek(),
				messages.thisMonth(), messages.lastWeek(),
				messages.lastMonth(), messages.thisFinancialYear(),
				messages.lastFinancialYear(), messages.thisFinancialQuarter(),
				messages.lastFinancialQuarter(),
				messages.financialYearToDate(), messages.custom() };
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangesList.add(dateRangeArray[i]);
		}
		dateRangeItemCombo = new SelectCombo(messages.dateRange());
		dateRangeItemCombo.initCombo(dateRangesList);
		initData();
		dateRangeItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						refreshPortletData(selectItem);
					}
				});
		addItems(dateRangeItemCombo);
	}

	protected void initData() {
		// TODO Auto-generated method stub

	}

	protected void refreshPortletData(String selectItem) {

	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {

	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {

	}

	// @SuppressWarnings("unchecked")
	@Override
	public void setDefaultDateRange(String defaultDateRange) {

	}
}
