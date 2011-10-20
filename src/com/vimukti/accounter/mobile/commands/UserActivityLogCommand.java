package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Activity;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

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

		setDefaultValules();
		result = creatOptionalResult(context);
		if (result != null)
			return result;
		return result;
	}

	private void setDefaultValules() {
		get(FROM_DATE).setDefaultValue(
				new ClientFinanceDate(System.currentTimeMillis()));
		get(END_DATE).setDefaultValue(
				new ClientFinanceDate(System.currentTimeMillis()));

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

		Result result = dateOptionalRequirement(context, list, FROM_DATE,
				getConstants().fromDate(),
				getMessages().pleaseEnter(getConstants().fromDate()), selection);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, list, END_DATE,
				getConstants().endDate(),
				getMessages().pleaseEnter(getConstants().endDate()), selection);
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

	private Result userActivityLogList(Context context, Date fromDate,
			Date endDate) {
		Result result = context.makeResult();
		ResultList activitiesList = new ResultList("activitylog");
		int num = 0;
		List<Activity> activities = getActivityList(fromDate, endDate);
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

	private List<Activity> getActivityList(Date fromDate, Date endDate) {
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
