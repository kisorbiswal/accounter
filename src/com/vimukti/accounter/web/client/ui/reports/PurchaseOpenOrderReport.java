package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseOpenOrderServerReport;

@SuppressWarnings("unchecked")
public class PurchaseOpenOrderReport extends
		AbstractReportView<OpenAndClosedOrders> {

	@SuppressWarnings("unused")
	private boolean isPurchases;
	private String status;

	public PurchaseOpenOrderReport() {
		this.serverReport = new PurchaseOpenOrderServerReport(this);
		isPurchases = Accounter.isPurchases();
	}

	@Override
	public void init() {
		super.init();
		toolbar.setDateRanageOptions(Accounter.getReportsMessages()
				.all(), Accounter.getReportsMessages().thisWeek(),
				Accounter.getReportsMessages().thisMonth(),
				Accounter.getReportsMessages().lastWeek(),
				Accounter.getReportsMessages().lastMonth(),
				Accounter.getReportsMessages().thisFinancialYear(),
				Accounter.getReportsMessages().lastFinancialYear(),
				Accounter.getReportsMessages().thisFinancialQuarter(),
				Accounter.getReportsMessages().lastFinancialQuarter(),
				Accounter.getReportsMessages().custom());
	}

	@Override
	public void OnRecordClick(OpenAndClosedOrders record) {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			ReportsRPC.openTransactionView(record.getTransactionType(), record
					.getTransactionID());
			// resetReport(toolbar.getStartDate(), toolbar.getEndDate());
		}
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_SALES_PURCAHASE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

	}

	@Override
	public void makeReportRequest(int status, ClientFinanceDate start,
			ClientFinanceDate end) {
		if (status == 1)
			Accounter.createReportService()
					.getPurchaseOpenOrderReport(start.getTime(), end.getTime(),
							this);
		else if (status == 2)
			Accounter.createReportService()
					.getPurchaseCompletedOrderReport(start.getTime(),
							end.getTime(), this);
		else if (status == 3)
			Accounter.createReportService()
					.getPurchaseCancelledOrderReport(start.getTime(),
							end.getTime(), this);
		else
			Accounter.createReportService().getPurchaseOrderReport(
					start.getTime(), end.getTime(), this);
		this.status = String.valueOf(status);
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
				.valueOf(endDate.getTime())), 134, "", "", status);
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	public int sort(OpenAndClosedOrders obj1, OpenAndClosedOrders obj2, int col) {

		int ret = obj1.getVendorOrCustomerName().toLowerCase().compareTo(
				obj2.getVendorOrCustomerName().toLowerCase());
		if (ret != 0) {
			return ret;
		}
		switch (col) {

		case 0:
			return obj1.getTransactionDate().compareTo(
					obj2.getTransactionDate());

		case 1:
			return obj1.getVendorOrCustomerName().toLowerCase().compareTo(
					obj2.getVendorOrCustomerName().toLowerCase());

			// case 2:
			// // if (isPurchases)
			// return obj1.getDescription().toLowerCase().compareTo(
			// obj2.getDescription().toLowerCase());
			// else
			// return UIUtils
			// .compareDouble(obj1.getAmount(), obj2.getAmount());
			// case 2:
			// return UIUtils
			// .compareDouble(obj1.getQuantity(), obj2.getQuantity());

		case 2:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());

		}
		return 0;
	}

	public void exportToCsv() {
		UIUtils.exportReport(Integer.parseInt(String.valueOf(startDate
				.getTime())), Integer.parseInt(String
				.valueOf(endDate.getTime())), 134, "", "", status);
	}
}
