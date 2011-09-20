package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Expense;
import com.vimukti.accounter.core.PaymentTerms;
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

		Result result = viewTypeRequirement(context, selection);
		if (result != null) {
			return result;
		}
		String viewType = get(VIEW_TYPE).getValue();
		result = expensesList(context, viewType);
		return result;
	}

	private Result expensesList(Context context, String viewType) {
		Result result = context.makeResult();
		result.add("");
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
		result.add(expensesList);
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

	private Result viewTypeRequirement(Context context, Object selection) {

		Object payamentObj = context.getSelection(VIEW_TYPE);
		Requirement viewsRequirement = get(VIEW_TYPE);
		List<String> views = viewsRequirement.getValue();
		Result result = null;
		ResultList list = new ResultList(VIEW_TYPE);
		if (selection == views) {

			List<String> viewTypes = getViewTypes();
			result = context.makeResult();
			result.add("Select View Type");

			int num = 0;
			for (String view : viewTypes) {
				list.add(createViewTypeRecord(view));
				num++;
				if (num == 0) {
					break;
				}
			}

			result.add(list);
		} else {
			if (views != null) {
				views.add(new String("All"));
				viewsRequirement.setValue(views);
			}

			Record viewRecord = new Record(views);
			viewRecord.add("Name", "");
			viewRecord.add("Value", views.toString());
			list.add(viewRecord);
		}

		return null;
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
