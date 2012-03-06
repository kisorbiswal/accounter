package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.LocationGroupListDialog;
import com.vimukti.accounter.web.client.ui.core.QuickAddDialog;

/**
 * 
 * @author Lingarao.r
 * 
 */
public class LocationCombo extends CustomCombo<ClientLocation> {
	private ValueCallBack<ClientLocation> newLocationtHandler;

	public LocationCombo(String title) {
		super(title, true, 1,"LocationCombo");
		initCombo(getCompany().getLocations());
	}

	@Override
	protected String getDisplayName(ClientLocation object) {
		if (object != null)
			return object.getLocationName() != null ? object.getLocationName()
					: "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientLocation object, int col) {
		switch (col) {
		case 0:
			return object.getLocationName();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return Global.get().Location();
	}

	@Override
	public void onAddNew() {
		LocationGroupListDialog itemGroupDialog = new LocationGroupListDialog(
				"", "");
		itemGroupDialog.removeFromParent();
		itemGroupDialog
				.addCallBack(new AccounterAsyncCallback<ClientLocation>() {

					@Override
					public void onResultSuccess(ClientLocation result) {
						addItemThenfireEvent(result);
					}

					@Override
					public void onException(AccounterException exception) {
						exception.printStackTrace();
						Accounter.showError(exception.getMessage());
					}
				});
		itemGroupDialog.showAddEditGroupDialog(null);
	}

	/**
	 * @param valueCallBack
	 */

	public void addNewLocationHandler(
			ValueCallBack<ClientLocation> newLocationtHandler) {
		this.newLocationtHandler = newLocationtHandler;
	}

	@Override
	protected void selectionFaildOnClose() {
		final QuickAddDialog dialog = new QuickAddDialog("New "
				+ Global.get().Location());
		dialog.setDefaultText(textBox.getText());
		dialog.setListener(new QuickAddListenerImpl(this) {
			@Override
			public IAccounterCore getData(String text) {
				if (!isLocationExists(text, dialog)) {
					return super.getData(text);
				}
				return null;
			}
		});
		dialog.show();
	}

	protected boolean isLocationExists(String text, QuickAddDialog dialog) {
		for (ClientLocation location : getCompany().getLocations()) {
			if (location.getName().equals(text)) {
				if (location instanceof ClientLocation) {
					this.setComboItem((ClientLocation) location);
					dialog.hide();
				}
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onAddAllInfo(String text) {
		LocationGroupListDialog itemGroupDialog = new LocationGroupListDialog(
				"", "");
		itemGroupDialog.removeFromParent();
		itemGroupDialog
				.addCallBack(new AccounterAsyncCallback<ClientLocation>() {

					@Override
					public void onResultSuccess(ClientLocation result) {
						addItemThenfireEvent(result);
					}

					@Override
					public void onException(AccounterException exception) {
						exception.printStackTrace();
						Accounter.showError(exception.getMessage());
					}
				});
		ClientLocation clientLocation = new ClientLocation();
		clientLocation.setLocationName(textBox.getText());
		itemGroupDialog.showAddEditGroupDialog(clientLocation);
	}

	@Override
	protected ClientLocation getQuickAddData(String text) {
		ClientLocation clientLocation = new ClientLocation();
		clientLocation.setLocationName(textBox.getText());
		return clientLocation;
	}

}
