package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Expense;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class ExpensesListCommand extends AbstractTransactionCommand {

	private static final String VIEW_TYPE = "viewType";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(VIEW_TYPE, true, true));

	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		return null;
	}

	private Result createOptionalResult(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(VIEW_TYPE);

		ResultList list = new ResultList("viewlist");
		Result result = viewTypeRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		String viewType = get(VIEW_TYPE).getValue();
		result = expensesList(context, viewType);
		return result;
	}

	private Result expensesList(Context context, String viewType) {
		Result result = context.makeResult();
		result.add("Expenses List");
		ResultList expensesList = new ResultList("accountsList");
		int num = 0;
		List<Expense> expenses = getExpenses(context.getHibernateSession(),
				viewType);
		for (Expense exp : expenses) {
			expensesList.add(createExpenseRecord(exp));
			num++;
			if (num == EXPENSES_TO_SHOW) {
				break;
			}
		}
		int size = expensesList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Expense");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(expensesList);
		result.add(commandList);
		result.add("Type for Expense");

		return result;
	}

	private Record createExpenseRecord(Expense exp) {
		Record record = new Record(exp);
		record.add("Type", exp.getType());
		record.add("No", exp.getNumber());
		record.add("Vendor", exp.getPayee().getName());
		record.add("OriginalAmount", exp.getTotal());
		record.add("Balance", exp.getPayee().getBalance());
		record.add("IsVoid", exp.isVoid());

		return record;
	}

	private List<Expense> getExpenses(Session hibernateSession, String viewType) {
		// TODO Auto-generated method stub
		return null;
	}

	private Result viewTypeRequirement(Context context, ResultList list,
			Object selection) {

		Object viewType = context.getSelection(VIEW_TYPE);
		Requirement viewReq = get(VIEW_TYPE);
		String view = viewReq.getValue();

		if (selection == view) {
			return viewTypes(context, view);

		}
		if (viewType != null) {
			view = (String) viewType;
			viewReq.setValue(view);
		}

		Record viewtermRecord = new Record(view);
		viewtermRecord.add("Name", "viewType");
		viewtermRecord.add("Value", view);
		list.add(viewtermRecord);
		return null;
	}

	private Result viewTypes(Context context, String view) {
		ResultList list = new ResultList("viewslist");
		Result result = null;
		List<String> viewTypes = getViewTypes();
		result = context.makeResult();
		result.add("Select View Type");

		int num = 0;
		if (view != null) {
			list.add(createViewTypeRecord(view));
			num++;
		}
		for (String v : viewTypes) {
			if (v != view) {
				list.add(createViewTypeRecord(v));
				num++;
			}
			if (num == 0) {
				break;
			}

		}

		result.add(list);

		return result;

	}

	private Record createViewTypeRecord(String view) {
		Record record = new Record(view);
		record.add("Name", "ViewType");
		record.add("Value", view);
		return record;
	}

	private List<String> getViewTypes() {
		List<String> list = new ArrayList<String>();
		list.add("All");
		list.add("Cash");
		list.add("Credit Card");
		list.add("Voided");
		return list;
	}

}
