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
	private static final Object LOCATION_PROCESS = null;
	private static final String OLD_LOCATION_ATTR = null;
	private static final String LOCATION_ATTR = null;
	private static final String LOCATION_DETAILS = null;

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

		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = null;
		if (process.equals(LOCATION_PROCESS)) {
			result = locationProcess(context);
			if (result != null) {
				return result;
			}
		}
		result = optionalRequirements(context);

		return result;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */

	private Result optionalRequirements(Context context) {

		context.setAttribute(INPUT_ATTR, "optional");
		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionNames = (ActionNames) selection;
			switch (actionNames) {
			case ADD_MORE_LOCATIONS:
				locationProcess(context);
			case FINISH:
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("locationList");

		if (selection != null) {
			Result result = location(context, (Location) selection);
			if (result != null) {
				return result;
			}
		}

		Result result = getLocationResult(context);

		ResultList actions = new ResultList(ACTIONS);
		Record moreItems = new Record(ActionNames.ADD_MORE_LOCATIONS);
		moreItems.add("", "Add more Locations");
		actions.add(moreItems);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result locationProcess(Context context) {
		Location location = (Location) context.getAttribute(LOCATION_ATTR);
		Result result = location(context, location);
		if (result == null) {
			ActionNames actionName = context.getSelection(ACTIONS);
			if (actionName == ActionNames.DELETE_ITEM) {
				Requirement itemsReq = get("location");
				List<Location> locations = itemsReq.getValue();
				locations.remove(location);
				context.removeAttribute(OLD_LOCATION_ATTR);
			}
		}
		return result;
	}

	/**
	 * Editing the selected location
	 * 
	 * @param context
	 * @param selectionLocation
	 * @return
	 */
	private Result location(Context context, Location selectionLocation) {
		context.setAttribute(PROCESS_ATTR, LOCATION_PROCESS);
		context.setAttribute(OLD_LOCATION_ATTR, selectionLocation);
		String location = (String) context.getAttribute(LOCATION_ATTR);
		if (location != null) {
			context.removeAttribute(LOCATION_ATTR);
			if (location.equals("location")) {
				selectionLocation.setLocationName(context.getString());
			}
		} else {
			Object selection = context.getSelection(LOCATION_DETAILS);
			if (selection != null) {
				if (selection == selectionLocation.getLocationName()) {
					context.setAttribute(LOCATION_ATTR, "location");
					return text(context, "Enter Location Name",
							selectionLocation.getLocationName());
				}
			} else {
				selection = context.getSelection(ACTIONS);
				if (selection == ActionNames.FINISH) {
					context.removeAttribute(PROCESS_ATTR);
					context.removeAttribute(OLD_LOCATION_ATTR);
					return null;
				} else if (selection == ActionNames.DELETE_ITEM) {
					context.removeAttribute(PROCESS_ATTR);
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * processing the location List
	 * 
	 * @param context
	 * @return {@link Result}
	 */
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
		commandList.add("Remove");

		result.add(commandList);

		return result;
	}

	/**
	 * create Location Record
	 * 
	 * @param location
	 * @return {@link Record}
	 */
	private Record createLocationRecord(Location location) {
		Record record = new Record(location);
		record.add("Name", "Location Name");
		record.add("value", location.getLocationName());
		return record;
	}

	/**
	 * getting location List
	 * 
	 * @param context
	 * @return {@link LocationList}
	 */
	private List<Location> getLocationList(Context context) {
		return new ArrayList<Location>(context.getCompany().getLocations());
	}

}
