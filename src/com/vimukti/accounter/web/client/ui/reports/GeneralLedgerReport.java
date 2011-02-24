package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.ui.serverreports.GeneralLedgerServerReport;

/**
 * GL Report is an TransactionDetailByAccountReport only
 * 
 * @author kumar kasimala
 * 
 */
public class GeneralLedgerReport extends TransactionDetailByAccountReport {

	public GeneralLedgerReport() {
		this.serverReport = new GeneralLedgerServerReport(this.startDate
				.getTime(), this.endDate.getTime(), 0);
	}

}
