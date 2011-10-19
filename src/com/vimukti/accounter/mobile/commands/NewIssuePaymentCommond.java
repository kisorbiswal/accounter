package com.vimukti.accounter.mobile.commands;

import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.IssuePayment;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionIssuePayment;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientTransactionIssuePayment;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.Accounter;

public class NewIssuePaymentCommond extends AbstractTransactionCommand {
	private static final String PAYMENT_METHOD = "Payment Mehtod";
	private static final String ACCOUNTS = "depositeOrTransferTo";
	private static final String CHEQUE_NO = "Starting cheque No";
	private static final String PAYMENTS_TO_ISSUED = "Starting cheque No";
	private static final String DATE = "Date";
	private static final String NUMBER = "Number";
	private static final String NAME = "Name";
	private static final String MEMO = "Memo";
	private static final String AMOUNT = "Amount";
	public static final String PAYMENT_METHOD_CHECK = "Check";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(PAYMENT_METHOD, false, true));
		list.add(new Requirement(ACCOUNTS, false, true));
		list.add(new Requirement(CHEQUE_NO, true, true));
		list.add(new ObjectListRequirement(PAYMENTS_TO_ISSUED, false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement(DATE, true, true));
				list.add(new Requirement(NUMBER, true, true));
				list.add(new Requirement(NAME, true, true));
				list.add(new Requirement(MEMO, true, true));
				list.add(new Requirement(AMOUNT, true, true));
				list.add(new Requirement(PAYMENT_METHOD, true, true));
			}
		});
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}

		Result makeResult = context.makeResult();
		makeResult
				.add(" Issue Payment is ready to create with following values.");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);
		
		 result = paymentMethodRequirement(context, list, "please enter the payment method");
		if (result != null) {
			return result;
		}
		 
		result = accountRequirement(context, list, ACCOUNTS,new ListFilter<ClientAccount>() {

							@Override
							public boolean filter(ClientAccount e) {
								return Arrays.asList(ClientAccount.TYPE_BANK,
										ClientAccount.TYPE_OTHER_CURRENT_ASSET)
										.contains(e.getType());
							
							}
		});
		if (result != null) {
			return result;
		}
		
		result = numberRequirement(context, list, CHEQUE_NO, "Please enter the cheque number");
		if (result != null) {
			return result;
		}
		
		
		/**
		 * here write for the  list of items
		 */
		
		
		result = accountItemsRequirement(context, null,
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						return true;
					}
				}, null);
		if (result != null) {
			return result;
		}
		result = getIssuePaymentRecords(context);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		return null;
	}

	private void completeProcess(Context context) {
		IssuePayment issuePayment = new IssuePayment();
		Requirement paymentMethodReq = get(PAYMENT_METHOD);
		String paymentMethod = (String) paymentMethodReq.getValue();
		issuePayment.setPaymentMethod(paymentMethod);
		issuePayment.setType(Transaction.TYPE_ISSUE_PAYMENT);
		Requirement accountsReq = get(ACCOUNTS);
		List<TransactionItem> transactionList = accountsReq.getValue();
		issuePayment.setTransactionItems(transactionList);
		// TODO
		Requirement checkNoReq = get(CHEQUE_NO);
		String chequeNo = checkNoReq.getValue();
		Requirement transactionListReq = get(PAYMENTS_TO_ISSUED);
		List<TransactionIssuePayment> list = transactionListReq.getValue();
		issuePayment.setTransactionIssuePayment(list);
		create(issuePayment, context);
	}

	private Result getIssuePaymentRecords(Context context) {

		Result result = context.makeResult();
		String paymentMethod = (String) context.getSelection(PAYMENT_METHOD);
		TransactionItem transactionItem = (TransactionItem) context
				.getSelection(ACCOUNTS);
		result.add("Issue Payments List");
		ResultList issuePaymentData = new ResultList("Issue Payments List");
		int num = 0;
		List<ClientTransactionIssuePayment> issuePaymentTransactionsList = getIssuePaymentTransactionsList(
				paymentMethod, transactionItem.getAccount().getName());
		for (ClientTransactionIssuePayment est : issuePaymentTransactionsList) {
			issuePaymentData.add(createIssuePaymentTransactionRecord(est));
			num++;
			if (num == ISSUE_PAYMENTS_TO_SHOW) {
				break;
			}
		}

		int size = issuePaymentTransactionsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a Transaction");
		}
		result.add(message.toString());
		result.add(issuePaymentData);
		result.add("Type for IssuePayment");

		return result;
	}

	private Record createIssuePaymentTransactionRecord(
			ClientTransactionIssuePayment est) {
		Record record = new Record(est);
		record.add("Name", est.getName());
		record.add("Value", est.getAmount());
		// TODO need to add more coloumns to record.
		return record;
	}

	private Result selectPaymentMethod(Context context) {
		String paymentmethod = getpaymentMethod(PAYMENT_METHOD_CHECK, context);
		Result result = context.makeResult();
		result.add("Select PaymentMethod");
		ResultList list = new ResultList(PAYMENT_METHOD);
		Record record = createPayMentMethodRecord(paymentmethod);
		list.add(record);
		result.add(list);
		return result;
	}

	private String getpaymentMethod(String paymentMethod, Context context) {
		if (paymentMethod == null) {
			return paymentMethod;
		}
		if (paymentMethod.equals(Accounter.constants().cheque())
				|| paymentMethod.equals(Accounter.constants().check())) {
			if (context.getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_US)
				return "Cheque";
			else if (context.getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_UK)
				return "Check";
		}
		return paymentMethod;

	}

}
