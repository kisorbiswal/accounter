package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;

/**
 * 
 * @author SaiPrasad N
 * 
 */
public class NewQuoteCommand extends AbstractTransactionCommand {

	private static final String PHONE = "phone";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement("customer", false, true));
		list.add(new ObjectListRequirement(ITEMS, false, true) {

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
		list.add(new Requirement(NUMBER, true, false));
		list.add(new Requirement(PAYMENT_TERMS, true, true));
		list.add(new Requirement(CONTACT, true, true));
		list.add(new Requirement(BILL_TO, true, true));
		list.add(new Requirement(PHONE, true, true));
		list.add(new Requirement("deliveryDate", true, true));
		list.add(new Requirement("expirationDate", true, false));
		list.add(new Requirement(MEMO, true, true));

		list.add(new Requirement(TAXCODE, false, true));
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
		makeResult.add(getMessages().readyToCreate(getConstants().newQuote()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);

		setTransactionType(CUSTOMER_TRANSACTION);
		result = customerRequirement(context, list, "customer", Global.get()
				.customer());
		if (result != null) {
			return result;
		}
		result = itemsRequirement(context, makeResult, actions);
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
		result.add(getMessages().createSuccessfully(getConstants().quote()));
		return result;
	}

	private void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_ESTIMATE, context.getCompany()));
		get(PHONE).setDefaultValue("");
		get(CONTACT).setDefaultValue(null);
		ArrayList<ClientPaymentTerms> paymentTerms = getClientCompany()
				.getPaymentsTerms();
		for (ClientPaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}

		get("deliveryDate").setDefaultValue(new ClientFinanceDate());
		get("expirationDate").setDefaultValue(new ClientFinanceDate());

		get(MEMO).setDefaultValue(" ");
		get(BILL_TO).setDefaultValue(new ClientAddress());

	}

	/**
	 * 
	 * @param context
	 */
	private void completeProcess(Context context) {

		ClientEstimate estimate = new ClientEstimate();

		ClientCustomer customer = get("customer").getValue();
		estimate.setCustomer(customer);

		ClientFinanceDate date = get(DATE).getValue();
		estimate.setDate(date.getDate());

		String number = get(NUMBER).getValue();
		estimate.setNumber(number);

		ClientContact contact = get(CONTACT).getValue();
		if (contact != null)
			estimate.setContact(contact);

		ClientAddress billTo = get(BILL_TO).getValue();
		estimate.setAddress(billTo);

		String phone = get(PHONE).getValue();
		estimate.setPhone(phone);

		List<ClientTransactionItem> items = get(ITEMS).getValue();
		ClientCompanyPreferences preferences = getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		estimate.setTransactionItems(items);
		updateTotals(estimate);
		
		ClientPaymentTerms paymentTerm = get(PAYMENT_TERMS).getValue();
		estimate.setPaymentTerm(paymentTerm.getID());

		ClientFinanceDate d = get(DATE).getValue();
		estimate.setDate(d.getDate());

		ClientFinanceDate deliveryDate = get("deliveryDate").getValue();
		estimate.setDeliveryDate(deliveryDate.getDate());
		ClientFinanceDate expiryDdate = get("expirationDate").getValue();
		estimate.setExpirationDate(expiryDdate.getDate());

		String memo = get(MEMO).getValue();
		estimate.setMemo(memo);
		create(estimate, context);

	}

	/**
	 * 
	 * @param context
	 * @param makeResult
	 * @param actions2
	 * @param list
	 * @return
	 */
	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {
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

		selection = context.getSelection("values");
		Result result = numberOptionalRequirement(context, list, selection,
				NUMBER, getConstants().quote() + getConstants().number(),
				getMessages().pleaseEnter(getConstants().number()));
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, DATE, getConstants()
				.transactionDate(),
				getMessages().pleaseEnter(getConstants().transactionDate()),
				selection);
		if (result != null) {
			return result;
		}
		ClientCustomer c = get("customer").getValue();
		result = contactRequirement(context, list, selection, c);
		if (result != null) {
			return result;
		}
		result = addressOptionalRequirement(context, list, selection, BILL_TO,
				getMessages().pleaseEnter(getConstants().billTo()),
				getConstants().billTo());
		if (result != null) {
			return result;
		}
		result = paymentTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, PHONE,
				getConstants().phoneNumber(),
				getMessages().pleaseEnter(getConstants().phoneNumber()));
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, "expirationDate",
				getConstants().expirationDate(),
				getMessages().pleaseEnter(getConstants().expirationDate()),
				selection);
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, "deliveryDate",
				getConstants().deliveryDate(),
				getMessages().pleaseEnter(getConstants().deliveryDate()),
				selection);
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, MEMO,
				getConstants().memo(),
				getMessages().pleaseEnter(getConstants().memo()));
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", getMessages().finishToCreate(getConstants().quote()));
		actions.add(finish);

		return makeResult;
	}

}
