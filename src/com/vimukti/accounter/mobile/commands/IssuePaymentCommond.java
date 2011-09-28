package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.ui.Accounter;

public class IssuePaymentCommond extends AbstractTransactionCommand {
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

		Result result = selectPaymentMethod(context);
		if (result != null) {
			return result;
		}
		result = accountsRequirement(context, ACCOUNTS);
		if (result != null) {
			return result;
		}

		return null;
	}

	private Result selectPaymentMethod(Context context) {
		String paymentmethod = getpaymentMethod(PAYMENT_METHOD_CHECK);
		Result result = context.makeResult();
		result.add("Select PaymentMethod");
		ResultList list = new ResultList(PAYMENT_MENTHOD);
		Record record = createPayMentMethodRecord(paymentmethod);
		list.add(record);
		result.add(list);
		return result;
	}

	private String getpaymentMethod(String paymentMethod) {
		if (paymentMethod == null) {
			return paymentMethod;
		}
		if (paymentMethod.equals(Accounter.constants().cheque())
				|| paymentMethod.equals(Accounter.constants().check())) {
			if (getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_US)
				return "Cheque";
			else if (getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_UK)
				return "Check";
		}
		return paymentMethod;

	}

}
