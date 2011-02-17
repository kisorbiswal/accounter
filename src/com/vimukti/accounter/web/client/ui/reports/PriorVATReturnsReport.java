package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class PriorVATReturnsReport extends AbstractReportView<VATSummary> {

	private String sectionName = "";
	private int row = -1;
	protected Double box3amount = 0.0D;
	protected Double box4amount = 0.0D;

	public PriorVATReturnsReport() {
		super(false, FinanceApplication.getReportsMessages()
				.pleaseSelectVATAgencyAndEndingDateToViewReport());
		isVATSummaryReport = true;
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void OnRecordClick(VATSummary record) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getColumnData(VATSummary record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getName();
		case 1:
			return record.getValue();
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {

		// if (toolbar.getStartDate().getTime() == 0
		// || toolbar.getEndDate().getTime() == 0) {
		return new String[] { "", " " };
		// }
		// return new String[] {
		// "",
		// UIUtils.dateFormat(toolbar.getStartDate()) + "-"
		// + UIUtils.getDateByCompanyType(toolbar.getEndDate()) };
	}

	@Override
	protected String[] getDynamicHeaders() {

		return new String[] { "",
				UIUtils.getDateByCompanyType(toolbar.getEndDate()) };

		// return new String[] {
		// "",
		// UIUtils.getDateByCompanyType(toolbar.getStartDate()) + "-"
		// + UIUtils.getDateByCompanyType(toolbar.getEndDate()) };

	}

	@Override
	public String getTitle() {
		return FinanceApplication.getReportsMessages().priorVATReturns();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_PRIOR_VATRETURN;
	}

	@Override
	public void makeReportRequest(String vatAgency, ClientFinanceDate end) {
		this.row = -1;
		FinanceApplication.createReportService().getPriorReturnVATSummary(
				vatAgency, end.getTime(), this);

	}

	@Override
	public void processRecord(VATSummary record) {
		if (this.handler == null)
			iniHandler();
		if (this.row == -1) {
			this.sectionName = "";
			addSection("", FinanceApplication.getReportsMessages()
					.netVATToPayOrReclaimIfNegativeBOX5(), new int[] {});

			this.sectionName = FinanceApplication.getReportsMessages().vatDue();
			addSection(this.sectionName, FinanceApplication
					.getReportsMessages().totalVATDueBOX3(), new int[] { 1 });
			row = 0;
		} else if (this.row < 4) {
			row = row + 1;
			if (row == 3) {
				// end vat Due section
				box4amount = record.getValue();
				endSection();
				return;
			}
			if (row == 4) {
				// end net VAT pay section
				endSection();
			}

			return;
		} else {
			return;
		}
		// Go on recursive calling if we reached this place
		processRecord(record);

	}

	protected void iniHandler() {

		this.handler = new ISectionHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void OnSectionAdd(
					com.vimukti.accounter.web.client.ui.reports.AbstractReportView.Section section) {
				if (section.title == FinanceApplication.getReportsMessages()
						.netVATToPayOrReclaimIfNegativeBOX5()) {
					section.data[0] = "";
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			public void OnSectionEnd(
					com.vimukti.accounter.web.client.ui.reports.AbstractReportView.Section section) {

				if (section.footer.equals(FinanceApplication
						.getReportsMessages().totalVATDueBOX3())) {
					box3amount = Double.valueOf(section.data[1].toString());
				}
				if (section.footer.equals(FinanceApplication
						.getReportsMessages()
						.netVATToPayOrReclaimIfNegativeBOX5())) {
					section.data[1] = box3amount - box4amount;
				}

			}
		};

	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

		removeLoadingImage();

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

		gridhtml = gridhtml.replaceAll(firsRow, headerhtml);
		gridhtml = gridhtml.replaceAll("<tbody>", "");
		gridhtml = gridhtml.replaceAll("</tbody>", "");

		String dateRangeHtml = null;

		UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public ClientFinanceDate getEndDate(VATSummary obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(VATSummary obj) {
		return obj.getStartDate();
	}

	@Override
	public int sort(VATSummary obj1, VATSummary obj2, int col) {

		// switch (col) {
		// case 0:
		// return obj1.getName().toLowerCase().compareTo(
		// obj2.getName().toLowerCase());
		// case 1:
		// return UIUtils.compareDouble(obj1.getValue(), obj2.getValue());
		// }
		return 0;
	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
		this.sectionName = "";
		this.row = -1;
	}

	@Override
	protected int getColumnWidth(int index) {
		if (index == 1) {
			return 200;
		}
		return -1;
	}

}
