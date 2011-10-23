package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;

public class MyCommand extends NewCommand {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement("FirstName", "Enter Fist Name",
				"First Name", false, true));

		list.add(new NameRequirement("LastName", "Enter Last Name",
				"Last Name", true, true));

		list.add(new CustomerRequirement("customer",
				"enter customer name or number", "Customer", false, true, null) {

			@Override
			protected List<ClientCustomer> getLists(Context context) {
				return getClientCompany().getCustomers();
			}

			@Override
			protected List<ClientCustomer> getLists(Context context,
					final String name) {
				return Utility.filteredList(new ListFilter<ClientCustomer>() {

					@Override
					public boolean filter(ClientCustomer e) {
						return e.getName().contains(name)
								|| e.getNumber().equals(name);
					}
				}, getClientCompany().getCustomers());
			}
		});
	}

	@Override
	protected String getReadyToCreateMessage() {
		return "Command is ready to create";
	}

	@Override
	protected void setDefaultValues() {
		get("LastName").setDefaultValue("");
		get("LastName").setDefaultValue("");
		get("customer").setDefaultValue(null);
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSuccessMessage() {
		return "Success Command";
	}
}
