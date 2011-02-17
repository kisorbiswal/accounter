package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.ui.FinanceApplication;

/**
 * GL Report is an TransactionDetailByAccountReport only
 * 
 * @author kumar kasimala
 * 
 */
public class GeneralLedgerReport extends TransactionDetailByAccountReport {

	public GeneralLedgerReport() {
	}

	@Override
	public String getTitle() {
		return FinanceApplication.getReportsMessages().generalLedgerReport();
	}
}
