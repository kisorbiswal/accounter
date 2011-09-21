package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.PayVAT;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TransactionPayVAT;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class PayVATCommand extends AbstractVATCommand {

	private static final String VAT_RETURN_END_DATE = "vatReturnEndDate";
	private static final String BILLS_TO_PAY = "billToPay";
	private static final String BILLS_TO_PAY_LIST = null;
	private static final String PAY_FROM = null;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(PAY_FROM, false, true));
		list.add(new Requirement(PAYMENT_METHOD, false, true));
		list.add(new Requirement(VAT_RETURN_END_DATE, true, true));
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(ORDER_NO, true, true));
		list.add(new Requirement(BILLS_TO_PAY, true, true));
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = accountRequirement(context, PAY_FROM);
		if (result != null) {
			return result;
		}

		result = paymentMethodRequirement(context);
		if (result != null) {
			return result;
		}

		result = billsToPayRequirement(context);
		if (result != null) {
			return result;
		}

		result = createOptionalRequirement(context);
		if (result != null) {
			return result;
		}

		return createPayVat(context);
	}

	private Result createPayVat(Context context) {
		PayVAT payVAT = new PayVAT();

		Account payFrom = get(PAY_FROM).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		List<TransactionPayVAT> billsToPay = get(BILLS_TO_PAY).getValue();
		FinanceDate vatReturnDate = get(VAT_RETURN_END_DATE).getValue();
		FinanceDate transactionDate = get(DATE).getValue();
		String orderNo = get(ORDER_NO).getValue();

		payVAT.setPayFrom(payFrom);
		payVAT.setPaymentMethod(paymentMethod);
		payVAT.setTransactionPayVAT(billsToPay);
		payVAT.setReturnsDueOnOrBefore(vatReturnDate);
		payVAT.setDate(transactionDate);
		payVAT.setNumber(orderNo);

		create(payVAT, context);

		markDone();
		Result result = new Result();
		result.add("Pay Vat created successfully.");

		return result;
	}

	private Result createOptionalRequirement(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_BILLS:
				return getBillsToPayResult(context);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");

		Requirement payFromReq = get(PAY_FROM);
		Account payFrom = (Account) payFromReq.getValue();
		if (payFrom == selection) {
			context.setAttribute(INPUT_ATTR, PAY_FROM);
			return getAccountResult(context);
		}

		Requirement paymentMethodReq = get(PAYMENT_METHOD);
		String paymentMethod = (String) paymentMethodReq.getValue();
		if (paymentMethod == selection) {
			context.setAttribute(INPUT_ATTR, PAYMENT_METHOD);
			return paymentMethod(context, paymentMethod);
		}

		Requirement billsToPayReq = get(BILLS_TO_PAY);
		List<TransactionPayVAT> transPayVats = billsToPayReq.getValue();

		selection = context.getSelection("transactionPayVats");
		if (selection != null) {
			Result result = getBillsToPayResult(context);
			if (result != null) {
				return result;
			}
		}

		ResultList list = new ResultList("values");

		Record payFromRecord = new Record(payFrom);
		payFromRecord.add("Name", "Pay From");
		payFromRecord.add("Value", payFrom.getName());
		list.add(payFromRecord);

		Record paymentMethodRecord = new Record(paymentMethod);
		paymentMethodRecord.add("Name", "Pay From");
		paymentMethodRecord.add("Value", paymentMethod);
		list.add(paymentMethodRecord);

		Result result = dateOptionalRequirement(context, list,
				VAT_RETURN_END_DATE, "Filter by VAT return end date", selection);
		if (result != null) {
			return result;
		}

		result = dateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = orderNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add("Pay Vat is ready to create with following values.");
		result.add(list);
		result.add("Bill To Pay:-");
		ResultList payVats = new ResultList("transactionPayVats");
		for (TransactionPayVAT payVat : transPayVats) {
			Record itemRec = createTransactionPayVatRecord(payVat);
			payVats.add(itemRec);
		}
		result.add(payVats);

		ResultList actions = new ResultList(ACTIONS);
		Record moreItems = new Record(ActionNames.ADD_MORE_BILLS);
		moreItems.add("", "Add more bills");
		actions.add(moreItems);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to Pay vat.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result billsToPayRequirement(Context context) {
		Requirement billsToPayReq = get(BILLS_TO_PAY);
		List<TransactionPayVAT> transactionPayVatBills = context
				.getSelections(BILLS_TO_PAY);
		if (!billsToPayReq.isDone()) {
			if (transactionPayVatBills.size() > 0) {
				billsToPayReq.setValue(transactionPayVatBills);
			} else {
				return getBillsToPayResult(context);
			}
		}
		if (transactionPayVatBills != null && transactionPayVatBills.size() > 0) {
			List<TransactionPayVAT> items = billsToPayReq.getValue();
			items.addAll(transactionPayVatBills);
		}
		return null;
	}

	private Result getBillsToPayResult(Context context) {
		Result result = context.makeResult();
		List<TransactionPayVAT> transactionPayVats = getTransactionPayVatBills(context
				.getHibernateSession());
		ResultList list = new ResultList(BILLS_TO_PAY_LIST);
		Object last = context.getLast(RequirementType.TRANSACTION_PAY_VAT);
		int num = 0;
		if (last != null) {
			list.add(createTransactionPayVatRecord((TransactionPayVAT) last));
			num++;
		}
		Requirement payBillsReq = get(BILLS_TO_PAY_LIST);
		List<TransactionPayVAT> transPayVats = payBillsReq.getValue();
		List<TransactionPayVAT> availablePayVats = new ArrayList<TransactionPayVAT>();
		for (TransactionPayVAT transactionItem : transPayVats) {
			availablePayVats.add(transactionItem);
		}
		for (TransactionPayVAT transactionPayVat : transactionPayVats) {
			if (transactionPayVat != last
					|| !availablePayVats.contains(transactionPayVat)) {
				list.add(createTransactionPayVatRecord(transactionPayVat));
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

	private List<TransactionPayVAT> getTransactionPayVatBills(
			Session hibernateSession) {
		// TODO Auto-generated method stub
		return null;
	}

	private Record createTransactionPayVatRecord(TransactionPayVAT payVatBill) {
		Record record = new Record(payVatBill);
		TAXAgency taxAgency = payVatBill.getTaxAgency();
		record.add("Vat Agency", taxAgency != null ? taxAgency.getName() : "");
		record.add("Tax Due", payVatBill.getTaxDue());
		record.add("Amount to pay", payVatBill.getAmountToPay());
		return record;
	}

	@Override
	protected List<Account> getAccounts(Session session) {
		// TODO Auto-generated method stub
		return super.getAccounts(session);
	}

}
