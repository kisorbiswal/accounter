package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.QuickAddDialog;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorAction;

public class VendorCombo extends CustomCombo<ClientVendor> {

	public VendorCombo(String title) {
		super(title);
		initCombo(getCompany().getActiveVendors());
	}

	public VendorCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1);
		initCombo(getCompany().getActiveVendors());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return Global.get().Vendor();

	}

	@Override
	protected String getDisplayName(ClientVendor object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		NewVendorAction action = ActionFactory.getNewVendorAction();
		action.setCallback(new ActionCallback<ClientVendor>() {

			@Override
			public void actionResult(ClientVendor result) {
				if (result.getName() != null && result.isActive())
					addItemThenfireEvent(result);

			}
		});

		action.run(null, true);
	}

	@Override
	protected String getColumnData(ClientVendor object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

	@Override
	protected void selectionFaildOnClose() {
		final QuickAddDialog dialog = new QuickAddDialog(
				messages.newPayee(Global.get().vendor())) {

		};
		dialog.setDefaultText(textBox.getText());
		dialog.setListener(new QuickAddListenerImpl(this) {
			@Override
			public IAccounterCore getData(String text) {
				if (isVenodrExists(text)) {
					return null;
				}
				return super.getData(text);
			}
		});
		dialog.show();
	}

	protected boolean isVenodrExists(String text) {
		for (ClientPayee customer : getCompany().getPayees()) {
			if (customer.getName().equals(text)) {
				if (customer instanceof ClientVendor) {
					this.setComboItem(null);
					this.setComboItem((ClientVendor) customer);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected void onAddAllInfo(String text) {
		NewVendorAction action = ActionFactory.getNewVendorAction(text);
		action.setCallback(new ActionCallback<ClientVendor>() {

			@Override
			public void actionResult(ClientVendor result) {
				if (result.getDisplayName() != null)
					addItemThenfireEvent(result);
			}
		});
		action.run(null, true);
	}

	@Override
	protected ClientVendor getQuickAddData(String text) {
		ClientVendor clientVendor = new ClientVendor();
		clientVendor.setName(text);
		clientVendor.setCurrency(getCompany().getPrimaryCurrency().getID());
		clientVendor.setBalanceAsOf(new ClientFinanceDate().getDate());
		return clientVendor;
	}
}
