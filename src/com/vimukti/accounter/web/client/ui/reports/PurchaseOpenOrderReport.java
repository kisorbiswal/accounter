package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class PurchaseOpenOrderReport extends
		AbstractReportView<OpenAndClosedOrders> {

	private String sectionName;
	private boolean isPurchases;

	public PurchaseOpenOrderReport() {

		isPurchases = FinanceApplication.isPurchases();
	}

	@Override
	public void init() {
		super.init();
		toolbar.setDateRanageOptions(FinanceApplication.getReportsMessages()
				.all(), FinanceApplication.getReportsMessages().thisWeek(),
				FinanceApplication.getReportsMessages().thisMonth(),
				FinanceApplication.getReportsMessages().lastWeek(),
				FinanceApplication.getReportsMessages().lastMonth(),
				FinanceApplication.getReportsMessages().thisFinancialYear(),
				FinanceApplication.getReportsMessages().lastFinancialYear(),
				FinanceApplication.getReportsMessages().thisFinancialQuarter(),
				FinanceApplication.getReportsMessages().lastFinancialQuarter(),
				FinanceApplication.getReportsMessages().custom());
	}

	@Override
	public void OnRecordClick(OpenAndClosedOrders record) {
		ReportsRPC.openTransactionView(record.getTransactionType(), record
				.getTransactionID());
	}

	@Override
	public Object getColumnData(OpenAndClosedOrders record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if (record.getTransactionDate() != null)
				return UIUtils
						.getDateByCompanyType(record.getTransactionDate());
			else
				break;
		case 1:
			return record.getVendorOrCustomerName();
//		case 2:
//			// if (isPurchases)
//			return record.getDescription();
			// else
			// return record.getAmount();
//		case 2:
//			return ((Double) record.getQuantity()).toString();

		case 2:
			return record.getAmount();

		default:
			break;
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		// if (isPurchases)
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
		// else
		// return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
		// COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		// if (isPurchases)
		return new String[] {
				FinanceApplication.getReportsMessages().orderDate(),
				FinanceApplication.getReportsMessages().supplier(),
//				FinanceApplication.getReportsMessages().description(),
//				FinanceApplication.getReportsMessages().quantity(),
				FinanceApplication.getReportsMessages().amount() };
		// else
		// return new String[] {
		// FinanceApplication.getReportsMessages().orderDate(),
		// FinanceApplication.getReportsMessages().supplier(),
		// FinanceApplication.getReportsMessages().value() };
	}

	@Override
	public String getTitle() {
		return FinanceApplication.getReportsMessages().purchaseOrderReport();
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
		resetVariables();
		if (status == 1)
			FinanceApplication.createReportService()
					.getPurchaseOpenOrderReport(start.getTime(), end.getTime(),
							this);
		else if (status == 2)
			FinanceApplication.createReportService()
					.getPurchaseCompletedOrderReport(start.getTime(),
							end.getTime(), this);
		else if (status == 3)
			FinanceApplication.createReportService()
			.getPurchaseCancelledOrderReport(start.getTime(),
					end.getTime(), this);
		else
			FinanceApplication.createReportService()
					.getPurchaseOrderReport(start.getTime(),
							end.getTime(), this);

	}

	@Override
	public void processRecord(OpenAndClosedOrders record) {

		int col;
		// if (isPurchases)
		col = 2;
		// else
		// col = 2;
		if (sectionDepth == 0) {
			addSection("", FinanceApplication.getReportsMessages().total(),
					new int[] { col });
		} else if (sectionDepth == 1) {
			// First time
			this.sectionName = record.getVendorOrCustomerName();
			addSection(sectionName, FinanceApplication.getReportsMessages()
					.total(), new int[] { col });
		} else if (sectionDepth == 2) {
			// No need to do anything, just allow adding this record
			if (sectionName != null
					&& !sectionName.equals(record.getVendorOrCustomerName())) {
				endSection();
			} else {
				return;
			}

		}
		processRecord(record);
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
		String gridhtml = grid.toString();
		String headerhtml = grid.getHeader();
		String footerhtml = grid.getFooter();

		gridhtml = gridhtml.replaceAll(headerhtml, "");
		gridhtml = gridhtml.replaceAll(footerhtml, "");
		headerhtml = headerhtml.replaceAll("td", "th");
		headerhtml = headerhtml.substring(headerhtml.indexOf("<tr "),
				headerhtml.indexOf("</tbody>"));
		footerhtml = footerhtml.substring(footerhtml.indexOf("<tr>"),
				footerhtml.indexOf("</tbody"));
		footerhtml = footerhtml.replaceAll("<tr>",
				"<tr class=\"listgridfooter\">");

		String firsRow = "<tr class=\"ReportGridRow\">"
				+ grid.rowFormatter.getElement(0).getInnerHTML() + "</tr>";
		String lastRow = "<tr class=\"ReportGridRow\">"
				+ grid.rowFormatter.getElement(grid.getRowCount() - 1)
						.getInnerHTML() + "</tr>";

		headerhtml = headerhtml + firsRow;
		footerhtml = lastRow + footerhtml;

		gridhtml = gridhtml.replace(firsRow, headerhtml);
		gridhtml = gridhtml.replace(lastRow, footerhtml);
		gridhtml = gridhtml.replaceAll("<tbody>", "");
		gridhtml = gridhtml.replaceAll("</tbody>", "");

		String dateRangeHtml = "<div style=\"font-family:sans-serif;\"><strong>"
				+ this.toolbar.getStartDate()
				+ " - "
				+ this.toolbar.getEndDate() + "</strong></div>";

		UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public ClientFinanceDate getEndDate(OpenAndClosedOrders obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(OpenAndClosedOrders obj) {
		return obj.getStartDate();
	}

	@Override
	protected int getColumnWidth(int index) {
		// if (isPurchases) {
		if (index == 0 || index == 2)
			return 200;
		

		// }
		// else {
		// if (index == 1)
		// return 300;
		// else if (index == 2)
		// return 200;
		// }
		else
			return -1;
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

//		case 2:
//			// if (isPurchases)
//			return obj1.getDescription().toLowerCase().compareTo(
//					obj2.getDescription().toLowerCase());
			// else
			// return UIUtils
			// .compareDouble(obj1.getAmount(), obj2.getAmount());
//		case 2:
//			return UIUtils
//					.compareDouble(obj1.getQuantity(), obj2.getQuantity());

		case 2:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());

		}
		return 0;
	}

	public void resetVariables() {
		sectionDepth = 0;
		sectionName = "";
		this.grid.getFooterTable().setText(0, 0, "");
		this.grid.getFooterTable().setText(0, 1, "");
		this.grid.getFooterTable().setText(0, 2, "");
		super.resetVariables();
	}

}
