package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;

public class NewProductItemCommand extends AbstractItemCreateCommand {

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);
		list.add(new AmountRequirement(WEIGHT, getMessages().pleaseEnter(
				getConstants().weight()), getConstants().weight(), true, true));
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		// TODO Auto-generated method stub
		return null;
	}
}
