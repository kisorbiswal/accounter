package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;

public class NewShippingMethodCommand extends NewAbstractCommand {

	private static final String SHIPPING_METHOD_NAME = "Shipping Mehtod";
	private static final String DESCRIPTION = "description";
	private ClientShippingMethod shippingMethod;

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
	protected String getDeleteCommand(Context context) {
		return shippingMethod != null ? "Delete Shipping Method "
				+ shippingMethod.getID() : null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, "Select a Shipping Method to update.");
				return "Shipping Methods";
			}

			shippingMethod = (ClientShippingMethod) CommandUtils
					.getClientObjectById(Long.parseLong(string),
							AccounterCoreType.SHIPPING_METHOD, getCompanyId());

			if (shippingMethod == null) {
				addFirstMessage(context, "Select a Shipping Method to update.");
				return "Shipping Methods " + string.trim();
			}
			get(SHIPPING_METHOD_NAME).setValue(shippingMethod.getName());
			get(DESCRIPTION).setValue(shippingMethod.getDescription());
		} else {
			String string = context.getString();
			if (!string.isEmpty()) {
				get(SHIPPING_METHOD_NAME).setValue(string);
			}
			shippingMethod = new ClientShippingMethod();
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		if (shippingMethod.getID() == 0) {
			return "Shipping Method Commond is activated";
		} else {
			return "Update shipping method command activated";
		}
	}

	@Override
	protected String getDetailsMessage() {
		if (shippingMethod.getID() == 0) {
			return " Shipping Method is ready to create with following details.";
		} else {
			return "Shipping method is ready to update with following details";
		}
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		if (shippingMethod.getID() == 0) {
			return "Shipping Method is Created Successfully";
		} else {
			return "Shipping method updated successdfully";
		}
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		shippingMethod.setName((String) get(SHIPPING_METHOD_NAME).getValue());
		shippingMethod.setDescription((String) get(DESCRIPTION).getValue());
		create(shippingMethod, context);
		markDone();
		return null;
	}

}
