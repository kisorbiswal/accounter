package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.PaySheetServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class PaySheetRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_PAYSHEET;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		PaySheetServerReport byCatgoryServerReport = new PaySheetServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(byCatgoryServerReport, financeTool);
		byCatgoryServerReport.resetVariables();

		byCatgoryServerReport.onResultSuccess(financeTool.getPayrollManager()
				.getPaySheet(startDate, endDate, company.getId()));

		return byCatgoryServerReport.getGridTemplate();
	}

}
