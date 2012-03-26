package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.StatementServerReport;

public class CustomerStatementRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_CUSTOMERSTATEMENT;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		StatementServerReport statementReport = new StatementServerReport(
				false, this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(statementReport, financeTool);
		statementReport.resetVariables();
		try {
			statementReport.onResultSuccess(financeTool.getReportManager()
					.getPayeeStatementsList(false, getInputAsLong(0), 0,
							startDate, endDate, company.getID()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return statementReport.getGridTemplate();
	}

}
