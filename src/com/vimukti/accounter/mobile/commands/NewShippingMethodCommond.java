package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;

public class NewShippingMethodCommond extends NewAbstractCommand {

	private static final String SHIPPING_METHOD_NAME = "Shipping Mehtod";
	private static final String DESCRIPTION = "description";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(SHIPPING_METHOD_NAME,
				"Enter Shipping Method Name", "Shipping Method", false, true));
		list.add(new NameRequirement(DESCRIPTION, "Enter Description",
				"Description", true, true));
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "Shipping Method Commond is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return " Shppping Method is ready to created with following details.";
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return "New Shipping Method is Created Successfully";
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientShippingMethod newShippingTerm = new ClientShippingMethod();
		newShippingTerm.setName((String) get(SHIPPING_METHOD_NAME).getValue());
		newShippingTerm.setDescription((String) get(DESCRIPTION).getValue());
		create(newShippingTerm, context);
		markDone();
		return null;
	}

}
