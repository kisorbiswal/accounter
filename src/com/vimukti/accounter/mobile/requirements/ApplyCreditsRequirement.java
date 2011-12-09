package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;

public abstract class ApplyCreditsRequirement extends MultiRequirement<Double> {

	private static final String CREDITS_PAYMENTS = "creditsandpayments";
	private static final String AMOUNT_TO_USE = "creditspaymentsamounttouse";
	private static final String AMOUNT_DUE = "creditspaymentsamountDue";

	public ApplyCreditsRequirement(String requirementName, String enterString,
			String recordName) {
		super(requirementName, enterString, recordName, true, true);
		setValue(new ArrayList<ClientTransactionCreditsAndPayments>());
	}

	@Override
	protected void setDefaultValues() {
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		CurrencyAmountRequirement amountDue = new CurrencyAmountRequirement(
				AMOUNT_DUE, getMessages()
						.pleaseEnter(getMessages().amountDue()), getMessages()
						.amountDue(), false, true) {

			@Override
			protected String getFormalName() {
				return ApplyCreditsRequirement.this.getFormalName();
			}
		};
		amountDue.setEditable(false);
		list.add(amountDue);

		list.add(new ShowListRequirement<ClientCreditsAndPayments>(
				CREDITS_PAYMENTS, getMessages().pleaseSelect(
						getMessages().creditsPayments()), 40) {

			@Override
			protected String onSelection(ClientCreditsAndPayments value) {
				return null;
			}

			@Override
			protected String getShowMessage() {
				return null;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().creditsPayments());
			}

			@Override
			protected Record createRecord(ClientCreditsAndPayments value) {
				Record record = new Record(value);
				record.add(getMessages().creditAmount(),
						value.getCreditAmount());
				record.add(getMessages().balance(), value.getBalance());
				record.add(getMessages().amountToUse(), value.getAmtTouse());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
			}

			@Override
			protected boolean filter(ClientCreditsAndPayments e, String name) {
				return false;
			}

			@Override
			protected List<ClientCreditsAndPayments> getLists(Context context) {
				return ApplyCreditsRequirement.this.getCreditsPayments();
			}
		});

		list.add(new CurrencyAmountRequirement(AMOUNT_TO_USE, getMessages()
				.pleaseEnter(getMessages().amountToUse()), getMessages()
				.amountToUse(), false, true) {

			@Override
			protected String getFormalName() {
				return ApplyCreditsRequirement.this.getFormalName();
			}
		});
	}

	protected abstract String getFormalName();

	protected abstract List<ClientCreditsAndPayments> getCreditsPayments();

	@Override
	protected Result onFinish(Context context) {
		setValue(getRequirement(AMOUNT_TO_USE).getValue());
		return null;
	}

	@Override
	protected String getDisplayValue() {
		return String.valueOf((Double) getValue());
	}

}
