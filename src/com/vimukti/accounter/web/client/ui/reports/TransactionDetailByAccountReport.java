package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class TransactionDetailByAccountReport extends
		AbstractReportView<TransactionDetailByAccount> {
	private String sectionName = "";

	private double accountBalance = 0.0D;

	private String currentsectionName = "";

	@Override
	public void OnRecordClick(TransactionDetailByAccount record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(getType(record), record
				.getTransactionId());
	}

	@Override
	public Object getColumnData(TransactionDetailByAccount record,
			int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "";
		case 1:
			return record.getName();
		case 2:
			return UIUtils.getDateByCompanyType(record.getTransactionDate());
		case 3:
			return Utility.getTransactionName(getType(record));
		case 4:
			return record.getTransactionNumber();
		case 5:
			return record.getTotal();
		case 6:
			if (!currentsectionName.equals(record.getAccountName())) {
				currentsectionName = record.getAccountName();
				accountBalance = 0.0D;
			}
			return accountBalance += record.getTotal();
		}
		return null;
	}
	int getType(TransactionDetailByAccount record) {
		if (record.getTransactionType() == 11) {
			return (record.getMemo()!=null&&record.getMemo().equalsIgnoreCase("supplier prepayment"))? ClientTransaction.TYPE_VENDOR_PAYMENT
					: ClientTransaction.TYPE_PAY_BILL;
		}

		return record.getTransactionType();
	}
	@Override
	protected String getDefaultDateRange() {
		return FinanceApplication.getReportsMessages().financialYearToDate();
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_DATE, COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { "",
				FinanceApplication.getReportsMessages().name(),
				FinanceApplication.getReportsMessages().date(), " ",
				FinanceApplication.getReportsMessages().number(),
				FinanceApplication.getReportsMessages().amount(),
				FinanceApplication.getReportsMessages().balance() };
	}

	@Override
	public String getTitle() {
		return FinanceApplication.getReportsMessages()
				.transactionDetailsByAccount();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		TrialBalance accountdetails = (TrialBalance) data;
		if (accountdetails == null) {
			FinanceApplication.createReportService()
					.getTransactionDetailByAccount(start.getTime(),
							end.getTime(), this);
		} else if (accountdetails.getAccountName() != null) {
			FinanceApplication.createReportService()
					.getTransactionDetailByAccount(
							accountdetails.getAccountName(), start.getTime(),
							end.getTime(), this);
		}
	}

	@Override
	public void processRecord(TransactionDetailByAccount record) {
		if (sectionDepth == 0) {
			addSection(new String[] { "", "" }, new String[] { "", "", "", "",
					FinanceApplication.getReportsMessages().total() },
					new int[] { 5 });
		} else if (sectionDepth == 1) {
			this.sectionName = record.getAccountName();
			addSection(new String[] { sectionName }, new String[] { "" },
					new int[] { 5 });
		} else if (sectionDepth == 2) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getAccountName())) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

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
	public ClientFinanceDate getEndDate(TransactionDetailByAccount obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(TransactionDetailByAccount obj) {
		return obj.getStartDate();

	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((BaseReport) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((BaseReport) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((BaseReport) object).getEndDate();
	}

	@Override
	protected int getColumnWidth(int index) {
		switch (index) {
		case 0:
			return 230;
		case 1:
			return 140;
		case 2:
			return 80;
		case 3:
			return 140;
		case 4:
			return 55;

		default:
			return 120;
		}
	}

	@Override
	public int sort(TransactionDetailByAccount obj1,
			TransactionDetailByAccount obj2, int col) {
		int ret = obj1.getAccountName().toLowerCase().compareTo(
				obj2.getAccountName().toLowerCase());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 0:
			return obj1.getAccountName().toLowerCase().compareTo(obj2.getAccountName().toLowerCase());
		case 1:
			String name1=obj1.getName();
			String name2=obj2.getName();
			return name1.toLowerCase().compareTo(name2.toLowerCase());
		case 2:
			return obj1.getTransactionDate().compareTo(obj2.getTransactionDate());
		case 3:
			return UIUtils.compareInt(obj1.getTransactionType(),obj2.getTransactionType());
		case 4:
			int num1 = UIUtils.isInteger(obj1.getTransactionNumber())?Integer.parseInt(obj1.getTransactionNumber()):0;
			int num2 = UIUtils.isInteger(obj2.getTransactionNumber())?Integer.parseInt(obj2.getTransactionNumber()):0;
			if (num1!=0 && num2!=0)
				return UIUtils.compareInt(num1, num2);
			else
				return obj1.getTransactionNumber().compareTo(obj2.getTransactionNumber());

		case 5:
			return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
		case 6:
			if (!currentsectionName.toLowerCase().equals(
					obj1.getAccountName().toLowerCase())) {
				return obj1.getAccountName().toLowerCase().compareTo(
						obj2.getAccountName().toLowerCase());
			} else {
				return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
			}
		}
		return 0;
	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
		this.sectionName = "";
		this.currentsectionName = "";
	}

	@Override
	public TransactionDetailByAccount getObject(
			TransactionDetailByAccount parent, TransactionDetailByAccount child) {
		if (parent == null) {

		}
		return null;
	}

	@Override
	public boolean isWiderReport() {
		return true;
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

		UIUtils.generateReportPDF(this.getAction().getText(), gridhtml,
				dateRangeHtml);
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

		UIUtils.generateReportPDF(this.getAction().getText(), gridhtml,
				dateRangeHtml);
	}
}
