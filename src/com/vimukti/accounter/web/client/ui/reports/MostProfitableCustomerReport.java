package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.MostProfitableCustomers;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;

/**
 * 
 * @author kumar kasimala
 * 
 * 
 */
public class MostProfitableCustomerReport extends
		AbstractReportView<MostProfitableCustomers> {

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_PERCENTAGE };
	}

	@Override
	public String[] getColunms() {
		return new String[] {
				FinanceApplication.getReportsMessages().customer(),
				FinanceApplication.getReportsMessages().invoicedAmount(),
				FinanceApplication.getReportsMessages().cost(),
				FinanceApplication.getReportsMessages().dollarMargin(),
				FinanceApplication.getReportsMessages().percMargin() };
	}

	@Override
	public String getTitle() {
		return FinanceApplication.getReportsMessages()
				.mostProfitableCustomers();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		FinanceApplication.createReportService().getMostProfitableCustomers(
				start.getTime(), end.getTime(), this);
	}

	@Override
	public void OnRecordClick(MostProfitableCustomers record) {
		// nothing to do
	}

	@Override
	public Object getColumnData(MostProfitableCustomers record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getCustomer();
		case 1:
			return record.getInvoicedAmount();
		case 2:
			return record.getCost();
		case 3:
			return record.getMargin();
		case 4:
			return record.getMarginPercentage();
		}
		return null;
	}

	@Override
	public void processRecord(MostProfitableCustomers record) {
		if (sectionDepth == 0) {
			addSection("", FinanceApplication.getReportsMessages().total(),
					new int[] { 1, 2, 3, 4 });
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

		gridhtml = gridhtml.replace(headerhtml, "");
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
	public ClientFinanceDate getEndDate(MostProfitableCustomers obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(MostProfitableCustomers obj) {
		return obj.getStartDate();
	}

	@Override
	protected int getColumnWidth(int index) {
		if (index == 0)
			return 200;
		if (index == 1 || index == 2 || index == 3 || index == 4)
			return 150;

		if (index == 0)
			return 200;
		if (index == 1 || index == 2 || index == 3 || index == 4)
			return 150;
		else
			return -1;
	}

	public int sort(MostProfitableCustomers obj1, MostProfitableCustomers obj2,
			int col) {
		switch (col) {
		case 0:
			return obj1.getCustomer().toLowerCase().compareTo(
					obj2.getCustomer().toLowerCase());
		case 1:
			return UIUtils.compareDouble(obj1.getInvoicedAmount(), obj2
					.getInvoicedAmount());
		case 2:
			return UIUtils.compareDouble(obj1.getCost(), obj2.getCost());
		case 3:
			return UIUtils.compareDouble(obj1.getMargin(), obj2.getMargin());
		case 4:
			return UIUtils.compareDouble(obj1.getMarginPercentage(), obj2
					.getMarginPercentage());
		}
		return 0;
	}

}
