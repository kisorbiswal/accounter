package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.server.FinanceTool;

public class UserActivityLogCommand extends NewAbstractCommand {

//	private static final String FROM_DATE = "fromdate";
//	private static final String END_DATE = "enddate";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new DateRequirement(FROM_DATE, getMessages().pleaseEnter(
				getConstants().fromDate()), getConstants().fromDate(), true,
				true));

		list.add(new DateRequirement(TO_DATE, getMessages().pleaseEnter(
				getConstants().toDate()), getConstants().toDate(), true, true));

		list.add(new ShowListRequirement<ClientActivity>("activitylog", "", 5) {

			@Override
			protected List<ClientActivity> getLists(Context context) {
				return getActivityList(context);
			}

			@Override
			protected String onSelection(ClientActivity value) {
				return null;
			}

			protected Record createRecord(ClientActivity value) {
				Record record = new Record(value);
				record.add("",
						new ClientFinanceDate(value.getTime()));
				record.add("", value.getUserName());
				record.add("", getActivityDataType(value));
				return record;
			}

			@Override
			protected boolean filter(ClientActivity e, String name) {
				return e.getName().startsWith(name);
			}

			@Override
			protected String getShowMessage() {
				return getConstants().usersActivityLogTitle();
			}

			@Override
			protected String getEmptyString() {
				return getConstants().noRecordsToShow();
			}

			@Override
			protected void setCreateCommand(CommandList list) {
			}
		});
	}

	private List<ClientActivity> getActivityList(Context context) {

		ClientFinanceDate startDate = get(FROM_DATE).getValue();
		ClientFinanceDate endDate = get(TO_DATE).getValue();

		List<ClientActivity> activities = new FinanceTool().getUserManager()
				.getUsersActivityLog(startDate, endDate, 1, VALUES_TO_SHOW,
						context.getCompany().getId());

		return activities;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getConstants().usersActivityLogTitle();
	}

	@Override
	protected String getDetailsMessage() {
		return getConstants().usersActivityLogTitle();
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(FROM_DATE).setDefaultValue(new ClientFinanceDate());
		get(TO_DATE).setDefaultValue(new ClientFinanceDate());
	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

	protected String getActivityDataType(ClientActivity activity) {
		String dataType = "";
		StringBuffer buffer = new StringBuffer();
		int type = activity.getActivityType();
		switch (type) {
		case 0:
			return dataType = getConstants().loggedIn();
		case 1:
			return dataType = getConstants().loggedOut();
		case 2:
			buffer.append(getConstants().added());
			buffer.append(" : ");
			buffer.append(activity.getDataType() != null ? activity
					.getDataType() : "");
			return buffer.toString();
		case 3:
			buffer.append(getConstants().edited());
			buffer.append(" : ");
			buffer.append(activity.getDataType() != null ? activity
					.getDataType() : "");
			return buffer.toString();
		case 4:
			buffer.append(getConstants().deleted());
			buffer.append(" : ");
			buffer.append(activity.getDataType() != null ? activity
					.getDataType() : "");
			return buffer.toString();
		case 5:
			return dataType = getConstants().updatedPreferences();
		default:
			break;
		}
		return null;
	}
}
