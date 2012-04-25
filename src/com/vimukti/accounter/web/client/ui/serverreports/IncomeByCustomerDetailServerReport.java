package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.IncomeByCustomerDetail;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class IncomeByCustomerDetailServerReport extends
		AbstractFinaneReport<IncomeByCustomerDetail> {

	public IncomeByCustomerDetailServerReport(
			IFinanceReport<IncomeByCustomerDetail> reportView) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getDynamicHeaders() {
		// TODO Auto-generated method stub
		return null;
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
	public void processRecord(IncomeByCustomerDetail record) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getColumnData(IncomeByCustomerDetail record, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(IncomeByCustomerDetail obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientFinanceDate getEndDate(IncomeByCustomerDetail obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

}
