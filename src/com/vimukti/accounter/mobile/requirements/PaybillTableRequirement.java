package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;

public abstract class PaybillTableRequirement extends
		AbstractTableRequirement<ClientTransactionPayBill> {
	private static final String BILL_NO = "BillNo";
	private static final String ORIGINAL_AMOUNT = "OriginalAmount";
	private static final String AMOUNT = "Amount";
	private static final String DUE_DATE = "BillDueDate";
	private static final String CASH_DISCOUNT = "cashDiscount";// IF enabled
																// discounts
	private static final String APPLIED_CREDITS = "appliedcredits";
	private static final String PAYMENT = "payment";

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
		CurrencyAmountRequirement amountDue = new CurrencyAmountRequirement(
				AMOUNT, getMessages().pleaseEnter(getMessages().amountDue()),
				getMessages().amountDue(), true, true) {
			@Override
			protected Currency getCurrency() {
				return PaybillTableRequirement.this.getCurrency();
			}
		};
		amountDue.setEditable(false);
		list.add(amountDue);

		list.add(new ApplyCreditsRequirement(APPLIED_CREDITS, getMessages()
				.pleaseEnter(getMessages().appliedCredits()), getMessages()
				.appliedCredits()) {

			@Override
			protected Currency getCurrency() {
				return PaybillTableRequirement.this.getCurrency();
			}

			@Override
			protected double getAmountDue() {
				return PaybillTableRequirement.this.get(AMOUNT).getValue();
			}

			@Override
			public List<ClientCreditsAndPayments> getTotalCredits() {
				return PaybillTableRequirement.this.getCreditsPayments();
			}

			public java.util.List<ClientTransactionPayBill> getPayBillPayments() {
				return PaybillTableRequirement.this.getValue();
			}

			@Override
			public void setValue(Object value) {
				super.setValue(value);
				ClientTransactionPayBill obj = PaybillTableRequirement.this.currentValue;
				if (obj != null) {
					if (value != null) {
						obj.setAppliedCredits((Double) value, true);
					}
					obj.updatePayment();
					// obj.setDummyDue(obj.getAmountDue() - obj.getPayment());
					updatePayment(obj);
					PaybillTableRequirement.this.get(PAYMENT).setValue(
							obj.getPayment());
				}
			}

		});

		CurrencyAmountRequirement amount = new CurrencyAmountRequirement(
				PAYMENT, getMessages().pleaseEnter(getMessages().payment()),
				getMessages().payment(), true, true) {
			@Override
			protected Currency getCurrency() {
				return PaybillTableRequirement.this.getCurrency();
			}
		};
		list.add(amount);

	}

	public void vendorSelected() {
		((ApplyCreditsRequirement) get(APPLIED_CREDITS))
				.addCreditsAndPayments();
	}

	protected abstract List<ClientCreditsAndPayments> getCreditsPayments();

	public abstract long getTransactionId();

	protected void updatePayment(ClientTransactionPayBill obj) {
	}

	@Override
	protected void getRequirementsValues(ClientTransactionPayBill obj) {
		Double amount = get(PAYMENT).getValue();
		obj.setPayment(amount);
		Double appliedCredits = get(APPLIED_CREDITS).getValue();
		obj.setAppliedCredits(appliedCredits, true);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientTransactionPayBill obj) {
		get(DUE_DATE).setValue(new ClientFinanceDate(obj.getDueDate()));
		get(BILL_NO).setValue(obj.getBillNumber());
		get(ORIGINAL_AMOUNT).setValue(obj.getOriginalAmount());
		get(AMOUNT).setValue(obj.getAmountDue());
		get(APPLIED_CREDITS).setValue(obj.getAppliedCredits());
		get(PAYMENT).setDefaultValue(obj.getAmountDue());
		ApplyCreditsRequirement applyCreditsRequirement = (ApplyCreditsRequirement) get(APPLIED_CREDITS);
		applyCreditsRequirement.setTransactionCreditsAndPayments(obj
				.getTransactionCreditsAndPayments());
	}

	@Override
	protected ClientTransactionPayBill getNewObject() {
		return null;
	}

	@Override
	protected Record createFullRecord(ClientTransactionPayBill t) {
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
		record.add(getMessages().appliedCredits(), t.getAppliedCredits());
		record.add(getMessages().payment() + "(" + formalName + ")",
				t.getPayment());
		return record;
	}

	@Override
	protected Record createRecord(ClientTransactionPayBill t) {
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
	protected boolean contains(List<ClientTransactionPayBill> oldValues,
			ClientTransactionPayBill t) {
		for (ClientTransactionPayBill payBillTransactionList : oldValues) {
			if (payBillTransactionList.getEnterBill() == t.getEnterBill()) {
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
