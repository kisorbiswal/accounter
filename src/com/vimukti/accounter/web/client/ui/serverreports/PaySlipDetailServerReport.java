package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.PaySlipDetail;
import com.vimukti.accounter.web.client.ui.reports.PaySlipDetailReport;

public class PaySlipDetailServerReport extends
		AbstractFinaneReport<PaySlipDetail> {

	public PaySlipDetailServerReport(PaySlipDetailReport paySlipDetailReport) {
		this.reportView = paySlipDetailReport;
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().name(), getMessages().number(),
				getMessages().accountNumber(), getMessages().bankName(),
				getMessages().branchOrdivison(), getMessages().amount(),
				getMessages().email() };
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getColunms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processRecord(PaySlipDetail record) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getColumnData(PaySlipDetail record, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(PaySlipDetail obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientFinanceDate getEndDate(PaySlipDetail obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

}
