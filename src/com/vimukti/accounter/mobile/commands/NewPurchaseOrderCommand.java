package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.AccountTransaction;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.PurchaseOrder;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class NewPurchaseOrderCommand extends AbstractTransactionCommand {

	private static final String INPUT_ATTR = "input";
	private static final String STATUS = "status";
	private static final String NAME = "name";
	private static final int CONTACTS_TO_SHOW = 5;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement("vendor", false, true));
		list.add(new Requirement("status", false, true));
		list.add(new ObjectListRequirement("accounts", false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("amount", true, true));
				list.add(new Requirement("discount", true, true));
				list.add(new Requirement("total", true, true));
			}
		});

		list.add(new ObjectListRequirement("items", false, true) {
			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("quantity", true, true));
				list.add(new Requirement("price", true, true));
				list.add(new Requirement("discount", true, true));
				list.add(new Requirement("total", true, true));
			}
		});

		list.add(new Requirement("contact", true, false));
		list.add(new Requirement("phone", true, true));
		list.add(new Requirement("billto", true, false));
		list.add(new Requirement("date", true, true));
		list.add(new Requirement("orderno", true, true));
		list.add(new Requirement("orderorderno", true, true));
		list.add(new Requirement("paymentterms", true, true));
		list.add(new Requirement("duedate", true, true));
		list.add(new Requirement("dispatchdate", true, true));
		list.add(new Requirement("receiveddate", true, true));

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
			}
			if (process.equals(ACCOUNTS_PROCESS)) {
				result = transactionAccountProcess(context);
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

		result = VendorRequirement(context);
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

	private Result VendorRequirement(Context context) {
		Requirement vendReq = get("vendor");
		Vendor vendor = context.getSelection("vendors");
		if (vendor != null) {
			vendReq.setValue(vendor);
		}
		if (!vendReq.isDone()) {
			return vendors(context);
		}
		return null;
	}

	/**
	 * 
	 * @param context
	 */
	private void completeProcess(Context context) {

		PurchaseOrder newPurchaseOrder = new PurchaseOrder();

		Vendor vendor = get("vendor").getValue();
		newPurchaseOrder.setVendor(vendor);

		newPurchaseOrder.setPhone((String) get("phone").getValue());

		newPurchaseOrder.setStatus((Integer) get(STATUS).getValue());

		newPurchaseOrder.setNumber((String) get(ORDER_NO).getValue());

		PaymentTerms newPaymentTerms = get(PAYMENT_TERMS).getValue();
		newPurchaseOrder.setPaymentTerm(newPaymentTerms);

		Date dueDate = get("duedate").getValue();
		newPurchaseOrder.setDate(new FinanceDate(dueDate));

		Date receivedDate = get("receiveddate").getValue();
		newPurchaseOrder.setDate(new FinanceDate(receivedDate));

		Date dispatchDate = get("dispatchdate").getValue();
		newPurchaseOrder.setDate(new FinanceDate(dispatchDate));

		List<TransactionItem> items = get("items").getValue();
		newPurchaseOrder.setTransactionItems(items);

		Set<AccountTransaction> accountTransactionEntriesList = get("accounts")
				.getValue();
		newPurchaseOrder
				.setAccountTransactionEntriesList(accountTransactionEntriesList);

		create(newPurchaseOrder, context);
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

		Requirement vendrReq = get("vendor");
		Vendor vendor = (Vendor) vendrReq.getValue();

		ResultList list = new ResultList("values");

		Record custRecord = new Record(vendor);
		custRecord.add("Name", "Vendor");
		custRecord.add("Value", vendor.getName());

		list.add(custRecord);

		Result result = contactRequirement(context, list, selection, vendor);
		if (result != null) {
			return result;
		}

		result = paymentTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = dueDateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = dispatchDateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = receivedDateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = orderNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = vendorOrderNoRequirement(context, list, selection);
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

	private Result receivedDateRequirement(Context context, ResultList list,
			Object selection) {
		// TODO Auto-generated method stub
		return null;
	}

	private Result dispatchDateRequirement(Context context, ResultList list,
			Object selection) {
		// TODO Auto-generated method stub
		return null;
	}

	private Result vendorOrderNoRequirement(Context context, ResultList list,
			Object selection) {
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

	/**
	 * Contact requirement checking
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @param vendor
	 * @return
	 */
	private Result contactRequirement(Context context, ResultList list,
			Object selection, Vendor vendor) {
		Object contactObj = context.getSelection(CONTACTS);
		Requirement contactReq = get("contact");
		Contact contact = (Contact) contactReq.getValue();
		if (selection == contact) {
			return contactList(context, vendor, contact);

		}
		if (contactObj != null) {
			contact = (Contact) contactObj;
			contactReq.setValue(contact);
		}

		Record contactRecord = new Record(contact);
		contactRecord.add("Name", "Customer Contact");
		contactRecord.add("Value", contact.getName());
		list.add(contactRecord);
		return null;
	}

	private Result contactList(Context context, Vendor vendor,
			Contact oldContact) {
		Set<Contact> contacts = vendor.getContacts();
		ResultList list = new ResultList(CONTACTS);
		int num = 0;
		if (oldContact != null) {
			// list.add(createContactRecord(oldContact));
			num++;
		}
		for (Contact contact : contacts) {
			if (contact != oldContact) {
				// list.add(createContactRecord(contact));
				num++;
			}
			if (num == CONTACTS_TO_SHOW) {
				break;
			}
		}

		Result result = context.makeResult();
		result.add("Select " + vendor.getName() + "'s Contact");
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create Contact");
		result.add(commandList);

		return result;
	}

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
