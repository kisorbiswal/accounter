package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;

public class NewQuoteCommand extends AbstractTransactionCommand {

	private static final String PHONE = "phone";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement("customer Name", false, true));
		list.add(new ObjectListRequirement("Item", false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("quantity", true, true));
				list.add(new Requirement("price", true, true));
				list.add(new Requirement("vatCode", true, true));
			}
		});
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement("number", true, false));
		list.add(new Requirement("paymentTerms", true, true));
		list.add(new Requirement("contact", true, true));
		list.add(new Requirement("billTo", true, true));
		list.add(new Requirement("phone", true, true));
		list.add(new Requirement("delivery Date", true, true));
		list.add(new Requirement("expiration Date", true, false));
		list.add(new Requirement(MEMO, true, true));
		if (getClientCompany().getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			list.add(new Requirement("tax", false, true));
		}
	}

	@Override
	public Result run(Context context) {
		Result result = null;
		// result = customerRequirement(context);
		if (result != null) {
			return result;
		}
		result = itemsRequirement(context, null, null);
		if (result != null) {
			return result;
		}
		Company company = context.getCompany();
		if (company.getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			Requirement taxReq = get("tax");
			TAXCode taxcode = context.getSelection(TAXCODE);
			if (!taxReq.isDone()) {
				if (taxcode != null) {
					taxReq.setValue(taxcode);
				} else {
					return taxCode(context, null);
				}
			}
			if (taxcode != null) {
				taxReq.setValue(taxcode);
			}
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

		Company company = context.getCompany();
		Estimate estimate = new Estimate();

		Customer customer = get("customer Name").getValue();
		estimate.setCustomer(customer);

		Date date = get(DATE).getValue();
		estimate.setDate(new FinanceDate(date));

		String number = get("number").getValue();
		estimate.setNumber(number);

		Contact contact = get("contact").getValue();
		estimate.setContact(contact);

		Address billTo = get("billTo").getValue();
		estimate.setAddress(billTo);

		String phone = get("phone").getValue();
		estimate.setPhone(phone);

		List<ClientTransactionItem> items = get("items").getValue();
		if (company.getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			ClientTAXCode taxCode = get("tax").getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}
		PaymentTerms paymentTerm = get("paymentTerms").getValue();
		estimate.setPaymentTerm(paymentTerm);

		Date expirationDate = get(DATE).getValue();
		estimate.setExpirationDate(new FinanceDate(expirationDate));

		Date deliveryDate = get(DATE).getValue();
		estimate.setDeliveryDate(new FinanceDate(deliveryDate));

		String memo = get(MEMO).getValue();
		estimate.setMemo(memo);

	}

	/**
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
					(ClientTransactionItem) selection);
			if (result != null) {
				return result;
			}
		}

		selection = context.getSelection("values");
		ResultList list = new ResultList("values");

		Requirement custmerReq = get("customer");
		ClientCustomer customer = (ClientCustomer) custmerReq.getValue();
		Record custRecord = new Record(customer);
		custRecord.add("Name", "Customer");
		custRecord.add("Value", customer.getName());

		list.add(custRecord);

		Result result = dateOptionalRequirement(context, list, DATE,
				"Enter date", selection);
		if (result != null) {
			return result;
		}
		result = quoteNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = contactRequirement(context, list, selection, customer);
		if (result != null) {
			return result;
		}
		result = billToRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = paymentTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, PHONE,
				"Enter phone Number");
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, "expirationDate",
				"Enter Expiration Date ", selection);
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, "deliveryDate",
				"Enter Delivery Date ", selection);
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add("Quote is ready to create with following values.");
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
		finish.add("", "Finish to create Quote.");
		actions.add(finish);
		result.add(actions);

		return null;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result quoteNoRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("number");
		String auoteNo = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("number")) {
			String order = context.getSelection(NUMBER);
			if (order == null) {
				order = context.getString();
			}
			auoteNo = order;
			req.setValue(auoteNo);
		}

		if (selection == auoteNo) {
			context.setAttribute(INPUT_ATTR, ORDER_NO);
			return number(context, "Enter quote number", auoteNo);
		}

		Record quoteNoRec = new Record(auoteNo);
		quoteNoRec.add("Name", "Quote Number");
		quoteNoRec.add("Value", auoteNo);
		list.add(quoteNoRec);
		return null;
	}
}
