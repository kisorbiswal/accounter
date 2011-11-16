package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;

public abstract class PaybillTableRequirement extends
		AbstractTableRequirement<PayBillTransactionList> {
	private static final String BILL_NO = "BillNo";
	private static final String ORIGINAL_AMOUNT = "OriginalAmount";
	private static final String AMOUNT = "Amount";

	public PaybillTableRequirement(String requirementName, String enterString,
			String recordName) {
		super(requirementName, enterString, recordName, false, false, true);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {

		DateRequirement billDueDate = new DateRequirement(DUE_DATE,
				getMessages().pleaseEnter(getConstants().dueDate()),
				getConstants().dueDate(), true, true);
		billDueDate.setEditable(false);
		list.add(billDueDate);

		NumberRequirement billNo = new NumberRequirement(BILL_NO, "",
				getConstants().billNo(), true, true);
		billNo.setEditable(false);
		list.add(billNo);

		AmountRequirement originalAmount = new AmountRequirement(
				ORIGINAL_AMOUNT, "", getConstants().originalAmount(), true,
				true);
		originalAmount.setEditable(false);
		list.add(originalAmount);

		AmountRequirement amount = new AmountRequirement(AMOUNT, getMessages()
				.pleaseEnter(getConstants().amount()), getConstants().amount(),
				true, true);
		list.add(amount);

	}

	@Override
	public void setOtherFields(ResultList list, PayBillTransactionList obj) {
		double amount = get(AMOUNT).getValue();
		Double due = obj.getAmountDue() - amount;
		Record record = new Record(due);
		record.add("", getConstants().amountDue());
		record.add("", due);
		list.add(3, record);
	}

	@Override
	protected void getRequirementsValues(PayBillTransactionList obj) {
		Double amount = get(AMOUNT).getValue();
		Double due = obj.getAmountDue() - amount;
		obj.setPayment(amount);
		obj.setAmountDue(due);
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
		Record record = new Record(t);
		record.add("", getConstants().dueDate());
		record.add("", t.getDueDate());
		record.add("", getConstants().billNo());
		record.add("", t.getBillNumber());
		record.add("", getConstants().originalAmount());
		record.add("", t.getOriginalAmount());
		record.add("", getConstants().amountDue());
		record.add("", t.getAmountDue());
		record.add("", getConstants().payment());
		record.add("", t.getPayment());
		return record;
	}

	@Override
	protected Record createRecord(PayBillTransactionList t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getConstants().billsToPay());
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getConstants().billsToPay());
	}

}
