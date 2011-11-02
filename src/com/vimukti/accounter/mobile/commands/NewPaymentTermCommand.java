package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;

public class NewPaymentTermCommand extends NewAbstractCommand {

	private final static String PAYMENT_TERMS = "Payment Terms";
	private final static String DESCRIPTION = "Description";
	private final static String DUE_DAYS = "Due Days";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new StringRequirement(PAYMENT_TERMS, getMessages()
				.pleaseEnter(getConstants().paymentTerm()), getConstants()
				.paymentTerm(), false, true));

		list.add(new StringRequirement(DESCRIPTION, getMessages().pleaseEnter(
				getConstants().paymentTermDescription()), getConstants()
				.paymentTermDescription(), true, true));

		list.add(new NumberRequirement(DUE_DAYS, getMessages().pleaseEnter(
				getConstants().dueDays()), getConstants().dueDays(), true, true));

	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientPaymentTerms paymentTerms = new ClientPaymentTerms();

		String paymnetTermName = get(PAYMENT_TERMS).getValue();
		paymentTerms.setName(paymnetTermName);

		String description = get(DESCRIPTION).getValue();
		paymentTerms.setDescription(description);

		Integer dueDays = Integer.parseInt((String) get(DUE_DAYS).getValue());
		paymentTerms.setDueDays(dueDays);

		create(paymentTerms, context);

		markDone();
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "New Payment term commond is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return "New Payment term commond is ready to create with the following values";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DESCRIPTION).setDefaultValue("");
		get(DUE_DAYS).setDefaultValue("0");
	}

	@Override
	public String getSuccessMessage() {
		return "New payment term commond is created successfully";
	}

}
