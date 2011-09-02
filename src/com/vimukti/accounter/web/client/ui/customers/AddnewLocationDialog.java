package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.core.ValidationResult;
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

	public AddnewLocationDialog(String title) {
		super(title, "");
		setWidth("400px");
		createControls();
		center();
	}

	private void createControls() {
		form = new DynamicForm();
		form.setWidth("100%");
		locationName = new TextItem("Add Location");
		locationName.setHelpInformation(true);
		locationName.setRequired(true);
		VerticalPanel layout = new VerticalPanel();
		form.setItems(locationName);
		layout.add(form);
		setBodyLayout(layout);
	}

	@Override
	protected ValidationResult validate() {
		return form.validate();
	}

	@Override
	protected boolean onOK() {
		if (successCallback != null) {
			successCallback.execute(createLocation());
		}
		return true;
	}

	/**
	 * creating the ClientLocation
	 * 
	 * @return {@link ClientLocation}
	 */
	private ClientLocation createLocation() {
		ClientLocation location = new ClientLocation();
		location.setLocationName(locationName.getValue());
		return location;
	}

	public void addSuccessCallback(
			ValueCallBack<ClientLocation> newContactHandler) {
		this.successCallback = newContactHandler;
	}
}
