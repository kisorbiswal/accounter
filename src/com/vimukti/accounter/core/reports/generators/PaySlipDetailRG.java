package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.PaySlipDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class PaySlipDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_PAYSLIP_DETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		PaySlipDetailServerReport byCatgoryServerReport = new PaySlipDetailServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(byCatgoryServerReport, financeTool);
		byCatgoryServerReport.resetVariables();

		long employeeId = getInputAsLong(0);

		byCatgoryServerReport.onResultSuccess(financeTool.getPayrollManager()
				.getPaySlipDetail(employeeId, startDate, endDate,
						company.getId()));

		return byCatgoryServerReport.getGridTemplate();
	}

}
