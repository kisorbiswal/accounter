package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.core.reports.ClientBudgetList;
import com.vimukti.accounter.web.client.ui.serverreports.AbstractFinaneReport;

public class BudgetOverviewServerReport extends
		AbstractFinaneReport<ClientBudgetList> {

	private String sectionName = "";
	private String currentsectionName = "";

	public BudgetOverviewServerReport(
			IFinanceReport<ClientBudgetList> reportView) {
		this.reportView = reportView;

	}

	public BudgetOverviewServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public Object getColumnData(ClientBudgetList record, int columnIndex) {

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

		return null;
	}

	@Override
	public String getDefaultDateRange() {
		return getMessages().all();
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

		return new String[] { getMessages().name(), getMessages().jan(),
				getMessages().feb(), getMessages().mar(), getMessages().apr(),
				getMessages().may(), getMessages().jun(), getMessages().jul(),
				getMessages().aug(), getMessages().sept(), getMessages().oct(),
				getMessages().nov(), getMessages().dec(), getMessages().total() };

	}

	@Override
	public String getTitle() {
		return getMessages().expenseReport();
	}

	@Override
	public void processRecord(ClientBudgetList record) {

	/*	if (sectionDepth == 0) {
			addSection("", getMessages().total(), new int[] { 13 });
		} else if (sectionDepth == 1) {
			addSection("", getMessages().total(), new int[] { 13 });
		} else if (sectionDepth == 2) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getName())) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place

		processRecord(record);*/

		
		
		if (sectionDepth == 0) {
			addSection("", getMessages().total(), new int[] {13 });
		} else if (sectionDepth == 1) {
			return;
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

		return new String[] { getMessages().name(), getMessages().jan(),
				getMessages().feb(), getMessages().mar(), getMessages().apr(),
				getMessages().may(), getMessages().jun(), getMessages().jul(),
				getMessages().aug(), getMessages().sept(), getMessages().oct(),
				getMessages().nov(), getMessages().dec(), getMessages().total() };

	}

	@Override
	public void makeReportRequest(long start, long end) {

	}

}
