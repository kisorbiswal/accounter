package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Location;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class LocationListCommand extends AbstractTransactionCommand {

	private static final int RECORDS_TO_SHOW = 5;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

	}

	@Override
	public Result run(Context context) {

		Result result = optionalRequirements(context);
		return result;
	}

	private Result optionalRequirements(Context context) {

		context.setAttribute(INPUT_ATTR, "optional");
		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionNames = (ActionNames) selection;
			switch (actionNames) {
			case FINISH:
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("locationList");
		if (selection != null) {

		}
		Result result = getLocationResult(context);

		return result;
	}

	private Result getLocationResult(Context context) {
		ResultList locResultList = new ResultList("locationList");

		Result result = context.makeResult();
		List<Location> locationList = getLocationList(context);
		int record = 0;
		for (Location location : locationList) {
			locResultList.add(createLocationRecord(location));
			record++;
			if (record == RECORDS_TO_SHOW) {
				break;
			}
		}

		result.add(locResultList);

		CommandList commandList = new CommandList();
		commandList.add("Create");

		// TODO for edit and delete
		// commandList.add("Edit");
		// commandList.add("Remove");

		result.add(commandList);

		return result;
	}

	private Record createLocationRecord(Location location) {
		Record record = new Record(location);
		record.add("Name", "Location Name");
		record.add("value", location.getName());
		return record;
	}

	private List<Location> getLocationList(Context context) {
		return new ArrayList<Location>(context.getCompany().getLocations());
	}

}
