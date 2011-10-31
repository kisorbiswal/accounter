package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;

public abstract class VendorGroupRequirement extends
		ListRequirement<ClientVendorGroup> {

	public VendorGroupRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientVendorGroup> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(
				getMessages().vendorGroup(Global.get().Vendor()));
	}

	@Override
	protected Record createRecord(ClientVendorGroup value) {
		Record record = new Record(value);
		record.add("value", value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(ClientVendorGroup value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(
				getMessages().vendorGroup(Global.get().Vendor())));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(
				getMessages().vendorGroup(Global.get().Vendor()));
	}
}
