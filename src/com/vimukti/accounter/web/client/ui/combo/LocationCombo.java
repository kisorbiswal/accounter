package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientLocation;
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
		super(title, true, 1);
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
		return "--- Add New ---";
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
		QuickAddDialog dialog = new QuickAddDialog("New "
				+ Global.get().Location());
		dialog.setDefaultText(textBox.getText());
		dialog.setListener(new QuickAddListenerImpl(this));
		dialog.show();
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
