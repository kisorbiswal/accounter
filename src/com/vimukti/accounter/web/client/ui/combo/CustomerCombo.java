package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.QuickAddDialog;
import com.vimukti.accounter.web.client.ui.customers.NewCustomerAction;

public class CustomerCombo extends CustomCombo<ClientCustomer> {

	public CustomerCombo(String title) {
		super(title);
		super
				.setToolTip(Accounter.messages()
						.selectWhichWeHaveInOurCompanyOrAddNew(
								Global.get().Customer()));
		initCombo(getCompany().getCustomers());
	}

	public CustomerCombo(String title, boolean isAddNewRequire) {
		super(title, isAddNewRequire, 1);
		if (isAddNewRequire)
			super.setToolTip(Accounter.messages()
					.selectWhichWeHaveInOurCompanyOrAddNew(
							Global.get().Customer()));
		initCombo(getCompany().getCustomers());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboMessages.newCustomer(Global.get().Customer());
	}

	@Override
	protected String getDisplayName(ClientCustomer object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		NewCustomerAction action = ActionFactory.getNewCustomerAction();
		action.setCallback(new ActionCallback<ClientCustomer>() {

			@Override
			public void actionResult(ClientCustomer result) {
				if (result.getDisplayName() != null && result.isActive())
					addItemThenfireEvent(result);
			}
		});

		action.run(null, true);
	}

	@Override
	protected String getColumnData(ClientCustomer object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

	@Override
	protected void selectionFaildOnClose() {
		QuickAddDialog dialog = new QuickAddDialog("New Customer");
		dialog.setDefaultText(textBox.getText());
		dialog.setListener(new QuickAddListenerImpl(this));
		dialog.show();
	}

	@Override
	protected void onAddAllInfo(String text) {
		NewCustomerAction action = ActionFactory.getNewCustomerAction(text);
		action.setCallback(new ActionCallback<ClientCustomer>() {

			@Override
			public void actionResult(ClientCustomer result) {
				if (result.getDisplayName() != null)
					addItemThenfireEvent(result);
			}
		});
		action.run(null, true);
	}

	@Override
	protected ClientCustomer getQuickAddData(String text) {
		ClientCustomer clientCustomer = new ClientCustomer();
		clientCustomer.setName(text);
		clientCustomer.setBalanceAsOf(new ClientFinanceDate().getDate());
		return clientCustomer;
	}
}
