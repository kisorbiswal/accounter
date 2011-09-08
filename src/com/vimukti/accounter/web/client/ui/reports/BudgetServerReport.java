package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.core.reports.ClientBudgetList;
import com.vimukti.accounter.web.client.core.reports.ExpenseList;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.AbstractFinaneReport;

public class BudgetServerReport extends AbstractFinaneReport<ClientBudgetList> {

	private String sectionName = "";

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
		switch (columnIndex) {
		case 0:
			return null;
		case 1:
			return null;
		case 2:
			return null;
		case 3:
			return null;
		case 4:
			return null;
		case 5:
			return null;
		case 6:
			return null;
		case 7:
			return null;
		case 8:
			return null;
		case 9:
			return null;
		case 10:
			return null;
		case 12:
			return null;
		case 13:
			return null;

		}
		return null;
	}

	@Override
	public String getDefaultDateRange() {
		return getConstants().all();
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {

		if (budgettype == 2) {
			return new String[] { getConstants().accountName(),
					getConstants().month(), getConstants().total() };
		} else if (budgettype == 3) {
			return new String[] { getConstants().accountName(),
					getConstants().jan() + " - " + getConstants().mar(),
					getConstants().apr() + " - " + getConstants().jun(),
					getConstants().jul() + " - " + getConstants().sept(),
					getConstants().oct() + " - " + getConstants().dec(),
					getConstants().total() };
		} else if (budgettype == 4) {
			return new String[] { getConstants().accountName(),
					getConstants().total() };
		} else {
			return new String[] { getConstants().accountName(),
					getConstants().date(), getConstants().total() };
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
			this.sectionName = record.getName();
			addSection(sectionName, "", new int[] { 2 });
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

	@Override
	public int getColumnWidth(int index) {
		if (index == 2)
			return 100;
		else if (index == 3)
			return 100;
		else if (index == 1)
			return 100;
		else
			return 100;
	}

	public int sort(ExpenseList obj1, ExpenseList obj2, int col) {
		switch (col) {
		case 0:
			return UIUtils.compareInt(obj1.getTransactionType(),
					obj2.getTransactionType());
		case 1:
			return obj1.getTransactionDate().compareTo(
					obj2.getTransactionDate());
		case 2:
			return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
		case 3:
			if (!currentsectionName.toLowerCase().equals(
					obj1.getName().toLowerCase())) {
				return obj1.getName().toLowerCase()
						.compareTo(obj2.getName().toLowerCase());
			} else {
				return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
			}
		}
		return 0;
	}

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
		if (budgettype == 2) {
			return new String[] { getConstants().accountName(),
					getConstants().jan(), getConstants().feb(),
					getConstants().mar(), getConstants().apr(),
					getConstants().may(), getConstants().jun(),
					getConstants().jul(), getConstants().aug(),
					getConstants().sept(), getConstants().oct(),
					getConstants().nov(), getConstants().dec(),
					getConstants().total() };
		} else if (budgettype == 3) {
			return new String[] { getConstants().accountName(),
					getConstants().jan(), getConstants().feb(),
					getConstants().mar(), getConstants().apr(),
					getConstants().may(), getConstants().jun(),
					getConstants().jul(), getConstants().aug(),
					getConstants().sept(), getConstants().oct(),
					getConstants().nov(), getConstants().dec(),
					getConstants().total() };
		} else if (budgettype == 4) {
			return new String[] { getConstants().accountName(),
					getConstants().jan(), getConstants().feb(),
					getConstants().mar(), getConstants().apr(),
					getConstants().may(), getConstants().jun(),
					getConstants().jul(), getConstants().aug(),
					getConstants().sept(), getConstants().oct(),
					getConstants().nov(), getConstants().dec(),
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

}
