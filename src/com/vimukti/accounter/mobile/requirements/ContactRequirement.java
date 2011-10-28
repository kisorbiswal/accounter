package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientContact;

public abstract class ContactRequirement extends ListRequirement<ClientContact> {

	public ContactRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientContact> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	public boolean isDone() {
		if (!super.isDone()) {
			List<ClientContact> lists = getLists(null);
			return lists.size() == 0;
		}
		return true;
	}

	@Override
	protected Record createRecord(ClientContact value) {
		Record record = new Record(value);
		record.add("", value.getName());
		return record;
	}

	@Override
	protected String getSetMessage() {
		return "Contact is selected";
	}

	@Override
	protected String getEmptyString() {
		return "There are no Contacts";
	}

	@Override
	protected String getDisplayValue(ClientContact value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getConstants().contact()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().contact()) + " for "
				+ getContactHolderName();
	}

	protected abstract String getContactHolderName();

	@Override
	protected boolean filter(ClientContact e, String name) {
		return e.getName().contains(name);
	}
}
