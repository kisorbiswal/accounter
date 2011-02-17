package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ReportsActionFactory;

public class ProfitAndLossReport extends AbstractReportView<TrialBalance> {
	protected List<String> types = new ArrayList<String>();
	protected List<String> sectiontypes = new ArrayList<String>();
	private String curentParent;

	protected Double totalincome = 0.0D;
	protected Double totalCGOS = 0.0D;
	protected Double grosProft = 0.0D;
	protected Double totalexpese = 0.0D;
	protected Double netIncome = 0.0D;
	protected Double otherIncome = 0.0D;
	protected Double otherExpense = 0.0D;
	protected Double otherNetIncome = 0.0D;

	protected Double totalincome2 = 0.0D;
	protected Double totalCGOS2 = 0.0D;
	protected Double grosProft2 = 0.0D;
	protected Double totalexpese2 = 0.0D;
	protected Double netIncome2 = 0.0D;
	protected Double otherIncome2 = 0.0D;
	protected Double otherExpense2 = 0.0D;
	protected Double otherNetIncome2 = 0.0D;

	public ProfitAndLossReport() {
		this.columnstoHide.add(3);
		this.columnstoHide.add(5);
	}

	@Override
	public void OnRecordClick(TrialBalance record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record, ReportsActionFactory
				.getTransactionDetailByAccountAction());
	}

	@Override
	public Object getColumnData(TrialBalance record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getAccountNumber();
		case 1:
			return record.getAccountName();
		case 2:
			return record.getAmount();
		case 3:
			return record.getAmount();
		case 4:
			return record.getTotalAmount();
		case 5:
			return record.getTotalAmount();
		default:
			return null;
		}

	}

	@Override
	protected String getDefaultDateRange() {
		return FinanceApplication.getReportsMessages().thisMonth();
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] {
				FinanceApplication.getReportsMessages().categoryNumber(),
				"",
				"" + UIUtils.getDateByCompanyType(toolbar.getStartDate())
						+ " - "
						+ UIUtils.getDateByCompanyType(toolbar.getEndDate()),
				"",
				UIUtils.getDateByCompanyType(getLastMonthStartDate())
						+ " - "
						+ UIUtils.getDateByCompanyType(getLastMonth(toolbar
								.getEndDate())),

				"" };
	}

	@Override
	protected String[] getDynamicHeaders() {

		return new String[] {
				FinanceApplication.getReportsMessages().categoryNumber(),
				"",
				"" + UIUtils.getDateByCompanyType(toolbar.getStartDate())
						+ " - "
						+ UIUtils.getDateByCompanyType(toolbar.getEndDate()),
				"",
				UIUtils.getDateByCompanyType(getLastMonthStartDate())
						+ " - "
						+ UIUtils.getDateByCompanyType(getLastMonth(toolbar
								.getEndDate())),

				"" };
	}

	@Override
	public String getTitle() {
		return FinanceApplication.getReportsMessages().profitAndLoss();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		FinanceApplication.createReportService().getProfitAndLossReport(
				start.getTime(), end.getTime(), this);
		initValues();

	}

	@Override
	public void processRecord(TrialBalance record) {
		if (this.handler == null)
			iniHandler();

		if (sectionDepth == 0) {
			addTypeSection("", FinanceApplication.getReportsMessages()
					.netOrdinaryIncome());
		}
		addOrdinaryIncomeOrExpenseTypes(record);
		// if (record.getBaseType() ==
		// ClientAccount.BASETYPE_ORDINARY_INCOME_OR_EXPENSE) {
		// addOrdinaryIncomeOrExpenseTypes(record);
		// } else if (record.getBaseType() ==
		// ClientAccount.BASETYPE_OTHER_INCOME_OR_EXPENSE) {
		// addOtherIncomeOrExpenseTypes(record);
		// } else {
		// closeAllSection();
		// }

		if (closePrevSection(record.getParentAccount() == null ? record
				.getAccountName() : getAccountNameById(record
				.getParentAccount()))) {
			processRecord(record);
		} else {
			addSection(record);
			return;
		}

	}

	protected void initValues() {
		totalincome = 0.0D;
		totalCGOS = 0.0D;
		grosProft = 0.0D;
		totalexpese = 0.0D;
		netIncome = 0.0D;
		otherIncome = 0.0D;
		otherExpense = 0.0D;
		otherNetIncome = 0.0D;

		totalincome2 = 0.0D;
		totalCGOS2 = 0.0D;
		grosProft2 = 0.0D;
		totalexpese2 = 0.0D;
		netIncome2 = 0.0D;
		otherIncome2 = 0.0D;
		otherExpense2 = 0.0D;
		otherNetIncome2 = 0.0D;
	}

	protected void iniHandler() {

		initValues();
		this.handler = new ISectionHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void OnSectionAdd(
					com.vimukti.accounter.web.client.ui.reports.AbstractReportView.Section section) {
				if (section.title == FinanceApplication.getReportsMessages()
						.grossProfit()) {
					section.data[0] = "";
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			public void OnSectionEnd(
					com.vimukti.accounter.web.client.ui.reports.AbstractReportView.Section section) {
				if (section.title.equals(FinanceApplication
						.getReportsMessages().income())) {
					totalincome = Double.valueOf(section.data[3].toString());
					totalincome2 = Double.valueOf(section.data[5].toString());
				}
				if (section.title.equals(FinanceApplication
						.getReportsMessages().costOfGoodSold())) {
					totalCGOS = Double.valueOf(section.data[3].toString());
					totalCGOS2 = Double.valueOf(section.data[5].toString());
				}
				if (section.title.equals(FinanceApplication
						.getReportsMessages().otherExpense())) {
					otherExpense = Double.valueOf(section.data[3].toString());
					otherExpense2 = Double.valueOf(section.data[5].toString());
				}
				if (section.footer.equals(FinanceApplication
						.getReportsMessages().grossProfit())) {
					grosProft = totalincome - totalCGOS - otherExpense;
					section.data[3] = grosProft;
					grosProft2 = totalincome2 - totalCGOS2 - otherExpense2;
					section.data[5] = grosProft2;
				}
				if (section.title.equals(FinanceApplication
						.getReportsMessages().expense())) {
					totalexpese = (Double) section.data[3];
					totalexpese2 = (Double) section.data[5];
				}
				if (section.footer.equals(FinanceApplication
						.getReportsMessages().netOrdinaryIncome())) {
					netIncome = grosProft - totalexpese;
					section.data[3] = netIncome;
					netIncome2 = grosProft2 - totalexpese2;
					section.data[5] = netIncome2;
				}
				if (section.title.equals(FinanceApplication
						.getReportsMessages().otherIncome())) {
					otherIncome = Double.valueOf(section.data[3].toString());
					otherIncome2 = Double.valueOf(section.data[5].toString());
				}

				if (section.title.equals(FinanceApplication
						.getReportsMessages().otherIncomeOrExpense())) {
					otherNetIncome = otherIncome - otherExpense;
					section.data[3] = otherNetIncome;
					otherNetIncome2 = otherIncome2 - otherExpense2;
					section.data[5] = otherNetIncome2;
				}
				// if (section.footer == FinanceApplication.getReportsMessages()
				// .netIncome()) {
				// section.data[1] = netIncome + otherNetIncome;
				// }
			}
		};

	}

	public String getAccountNameById(String id) {
		for (TrialBalance balance : this.records)
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

	public void addOrdinaryIncomeOrExpenseTypes(TrialBalance record) {
		// if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
		// .ordinaryIncomeOrExpense())) {
		// closeAllSection();
		// addTypeSection(FinanceApplication.getReportsMessages()
		// .ordinaryIncomeOrExpense(), FinanceApplication
		// .getReportsMessages().netOrdinaryIncome());
		// }
		if (record.getAccountType() == ClientAccount.TYPE_INCOME
				|| record.getAccountType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD) {
			if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
					.grossProfit())) {
				addTypeSection(FinanceApplication.getReportsMessages()
						.grossProfit(), "", FinanceApplication
						.getReportsMessages().grossProfit());

			}
			if (record.getAccountType() == ClientAccount.TYPE_INCOME)
				if (!sectiontypes.contains(FinanceApplication
						.getReportsMessages().income())) {

					addTypeSection(FinanceApplication.getReportsMessages()
							.income(), FinanceApplication.getReportsMessages()
							.totalIncome());
				}
			if (record.getAccountType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD)
				if (!sectiontypes.contains(FinanceApplication
						.getReportsMessages().costOfGoodSold())) {
					closeOtherSections();
					closeSection(types.indexOf(FinanceApplication
							.getReportsMessages().income()));
					addTypeSection(FinanceApplication.getReportsMessages()
							.costOfGoodSold(), FinanceApplication
							.getReportsMessages().totalCOGS());

				}

		}

		if (record.getAccountType() == ClientAccount.TYPE_OTHER_EXPENSE) {

			if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
					.otherExpense())) {
				for (int i = types.size() - 2; i > 0; i--) {
					closeSection(i);
				}
				addTypeSection(FinanceApplication.getReportsMessages()
						.otherExpense(), FinanceApplication
						.getReportsMessages().totalOtherExpense());
			}
		}

		if (record.getAccountType() == ClientAccount.TYPE_EXPENSE) {

			if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
					.expense())) {
				closeAllSection();
				addTypeSection(FinanceApplication.getReportsMessages()
						.expense(), FinanceApplication.getReportsMessages()
						.totalExpense());
			}
		}

	}

	public void addOtherIncomeOrExpenseTypes(TrialBalance record) {
		if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
				.otherIncomeOrExpense())) {
			closeAllSection();
			addTypeSection(FinanceApplication.getReportsMessages()
					.otherIncomeOrExpense(), FinanceApplication
					.getReportsMessages().netOtherIncome());
		}
		// if (record.getAccountType() == ClientAccount.TYPE_OTHER_INCOME) {
		// if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
		// .otherIncome())) {
		// addTypeSection(FinanceApplication.getReportsMessages()
		// .otherIncome(), FinanceApplication.getReportsMessages()
		// .totalOtherIncome());
		// }
		// }

		if (record.getAccountType() == ClientAccount.TYPE_OTHER_EXPENSE) {
			if (!sectiontypes.contains(FinanceApplication.getReportsMessages()
					.otherExpense())) {
				for (int i = types.size() - 2; i > 0; i--) {
					closeSection(i);
				}
				addTypeSection(FinanceApplication.getReportsMessages()
						.otherExpense(), FinanceApplication
						.getReportsMessages().totalOtherExpense());
			}
		}

	}

	public boolean isParent(TrialBalance record) {
		for (TrialBalance balance : this.records) {
			if (balance.getParentAccount() != null) {
				if (balance.getParentAccount().equals(record.getAccountId()))
					return true;
			}
		}
		return false;
	}

	public boolean addSection(TrialBalance record) {
		if (isParent(record)) {
			types.add(record.getAccountName());
			curentParent = record.getAccountName();
			// System.out.println("Add:" + curentParent);
			addSection(record.getAccountNumber() + "-"
					+ record.getAccountName(), record.getAccountName() + "  "
					+ FinanceApplication.getReportsMessages().total(),
					new int[] { 3, 5 });
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
					if (sectionDepth > 0)
						endSection();
					return true;
				}
			}
		return false;
	}

	public void closeSection(int index) {
		if (index >= 0) {
			types.remove(index);
			curentParent = "";
			endSection();
		}
	}

	/**
	 * add Type Section
	 * 
	 * @param title
	 */
	public void addTypeSection(String title, String bottomTitle) {
		if (!sectiontypes.contains(title)) {
			addSection(new String[] { "", title }, new String[] { "",
					bottomTitle }, new int[] { 3, 5 });
			types.add(title);
			sectiontypes.add(title);
		}
	}

	public void addTypeSection(String sectionType, String title,
			String bottomTitle) {
		if (!sectiontypes.contains(sectionType)) {
			addSection(new String[] { "", title }, new String[] { "",
					bottomTitle }, new int[] { 3, 5 });
			types.add(title);
			sectiontypes.add(sectionType);
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
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

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

		switch (index) {
		case 0:
			return 115;
		case 2:
			return 145;
		case 3:
			return 100;
		case 4:
			return 145;
		case 5:
			return 100;

		default:
			break;
		}
		// if (index == 1)
		// return 310;
		// else if (index == 2)
		// return 270;
		// else
		return 270;
	}

	@Override
	public boolean isWiderReport() {
		return true;
	}

	public ClientFinanceDate getLastMonth(ClientFinanceDate date) {
		int month = date.getMonth();
		int year = date.getYear();

		int lastDay;
		switch (month) {
		case 0:
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
			lastDay = 31;
			break;
		case 2:
			if (year % 4 == 0 && year % 100 == 0)
				lastDay = 29;
			else
				lastDay = 28;
			break;

		default:
			lastDay = 30;
			break;
		}
		return new ClientFinanceDate(year, date.getMonth() - 1, lastDay);
		// return lastDay;
	}

	public ClientFinanceDate getLastMonthStartDate() {
		ClientFinanceDate openFiscalYearDate = Utility
				.getCurrentFiscalYearStartDate();
		ClientFinanceDate lastmonthEndDate = getLastMonth(toolbar.getEndDate());
		if (openFiscalYearDate.getYear() != lastmonthEndDate.getYear())
			return new ClientFinanceDate(lastmonthEndDate.getYear(),
					openFiscalYearDate.getMonth(), openFiscalYearDate.getDate());

		return openFiscalYearDate;

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
