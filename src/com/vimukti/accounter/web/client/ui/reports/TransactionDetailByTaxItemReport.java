package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.SalesTaxLiability;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ReportsActionFactory;

public class TransactionDetailByTaxItemReport extends
		AbstractReportView<TransactionDetailByTaxItem> {

	private String sectionName = "";

	@Override
	public void OnRecordClick(TransactionDetailByTaxItem record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getTransactionType(), record
				.getTransactionId());

	}

	@Override
	public Object getColumnData(TransactionDetailByTaxItem record,
			int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getTaxItemName();
		case 1:
			return record.getRate();
		case 2:
			return UIUtils.getDateByCompanyType(record.getDate());
		case 3:
			return record.getNumber();
		case 4:
			return Utility.getTransactionName(record.getTransactionType());
		case 5:
			return record.getMemo();
		case 6:
			return record.getSalesTaxAmount();
		case 7:
			return record.getTaxableAmount();
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_DATE, COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { "",
				FinanceApplication.getReportsMessages().taxRate(),
				FinanceApplication.getReportsMessages().date(),
				FinanceApplication.getReportsMessages().no(), "",
				FinanceApplication.getReportsMessages().memo(),
				FinanceApplication.getReportsMessages().salesTax(),
				FinanceApplication.getReportsMessages().taxableAmount() };

	}

	@Override
	public String getTitle() {
		return FinanceApplication.getReportsMessages()
				.transactionDetailByTaxItem();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		SalesTaxLiability taxLiability = (SalesTaxLiability) data;
		if (taxLiability == null) {
			FinanceApplication.createReportService()
					.getTransactionDetailByTaxItem(start.getTime(),
							end.getTime(), this);
		} else if (taxLiability.getTaxItemName() != null) {
			FinanceApplication.createReportService()
					.getTransactionDetailByTaxItem(
							taxLiability.getTaxItemName(), start.getTime(),
							end.getTime(), this);

		}
	}

	@Override
	public void processRecord(TransactionDetailByTaxItem record) {
		if (sectionDepth == 0) {
			addSection("", FinanceApplication.getReportsMessages().total(),
					new int[] { 6, 7 });
		} else if (sectionDepth == 1) {
			this.sectionName = record.getTaxAgencyName();
			addSection(new String[] { sectionName }, new String[] {""},
					new int[] { 6, 7 });
		} else if (sectionDepth == 2) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getTaxAgencyName())) {
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
	public ClientFinanceDate getEndDate(TransactionDetailByTaxItem obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(TransactionDetailByTaxItem obj) {
		return obj.getStartDate();
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
