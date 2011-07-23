package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.SalesClosedOrderServerReport;

@SuppressWarnings("unchecked")
public class SalesClosedOrderReport extends
		AbstractReportView<OpenAndClosedOrders> {

	public SalesClosedOrderReport() {
		this.serverReport = new SalesClosedOrderServerReport(this);
	}

	@Override
	public void init() {
		super.init();
		toolbar.setDateRanageOptions("", Accounter
				.getReportsMessages().present(), Accounter
				.getReportsMessages().lastMonth(), Accounter
				.getReportsMessages().last3Months(), Accounter
				.getReportsMessages().last6Months(), Accounter
				.getReportsMessages().lastYear(), Accounter
				.getReportsMessages().untilEndOfYear(), Accounter
				.getReportsMessages().custom());
	}

	@Override
	public void OnRecordClick(OpenAndClosedOrders record) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getSalesClosedOrderReport(
				start.getTime(), end.getTime(), this);
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
				.valueOf(endDate.getTime())), 126, "", "");

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	public void exportToCsv() {
		UIUtils.exportReport(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 126, "", "");
	}

}
