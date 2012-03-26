package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;
import java.util.Map;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationSummary;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByLocationsummaryServerReport;

public class SalesByLocationsummaryReport extends
		AbstractReportView<SalesByLocationSummary> {

	private boolean isLocation;
	private boolean isCustomer;

	public SalesByLocationsummaryReport(boolean isLocation, boolean isCustomer) {
		this.isLocation = isLocation;
		this.isCustomer = isCustomer;

		this.serverReport = new SalesByLocationsummaryServerReport(this,
				isLocation, isCustomer);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getSalesByLocationSummaryReport(
				isLocation, isCustomer, start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(SalesByLocationSummary record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record, ActionFactory
				.getSalesByLocationDetailsAction(isLocation, isCustomer));

	}

	@Override
	public void export(int generationType) {
		int reportType;
		if (isCustomer) {
			if (isLocation) {
				reportType = 152;
			} else {
				reportType = 160;
			}
		} else {
			if (isLocation) {
				reportType = 197;
			} else {
				reportType = 198;
			}
		}

		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), reportType);
	}

	@Override
	public void restoreView(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			isDatesArranged = false;
			return;
		}
		ClientFinanceDate startDate = (ClientFinanceDate) map.get("startDate");
		ClientFinanceDate endDate = (ClientFinanceDate) map.get("endDate");
		this.serverReport.setStartAndEndDates(startDate, endDate);
		toolbar.setEndDate(endDate);
		toolbar.setStartDate(startDate);
		toolbar.setDefaultDateRange((String) map.get("selectedDateRange"));
		isDatesArranged = true;
	}

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		String selectedDateRange = toolbar.getSelectedDateRange();
		ClientFinanceDate startDate = toolbar.getStartDate();
		ClientFinanceDate endDate = toolbar.getEndDate();
		map.put("selectedDateRange", selectedDateRange);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;
	}

}
