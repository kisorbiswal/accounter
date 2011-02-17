package com.vimukti.accounter.web.client.ui.reports;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class VAT100Report extends AbstractReportView<VATSummary> {
	private String sectionName = "";
	private int row = -1;
	protected Double box3amount = 0.0D;
	protected Double box4amount = 0.0D;

	public VAT100Report() {
		super(false, "No records to show");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void init() {
		super.init();
		toolbar.setDateRanageOptions(FinanceApplication.getReportsMessages()
				.all(), FinanceApplication.getReportsMessages().thisWeek(),
				FinanceApplication.getReportsMessages().thisMonth(),
				FinanceApplication.getReportsMessages().lastWeek(),
				FinanceApplication.getReportsMessages().lastMonth(),
				FinanceApplication.getReportsMessages().thisFinancialYear(),
				FinanceApplication.getReportsMessages().lastFinancialYear(),
				FinanceApplication.getReportsMessages().thisFinancialQuarter(),
				FinanceApplication.getReportsMessages().lastFinancialQuarter(),
				FinanceApplication.getReportsMessages().financialYearToDate(),
				FinanceApplication.getReportsMessages().lastVATQuarter(),
				FinanceApplication.getReportsMessages().lastVATQuarterToDate(),
				FinanceApplication.getReportsMessages().custom());

		// Make rpc request for default VAT Agency and default DateRange
		List<ClientTAXAgency> taxAgencies = FinanceApplication.getCompany()
				.getTaxAgencies();
		for (ClientTAXAgency taxAgency : taxAgencies) {
			if (taxAgency.getName().equalsIgnoreCase(
					FinanceApplication.getReportsMessages()
							.hmCustomsExciseVAT())) {
				ClientFinanceDate date = new ClientFinanceDate();
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				ClientFinanceDate startDate = new ClientFinanceDate(date
						.getYear(), startMonth, 1);
				ClientFinanceDate start = startDate;
				ClientFinanceDate end = date;
				makeReportRequest(taxAgency.getStringID(), start, end);
				break;
			}
		}
	}

	@Override
	public void OnRecordClick(VATSummary record) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getColumnData(VATSummary record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getVatReturnEntryName();
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
		return new String[] {
				"",
				UIUtils.getDateByCompanyType(toolbar.getStartDate()) + "-"
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
		return FinanceApplication.getReportsMessages().vat100();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE_VATAGENCY;
	}

	@Override
	public void makeReportRequest(String vatAgency,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		row = -1;
		this.sectionName = "";
		FinanceApplication.createReportService().getVAT100Report(vatAgency,
				startDate.getTime(), endDate.getTime(), this);

	}

	@Override
	protected String getDefaultDateRange() {
		return FinanceApplication.getReportsMessages().lastVATQuarter();
	}

	@Override
	public void processRecord(VATSummary record) {
		if (this.handler == null)
			iniHandler();
		if (this.row == -1) {
			this.sectionName = "";
			addSection("", FinanceApplication.getReportsMessages()
					.box5NetVATToPayOrReclaimIfNegative(), new int[] {});

			this.sectionName = FinanceApplication.getReportsMessages().vatDue();
			addSection(this.sectionName, FinanceApplication
					.getReportsMessages().box3TotalVATDue(), new int[] { 1 });
			row = 0;
		} else if (this.row < 4) {
			row = row + 1;
			if (row == 3) {
				box4amount = record.getValue();
				// end vat Due section
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
				if (section.footer.equals(FinanceApplication
						.getReportsMessages()
						.box5NetVATToPayOrReclaimIfNegative())) {
					section.data[0] = "";
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			public void OnSectionEnd(
					com.vimukti.accounter.web.client.ui.reports.AbstractReportView.Section section) {

				if (section.footer.equals(FinanceApplication
						.getReportsMessages().box3TotalVATDue())) {
					box3amount = Double.valueOf(section.data[1].toString());
				}
				if (section.footer.equals(FinanceApplication
						.getReportsMessages()
						.box5NetVATToPayOrReclaimIfNegative())) {
					section.data[1] = box3amount - box4amount;
					sectionDepth = 1;
				}

			}
		};

	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

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

		if (UIUtils.isMSIEBrowser()) {
			printDataForIEBrowser();
		} else
			printDataForOtherBrowser();
	}

	private void printDataForOtherBrowser() {
		String gridhtml = grid.toString();
		String headerhtml = grid.getHeader();

		gridhtml = gridhtml.replaceAll(headerhtml, "");

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

		UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}

	private void printDataForIEBrowser() {
		String gridhtml = grid.toString();
		String headerhtml = grid.getHeader();
		gridhtml = gridhtml.replaceAll("\r\n", "");
		headerhtml = headerhtml.replaceAll("\r\n", "");

		gridhtml = gridhtml.replaceAll(headerhtml, "");

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
		// return obj1.getName().compareTo(obj2.getName());
		// case 1:
		// return UIUtils.compareDouble(obj1.getValue(), obj2.getValue());
		// }
		return 0;
	}

	@Override
	public void resetVariables() {
		this.sectionName = "";
		this.row = -1;
		sectionDepth = 0;
	}

	@Override
	protected int getColumnWidth(int index) {
		if (index == 1)
			return 175;
		else
			return -1;
	}
}
