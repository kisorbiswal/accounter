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
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerPrePayment;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class NewCustomerPrepaymentCommand extends AbstractTransactionCommand {
	private static final String DEPOSITSANDTRANSFERS = "DepositOrTransferTo";
	private static final String AMOUNT = "Amount";
	private static final String TOBEPRINTED = "To be printed";
	private static final String CHEQUE_NUM = "cheque Num";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(CUSTOMER, false, true));
		list.add(new Requirement(ORDER_NO, true, true));
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
		setDefaultValues();
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = context.makeResult();
		if (process != null) {
			if (process.equals(ADDRESS_PROCESS)) {
				result = addressProcess(context);
				if (result != null) {
					return result;
				}
			}
		}
		setTransactionType(CUSTOMER_TRANSACTION);
		Result makeResult = context.makeResult();
		ResultList actions = new ResultList("actions");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		result = customerRequirement(context, list, CUSTOMER);
		if (result != null) {
			return result;
		}
		result = accountRequirement(context, list, DEPOSITSANDTRANSFERS,
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount acc) {
						return Arrays.asList(ClientAccount.TYPE_BANK,
								ClientAccount.TYPE_CREDIT_CARD,
								ClientAccount.TYPE_OTHER_CURRENT_ASSET,
								ClientAccount.TYPE_FIXED_ASSET).contains(
								acc.getType())
								&& acc.getID() != getClientCompany()
										.getAccountsReceivableAccountId();
					}
				});
		if (result != null) {
			return result;
		}

		result = amountRequirement(context, list, AMOUNT, "Enter Amount");
		if (result != null) {
			return result;
		}
		result = paymentMethodRequirement(context, list, PAYMENT_MENTHOD);
		if (result != null) {
			return result;
		}
		makeResult.add(actions);
		result = createOptionalRequirement(context, list, makeResult, actions);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		result = new Result();
		result.add("Customer Prepayment was created successfully");
		return result;
	}

	private void completeProcess(Context context) {
		ClientCustomerPrePayment prePayment = new ClientCustomerPrePayment();
		Date date = get(DATE).getValue();
		prePayment.setDate(new ClientFinanceDate(date).getDate());
		String number = get(ORDER_NO).getValue();
		prePayment.setNumber(number);
		ClientCustomer customer = get(CUSTOMER).getValue();
		prePayment.setCustomer(customer.getID());
		ClientAccount depositIn = get(DEPOSITSANDTRANSFERS).getValue();
		prePayment.setDepositIn(depositIn.getID());
		String amount = get(AMOUNT).getValue();
		prePayment.setTotal(Double.valueOf(amount));
		String paymentMethod = get(PAYMENT_MENTHOD).getValue();
		prePayment.setPaymentMethod(paymentMethod);
		String checkNum = get(CHEQUE_NUM).getValue();
		prePayment.setCheckNumber(checkNum);
		Boolean tobePrinted = get(TOBEPRINTED).getValue();
		prePayment.setToBePrinted(tobePrinted);
		String memo = get(MEMO).getValue();
		prePayment.setMemo(memo);
		prePayment.setType(ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);
		adjustBalance(Double.valueOf(amount), customer, prePayment);
		create(prePayment, context);
	}

	private void setDefaultValues() {
		get(DATE).setDefaultValue(new Date());
		get(ORDER_NO).setDefaultValue("1");
		get(TOBEPRINTED).setDefaultValue(true);
		get(CHEQUE_NUM).setDefaultValue("1");
	}

	/**
	 * 
	 * @param context
	 * @param actions2
	 * @param makeResult
	 * @param list2
	 * @return
	 */
	private Result createOptionalRequirement(Context context, ResultList list,
			Result makeResult, ResultList actions) {
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
		Result result = numberRequirement(context, list, ORDER_NO,
				"Enter Order Number");
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, DATE, "Date", selection);
		if (result != null) {
			return result;
		}

		result = numberRequirement(context, list, CHEQUE_NUM,
				"Enter Check Number");
		if (result != null) {
			return result;
		}

		booleanOptionalRequirement(context, selection, list, TOBEPRINTED,
				"Printed", "Not Printed");
		result = stringOptionalRequirement(context, list, selection, MEMO,
				"Enter Memo");
		if (result != null) {
			return result;
		}
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Invoice.");
		actions.add(finish);
		return makeResult;
	}

	private void adjustBalance(double amount, ClientCustomer customer,
			ClientCustomerPrePayment customerPrePayment) {
		double enteredBalance = amount;

		if (DecimalUtil.isLessThan(enteredBalance, 0)
				|| DecimalUtil.isGreaterThan(enteredBalance, 1000000000000.00)) {
			enteredBalance = 0D;
		}
		if (customer != null) {
			customerPrePayment.setCustomerBalance(customer.getBalance()
					- enteredBalance);

		}
		ClientAccount depositIn = getClientCompany().getAccount(
				customerPrePayment.getDepositIn());
		if (depositIn.isIncrease()) {
			customerPrePayment.setEndingBalance(depositIn.getTotalBalance()
					- enteredBalance);
		} else {
			customerPrePayment.setEndingBalance(depositIn.getTotalBalance()
					+ enteredBalance);
		}
	}
}
