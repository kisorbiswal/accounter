package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class PurchaseByVendorDetailReport extends
		AbstractReportView<SalesByCustomerDetail> {

	private String sectionName = "";

	@Override
	public void OnRecordClick(SalesByCustomerDetail record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(record.getType(), record
				.getTransactionId());
	}

	@Override
	public Object getColumnData(SalesByCustomerDetail record, int columnIndex) {
		switch (columnIndex) {
		case 2:
			return Utility.getTransactionName(record.getType());
		case 1:
			return UIUtils.getDateByCompanyType(record.getDate());
		case 3:
			return record.getNumber();
		case 0:
			return "";
			// case 4:
			// return record.getDueDate();
		case 4:
			return record.getAmount();
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] {
				UIUtils.getVendorString(FinanceApplication.getVendorsMessages()
						.supplier(), FinanceApplication.getVendorsMessages()
						.vendor()),
				FinanceApplication.getReportsMessages().date(),
				FinanceApplication.getReportsMessages().type(),
				FinanceApplication.getReportsMessages().num(),
				// FinanceApplication.getReportsMessages().dueDate(),
				FinanceApplication.getReportsMessages().amount() };
	}

	@Override
	public String getTitle() {
		return UIUtils.getVendorString(FinanceApplication.getVendorsMessages()
				.purchaseBySupplierDetail(), FinanceApplication
				.getVendorsMessages().purchaseByVendorDetail());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	protected int getColumnWidth(int index) {
		// if (index == 0)
		// return 180;
		if (index == 1)
			return 70;
		else if (index == 4)
			return 160;
		else if (index == 0)
			return 200;
		else if (index == 3)
			return 100;
		else
			return 200;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		SalesByCustomerDetail byCustomerDetail = (SalesByCustomerDetail) this.data;
		if (byCustomerDetail == null) {
			FinanceApplication.createReportService()
					.getPurchasesByVendorDetail(start.getTime(), end.getTime(),
							this);
		} else if (byCustomerDetail.getName() != null) {
			FinanceApplication.createReportService()
					.getPurchasesByVendorDetail(byCustomerDetail.getName(),
							start.getTime(), end.getTime(), this);
		}
	}

	@Override
	public void processRecord(SalesByCustomerDetail record) {
		if (sectionDepth == 0) {
			addSection(new String[] { "", "" }, new String[] { "", "", "",
					FinanceApplication.getReportsMessages().total() },
					new int[] { 4 });
		} else if (sectionDepth == 1) {
			this.sectionName = record.getName();
			addSection(new String[] { sectionName }, new String[] { "", "", "",
					FinanceApplication.getReportsMessages().total() },
					new int[] { 4 });
		} else if (sectionDepth == 2) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getName())) {
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
	public ClientFinanceDate getEndDate(SalesByCustomerDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(SalesByCustomerDetail obj) {
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
	public int sort(SalesByCustomerDetail obj1, SalesByCustomerDetail obj2,
			int col) {

		int ret = obj1.getName().toLowerCase().compareTo(
				obj2.getName().toLowerCase());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 2:
			return UIUtils.compareInt(obj1.getType(), obj2.getType());
		case 1:
			return obj1.getDate().compareTo(obj2.getDate());
		case 3:
			return UIUtils.compareInt(Integer.parseInt(obj1.getNumber()),
					Integer.parseInt(obj2.getNumber()));
		case 0:
			return obj1.getName().toLowerCase().compareTo(
					obj2.getName().toLowerCase());
			// case 4:
			// return obj1.getDueDate().compareTo(obj2.getDueDate());
		case 4:
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
