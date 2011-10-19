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
import com.vimukti.accounter.web.client.core.Lists.SalesOrdersList;
import com.vimukti.accounter.web.server.FinanceTool;

public class SalesOrderListCommand extends AbstractTransactionCommand {

	private static final String CURRENT_VIEW = "open";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

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

		ActionNames selection = context.getSelection(ACTIONS);
		if (selection != null) {
			switch (selection) {
			case FINISH:
				markDone();
				return new Result();
			case OPEN:
				context.setAttribute(CURRENT_VIEW, Transaction.STATUS_OPEN);
				break;
			case COMPLETED:
				context.setAttribute(CURRENT_VIEW, Transaction.STATUS_COMPLETED);
				break;
			case CANCELLED:
				context.setAttribute(CURRENT_VIEW, Transaction.STATUS_CANCELLED);
				break;
			default:
				break;
			}
		} else {
			context.setAttribute(CURRENT_VIEW, Transaction.STATUS_OPEN);
		}

		Result result = salesOrderList(context, selection);
		return result;
	}

	private Result salesOrderList(Context context, ActionNames selection) {

		Result result = context.makeResult();
		ResultList salesList = new ResultList("salesOrderList");
		result.add("Sales Order List");

		Integer currentView = (Integer) context.getAttribute(CURRENT_VIEW);
		List<SalesOrdersList> orders = getSalesOrders(context, currentView);

		ResultList actions = new ResultList("actions");

		List<SalesOrdersList> pagination = pagination(context, selection,
				actions, orders, new ArrayList<SalesOrdersList>(),
				VALUES_TO_SHOW);

		for (SalesOrdersList salesOrdersList : pagination) {
			salesList.add(createNewSalesOrderRecord(salesOrdersList));
		}

		result.add(salesList);

		Record inActiveRec = new Record(ActionNames.OPEN);
		inActiveRec.add("", "Open Sales Orders");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.COMPLETED);
		inActiveRec.add("", "Completed  Sales Orders");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.CANCELLED);
		inActiveRec.add("", "Cancelled  Sales Orders");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.FINISH);
		inActiveRec.add("", "Close");
		actions.add(inActiveRec);

		result.add(actions);

		CommandList commandList = new CommandList();
		commandList.add("New Sales Order");
		result.add(commandList);

		return result;
	}

	private Record createNewSalesOrderRecord(SalesOrdersList order) {
		Record record = new Record(order);
		record.add("Customer", order.getCustomerName());
		record.add("Date", order.getDate());
		record.add("Order No", order.getNumber());
		record.add("Phone", order.getPhone());
		record.add("TotalPrice", order.getTotal());
		return record;
	}

	private List<SalesOrdersList> getSalesOrders(Context context,
			int currentView) {
		FinanceTool tool = new FinanceTool();
		List<SalesOrdersList> orders;
		List<SalesOrdersList> result = new ArrayList<SalesOrdersList>();
		try {
			orders = tool.getSalesManager().getSalesOrdersList(
					context.getCompany().getID());

			if (orders != null) {
				for (SalesOrdersList salesOrder : orders) {
					if (salesOrder.getStatus() == currentView) {
						result.add(salesOrder);
					}
				}
			}
			return result;
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected List<String> getViewTypes() {
		List<String> list = new ArrayList<String>();
		list.add("Open");
		list.add("Completed");
		list.add("Canceled");
		return list;
	}

}
