package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.VATItemSummary;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ReportsActionFactory;

public class VATItemSummaryReport extends AbstractReportView<VATItemSummary> {

	public VATItemSummaryReport() {
		super(false, FinanceApplication.getVendorsMessages().norecordstoshow());
	}

	@Override
	public void OnRecordClick(VATItemSummary record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils
				.runAction(record, ReportsActionFactory
						.getVaTItemDetailAction());

	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public Object getColumnData(VATItemSummary record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getName();
		case 1:
			return record.getAmount();
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { FinanceApplication.getReportsMessages().name(),
				FinanceApplication.getReportsMessages().amount() };
	}

	@Override
	public ClientFinanceDate getEndDate(VATItemSummary obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(VATItemSummary obj) {
		return obj.getStartDate();
	}

	@Override
	public String getTitle() {
		return FinanceApplication.getReportsMessages().vatItemSummary();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	protected int getColumnWidth(int index) {
		return -1;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		FinanceApplication.createReportService().getVATItemSummaryReport(
				start.getTime(), end.getTime(), this);

	}

	@Override
	public void processRecord(VATItemSummary record) {

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

	private void printDataForOtherBrowser() {
		String gridhtml = grid.toString();
		String headerhtml = grid.getHeader();

		gridhtml = gridhtml.replaceAll(headerhtml, "");
		gridhtml = gridhtml.replaceAll(grid.getFooter(), "");
		headerhtml = headerhtml.replaceAll("td", "th");
		headerhtml = headerhtml.substring(headerhtml.indexOf("<tr "),
				headerhtml.indexOf("</tbody>"));

		String firsRow = "<tr class=\"ReportGridRow depth\">"
				+ grid.rowFormatter.getElement(0).getInnerHTML() + "</tr>";
		headerhtml = headerhtml + firsRow;

		gridhtml = gridhtml.replace(firsRow, headerhtml);
		gridhtml = gridhtml.replaceAll("<tbody>", "");
		gridhtml = gridhtml.replaceAll("</tbody>", "");

		String dateRangeHtml = "<div style=\"font-family:sans-serif;\"><strong>"
				+ this.toolbar.getStartDate()
				+ " - "
				+ this.toolbar.getEndDate() + "</strong></div>";

		UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}

	private void printDataForIEBrowser() {
		String gridhtml = grid.toString();
		String headerhtml = grid.getHeader();
		String footerHtml = grid.getFooter();

		gridhtml = gridhtml.replaceAll("\r\n", "");
		headerhtml = headerhtml.replaceAll("\r\n", "");
		footerHtml = footerHtml.replaceAll("\r\n", "");

		gridhtml = gridhtml.replaceAll(headerhtml, "");
		gridhtml = gridhtml.replaceAll(footerHtml, "");

		headerhtml = headerhtml.replaceAll("TD", "TH");
		headerhtml = headerhtml.substring(headerhtml.indexOf("<TR "),
				headerhtml.indexOf("</TBODY>"));

		String firsRow = "<TR class=\"ReportGridRow depth\">"
				+ grid.rowFormatter.getElement(0).getInnerHTML() + "</TR>";
		firsRow = firsRow.replaceAll("\r\n", "");
		headerhtml = headerhtml + firsRow;

		gridhtml = gridhtml.replace(firsRow, headerhtml);
		gridhtml = gridhtml.replaceAll("<TBODY>", "");
		gridhtml = gridhtml.replaceAll("</TBODY>", "");

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
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	public int sort(VATItemSummary obj1, VATItemSummary obj2, int col) {

		switch (col) {
		case 0:
			return obj1.getName().toLowerCase().compareTo(
					obj2.getName().toLowerCase());
		case 1:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		}
		return 0;
	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
	}

}
