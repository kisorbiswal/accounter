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
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.server.FinanceTool;

public class PurchaseOrderListCommand extends AbstractTransactionCommand {

	private static final String CURRENT_VIEW = "Open";

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
		return null;
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

		Result result = purchaseOrderList(context, selection);
		return result;
	}

	private Result purchaseOrderList(Context context, ActionNames viewType) {

		Result result = context.makeResult();
		ResultList purchaseList = new ResultList("purchaseOrderList");
		result.add("Sales Order List");

		Integer currentView = (Integer) context.getAttribute(CURRENT_VIEW);
		List<PurchaseOrdersList> orders = getPurchaseOrder(context, currentView);

		ResultList actions = new ResultList("actions");

		List<PurchaseOrdersList> pagination = pagination(context, viewType,
				actions, orders, new ArrayList<PurchaseOrdersList>(),
				VALUES_TO_SHOW);

		for (PurchaseOrdersList purchaseOrdersList : pagination) {
			purchaseList.add(createNewPurchaseOrderRecord(purchaseOrdersList));
		}

		StringBuilder message = new StringBuilder();
		if (purchaseList.size() > 0) {
			message.append("Select an Purchase Order");
		}

		result.add(message.toString());
		result.add(purchaseList);

		Record inActiveRec = new Record(ActionNames.OPEN);
		inActiveRec.add("", "Open Purchase Orders");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.COMPLETED);
		inActiveRec.add("", "Completed  Purchase Orders");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.CANCELLED);
		inActiveRec.add("", "Cancelled  Purchase Orders");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.FINISH);
		inActiveRec.add("", "Close");
		actions.add(inActiveRec);

		result.add(actions);

		CommandList commandList = new CommandList();
		commandList.add("Add PurchaseOrder Command");
		result.add(commandList);

		return result;
	}

	private Record createNewPurchaseOrderRecord(PurchaseOrdersList order) {

		Record record = new Record(order);
		record.add("Supplier", order.getVendorName());
		record.add("Date", order.getDate());
		record.add("Order No", order.getNumber());
		record.add("Phone", order.getPhone());
		record.add("TotalPrice", order.getPurchasePrice());
		return record;
	}

	private List<PurchaseOrdersList> getPurchaseOrder(Context context,
			int currentView) {
		FinanceTool tool = new FinanceTool();
		List<PurchaseOrdersList> orders;
		List<PurchaseOrdersList> result = new ArrayList<PurchaseOrdersList>();
		try {
			orders = tool.getPurchageManager().getPurchaseOrdersList(
					context.getCompany().getID());

			if (orders != null) {
				for (PurchaseOrdersList purchaseOrder : orders) {
					if (purchaseOrder.getStatus() == currentView) {
						result.add(purchaseOrder);
					}
				}
			}
			return result;
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
