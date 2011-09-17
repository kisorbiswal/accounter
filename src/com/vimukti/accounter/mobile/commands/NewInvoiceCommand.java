package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TransactionItem;
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
		return null;
	}

	private Result createOptionalResult(Context context) {
		
		Result result = context.makeResult();

		result.add("Invoice is ready to create with following values.");
		ResultList list = new ResultList();

		Customer customer = (Customer) get("customer").getValue();
		Record custRecord = new Record(customer);
		custRecord.add("Name", "Customer");
		custRecord.add("Value", customer.getName());
		list.add(custRecord);

		Date transDate = (Date) get("date").getDefaultValue();
		Record transDateRecord = new Record(transDate);
		transDateRecord.add("Name", "Invoice Date");
		transDateRecord.add("Value", transDate.toString());
		list.add(transDateRecord);

		Contact contact = (Contact) get("contact").getValue();
		Record contactRecord = new Record(contact);
		contactRecord.add("Name", "Customer Contact");
		contactRecord.add("Value", contact.getName());
		list.add(contactRecord);

		String invoiceNo = (String) get("number").getValue();
		Record invoiceNoRec = new Record(invoiceNo);
		invoiceNoRec.add("Name", "Invoice Number");
		invoiceNoRec.add("Value", invoiceNo);
		list.add(invoiceNoRec);

		PaymentTerms paymentTerm = (PaymentTerms) get("paymentTerms")
				.getValue();
		Record paymentTermRecord = new Record(paymentTerm);
		paymentTermRecord.add("Name", "Payment Terms");
		paymentTermRecord.add("Value", paymentTerm.getName());
		list.add(paymentTermRecord);

		Address billTo = (Address) get("billTo").getValue();
		Record billToRecord = new Record(billTo);
		billToRecord.add("Name", "Bill To");
		billToRecord.add("Value", billTo.toString());
		list.add(billToRecord);

		Address shipTo = (Address) get("shipTo").getValue();
		Record shipToRecord = new Record(shipTo);
		shipToRecord.add("Name", "Ship To");
		shipToRecord.add("Value", shipTo.toString());
		list.add(shipToRecord);

		Date dueDate = (Date) get("due").getDefaultValue();
		Record dueDateRecord = new Record(dueDate);
		dueDateRecord.add("Name", "Due Date");
		dueDateRecord.add("Value", dueDate.toString());
		list.add(dueDateRecord);

		String orderNo = (String) get("orderNo").getDefaultValue();
		Record orderNoRecord = new Record(orderNo);
		orderNoRecord.add("Name", "Order No");
		orderNoRecord.add("Value", orderNo);
		list.add(orderNoRecord);

		String memo = (String) get("memo").getDefaultValue();
		Record memoRecord = new Record(orderNo);
		memoRecord.add("Name", "Memo");
		memoRecord.add("Value", memo);
		list.add(memoRecord);

		result.add(list);

		result.add("Items:-");
		ResultList items = new ResultList();
		List<TransactionItem> transItems = get("items").getValue();
		for (TransactionItem item : transItems) {
			Record itemRec = new Record(item);
			itemRec.add("Name", item.getItem().getName());
			itemRec.add("Total", item.getLineTotal());
			itemRec.add("VatCode", item.getVATfraction());
		}
		result.add(items);

		ResultList actions = new ResultList();
		Record moreItems = new Record(null);
		moreItems.add("", "Add more items");
		actions.add(moreItems);
		Record finish = new Record(null);
		finish.add("", "Finish to create Invoice.");
		actions.add(finish);
		result.add(actions);

		return result;
	}
}
