package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.ReceiveVAT;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TransactionReceiveVAT;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ListFilter;

public class ReceiveVATCommand extends AbstractVATCommand {

	private static final String VAT_RETURN_END_DATE = "vatReturnEndDate";
	private static final String BILLS_TO_RECEIVE = "billToReceive";
	private static final String BILLS_TO_RECEIVE_LIST = "billToReceiveList";
	private static final String DEPOSIT_TO = "depositTo";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(DEPOSIT_TO, false, true));
		list.add(new Requirement(PAYMENT_METHOD, false, true));
		list.add(new Requirement(VAT_RETURN_END_DATE, true, true));
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(ORDER_NO, true, true));
		list.add(new Requirement(BILLS_TO_RECEIVE, true, true));
	}

	@Override
	public Result run(Context context) {
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result result = context.makeResult();

		setDefaultValues();

		Result makeResult = context.makeResult();
		makeResult
				.add(getMessages().readyToCreate(getConstants().receiveVAT()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = accountRequirement(context, list, DEPOSIT_TO,
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						// TODO Auto-generated method stub
						return false;
					}
				});
		if (result != null) {
			return result;
		}

		result = paymentMethodRequirement(context, null, null);
		if (result != null) {
			return result;
		}

		result = billsToReceiveRequirement(context);
		if (result != null) {
			return result;
		}

		result = createOptionalRequirement(context);
		if (result != null) {
			return result;
		}

		return createPayVat(context);
	}

	private void setDefaultValues() {
		// TODO Auto-generated method stub

	}

	private Result createPayVat(Context context) {
		ReceiveVAT receiveVAT = new ReceiveVAT();

		Account depositTo = get(DEPOSIT_TO).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		List<TransactionReceiveVAT> billsToReceive = get(BILLS_TO_RECEIVE)
				.getValue();
		FinanceDate vatReturnDate = get(VAT_RETURN_END_DATE).getValue();
		FinanceDate transactionDate = get(DATE).getValue();
		String orderNo = get(ORDER_NO).getValue();

		receiveVAT.setDepositIn(depositTo);
		receiveVAT.setPaymentMethod(paymentMethod);
		receiveVAT.setTransactionReceiveVAT(billsToReceive);
		receiveVAT.setReturnsDueOnOrBefore(vatReturnDate);
		receiveVAT.setDate(transactionDate);
		receiveVAT.setNumber(orderNo);

		create(receiveVAT, context);

		markDone();
		Result result = new Result();
		result.add("Receive Vat created successfully.");

		return result;
	}

	private Result createOptionalRequirement(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_BILLS:
				return getBillsToReceiveResult(context);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");

		Requirement paymentMethodReq = get(PAYMENT_METHOD);
		String paymentMethod = (String) paymentMethodReq.getValue();
		if (paymentMethod == selection) {
			context.setAttribute(INPUT_ATTR, PAYMENT_METHOD);
			return paymentMethod(context, paymentMethod);
		}

		Requirement billsToReceiveReq = get(BILLS_TO_RECEIVE);
		List<TransactionReceiveVAT> transReceiveVats = billsToReceiveReq
				.getValue();

		selection = context.getSelection("transactionReceiveVats");
		if (selection != null) {
			Result result = getBillsToReceiveResult(context);
			if (result != null) {
				return result;
			}
		}

		ResultList list = new ResultList("values");

		Record paymentMethodRecord = new Record(paymentMethod);
		paymentMethodRecord.add("Name", "Pay Method");
		paymentMethodRecord.add("Value", paymentMethod);
		list.add(paymentMethodRecord);

		Result result = dateOptionalRequirement(context, list,
				VAT_RETURN_END_DATE, "Filter by VAT return end date", selection);
		if (result != null) {
			return result;
		}

		result = dateRequirement(context, list, selection, DATE,
				"Enter the date");
		if (result != null) {
			return result;
		}

		result = orderNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add("Receive Vat is ready to create with following values.");
		result.add(list);
		result.add("Bill To Receive:-");
		ResultList payVats = new ResultList("transactionReceiveVats");
		for (TransactionReceiveVAT payVat : transReceiveVats) {
			Record itemRec = createTransactionReceiveVatRecord(payVat);
			payVats.add(itemRec);
		}
		result.add(payVats);

		ResultList actions = new ResultList(ACTIONS);
		Record moreItems = new Record(ActionNames.ADD_MORE_BILLS);
		moreItems.add("", "Add more bills");
		actions.add(moreItems);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to Receive vat.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result billsToReceiveRequirement(Context context) {
		Requirement billsToReceiveReq = get(BILLS_TO_RECEIVE);
		List<TransactionReceiveVAT> transactionReceiveVatBills = context
				.getSelections(BILLS_TO_RECEIVE_LIST);
		if (!billsToReceiveReq.isDone()) {
			if (transactionReceiveVatBills.size() > 0) {
				billsToReceiveReq.setValue(transactionReceiveVatBills);
			} else {
				return getBillsToReceiveResult(context);
			}
		}
		if (transactionReceiveVatBills != null
				&& transactionReceiveVatBills.size() > 0) {
			List<TransactionReceiveVAT> receiveVats = billsToReceiveReq
					.getValue();
			receiveVats.addAll(transactionReceiveVatBills);
		}
		return null;
	}

	private Result getBillsToReceiveResult(Context context) {
		Result result = context.makeResult();
		List<TransactionReceiveVAT> transactionPayVats = getTransactionReceiveVatBills(context
				.getHibernateSession());
		ResultList list = new ResultList(BILLS_TO_RECEIVE_LIST);
		Object last = context.getLast(RequirementType.TRANSACTION_PAY_VAT);
		int num = 0;
		if (last != null) {
			list.add(createTransactionReceiveVatRecord((TransactionReceiveVAT) last));
			num++;
		}
		Requirement payBillsReq = get(BILLS_TO_RECEIVE_LIST);
		List<TransactionReceiveVAT> transPayVats = payBillsReq.getValue();
		List<TransactionReceiveVAT> availablePayVats = new ArrayList<TransactionReceiveVAT>();
		for (TransactionReceiveVAT transactionItem : transPayVats) {
			availablePayVats.add(transactionItem);
		}
		for (TransactionReceiveVAT transactionPayVat : transactionPayVats) {
			if (transactionPayVat != last
					|| !availablePayVats.contains(transactionPayVat)) {
				list.add(createTransactionReceiveVatRecord(transactionPayVat));
				num++;
			}
			if (num == VALUES_TO_SHOW) {
				break;
			}
		}
		list.setMultiSelection(true);
		if (list.size() > 0) {
			result.add("Slect Bill to pay.");
		} else {
			result.add("You don't have Bills.");
		}

		result.add(list);
		return result;
	}

	private List<TransactionReceiveVAT> getTransactionReceiveVatBills(
			Session hibernateSession) {
		// TODO Auto-generated method stub
		return null;
	}

	private Record createTransactionReceiveVatRecord(
			TransactionReceiveVAT receiveVat) {
		Record record = new Record(receiveVat);
		TAXAgency taxAgency = receiveVat.getTaxAgency();
		record.add("Vat Agency", taxAgency != null ? taxAgency.getName() : "");
		record.add("Tax Due", receiveVat.getTaxDue());
		record.add("Amount to Receive", receiveVat.getAmountToReceive());
		return record;
	}

}
