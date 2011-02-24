package com.vimukti.accounter.web.client.ui.serverreports;

/**
 * GL Report is an TransactionDetailByAccountReport only
 * 
 * @author kumar kasimala
 * 
 */
public class GeneralLedgerServerReport extends TransactionDetailByAccountServerReport {

	public GeneralLedgerServerReport(long startDate,long endDate,int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String getTitle() {
		return "General Ledger Report";
	}
}
