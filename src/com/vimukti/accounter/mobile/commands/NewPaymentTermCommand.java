package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;

public class NewPaymentTermCommand extends NewAbstractCommand {

	private final static String PAYMENT_TERMS = "Payment Terms";
	private final static String DESCRIPTION = "Description";
	private final static String DUE_DAYS = "Due Days";

	private ClientPaymentTerms paymentTerms;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new StringRequirement(PAYMENT_TERMS, getMessages()
				.pleaseEnter(getMessages().paymentTerm()), getMessages()
				.paymentTerm(), false, true));

		list.add(new StringRequirement(DESCRIPTION, getMessages().pleaseEnter(
				getMessages().paymentTermDescription()), getMessages()
				.paymentTermDescription(), true, true));

		list.add(new NumberRequirement(DUE_DAYS, getMessages().pleaseEnter(
				getMessages().dueDays()), getMessages().dueDays(), true, true));

	}

	@Override
	protected Result onCompleteProcess(Context context) {
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
	protected String getDeleteCommand(Context context) {
		return paymentTerms != null ? "Delete PaymentTerm "
				+ paymentTerms.getID() : null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, "Select a Payment Term to update.");
				return "Payment Terms List";
			}
			ClientPaymentTerms paymentTermsByName = CommandUtils
					.getPaymentTermByName(context.getCompany(), string);
			if (paymentTermsByName == null) {
				addFirstMessage(context, "Select a Payment Term to update.");
				return "Payment Terms List " + string.trim();
			}
			paymentTerms = paymentTermsByName;
			get(PAYMENT_TERMS).setValue(paymentTermsByName.getName());
			get(DUE_DAYS).setValue(paymentTermsByName.getDueDays());
			get(DESCRIPTION).setValue(paymentTermsByName.getDescription());
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(PAYMENT_TERMS).setValue(string);
			}
			paymentTerms = new ClientPaymentTerms();
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		if (paymentTerms.getID() == 0) {
			return "New Payment term command is activated";
		} else {
			return "Update payment term command activated";
		}
	}

	@Override
	protected String getDetailsMessage() {
		if (paymentTerms.getID() == 0) {
			return "New Payment term command is ready to create with the following values";
		} else {
			return "Payment term is ready update with following values";
		}
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DESCRIPTION).setDefaultValue("");
		get(DUE_DAYS).setDefaultValue("0");
	}

	@Override
	public String getSuccessMessage() {
		if (paymentTerms.getID() == 0) {
			return "New payment term is created successfully";
		} else {
			return "Payment term updated successfully";
		}
	}

}
