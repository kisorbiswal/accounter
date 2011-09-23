package com.vimukti.accounter.web.client.ui.serverreports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;
import com.vimukti.accounter.web.client.ui.reports.ISectionHandler;
import com.vimukti.accounter.web.client.ui.reports.Section;

public class ProfitAndLossServerReport extends
		AbstractFinaneReport<TrialBalance> {
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

	// TrialBalance trialBalance
	public ProfitAndLossServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
		this.columnstoHide.add(3);
		this.columnstoHide.add(5);
	}

	public ProfitAndLossServerReport(long startDate, long endDate,
			int generationType, IFinanceReport<TrialBalance> reportView) {
		super(startDate, endDate, generationType);
		this.columnstoHide.add(3);
		this.columnstoHide.add(5);
		this.reportView = reportView;
	}

	public ProfitAndLossServerReport(IFinanceReport<TrialBalance> reportView) {
		this.columnstoHide.add(3);
		this.columnstoHide.add(5);
		this.reportView = reportView;
	}

	@Override
	public Object getColumnData(TrialBalance record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if (getPreferences().getUseAccountNumbers() == true) {
				return record.getAccountNumber();
			} else {
				return null;
			}
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
	public String getDefaultDateRange() {
		return getConstants().financialYearToDate();
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
				getConstants().categoryNumber(),
				"",
				// FIXME for reports these are NULL...
				"" + getDateByCompanyType(getStartDate()) + " - "
						+ getDateByCompanyType(getEndDate()),
				"",
				getDateByCompanyType(getCurrentFiscalYearStartDate())
						+ " - "
						+ getDateByCompanyType(getLastMonth(new ClientFinanceDate())),

				"" };
	}

	@Override
	public String[] getDynamicHeaders() {

		return new String[] {
				getConstants().categoryNumber(),
				"",
				"" + getDateByCompanyType(getStartDate()) + " - "
						+ getDateByCompanyType(getEndDate()),
				"",
				getDateByCompanyType(getCurrentFiscalYearStartDate())
						+ " - "
						+ getDateByCompanyType(getLastMonth(new ClientFinanceDate())),

				"" };
	}

	@Override
	public String getTitle() {
		return getConstants().profitAndLoss();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// initValues();
		// try {
		// onSuccess(this.financeTool.getProfitAndLossReport(start, end));
		// } catch (DAOException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public void processRecord(TrialBalance record) {
		if (this.handler == null)
			iniHandler();

		if (sectionDepth == 0) {
			addTypeSection("", getConstants().netOrdinaryIncome());
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

		if (closePrevSection(record.getParentAccount() == 0 ? record
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
		this.handler = new ISectionHandler<TrialBalance>() {

			@Override
			public void OnSectionAdd(Section<TrialBalance> section) {
				if (section.title == "GrossProfit") {
					section.data[0] = "";
				}
			}

			@Override
			public void OnSectionEnd(Section<TrialBalance> section) {
				if (section.title.equals("Income")) {
					totalincome = Double.valueOf(section.data[3].toString());
					totalincome2 = Double.valueOf(section.data[5].toString());
				}
				if (section.title.equals("Cost of good Sold")) {
					totalCGOS = Double.valueOf(section.data[3].toString());
					totalCGOS2 = Double.valueOf(section.data[5].toString());
				}
				if (section.title.equals("Other Expense")) {
					otherExpense = Double.valueOf(section.data[3].toString());
					otherExpense2 = Double.valueOf(section.data[5].toString());
				}
				if (section.footer.equals("GrossProfit")) {
					grosProft = totalincome - totalCGOS - otherExpense;
					section.data[3] = grosProft;
					grosProft2 = totalincome2 - totalCGOS2 - otherExpense2;
					section.data[5] = grosProft2;
				}
				if (section.title.equals("Expense")) {
					totalexpese = (Double) section.data[3];
					totalexpese2 = (Double) section.data[5];
				}
				if (section.footer.equals("Net Ordinary Income")) {
					netIncome = grosProft - totalexpese;
					section.data[3] = netIncome;
					netIncome2 = grosProft2 - totalexpese2;
					section.data[5] = netIncome2;
				}
				if (section.title.equals("Other Income")) {
					otherIncome = Double.valueOf(section.data[3].toString());
					otherIncome2 = Double.valueOf(section.data[5].toString());
				}

				if (section.title.equals("Other Income or Expense")) {
					otherNetIncome = otherIncome - otherExpense;
					section.data[3] = otherNetIncome;
					otherNetIncome2 = otherIncome2 - otherExpense2;
					section.data[5] = otherNetIncome2;
				}
				// if (section.footer == FinanceApplication.constants()
				// .netIncome()) {
				// section.data[1] = netIncome + otherNetIncome;
				// }
			}
		};

	}

	public String getAccountNameById(long id) {
		for (TrialBalance balance : this.records)
			if (balance.getAccountId() == id)
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
		// if (!sectiontypes.contains(FinanceApplication.constants()
		// .ordinaryIncomeOrExpense())) {
		// closeAllSection();
		// addTypeSection(FinanceApplication.constants()
		// .ordinaryIncomeOrExpense(), FinanceApplication
		// .constants().netOrdinaryIncome());
		// }
		if (record.getAccountType() == ClientAccount.TYPE_INCOME
				|| record.getAccountType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD) {
			if (!sectiontypes.contains("GrossProfit")) {
				addTypeSection(getConstants().grossProfit(), "", getConstants()
						.grossProfit());

			}
			if (record.getAccountType() == ClientAccount.TYPE_INCOME)
				if (!sectiontypes.contains("Income")) {

					addTypeSection(getConstants().income(), getConstants()
							.incomeTotals());
				}
			if (record.getAccountType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD)
				if (!sectiontypes.contains("Cost of good Sold")) {
					closeOtherSections();
					closeSection(types.indexOf("Income"));
					addTypeSection(getConstants().costOfGoodSold(),
							getConstants().cogsTotal());

				}

		}

		if (record.getAccountType() == ClientAccount.TYPE_OTHER_EXPENSE) {

			if (!sectiontypes.contains("Other Expense")) {
				for (int i = types.size() - 2; i > 0; i--) {
					closeSection(i);
				}
				addTypeSection(getConstants().otherExpense(), getConstants()
						.otherExpenseTotals());
			}
		}

		if (record.getAccountType() == ClientAccount.TYPE_EXPENSE) {

			if (!sectiontypes.contains("Expense")) {
				closeAllSection();
				addTypeSection(getConstants().expense(), getConstants()
						.expenseTotals());
			}
		}

	}

	public void addOtherIncomeOrExpenseTypes(TrialBalance record) {
		if (!sectiontypes.contains("Other Income or Expense")) {
			closeAllSection();
			addTypeSection(getConstants().otherIncomeOrExpense(),
					getConstants().netOtherIncome());
		}
		// if (record.getAccountType() == ClientAccount.TYPE_OTHER_INCOME) {
		// if (!sectiontypes.contains(FinanceApplication.constants()
		// .otherIncome())) {
		// addTypeSection(FinanceApplication.constants()
		// .otherIncome(), FinanceApplication.constants()
		// .totalOtherIncome());
		// }
		// }

		if (record.getAccountType() == ClientAccount.TYPE_OTHER_EXPENSE) {
			if (!sectiontypes.contains("Other Expense")) {
				for (int i = types.size() - 2; i > 0; i--) {
					closeSection(i);
				}
				addTypeSection(getConstants().otherExpense(), getConstants()
						.otherExpenseTotals());
			}
		}

	}

	public boolean isParent(TrialBalance record) {
		for (TrialBalance balance : this.records) {
			if (balance.getParentAccount() != 0) {
				if (balance.getParentAccount() == record.getAccountId())
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
			addSection(
					record.getAccountNumber() + "-" + record.getAccountName(),
					record.getAccountName() + "  " + getConstants().total(),
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

	public void print() {

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
	public int getColumnWidth(int index) {

		switch (index) {
		case 0:
			return 110;
		case 2:
			return 135;
		case 3:
			return 95;
		case 4:
			return 135;
		case 5:
			return 90;

		default:
			break;
		}
		// if (index == 1)
		// return 310;
		// else if (index == 2)
		// return 270;
		// else
		return 260;
	}

	@Override
	public boolean isWiderReport() {
		return true;
	}

	public ClientFinanceDate getLastMonth(ClientFinanceDate date) {
		int month = date.getMonth() - 1;
		int year = date.getYear();

		int lastDay;
		switch (month) {
		case 0:
		case 2:
		case 4:
		case 6:
		case 7:
		case 9:
		case 11:
			lastDay = 31;
			break;
		case 1:
			if (year % 4 == 0 && year % 100 == 0)
				lastDay = 29;
			else
				lastDay = 28;
			break;

		default:
			lastDay = 30;
			break;
		}
		return new ClientFinanceDate(date.getYear(), date.getMonth() - 1,
				lastDay);
		// return lastDay;
	}

}
