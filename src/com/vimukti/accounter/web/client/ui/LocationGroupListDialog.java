package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.customers.NewLocationDialog;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

public class LocationGroupListDialog extends GroupDialog<ClientLocation> {

	ClientLocation clientLocation;
	private GroupDialogButtonsHandler dialogButtonsHandler;
	List<ClientLocation> clientLocationGroups;
	private NewLocationDialog locationGroupDg;

	public LocationGroupListDialog(String title, String descript) {
		super(title, descript);
		// setWidth("400px");
		initialise();
		center();
	}

	private void initialise() {
		getGrid().setType(AccounterCoreType.LOCATION);
		getGrid().addRecordClickHandler(
				new GridRecordClickHandler<ClientLocation>() {

					@Override
					public boolean onRecordClick(ClientLocation core, int column) {
						ClientLocation clientLocation = core;
						if (clientLocation != null)
							enableEditRemoveButtons(true);
						else
							enableEditRemoveButtons(false);

						return false;
					}

				});

		dialogButtonsHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {

			}

			public void onFirstButtonClick() {
				showAddEditGroupDialog(null);
			}

			public void onSecondButtonClick() {
				showAddEditGroupDialog((ClientLocation) listGridView
						.getSelection());

			}

			public void onThirdButtonClick() {
				deleteObject(getSelectedLocationGroup());
				if (clientLocationGroups == null)
					enableEditRemoveButtons(false);
			}

			public ClientLocation getSelectedLocationGroup() {
				return (ClientLocation) listGridView.getSelection();
			}
		};
		addGroupButtonsHandler(dialogButtonsHandler);
		this.okbtn.setVisible(false);

	}

	public long getSelectedLocationId() {
		return ((ClientLocation) listGridView.getSelection()).getID();
	}

	public void showAddEditGroupDialog(ClientLocation rec) {
		clientLocation = rec;
		locationGroupDg = new NewLocationDialog(this,
				messages.locationTracking(Global.get().Location()), "",
				clientLocation);
		locationGroupDg.show();
	}

	@Override
	public String[] setColumns() {
		return new String[] { messages.locationName(Global.get().Location()), };
	}

	@Override
	public String getHeaderStyle(int index) {
		return "locationName";
	}

	@Override
	public String getRowElementsStyle(int index) {
		return "locationNameValue";
	}

	@Override
	protected List<ClientLocation> getRecords() {
		return (List<ClientLocation>) getCompany().getLocations();
	}

	@Override
	public boolean onOK() {
		if (locationGroupDg != null) {
			if (clientLocation != null) {
				editLocationGroups();
				locationGroupDg = null;
			} else {
				createLocationSGroups();
				locationGroupDg = null;
			}

		}
		return true;
	}

	@Override
	public Object getGridColumnValue(ClientLocation obj, int index) {
		ClientLocation clientLocation = obj;
		if (clientLocation != null) {
			switch (index) {
			case 0:
				return clientLocation.getLocationName();
			}
		}
		return null;
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		String name = locationGroupDg.getLocationGroupName().trim();
		if (name.isEmpty())
			result.addError(this, messages.pleaseEnterValidLocationName(Global
					.get().Location()));
		if (clientLocation != null) {
			String locationGroupName = clientLocation.getName();
			ClientLocation groupByName = company.getLocationByName(name);
			if (!(locationGroupName.equalsIgnoreCase(name) ? true
					: groupByName == null)) {
				result.addError(this, messages.alreadyExist());
			}

		} else {
			ClientLocation locationGroupByName = company
					.getLocationByName(name);
			if (locationGroupByName != null) {
				result.addError(this,
						messages.aLocationAlreadyExistswiththisname());
			}
		}

		return result;
	}

	protected void createLocationSGroups() {
		saveOrUpdate(locationGroupDg.createOrEditLocation());
	}

	protected void editLocationGroups() {
		saveOrUpdate(locationGroupDg.createOrEditLocation());
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
