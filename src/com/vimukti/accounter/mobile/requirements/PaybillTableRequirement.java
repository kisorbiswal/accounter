package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;

public abstract class PaybillTableRequirement extends
		AbstractTableRequirement<PayBillTransactionList> {
	private static final String BILL_NO = "BillNo";
	private static final String ORIGINAL_AMOUNT = "OriginalAmount";
	private static final String AMOUNT = "Amount";
	private static final String DUE_DATE = "BillDueDate";

	public PaybillTableRequirement(String requirementName, String enterString,
			String recordName) {
		super(requirementName, enterString, recordName, false, false, true);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {

		DateRequirement billDueDate = new DateRequirement(DUE_DATE,
				getMessages().pleaseEnter(getMessages().dueDate()),
				getMessages().dueDate(), true, true);
		billDueDate.setEditable(false);
		list.add(billDueDate);

		NumberRequirement billNo = new NumberRequirement(BILL_NO, "",
				getMessages().billNo(), true, true);
		billNo.setEditable(false);
		list.add(billNo);

		CurrencyAmountRequirement originalAmount = new CurrencyAmountRequirement(
				ORIGINAL_AMOUNT, "", getMessages().originalAmount(), true, true) {
			@Override
			protected Currency getCurrency() {
				return PaybillTableRequirement.this.getCurrency();
			}
		};
		originalAmount.setEditable(false);
		list.add(originalAmount);

		CurrencyAmountRequirement amount = new CurrencyAmountRequirement(
				AMOUNT, getMessages().pleaseEnter(getMessages().amount()),
				getMessages().amount(), true, true) {
			@Override
			protected Currency getCurrency() {
				return PaybillTableRequirement.this.getCurrency();
			}
		};
		list.add(amount);

	}

	@Override
	public void setOtherFields(ResultList list, PayBillTransactionList obj) {
		double amount = get(AMOUNT).getValue();
		Double due = obj.getAmountDue() - amount;
		Record record = new Record(due);
		record.add(getMessages().amountDue(), due);
		list.add(3, record);
	}

	@Override
	protected void getRequirementsValues(PayBillTransactionList obj) {
		Double amount = get(AMOUNT).getValue();
		obj.setPayment(amount);
	}

	@Override
	protected void setRequirementsDefaultValues(PayBillTransactionList obj) {
		get(DUE_DATE).setValue(obj.getDueDate());
		get(BILL_NO).setValue(obj.getBillNumber());
		get(ORIGINAL_AMOUNT).setValue(obj.getOriginalAmount());
		get(AMOUNT).setValue(obj.getAmountDue());
	}

	@Override
	protected PayBillTransactionList getNewObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Record createFullRecord(PayBillTransactionList t) {
		String formalName;
		if (getPreferences().isEnableMultiCurrency()) {
			formalName = getCurrency().getFormalName();
		} else {
			formalName = getPreferences().getPrimaryCurrency().getFormalName();
		}

		Record record = new Record(t);
		record.add(getMessages().dueDate(), t.getDueDate());
		record.add(getMessages().billNo(), t.getBillNumber());
		record.add(getMessages().originalAmount() + "(" + formalName + ")",
				t.getOriginalAmount());
		record.add(getMessages().amountDue() + "(" + formalName + ")",
				t.getAmountDue());
		record.add(getMessages().payment() + "(" + formalName + ")",
				t.getPayment());
		return record;
	}

	@Override
	protected Record createRecord(PayBillTransactionList t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getMessages().billsToPay());
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().billsToPay());
	}

	@Override
	protected boolean contains(List<PayBillTransactionList> oldValues,
			PayBillTransactionList t) {
		for (PayBillTransactionList payBillTransactionList : oldValues) {
			if (payBillTransactionList.getTransactionId() == t
					.getTransactionId()) {
				return true;
			}
		}
		return false;
	}

	protected abstract Payee getPayee();

	protected Currency getCurrency() {
		return getPayee().getCurrency();
	}
}
