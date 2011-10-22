package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientIssuePayment;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionIssuePayment;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.server.FinanceTool;

public class NewIssuePaymentCommand extends AbstractTransactionCommand {
	private static final String PAYMENTS_TO_ISSUED = "paymentstoissued";
	private static final String NAME = "Name";
	public static final String PAYMENT_METHOD_CHECK = "Check";
	private static final String PAYMENTS_TO_ISSUED_LIST = "paymentsToIssuesList";
	private static final String CHEQUE_NO = "checknum";
	protected static final String AMOUNT = "amount";
	private static final String OPTIONAL = "optional";
	private static final String VALUES = "values";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(PAYMENT_METHOD, false, true));
		list.add(new Requirement(ACCOUNT, false, true));
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
			context.setAttribute(INPUT_ATTR, OPTIONAL);
		}

		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(
				getConstants().issuePayment()));
		ResultList list = new ResultList(VALUES);
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);

		String paymentMethod = get(PAYMENT_METHOD).getValue();
		Record supplierRecord = new Record(PAYMENT_METHOD);
		supplierRecord.add("", getConstants().paymentMethod());
		supplierRecord.add("", paymentMethod);
		list.add(supplierRecord);

		result = accountRequirement(context, list, ACCOUNT, getConstants()
				.bankAccounts(), new ListFilter<ClientAccount>() {

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

		setDefaults();
		if (context.getSelection(ACCOUNT) != null) {
			get(PAYMENTS_TO_ISSUED).setValue(
					new ArrayList<ClientTransactionIssuePayment>());
		}

		result = transactionIssuePaymentRequirement(context, list, makeResult,
				actions);
		if (result != null) {
			return result;
		}
		makeResult.add(actions);
		result = optionalRequirement(context, makeResult, list, actions);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		result = new Result(getMessages().createSuccessfully(
				getConstants().issuePayment()));
		return result;
	}

	private Result optionalRequirement(Context context, Result makeResult,
			ResultList list, ResultList actions) {
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, OPTIONAL);
		}
		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}
		Result result = numberRequirement(context, list, CHEQUE_NO,
				getMessages().pleaseEnter(getConstants().chequeNo()),
				getConstants().chequeNo());
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("",
				getMessages().finishToCreate(getConstants().issuePayment()));
		actions.add(finish);

		return makeResult;
	}

	private void setDefaults() {
		get(CHEQUE_NO).setValue(getNextCheckNumber());
	}

	private String getNextCheckNumber() {
		ClientAccount account = get(ACCOUNT).getValue();
		String checknumber = "";
		try {
			String nextIssuePaymentCheckNumber = new FinanceTool()
					.getNextIssuePaymentCheckNumber(account.getID(),
							getClientCompany().getID());
			if (nextIssuePaymentCheckNumber != null) {
				checknumber = nextIssuePaymentCheckNumber;
			}
		} catch (Exception e) {
			e.printStackTrace();
			checknumber = "";
		}
		return checknumber;
	}

	private String getNextTransactionNumber() {
		String nextTransactionNumber = new FinanceTool()
				.getNextTransactionNumber(ClientTransaction.TYPE_ISSUE_PAYMENT,
						getClientCompany().getID());
		return nextTransactionNumber;
	}

	private Result transactionIssuePaymentRequirement(Context context,
			ResultList list, Result makeResult, ResultList actions) {

		Requirement transItemsReq = get(PAYMENTS_TO_ISSUED);
		List<ClientTransactionIssuePayment> items = context
				.getSelections(PAYMENTS_TO_ISSUED);
		List<ClientTransactionIssuePayment> transactionItems = transItemsReq
				.getValue();
		if (items != null) {
			for (ClientTransactionIssuePayment item : items) {
				transactionItems.add(item);
			}
		}
		List<ClientTransactionIssuePayment> deleted = context
				.getSelections(PAYMENTS_TO_ISSUED_LIST);
		if (deleted != null) {
			for (ClientTransactionIssuePayment item : deleted) {
				transactionItems.remove(item);
			}
		}

		if (transactionItems.size() == 0) {
			return clientTransactionIssuePayments(context);
		}

		Object selection = context.getSelection(ACTIONS);
		ActionNames actionName = (ActionNames) selection;
		if (actionName == ActionNames.ADD_MORE_ITEMS) {
			return clientTransactionIssuePayments(context);
		}

		makeResult.add(getConstants().issuePayment());
		ResultList itemsList = new ResultList(PAYMENTS_TO_ISSUED_LIST);
		for (ClientTransactionIssuePayment item : transactionItems) {
			itemsList.add(creatTransactioIssuePaymentRecord(item));
		}
		makeResult.add(itemsList);

		Record moreItems = new Record(ActionNames.ADD_MORE_ITEMS);
		moreItems
				.add("", getMessages().addMore(getConstants().issuePayments()));
		actions.add(moreItems);
		return null;
	}

	private Record creatTransactioIssuePaymentRecord(
			ClientTransactionIssuePayment entry) {
		Record issuepaymentrecord = new Record(entry);
		issuepaymentrecord.add("", getConstants().date());
		issuepaymentrecord.add("",
				getDateAsString(new ClientFinanceDate(entry.getDate())));
		if (entry.getNumber() != null)
			issuepaymentrecord.add("", getConstants().number());
		issuepaymentrecord.add("", entry.getNumber());
		issuepaymentrecord.add("", getConstants().name());
		issuepaymentrecord.add("", entry.getName() != null ? entry.getName()
				: "");
		issuepaymentrecord.add("", getConstants().memo());
		issuepaymentrecord.add("", entry.getMemo() != null ? entry.getMemo()
				: "");
		issuepaymentrecord.add("", getConstants().amount());
		issuepaymentrecord.add("", entry.getAmount());
		if (entry.getPaymentMethod() != null)
			issuepaymentrecord.add("", getConstants().paymentMethod());
		issuepaymentrecord.add("", entry.getPaymentMethod());
		return issuepaymentrecord;
	}

	private Result clientTransactionIssuePayments(Context context) {
		Result result = context.makeResult();
		List<ClientTransactionIssuePayment> items = getclientTransactionIssuePayments();
		ResultList list = new ResultList(PAYMENTS_TO_ISSUED);
		ClientTransactionIssuePayment last = (ClientTransactionIssuePayment) context
				.getLast(RequirementType.TRANSACTION_ISSUE_PAYMENT);
		int num = 0;
		if (last != null) {
			list.add(creatTransactioIssuePaymentRecord(last));
			num++;
		}
		Requirement itemsReq = get(PAYMENTS_TO_ISSUED);
		List<ClientTransactionIssuePayment> transItems = itemsReq.getValue();
		List<ClientTransactionIssuePayment> availableItems = new ArrayList<ClientTransactionIssuePayment>();
		for (ClientTransactionIssuePayment transactionItem : transItems) {
			availableItems.add(transactionItem);
		}
		for (ClientTransactionIssuePayment item : items) {
			if (item != last && !availableItems.contains(item.getID())) {
				list.add(creatTransactioIssuePaymentRecord(item));
				num++;
			}
			if (num == ITEMS_TO_SHOW) {
				break;
			}
		}
		list.setMultiSelection(true);
		if (list.size() > 0) {
			result.add(getMessages().pleaseSelect(
					getConstants().issuePayments()));
		} else {
			result.add(getMessages().youDontHaveAny(
					getConstants().issuePayments()));
		}
		result.add(list);
		return result;
	}

	private List<ClientTransactionIssuePayment> getclientTransactionIssuePayments() {
		ClientAccount account = get(ACCOUNT).getValue();
		return getchecks(account.getID());
	}

	private void completeProcess(Context context) {
		ClientIssuePayment issuePayment = new ClientIssuePayment();
		issuePayment.setType(ClientTransaction.TYPE_ISSUE_PAYMENT);
		issuePayment.setNumber(getNextTransactionNumber());
		issuePayment.setDate(new ClientFinanceDate().getDate());
		String paymentmethod = get(PAYMENT_METHOD).getValue();
		issuePayment.setPaymentMethod(paymentmethod);
		ClientAccount account = get(ACCOUNT).getValue();
		issuePayment.setAccount(account.getID());
		String chequenum = get(CHEQUE_NO).getValue();
		if (chequenum.isEmpty()) {
			chequenum = "1";
		}
		issuePayment.setCheckNumber(chequenum);
		ArrayList<ClientTransactionIssuePayment> issuepayments = get(
				PAYMENTS_TO_ISSUED).getValue();
		issuePayment.setTransactionIssuePayment(issuepayments);
		setTransactionTotal(issuePayment);
		create(issuePayment, context);
	}

	private void setTransactionTotal(ClientIssuePayment issuePayment) {
		double total = 0.0;
		for (ClientTransactionIssuePayment rec : issuePayment
				.getTransactionIssuePayment()) {
			total += rec.getAmount();
		}
		issuePayment.setTotal(total);

	}
}
