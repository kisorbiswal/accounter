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

public class NewVendorCreditMemoCommand extends AbstractTransactionCommand {
	private static final String NUMBER = "number";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(SUPPLIER, false, true));
		list.add(new ObjectListRequirement(ITEMS, false, true) {

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
		list.add(new Requirement(NUMBER, true, false));
		list.add(new Requirement(CONTACT, true, true));
		list.add(new Requirement(PHONE, true, true));
		list.add(new Requirement(MEMO, true, true));
	}

	@Override
	public Result run(Context context) {
		setDefaultValues(context);
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = context.makeResult();
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
		setTransactionType(VENDOR_TRANSACTION);
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

	private void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new Date());
		get(NUMBER).setDefaultValue("1");
		get(PHONE).setDefaultValue("");
		Contact contact = new Contact();
		contact.setName(null);
		get(CONTACT).setDefaultValue(contact);
		get(MEMO).setDefaultValue("");
	}

	private Result createOptionalRequirement(Context context) {
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}

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
		Requirement itemsReq = get(ITEMS);
		List<TransactionItem> transItems = itemsReq.getValue();

		selection = context.getSelection("transactionItems");
		if (selection != null) {
			Result result = transactionItem(context,
					(TransactionItem) selection);
			if (result != null) {
				return result;
			}
		}
		ResultList list = new ResultList("values");

		Requirement supplierReq = get(SUPPLIER);
		Vendor supplier = (Vendor) supplierReq.getValue();

		selection = context.getSelection("values");
		if (supplier == selection) {
			return vendors(context);
		}

		Record supplierRecord = new Record(supplier);
		supplierRecord.add("Supplier Name", supplier.getName());
		// supplierRecord.add("Value", supplier.getName());

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
			itemRec.add(", Total", item.getLineTotal());
			itemRec.add(", VatCode", item.getVATfraction());
			items.add(itemRec);
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
		Requirement req = get(NUMBER);
		String creditnoteno = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(ORDER_NO)) {
			String order = context.getSelection(NUMBER);
			if (order == null) {
				order = context.getNumber();
			}
			creditnoteno = order;
			req.setValue(creditnoteno);
		}

		if (selection == creditnoteno) {
			context.setAttribute(INPUT_ATTR, ORDER_NO);
			return number(context, "Enter Credit Note No", creditnoteno);
		}

		Record creditNoteNoRec = new Record(creditnoteno);
		// creditNoteNoRec.add("Name", "Credit Note No");
		creditNoteNoRec.add("Credit Note No", creditnoteno);
		list.add(creditNoteNoRec);
		return null;
	}

	private void completeProcess(Context context) {
		Company company = context.getCompany();
		VendorCreditMemo vendorCreditMemo = new VendorCreditMemo();

		Date date = get(DATE).getValue();
		vendorCreditMemo.setDate(new FinanceDate(date));

		String number = get(NUMBER).getValue();
		vendorCreditMemo.setNumber(number);

		List<TransactionItem> items = get(ITEMS).getValue();
		if (get("accounts") != null) {
			List<TransactionItem> accounts = get("accounts").getValue();
			items.addAll(accounts);
		}
		vendorCreditMemo.setTransactionItems(items);

		Contact contact = get(CONTACT).getValue();
		if (contact != null && contact.getName() != null) {
			vendorCreditMemo.setContact(contact);
		}
		// TODO Location
		// TODO Class
		String phone = get(PHONE).getValue();
		vendorCreditMemo.setPhone(phone);
		if (company.getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			TAXCode taxCode = get("tax").getValue();
			for (TransactionItem item : items) {
				item.setTaxCode(taxCode);
			}
		}

		Vendor supplier = get(SUPPLIER).getValue();
		vendorCreditMemo.setVendor(supplier);

		String memo = get(MEMO).getValue();
		vendorCreditMemo.setMemo(memo);
		vendorCreditMemo.setTotal(getTransactionTotal(items, company));
		create(vendorCreditMemo, context);
	}
}
