package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.customers.CustomerRefundView;

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
		makeResult
				.add(" Customer Refund is ready to create with following values.");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = customerRequirement(context, list, PAY_TO);
		if (result != null) {
			return result;
		}
		result = accountRequirement(context, list, PAY_FROM,
				new ListFilter<ClientAccount>() {
					@Override
					public boolean filter(ClientAccount e) {
						return true;
					}
				});
		if (result != null) {
			return result;
		}

		result = amountRequirement(context, list, AMOUNT, "Enter amount");
		if (result != null) {
			return result;
		}
		result = paymentMethodRequirement(context, list, PAYMENT_METHOD);
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
		result.add(" CustomerRefund was created successfully.");

		return result;
	}

	private void createCustomerRefundObject(Context context) {

		ClientCustomerRefund customerRefund = new ClientCustomerRefund();
		Date date = get(DATE).getValue();
		ClientPayee clientPayee = get(PAY_TO).getValue();
		ClientAccount account = get(PAY_FROM).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		double amount = Double.parseDouble(get(AMOUNT).getValue().toString());
		boolean istobePrinted = get(TOBEPRINTED).getValue();
		customerRefund.setPayTo(clientPayee.getID());
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
				"Enter Order Number");
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, "date", "Date",
				selection);
		if (result != null) {
			return result;
		}
		booleanOptionalRequirement(context, selection, list, TOBEPRINTED,
				"This To be Printed is Active",
				"This To be Printed is InActive");

		if (!(Boolean) get(TOBEPRINTED).getValue()) {
			result = amountOptionalRequirement(context, list, selection,
					CHEQUE_NO, "Enter check Number");
			if (result != null) {
				return result;
			}
		}
		result = stringOptionalRequirement(context, list, selection, MEMO,
				"Enter Memo");
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Customer Refund.");
		actions.add(finish);

		return makeResult;
	}
}
