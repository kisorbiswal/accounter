package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.ECSalesList;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ReportsActionFactory;

public class ECSalesListReport extends AbstractReportView<ECSalesList> {

	@SuppressWarnings("unused")
	private String sectionName;

	@Override
	public void OnRecordClick(ECSalesList record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsActionFactory.getECSalesListDetailAction().run(record, true);
	}

	@Override
	public Object getColumnData(ECSalesList record, int columnIndex) {
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
		if (toolbar.getStartDate().getTime() == 0
				|| toolbar.getEndDate().getTime() == 0) {
			return new String[] { "", " " + "-" + " " };
		}
		return new String[] {
				"",
				UIUtils.dateFormat(toolbar.getStartDate()) + "-"
						+ UIUtils.getDateByCompanyType(toolbar.getEndDate()) };
	}

	@Override
	protected String[] getDynamicHeaders() {
		return new String[] {
				"",
				UIUtils.getDateByCompanyType(toolbar.getStartDate()) + "-"
						+ UIUtils.getDateByCompanyType(toolbar.getEndDate()) };
	}

	@Override
	public String getTitle() {
		return FinanceApplication.getReportsMessages().ecSalesList();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		FinanceApplication.createReportService().getECSalesListReport(
				start.getTime(), end.getTime(), this);

	}

	@Override
	public void processRecord(ECSalesList record) {
		if (sectionDepth == 0) {
			addSection("", FinanceApplication.getReportsMessages().total(),
					new int[] { 1 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record

			return;
		}
		// Go on recursive calling if we reached this place
		// processRecord(record);

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

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public ClientFinanceDate getEndDate(ECSalesList obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(ECSalesList obj) {
		return obj.getStartDate();
	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((ECSalesList) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((ECSalesList) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((ECSalesList) object).getEndDate();
	}

	@Override
	public int sort(ECSalesList obj1, ECSalesList obj2, int col) {
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
		this.sectionName = "";
	}

}
