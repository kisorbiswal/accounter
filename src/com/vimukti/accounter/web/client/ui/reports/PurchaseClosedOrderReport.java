package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class PurchaseClosedOrderReport extends
		AbstractReportView<OpenAndClosedOrders> {

	private String sectionName;

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

	public PurchaseClosedOrderReport() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void OnRecordClick(OpenAndClosedOrders record) {
		// TODO Auto-generated method stub

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
		case 2:
			return record.getDescription();
		case 3:
			return ((Double) record.getQuantity()).toString();
		case 4:
			return record.getAmount();

		default:
			break;
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] {
				FinanceApplication.getReportsMessages().orderDate(),
				FinanceApplication.getReportsMessages().vendor(),
				FinanceApplication.getReportsMessages().description(),
				FinanceApplication.getReportsMessages().quantity(),
				FinanceApplication.getReportsMessages().value() };

	}

	@Override
	public String getTitle() {

		return FinanceApplication.getReportsMessages()
				.purchaseClosedOrderReport();
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
	public void processRecord(OpenAndClosedOrders record) {
		if (sectionDepth == 0) {
			addSection("", FinanceApplication.getReportsMessages().totalOf(),
					new int[] { 4 });
		} else if (sectionDepth == 1) {
			// First time
			this.sectionName = record.getVendorOrCustomerName();
			addSection(sectionName, FinanceApplication.getReportsMessages()
					.totalOf()
					+ sectionName, new int[] { 4 });
		} else if (sectionDepth == 2) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getVendorOrCustomerName())) {
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

}
