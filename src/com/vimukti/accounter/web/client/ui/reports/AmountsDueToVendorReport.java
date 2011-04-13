package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.AmountsDueToVendor;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.AmountsDueToVendorServerReport;

@SuppressWarnings("unchecked")
public class AmountsDueToVendorReport extends
		AbstractReportView<AmountsDueToVendor> {

	public AmountsDueToVendorReport() {
		this.serverReport = new AmountsDueToVendorServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		FinanceApplication.createReportService().getAmountsDueToVendor(
				start.getTime(), end.getTime(), this);
	}

	@Override
	public void OnRecordClick(AmountsDueToVendor record) {
		// nothing to do
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {

		UIUtils.generateReportPDF(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 149, "", "");
	}

	@Override
	public void printPreview() {

	}

	@Override
	public int getToolbarType() {
		return 0;
	}

	public void exportToCsv() {
		UIUtils.exportReport(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 149, "", "");
	}


}
