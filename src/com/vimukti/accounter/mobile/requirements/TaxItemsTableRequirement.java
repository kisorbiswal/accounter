package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.ClientTAXItem;

public abstract class TaxItemsTableRequirement extends
		AbstractTableRequirement<ClientTAXItem> {

	private static final String NAME = "name";
	private static final String TAX_RATE = "taxRate";

	public TaxItemsTableRequirement(String requirementName, String enterString,
			String recordName, boolean isCreatable, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isCreatable,
				isOptional, isAllowFromContext);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		NameRequirement name = new NameRequirement(NAME, "", getConstants()
				.name(), true, true);
		name.setEditable(false);
		list.add(name);

		AmountRequirement taxRate = new AmountRequirement(TAX_RATE, "",
				getConstants().taxRate(), true, true);
		taxRate.setEditable(false);
		list.add(taxRate);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getConstants().taxItem());
	}

	@Override
	protected void getRequirementsValues(ClientTAXItem obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setRequirementsDefaultValues(ClientTAXItem obj) {
		get(NAME).setDefaultValue(obj.getName());
		get(TAX_RATE).setDefaultValue(obj.getTaxRate());
	}

	@Override
	protected ClientTAXItem getNewObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Record createFullRecord(ClientTAXItem t) {
		Record record = new Record(t);
		record.add("", getConstants().name());
		record.add("", t.getName());
		record.add("", getConstants().taxRate());
		record.add("", t.getTaxRate());
		return record;
	}

	@Override
	protected Record createRecord(ClientTAXItem t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getConstants().taxItem());
	}

}
