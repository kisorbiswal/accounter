package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ActionRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;

public class SalesPersonsListCommand extends NewAbstractCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
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
		get(VIEW_BY).setDefaultValue(getMessages().active());
	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ActionRequirement(VIEW_BY, null) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().active());
				list.add(getMessages().inActive());
				return list;
			}
		});

		list.add(new ShowListRequirement<SalesPerson>(getMessages()
				.salesPersonList(), "", 10) {

			@Override
			protected String onSelection(SalesPerson value) {
				return "Update Sales Person " + value.getFirstName();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().salesPersonList();
			}

			@Override
			protected String getEmptyString() {
				return "No Sales Persons are available";
			}

			@Override
			protected Record createRecord(SalesPerson salesPrson) {
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
			protected boolean filter(SalesPerson e, String name) {
				return e.getFirstName().startsWith(name);
			}

			@Override
			protected List<SalesPerson> getLists(Context context) {

				return getSalesPersons(context);
			}
		});

	}

	protected List<SalesPerson> getSalesPersons(Context context) {
		String isActive = get(VIEW_BY).getValue();
		List<SalesPerson> list = new ArrayList<SalesPerson>();
		Set<SalesPerson> salesPersons = context.getCompany().getSalesPersons();
		if (salesPersons != null) {
			for (SalesPerson person : salesPersons) {
				if (isActive.equals(getMessages().active())) {
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
