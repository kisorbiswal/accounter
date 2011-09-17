package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class NewInvoiceCommand extends AbstractTransactionCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("customer", false, true));
		list.add(new ObjectListRequirement("items", false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("quantity", true, true));
				list.add(new Requirement("price", true, true));
				list.add(new Requirement("vatCode", true, true));
			}
		});

		list.add(new Requirement("date", true, true));
		list.add(new Requirement("number", true, false));
		list.add(new Requirement("paymentTerms", true, true));
		list.add(new Requirement("contact", true, true));
		list.add(new Requirement("billTo", true, true));
		list.add(new Requirement("shipTo", true, true));
		list.add(new Requirement("due", true, true));
		list.add(new Requirement("orderNo", true, true));
		list.add(new Requirement("memo", true, true));
	}

	@Override
	public Result run(Context context) {
		Result result = customerRequirement(context);
		if (result == null) {
			// TODO
		}

		result = itemsRequirement(context);
		if (result == null) {
			// TODO
		}

		result = createOptionalResult(context);
		if (result == null) {
			// TODO
		}

		markDone();
		return null;
	}

	private Result createOptionalResult(Context context) {
		Object selection = context.getSelection("actions");
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_ITEMS:
				return itemsResult(context);
			case FINISH:
				return null;
			default:
				break;
			}
		}

		Requirement itemsReq = get("items");
		List<TransactionItem> transItems = itemsReq.getValue();

		selection = context.getSelection("transactionItems");
		if (selection != null) {
			TransactionItem transItem = (TransactionItem) selection;
			transItems.add(transItem);
		}

		Requirement custmerReq = get("customer");
		Customer customer = (Customer) custmerReq.getValue();

		selection = context.getSelection("values");
		if (customer == selection) {
			return customerRequirement(context);
		}

		Result result = context.makeResult();
		result.add("Invoice is ready to create with following values.");
		ResultList list = new ResultList("values");

		Record custRecord = new Record(customer);
		custRecord.add("Name", "Customer");
		custRecord.add("Value", customer.getName());

		list.add(custRecord);

		result = invoiceDateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = contactRequirement(context, list, selection, customer);

		// list.add(paymentTermRequirement(context));
		//
		// list.add(invoiceNoRequirement(context));
		//
		// list.add(billToRequirement(context));
		//
		// list.add(shipToRequirement(context));
		//
		// list.add(dueDateRequirement(context));
		//
		// list.add(orderNoRequirement(context));
		//
		// list.add(memoRequirement(context));

		result.add(list);

		result.add("Items:-");
		ResultList items = new ResultList("transactionItems");
		for (TransactionItem item : transItems) {
			Record itemRec = new Record(item);
			itemRec.add("Name", item.getItem().getName());
			itemRec.add("Total", item.getLineTotal());
			itemRec.add("VatCode", item.getVATfraction());
		}
		result.add(items);

		ResultList actions = new ResultList("actions");
		Record moreItems = new Record(ActionNames.ADD_MORE_ITEMS);
		moreItems.add("", "Add more items");
		actions.add(moreItems);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Invoice.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Record memoRequirement(Context context) {
		Requirement memoReq = get("memo");
		String memo = (String) memoReq.getDefaultValue();
		Record memoRecord = new Record(memo);
		memoRecord.add("Name", "Order No");
		memoRecord.add("Value", memo);
		return memoRecord;
	}

	private Record orderNoRequirement(Context context) {
		Requirement orderNoReq = get("orderNo");
		String orderNo = (String) orderNoReq.getDefaultValue();
		Record orderNoRecord = new Record(orderNo);
		orderNoRecord.add("Name", "Order No");
		orderNoRecord.add("Value", orderNo);
		return orderNoRecord;
	}

	private Record dueDateRequirement(Context context) {
		Requirement dueDateReq = get("due");
		Date dueDate = (Date) dueDateReq.getDefaultValue();
		Record dueDateRecord = new Record(dueDate);
		dueDateRecord.add("Name", "Due Date");
		dueDateRecord.add("Value", dueDate.toString());
		return dueDateRecord;
	}

	private Record shipToRequirement(Context context) {
		Requirement shipToReq = get("shipTo");
		Address shipTo = (Address) shipToReq.getValue();
		Record shipToRecord = new Record(shipTo);
		shipToRecord.add("Name", "Ship To");
		shipToRecord.add("Value", shipTo.toString());
		return shipToRecord;
	}

	private Record billToRequirement(Context context) {
		Requirement billToReq = get("billTo");
		Address billTo = (Address) billToReq.getValue();
		Record billToRecord = new Record(billTo);
		billToRecord.add("Name", "Bill To");
		billToRecord.add("Value", billTo.toString());
		return billToRecord;
	}

	private Record invoiceNoRequirement(Context context) {
		Requirement invoiceNoReq = get("number");
		String invoiceNo = (String) invoiceNoReq.getValue();
		Record invoiceNoRec = new Record(invoiceNo);
		invoiceNoRec.add("Name", "Invoice Number");
		invoiceNoRec.add("Value", invoiceNo);
		return invoiceNoRec;
	}

	private Record paymentTermRequirement(Context context) {
		Requirement paymentReq = get("paymentTerms");
		PaymentTerms paymentTerm = (PaymentTerms) paymentReq.getValue();
		Record paymentTermRecord = new Record(paymentTerm);
		paymentTermRecord.add("Name", "Payment Terms");
		paymentTermRecord.add("Value", paymentTerm.getName());
		return paymentTermRecord;
	}

	private Result contactRequirement(Context context, ResultList list,
			Object selection, Customer customer) {
		Object contactObj = context.getSelection("contact");
		Requirement contactReq = get("contact");
		Contact contact = (Contact) contactReq.getValue();
		if (selection == contact) {
			return contactList(context, customer, "contact");

		}
		if (contactObj != null) {
			contact = (Contact) contactObj;
			contactReq.setDefaultValue(contact);
		}

		Record contactRecord = new Record(contact);
		contactRecord.add("Name", "Customer Contact");
		contactRecord.add("Value", contact.getName());
		list.add(contactRecord);
		return null;
	}

	private Result contactList(Context context, Customer customer, String string) {
		// TODO Auto-generated method stub
		return null;
	}

	private Result invoiceDateRequirement(Context context, ResultList list,
			Object selection) {
		Object date = context.getSelection("invoiceDate");
		Requirement dateReq = get("date");
		Date transDate = (Date) dateReq.getDefaultValue();
		if (selection == transDate) {
			return dateRequirement(context, "invoiceDate", transDate);

		}
		if (date != null) {
			transDate = (Date) date;
			dateReq.setDefaultValue(transDate);
		}

		Record transDateRecord = new Record(transDate);
		transDateRecord.add("Name", "Invoice Date");
		transDateRecord.add("Value", transDate.toString());
		list.add(transDateRecord);
		return null;
	}

	private Result dateRequirement(Context context, String string, Date date) {
		// TODO Auto-generated method stub
		return null;
	}
}
