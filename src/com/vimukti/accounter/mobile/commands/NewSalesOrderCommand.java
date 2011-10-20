package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;

public class NewSalesOrderCommand extends AbstractTransactionCommand {

	private static final String CUSTOMER = "customer";
	private static final String STATUS = "status";
	private static final String ITEMS = "items";
	private static final String NAME = "name";
	private static final String DESC = "description";
	private static final String QUANTITY = "quantity";
	private static final String PRICE = "price";
	private static final String DISCOUNT = "discount";
	private static final String TOTAL = "total";
	private static final String CONTACT = "contact";
	private static final String PHONE = "phone";
	private static final String BILL_TO = "billTo";
	private static final String DATE = "date";
	private static final String ORDER_NO = "orderNo";
	private static final String CUSTOMER_ORDERNO = "customerOrderNo";
	private static final String PAYMENT_TERMS = "paymentTerms";
	private static final String SHIPPING_TERMS = "shippingTerms";
	private static final String SHIPPING_METHODS = "shippingMethods";
	private static final String DUE_DATE = "dueDate";
	private static final String INPUT_ATTR = "input";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * here in add requirement we add the items which are required for the
	 * command
	 */
	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(CUSTOMER, false, true));
		list.add(new Requirement(STATUS, false, true));

		list.add(new ObjectListRequirement(ITEMS, false, true) {
			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement(NAME, false, true));
				list.add(new Requirement(DESC, true, true));
				list.add(new Requirement(QUANTITY, true, true));
				list.add(new Requirement(PRICE, true, true));
				list.add(new Requirement(DISCOUNT, true, true));
				list.add(new Requirement(TOTAL, true, true));
			}
		});

		list.add(new Requirement(CONTACT, true, false));
		list.add(new Requirement(PHONE, true, true));
		list.add(new Requirement(BILL_TO, true, false));
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(TAXCODE, false, true));
		list.add(new Requirement(ORDER_NO, true, true));
		list.add(new Requirement(CUSTOMER_ORDERNO, true, true));
		list.add(new Requirement(PAYMENT_TERMS, true, true));
		list.add(new Requirement(SHIPPING_TERMS, true, true));
		list.add(new Requirement(SHIPPING_METHODS, true, true));
		list.add(new Requirement(DUE_DATE, true, true));
		list.add(new Requirement(MEMO, true, true));

	}

	@Override
	public Result run(Context context) {
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = null;
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

		Result makeResult = context.makeResult();
		makeResult
				.add(getMessages().readyToCreate(getConstants().salesOrder()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = customerRequirement(context, list, CUSTOMER, Global.get()
				.Customer());
		if (result != null) {
			return result;
		}
		ClientCompanyPreferences preferences = getClientCompany()
				.getPreferences();

		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			result = taxCodeRequirement(context, list);
			if (result != null) {
				return result;
			}
		}
		result = itemsRequirement(context, makeResult, actions);
		if (result != null) {
			return result;
		}
		result = statusRequirement(context, list, STATUS, getConstants()
				.status());
		if (result != null) {
			return result;
		}
		setDefaultValues();
		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		completeProcess(context);
		markDone();
		return null;

	}

	private void setDefaultValues() {

		get(ORDER_NO).setDefaultValue(Integer.toString(1));
		get(DUE_DATE).setDefaultValue(new Date());
		get(DATE).setDefaultValue(new Date());
	}

	/**
	 * 
	 * @param context
	 */
	private void completeProcess(Context context) {

		ClientSalesOrder newSalesOrder = new ClientSalesOrder();

		ClientCustomer customer = get(CUSTOMER).getValue();
		newSalesOrder.setCustomer(customer.getID());

		newSalesOrder.setPhone((String) get(PHONE).getValue());
		int statusNumber = 0;
		if (get(STATUS).getValue() == "Open") {
			statusNumber = 1;
		} else if (get(STATUS).getValue() == "Open") {
			statusNumber = 2;
		} else if (get(STATUS).getValue() == "Open") {
			statusNumber = 3;
		}
		newSalesOrder.setStatus(statusNumber);
		Date value = get(DATE).getValue();
		newSalesOrder.setDate(new FinanceDate(value).getDate());
		newSalesOrder.setNumber((String) get(ORDER_NO).getValue());

		newSalesOrder.setCustomerOrderNumber((String) get(CUSTOMER_ORDERNO)
				.getValue());

		ClientPaymentTerms newPaymentTerms = get(PAYMENT_TERMS).getValue();
		if (newPaymentTerms != null)
			newSalesOrder.setPaymentTerm(newPaymentTerms.getID());

		CompanyPreferences preferences = context.getCompany().getPreferences();

		if (preferences.isDoProductShipMents()) {
			ClientShippingTerms newShippingTerms = get(SHIPPING_TERMS)
					.getValue();
			if (newPaymentTerms != null)
				newSalesOrder.setShippingTerm(newShippingTerms.getID());

			ClientShippingMethod newShippingMethod = get(SHIPPING_METHODS)
					.getValue();
			if (newShippingMethod != null)
				newSalesOrder.setShippingMethod(newShippingMethod.getID());
		}
		Date date = get(DUE_DATE).getValue();
		newSalesOrder.setDate(new FinanceDate(date).getDate());

		List<ClientTransactionItem> items = get(ITEMS).getValue();
		newSalesOrder.setTransactionItems(items);

		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}
		String memo = get("memo").getValue();
		newSalesOrder.setMemo(memo);

		create(newSalesOrder, context);
	}

	/**
	 * Creating optional results
	 * 
	 * @param context
	 * @param makeResult
	 * @param actions
	 * @param list
	 * @return
	 */
	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {

		// context.setAttribute(INPUT_ATTR, "optional");

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

		ClientCustomer customer = (ClientCustomer) get("customer").getValue();

		Result result = contactRequirement(context, list, selection, customer);
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, DATE, getMessages()
				.pleaseEnter(getConstants().date()), getConstants().date(),
				selection);
		if (result != null) {
			return result;
		}
		result = paymentTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = numberOptionalRequirement(context, list, selection, PHONE,
				getMessages().pleaseEnter(getConstants().phone()),
				getConstants().phone());
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(
				context,
				list,
				selection,
				CUSTOMER_ORDERNO,
				getMessages().pleaseEnter(
						getMessages().customerOrderNumber(
								Global.get().Customer())), getMessages()
						.customerOrderNumber(Global.get().Customer()));
		if (result != null) {
			return result;
		}
		// TODO bill to was disabled so removed
		// result = billToRequirement(context, list, selection);
		// if (result != null) {
		// return result;
		// }

		CompanyPreferences preferences = context.getCompany().getPreferences();

		if (preferences.isDoProductShipMents()) {
			result = shippingMethodRequirement(context, list, selection);
			if (result != null) {
				return result;
			}

			result = shippingTermsRequirement(context, list, selection);
			if (result != null) {
				return result;
			}
		}

		result = dateOptionalRequirement(context, list, DUE_DATE, getMessages()
				.pleaseEnter(getConstants().dueDate()), getConstants()
				.dueDate(), selection);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, ORDER_NO,
				getMessages().pleaseEnter(getConstants().orderNumber()),
				getConstants().orderNumber());
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, MEMO,
				getMessages().pleaseEnter(getConstants().memo()),
				getConstants().memo());
		if (result != null) {
			return result;
		}
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", getMessages()
				.finishToCreate(getConstants().salesOrder()));
		actions.add(finish);

		return makeResult;
	}
}