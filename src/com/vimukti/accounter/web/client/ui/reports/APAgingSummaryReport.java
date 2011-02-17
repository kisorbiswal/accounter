package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.DummyDebitor;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ReportsActionFactory;

public class APAgingSummaryReport extends AbstractReportView<DummyDebitor> {

	@Override
	public void OnRecordClick(DummyDebitor record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record, ReportsActionFactory
				.getAorpAgingDetailAction());

	}

	@Override
	public Object getColumnData(DummyDebitor record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getDebitorName();
		case 1:
			return record.getDebitdays_in30() + record.getDebitdays_incurrent();
		case 2:
			return record.getDebitdays_in60();
		case 3:
			return record.getDebitdays_in90();
		case 4:
			return record.getDebitdays_inolder();
		case 5:
			return (record.getDebitdays_in30() + record.getDebitdays_in60()
					+ record.getDebitdays_in90()
					+ record.getDebitdays_inolder() + record
					.getDebitdays_incurrent());
		}

		return null;
	}

	@Override
	public int[] getColumnTypes() {

		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {

		return new String[] {
				FinanceApplication.getReportsMessages().creditor(),
				FinanceApplication.getReportsMessages().days30(),
				FinanceApplication.getReportsMessages().days60(),
				FinanceApplication.getReportsMessages().days90(),
				FinanceApplication.getReportsMessages().older(),
				FinanceApplication.getReportsMessages().totalBalance() };
	}

	@Override
	public String getTitle() {
		return FinanceApplication.getReportsMessages().APAgingSummary();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_AS_OF;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		FinanceApplication.createReportService().getCreditors(start.getTime(),
				new ClientFinanceDate().getTime(), this);

	}

	@Override
	public void processRecord(DummyDebitor record) {
		if (sectionDepth == 0) {
			addSection("", FinanceApplication.getReportsMessages().total(),
					new int[] { 1, 2, 3, 4, 5 });
		} else if (sectionDepth == 1) {
			return;
		}
		// Go on recursive calling if we reached this place
		processRecord(record);

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

		String dateRangeHtml = null;

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

		String dateRangeHtml = null;

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
	public ClientFinanceDate getEndDate(DummyDebitor obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(DummyDebitor obj) {
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
		if (index == 0)
			return 300;
		return -1;
	}

	public int sort(DummyDebitor obj1, DummyDebitor obj2, int col) {
		switch (col) {
		case 0:
			return obj1.getDebitorName().toLowerCase().compareTo(
					obj2.getDebitorName().toLowerCase());

		case 1:
			return UIUtils.compareDouble((obj1.getDebitdays_in30() + obj1
					.getDebitdays_incurrent()),
					(obj2.getDebitdays_in30() + obj2.getDebitdays_incurrent()));
		case 2:
			return UIUtils.compareDouble(obj1.getDebitdays_in60(), obj2
					.getDebitdays_in60());
		case 3:
			return UIUtils.compareDouble(obj1.getDebitdays_in90(), obj2
					.getDebitdays_in90());
		case 4:
			return UIUtils.compareDouble(obj1.getDebitdays_inolder(), obj2
					.getDebitdays_inolder());
		case 5:
			return UIUtils.compareDouble((obj1.getDebitdays_in30()
					+ obj1.getDebitdays_in60() + obj1.getDebitdays_in90()
					+ obj1.getDebitdays_inolder() + obj1
					.getDebitdays_incurrent()), (obj2.getDebitdays_in30()
					+ obj2.getDebitdays_in60() + obj2.getDebitdays_in90()
					+ obj2.getDebitdays_inolder() + obj2
					.getDebitdays_incurrent()));
		}
		return 0;
	}

	@Override
	public DummyDebitor getObject(DummyDebitor parent, DummyDebitor child) {
		// TODO Auto-generated method stub
		return null;
	}
}
