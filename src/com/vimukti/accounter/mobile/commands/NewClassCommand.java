package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;

public class NewClassCommand extends AbstractTransactionCommand {
	private static final String CLASS_NAME = "Class";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(CLASS_NAME, false, true));

	}

	@Override
	public Result run(Context context) {
		Result result = createClassNameReq(context);
		if (result == null) {
			// TODO
		}
		return null;
	}

	private Result createClassNameReq(Context context) {

		Requirement requirement = get(CLASS_NAME);
		String className = context.getSelection(TEXT);
		if (!requirement.isDone()) {
			if (className != null) {
				requirement.setValue(className);
			} else {
				return text(context, "Please enter the  Class Name", null);
			}
		}
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(CLASS_NAME)) {
			requirement.setValue(input);
		}
		return null;
	}

}
