package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
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
		list.add(new Requirement(TAXCODE, false, true));
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
		// Preparing Result
		Result makeResult = context.makeResult();
		makeResult.add("Enter Bill is ready to create with following values.");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);

		setTransactionType(CUSTOMER_TRANSACTION);

		result = createSupplierRequirement(context, list, VENDOR);
		if (result != null) {
			return result;
		}
		result = itemsAndAccountsRequirement(context, makeResult, actions,
				new ListFilter<ClientAccount>() {
					@Override
					public boolean filter(ClientAccount e) {
						return true;
					}
				});
		if (result != null) {
			return result;
		}

		makeResult.add(actions);
		ClientCompanyPreferences preferences = getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			result = taxCodeRequirement(context, list);
			if (result != null) {
				return result;
			}
		}
		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();

		result = new Result();
		result.add("Enter bill created successfully");
		return result;
	}

	private void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new Date(System.currentTimeMillis()));
		get(NUMBER)
				.setDefaultValue(
						NumberUtils.getNextTransactionNumber(
								ClientTransaction.TYPE_ENTER_BILL,
								context.getCompany()));
		get(PHONE).setDefaultValue("");
		get(CONTACT).setDefaultValue(new ClientContact());
		ArrayList<ClientPaymentTerms> paymentTerms = getClientCompany()
				.getPaymentsTerms();
		for (ClientPaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}

		get(DUE_DATE).setDefaultValue(new Date(System.currentTimeMillis()));
		get(DELIVERY_DATE)
				.setDefaultValue(new Date(System.currentTimeMillis()));

		ClientVendor v = (ClientVendor) get(VENDOR).getValue();
		if (v != null) {
			Set<ClientContact> contacts2 = v.getContacts();
			if (contacts2 != null)
				for (ClientContact c : contacts2) {
					get(CONTACT).setDefaultValue(c);
				}
		}
		get(MEMO).setDefaultValue(" ");
	}

	private void completeProcess(Context context) {

		ClientEnterBill enterBill = new ClientEnterBill();

		ClientVendor vendor = (ClientVendor) get(VENDOR).getValue();
		enterBill.setVendor(vendor);
		Date date = get(DATE).getValue();
		if (date != null) {
			enterBill.setDate(date.getTime());
		} else {
			enterBill.setDate(System.currentTimeMillis());
		}

		enterBill.setType(ClientTransaction.TYPE_ENTER_BILL);

		List<ClientTransactionItem> items = get("items").getValue();

		List<ClientTransactionItem> accounts = get("accounts").getValue();
		items.addAll(accounts);

		enterBill.setTransactionItems(items);

		Date dueDate = get(DUE_DATE).getValue();
		enterBill.setDueDate(dueDate.getTime());

		Date deliveryDate = get(DELIVERY_DATE).getValue();
		// enterBill.setDeliveryDate(new FinanceDate(deliveryDate));

		ClientContact contact = get(CONTACT).getValue();
		enterBill.setContact(contact);

		ClientPaymentTerms paymentTerm = get("paymentTerms").getValue();
		enterBill.setPaymentTerm(paymentTerm);

		String phone = get(PHONE).getValue();
		enterBill.setPhone(phone);

		String memo = get(MEMO).getValue();
		enterBill.setMemo(memo);
		updateTotals(enterBill);
		create(enterBill, context);

	}

	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			// case ADD_MORE_ITEMS:
			// return items(context);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("values");
		Result result = dateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = contactRequirement(context, list, selection,
				(ClientVendor) get(VENDOR).getValue());
		if (result != null) {
			return result;
		}
		result = paymentTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, PHONE,
				"Enter Phone Number");
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, DUE_DATE,
				"Enter Due date", selection);

		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, DELIVERY_DATE,
				"Enter the " + DELIVERY_DATE, selection);

		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, MEMO,
				"Enter Memo");
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Bill.");
		actions.add(finish);

		return makeResult;
	}
}
