package com.vimukti.accounter.web.client.ui.customers;

import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.LocationGroupListDialog;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * Add new Location.
 * 
 * @author Lingarao.R
 * 
 */

public class AddnewLocationDialog extends BaseDialog<ClientLocation> {

	private ValueCallBack<ClientLocation> successCallback;
	private DynamicForm form;
	private TextItem locationName;
	ClientLocation clientLocation;
	LocationGroupListDialog locationGroupListDialog;

	public AddnewLocationDialog(String title) {
		super(title, "");
		this.addStyleName("AddnewLocationDialog");
		createControls();
		center();
	}

	public AddnewLocationDialog(
			LocationGroupListDialog locationGroupListDialog, String title,
			String string, ClientLocation clientLocation2) {
		super(title, "");
		this.addStyleName("add-new-location-dialog");
		this.clientLocation = clientLocation2;
		this.locationGroupListDialog = locationGroupListDialog;
		createControls();
		center();
	}

	private void initGridData() {
		// List<ClientLocation> locationList = getCompany().getLocations();
		// for (ClientLocation clientItem : locationList) {
		// if (clientItem.getLocationName() != null) {
		// locationGroupListDialog.add
		// continue;
		// }
		// }
		// }
	}

	private void createControls() {
		form = new DynamicForm("newLOcationForm");
		locationName = new TextItem(messages.AddLocation(), "locationName");
		locationName.setRequired(true);
		StyledPanel layout = new StyledPanel("layout");
		form.add(locationName);
		if (this.clientLocation != null) {
			locationName.setValue(clientLocation.getName());
		}
		layout.add(form);
		setBodyLayout(layout);
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = form.validate();
		if (locationGroupListDialog != null)
			result.add(locationGroupListDialog.validate());
		return result;
	}

	@Override
	protected boolean onOK() {
		return locationGroupListDialog.onOK();
	}

	public void addSuccessCallback(
			ValueCallBack<ClientLocation> newContactHandler) {
		this.successCallback = newContactHandler;
	}

	public String getLocationGroupName() {
		return this.locationName.getValue().toString();
	}

	public ClientLocation createOrEditLocation() {
		if (clientLocation == null) {
			clientLocation = new ClientLocation();
			this.clientLocation.setLocationName(locationName.getValue()
					.toString());
		} else {
			this.clientLocation.setLocationName(locationName.getValue()
					.toString());
		}

		return this.clientLocation;
	}

	@Override
	public void setFocus() {
		locationName.setFocus();

	}
}
