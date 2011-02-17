package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.UncategorisedAmountsReport;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class VATUncategorisedAmountsReport extends
		AbstractReportView<UncategorisedAmountsReport> {

	private double balance = 0.0;

	public VATUncategorisedAmountsReport() {
		super();
	}

	@Override
	public void OnRecordClick(UncategorisedAmountsReport record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getTransactionType(), record
				.getStringId()
				+ "");
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public Object getColumnData(UncategorisedAmountsReport record,
			int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Utility.getTransactionName(record.getTransactionType());
		case 1:
			if (record.getDate() != null)
				return UIUtils.getDateByCompanyType(record.getDate());
			else
				break;
		case 2:
			return record.getTransactionNumber();
		case 3:
			return record.getSourceName();
			// case 4:
			// return record.getMemo();
		case 4:
			balance += record.getAmount();
			return record.getAmount();
		case 5:
			return balance;
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { FinanceApplication.getReportsMessages().type(),
				FinanceApplication.getReportsMessages().clientFinanceDate(),
				FinanceApplication.getReportsMessages().no(),
				FinanceApplication.getReportsMessages().sourceName(),
				FinanceApplication.getReportsMessages().amount(),
				FinanceApplication.getReportsMessages().balance() };
	}

	@Override
	public String getTitle() {
		return FinanceApplication.getReportsMessages()
				.unCategorisedVATAmountsDetail();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	protected int getColumnWidth(int index) {

		if (index == 2 || index == 1)
			return 68;
		else if (index == 3)
			return 200;
		else if (index == 4 || index == 5)
			return 125;
		else
			return 200;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		balance = 0.0;
		FinanceApplication.createReportService().getUncategorisedAmountsReport(
				start.getTime(), end.getTime(), this);

	}

	@Override
	public void processRecord(UncategorisedAmountsReport record) {
		if (sectionDepth == 0) {
			addSection(FinanceApplication.getReportsMessages()
					.unCategorisedTaxAmounts(), FinanceApplication
					.getReportsMessages().unCategorisedTaxAmounts(),
					new int[] { 5 });
		} else if (sectionDepth == 1) {
			return;
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
	public ClientFinanceDate getEndDate(UncategorisedAmountsReport obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(UncategorisedAmountsReport obj) {
		return obj.getStartDate();
	}

	@Override
	public int sort(UncategorisedAmountsReport obj1,
			UncategorisedAmountsReport obj2, int col) {

		switch (col) {
		case 0:
			return UIUtils.compareInt(obj1.getTransactionType(), obj2
					.getTransactionType());
		case 1:
			return obj1.getDate().compareTo(obj2.getDate());
		case 2:
			return UIUtils.compareInt(Integer.parseInt(obj1
					.getTransactionNumber()), Integer.parseInt(obj2
					.getTransactionNumber()));
		case 3:
			return obj1.getSourceName().toLowerCase().compareTo(
					obj2.getSourceName().toLowerCase());
		case 4:
			return obj1.getMemo().toLowerCase().compareTo(
					obj2.getMemo().toLowerCase());
		case 5:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		}
		return 0;
	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
		this.balance = 0;
	}

}
