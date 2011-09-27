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

	private Result viewTypeRequirement(Context context, ResultList list,
			Object selection) {

		Object viewType = context.getSelection(BILLS_VIEW_BY);
		Requirement viewReq = get(BILLS_VIEW_BY);
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

	private List<String> getViewTypes() {
		List<String> list = new ArrayList<String>();
		list.add("All");
		list.add("Open");
		list.add("Voided");
		list.add("OverDue");

		return list;
	}

	private Record createViewTypeRecord(String view) {
		Record record = new Record(view);
		record.add("Name", "ViewType");
		record.add("Value", view);
		return record;
	}

}
