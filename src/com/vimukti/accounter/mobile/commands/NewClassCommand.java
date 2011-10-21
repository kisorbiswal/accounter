package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class NewClassCommand extends AbstractTransactionCommand {
	private static final String CLASS_NAME = "Class";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(CLASS_NAME, false, true));

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

		Result result = nameRequirement(context, list, CLASS_NAME,
				getConstants().className(),
				getMessages().pleaseEnter(getConstants().className()));
		if (result != null) {
			return result;
		}
		markDone();
		return createClassObject(context);
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result createClassObject(Context context) {
		AccounterClass accounterClass = new AccounterClass();
		accounterClass.setclassName((String) get(CLASS_NAME).getValue());
		create(accounterClass, context);
		markDone();
		Result result = new Result();
		result.add(getMessages().createSuccessfully(getConstants().className()));

		return result;
	}

}
