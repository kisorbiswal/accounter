package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * @author kumar kasimala
 * 
 * 
 */
public class VendorTransactionHistoryReport extends
		AbstractReportView<TransactionHistory> {

	protected TransactionHistory transactionHistory;
	private String sectionName = "";

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] {
				UIUtils.getVendorString(FinanceApplication.getReportsMessages()
						.supplier(), FinanceApplication.getReportsMessages()
						.vendor()),
				FinanceApplication.getReportsMessages().date(),
				FinanceApplication.getReportsMessages().type(),
				FinanceApplication.getReportsMessages().no(),
				// FinanceApplication.getReportsMessages().reference(),
				FinanceApplication.getReportsMessages().account(),
				FinanceApplication.getReportsMessages().amount()
		// FinanceApplication.getReportsMessages().transactionAmount(),
		// FinanceApplication.getReportsMessages().paidAmount(),
		// FinanceApplication.getReportsMessages().discount(),
		// FinanceApplication.getReportsMessages().balance(),
		// FinanceApplication.getReportsMessages().paymentSatus(),
		// FinanceApplication.getReportsMessages().PONum(),
		// FinanceApplication.getReportsMessages().memo(),
		// FinanceApplication.getReportsMessages().dueDate(),
		// FinanceApplication.getReportsMessages().debit(),
		// FinanceApplication.getReportsMessages().credit()
		};

		// "Aging(days)", "Payment Terms",
		// "Vendor Group"
		// FIXME if required add it, "Void", "Reference"

	}

	@Override
	public String getTitle() {
		return UIUtils.getVendorString(FinanceApplication.getReportsMessages()
				.supplierTransactionHistory(), FinanceApplication
				.getReportsMessages().vendorTransactionHistory());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	protected int getColumnWidth(int index) {
		if (index == 1)
			return 85;
		else if (index == 3)
			return 60;
		else if (index == 2)
			return 145;
		// else if (index == 4)
		// return 250;
		// else if (index == 4)
		// return 200;
		else if (index == 0)
			return 150;
		else if (index == 5)
			return 145;
		else
			return 175;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		FinanceApplication.createReportService().getVendorTransactionHistory(
				start.getTime(), end.getTime(), this);
	}

	@Override
	public void OnRecordClick(TransactionHistory record) {
		ReportsRPC.openTransactionView(getType(record), record
				.getTransactionId());
	}

	int getType(TransactionHistory record) {
		if (record.getType() == 11) {
			return (record.getMemo() != null && record.getMemo()
					.equalsIgnoreCase("supplier prepayment")) ? ClientTransaction.TYPE_VENDOR_PAYMENT
					: ClientTransaction.TYPE_PAY_BILL;
		}

		return record.getType();
	}

	@Override
	public Object getColumnData(TransactionHistory record, int columnIndex) {
		switch (columnIndex) {
		// " ", "ClientFinanceDate", "No.", "Transaction Amount",
		// "Paid Amount", "Discount", "Balance", "Payment Satus",
		// "PO No.", "Memo", "Due ClientFinanceDate", "Aging(days)",
		// "Payment Terms",
		// "Vendor Group", "Debit", "Credit"
		case 0:
			return "";
		case 2:
			return Utility.getTransactionName(getType(record));
		case 1:
			return UIUtils.getDateByCompanyType(record.getDate());
		case 3:
			return record.getNumber();
			// case 3:
			// return record.getReference();
			// return record.getInvoicedAmount();
		case 4:
			return record.getAccount();
			// return record.getPaidAmount();
		case 5:
			return DecimalUtil.isEquals(record.getInvoicedAmount(), 0.0) ? record
					.getPaidAmount()
					: record.getInvoicedAmount();
			// return record.getDiscount();
			// case 6:
			// return record.getInvoicedAmount() - record.getPaidAmount()
			// - record.getDiscount();
			// case 7:
			// return Utility.getTransactionStatus(record.getType(), record
			// .getStatus());
			// case 8:
			// return record.getNumber();
			// case 9:
			// return record.getMemo();sort
			// case 10:
			// return record.getDueDate();
			// case 11:
			// return record.getDebit();
			// case 12:
			// return record.getCredit();

		}
		return null;
	}

	@Override
	public void processRecord(TransactionHistory record) {
		if (sectionDepth == 0) {
			addSection(new String[] { "", "" }, new String[] { "", "", "", "",
					FinanceApplication.getReportsMessages().total() },
					new int[] { 5 });
		} else if (sectionDepth == 1) {
			// First time
			this.sectionName = record.getName();
			addSection(new String[] { sectionName }, new String[] { "", "", "",
					"", FinanceApplication.getReportsMessages().total() },
					new int[] { 5 });
		}
		// else if (sectionDepth == 2) {
		// // Inside fist section
		// addSection(FinanceApplication.getReportsMessages()
		// .beginingBalance(), FinanceApplication.getReportsMessages()
		// .endingBalance(), new int[] { 5 });
		// }
		else if (sectionDepth == 2) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getName())) {
				endSection();
				// endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
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
	public ClientFinanceDate getEndDate(TransactionHistory obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(TransactionHistory obj) {
		return obj.getStartDate();
	}

	@Override
	public int sort(TransactionHistory obj1, TransactionHistory obj2, int col) {
		int ret = obj1.getName().toLowerCase().compareTo(
				obj2.getName().toLowerCase());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 0:
			return obj1.getName().compareTo(obj2.getName());
		case 2:
			return UIUtils.compareInt(obj1.getType(), obj2.getType());
		case 1:
			return obj1.getDate().compareTo(obj2.getDate());
		case 3:
			return UIUtils.compareInt(Integer.parseInt(obj1.getNumber()),
					Integer.parseInt(obj2.getNumber()));
			// case 3:
			// return obj1.getReference().toLowerCase().compareTo(
			// obj2.getReference().toLowerCase());
		case 4:
			return obj1.getAccount().toLowerCase().compareTo(
					obj2.getAccount().toLowerCase());
		case 5:
			if (DecimalUtil.isEquals(obj1.getInvoicedAmount(), 0.0))
				return UIUtils.compareDouble(obj1.getPaidAmount(), obj2
						.getPaidAmount());
			else
				return UIUtils.compareDouble(obj1.getInvoicedAmount(), obj2
						.getInvoicedAmount());
		}
		return 0;
	}

	public void resetVariables() {
		sectionDepth = 0;
		sectionName = "";
		super.resetVariables();
	}
}
