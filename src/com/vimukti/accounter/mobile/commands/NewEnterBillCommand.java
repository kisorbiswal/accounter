package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ListFilter;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewEnterBillCommand extends AbstractTransactionCommand {

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
		list.add(new ObjectListRequirement("accounts", false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("amount", false, true));
				list.add(new Requirement("discount", true, true));
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

		setTransactionType(VENDOR_TRANSACTION);

		result = vendorRequirement(context);
		if (result != null) {
			return result;
		}

		result = accountsRequirement(context, "accounts",
				new ListFilter<Account>() {

					@Override
					public boolean filter(Account e) {
						return true;
					}
				});
		if (result != null) {
			return result;
		}

		result = itemsRequirement(context);
		if (result != null) {
			return result;
		}

		setDefaultValues(context);
		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();

		return result;
	}

	private void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new Date(System.currentTimeMillis()));
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						Transaction.TYPE_ENTER_BILL, context.getCompany()));
		get(PHONE).setDefaultValue("");
		get(CONTACT).setDefaultValue(new Contact());
		Set<PaymentTerms> paymentTerms = context.getCompany().getPaymentTerms();
		for (PaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}

		get(DUE_DATE).setDefaultValue(new Date(System.currentTimeMillis()));
		get(DELIVERY_DATE)
				.setDefaultValue(new Date(System.currentTimeMillis()));

		Vendor v = (Vendor) get(VENDOR).getValue();
		Set<Contact> contacts2 = v.getContacts();
		if (contacts2 != null)
			for (Contact c : contacts2) {
				get(CONTACT).setDefaultValue(c);
			}

		get(MEMO).setDefaultValue(" ");
	}

	private void completeProcess(Context context) {

		Company company = context.getCompany();

		EnterBill enterBill = new EnterBill();

		Vendor vendor = (Vendor) get(VENDOR).getValue();
		vendor = (Vendor) context.getHibernateSession().merge(vendor);
		enterBill.setVendor(vendor);
		Date date = get(DATE).getValue();
		if (date != null) {
			enterBill.setDate(new FinanceDate(date));
		} else {
			enterBill.setDate(new FinanceDate(System.currentTimeMillis()));
		}

		enterBill.setType(Transaction.TYPE_ENTER_BILL);

		enterBill.setCompany(company);

		List<TransactionItem> items = get("items").getValue();

		if (get("accounts") != null) {
			List<TransactionItem> accounts = get("accounts").getValue();
			items.addAll(accounts);
		}

		Session hibernateSession = context.getHibernateSession();
		for (TransactionItem transactionItem : items) {
			Item item = transactionItem.getItem();
			if (item != null) {
				item = (Item) hibernateSession.merge(item);
				transactionItem.setItem(item);
			}
			Account account = transactionItem.getAccount();
			if (account != null) {
				account = (Account) hibernateSession.merge(account);
				transactionItem.setAccount(account);
			}
		}
		enterBill.setTransactionItems(items);

		enterBill.setTotal(getTransactionTotal(items, company));

		Date dueDate = get(DUE_DATE).getValue();
		enterBill.setDueDate(new FinanceDate(dueDate));

		Date deliveryDate = get(DELIVERY_DATE).getValue();
		enterBill.setDeliveryDate(new FinanceDate(deliveryDate));

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
		Requirement accountsReq = get("accounts");
		List<TransactionItem> accountTransItems = accountsReq.getValue();
		selection = context.getSelection("accountItems");
		if (selection != null) {
			Result result = transactionAccountItem(context,
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

		result = phoneRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, DUE_DATE,
				"Enter Due date", selection);

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
		ResultList items = new ResultList("items");
		for (TransactionItem item : transItems) {
			Record itemRec = new Record(item);
			itemRec.add("Name", item.getItem().getName());
			itemRec.add("Total", item.getLineTotal());
			itemRec.add("VatCode", item.getVATfraction());
			items.add(itemRec);
		}

		result.add(items);
		result.add(":-");
		ResultList accountItems = new ResultList("accountItems");
		for (TransactionItem item : accountTransItems) {
			Record itemRec = new Record(item);
			itemRec.add("Name", item.getAccount().getName());
			itemRec.add("Amount", item.getUnitPrice());
			itemRec.add("Discount", item.getDiscount());
			itemRec.add("Total", item.getLineTotal());
			accountItems.add(itemRec);
		}
		result.add(accountItems);

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

	private Result deliveryDateRequirement(Context context, ResultList list,
			Object selection) {

		Requirement req = get(DELIVERY_DATE);
		Date dueDate = (Date) req.getValue();
		if (dueDate != null)
			return null;

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(DELIVERY_DATE)) {
			Date date = context.getSelection(DATE);
			if (date == null) {
				date = context.getDate();
			}
			dueDate = date;
			req.setValue(dueDate);
		}
		if (selection == dueDate) {
			context.setAttribute(INPUT_ATTR, DELIVERY_DATE);
			return date(context, DELIVERY_DATE, dueDate);
		}

		Record dueDateRecord = new Record(dueDate);
		dueDateRecord.add("Name", DELIVERY_DATE);
		dueDateRecord.add("Value", dueDate.toString());
		list.add(dueDateRecord);
		return null;

	}

}
