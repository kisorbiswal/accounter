package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.web.client.core.ClientCustomer;

public class NewCreditNoteCommand extends NewAbstractTransactionCommand {

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new CustomerRequirement(CUSTOMER,
				"Please Enter Customer name or number to set InvoiceCustomer",
				"Customer", false, true, new ChangeListner<ClientCustomer>() {

					@Override
					public void onSelection(ClientCustomer value) {
						NewCreditNoteCommand.this.get(CONTACT).setValue(null);
					}
				}) {

			@Override
			protected List<ClientCustomer> getLists(Context context) {
				return getClientCompany().getCustomers();
			}
		});

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
