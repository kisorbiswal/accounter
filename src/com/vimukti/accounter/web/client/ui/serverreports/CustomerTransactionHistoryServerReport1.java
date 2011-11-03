package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class CustomerTransactionHistoryServerReport1 extends
		AbstractFinaneReport<TransactionHistory> {

	private String sectionName = "";

	public CustomerTransactionHistoryServerReport1(
			IFinanceReport<TransactionHistory> reportView) {
		this.reportView = reportView;
	}

	public CustomerTransactionHistoryServerReport1(long startDate,
			long endDate, int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { " ", getConstants().date(),
				getConstants().noDot(), getConstants().invoicedAmount(),
				getConstants().paidAmount(), getConstants().balance(),
				getConstants().paymentTerms(), getConstants().dueDate(),
				getConstants().debit(), getConstants().credit() };
	}

	@Override
	public String getTitle() {
		return Accounter.messages().payeeTransactionHistory(
				Global.get().Customer());
	}

	@Override
	public void makeReportRequest(long start, long end) {
		Accounter.createReportService().getCustomerTransactionHistory(
				new ClientFinanceDate(start), new ClientFinanceDate(end), this);
	}

	@Override
	public Object getColumnData(TransactionHistory record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Utility.getTransactionName(record.getType());
		case 1:
			return getDateByCompanyType(record.getDate());
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
			addSection("", "Total", new int[] { 3, 5, 8, 9 });
		} else if (sectionDepth == 1) {
			// First time
			this.sectionName = record.getName();
			addSection(sectionName, "", new int[0]);
		} else if (sectionDepth == 2) {
			// Inside fist section
			addSection(getConstants().beginingBalance(), getConstants()
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

	public void print() {

		if (UIUtils.isMSIEBrowser()) {
			printDataForIEBrowser();
		} else
			printDataForOtherBrowser();

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
		// String gridhtml = grid.toString();
		// String headerhtml = grid.getHeader();
		// String footerhtml = grid.getFooter();
		//
		// gridhtml = gridhtml.replaceAll("\r\n", "");
		// headerhtml = headerhtml.replaceAll("\r\n", "");
		// footerhtml = footerhtml.replaceAll("\r\n", "");
		//
		// gridhtml = gridhtml.replaceAll(headerhtml, "");
		// gridhtml = gridhtml.replaceAll(footerhtml, "");
		// headerhtml = headerhtml.replaceAll("TD", "TH");
		// headerhtml = headerhtml.substring(headerhtml.indexOf("<TR "),
		// headerhtml.indexOf("</TBODY>"));
		// footerhtml = footerhtml.substring(footerhtml.indexOf("<TR>"),
		// footerhtml.indexOf("</TBODY"));
		// footerhtml = footerhtml.replaceAll("<TR>",
		// "<TR class=listgridfooter>");
		//
		// String firsRow = "<TR class=ReportGridRow>"
		// + "" + "</TR>";
		// String lastRow = "<TR class=ReportGridRow>"
		// + grid.rowFormatter.getElement(grid.getRowCount() - 1)
		// .getInnerHTML() + "</TR>";
		//
		// firsRow = firsRow.replaceAll("\r\n", "");
		// lastRow = lastRow.replaceAll("\r\n", "");
		//
		// headerhtml = headerhtml + firsRow;
		// footerhtml = lastRow + footerhtml;
		//
		// gridhtml = gridhtml.replace(firsRow, headerhtml);
		// gridhtml = gridhtml.replace(lastRow, footerhtml);
		// gridhtml = gridhtml.replaceAll("<TBODY>", "");
		// gridhtml = gridhtml.replaceAll("</TBODY>", "");
		//
		// String dateRangeHtml =
		// "<div style=\"font-family:sans-serif;\"><strong>"
		// + this.getStartDate()
		// + " - "
		// + this.getEndDate() + "</strong></div>";
		//
		// generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}

	private void printDataForOtherBrowser() {
		// String gridhtml = grid.toString();
		// String headerhtml = grid.getHeader();
		// String footerhtml = grid.getFooter();
		//
		// gridhtml = gridhtml.replaceAll(headerhtml, "");
		// gridhtml = gridhtml.replaceAll(footerhtml, "");
		// headerhtml = headerhtml.replaceAll("td", "th");
		// headerhtml = headerhtml.substring(headerhtml.indexOf("<tr "),
		// headerhtml.indexOf("</tbody>"));
		// footerhtml = footerhtml.substring(footerhtml.indexOf("<tr>"),
		// footerhtml.indexOf("</tbody"));
		// footerhtml = footerhtml.replaceAll("<tr>",
		// "<tr class=\"listgridfooter\">");
		//
		// String firsRow = "<tr class=\"ReportGridRow\">"
		// + "" + "</tr>";
		// String lastRow = "<tr class=\"ReportGridRow\">"
		// + grid.rowFormatter.getElement(grid.getRowCount() - 1)
		// .getInnerHTML() + "</tr>";
		//
		// headerhtml = headerhtml + firsRow;
		// footerhtml = lastRow + footerhtml;
		//
		// gridhtml = gridhtml.replace(firsRow, headerhtml);
		// gridhtml = gridhtml.replace(lastRow, footerhtml);
		// gridhtml = gridhtml.replaceAll("<tbody>", "");
		// gridhtml = gridhtml.replaceAll("</tbody>", "");
		//
		// String dateRangeHtml =
		// "<div style=\"font-family:sans-serif;\"><strong>"
		// + this.getStartDate()
		// + " - "
		// + this.getEndDate() + "</strong></div>";
		//
		// generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { " ", getConstants().date(),
				getConstants().noDot(), getConstants().invoicedAmount(),
				getConstants().paidAmount(), getConstants().balance(),
				getConstants().paymentTerms(), getConstants().dueDate(),
				getConstants().debit(), getConstants().credit() };
	}

}
