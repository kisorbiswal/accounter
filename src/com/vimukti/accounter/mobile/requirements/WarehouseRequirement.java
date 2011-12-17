package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Warehouse;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.utils.HibernateUtil;

public class WarehouseRequirement extends ListRequirement<Warehouse> {

	public WarehouseRequirement(String requirementName, String enterString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<Warehouse> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().noRecordsToShow();
	}

	@Override
	protected Record createRecord(Warehouse value) {
		Record record = new Record(value);
		record.add(getMessages().name(), value.getName());
		return record;
	}

	@Override
	protected String getDisplayValue(Warehouse value) {
		return value.getName();
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getMessages().wareHouse()));

	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getMessages().wareHouse());
	}

	@Override
	protected boolean filter(Warehouse e, String name) {
		return e.getName().equals(name);
	}

	@Override
	protected List<Warehouse> getLists(Context context) {
		return new ArrayList<Warehouse>(context.getCompany().getWarehouses());
	}

	@Override
	protected String getSetMessage() {
		return null;
	}

	@Override
	public void setValue(Object value) {
		Session currentSession = HibernateUtil.getCurrentSession();
		Warehouse warehouse = (Warehouse) value;
		super.setValue(currentSession.load(Warehouse.class, warehouse.getID()));
	}

	@Override
	public void setDefaultValue(Object defaultValue) {
		Session currentSession = HibernateUtil.getCurrentSession();
		Warehouse warehouse = (Warehouse) defaultValue;
		super.setDefaultValue(currentSession.load(Warehouse.class,
				warehouse.getID()));
	}

}
