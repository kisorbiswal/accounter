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

	private static final String CURRENT_VIEW = "currentView";
	private static String OPEN = "open";
	private static String COMPLETED = "completed";
	private static String CANCELLED = "cancelled";

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
				context.setAttribute(CURRENT_VIEW, "open");
				break;
			case COMPLETED:
				context.setAttribute(CURRENT_VIEW, "completed");
				break;
			case CANCELLED:
				context.setAttribute(CURRENT_VIEW, "cancelled");
				break;
			case ALL:
				context.setAttribute(CURRENT_VIEW, null);
				break;
			default:
				break;
			}
		}

		Result result = salesOrderList(context, selection);
		return result;
	}

	private Result salesOrderList(Context context, ActionNames selection) {

		Result result = context.makeResult();
		ResultList salesList = new ResultList("salesOrderList");
		result.add(getConstants().salesOrderList());

		String currentView = (String) context.getAttribute(CURRENT_VIEW);
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
		inActiveRec.add("", getConstants().open());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.COMPLETED);
		inActiveRec.add("", getConstants().completed());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.CANCELLED);
		inActiveRec.add("", getConstants().cancelled());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.ALL);
		inActiveRec.add("", getConstants().all());
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.FINISH);
		inActiveRec.add("", getConstants().close());
		actions.add(inActiveRec);

		result.add(actions);

		CommandList commandList = new CommandList();
		commandList.add(getConstants().newSalesOrder());
		result.add(commandList);

		return result;
	}

	private Record createNewSalesOrderRecord(SalesOrdersList order) {
		Record record = new Record(order);
		record.add("Name", order.getCustomerName());
		record.add("Date", getDateAsString(order.getDate()));
		record.add("Order No", order.getNumber());
		record.add("Phone", order.getPhone());
		record.add("TotalPrice", order.getTotal());
		return record;
	}

	private List<SalesOrdersList> getSalesOrders(Context context,
			String currentView) {
		FinanceTool tool = new FinanceTool();
		List<SalesOrdersList> salesOrders;
		List<SalesOrdersList> result = new ArrayList<SalesOrdersList>();
		try {
			salesOrders = tool.getSalesManager().getSalesOrdersList(
					context.getCompany().getID());

			if (currentView == null) {
				return salesOrders;
			}
			if (salesOrders != null) {
				for (SalesOrdersList salesOrder : salesOrders) {
					if (currentView.equals(OPEN)) {
						if (salesOrder.getStatus() == Transaction.STATUS_OPEN
								|| salesOrder.getStatus() == Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED) {
							result.add(salesOrder);
						}
						continue;
					}
					if (currentView.equals(COMPLETED)) {
						if (salesOrder.getStatus() == Transaction.STATUS_COMPLETED)
							result.add(salesOrder);
						continue;
					}
					if (currentView.equals(CANCELLED)) {
						if (salesOrder.getStatus() == Transaction.STATUS_CANCELLED)
							result.add(salesOrder);
						continue;
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
