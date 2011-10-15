package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.Accounter;

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
		return result;
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
		List<BillsList> expenses = getExpenses(viewType);
		for (BillsList b : expenses) {
			expensesList.add(createExpenseRecord(b));
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

	private Record createExpenseRecord(BillsList exp) {
		Record record = new Record(exp);
		record.add("Type", exp.getType());
		record.add("No", exp.getNumber());
		record.add("Vendor", exp.getVendorName());
		record.add("OriginalAmount", exp.getOriginalAmount());
		record.add("Balance", exp.getBalance());
		record.add("IsVoid", exp.isVoided());

		return record;
	}

	@Override
	protected List<String> getViewTypes() {
		List<String> list = new ArrayList<String>();
		list.add(Accounter.constants().all());
		list.add(Accounter.constants().cash());
		list.add(Accounter.constants().creditCard());
		list.add(Accounter.constants().Voided());
		return list;
	}

}
