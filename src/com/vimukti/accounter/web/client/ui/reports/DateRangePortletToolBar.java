package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;

public abstract class DateRangePortletToolBar extends PortletToolBar {
	protected SelectCombo dateRangeItemCombo;
	protected List<String> dateRangesList;

	public DateRangePortletToolBar() {
		init();
	}

	protected void init() {
		createControls();
	}

	private void createControls() {
		dateRangesList = new ArrayList<String>();
		for (int i = 0; i < allDateRangeArray.length; i++) {
			dateRangesList.add(allDateRangeArray[i]);
		}
		dateRangeItemCombo = new SelectCombo(messages.dateRange());
		dateRangeItemCombo.initCombo(dateRangesList);
		setDefaultDateRange(getSelectedItem());
		dateRangeItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						refreshPortletData(selectItem);
					}
				});
		addItems(dateRangeItemCombo);
	}

	protected abstract void refreshPortletData(String selectItem);

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
	protected abstract String getSelectedItem();
}
