package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.IncomeByCustomerDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.serverreports.IncomeByCustomerDetailServerReport;

public class IncomeByCustomerDetailReport extends
		AbstractReportView<IncomeByCustomerDetail> {

	public IncomeByCustomerDetailReport() {
		this.serverReport = new IncomeByCustomerDetailServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getIncomeByCustomerDetail(start, end,
				this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(IncomeByCustomerDetail record) {

	}

	@Override
	public void export(int generationType) {
		// TODO Auto-generated method stub

	}

}
