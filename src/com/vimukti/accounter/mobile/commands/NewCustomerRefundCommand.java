package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

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
	private static final String NO = "date";
	private static final String BANK_BALANCE = "Bank Balance";
	private static final String CUSTOMER_BALANCE = "Customer Balance";

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

		Result result = null;

		result = customerRequirement(context);
		if (result != null) {
			return result;
		}
		result = depositeOrTransferTo(context, "payfrom");
		if (result != null) {
			return result;
		}

		result = amountRequireMent(context);
		if (result != null) {
			return result;
		}
		result = paymentMethodRequirement(context);
		if (result != null) {
			return result;
		}

		result = optionalRequirement(context);
		if (result != null) {
			return result;
		}
		return null;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result optionalRequirement(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

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
		ResultList list = new ResultList("values");

		Requirement payto = get(PAY_TO);
		Customer customer = (Customer) payto.getValue();
		Record custRecord = new Record(customer);
		custRecord.add("Name", "Payto");
		custRecord.add("Value", customer.getName());
		list.add(custRecord);

		Requirement payFromReq = get(PAY_FROM);
		Account account = payFromReq.getValue();
		Record accountRec = new Record(account);
		accountRec.add("Number", "Account No");
		accountRec.add("value", account.getNumber());
		accountRec.add("Account name", "Account Name");
		accountRec.add("value", account.getNumber());
		accountRec.add("Account type", "Account Type");
		accountRec.add("Account Type", getAccountTypeString(account.getType()));
		list.add(accountRec);

		Requirement amountReq = get(AMOUNT);
		Double amount = (Double) amountReq.getValue();
		if (amount == selection) {
			context.setAttribute(INPUT_ATTR, AMOUNT);
			return number(context, "Please Enter the Amount", "" + amount);
		}

		Requirement paymentMethodReq = get(PAYMENT_MENTHOD);
		String paymentMethod = paymentMethodReq.getValue();
		if (paymentMethod == selection) {
			context.setAttribute(INPUT_ATTR, PAYMENT_MENTHOD);
			return text(context, "Please Enter PaymentMethod", ""
					+ paymentMethod);
		}
		Result result = orderNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, "date", "Date",
				selection);
		if (result != null) {
			return result;
		}
		Requirement tobePrintedReq = get(TOBEPRINTED);
		Boolean isTobePrinted = tobePrintedReq.getValue();
		if (selection == isTobePrinted) {
			context.setAttribute(INPUT_ATTR, TOBEPRINTED);
			isTobePrinted = !isTobePrinted;
			tobePrintedReq.setValue(isTobePrinted);
		}
		String tobePrintedString = "";
		if (isTobePrinted) {
			tobePrintedString = "This To be Printed is Active";
		} else {
			tobePrintedString = "This To be Printed is Active";
		}
		Record isTobePrintedRecord = new Record(TOBEPRINTED);
		isTobePrintedRecord.add("Name", "");
		isTobePrintedRecord.add("Value", tobePrintedString);
		list.add(isTobePrintedRecord);

		if (!isTobePrinted) {
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
		ResultList actions = new ResultList(ACTIONS);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Customer Refund.");
		actions.add(finish);
		result.add(actions);
		return result;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result amountRequireMent(Context context) {
		Requirement amountReq = get(AMOUNT);
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(AMOUNT)) {
			input = context.getString();
			amountReq.setValue(input);
			context.setAttribute(INPUT_ATTR, "default");
		}
		if (!amountReq.isDone()) {
			context.setAttribute(INPUT_ATTR, AMOUNT);
			return text(context, "Please Enter the  Amount", null);
		}

		return null;
	}

}
