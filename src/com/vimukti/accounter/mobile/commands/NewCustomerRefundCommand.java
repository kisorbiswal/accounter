package com.vimukti.accounter.mobile.commands;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ListFilter;

/**
 * 
 * @author Lingarao
 * 
 */
public class NewCustomerRefundCommand extends AbstractTransactionCommand {

	private static final String INPUT_ATTR = "input";

	private static final String PAY_TO = "Pay to";
	private static final String ADDRESS = "Address";
	private static final String PAY_FROM = "Pay from";
	private static final String AMOUNT = "Amount";
	private static final String PAYMENT_METHOD = "Payment method";
	private static final String TOBEPRINTED = "To be printed";
	private static final String CHEQUE_NO = "Cheque No";
	private static final String MEMO = "Memo";
	private static final String DATE = "date";
	private static final String NO = "Number";
	private static final String BANK_BALANCE = "Bank Balance";
	private static final String CUSTOMER_BALANCE = "Customer Balance";
	private static final String LOCATION = "Location";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(PAY_TO, false, true));
		list.add(new Requirement(ADDRESS, true, true));
		list.add(new Requirement(PAY_FROM, false, true));
		list.add(new Requirement(AMOUNT, false, true));
		list.add(new Requirement(PAYMENT_METHOD, false, true));
		list.add(new Requirement(TOBEPRINTED, true, true));
		list.add(new Requirement(CHEQUE_NO, false, true));
		list.add(new Requirement(MEMO, true, true));
		list.add(new Requirement(BANK_BALANCE, true, true));
		list.add(new Requirement(CUSTOMER_BALANCE, true, true));
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(NO, true, true));

	}

	@Override
	public Result run(Context context) {
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result result = null;

		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(
				getMessages().customerRefund(Global.get().Customer())));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = customerRequirement(context, list, PAY_TO, Global.get()
				.Customer());
		if (result != null) {
			return result;
		}
		result = accountRequirement(context, list, PAY_FROM, getConstants()
				.Accounts(), new ListFilter<ClientAccount>() {
			@Override
			public boolean filter(ClientAccount e) {
				return Arrays.asList(ClientAccount.TYPE_BANK,
						ClientAccount.TYPE_OTHER_CURRENT_ASSET).contains(
						e.getType());
			}
		});
		if (result != null) {
			return result;
		}

		result = amountRequirement(context, list, AMOUNT, getMessages()
				.pleaseEnter(getConstants().amount()), getConstants().amount());
		if (result != null) {
			return result;
		}
		result = paymentMethodRequirement(context, list, PAYMENT_METHOD,
				getConstants().paymentMethod());
		if (result != null) {
			return result;
		}
		setdefaultValues();
		result = optionalRequirement(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		createCustomerRefundObject(context);

		markDone();

		result = new Result();
		result.add(getMessages().createSuccessfully(
				getMessages().customerRefund(Global.get().Customer())));

		return result;
	}

	private void createCustomerRefundObject(Context context) {

		ClientCustomerRefund customerRefund = new ClientCustomerRefund();
		Date date = get(DATE).getValue();
		ClientCustomer clientcustomer = get(PAY_TO).getValue();
		ClientAccount account = get(PAY_FROM).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		double amount = Double.parseDouble(get(AMOUNT).getValue().toString());
		boolean istobePrinted = get(TOBEPRINTED).getValue();
		customerRefund.setPayTo(clientcustomer.getID());
		customerRefund.setPayFrom(account.getID());
		customerRefund.setPaymentMethod(paymentMethod);
		customerRefund.setIsToBePrinted(istobePrinted);
		if (!istobePrinted) {
			Double cheqNum = get(CHEQUE_NO).getValue();
			customerRefund.setCheckNumber(String.valueOf(cheqNum));
		}
		customerRefund.setMemo(get(MEMO).getValue() == null ? "" : get(MEMO)
				.getValue().toString());
		customerRefund.setTotal(amount);
		customerRefund.setDate(new ClientFinanceDate(date).getDate());
		double value = Double
				.parseDouble(get(BANK_BALANCE).getValue() == null ? "0.0"
						: get(BANK_BALANCE).getValue().toString());
		double customerbalance = clientcustomer.getBalance();
		customerRefund.setCustomerBalance(customerbalance + amount);
		customerRefund.setEndingBalance(value - amount);
		// if
		// (context.getCompany().getPreferences().isLocationTrackingEnabled()) {
		// ClientLocation location = get(LOCATION).getValue();
		// customerRefund.setLocation(location.getID());
		// }
		// Class
		create(customerRefund, context);
	}

	private void setdefaultValues() {
		get(TOBEPRINTED).setDefaultValue(Boolean.TRUE);
		get(AMOUNT).setDefaultValue(Double.valueOf(0.0D));
		get(DATE).setDefaultValue(new Date());
	}

	/**
	 * 
	 * @param context
	 * @param makeResult
	 * @param actions
	 * @param list
	 * @return
	 */
	private Result optionalRequirement(Context context, ResultList list,
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

		Result result = numberOptionalRequirement(context, list, selection, NO,
				getMessages().pleaseEnter(getConstants().orderNumber()),
				getConstants().orderNumber());
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, "date", getMessages()
				.pleaseEnter(getConstants().date()), getConstants().date(),
				selection);
		if (result != null) {
			return result;
		}
		booleanOptionalRequirement(context, selection, list, TOBEPRINTED,
				"This To be Printed is Active",
				"This To be Printed is InActive");

		if (!(Boolean) get(TOBEPRINTED).getValue()) {
			result = amountOptionalRequirement(context, list, selection,
					CHEQUE_NO,
					getMessages().pleaseEnter(getConstants().checkNo()),
					getConstants().checkNo());
			if (result != null) {
				return result;
			}
		}
		result = stringOptionalRequirement(context, list, selection, MEMO,
				getMessages().pleaseEnter(getConstants().memo()),
				getConstants().memo());
		if (result != null) {
			return result;
		}
		ClientCustomer customer = get(PAY_TO).getValue();
		for (ClientAddress adress : customer.getAddress()) {
			Record record = new Record(adress);
			record.add("", getConstants().address());
			record.add("", adress.toString());
			list.add(record);
		}

		double bankBalance = Double
				.parseDouble(get(BANK_BALANCE).getValue() == null ? "0.0"
						: get(BANK_BALANCE).getValue().toString());

		double amount = Double
				.parseDouble(get(AMOUNT).getValue() == null ? "0.0" : get(
						AMOUNT).getValue().toString());

		Record bankbalanceRecord = new Record(BANK_BALANCE);
		bankbalanceRecord.add("", BANK_BALANCE);
		bankbalanceRecord.add("", bankBalance - amount);
		list.add(bankbalanceRecord);
		double customerbalance = customer.getBalance();

		Record customerBalanceRecord = new Record(CUSTOMER_BALANCE);
		customerBalanceRecord.add("", CUSTOMER_BALANCE);
		customerBalanceRecord.add("", customerbalance + amount);
		list.add(customerBalanceRecord);

		Record finish = new Record(ActionNames.FINISH);
		finish.add(
				"",
				getMessages().finishToCreate(
						getMessages().customerRefund(Global.get().Customer())));
		actions.add(finish);
		return makeResult;
	}

}
