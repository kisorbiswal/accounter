package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.core.reports.ClientBudgetList;
import com.vimukti.accounter.web.client.ui.serverreports.AbstractFinaneReport;

public class BudgetServerReport extends AbstractFinaneReport<ClientBudgetList> {

	private int TOOLBAR_TYPE_CUSTOM = 1;
	private int TOOLBAR_TYPE_MONTH = 2;
	private int TOOLBAR_TYPE_QUATER = 3;
	private int TOOLBAR_TYPE_YEAR = 4;

	private String sectionName = "";
	int monthSelected;
	private String currentsectionName = "";

	int budgettype;

	public BudgetServerReport(IFinanceReport<ClientBudgetList> reportView,
			int budgetType) {
		this.reportView = reportView;
		budgettype = budgetType;

	}

	public BudgetServerReport(long startDate, long endDate, int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public Object getColumnData(ClientBudgetList record, int columnIndex) {

		if (budgettype == TOOLBAR_TYPE_MONTH) {
			switch (columnIndex) {
			case 0:
				return record.getAccount().getID();
			case 1:
				return record.getAccount().getName();
			case 2:
				if (monthSelected == 1)
					return record.getJanuaryAmount();
				else if (monthSelected == 2)
					return record.getFebrauaryAmount();
				else if (monthSelected == 3)
					return record.getMarchAmount();
				else if (monthSelected == 4)
					return record.getAprilAmount();
				else if (monthSelected == 5)
					return record.getMayAmount();
				else if (monthSelected == 6)
					return record.getJuneAmount();
				else if (monthSelected == 7)
					return record.getJulyAmount();
				else if (monthSelected == 8)
					return record.getAugustAmount();
				else if (monthSelected == 9)
					return record.getSeptemberAmount();
				else if (monthSelected == 10)
					return record.getOctoberAmount();
				else if (monthSelected == 11)
					return record.getNovemberAmount();
				else if (monthSelected == 12)
					return record.getJanuaryAmount();
				else
					return record.getDecemberAmount();
			case 3:
				return record.getTotalAmount();
			}
		} else if (budgettype == TOOLBAR_TYPE_QUATER) {
			switch (columnIndex) {
			case 0:
				return record.getAccount().getName();
			case 1:
				return record.getJanuaryAmount() + record.getFebrauaryAmount()
						+ record.getMarchAmount();
			case 2:
				return record.getAprilAmount() + record.getMayAmount()
						+ record.getJuneAmount();
			case 3:
				return record.getJulyAmount() + record.getAugustAmount()
						+ record.getSeptemberAmount();
			case 4:
				return record.getOctoberAmount() + record.getNovemberAmount()
						+ record.getDecemberAmount();
			case 5:
				return record.getTotalAmount();
			}

		} else if (budgettype == TOOLBAR_TYPE_YEAR) {
			switch (columnIndex) {
			case 0:
				return record.getAccount().getID();
			case 1:
				return record.getAccount().getName();
			case 2:
				return record.getTotalAmount();
			}

		} else {
			switch (columnIndex) {
			case 0:
				return record.getAccount().getName();
			case 1:
				return record.getJanuaryAmount();
			case 2:
				return record.getFebrauaryAmount();
			case 3:
				return record.getMarchAmount();
			case 4:
				return record.getAprilAmount();
			case 5:
				return record.getMayAmount();
			case 6:
				return record.getJuneAmount();
			case 7:
				return record.getJulyAmount();
			case 8:
				return record.getAugustAmount();
			case 9:
				return record.getSeptemberAmount();
			case 10:
				return record.getOctoberAmount();
			case 11:
				return record.getNovemberAmount();
			case 12:
				return record.getDecemberAmount();
			case 13:
				return record.getTotalAmount();
			}
		}

		return null;
	}

	@Override
	public String getDefaultDateRange() {
		return getConstants().all();
	}

	@Override
	public int[] getColumnTypes() {
		if (budgettype == TOOLBAR_TYPE_MONTH) {
			return new int[] { COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
					COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
		} else if (budgettype == TOOLBAR_TYPE_QUATER) {
			return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
					COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
					COLUMN_TYPE_AMOUNT };
		} else if (budgettype == TOOLBAR_TYPE_YEAR) {
			return new int[] { COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
					COLUMN_TYPE_AMOUNT };
		} else {
			return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
					COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
					COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
					COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
					COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
		}

	}

	@Override
	public String[] getColunms() {

		if (budgettype == TOOLBAR_TYPE_MONTH) {
			return new String[] { "", getConstants().accountName(),
					getConstants().jan(), getConstants().total() };
		} else if (budgettype == TOOLBAR_TYPE_QUATER) {
			return new String[] { getConstants().accountName(),
					getConstants().jan() + " - " + getConstants().mar(),
					getConstants().apr() + " - " + getConstants().jun(),
					getConstants().jul() + " - " + getConstants().sept(),
					getConstants().oct() + " - " + getConstants().dec(),
					getConstants().total() };
		} else if (budgettype == TOOLBAR_TYPE_YEAR) {
			return new String[] { "", getConstants().accountName(),
					getConstants().total() };
		} else {
			return new String[] { getConstants().accountName(),
					getConstants().jan(), getConstants().feb(),
					getConstants().mar(), getConstants().apr(),
					getConstants().may(), getConstants().jun(),
					getConstants().jul(), getConstants().aug(),
					getConstants().sept(), getConstants().oct(),
					getConstants().nov(), getConstants().dec(),
					getConstants().total() };
		}
	}

	@Override
	public String getTitle() {
		return getConstants().expenseReport();
	}

	@Override
	public void processRecord(ClientBudgetList record) {

		if (sectionDepth == 0) {
			addSection("", getConstants().total(), new int[] { 2 });
		} else if (sectionDepth == 1) {
			if (budgettype == TOOLBAR_TYPE_MONTH) {

				addSection("", getConstants().total(), new int[] { 3 });
			} else if (budgettype == TOOLBAR_TYPE_QUATER) {

				addSection("", getConstants().total(), new int[] { 5 });
			} else if (budgettype == TOOLBAR_TYPE_YEAR) {

				addSection("", getConstants().total(), new int[] { 2 });
			} else {

				addSection("", getConstants().total(), new int[] { 13 });
			}

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
	public ClientFinanceDate getEndDate(ClientBudgetList obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(ClientBudgetList obj) {
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

	/*
	 * public int sort(ExpenseList obj1, ExpenseList obj2, int col) { switch
	 * (col) { case 0: return UIUtils.compareInt(obj1.getTransactionType(),
	 * obj2.getTransactionType()); case 1: return
	 * obj1.getTransactionDate().compareTo( obj2.getTransactionDate()); case 2:
	 * return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal()); case 3:
	 * if (!currentsectionName.toLowerCase().equals(
	 * obj1.getName().toLowerCase())) { return obj1.getName().toLowerCase()
	 * .compareTo(obj2.getName().toLowerCase()); } else { return
	 * UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal()); } } return 0; }
	 */

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
		this.sectionName = "";
		this.currentsectionName = "";
	}

	@Override
	public boolean isWiderReport() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String[] getDynamicHeaders() {
		if (budgettype == TOOLBAR_TYPE_MONTH) {

			if (monthSelected == 1)
				return new String[] { "", getConstants().accountName(),
						getConstants().jan(), getConstants().total() };
			else if (monthSelected == 2)
				return new String[] { "", getConstants().accountName(),
						getConstants().feb(), getConstants().total() };
			else if (monthSelected == 3)
				return new String[] { "", getConstants().accountName(),
						getConstants().mar(), getConstants().total() };
			else if (monthSelected == 4)
				return new String[] { "", getConstants().accountName(),
						getConstants().apr(), getConstants().total() };
			else if (monthSelected == 5)
				return new String[] { "", getConstants().accountName(),
						getConstants().may(), getConstants().total() };
			else if (monthSelected == 6)
				return new String[] { "", getConstants().accountName(),
						getConstants().jun(), getConstants().total() };
			else if (monthSelected == 7)
				return new String[] { "", getConstants().accountName(),
						getConstants().jul(), getConstants().total() };
			else if (monthSelected == 8)
				return new String[] { "", getConstants().accountName(),
						getConstants().aug(), getConstants().total() };
			else if (monthSelected == 9)
				return new String[] { "", getConstants().accountName(),
						getConstants().sept(), getConstants().total() };
			else if (monthSelected == 10)
				return new String[] { "", getConstants().accountName(),
						getConstants().oct(), getConstants().total() };
			else if (monthSelected == 11)
				return new String[] { "", getConstants().accountName(),
						getConstants().nov(), getConstants().total() };
			else if (monthSelected == 12)
				return new String[] { "", getConstants().accountName(),
						getConstants().dec(), getConstants().total() };
			else
				return new String[] { "", getConstants().accountName(),
						getConstants().jan(), getConstants().total() };

		} else if (budgettype == TOOLBAR_TYPE_QUATER) {
			return new String[] { getConstants().accountName(),
					getConstants().jan() + " - " + getConstants().mar(),
					getConstants().apr() + " - " + getConstants().jun(),
					getConstants().jul() + " - " + getConstants().sept(),
					getConstants().oct() + " - " + getConstants().dec(),
					getConstants().total() };
		} else if (budgettype == TOOLBAR_TYPE_YEAR) {
			return new String[] { "", getConstants().accountName(),
					getConstants().total() };
		} else {
			return new String[] { getConstants().accountName(),
					getConstants().jan(), getConstants().feb(),
					getConstants().mar(), getConstants().apr(),
					getConstants().may(), getConstants().jun(),
					getConstants().jul(), getConstants().aug(),
					getConstants().sept(), getConstants().oct(),
					getConstants().nov(), getConstants().dec(),
					getConstants().total() };
		}
	}

	@Override
	public void makeReportRequest(long start, long end) {

	}

	public void setMonth(int month) {
		monthSelected = month;

	}

}
