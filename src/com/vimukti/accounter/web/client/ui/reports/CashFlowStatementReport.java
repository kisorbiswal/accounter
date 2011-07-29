package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.serverreports.CashFlowStatementServerReport;


public class CashFlowStatementReport extends AbstractReportView<TrialBalance> {

	public CashFlowStatementReport() {
		this.serverReport = new CashFlowStatementServerReport(this);
	}

	@Override
	public void OnRecordClick(TrialBalance record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record,
				ActionFactory.getTransactionDetailByAccountAction());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getCashFlowReport(start, end, this);

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 148, "",
				"");
	}

	@Override
	public void printPreview() {

	}

	public void exportToCsv() {
		UIUtils.exportReport(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 148, "",
				"");
	}

	/*
	 * private void printDataForIEBrowser() { String gridhtml = grid.toString();
	 * String headerhtml = grid.getHeader(); String footerhtml =
	 * grid.getFooter();
	 * 
	 * gridhtml = gridhtml.replaceAll("\r\n", ""); headerhtml =
	 * headerhtml.replaceAll("\r\n", ""); footerhtml =
	 * footerhtml.replaceAll("\r\n", "");
	 * 
	 * gridhtml = gridhtml.replaceAll(headerhtml, ""); gridhtml =
	 * gridhtml.replaceAll(footerhtml, ""); headerhtml =
	 * headerhtml.replaceAll("TD", "TH"); headerhtml =
	 * headerhtml.substring(headerhtml.indexOf("<TR "),
	 * headerhtml.indexOf("</TBODY>")); footerhtml =
	 * footerhtml.substring(footerhtml.indexOf("<TR>"),
	 * footerhtml.indexOf("</TBODY")); footerhtml =
	 * footerhtml.replaceAll("<TR>", "<TR class=listgridfooter>");
	 * 
	 * String firsRow = "<TR class=ReportGridRow>" +
	 * grid.rowFormatter.getElement(0).getInnerHTML() + "</TR>"; String lastRow
	 * = "<TR class=ReportGridRow>" +
	 * grid.rowFormatter.getElement(grid.getRowCount() - 1) .getInnerHTML() +
	 * "</TR>";
	 * 
	 * firsRow = firsRow.replaceAll("\r\n", ""); lastRow =
	 * lastRow.replaceAll("\r\n", "");
	 * 
	 * headerhtml = headerhtml + firsRow; footerhtml = lastRow + footerhtml;
	 * 
	 * gridhtml = gridhtml.replace(firsRow, headerhtml); gridhtml =
	 * gridhtml.replace(lastRow, footerhtml); gridhtml =
	 * gridhtml.replaceAll("<TBODY>", ""); gridhtml =
	 * gridhtml.replaceAll("</TBODY>", "");
	 * 
	 * String dateRangeHtml = null;
	 * 
	 * UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml); }
	 * 
	 * private void printDataForOtherBrowser() { String gridhtml =
	 * grid.toString(); String headerhtml = grid.getHeader(); String footerhtml
	 * = grid.getFooter();
	 * 
	 * gridhtml = gridhtml.replaceAll(headerhtml, ""); gridhtml =
	 * gridhtml.replaceAll(footerhtml, ""); headerhtml =
	 * headerhtml.replaceAll("td", "th"); headerhtml =
	 * headerhtml.substring(headerhtml.indexOf("<tr "),
	 * headerhtml.indexOf("</tbody>")); footerhtml =
	 * footerhtml.substring(footerhtml.indexOf("<tr>"),
	 * footerhtml.indexOf("</tbody")); footerhtml =
	 * footerhtml.replaceAll("<tr>", "<tr class=\"listgridfooter\">");
	 * 
	 * String firsRow = "<tr class=\"ReportGridRow\">" +
	 * grid.rowFormatter.getElement(0).getInnerHTML() + "</tr>"; String lastRow
	 * = "<tr class=\"ReportGridRow\">" +
	 * grid.rowFormatter.getElement(grid.getRowCount() - 1) .getInnerHTML() +
	 * "</tr>";
	 * 
	 * headerhtml = headerhtml + firsRow; footerhtml = lastRow + footerhtml;
	 * 
	 * gridhtml = gridhtml.replace(firsRow, headerhtml); gridhtml =
	 * gridhtml.replace(lastRow, footerhtml); gridhtml =
	 * gridhtml.replaceAll("<tbody>", ""); gridhtml =
	 * gridhtml.replaceAll("</tbody>", "");
	 * 
	 * String dateRangeHtml = null;
	 * 
	 * UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml); }
	 */
}