package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.reports.ExpenseList;
import com.vimukti.accounter.web.server.FinanceTool;

public class ExpenseReportCommand extends NewAbstractReportCommand<ExpenseList> {

	private static String EXPENSETYPE = "expenseType";

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringListRequirement(EXPENSETYPE, getMessages()
				.pleaseSelectExpenseType(), "Expense Type", true, true, null) {

			@Override
			protected String getSetMessage() {
				return "Expense type selected";
			}

			@Override
			protected String getSelectString() {
				return "Please select any one of the expense type";
			}

			@Override
			protected List<String> getLists(Context context) {
				String[] statusArray = new String[] {
						getMessages().allExpenses(), getMessages().cash(),
						getMessages().creditCard() };
				return Arrays.asList(statusArray);
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});
		addDateRangeFromToDateRequirements(list);
	}

	protected Record createReportRecord(ExpenseList record) {
		Record expenseRecord = new Record(record);

		expenseRecord.add(getMessages().transactionName(),
				Utility.getTransactionName(record.getTransactionType()));
		expenseRecord.add(getMessages().transactionDate(),
				record.getTransactionDate());
		expenseRecord.add(getMessages().amount(), record.getTotal());
		return expenseRecord;
	}

	protected List<ExpenseList> getRecords() {
		List<ExpenseList> expensesList = new ArrayList<ExpenseList>();
		try {
			expensesList = new FinanceTool().getReportManager()
					.getExpenseReportByType(getExpenseType(), getStartDate(),
							getEndDate(), getCompanyId());
		} catch (DAOException e) {
			e.printStackTrace();
			expensesList = new ArrayList<ExpenseList>();
		}
		return expensesList;
	}

	private int getExpenseType() {
		String type = get(EXPENSETYPE).getValue();
		if (type.equals(getMessages().cash())) {
			return ClientTransaction.TYPE_CASH_EXPENSE;
		} else if (type.equals(getMessages().creditCard())) {
			return ClientTransaction.TYPE_CREDIT_CARD_EXPENSE;
		}
		return 0;
	}

	@Override
	protected void setDefaultValues(Context context) {
		super.setDefaultValues(context);
		get(EXPENSETYPE).setDefaultValue(getMessages().allExpenses());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		get(DATE_RANGE).setValue(getMessages().financialYearToDate());
		dateRangeChanged(getMessages().financialYearToDate());
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "Expense report command activated successfully";
	}

	@Override
	protected String getDetailsMessage() {
		return "Expense report details as follows";
	}

	@Override
	public String getSuccessMessage() {
		return "Expense report command closed successfully";
	}
}
