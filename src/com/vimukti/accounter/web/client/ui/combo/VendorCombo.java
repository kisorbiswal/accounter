package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.QuickAddDialog;
import com.vimukti.accounter.web.client.ui.core.QuickAddDialog.QuickAddListener;
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
		return comboMessages.newSupplier(Global.get().Vendor());

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
				if (result.getName() != null)
					addItemThenfireEvent(result);

			}
		});

		action.run(null, true);
	}

	@Override
	protected String getColumnData(ClientVendor object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

	@Override
	protected void selectionFaildOnClose() {
		QuickAddDialog dialog = new QuickAddDialog(Accounter.messages()
				.newVendor(Global.get().vendor()));
		dialog.setDefaultText(textBox.getText());
		dialog.setListener(new QuickAddListenerImpl());
		dialog.show();
	}

	private class QuickAddListenerImpl implements
			QuickAddListener<ClientVendor> {

		@Override
		public void onAddAllInfo(String text) {
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
		public void saveFailed(AccounterException exception) {
			Accounter.showError(exception.getMessage());
		}

		@Override
		public void saveSuccess(IAccounterCore object) {
			if (object instanceof ClientVendor) {
				addItemThenfireEvent((ClientVendor) object);
			}
		}

		@Override
		public ClientVendor getData(String text) {
			ClientVendor clientVendor = new ClientVendor();
			clientVendor.setName(text);
			clientVendor.setBalanceAsOf(new ClientFinanceDate().getDate());
			return clientVendor;
		}

		@Override
		public void onCancel() {
			changeValue(-1);
		}

	}
}
