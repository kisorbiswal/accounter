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
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.server.FinanceTool;

public class PurchaseOrderListCommand extends AbstractTransactionCommand {

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
		
		Result result = purchaseOrderList(context, selection);
		return result;
	}

	private Result purchaseOrderList(Context context, ActionNames viewType) {

		Result result = context.makeResult();
		ResultList purchaseList = new ResultList("purchaseOrderList");
		result.add("Sales Order List");

		String currentView =(String) context.getAttribute(CURRENT_VIEW);
		List<PurchaseOrdersList> orders = getPurchaseOrder(context, currentView);

		ResultList actions = new ResultList("actions");

		List<PurchaseOrdersList> pagination = pagination(context, viewType,
				actions, orders, new ArrayList<PurchaseOrdersList>(),
				VALUES_TO_SHOW);

		for (PurchaseOrdersList purchaseOrdersList : pagination) {
			purchaseList.add(createNewPurchaseOrderRecord(purchaseOrdersList));
		}

		result.add(purchaseList);

		Record inActiveRec = new Record(ActionNames.OPEN);
		inActiveRec.add("", "Open Orders");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.COMPLETED);
		inActiveRec.add("", "Completed Orders");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.CANCELLED);
		inActiveRec.add("", "Cancelled Orders");
		actions.add(inActiveRec);
		inActiveRec = new Record(ActionNames.ALL);
		inActiveRec.add("", "All Orders");
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
			String currentView) {
		FinanceTool tool = new FinanceTool();
		List<PurchaseOrdersList> purchaseOrders;
		List<PurchaseOrdersList> result = new ArrayList<PurchaseOrdersList>();
		try {
			purchaseOrders = tool.getPurchageManager().getPurchaseOrdersList(
					context.getCompany().getID());

			if (currentView == null) {
				return purchaseOrders;
			}
			if (purchaseOrders != null) {
				for (PurchaseOrdersList purchaseOrder : purchaseOrders) {
					if (currentView.equals(OPEN)) {
						if (purchaseOrder.getStatus() == ClientTransaction.STATUS_OPEN
								|| purchaseOrder.getStatus() == ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED)
							result.add(purchaseOrder);
						continue;
					}
					if (currentView.equals(COMPLETED)) {
						if (purchaseOrder.getStatus() == ClientTransaction.STATUS_COMPLETED)
							result.add(purchaseOrder);
						continue;
					}
					if (currentView.equals(CANCELLED)) {
						if (purchaseOrder.getStatus() == ClientTransaction.STATUS_CANCELLED)
							result.add(purchaseOrder);
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

}
