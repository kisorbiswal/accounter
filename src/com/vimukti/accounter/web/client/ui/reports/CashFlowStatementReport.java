package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ReportsActionFactory;

public class CashFlowStatementReport extends AbstractReportView<TrialBalance> {
	private List<String> types = new ArrayList<String>();
	private List<String> sectiontypes = new ArrayList<String>();
	private String curentParent;
	@SuppressWarnings("unused")
	private Double netIncome;
	private double cashBegigginperiod;

	@Override
	public void OnRecordClick(TrialBalance record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record, ReportsActionFactory
				.getTransactionDetailByAccountAction());
	}

	@Override
	protected String getDefaultDateRange() {
		return FinanceApplication.getReportsMessages().financialYearToDate();
	}

	@Override
	public Object getColumnData(TrialBalance record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getAccountNumber() != null ? record
					.getAccountNumber()
					+ "-" + record.getAccountName() : ""
					+ record.getAccountName();
		case 1:
			return record.getAmount();
		default:
			return null;
		}

	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		// String startDate = DateTimeFormat.getFormat("MM/DD/YY").format(
		// toolbar.getStartDate());
		// String endDate = DateTimeFormat.getFormat("MM/DD/YY").format(
		// toolbar.getEndDate());
		// return new String[] { "", startDate + " - " + endDate };
		return new String[] {
				"",
				UIUtils.getDateByCompanyType(toolbar.getStartDate()) + "  -  "
						+ UIUtils.getDateByCompanyType(toolbar.getEndDate()) };
	}

	@Override
	protected String[] getDynamicHeaders() {
		return new String[] {
				"",
				UIUtils.getDateByCompanyType(toolbar.getStartDate()) + "  -  "
						+ UIUtils.getDateByCompanyType(toolbar.getEndDate()) };
	}

	@Override
	public String getTitle() {
		// return FinanceApplication.getReportsMessages().profitAndLoss();
		return FinanceApplication.getReportsMessages().cashFlowReport();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		FinanceApplication.createReportService().getCashFlowReport(
				start.getTime(), end.getTime(), this);

	}

	@Override
	public void processRecord(TrialBalance record) {
		if (sectionDepth == 0) {
			addTypeSection(" ", FinanceApplication.getReportsMessages()
					.cashAtEndOfPeriod());
		}
		if (record.getAccountName().equals(
				FinanceApplication.getReportsMessages().netIncome())) {
			addOperatingTypes(record);
			this.netIncome = record.getAmount();
			return;
		}
		cashBegigginperiod = record.getCashAtBeginningOfPeriod();
		if (this.handler == null)
			iniHandler();

		if (record.getCashFlowCategory() == ClientAccount.CASH_FLOW_CATEGORY_OPERATING) {
			addOperatingTypes(record);
		} else if (record.getCashFlowCategory() == ClientAccount.CASH_FLOW_CATEGORY_FINANCING) {
			addFinancingTypes(record);
		} else if (record.getCashFlowCategory() == ClientAccount.CASH_FLOW_CATEGORY_INVESTING) {
			addInvestingTypes(record);
		}
		if (closePrevSection(record.getParentAccount() == null ? record
				.getAccountName() : getAccountNameById(record
				.getParentAccount()))) {
			processRecord(record);
		} else {
			addSection(record);
			return;
		}

	}

	private void iniHandler() {
		this.handler = new ISectionHandler() {

			private Double totaloperating = 0.0D;
			private Double totalInvesting = 0.0D;
			private Double totalFinaning = 0.0D;

			@SuppressWarnings("unchecked")
			@Override
			public void OnSectionAdd(
					com.vimukti.accounter.web.client.ui.reports.AbstractReportView.Section section) {

			}

			@SuppressWarnings("unchecked")
			@Override
			public void OnSectionEnd(
					com.vimukti.accounter.web.client.ui.reports.AbstractReportView.Section section) {

				// if (section.footer == "AR-NI-NC") {
				// section.data[1] = netIncome
				// + +Double.valueOf(section.data[1].toString());
				// }
				if (section.title == FinanceApplication.getReportsMessages()
						.operatingActivities()) {
					totaloperating = Double.valueOf(section.data[1].toString());
				}
				if (section.title == FinanceApplication.getReportsMessages()
						.investingActivities()) {
					totalInvesting = Double.valueOf(section.data[1].toString());
				}
				if (section.title == FinanceApplication.getReportsMessages()
						.financingActivities()) {
					totalFinaning = Double.valueOf(section.data[1].toString());
				}
				if (section.footer == FinanceApplication.getReportsMessages()
						.cashAtEndOfPeriod()) {
					CashFlowStatementReport.this.grid
							.addRow(
									null,
									1,
									new Object[] {
											FinanceApplication
													.getReportsMessages()
													.netCashChangeForThePeriod(),
											(totalFinaning + totaloperating + totalInvesting) },
									true, true, true);
					CashFlowStatementReport.this.grid.addRow(null, 1,
							new Object[] {
									FinanceApplication.getReportsMessages()
											.cashAtBeginningOfThePeriod(),
									cashBegigginperiod }, true, true, true);
					section.data[1] = (totalFinaning + totaloperating
							+ totalInvesting + cashBegigginperiod);

				}
			}
		};

	}

	public String getAccountNameById(String id) {
		for (TrialBalance balance : this.records)
			if (balance.getAccountId() != null)
				if (balance.getAccountId().equals(id))
					return balance.getAccountName();
		return null;
	}

	public void closeAllSection() {
		for (int i = types.size() - 1; i > 0; i--) {
			closeSection(i);
		}
	}

	public void closeOtherSections() {
		for (int i = types.size() - 1; i > 0; i--) {
			closePrevSection(types.get(i));
		}
	}

	public void addOperatingTypes(TrialBalance record) {
		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.operatingActivities())) {
			closeAllSection();
			addTypeSection(FinanceApplication.getReportsMessages()
					.operatingActivities(), FinanceApplication
					.getReportsMessages()
					.netCashProvidedByOperatingActivities());
		}
		if (record.getAccountName().equals(
				FinanceApplication.getReportsMessages().netIncome()))
			return;
		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.adjustmentsToReconcileNetIncomeToNetCash())) {
			addTypeSection(FinanceApplication.getReportsMessages()
					.adjustmentsToReconcileNetIncomeToNetCash(),
					FinanceApplication.getReportsMessages().endOfARNINC());
		}

	}

	public void addInvestingTypes(TrialBalance record) {
		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.investingActivities())) {
			closeAllSection();
			addTypeSection(FinanceApplication.getReportsMessages()
					.investingActivities(), FinanceApplication
					.getReportsMessages()
					.netCashProvidedByinvestingActivities());
		}
	}

	public void addFinancingTypes(TrialBalance record) {
		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.financingActivities())) {
			closeAllSection();
			addTypeSection(FinanceApplication.getReportsMessages()
					.financingActivities(), FinanceApplication
					.getReportsMessages()
					.netCashProvidedByfinancingActivities());
		}
	}

	public boolean isParent(TrialBalance record) {
		for (TrialBalance balance : this.records) {
			if (balance.getParentAccount() != null)
				if (balance.getParentAccount().equals(record.getAccountId()))
					return true;
		}
		return false;
	}

	public boolean addSection(TrialBalance record) {
		if (isParent(record)) {
			types.add(record.getAccountName());
			curentParent = record.getAccountName();
			addSection(record.getAccountNumber() + "-"
					+ record.getAccountName(), FinanceApplication
					.getReportsMessages().total()
					+ record.getAccountName(), new int[] { 1 });
			return true;
		}
		return false;
	}

	public boolean closePrevSection(String title) {
		if (curentParent != null && curentParent != "")
			if (!title.equals(curentParent)) {
				if (!sectiontypes.contains(curentParent)) {
					types.remove(types.size() - 1);
					if (types.size() > 0) {
						curentParent = types.get(types.size() - 1);
					}
					endSection();
					return true;
				}
			}
		return false;
	}

	public void closeSection(int index) {
		types.remove(index);
		curentParent = "";
		endSection();
	}

	/**
	 * add Type Section
	 * 
	 * @param title
	 */
	public void addTypeSection(String title, String bottomTitle) {
		if (!sectiontypes.contains(title)) {
			addSection(title, bottomTitle, new int[] { 1 });
			types.add(title);
			sectiontypes.add(title);
		}
	}

	public boolean isExist(String title) {
		if (!sectiontypes.contains(title))
			return true;
		return false;
	}

	/**
	 * Reset Variables in Report,
	 */
	@Override
	public void resetVariables() {
		this.types.clear();
		this.sectiontypes.clear();
		curentParent = "";
		;
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

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public ClientFinanceDate getEndDate(TrialBalance obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(TrialBalance obj) {
		return obj.getStartDate();

	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((TrialBalance) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((TrialBalance) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((TrialBalance) object).getEndDate();
	}

	@Override
	protected int getColumnWidth(int index) {
		if (index == 1)
			return 400;
		else
			return -1;
	}

	private void printDataForIEBrowser() {
		String gridhtml = grid.toString();
		String headerhtml = grid.getHeader();
		String footerhtml = grid.getFooter();

		gridhtml = gridhtml.replaceAll("\r\n", "");
		headerhtml = headerhtml.replaceAll("\r\n", "");
		footerhtml = footerhtml.replaceAll("\r\n", "");

		gridhtml = gridhtml.replaceAll(headerhtml, "");
		gridhtml = gridhtml.replaceAll(footerhtml, "");
		headerhtml = headerhtml.replaceAll("TD", "TH");
		headerhtml = headerhtml.substring(headerhtml.indexOf("<TR "),
				headerhtml.indexOf("</TBODY>"));
		footerhtml = footerhtml.substring(footerhtml.indexOf("<TR>"),
				footerhtml.indexOf("</TBODY"));
		footerhtml = footerhtml.replaceAll("<TR>", "<TR class=listgridfooter>");

		String firsRow = "<TR class=ReportGridRow>"
				+ grid.rowFormatter.getElement(0).getInnerHTML() + "</TR>";
		String lastRow = "<TR class=ReportGridRow>"
				+ grid.rowFormatter.getElement(grid.getRowCount() - 1)
						.getInnerHTML() + "</TR>";

		firsRow = firsRow.replaceAll("\r\n", "");
		lastRow = lastRow.replaceAll("\r\n", "");

		headerhtml = headerhtml + firsRow;
		footerhtml = lastRow + footerhtml;

		gridhtml = gridhtml.replace(firsRow, headerhtml);
		gridhtml = gridhtml.replace(lastRow, footerhtml);
		gridhtml = gridhtml.replaceAll("<TBODY>", "");
		gridhtml = gridhtml.replaceAll("</TBODY>", "");

		String dateRangeHtml = null;

		UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}

	private void printDataForOtherBrowser() {
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

		String dateRangeHtml = null;

		UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}
}