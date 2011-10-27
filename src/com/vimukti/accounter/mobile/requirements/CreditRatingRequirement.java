package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientCreditRating;

public abstract class CreditRatingRequirement extends
		ListRequirement<ClientCreditRating> {

	public CreditRatingRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientCreditRating> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getConstants().creditRating());
	}

	@Override
	protected Record createRecord(ClientCreditRating value) {
		Record record = new Record(value);
		record.add("Name", value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientCreditRating value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getConstants().creditRating()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().creditRating());
	}

}
