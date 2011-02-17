package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.VATItemDetail;
import com.vimukti.accounter.web.client.core.reports.VATItemSummary;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class VATItemDetailReport extends AbstractReportView<VATItemDetail> {
	private String sectionName;
	private String name;

	public VATItemDetailReport() {
		super();
	}

	@Override
	public void OnRecordClick(VATItemDetail record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getTransactionType(), record
				.getTransactionId()
				+ "");
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public Object getColumnData(VATItemDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Utility.getTransactionName(record.getTransactionType());
		case 1:
			return UIUtils.getDateByCompanyType(record.getDate());
		case 2:
			return record.getTransactionNumber();
		case 3:
			return record.getName();
		case 4:
			return record.getMemo();
		case 5:
			return record.getAmount();
		case 6:
			return record.getSalesPrice();

		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { FinanceApplication.getReportsMessages().type(),
				FinanceApplication.getReportsMessages().clientFinanceDate(),
				FinanceApplication.getReportsMessages().number(),
				FinanceApplication.getReportsMessages().name(),
				FinanceApplication.getReportsMessages().memo(),
				FinanceApplication.getReportsMessages().amount(),
				FinanceApplication.getReportsMessages().salesPrice() };
	}

	@Override
	public ClientFinanceDate getEndDate(VATItemDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(VATItemDetail obj) {
		return obj.getStartDate();
	}

	@Override
	public String getTitle() {
		return FinanceApplication.getReportsMessages().vatItemDetail();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	protected int getColumnWidth(int index) {
		if (index == 2)
			return 65;
		else if (index == 3)
			return 150;
		else if (index == 4)
			return 100;
		else if (index == 1)
			return 85;
		else if (index == 5 || index == 6)
			return 125;
		return 210;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		VATItemSummary itemList = (VATItemSummary) data;
		FinanceApplication.createReportService().getVATItemDetailReport(
				itemList.getName(), start.getTime(), end.getTime(), this);

	}

	@Override
	public void processRecord(VATItemDetail record) {
		VATItemSummary itemList = (VATItemSummary) data;
		if (sectionDepth == 0) {
			addSection("", FinanceApplication.getReportsMessages().total(),
					new int[] { 5 });
		} else if (sectionDepth == 1) {
			this.sectionName = itemList.getName();
			this.name = record.getName();
			addSection(sectionName, "Total", new int[] { 5 });
		} else if (sectionDepth == 2) {
			// No need to do anything, just allow adding this record
			if (!name.equals(record.getName())) {
				endSection();
			} else {
				return;
			}
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
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	public int sort(VATItemDetail obj1, VATItemDetail obj2, int col) {
		int ret = obj1.getName().compareTo(obj2.getName());
		if (ret != 0) {
			return ret;
		}

		switch (col) {
		case 0:
			return UIUtils.compareInt(obj1.getTransactionType(), obj2
					.getTransactionType());
		case 1:
			return obj1.getDate().compareTo(obj2.getDate());
		case 2:
			return obj1.getTransactionNumber().compareTo(
					obj2.getTransactionNumber());
		case 3:
			return obj1.getName().compareTo(obj2.getName());
		case 4:
			return obj1.getMemo().compareTo(obj2.getMemo());
		case 5:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		case 6:
			return UIUtils.compareDouble(obj1.getSalesPrice(), obj2
					.getSalesPrice());
		}
		return 0;
	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
		this.sectionName = "";
	}

	@Override
	public boolean isWiderReport() {
		return true;
	}

}
