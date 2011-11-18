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
	private static final String PAYMENT = "payment";

	public ReceivePaymentTableRequirement(String requirementName,
			String enterString, String recordName) {
		super(requirementName, enterString, recordName, false, false, true);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		DateRequirement billDueDate = new DateRequirement(DUE_DATE,
				getMessages().pleaseEnter(getMessages().dueDate()),
				getMessages().dueDate(), true, true);
		billDueDate.setEditable(false);
		list.add(billDueDate);

		NumberRequirement billNo = new NumberRequirement(INVOICE_NO, "",
				getMessages().billNo(), true, true);
		billNo.setEditable(false);
		list.add(billNo);

		AmountRequirement originalAmount = new AmountRequirement(
				INVOICE_AMOUNT, "", getMessages().originalAmount(), true, true);
		originalAmount.setEditable(false);
		list.add(originalAmount);

		AmountRequirement amount = new AmountRequirement(PAYMENT, getMessages()
				.pleaseEnter(getMessages().amount()), getMessages().amount(),
				true, true);
		list.add(amount);
	}

	@Override
	protected void getRequirementsValues(ClientTransactionReceivePayment obj) {
		Double payment = get(PAYMENT).getValue();
		Double originalAmt = get(INVOICE_AMOUNT).getValue();
		Double due = originalAmt - payment;
		obj.setPayment(payment);
		obj.setAmountDue(due);
	}

	@Override
	protected void setRequirementsDefaultValues(
			ClientTransactionReceivePayment obj) {
		get(DUE_DATE).setDefaultValue(new ClientFinanceDate(obj.getDueDate()));
		get(INVOICE_NO).setDefaultValue(obj.getNumber());
		get(INVOICE_AMOUNT).setDefaultValue(obj.getInvoiceAmount());
		get(PAYMENT).setDefaultValue(obj.getInvoiceAmount());

	}

	@Override
	protected ClientTransactionReceivePayment getNewObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Record createFullRecord(ClientTransactionReceivePayment t) {
		Record record = new Record(t);
		record.add("", getMessages().dueDate());
		record.add("", t.getDueDate());
		record.add("", getMessages().invoiceNo());
		record.add("", t.getNumber());
		record.add("", getMessages().invoiceAmount());
		record.add("", t.getInvoiceAmount());
		record.add("", getMessages().amountDue());
		record.add("", t.getAmountDue());
		record.add("", getMessages().payment());
		record.add("", t.getPayment());
		return record;
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().dueForPayment());
	}

	@Override
	protected Record createRecord(ClientTransactionReceivePayment t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getMessages().dueForPayment());
	}

}
