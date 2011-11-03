package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.ClientContact;

public abstract class CustomerContactRequirement extends
		AbstractTableRequirement<ClientContact> {
	private static final String CONTACT_NAME = "contactName";
	private static final String TITLE = "title";
	private static final String BUSINESS_PHONE = "businessPhone";
	private static final String EMAIL = "email";
	private static final String IS_PRIMARY = "isPrimary";

	public CustomerContactRequirement(String requirementName,
			String enterString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, true, isOptional,
				isAllowFromContext);
		setDefaultValue(new ArrayList<ClientContact>());
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new StringRequirement(CONTACT_NAME, getMessages().pleaseEnter(
				getConstants().contactName()), CONTACT_NAME, true, true));
		list.add(new StringRequirement(TITLE, getMessages().pleaseEnter(
				getConstants().title()), TITLE, true, true));
		list.add(new NumberRequirement(BUSINESS_PHONE, getMessages()
				.pleaseEnter(getConstants().businessPhone()), BUSINESS_PHONE,
				false, true));

		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getConstants().email()), EMAIL, true, true));

		list.add(new BooleanRequirement(IS_PRIMARY, true) {

			@Override
			protected String getTrueString() {
				return "Primary Contact";
			}

			@Override
			protected String getFalseString() {
				return "Not Primary Contact";
			}
		});
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getConstants().contacts());
	}

	@Override
	protected void getRequirementsValues(ClientContact obj) {

	}

	@Override
	protected void setRequirementsDefaultValues(ClientContact obj) {
		get(CONTACT_NAME).setDefaultValue(obj.getName());
		get(EMAIL).setDefaultValue(obj.getEmail());
		get(IS_PRIMARY).setDefaultValue(obj.isPrimary());
		get(BUSINESS_PHONE).setDefaultValue(obj.getBusinessPhone());
		get(TITLE).setDefaultValue(obj.getTitle());
	}

	@Override
	protected ClientContact getNewObject() {
		return new ClientContact();
	}

	@Override
	protected Record createFullRecord(ClientContact t) {
		Record record = new Record(t);
		record.add("", t.isPrimary());
		record.add("", t.getName());
		record.add("", t.getTitle());
		record.add("", t.getBusinessPhone());
		record.add("", t.getEmail());
		return record;
	}

	protected abstract List<ClientContact> getList();

	@Override
	protected Record createRecord(ClientContact t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getConstants().contacts());
	}
}
