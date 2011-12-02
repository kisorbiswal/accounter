package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseOpenOrderServerReport;

public class PurchaseOpenOrderReport extends
		AbstractReportView<OpenAndClosedOrders> {

	private long status;

	public PurchaseOpenOrderReport() {
		this.serverReport = new PurchaseOpenOrderServerReport(this);
	}

	@Override
	public void init() {
		super.init();
		toolbar.setDateRanageOptions(Accounter.messages().all(), Accounter
				.messages().thisWeek(), Accounter.messages().thisMonth(),
				Accounter.messages().lastWeek(), Accounter.messages()
						.lastMonth(),
				Accounter.messages().thisFinancialYear(), Accounter
						.messages().lastFinancialYear(), Accounter.messages()
						.thisFinancialQuarter(), Accounter.messages()
						.lastFinancialQuarter(), Accounter.messages().custom());
	}

	@Override
	public void OnRecordClick(OpenAndClosedOrders record) {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			ReportsRPC.openTransactionView(record.getTransactionType(),
					record.getTransactionID());
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
			Accounter.createReportService().getPurchaseOpenOrderReport(start,
					end, this);
		else if (status == 2)
			Accounter.createReportService().getPurchaseCompletedOrderReport(
					start, end, this);
		else if (status == 3)
			Accounter.createReportService().getPurchaseCancelledOrderReport(
					start, end, this);
		else
			Accounter.createReportService().getPurchaseOrderReport(start, end,
					this);
		this.status = status;
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 134, "",
				"", status);
	}

	@Override
	public void printPreview() {

	}

	public int sort(OpenAndClosedOrders obj1, OpenAndClosedOrders obj2, int col) {

		int ret = obj1.getVendorOrCustomerName().toLowerCase()
				.compareTo(obj2.getVendorOrCustomerName().toLowerCase());
		if (ret != 0) {
			return ret;
		}
		switch (col) {

		case 0:
			return obj1.getTransactionDate().compareTo(
					obj2.getTransactionDate());

		case 1:
			return obj1.getVendorOrCustomerName().toLowerCase()
					.compareTo(obj2.getVendorOrCustomerName().toLowerCase());

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
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 134, "",
				"", status);
	}
}
