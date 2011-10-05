package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.GeneralLedgerServerReport;

/**
 * GL Report is an TransactionDetailByAccountReport only
 * 
 * @author kumar kasimala
 * 
 */
public class GeneralLedgerReport extends TransactionDetailByAccountReport {

	public GeneralLedgerReport() {
		setReportType(REPORT_TYPE_GENERAL_LEDGER_REPORT);
		this.serverReport = new GeneralLedgerServerReport(
				new ClientFinanceDate().getDate(),
				new ClientFinanceDate().getDate(), 0, this);
	}

}
