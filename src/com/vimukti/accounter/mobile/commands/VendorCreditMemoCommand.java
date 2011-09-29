package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.VendorCreditMemo;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class VendorCreditMemoCommand extends AbstractTransactionCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("supplier", false, true));
		list.add(new ObjectListRequirement("items", false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("quantity", true, true));
				list.add(new Requirement("price", true, true));
				list.add(new Requirement("discount", true, true));
				list.add(new Requirement("vatCode", true, true));
			}
		});

		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement("number", true, false));
		list.add(new Requirement("contact", true, true));
		list.add(new Requirement("phone", true, true));
		list.add(new Requirement(MEMO, true, true));
	}

	@Override
	public Result run(Context context) {
		Result result = context.makeResult();
		result = createSupplierRequirement(context);
		if (result != null) {
			return result;
		}
		result = itemsRequirement(context);
		if (result != null) {
			return result;
		}

		result = createOptionalRequirement(context);
		if (result != null) {
			return result;
		}

		completeProcess(context);
		markDone();
		return null;
	}

	private Result createOptionalRequirement(Context context) {
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

		Requirement supplierReq = get("supplier");
		Vendor supplier = (Vendor) supplierReq.getValue();

		selection = context.getSelection("values");
		if (supplier == selection) {
			return createSupplierRequirement(context);
		}

		Record supplierRecord = new Record(supplier);
		supplierRecord.add("Name", "Supplier");
		supplierRecord.add("Value", supplier.getName());

		list.add(supplierRecord);

		Result result = dateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = creditNoteNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = contactRequirement(context, list, selection, supplier);
		if (result != null) {
			return result;
		}

		result = phoneRequirement(context, list, (String) selection);
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, "memo",
				"Add a memo");
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add("Suppiler Credit is ready to create with following values.");
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
		finish.add("", "Finish to create Supplier Credit.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result creditNoteNoRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("number");
		String creditnoteno = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(ORDER_NO)) {
			String order = context.getSelection(NUMBER);
			if (order == null) {
				order = context.getString();
			}
			creditnoteno = order;
			req.setValue(creditnoteno);
		}

		if (selection == creditnoteno) {
			context.setAttribute(INPUT_ATTR, ORDER_NO);
			return number(context, "Enter Credit Note No", creditnoteno);
		}

		Record creditNoteNoRec = new Record(creditnoteno);
		creditNoteNoRec.add("Name", "Credit Note No");
		creditNoteNoRec.add("Value", creditnoteno);
		list.add(creditNoteNoRec);
		return null;
	}

	private void completeProcess(Context context) {
		Company company = context.getCompany();
		VendorCreditMemo vendorCreditMemo = new VendorCreditMemo();

		Date date = get(DATE).getValue();
		vendorCreditMemo.setDate(new FinanceDate(date));

		String number = get("number").getValue();
		vendorCreditMemo.setNumber(number);

		List<TransactionItem> items = get("items").getValue();
		List<TransactionItem> accounts = get("accounts").getValue();
		items.addAll(accounts);
		vendorCreditMemo.setTransactionItems(items);

		// TODO Contact
		Contact contact = get("contact").getValue();
		// TODO Location
		// TODO Class
		// TODO Phone
		String phone = get("phone").getValue();

		if (company.getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			TAXCode taxCode = get("tax").getValue();
			for (TransactionItem item : items) {
				item.setTaxCode(taxCode);
			}
		}

		Vendor supplier = get("supplier").getValue();
		vendorCreditMemo.setVendor(supplier);

		String memo = get(MEMO).getValue();
		vendorCreditMemo.setMemo(memo);
		vendorCreditMemo.setTotal(getTransactionTotal(items, company));
		create(vendorCreditMemo, context);
	}
}
