package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.PurchaseOrder;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class PurchaseOrderListCommand extends AbstractTransactionCommand {

	private static final String CURRENT_VIEW = "Open";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(CURRENT_VIEW, true, true));

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

		Object selection = context.getSelection(CURRENT_VIEW);

		ResultList list = new ResultList("viewlist");
		Result result = viewTypeRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		String viewType = get(CURRENT_VIEW).getValue();
		result = purchaseOrderList(context, viewType);
		return result;
	}

	private Result purchaseOrderList(Context context, String viewType) {
		Result result = context.makeResult();
		result.add("Purchase Order List");
		ResultList purchaseList = new ResultList("purchaseOrderList");
		int num = 0;
		List<PurchaseOrder> orders = getPurchaseOrder(
				context.getHibernateSession(), viewType);
		for (PurchaseOrder order : orders) {
			purchaseList.add(createNewPurchaseOrderRecord(order));
			num++;
			if (num == EXPENSES_TO_SHOW) {
				break;
			}
		}
		result.add(purchaseList);
		return result;
	}

	private Record createNewPurchaseOrderRecord(PurchaseOrder order) {

		Record record = new Record(order);
		record.add("customer", order.getVendor());
		record.add("phone", order.getPhone());
		record.add("status", order.getStatus());
		record.add("orderNo", order.getNumber());
		record.add("paymentTerms", order.getPaymentTerm());
		record.add("shippingMethods", order.getShippingMethod());

		return record;
	}

	private List<PurchaseOrder> getPurchaseOrder(Session hibernateSession,
			String viewType) {
		// TODO Auto-generated method stub
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
