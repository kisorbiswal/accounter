package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;

public abstract class ReceivePaymentTableRequirement extends
		AbstractTableRequirement<ReceivePaymentTransactionList> {

	private static final String INVOICE_NO = "invoiceNo";
	private static final String INVOICE_AMOUNT = "invoiceAmount";
	private static final String PAYMENT = "receivePayment";
	private static final String AMOUNT_DUE = "amountDue";
	private static final String DUE_DATE = "BillDueDate";

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

		CurrencyAmountRequirement originalAmount = new CurrencyAmountRequirement(
				INVOICE_AMOUNT, "", getMessages().originalAmount(), true, true) {

			@Override
			protected String getFormalName() {
				return getCurrency().getFormalName();
			}

		};
		originalAmount.setEditable(false);
		list.add(originalAmount);

		CurrencyAmountRequirement amount = new CurrencyAmountRequirement(
				AMOUNT_DUE, getMessages()
						.pleaseEnter(getMessages().amountDue()), getMessages()
						.amountDue(), true, true) {

			@Override
			protected String getFormalName() {
				return getCurrency().getFormalName();
			}

		};
		CurrencyAmountRequirement receivePayment = new CurrencyAmountRequirement(
				PAYMENT, getMessages().pleaseEnter(
						getMessages().receivedPayment()), getMessages()
						.receivedPayment(), true, true) {

			@Override
			protected String getFormalName() {
				return getCurrency().getFormalName();
			}

		};
		list.add(amount);
		list.add(receivePayment);
	}

	@Override
	protected void getRequirementsValues(ReceivePaymentTransactionList obj) {
		Double amount = get(PAYMENT).getValue();
		Double due = obj.getAmountDue() - amount;
		obj.setPayment(amount);
		obj.setAmountDue(due);
	}

	@Override
	protected void setRequirementsDefaultValues(
			ReceivePaymentTransactionList obj) {
		get(DUE_DATE).setDefaultValue(obj.getDueDate());
		get(INVOICE_NO).setDefaultValue(obj.getNumber());
		get(INVOICE_AMOUNT).setDefaultValue(obj.getInvoiceAmount());
		get(AMOUNT_DUE).setDefaultValue(obj.getAmountDue());
		get(PAYMENT).setDefaultValue(obj.getAmountDue());

	}

	@Override
	protected ReceivePaymentTransactionList getNewObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Record createFullRecord(ReceivePaymentTransactionList t) {
		String formalName;
		if (getPreferences().isEnableMultiCurrency()) {
			formalName = getCurrency().getFormalName();
		} else {
			formalName = getPreferences().getPrimaryCurrency().getFormalName();
		}
		Record record = new Record(t);
		record.add(getMessages().dueDate(), t.getDueDate());
		record.add(getMessages().invoiceNo(), t.getNumber());
		record.add(getMessages().invoiceAmount() + "(" + formalName + ")",
				t.getInvoiceAmount());
		record.add(getMessages().amountDue() + "(" + formalName + ")",
				t.getAmountDue());
		record.add(getMessages().payment() + "(" + formalName + ")",
				t.getPayment());
		return record;
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().dueForPayment());
	}

	@Override
	protected Record createRecord(ReceivePaymentTransactionList t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getMessages().dueForPayment());
	}

	@Override
	protected boolean contains(List<ReceivePaymentTransactionList> oldValues,
			ReceivePaymentTransactionList t) {
		for (ReceivePaymentTransactionList receivePaymentTransactionList : oldValues) {
			if (t.getTransactionId() == receivePaymentTransactionList
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
