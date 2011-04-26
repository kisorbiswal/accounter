package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.StatementServerReport;

@SuppressWarnings("unchecked")
public class StatementReport extends AbstractReportView<PayeeStatementsList> {
	public int precategory = 1001;
	public String customerId;

	public StatementReport() {
		this.serverReport = new StatementServerReport(this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_CUSTOMER;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
	}

	@Override
	public void makeReportRequest(String customer, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		// resetReport(endDate, endDate);
		FinanceApplication.createReportService().getStatements(customer,
				new ClientFinanceDate().getTime(), startDate.getTime(),
				endDate.getTime(), 0, false, false, 0.00, false, false, this);
		customerId = customer;
	}

	@Override
	public void OnRecordClick(PayeeStatementsList record) {
		record.setStartDate(toolbar.getEndDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getTransactiontype(), record
				.getTransactionId());

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	public void onEdit() {

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

		String firsRow = "<tr class=\"ReportGridRow\">"
				+ grid.rowFormatter.getElement(0).getInnerHTML() + "</tr>";
		headerhtml = headerhtml + firsRow;

		gridhtml = gridhtml.replace(firsRow, headerhtml);
		gridhtml = gridhtml.replaceAll("<tbody>", "");
		gridhtml = gridhtml.replaceAll("</tbody>", "");

		String dateRangeHtml = null;

		UIUtils.generateStatementPDF(this.getTitle(), gridhtml, dateRangeHtml,
				customerId);
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

		String firsRow = "<TR class=ReportGridRow>"
				+ grid.rowFormatter.getElement(0).getInnerHTML() + "</TR>";
		firsRow = firsRow.replaceAll("\r\n", "");
		headerhtml = headerhtml + firsRow;

		gridhtml = gridhtml.replace(firsRow, headerhtml);
		gridhtml = gridhtml.replaceAll("<TBODY>", "");
		gridhtml = gridhtml.replaceAll("</TBODY>", "");

		String dateRangeHtml = null;

		UIUtils.generateStatementPDF(this.getTitle(), gridhtml, dateRangeHtml,
				customerId);
	}

	@Override
	public void printPreview() {

	}

	@Override
	public PayeeStatementsList getObject(PayeeStatementsList parent,
			PayeeStatementsList child) {
		return super.getObject(parent, child);
	}

	@Override
	public int sort(PayeeStatementsList obj1, PayeeStatementsList obj2, int col) {
		int ret = UIUtils.compareInt(obj1.getCategory(), obj2.getCategory());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 2:
			int num1 = UIUtils.isInteger(obj1.getTransactionNumber()) ? Integer
					.parseInt(obj1.getTransactionNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getTransactionNumber()) ? Integer
					.parseInt(obj2.getTransactionNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return UIUtils.compareTo(obj1.getTransactionNumber(), obj2
						.getTransactionNumber());

		case 1:
			return UIUtils.compareInt(obj1.getTransactiontype(), obj2
					.getTransactiontype());

		case 3:
			return UIUtils.compareDouble(obj1.getAgeing(), obj2.getAgeing());

		case 0:
			return UIUtils.compareTo(obj1.getTransactionDate(), obj2
					.getTransactionDate());

		case 4:
			return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());

		}
		return 0;
	}

	@Override
	public String getDefaultDateRange() {
		return FinanceApplication.getReportsMessages().all();
	}
}
