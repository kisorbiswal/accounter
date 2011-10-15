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

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class BillsListCommand extends AbstractTransactionCommand {

	private static final String BILLS_VIEW_BY = "BillsViewBy";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(BILLS_VIEW_BY, true, true));

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

		Object selection = context.getSelection(BILLS_VIEW_BY);

		ResultList list = new ResultList("viewlist");
		Result result = viewTypeRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		String viewType = get(BILLS_VIEW_BY).getValue();
		result = billsList(context, viewType);
		return result;
	}

	private Result billsList(Context context, String viewType) {
		Result result = context.makeResult();
		result.add("Bills and Expenses List");
		ResultList billsListData = new ResultList("billsList");
		int num = 0;
		List<BillsList> bills = getBills(viewType);
		List<BillsList> expenses = getExpenses(viewType);
		for (BillsList bill : bills) {
			billsListData.add(createBillRecord(bill));
			num++;
			if (num == BILLS_TO_SHOW) {
				break;
			}
		}
		num = 0;
		for (BillsList bill : expenses) {
			billsListData.add(createBillRecord(bill));
			num++;
			if (num == EXPENSES_TO_SHOW) {
				break;
			}
		}
		int size = billsListData.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Bill or Expense");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(billsListData);
		result.add(commandList);
		result.add("Type for Bill or Expense");

		return result;
	}

	private Record createBillRecord(BillsList bill) {

		Record rec = new Record(bill);
		rec.add("Type", bill.getType());
		rec.add("No", bill.getNumber());
		rec.add("VendorName", bill.getVendorName());
		rec.add("OrginalAmount", bill.getOriginalAmount());
		rec.add("Balance", bill.getBalance());

		return rec;
	}

	@Override
	protected List<String> getViewTypes() {
		List<String> list = new ArrayList<String>();
		list.add(Accounter.constants().all());
		list.add(Accounter.constants().open());
		list.add(Accounter.constants().Voided());
		list.add(Accounter.constants().overDue());

		return list;
	}
}
