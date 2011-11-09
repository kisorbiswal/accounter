package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.web.client.core.ClientUserInfo;

public class UsersCommand extends NewAbstractCommand {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new ShowListRequirement<ClientUserInfo>("UsersList",
				"Please Select User", 5) {

			@Override
			protected String onSelection(ClientUserInfo value) {
				return null;
			}

			@Override
			protected String getShowMessage() {
				return getConstants().users();
			}

			@Override
			protected String getEmptyString() {
				return getConstants().noRecordsToShow();
			}

			@Override
			protected Record createRecord(ClientUserInfo value) {
				Record record = new Record(value);
				record.add("", value.getEmail());
				record.add("", value.getUserRole());
				record.add("", value.getFirstName());
				record.add("", value.getLastName());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("New User");
			}

			@Override
			protected boolean filter(ClientUserInfo e, String name) {
				return e.getFirstName().equalsIgnoreCase(name)
						|| e.getLastName().equalsIgnoreCase(name);
			}

			@Override
			protected List<ClientUserInfo> getLists(Context context) {
				return getUsersList(context);
			}
		});
	}

	private List<ClientUserInfo> getUsersList(Context context) {
		return context.getClientCompany().getUsersList();

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

}
