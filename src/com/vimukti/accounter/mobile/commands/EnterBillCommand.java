package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class EnterBillCommand extends AbstractTransactionCommand {

	private static final String DUE_DATE = "duedate";
	private static final String DELIVERY_DATE = "deliveryDate";
	private static final String CONTACT = "contact";
	private static final String PHONE = "phone";
	private static final String PAYMENT_TERMS = "paymentTerms";
	private static final String NUMBER = "number";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(VENDOR, false, true));
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(NUMBER, true, false));
		list.add(new Requirement(CONTACT, true, true));
		list.add(new Requirement(PHONE, true, true));
		list.add(new Requirement(PAYMENT_TERMS, true, true));
		list.add(new Requirement(DUE_DATE, true, true));
		list.add(new Requirement(DELIVERY_DATE, true, true));
		list.add(new Requirement(MEMO, true, true));

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
	}

	@Override
	public Result run(Context context) {
		Result result = null;
		String process = (String) context.getAttribute(PROCESS_ATTR);
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
		result = vendorRequirement(context);
		if (result != null) {
			return result;
		}

		result = itemsRequirement(context);
		if (result != null) {
			return result;
		}

		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();

		return result;
	}

	private void completeProcess(Context context) {

		Company company = context.getCompany();
		EnterBill enterBill = new EnterBill();
		Vendor vendor = (Vendor) get(VENDOR).getValue();
		enterBill.setVendor(vendor);
		Date date = get(DATE).getValue();
		enterBill.setDate(new FinanceDate(date));

		enterBill.setType(Transaction.TYPE_ENTER_BILL);

		String number = get("number").getValue();
		enterBill.setNumber(number);

		List<TransactionItem> items = get("items").getValue();
		enterBill.setTransactionItems(items);

		enterBill.setTotal(getTransactionTotal(items, company));

		Date dueDate = get("due").getValue();
		enterBill.setDueDate(new FinanceDate(dueDate));

		Date deliveryDate = get(DELIVERY_DATE).getValue();
		enterBill.setDueDate(new FinanceDate(deliveryDate));

		Contact contact = get(CONTACT).getValue();
		enterBill.setContact(contact);

		PaymentTerms paymentTerm = get("paymentTerms").getValue();
		enterBill.setPaymentTerm(paymentTerm);

		String phone = get(PHONE).getValue();
		enterBill.setPhone(phone);

		String memo = get(MEMO).getValue();
		enterBill.setMemo(memo);
		create(enterBill, context);

	}

	private Result createOptionalResult(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_ITEMS:
				return items(context);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		Requirement itemsReq = get("items");
		List<TransactionItem> transItems = itemsReq.getValue();

		selection = context.getSelection("transactionItems");
		if (selection != null) {
			Result result = transactionItem(context,
					(TransactionItem) selection);
			if (result != null) {
				return result;
			}
		}

		selection = context.getSelection("values");
		ResultList list = new ResultList("values");

		Requirement vendorReq = get(VENDOR);
		Vendor vendor = (Vendor) vendorReq.getValue();
		Record vendorRecord = new Record(vendor);
		vendorRecord.add("Name", VENDOR);
		vendorRecord.add("Value", vendor.getName());

		list.add(vendorRecord);

		Result result = dateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = contactRequirement(context, list, selection, vendor);
		if (result != null) {
			return result;
		}

		result = paymentTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = phoneNumberRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, "due", DUE_DATE,
				selection);

		if (result != null) {
			return result;
		}
		result = deliveryDateRequirement(context, list, selection);

		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, MEMO,
				"Enter Memo");
		if (result != null) {
			return result;
		}
		result = context.makeResult();
		result.add("Bill is ready to create with following values.");
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

		ResultList actions = new ResultList(ACTIONS);
		Record moreItems = new Record(ActionNames.ADD_MORE_ITEMS);
		moreItems.add("", "Add more items");
		actions.add(moreItems);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Bill.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result phoneNumberRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(PHONE);
		String phoneNumber = (String) req.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(PHONE)) {
			String input = context.getSelection(TEXT);
			if (input == null) {
				input = context.getString();
			}
			phoneNumber = input;
			req.setValue(phoneNumber);
		}

		if (selection == phoneNumber) {
			context.setAttribute(attribute, PHONE);
			return text(context, PHONE, phoneNumber);
		}

		Record phoneNumberRecord = new Record(phoneNumber);
		phoneNumberRecord.add("Name", PHONE);
		phoneNumberRecord.add("Value", phoneNumber);
		list.add(phoneNumberRecord);
		return null;
	}

	private Result deliveryDateRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(DELIVERY_DATE);
		Date deliveryDate = (Date) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(DELIVERY_DATE)) {
			Date date = context.getSelection(DATE);
			if (date == null) {
				date = context.getDate();
			}
			deliveryDate = date;
			req.setValue(deliveryDate);
		}
		if (selection == deliveryDate) {
			context.setAttribute(INPUT_ATTR, DELIVERY_DATE);
			return date(context, "Enter the " + DELIVERY_DATE, deliveryDate);
		}

		Record dueDateRecord = new Record(deliveryDate);
		dueDateRecord.add("Name", DELIVERY_DATE);
		dueDateRecord.add("Value", deliveryDate.toString());
		list.add(dueDateRecord);
		return null;

	}

}
