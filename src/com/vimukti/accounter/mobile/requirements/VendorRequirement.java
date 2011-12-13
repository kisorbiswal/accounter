package com.vimukti.accounter.mobile.requirements;

import org.hibernate.Session;

import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;

public abstract class VendorRequirement extends ListRequirement<Vendor> {

	public VendorRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Vendor> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	public void setValue(Object value) {
		Session currentSession = HibernateUtil.getCurrentSession();
		Vendor vendor = (Vendor) value;
		super.setValue(currentSession.load(Vendor.class, vendor.getID()));
	}

	@Override
	protected Record createRecord(Vendor value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		record.add(getMessages().balance(), value.getBalance());
		return record;
	}

	@Override
	protected String getDisplayValue(Vendor value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(Global.get().Vendor()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(Global.get().Vendor());
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(Global.get().Vendor());
	}

}
