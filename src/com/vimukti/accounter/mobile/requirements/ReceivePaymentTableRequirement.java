package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;

public abstract class ReceivePaymentTableRequirement extends
		AbstractTableRequirement<ClientTransactionReceivePayment> {

	private static final String INVOICE_NO = "invoiceNo";
	private static final String INVOICE_AMOUNT = "invoiceAmount";
	private static final String PAYMENT = "receivePayment";
	private static final String AMOUNT_DUE = "amountDue";

	public ReceivePaymentTableRequirement(String requirementName,
			String enterString, String recordName) {
		super(requirementName, enterString, recordName, false, false, true);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		DateRequirement billDueDate = new DateRequirement(DUE_DATE,
				getMessages().pleaseEnter(getConstants().dueDate()),
				getConstants().dueDate(), true, true);
		billDueDate.setEditable(false);
		list.add(billDueDate);

		NumberRequirement billNo = new NumberRequirement(INVOICE_NO, "",
				getConstants().billNo(), true, true);
		billNo.setEditable(false);
		list.add(billNo);

		AmountRequirement originalAmount = new AmountRequirement(
				INVOICE_AMOUNT, "", getConstants().originalAmount(), true, true);
		originalAmount.setEditable(false);
		list.add(originalAmount);

		AmountRequirement amount = new AmountRequirement(AMOUNT_DUE,
				getMessages().pleaseEnter(getConstants().amountDue()),
				getConstants().amountDue(), true, true);
		AmountRequirement receivePayment = new AmountRequirement(PAYMENT,
				getMessages().pleaseEnter(getConstants().receivedPayment()),
				getConstants().receivedPayment(), true, true);
		list.add(amount);
		list.add(receivePayment);
	}

	@Override
	protected void getRequirementsValues(ClientTransactionReceivePayment obj) {
		Double amount = get(PAYMENT).getValue();
		Double due = obj.getAmountDue() - amount;
		obj.setPayment(amount);
		obj.setAmountDue(due);
	}

	@Override
	protected void setRequirementsDefaultValues(
			ClientTransactionReceivePayment obj) {
		get(DUE_DATE).setDefaultValue(new ClientFinanceDate(obj.getDueDate()));
		get(INVOICE_NO).setDefaultValue(obj.getNumber());
		get(INVOICE_AMOUNT).setDefaultValue(obj.getInvoiceAmount());
		get(AMOUNT_DUE).setDefaultValue(obj.getAmountDue());
		get(PAYMENT).setDefaultValue(obj.getAmountDue());

	}

	@Override
	protected ClientTransactionReceivePayment getNewObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Record createFullRecord(ClientTransactionReceivePayment t) {
		Record record = new Record(t);
		record.add("", getConstants().dueDate());
		record.add("", t.getDueDate());
		record.add("", getConstants().invoiceNo());
		record.add("", t.getNumber());
		record.add("", getConstants().invoiceAmount());
		record.add("", t.getInvoiceAmount());
		record.add("", getConstants().amountDue());
		record.add("", t.getAmountDue());
		record.add("", getConstants().payment());
		record.add("", t.getPayment());
		return record;
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getConstants().dueForPayment());
	}

	@Override
	protected Record createRecord(ClientTransactionReceivePayment t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getConstants().dueForPayment());
	}

}
