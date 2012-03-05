package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;

public abstract class ReceivePaymentTableRequirement extends
		AbstractTableRequirement<ReceivePaymentTransactionList> {

	private static final String INVOICE_NO = "invoiceNo";
	private static final String INVOICE_AMOUNT = "invoiceAmount";
	private static final String PAYMENT = "receivePayment";
	private static final String AMOUNT_DUE = "amountDue";
	private static final String DUE_DATE = "BillDueDate";

	// private static final String APPLIED_CREDITS = "appliedcredits";

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
			protected Currency getCurrency() {
				return ReceivePaymentTableRequirement.this.getCurrency();
			}

		};
		originalAmount.setEditable(false);
		list.add(originalAmount);

		CurrencyAmountRequirement amountDue = new CurrencyAmountRequirement(
				AMOUNT_DUE, getMessages()
						.pleaseEnter(getMessages().amountDue()), getMessages()
						.amountDue(), true, true) {

			@Override
			protected Currency getCurrency() {
				return ReceivePaymentTableRequirement.this.getCurrency();
			}

		};
		amountDue.setEditable(false);
		list.add(amountDue);

		// list.add(new ApplyCreditsRequirement(APPLIED_CREDITS,
		// "Please select record", "Record") {
		//
		// @Override
		// protected String getFormalName() {
		// return ReceivePaymentTableRequirement.this.getFormalName();
		// }
		//
		// @Override
		// protected List<ClientCreditsAndPayments> getCreditsPayments() {
		// return ReceivePaymentTableRequirement.this.getCreditsPayments();
		// }
		// });

		CurrencyAmountRequirement paymentReq = new CurrencyAmountRequirement(
				PAYMENT, getMessages().pleaseEnter(getMessages().payment()),
				getMessages().payment(), true, true) {

			@Override
			protected Currency getCurrency() {
				return ReceivePaymentTableRequirement.this.getCurrency();
			}

		};

		list.add(paymentReq);
	}

	protected String getFormalName() {
		return getPayee().getCurrency().getFormalName();
	}

	@Override
	protected void getRequirementsValues(ReceivePaymentTransactionList obj) {
		Double amount = get(PAYMENT).getValue();
		obj.setPayment(amount);
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

	public List<ClientTransactionCreditsAndPayments> getTransactionCredits(
			ClientTransactionReceivePayment payment) {
		// ApplyCreditsRequirement requirement = (ApplyCreditsRequirement)
		// get(APPLIED_CREDITS);
		return null;// requirement.getTransactionCredits(payment);
	}
}
