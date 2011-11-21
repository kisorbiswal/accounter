package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;

public abstract class ContactRequirement extends ListRequirement<Contact> {

	public ContactRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Contact> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	public boolean isDone() {
		if (!super.isDone()) {
			List<Contact> lists = getLists(null);
			return lists.size() == 0;
		}
		return true;
	}

	@Override
	protected Record createRecord(Contact value) {
		Record record = new Record(value);
		record.add("", value.getName());
		return record;
	}

	@Override
	protected String getSetMessage() {
		return getMessages().hasSelected(getMessages().contact());
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().contact());
	}

	@Override
	protected String getDisplayValue(Contact value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getMessages().contact()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().selectFor(getMessages().contact(),
				getContactHolderName());
	}

	protected abstract String getContactHolderName();

	@Override
	protected boolean filter(Contact e, String name) {
		return e.getName().contains(name);
	}
}
