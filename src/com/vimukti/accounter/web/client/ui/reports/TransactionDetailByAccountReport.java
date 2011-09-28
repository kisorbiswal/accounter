package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.TransactionDetailByAccountServerReport;

public class TransactionDetailByAccountReport extends
		AbstractReportView<TransactionDetailByAccount> {
	private String currentsectionName = "";

	public TransactionDetailByAccountReport() {
		this.serverReport = new TransactionDetailByAccountServerReport(this);
	}

	@Override
	public void OnRecordClick(TransactionDetailByAccount record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(getType(record),
					record.getTransactionId());
	}

	int getType(TransactionDetailByAccount record) {
		if (record.getTransactionType() == 11) {
			return (record.getMemo() != null && record.getMemo()
					.equalsIgnoreCase("supplier prepayment")) ? ClientTransaction.TYPE_VENDOR_PAYMENT
					: ClientTransaction.TYPE_PAY_BILL;
		}

		return record.getTransactionType();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		TrialBalance accountdetails = (TrialBalance) data;
		if (accountdetails == null) {
			Accounter.createReportService().getTransactionDetailByAccount(
					start, end, this);
		} else if (accountdetails.getAccountName() != null) {
			Accounter.createReportService().getTransactionDetailByAccount(
					accountdetails.getAccountName(),
					accountdetails.getStartDate(), accountdetails.getEndDate(),
					this);
		}
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {
		String accountName = data != null ? ((TrialBalance) data)
				.getAccountName() : "";
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 115, "",
				"", accountName);

	}

	@Override
	public void printPreview() {

	}

	public int sort(TransactionDetailByAccount obj1,
			TransactionDetailByAccount obj2, int col) {
		int ret = obj1.getAccountName().toLowerCase()
				.compareTo(obj2.getAccountName().toLowerCase());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 0:
			return obj1.getAccountName().toLowerCase()
					.compareTo(obj2.getAccountName().toLowerCase());
		case 1:
			String name1 = obj1.getName();
			String name2 = obj2.getName();
			return name1.toLowerCase().compareTo(name2.toLowerCase());
		case 2:
			return obj1.getTransactionDate().compareTo(
					obj2.getTransactionDate());
		case 3:
			return UIUtils.compareInt(obj1.getTransactionType(),
					obj2.getTransactionType());
		case 4:
			int num1 = UIUtils.isInteger(obj1.getTransactionNumber()) ? Integer
					.parseInt(obj1.getTransactionNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getTransactionNumber()) ? Integer
					.parseInt(obj2.getTransactionNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return obj1.getTransactionNumber().compareTo(
						obj2.getTransactionNumber());

		case 5:
			return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
		case 6:
			if (!currentsectionName.toLowerCase().equals(
					obj1.getAccountName().toLowerCase())) {
				return obj1.getAccountName().toLowerCase()
						.compareTo(obj2.getAccountName().toLowerCase());
			} else {
				return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
			}
		}
		return 0;
	}

	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 115, "",
				"");
	}

	// private void printDataForIEBrowser() {
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
	// footerhtml = footerhtml.replaceAll("<TR>", "<TR class=listgridfooter>");
	//
	// String firsRow = "<TR class=ReportGridRow>"
	// + grid.rowFormatter.getElement(0).getInnerHTML() + "</TR>";
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
	// String dateRangeHtml = "<div style=\"font-family:sans-serif;\"><strong>"
	// + this.toolbar.getStartDate()
	// + " - "
	// + this.toolbar.getEndDate() + "</strong></div>";
	//
	// UIUtils.generateReportPDF(this.getAction().getText(), gridhtml,
	// dateRangeHtml);
	// }
	//
	// private void printDataForOtherBrowser() {
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
	// + grid.rowFormatter.getElement(0).getInnerHTML() + "</tr>";
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
	// String dateRangeHtml = "<div style=\"font-family:sans-serif;\"><strong>"
	// + this.toolbar.getStartDate()
	// + " - "
	// + this.toolbar.getEndDate() + "</strong></div>";
	//
	// UIUtils.generateReportPDF(this.getAction().getText(), gridhtml,
	// dateRangeHtml);
	// }

}
