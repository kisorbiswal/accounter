package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseClosedOrderServerReport;

@SuppressWarnings("unchecked")
public class PurchaseClosedOrderReport extends
		AbstractReportView<OpenAndClosedOrders> {

	public PurchaseClosedOrderReport() {
		this.serverReport = new PurchaseClosedOrderServerReport(this);
	}

	@Override
	public void init() {
		super.init();
		toolbar.setDateRanageOptions("", FinanceApplication
				.getReportsMessages().present(), FinanceApplication
				.getReportsMessages().lastMonth(), FinanceApplication
				.getReportsMessages().last3Months(), FinanceApplication
				.getReportsMessages().last6Months(), FinanceApplication
				.getReportsMessages().lastYear(), FinanceApplication
				.getReportsMessages().untilEndOfYear(), FinanceApplication
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
		FinanceApplication.createReportService().getPurchaseClosedOrderReport(
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
				.valueOf(endDate.getTime())), 135, "", "");

		UIUtils.exportReport(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 135, "", "");

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}
}
