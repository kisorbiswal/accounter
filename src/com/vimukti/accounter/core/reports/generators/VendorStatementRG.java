package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.StatementServerReport;

public class VendorStatementRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_VENDORSTATEMENT;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		StatementServerReport statementReport1 = new StatementServerReport(
				true, this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(statementReport1);
		statementReport1.resetVariables();
		try {
			statementReport1.onResultSuccess(financeTool.getReportManager()
					.getPayeeStatementsList(true, getInputAsLong(0), 0,
							startDate, endDate, company.getID()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return statementReport1.getGridTemplate();
	}

}
