package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientItemGroup;

public class NewItemGroupCommand extends AbstractTransactionCommand {
	private static final String ITEMGROUP_NAME = "itemGroupName";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(ITEMGROUP_NAME, false, true));
	}

	@Override
	public Result run(Context context) {
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(getConstants().itemGroup()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		Result result = nameRequirement(context, list, ITEMGROUP_NAME,
				getConstants().itemGroup(),
				getMessages().pleaseEnter(getConstants().itemGroup()));
		if (result != null) {
			return result;
		}
		markDone();
		return createItemGroupObject(context);
	}

	private Result createItemGroupObject(Context context) {

		ClientItemGroup group = new ClientItemGroup();
		group.setName(get(ITEMGROUP_NAME).getValue().toString());
		create(group, context);
		markDone();
		Result result = new Result();
		result.add(getMessages().createSuccessfully(getConstants().itemGroup()));

		return result;
	}

}
