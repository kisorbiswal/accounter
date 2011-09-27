package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Entry;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewJournalEntryCommand extends AbstractTransactionCommand {

	private static final String TRANSACTION_DATE = "TransactionDate";
	private static final String VOUCHER = "Voucher";
	private static final String VOUCHER_NUMBER = "Voucher Number";
	private static final String DATE = "Date";
	private static final String ACCOUNT = "Account";
	private static final String CREDIT = "Credit";
	private static final String DEBIT = "Debit";
	private static final String MEMO = "memo";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(TRANSACTION_DATE, false, true));
		list.add(new Requirement(NUMBER, false, true));
		list.add(new ObjectListRequirement(VOUCHER, false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement(VOUCHER_NUMBER, false, true));
				list.add(new Requirement(DATE, false, true));
				list.add(new Requirement(ACCOUNT, false, true));
				list.add(new Requirement(CREDIT, false, true));
				list.add(new Requirement(DEBIT, false, true));
			}
		});

		list.add(new Requirement(MEMO, true, true));

	}

	@Override
	public Result run(Context context) {

		Result result = null;
		result = journelentryNumReq(context);
		if (result != null) {
			return result;
		}
		result = entryRequirement(context);
		if (result != null) {
			return result;
		}
		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}

		return null;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result journelentryNumReq(Context context) {

		Requirement requirement = get(NUMBER);
		if (!requirement.isDone()) {
			String journelentryNum = context.getSelection(TEXT);
			if (journelentryNum != null) {
				requirement.setValue(journelentryNum);
			} else {
				return text(context, "Please enter the  Journel Entry Number",
						null);
			}
		}
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(NUMBER)) {
			requirement.setValue(input);
		}
		return null;

	}

	private Result createOptionalResult(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_ITEMS:
				return items(context);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		Result result = null;
		Requirement entriesReq = get("entries");
		List<Entry> entries = entriesReq.getValue();

		selection = context.getSelection("entries");
		if (selection != null) {
			// TODO
		}

		ResultList list = new ResultList("values");

		result = dateOptionalRequirement(context, list, DATE,
				"Enter journelEntry Date", selection);
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, MEMO,
				"Enter Memo");
		if (result != null) {
			return result;
		}
		return null;
	}

	private Result transactionDateRequirement(Context context, ResultList list,
			Object selection) {
		// TODO Auto-generated method stub
		return null;
	}

	private Result entryRequirement(Context context) {
		Requirement entryReq = get("entries");
		List<Entry> entries = context.getSelections("entries");
		if (!entryReq.isDone()) {
			if (entries.size() > 0) {
				entryReq.setValue(entries);
			} else {
				return Entries(context);
			}
		}
		if (entries != null && entries.size() > 0) {
			List<Entry> entries1 = entryReq.getValue();
			entries1.addAll(entries);
		}
		return null;
	}

	private Result Entries(Context context) {
		// TODO Auto-generated method stub

		return null;
	}

}
