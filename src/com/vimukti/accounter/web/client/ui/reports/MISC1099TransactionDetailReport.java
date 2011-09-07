package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.reports.MISC1099TransactionDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.serverreports.MISC1099TransactionDetailServerReport;

public class MISC1099TransactionDetailReport extends

AbstractReportView<MISC1099TransactionDetail> {
	private long vendorId;
	private int boxNo;

	public MISC1099TransactionDetailReport(int boxNo) {
		this.serverReport = new MISC1099TransactionDetailServerReport();
		ClientVendor vendor = (ClientVendor) data;
		if (vendor != null)
			this.vendorId = vendor.getID();
		this.boxNo = boxNo;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getMISC1099TransactionDetailReport(
				vendorId, boxNo, start, end, this);
	}

	@Override
	public int getToolbarType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void OnRecordClick(MISC1099TransactionDetail record) {
		// TODO Auto-generated method stub
		// ReportsRPC.openTransactionView(record.getType(), record.gett)
	}

}
