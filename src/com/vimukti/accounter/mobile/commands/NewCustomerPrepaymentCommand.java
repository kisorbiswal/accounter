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

public class NewCustomerPrepaymentCommand extends AbstractTransactionCommand {
	private static final String INPUT_ATTR = "input";
	private static final String CUSTOMER_NAME = "customer";
	private static final String CUSTOMERPREPAYMENT_NUM = "number";
	private static final String DATE = "date";
	private static final String ADDRES = "address";
	private static final String DEPOSITSANDTRANSFERS = "Deposit / Transfer To";
	private static final String AMOUNT = "Amount";
	private static final String PAYMENT_MENTHOD = "Payment method";
	private static final String TOBEPRINTED = "To be printed";
	private static final String CHEQUE_NUM = "cheque Num";
	private static final String MEMO = "memo";
	private static final String BANK_BALANCE = "Bank Balance";
	private static final String CUSTOMER_BALANCE = "Customer Balance ";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(CUSTOMER_NAME, false, true));
		list.add(new Requirement(CUSTOMERPREPAYMENT_NUM, true, true));
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(DEPOSITSANDTRANSFERS, false, true));
		list.add(new Requirement(AMOUNT, false, true));
		list.add(new Requirement(PAYMENT_MENTHOD, false, true));
		list.add(new Requirement(TOBEPRINTED, true, true));
		list.add(new Requirement(CHEQUE_NUM, true, true));
		list.add(new Requirement(MEMO, true, true));
	}

	@Override
	public Result run(Context context) {

		Result result = null;

		result = customerRequirement(context);
		if (result != null) {
			return result;
		}
		result = depositeOrTransferTo(context, "depositOrTransferTo");
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

		Requirement custmerReq = get(CUSTOMER_NAME);
		Customer customer = (Customer) custmerReq.getValue();
		Record custRecord = new Record(customer);
		custRecord.add("Name", "Customer");
		custRecord.add("Value", customer.getName());
		list.add(custRecord);

		Requirement transferTo = get(DEPOSITSANDTRANSFERS);
		Account account = transferTo.getValue();
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
					CHEQUE_NUM, "Enter check Number");
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
		finish.add("", "Finish to create Invoice.");
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
