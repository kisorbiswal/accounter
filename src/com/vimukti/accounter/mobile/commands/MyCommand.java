package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemItemsRequirement;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientItem;

public class MyCommand extends NewCommand {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement("FirstName", "Enter Fist Name",
				"First Name", false, true));

		// list.add(new NameRequirement("LastName", "Enter Last Name",
		// "Last Name", true, true));

		// list.add(new CustomerRequirement("customer",
		// "enter customer name or number", "Customer", false, true, null) {
		//
		// @Override
		// protected List<ClientCustomer> getLists(Context context) {
		// return getClientCompany().getCustomers();
		// }
		//
		// @Override
		// protected List<ClientCustomer> getLists(Context context,
		// final String name) {
		// return Utility.filteredList(new ListFilter<ClientCustomer>() {
		//
		// @Override
		// public boolean filter(ClientCustomer e) {
		// return e.getName().contains(name)
		// || e.getNumber().equals(name);
		// }
		// }, getClientCompany().getCustomers());
		// }
		// });

		list.add(new TransactionItemItemsRequirement("Transaction_ITEms",
				"Enter Item name or number", "Items:", false, true, false) {

			@Override
			protected ClientCompany getClientCompany() {
				return MyCommand.this.getClientCompany();
			}

			@Override
			protected List<ClientItem> getLists(Context context, String name) {
				return new ArrayList<ClientItem>();
			}

			@Override
			protected List<ClientItem> getLists(Context context) {
				ClientCompany clientCompany2 = MyCommand.this
						.getClientCompany();
				return clientCompany2.getAllItems();
			}

		});
	}

	@Override
	protected String getReadyToCreateMessage() {
		return "MyCommand is ready to create";
	}

	@Override
	protected void setDefaultValues() {

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
