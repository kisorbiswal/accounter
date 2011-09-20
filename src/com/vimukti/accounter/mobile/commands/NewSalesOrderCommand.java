package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.SalesOrder;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class NewSalesOrderCommand extends AbstractTransactionCommand {

	private static final String CUSTOMER = "customer";
	private static final String STATUS = "status";
	private static final String ITEMS = "items";
	private static final String NAME = "name";
	private static final String DESC = "description";
	private static final String QUANTITY = "quantity";
	private static final String PRICE = "price";
	private static final String DISCOUNT = "discount";
	private static final String TOTAL = "total";
	private static final String CONTACT = "contact";
	private static final String PHONE = "phone";
	private static final String BILL_TO = "billTo";
	private static final String DATE = "date";
	private static final String ORDER_NO = "orderNo";
	private static final String CUSTOMER_ORDERNO = "customerOrderNo";
	private static final String PAYMENT_TERMS = "paymentTers";
	private static final String SHIPPING_TERMS = "shippingTerms";
	private static final String SHIPPING_METHODS = "shippingMethods";
	private static final String DUE_DATE = "dueDate";
	private static final String INPUT_ATTR = "input";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(CUSTOMER, false, true));
		list.add(new Requirement(STATUS, false, true));
		list.add(new ObjectListRequirement(ITEMS, false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement(NAME, false, true));
				list.add(new Requirement(DESC, true, true));
				list.add(new Requirement(QUANTITY, true, true));
				list.add(new Requirement(PRICE, true, true));
				list.add(new Requirement(DISCOUNT, true, true));
				list.add(new Requirement(TOTAL, true, true));
			}
		});

		list.add(new Requirement(CONTACT, true, false));
		list.add(new Requirement(PHONE, true, true));
		list.add(new Requirement(BILL_TO, true, false));
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(ORDER_NO, true, true));
		list.add(new Requirement(CUSTOMER_ORDERNO, true, true));
		list.add(new Requirement(PAYMENT_TERMS, true, true));
		list.add(new Requirement(SHIPPING_TERMS, true, true));
		list.add(new Requirement(SHIPPING_METHODS, true, true));
		list.add(new Requirement(DUE_DATE, true, true));

	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = customerRequirement(context);
		if (result != null) {
			return result;
		}

		result = statusRequirement(context);
		if (result != null) {
			return result;
		}

		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}

		createSalesOrderItem(context);

		return null;
	}

	private Result createOptionalResult(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("values");

		Requirement custmerReq = get("customer");
		Customer customer = (Customer) custmerReq.getValue();

		ResultList list = new ResultList("values");

		Record custRecord = new Record(customer);
		custRecord.add("Name", "Customer");
		custRecord.add("Value", customer.getName());

		list.add(custRecord);

		Result result = contactRequirement(context, list, selection, customer);
		if (result != null) {
			return result;
		}

		result = paymentTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = billToRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = shipToRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = dueDateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = orderNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add(" Item is ready to create with following values.");
		result.add(list);
		ResultList actions = new ResultList("actions");
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Item.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	/**
	 * order no requirement
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result orderNoRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get("orderNo");
		String orderNo = (String) req.getDefaultValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("orderNo")) {
			String order = context.getSelection(NUMBER);
			if (order == null) {
				order = context.getString();
			}
			orderNo = order;
			req.setDefaultValue(orderNo);
		}

		if (selection == orderNo) {
			context.setAttribute(INPUT_ATTR, "orderNo");
			return number(context, "Enter Order number", orderNo);
		}

		Record orderNoRecord = new Record(orderNo);
		orderNoRecord.add("Name", "Order No");
		orderNoRecord.add("Value", orderNo);
		list.add(orderNoRecord);
		return null;
	}

	/**
	 * due date requiremnt
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result dueDateRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("due");
		Date dueDate = (Date) req.getDefaultValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("dueDate")) {
			Date date = context.getSelection(DATE);
			if (date == null) {
				date = context.getDate();
			}
			dueDate = date;
			req.setDefaultValue(dueDate);
		}
		if (selection == dueDate) {
			context.setAttribute(INPUT_ATTR, "dueDate");
			return date(context, "Due", dueDate);
		}

		Record dueDateRecord = new Record(dueDate);
		dueDateRecord.add("Name", "Due Date");
		dueDateRecord.add("Value", dueDate.toString());
		list.add(dueDateRecord);
		return null;
	}

	/**
	 * Creating sales order item
	 * 
	 * @param context
	 * @return
	 */
	private Result createSalesOrderItem(Context context) {
		SalesOrder newSalesOrder = new SalesOrder();

		Customer customer = (Customer) get(CUSTOMER).getValue();

		newSalesOrder.setCustomer(customer);

		Session session = context.getSession();
		Transaction transaction = session.beginTransaction();
		session.saveOrUpdate(newSalesOrder);
		transaction.commit();

		markDone();

		Result result = new Result();
		result.add("Sales order created successfully.");

		return result;
	}

	private Result statusRequirement(Context context) {
		Requirement statusReq = get(STATUS);
		if (!statusReq.isDone()) {
			String string = context.getString();
			if (string != null) {
				statusReq.setValue(string);
			} else {
				return text(context, "Please select the status", null);
			}
		}
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(NAME)) {
			statusReq.setValue(input);
		}
		return null;
	}
}
