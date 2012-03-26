package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.TransactionDetailByAccountServerReport;

public class TranxDetailByAccountOrGeneralLedgerRG extends
		AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_TRANSACTIONDETAILBYACCOUNT;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		TransactionDetailByAccountServerReport transactionDetailByAccountServerReport = new TransactionDetailByAccountServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(transactionDetailByAccountServerReport, financeTool);
		transactionDetailByAccountServerReport.resetVariables();
		try {
			if (getInputAsLong(0) == 0) {
				transactionDetailByAccountServerReport
						.onResultSuccess(reportsSerivce
								.getTransactionDetailByAccount(
										startDate.toClientFinanceDate(),
										endDate.toClientFinanceDate(),
										company.getID()));
			} else {
				transactionDetailByAccountServerReport
						.onResultSuccess(reportsSerivce
								.getTransactionDetailByAccount(
										getInputAsString(2),
										startDate.toClientFinanceDate(),
										endDate.toClientFinanceDate(),
										company.getID()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionDetailByAccountServerReport.getGridTemplate();
	}

}
