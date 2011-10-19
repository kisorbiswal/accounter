package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.CompanyPreferences;
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
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;

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
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
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

		Result makeResult = context.makeResult();
		makeResult.add(" Customer is ready to create with following values.");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = customerRequirement(context, list, CUSTOMER);
		if (result != null) {
			return result;
		}

		result = itemsRequirement(context, makeResult, actions);
		if (result != null) {
			return result;
		}
		result = statusRequirement(context, list, STATUS);
		if (result != null) {
			return result;
		}
		setDefaultValues();
		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		completeProcess(context);
		markDone();
		return null;

	}

	private void setDefaultValues() {

		get(ORDER_NO).setDefaultValue(Integer.toString(1));
		get(DUE_DATE).setDefaultValue(new Date());
		get(DATE).setDefaultValue(new Date());
	}

	/**
	 * 
	 * @param context
	 */
	private void completeProcess(Context context) {

		ClientSalesOrder newSalesOrder = new ClientSalesOrder();

		ClientCustomer customer = get(CUSTOMER).getValue();
		newSalesOrder.setCustomer(customer.getID());

		newSalesOrder.setPhone((String) get(PHONE).getValue());
		int statusNumber = 0;
		if (get(STATUS).getValue() == "Open") {
			statusNumber = 1;
		} else if (get(STATUS).getValue() == "Open") {
			statusNumber = 2;
		} else if (get(STATUS).getValue() == "Open") {
			statusNumber = 3;
		}
		newSalesOrder.setStatus(statusNumber);
		Date value = get(DATE).getValue();
		newSalesOrder.setDate(new FinanceDate(value).getDate());
		newSalesOrder.setNumber((String) get(ORDER_NO).getValue());

		newSalesOrder.setCustomerOrderNumber((String) get(CUSTOMER_ORDERNO)
				.getValue());

		ClientPaymentTerms newPaymentTerms = get(PAYMENT_TERMS).getValue();
		if (newPaymentTerms != null)
			newSalesOrder.setPaymentTerm(newPaymentTerms.getID());

		CompanyPreferences preferences = context.getCompany().getPreferences();

		if (preferences.isDoProductShipMents()) {
			ClientShippingTerms newShippingTerms = get(SHIPPING_TERMS)
					.getValue();
			if (newPaymentTerms != null)
				newSalesOrder.setShippingTerm(newShippingTerms.getID());

			ClientShippingMethod newShippingMethod = get(SHIPPING_METHODS)
					.getValue();
			if (newShippingMethod != null)
				newSalesOrder.setShippingMethod(newShippingMethod.getID());
		}
		Date date = get(DUE_DATE).getValue();
		newSalesOrder.setDate(new FinanceDate(date).getDate());

		List<ClientTransactionItem> items = get(ITEMS).getValue();
		newSalesOrder.setTransactionItems(items);

		create(newSalesOrder, context);
	}

	/**
	 * Creating optional results
	 * 
	 * @param context
	 * @param makeResult
	 * @param actions
	 * @param list
	 * @return
	 */
	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {

		// context.setAttribute(INPUT_ATTR, "optional");

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

		ClientCustomer customer = (ClientCustomer) get("customer").getValue();

		Result result = contactRequirement(context, list, selection, customer);
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, DATE, "Enter Date",
				selection);
		if (result != null) {
			return result;
		}
		result = paymentTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = numberOptionalRequirement(context, list, selection, PHONE,
				"Enter Phone");
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection,
				CUSTOMER_ORDERNO, "Enter " + CUSTOMER_ORDERNO);
		if (result != null) {
			return result;
		}
		// TODO bill to was disabled so removed
		// result = billToRequirement(context, list, selection);
		// if (result != null) {
		// return result;
		// }

		CompanyPreferences preferences = context.getCompany().getPreferences();

		if (preferences.isDoProductShipMents()) {
			result = shippingMethodRequirement(context, list, selection);
			if (result != null) {
				return result;
			}

			result = shippingTermsRequirement(context, list, selection);
			if (result != null) {
				return result;
			}
		}

		result = dateOptionalRequirement(context, list, DUE_DATE,
				"Enter Due date", selection);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, ORDER_NO,
				"Enter Order Number");
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Item.");
		actions.add(finish);

		return makeResult;
	}

	private Result statusRequirement(Context context, ResultList list,
			String reqName) {
		Requirement statusReq = get(reqName);
		String statuses = context.getSelection("statusmethods");

		if (statuses != null) {
			statusReq.setValue(statuses);
		}
		String status = statusReq.getValue();
		Object selection = context.getSelection("values");

		if (!statusReq.isDone() || status == selection) {
			return statusPrevious(context, null);
		}
		Record paymentTermsRecord = new Record(status);
		paymentTermsRecord.add("", "Status");
		paymentTermsRecord.add("", status);
		list.add(paymentTermsRecord);

		return null;
	}

	private Result statusPrevious(Context context, String oldstatus) {
		List<String> statusmethods = getstatusmethod();
		Result result = context.makeResult();
		result.add("Select Status");

		ResultList list = new ResultList("statusmethods");
		int num = 0;
		if (oldstatus != null) {
			list.add(createStatusMethodRecord(oldstatus));
			num++;
		}
		for (String stats : statusmethods) {
			if (stats != oldstatus) {
				list.add(createStatusMethodRecord(stats));
				num++;
			}
		}
		result.add(list);
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
		ClientShippingTerms shippingTerm = (ClientShippingTerms) shippingTermsReq
				.getValue();

		if (selection != null)
			if (selection == "Shipping Terms") {
				context.setAttribute(INPUT_ATTR, "Shipping Terms");
				return shippingTerms(context, shippingTerm);
			}
		if (shippingObj != null) {
			shippingTerm = (ClientShippingTerms) shippingObj;
			shippingTermsReq.setValue(shippingTerm);
		}

		Record shippingTermRecord = new Record("Shipping Terms");
		shippingTermRecord.add("Name", "Shipping Terms");
		shippingTermRecord.add("Value", shippingTerm.getName());
		list.add(shippingTermRecord);
		return null;
	}

	private Result shippingTerms(Context context,
			ClientShippingTerms oldshippingTerm) {
		List<ClientShippingTerms> shippingTerms = getShippingTerms();
		Result result = context.makeResult();
		result.add("Select ShippingTerms");

		ResultList list = new ResultList(SHIPPING_TERMS);
		int num = 0;
		if (oldshippingTerm != null) {
			list.add(createShippingTermRecord(oldshippingTerm));
			num++;
		}
		for (ClientShippingTerms term : shippingTerms) {
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

	private Record createShippingTermRecord(ClientShippingTerms oldshippingTerm) {
		Record record = new Record(oldshippingTerm);
		record.add("Name", oldshippingTerm.getName());
		record.add("Desc", oldshippingTerm.getDescription());
		return record;
	}

	private List<ClientShippingTerms> getShippingTerms() {
		// TODO Auto-generated method stub
		return getClientCompany().getShippingTerms();
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
		ClientShippingMethod shippingMethods = (ClientShippingMethod) shippingMethodReq
				.getValue();

		if (selection != null) {
			if (selection == "Shipping Terms") {
				context.setAttribute(INPUT_ATTR, "Shipping Terms");
				return shippingMethods(context, shippingMethods);
			}
		}
		if (shippingMethodObj != null) {
			shippingMethods = (ClientShippingMethod) shippingMethodObj;
			shippingMethodReq.setValue(shippingMethods);
		}

		Record shippingTermRecord = new Record("Shipping Terms");
		shippingTermRecord.add("Name", "Shipping Terms");
		shippingTermRecord.add("Value", shippingMethods.getName());
		list.add(shippingTermRecord);
		return null;
	}

	private Result shippingMethods(Context context,
			ClientShippingMethod oldShippingMethods) {
		List<ClientShippingMethod> shippingMethods = getShippingMethods();
		Result result = context.makeResult();
		result.add("Select Shipping Methods");

		ResultList list = new ResultList(SHIPPING_TERMS);
		int num = 0;
		if (oldShippingMethods != null) {
			list.add(createShippingMethodsRecord(oldShippingMethods));
			num++;
		}
		for (ClientShippingMethod term : shippingMethods) {
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

	private Record createShippingMethodsRecord(
			ClientShippingMethod oldShippingMethods) {
		Record record = new Record(oldShippingMethods);
		record.add("Name", oldShippingMethods.getName());
		record.add("Desc", oldShippingMethods.getDescription());
		return record;
	}

	private List<ClientShippingMethod> getShippingMethods() {
		return getClientCompany().getShippingMethods();
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
		Requirement req = get("dueDate");
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

}