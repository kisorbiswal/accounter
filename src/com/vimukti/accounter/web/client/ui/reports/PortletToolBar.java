package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.ExpensesChartPortlet;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;

public class PortletToolBar extends ReportToolbar {
	private SelectCombo dateRangeItemCombo;
	private List<String> dateRangesList;
	private ExpensesChartPortlet portlet;

	public PortletToolBar(ExpensesChartPortlet portlet) {
		this.portlet = portlet;
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
		if (portlet.getConfiguration().getPortletKey() != null) {
			setDefaultDateRange(portlet.getConfiguration().getPortletKey());
		} else {
			setDefaultDateRange(messages.thisMonth());
		}
		dateRangeItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						refreshPortletData(selectItem);
					}
				});
		addItems(dateRangeItemCombo);
	}

	protected void refreshPortletData(String selectItem) {
		portlet.clearGraph();
		dateRangeItemCombo.setSelected(selectItem);
		portlet.getConfiguration().setPortletKey(selectItem);
		dateRangeChanged(selectItem);
		portlet.updateData(startDate.getDate(), endDate.getDate());
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
		dateRangeItemCombo.setSelected(defaultDateRange);
		dateRangeChanged(defaultDateRange);
		portlet.updateData(startDate.getDate(), endDate.getDate());
	}

}
