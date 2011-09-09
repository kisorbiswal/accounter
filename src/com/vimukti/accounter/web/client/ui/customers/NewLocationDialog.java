package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.LocationGroupListDialog;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * Add new Location.
 * 
 * @author Lingarao.R
 * 
 */

public class NewLocationDialog extends BaseDialog<ClientLocation> {

	private ValueCallBack<ClientLocation> successCallback;
	private DynamicForm form;
	private TextItem locationName;
	ClientLocation clientLocation;
	LocationGroupListDialog locationGroupListDialog;

	public NewLocationDialog(String title) {
		super(title, "");
		setWidth("400px");
		createControls();
		center();
	}

	public NewLocationDialog(LocationGroupListDialog locationGroupListDialog,
			String title, String string, ClientLocation clientLocation2) {
		super(title, "");
		this.clientLocation = clientLocation2;
		this.locationGroupListDialog = locationGroupListDialog;
		setWidth("400px");
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
		form = new DynamicForm();
		form.setWidth("100%");
		locationName = new TextItem("Add Location");
		locationName.setHelpInformation(true);
		locationName.setRequired(true);
		VerticalPanel layout = new VerticalPanel();
		form.setItems(locationName);
		DynamicForm addDynamicForm = new DynamicForm();
		if (this.clientLocation != null) {
			locationName.setValue(clientLocation.getName());
		}
		CheckBox titleCheckBox = new CheckBox(Accounter.constants()
				.useDifferentTitle());
		CheckBox companyNameCheckBox = new CheckBox(Accounter.constants()
				.useDifferentComapanyName());
		CheckBox addressCheckBox = new CheckBox(Accounter.constants()
				.useDifferentAddrerss());
		CheckBox emailCheckBox = new CheckBox(Accounter.constants()
				.useDifferentEmail());
		CheckBox phoneCheckBox = new CheckBox(Accounter.constants()
				.useDifferentPhoneNumber());
		TextItem titleTextBox = new TextItem(Accounter.constants().title());
		TextItem companyNameTextBox = new TextItem(Accounter.constants().name());
		// AddressDialog addressDialog = new AddressDialog(title, description,
		// textAreaItem, addressType, allAddresses)
		TextItem email = new TextItem(Accounter.constants().email());
		TextItem phone = new TextItem(Accounter.constants().phone());
		addDynamicForm.setItems(titleTextBox, companyNameTextBox, email, phone);
		titleCheckBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// titleTextBox.setVisible(titleCheckBox.getValue());
			}
		});
		companyNameCheckBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// companyNameTextBox.setVisible(companyNameCheckBox.getValue());
			}
		});
		addressCheckBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// companyNameTextBox.setVisible(addressCheckBox.getValue());
			}
		});
		emailCheckBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// email.setVisible(emailCheckBox.getValue());
			}
		});
		phoneCheckBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// phone.setVisible(phoneCheckBox.getValue());
			}
		});
		layout.add(form);
		layout.add(addDynamicForm);
		layout.add(titleCheckBox);
		layout.add(companyNameCheckBox);
		layout.add(addressCheckBox);
		layout.add(emailCheckBox);
		layout.add(phoneCheckBox);
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
		// TODO Auto-generated method stub
		
	}
}
