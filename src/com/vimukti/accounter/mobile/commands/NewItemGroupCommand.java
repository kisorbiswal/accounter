package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.ClientItemGroup;

public class NewItemGroupCommand extends NewAbstractCommand {
	private static final String ITEMGROUP_NAME = "itemGroupName";
	private ClientItemGroup itemGroup;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(ITEMGROUP_NAME, getMessages().pleaseEnter(
				getConstants().itemGroup()), getConstants().itemGroup(), false,
				true));
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (string.isEmpty()) {
			if (!isUpdate) {
				itemGroup = new ClientItemGroup();
				return null;
			}
		}

		itemGroup = CommandUtils.getItemGroupByName(string);
		if (itemGroup == null) {
			addFirstMessage(context, "Select an item group to update.");
			return "Item Groups " + string.trim();
		}

		if (itemGroup != null) {
			get(ITEMGROUP_NAME).setValue(itemGroup.getName());
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		if (itemGroup.getID() == 0) {
			return "Create Item Group Command is activated.";
		}
		return "Update Item Group (" + itemGroup.getName()
				+ ") Command is activated.";
	}

	@Override
	protected String getDetailsMessage() {
		if (itemGroup.getID() == 0) {
			return "Item Group is ready to created with following details.";
		} else {
			return "Item Group is ready to updated with following details.";
		}
	}

	@Override
	public Result onCompleteProcess(Context context) {
		String itemGroupName = get(ITEMGROUP_NAME).getValue();
		itemGroup.setName(itemGroupName);
		create(itemGroup, context);
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		if (itemGroup.getID() == 0) {
			return "Item Group is created succesfully.";
		} else {
			return "Item Group is updated successfully.";

		}
	}
}
