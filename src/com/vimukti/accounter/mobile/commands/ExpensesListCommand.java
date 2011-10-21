package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.server.FinanceTool;

public class ExpensesListCommand extends AbstractTransactionCommand {

	private static final String VIEW_TYPE = "viewType";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

	}

	@Override
	public Result run(Context context) {

		Result result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result createOptionalResult(Context context) {

		context.setAttribute(INPUT_ATTR, "optional");
		ActionNames selection = context.getSelection(ACTIONS);
		if (selection != null) {
			switch (selection) {
			case FINISH:
				return closeCommand();
			case CASH:
				context.setAttribute(VIEW_TYPE, getConstants().cash());
				break;
			case CREDIT_CARD:
				context.setAttribute(VIEW_TYPE, getConstants().creditCard());
				break;
			case VOIDED:
				context.setAttribute(VIEW_TYPE, getConstants().Voided());
				break;
			case ALL:
				context.setAttribute(VIEW_TYPE, getConstants().all());
				break;
			default:
				break;
			}
		}
		Result result = expensesList(context, selection);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result expensesList(Context context, ActionNames selection) {

		Result result = context.makeResult();
		ResultList expensesList = new ResultList("expensesList");
		result.add(getConstants().expensesList());

		String viewType = (String) context.getAttribute(VIEW_TYPE);
		List<BillsList> expenses = getExpensesList(context, viewType);

		ResultList actions = new ResultList("actions");

		List<BillsList> pagination = pagination(context, selection, actions,
				expenses, new ArrayList<BillsList>(), VALUES_TO_SHOW);

		for (BillsList b : pagination) {
			expensesList.add(createExpenseRecord(b));

		}

		result.add(expensesList);

		Record inActiveRec = new Record(ActionNames.CASH);
		inActiveRec.add("", getConstants().cash());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.CREDIT_CARD);
		inActiveRec.add("", getConstants().creditCard());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.VOIDED);
		inActiveRec.add("", getConstants().Voided());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.ALL);
		inActiveRec.add("", getConstants().all());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.FINISH);
		inActiveRec.add("", getConstants().close());
		actions.add(inActiveRec);

		result.add(actions);

		CommandList commandList = new CommandList();
		commandList.add(getConstants().addExpenses());
		result.add(commandList);

		return result;
	}

	private List<BillsList> getExpensesList(Context context, String viewType) {

		List<BillsList> result = new ArrayList<BillsList>();
		try {
			ArrayList<BillsList> billsLists = new FinanceTool()
					.getVendorManager().getBillsList(true,
							context.getCompany().getId());
			if (viewType == null) {
				return billsLists;
			}

			result.addAll(filterViewList(viewType, billsLists));
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return result;
	}

	private List<BillsList> filterViewList(String text,
			ArrayList<BillsList> billsLists) {
		List<BillsList> records = new ArrayList<BillsList>();
		if (text.equalsIgnoreCase(CASH)) {
			for (BillsList record : billsLists) {
				if (record.getType() == Transaction.TYPE_CASH_EXPENSE)
					records.add(record);
			}
		} else if (text.equalsIgnoreCase(CREDIT_CARD)) {
			for (BillsList record : billsLists) {
				if (record.getType() == Transaction.TYPE_CREDIT_CARD_EXPENSE)
					records.add(record);
			}
		} else if (text.equalsIgnoreCase(VOIDED)) {
			List<BillsList> allRecs = billsLists;
			for (BillsList rec : allRecs) {
				if (rec.isVoided()) {
					records.add(rec);
				}
			}

		} else if (text.equalsIgnoreCase(ALL)) {
			records.addAll(billsLists);
		}
		return records;

	}

	private Record createExpenseRecord(BillsList exp) {
		Record record = new Record(exp);
		record.add("Type", getType(exp.getType()));
		record.add("Date", getDateAsString(exp.getDate()));
		record.add("Vendor", exp.getVendorName());
		record.add("OriginalAmount", exp.getOriginalAmount());
		record.add("Balance", exp.getBalance());
		return record;
	}

	private String getType(int type) {
		switch (type) {
		case Transaction.TYPE_CASH_EXPENSE:
			return getConstants().cashExpense();
		case Transaction.TYPE_CREDIT_CARD_EXPENSE:
			return getConstants().creditCardExpense();
		default:
			break;
		}
		return null;
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
