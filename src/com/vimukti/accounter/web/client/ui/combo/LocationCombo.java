package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.ui.LocationGroupListDialog;

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
	protected String getColumnData(ClientLocation object, int row, int col) {
		switch (col) {
		case 0:
			return object.getLocationName();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return "  <<---- Add New ---->>  ";
	}

	@Override
	public void onAddNew() {
		LocationGroupListDialog itemGroupDialog = new LocationGroupListDialog("", "");
		itemGroupDialog.removeFromParent();
		itemGroupDialog.showAddEditGroupDialog(null);
		itemGroupDialog.addCallBack(createAddNewCallBack());
	}

	/**
	 * @param valueCallBack
	 */

	public void addNewLocationHandler(
			ValueCallBack<ClientLocation> newLocationtHandler) {
		this.newLocationtHandler = newLocationtHandler;
	}

}
