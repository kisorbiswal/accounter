package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ActionRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;

public class SalesPersonsListCommand extends NewAbstractCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getConstants().salesPersonList();
	}

	@Override
	protected String getDetailsMessage() {
		return getConstants().salesPersonList();
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(VIEW_BY).setDefaultValue(getConstants().active());
	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ActionRequirement(VIEW_BY, null) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getConstants().active());
				list.add(getConstants().inActive());
				return list;
			}
		});

		list.add(new ShowListRequirement<ClientSalesPerson>(getConstants()
				.salesPersonList(), "", 10) {

			@Override
			protected String onSelection(ClientSalesPerson value) {
				return null;
			}

			@Override
			protected String getShowMessage() {
				return getConstants().salesPersonList();
			}

			@Override
			protected String getEmptyString() {
				return "No Sales Persons are available";
			}

			@Override
			protected Record createRecord(ClientSalesPerson salesPrson) {
				Record record = new Record(salesPrson);
				record.add("", salesPrson.getName());
				record.add("", salesPrson.getJobTitle());
				record.add("", salesPrson.getDateOfBirth());
				record.add("", salesPrson.getEmail());
				record.add("", salesPrson.getPhoneNo());
				record.add("", salesPrson.getFaxNo());
				record.add("", salesPrson.getMemo());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("Create Sales Person");
			}

			@Override
			protected boolean filter(ClientSalesPerson e, String name) {
				return e.getName().startsWith(name);
			}

			@Override
			protected List<ClientSalesPerson> getLists(Context context) {

				return getSalesPersons(context);
			}
		});

	}

	protected List<ClientSalesPerson> getSalesPersons(Context context) {
		String isActive = get(VIEW_BY).getValue();
		List<ClientSalesPerson> list = new ArrayList<ClientSalesPerson>();
		List<ClientSalesPerson> salesPersons = context.getClientCompany()
				.getSalesPersons();
		if (salesPersons != null) {
			for (ClientSalesPerson person : salesPersons) {
				if (isActive.equals(getConstants().active())) {
					if (person.isActive()) {
						list.add(person);
					}
				} else {
					if (!person.isActive()) {
						list.add(person);
					}

				}
			}
		}
		return list;
	}

}
