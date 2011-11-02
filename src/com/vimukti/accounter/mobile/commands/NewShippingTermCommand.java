package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;

public class NewShippingTermCommand extends NewAbstractCommand {

	protected static final String SHIPPING_TERMNAME = "shippingTermsName";
	protected static final String DESCRIPTION = "description";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(SHIPPING_TERMNAME,
				"Enter Shipping Term Name", "Shipping Term", false, true));
		list.add(new NameRequirement(DESCRIPTION, "Enter Description",
				"Description", true, true));
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "Shipping Term Commond is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return " Shppping Term is ready to created with following details.";
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return "New Shipping Term is Created Successfully";
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientShippingTerms newShippingTerm = new ClientShippingTerms();
		newShippingTerm.setName((String) get(SHIPPING_TERMNAME).getValue());
		newShippingTerm.setDescription((String) get(DESCRIPTION).getValue());
		create(newShippingTerm, context);
		markDone();
		return null;
	}
}
