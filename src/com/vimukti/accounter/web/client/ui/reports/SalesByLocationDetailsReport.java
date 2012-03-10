package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;
import java.util.Map;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationDetails;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationSummary;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByLocationDetailsServerReport;

public class SalesByLocationDetailsReport extends
		AbstractReportView<SalesByLocationDetails> {

	private boolean isLocation;
	private boolean isCustomer;

	public SalesByLocationDetailsReport(boolean isLocation, boolean isCustomer) {
		this.serverReport = new SalesByLocationDetailsServerReport(this,
				isLocation);
		this.isLocation = isLocation;
		this.isCustomer = isCustomer;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		SalesByLocationSummary record = (SalesByLocationSummary) data;
		if (record == null) {
			Accounter.createReportService().getSalesByLocationDetailsReport(
					isLocation, isCustomer, start, end, this);
		} else {
			Accounter.createReportService()
					.getSalesByLocationDetailsForLocation(isLocation,
							record.getLocationName(), start, end, this);
		}
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(SalesByLocationDetails record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getType(),
				record.getTransactionid());
	}

	@Override
	public void print() {
		int reportType = 151;
		if (!isLocation) {
			reportType = 159;
		}
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())),
				reportType, "", "");
	}

	@Override
	public void exportToCsv() {
		int reportType = 151;
		if (!isLocation) {
			reportType = 159;
		}
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())),
				reportType, "", "");
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
