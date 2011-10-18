package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Location;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;

public class NewLocationCommond extends AbstractCommand {

	private static final String LOCATION_NAME = "location";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(LOCATION_NAME, false, true));

	}

	@Override
	public Result run(Context context) {

		Result result = createClassNameReq(context);
		if (result != null) {
			return result;
		}
		createLocationObject(context);
		markDone();
		return result;
	}

	private void createLocationObject(Context context) {
		Location location = new Location();
		location.setLocationName((String) get(LOCATION_NAME).getValue());
		create(location, context);
	}

	private Result createClassNameReq(Context context) {

		Requirement requirement = get(LOCATION_NAME);
		String className = context.getSelection(TEXT);
		if (!requirement.isDone()) {
			if (className != null) {
				requirement.setValue(className);
			} else {
				return text(context, "Please enter the  Location", null);
			}
		}
		String input = (String) context.getAttribute(INPUT_ATTR);
		if (input.equals(LOCATION_NAME)) {
			requirement.setValue(input);
		}
		return null;
	}

}
