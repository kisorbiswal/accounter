package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;

public final class CustomerTransactionHistoryReport1 extends
		AbstractReportView<TransactionHistory> {

	private String sectionName = "";

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { " ",
				FinanceApplication.getReportsMessages().date(),
				FinanceApplication.getReportsMessages().num(),
				FinanceApplication.getReportsMessages().invoicedAmount(),
				FinanceApplication.getReportsMessages().paidAmount(),
				FinanceApplication.getReportsMessages().balance(),
				FinanceApplication.getReportsMessages().paymentTerms(),
				FinanceApplication.getReportsMessages().dueDate(),
				FinanceApplication.getReportsMessages().debit(),
				FinanceApplication.getReportsMessages().credit() };
	}

	@Override
	public String getTitle() {
		return FinanceApplication.getReportsMessages()
				.customerTransactionHistory();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		FinanceApplication.createReportService().getCustomerTransactionHistory(
				start.getTime(), end.getTime(), this);
	}

	@Override
	public void OnRecordClick(TransactionHistory record) {
		ReportsRPC.openTransactionView(record.getType(), record
				.getTransactionId());
	}

	@Override
	public Object getColumnData(TransactionHistory record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Utility.getTransactionName(record.getType());
		case 1:
			return UIUtils.getDateByCompanyType(record.getDate());
		case 2:
			return record.getNumber();
		case 3:
			return record.getInvoicedAmount();
		case 4:
			return record.getPaidAmount();
		case 5:
			return record.getInvoicedAmount() - record.getPaidAmount();
		case 6:
			return record.getPaymentTerm();
		case 7:
			return record.getDueDate();
		case 8:
			return record.getDebit();
		case 9:
			return record.getCredit();
		}
		return null;
	}

	@Override
	public void processRecord(TransactionHistory record) {
		if (sectionDepth == 0) {
			addSection("", FinanceApplication.getReportsMessages().total(),
					new int[] { 3, 5, 8, 9 });
		} else if (sectionDepth == 1) {
			// First time
			this.sectionName = record.getName();
			addSection(sectionName, "", new int[0]);
		} else if (sectionDepth == 2) {
			// Inside fist section
			addSection(FinanceApplication.getReportsMessages()
					.beginingBalance(), FinanceApplication.getReportsMessages()
					.endingBalance(), new int[] { 3, 5, 8, 9 });
		} else if (sectionDepth == 3) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getName())) {
				endSection();
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

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

		if (UIUtils.isMSIEBrowser()) {
			printDataForIEBrowser();
		} else
			printDataForOtherBrowser();

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public ClientFinanceDate getEndDate(TransactionHistory obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(TransactionHistory obj) {
		return obj.getStartDate();
	}

	private void printDataForIEBrowser() {
		String gridhtml = grid.toString();
		String headerhtml = grid.getHeader();
		String footerhtml = grid.getFooter();

		gridhtml = gridhtml.replaceAll("\r\n", "");
		headerhtml = headerhtml.replaceAll("\r\n", "");
		footerhtml = footerhtml.replaceAll("\r\n", "");

		gridhtml = gridhtml.replaceAll(headerhtml, "");
		gridhtml = gridhtml.replaceAll(footerhtml, "");
		headerhtml = headerhtml.replaceAll("TD", "TH");
		headerhtml = headerhtml.substring(headerhtml.indexOf("<TR "),
				headerhtml.indexOf("</TBODY>"));
		footerhtml = footerhtml.substring(footerhtml.indexOf("<TR>"),
				footerhtml.indexOf("</TBODY"));
		footerhtml = footerhtml.replaceAll("<TR>", "<TR class=listgridfooter>");

		String firsRow = "<TR class=ReportGridRow>"
				+ grid.rowFormatter.getElement(0).getInnerHTML() + "</TR>";
		String lastRow = "<TR class=ReportGridRow>"
				+ grid.rowFormatter.getElement(grid.getRowCount() - 1)
						.getInnerHTML() + "</TR>";

		firsRow = firsRow.replaceAll("\r\n", "");
		lastRow = lastRow.replaceAll("\r\n", "");

		headerhtml = headerhtml + firsRow;
		footerhtml = lastRow + footerhtml;

		gridhtml = gridhtml.replace(firsRow, headerhtml);
		gridhtml = gridhtml.replace(lastRow, footerhtml);
		gridhtml = gridhtml.replaceAll("<TBODY>", "");
		gridhtml = gridhtml.replaceAll("</TBODY>", "");

		String dateRangeHtml = "<div style=\"font-family:sans-serif;\"><strong>"
				+ this.toolbar.getStartDate()
				+ " - "
				+ this.toolbar.getEndDate() + "</strong></div>";

		UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}

	private void printDataForOtherBrowser() {
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

}
