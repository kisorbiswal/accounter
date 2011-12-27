package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;

public abstract class TopPayeesBySalesPortletToolBar extends PortletToolBar {
	protected SelectCombo dateRangeItemCombo;
	private List<String> dateRangesList;
	protected SelectCombo limitCombo;
	protected String dateRange;
	protected String limit;

	public TopPayeesBySalesPortletToolBar() {
		createControls();
	}

	private void createControls() {
		dateRangesList = new ArrayList<String>();
		String[] dateRangeArray = { messages.all(), messages.thisWeek(),
				messages.thisMonth(), messages.lastWeek(),
				messages.lastMonth(), messages.thisFinancialYear(),
				messages.lastFinancialYear(), messages.thisFinancialQuarter(),
				messages.lastFinancialQuarter(), messages.financialYearToDate() };
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangesList.add(dateRangeArray[i]);
		}
		dateRangeItemCombo = new SelectCombo(messages.dateRange());
		dateRangeItemCombo.initCombo(dateRangesList);
		setDefaultDateRange(getSelectedItem().get(0));
		dateRangeItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						dateRange = selectItem;
						if (limit == null) {
							limit = "5";
						}
						refreshPortletData(dateRange, limit);
					}
				});
		limitCombo = new SelectCombo(messages.limit());
		List<String> limitList = new ArrayList<String>();
		for (int i = 0; i < 6; i++) {
			limitList.add(String.valueOf((i + 1) * 5));
		}
		limitCombo.initCombo(limitList);
		limitCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						limit = selectItem;
						refreshPortletData(dateRange, limit);
					}
				});
		limitCombo.setSelected(getSelectedItem().get(1));
		addItems(dateRangeItemCombo, limitCombo);
	}

	protected abstract void refreshPortletData(String selectItem, String limit);

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {

	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {

	}

	/**
	 * get default date range for date range combo
	 */
	protected abstract List<String> getSelectedItem();
}
