package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.ProfitAndLossByLocation;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.ProfitAndLossByLocationServerReport;

public class ProfitAndLossByLocationReport extends
		AbstractReportView<ProfitAndLossByLocation> {

	private boolean isLocation;

	public ProfitAndLossByLocationReport(boolean isLocation) {
		this.isLocation = isLocation;
		ProfitAndLossByLocationServerReport.locations = Accounter.getCompany()
				.getLocations();
		ProfitAndLossByLocationServerReport.classes = Accounter.getCompany()
				.getAccounterClasses();
		int numcolumns = 0;
		if (isLocation) {
			numcolumns = ProfitAndLossByLocationServerReport.locations.size() + 2;
		} else {
			numcolumns = ProfitAndLossByLocationServerReport.classes.size() + 2;
		}
		ProfitAndLossByLocationServerReport.noColumns = numcolumns;
		this.serverReport = new ProfitAndLossByLocationServerReport(this,
				isLocation);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getProfitAndLossByLocationReport(
				isLocation, start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(ProfitAndLossByLocation record) {
		// record.setStartDate(toolbar.getStartDate());
		// record.setEndDate(toolbar.getEndDate());
		// record.setDateRange(toolbar.getSelectedDateRange());
		// UIUtils.runAction(record,
		// ActionFactory.getTransactionDetailByAccountAction());

	}

	@Override
	public void print() {
		int reportType = 153;
		if (isLocation) {
			reportType = 161;
		}
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())),
				reportType, "", "");
	}
}
