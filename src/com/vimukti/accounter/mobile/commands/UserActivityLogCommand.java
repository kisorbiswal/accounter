package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Activity;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

/**
 * 
 * @author Sai Prasad N
 * 
 */

public class UserActivityLogCommand extends AbstractTransactionCommand {

	private static final String FROM_DATE = "fromdate";
	private static final String END_DATE = "enddate";
	private static final int ACTIVITY_LOG_TO_SHOW = 5;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(FROM_DATE, true, true));
		list.add(new Requirement(END_DATE, true, true));

	}

	@Override
	public Result run(Context context) {

		Result result = null;

		result = creatOptionalResult(context);
		return null;
	}

	private Result creatOptionalResult(Context context) {

		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("values");

		ResultList list = new ResultList("values");

		Result result = fromDateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = endDateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		Date fromDate = get(FROM_DATE).getValue();
		Date endDate = get(END_DATE).getValue();

		result = userActivityLogList(context, fromDate, endDate);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result endDateRequirement(Context context, ResultList list,
			Object selection) {
		Requirement dateReq = get(END_DATE);
		Date endDate = (Date) dateReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(END_DATE)) {
			Date date = context.getSelection(END_DATE);
			if (date == null) {
				date = context.getDate();
			}
			endDate = date;
			dateReq.setValue(endDate);
		}
		if (selection == endDate) {
			context.setAttribute(INPUT_ATTR, END_DATE);
			return date(context, "Enter end Date", endDate);
		}

		return null;
	}

	private Result fromDateRequirement(Context context, ResultList list,
			Object selection) {
		Requirement dateReq = get(FROM_DATE);
		Date fromDate = (Date) dateReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("fromdate")) {
			Date date = context.getSelection(FROM_DATE);
			if (date == null) {
				date = context.getDate();
			}
			fromDate = date;
			dateReq.setValue(fromDate);
		}
		if (selection == fromDate) {
			context.setAttribute(INPUT_ATTR, FROM_DATE);
			return date(context, "Enter From Date", fromDate);
		}

		return null;
	}

	private Result userActivityLogList(Context context, Date fromDate,
			Date endDate) {
		Result result = context.makeResult();
		ResultList activitiesList = new ResultList("activitylog");
		int num = 0;
		List<Activity> activities = getActivityList(context.getSession(),
				fromDate, endDate);
		for (Activity activity : activities) {
			activitiesList.add(createActivityRecord(activity));
			num++;
			if (num == ACTIVITY_LOG_TO_SHOW) {
				break;
			}
		}
		result.add(fromDate.toString());
		result.add(endDate.toString());
		result.add(activitiesList);
		return result;
	}

	private List<Activity> getActivityList(Session session, Date fromDate,
			Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	private Record createActivityRecord(Activity activity) {
		Record record = new Record(activity);
		record.add("ModifiedDate", activity.getTime().toString());
		record.add("UserName", activity.getUserName());
		record.add("Activity", activity.getActivityType());
		record.add("Name", activity.getName());
		record.add("Date", activity.getDate().toString());
		record.add("Amount", activity.getAmount());
		return record;
	}
}
