package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.SalesOrder;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.ShippingTerms;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
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
	private static final String PAYMENT_TERMS = "paymentTerms";
	private static final String SHIPPING_TERMS = "shippingTerms";
	private static final String SHIPPING_METHODS = "shippingMethods";
	private static final String DUE_DATE = "dueDate";
	private static final String INPUT_ATTR = "input";
	private int SHIPPINGTERMS_TO_SHOW = 5;
	private int SHIPPINGMETHODS_TO_SHOW = 5;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * here in add requirement we add the items which are required for the
	 * command
	 */
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
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = null;
		if (process != null) {
			if (process.equals(ADDRESS_PROCESS)) {
				result = addressProcess(context);
				if (result != null) {
					return result;
				}
			} else if (process.equals(TRANSACTION_ITEM_PROCESS)) {
				result = transactionItemProcess(context);
				if (result != null) {
					return result;
				}
			}
		}

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
		completeProcess(context);
		markDone();
		return null;

	}

	/**
	 * 
	 * @param context
	 */
	private void completeProcess(Context context) {
		
		SalesOrder newSalesOrder = new SalesOrder();

		Customer customer = get(CUSTOMER).getValue();
		newSalesOrder.setCustomer(customer);

		newSalesOrder.setPhone((String) get(PHONE).getValue());
		
		newSalesOrder.setStatus((Integer) get(STATUS).getValue());

		newSalesOrder.setNumber((String) get(ORDER_NO).getValue());
		
		newSalesOrder.setCustomerOrderNumber((String) get(CUSTOMER_ORDERNO).getValue());
		
		PaymentTerms newPaymentTerms = get(PAYMENT_TERMS).getValue();
		newSalesOrder.setPaymentTerm(newPaymentTerms);
		
		ShippingTerms newShippingTerms = get(SHIPPING_TERMS).getValue();
		newSalesOrder.setShippingTerm(newShippingTerms);
		
		ShippingMethod newShippingMethod = get(SHIPPING_METHODS).getValue();
		newSalesOrder.setShippingMethod(newShippingMethod);
		
		Date date = get(DUE_DATE).getValue();
		newSalesOrder.setDate(new FinanceDate(date));
		
		List<TransactionItem> items = get(ITEMS).getValue();
		newSalesOrder.setTransactionItems(items);
		
		create(newSalesOrder, context);
	}

	/**
	 * Creating optional results
	 * 
	 * @param context
	 * @return
	 */
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
		result = shippingMethodRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = shippingTermsRequirement(context, list, selection);
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
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result shippingTermsRequirement(Context context, ResultList list,
			Object selection) {
		Object shippingObj = context.getSelection(SHIPPING_TERMS);
		Requirement shippingTermsReq = get("shippingTerms");
		ShippingTerms shippingTerm = (ShippingTerms) shippingTermsReq
				.getValue();

		if (selection == shippingTerm) {
			return shippingTerms(context, shippingTerm);

		}
		if (shippingObj != null) {
			shippingTerm = (ShippingTerms) shippingObj;
			shippingTermsReq.setValue(shippingTerm);
		}

		Record shippingTermRecord = new Record(shippingTerm);
		shippingTermRecord.add("Name", "Shipping Terms");
		shippingTermRecord.add("Value", shippingTerm.getName());
		list.add(shippingTermRecord);
		return null;
	}

	private Result shippingTerms(Context context, ShippingTerms oldshippingTerm) {
		List<ShippingTerms> shippingTerms = getShippingTerms();
		Result result = context.makeResult();
		result.add("Select ShippingTerms");

		ResultList list = new ResultList(SHIPPING_TERMS);
		int num = 0;
		if (oldshippingTerm != null) {
			list.add(createShippingTermRecord(oldshippingTerm));
			num++;
		}
		for (ShippingTerms term : shippingTerms) {
			if (term != oldshippingTerm) {
				list.add(createShippingTermRecord(term));
				num++;
			}
			if (num == SHIPPINGTERMS_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create ShippingTerms");
		result.add(commandList);
		return result;
	}

	private Record createShippingTermRecord(ShippingTerms oldshippingTerm) {
		Record record = new Record(oldshippingTerm);
		record.add("Name", oldshippingTerm.getName());
		record.add("Desc", oldshippingTerm.getDescription());
		return record;
	}

	private List<ShippingTerms> getShippingTerms() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result shippingMethodRequirement(Context context, ResultList list,
			Object selection) {
		Object shippingMethodObj = context.getSelection(SHIPPING_TERMS);
		Requirement shippingMethodReq = get("shippingTerms");
		ShippingMethod shippingMethods = (ShippingMethod) shippingMethodReq
				.getValue();

		if (selection == shippingMethods) {
			return shippingMethods(context, shippingMethods);

		}
		if (shippingMethodObj != null) {
			shippingMethods = (ShippingMethod) shippingMethodObj;
			shippingMethodReq.setValue(shippingMethods);
		}

		Record shippingTermRecord = new Record(shippingMethods);
		shippingTermRecord.add("Name", "Shipping Terms");
		shippingTermRecord.add("Value", shippingMethods.getName());
		list.add(shippingTermRecord);
		return null;
	}

	private Result shippingMethods(Context context,
			ShippingMethod oldShippingMethods) {
		List<ShippingMethod> shippingMethods = getShippingMethods();
		Result result = context.makeResult();
		result.add("Select Shipping Methods");

		ResultList list = new ResultList(SHIPPING_TERMS);
		int num = 0;
		if (oldShippingMethods != null) {
			list.add(createShippingMethodsRecord(oldShippingMethods));
			num++;
		}
		for (ShippingMethod term : shippingMethods) {
			if (term != oldShippingMethods) {
				list.add(createShippingMethodsRecord(term));
				num++;
			}
			if (num == SHIPPINGMETHODS_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create ShippingMethods");
		result.add(commandList);
		return result;
	}

	private Record createShippingMethodsRecord(ShippingMethod oldShippingMethods) {
		Record record = new Record(oldShippingMethods);
		record.add("Name", oldShippingMethods.getName());
		record.add("Desc", oldShippingMethods.getDescription());
		return record;
	}

	private List<ShippingMethod> getShippingMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * due date requirement
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result dueDateRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("due");
		Date dueDate = (Date) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("dueDate")) {
			Date date = context.getSelection(DATE);
			if (date == null) {
				date = context.getDate();
			}
			dueDate = date;
			req.setValue(dueDate);
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

	// /**
	// * bill requirement
	// *
	// * @param context
	// * @param list
	// * @param selection
	// * @return
	// */
	// private Result billToRequirement(Context context, ResultList list,
	// Object selection) {
	// Requirement req = get("billTo");
	// Address billTo = (Address) req.getValue();
	//
	// String attribute = (String) context.getAttribute(INPUT_ATTR);
	// if (attribute.equals("billTo")) {
	// Address input = context.getSelection("address");
	// if (input == null) {
	// input = context.getAddress();
	// }
	// billTo = input;
	// req.setDefaultValue(billTo);
	// }
	//
	// if (selection == billTo) {
	// context.setAttribute(INPUT_ATTR, "billTo");
	// return address(context, "Bill to Address", billTo);
	// }
	//
	// Record billToRecord = new Record(billTo);
	// billToRecord.add("Name", "Bill To");
	// billToRecord.add("Value", billTo.toString());
	// list.add(billToRecord);
	// return null;
	// }
	//
	// /**
	// * payment terms requirement
	// *
	// * @param context
	// * @param list
	// * @param selection
	// * @return
	// */
	// private Result paymentTermRequirement(Context context, ResultList list,
	// Object selection) {
	// Object payamentObj = context.getSelection(PAYMENT_TERMS);
	// Requirement paymentReq = get("paymentTerms");
	// PaymentTerms paymentTerm = (PaymentTerms) paymentReq.getValue();
	//
	// if (selection == paymentTerm) {
	// return paymentTerms(context, paymentTerm);
	//
	// }
	// if (payamentObj != null) {
	// paymentTerm = (PaymentTerms) payamentObj;
	// paymentReq.setDefaultValue(paymentTerm);
	// }
	//
	// Record paymentTermRecord = new Record(paymentTerm);
	// paymentTermRecord.add("Name", "Payment Terms");
	// paymentTermRecord.add("Value", paymentTerm.getName());
	// list.add(paymentTermRecord);
	// return null;
	// }
	//
	// /**
	// * Contact requirement checking
	// *
	// * @param context
	// * @param list
	// * @param selection
	// * @param customer2
	// * @return
	// */
	// private Result contactRequirement(Context context, ResultList list,
	// Object selection, Customer customer2) {
	// Object contactObj = context.getSelection(CONTACTS);
	// Requirement contactReq = get("contact");
	// Contact contact = (Contact) contactReq.getValue();
	// if (selection == contact) {
	// return contactList(context, customer2, contact);
	//
	// }
	// if (contactObj != null) {
	// contact = (Contact) contactObj;
	// contactReq.setDefaultValue(contact);
	// }
	//
	// Record contactRecord = new Record(contact);
	// contactRecord.add("Name", "Customer Contact");
	// contactRecord.add("Value", contact.getName());
	// list.add(contactRecord);
	// return null;
	// }

	/**
	 * 
	 * @param context
	 * @return
	 */
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
