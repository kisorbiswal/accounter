package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemAccountsRequirement;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
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

		list.add(new TransactionItemAccountsRequirement("accountItem",
				"enter account item name or number", "accountItems", false,
				true) {

			@Override
			protected List<ClientAccount> getLists(Context context) {
				return getClientCompany().getAccounts();
			}
		});

		list.add(new TransactionItemAccountsRequirement("accountItem2",
				"enter account item2 name or number", "accountItems2", false,
				true) {

			@Override
			protected List<ClientAccount> getLists(Context context) {
				return getClientCompany().getAccounts();
			}
		});
		//
		// list.add(new TransactionItemItemsRequirement("Transaction_Items2",
		// "Enter Item2 name or number", "Items2:", false, true, false) {
		//
		// @Override
		// protected ClientCompany getClientCompany() {
		// return MyCommand.this.getClientCompany();
		// }
		//
		// @Override
		// protected List<ClientItem> getLists(Context context,
		// final String name) {
		// return Utility.filteredList(new ListFilter<ClientItem>() {
		//
		// @Override
		// public boolean filter(ClientItem e) {
		// return e.getName().contains(name);
		// }
		// }, getClientCompany().getItems());
		// }
		//
		// @Override
		// protected List<ClientItem> getLists(Context context) {
		// ClientCompany clientCompany2 = MyCommand.this
		// .getClientCompany();
		// return clientCompany2.getAllItems();
		// }
		//
		// });
	}

	@Override
	protected String getDetailsMessage() {
		return "MyCommand is ready to create";
	}

	@Override
	protected void setDefaultValues(Context context) {

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

	@Override
	protected String getWelcomeMessage() {
		// TODO Auto-generated method stub
		return null;
	}
}
