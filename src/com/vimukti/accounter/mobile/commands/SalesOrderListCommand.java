package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.SalesOrder;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class SalesOrderListCommand extends AbstractTransactionCommand {

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
		result = salesOrderList(context, viewType);
		return result;
	}

	private Result viewTypeRequirement(Context context, ResultList list,
			Object selection) {

		Object viewType = context.getSelection(CURRENT_VIEW);
		Requirement viewReq = get(CURRENT_VIEW);
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

	private Result salesOrderList(Context context, String viewType) {
		Result result = context.makeResult();
		result.add("Sales Order List");
		ResultList expensesList = new ResultList("salesOrderList");
		int num = 0;
		List<SalesOrder> orders = getSalesOrder(context.getHibernateSession(),
				viewType);
		for (SalesOrder order : orders) {
			expensesList.add(createNewSalesOrderRecord(order));
			num++;
			if (num == EXPENSES_TO_SHOW) {
				break;
			}
		}
		result.add(expensesList);
		return result;
	}

	private Record createNewSalesOrderRecord(SalesOrder order) {

		Record record = new Record(order);
		record.add("customer", order.getCustomer());
		record.add("phone", order.getPhone());
		record.add("status", order.getStatus());
		record.add("orderNo", order.getNumber());
		record.add("paymentTerms", order.getPaymentTerm());
		record.add("shippingMethods", order.getShippingMethod());
		record.add("shippingTerms", order.getShippingTerm());

		return record;
	}

	private List<SalesOrder> getSalesOrder(Session hibernateSession,
			String viewType) {
		// TODO Auto-generated method stub
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

	private Record createViewTypeRecord(String view) {
		Record record = new Record(view);
		record.add("Name", "ViewType");
		record.add("Value", view);
		return record;
	}

	private List<String> getViewTypes() {
		List<String> list = new ArrayList<String>();
		list.add("Open");
		list.add("Completed");
		list.add("Canceled");
		return list;
	}

}
