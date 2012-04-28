package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.LocationCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class LocationMergeDialog extends BaseDialog<ClientLocation>
		implements AsyncCallback<Void> {

	private DynamicForm form;
	private DynamicForm form1;

	private LocationCombo locationFromCombo;
	private LocationCombo locationToCombo;
	private TextItem locationFromIDTextItem;
	private TextItem locationToIDTextItem;

	private ClientLocation clientFromLocation;
	private ClientLocation clientToLocation;

	public LocationMergeDialog(String title, String descript) {
		super(title, descript);
		this.getElement().setId("LocationMergeDialog");
		okbtn.setText(messages.merge());
		createControls();
		center();
		clientToLocation = null;
		clientFromLocation = null;
	}

	private void createControls() {
		form = new DynamicForm("form");
		form1 = new DynamicForm("form1");
		StyledPanel layout = new StyledPanel("layout");
		StyledPanel layout1 = new StyledPanel("layout1");
		StyledPanel horizontalPanel = new StyledPanel("horizontalPanel");
		locationFromCombo = createFromLocationCombo();
		locationToCombo = createToLocationCombo();

		locationFromIDTextItem = new TextItem(
				messages.locationId(), "classIDTextItem");
		locationFromIDTextItem.setEnabled(false);

		locationToIDTextItem = new TextItem(messages.locationId(), "classIDTextItem1");
		locationToIDTextItem.setEnabled(false);

		locationFromCombo.setRequired(true);
		locationToCombo.setRequired(true);
		form.add(locationFromCombo, locationFromIDTextItem);
		form1.add(locationToCombo, locationToIDTextItem);
		layout.add(form);
		layout1.add(form1);
		horizontalPanel.add(layout);
		horizontalPanel.add(layout1);
		setBodyLayout(horizontalPanel);

	}

	private LocationCombo createToLocationCombo() {

		locationToCombo = new LocationCombo(messages.mergeTo(), false);
		locationToCombo.setRequired(true);
		locationToCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientLocation>() {
					@Override
					public void selectedComboBoxItem(
							ClientLocation selectItem) {
						clientToLocation = selectItem;
						classToSelected(selectItem);
					}
				});
		return locationToCombo;
	}

	private LocationCombo createFromLocationCombo() {
		locationFromCombo = new LocationCombo(messages.mergeFrom(), false);
		locationFromCombo.setRequired(true);
		locationFromCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientLocation>() {
					@Override
					public void selectedComboBoxItem(
							ClientLocation selectItem) {
						clientFromLocation = selectItem;
						classFromSelected(selectItem);
					}
				});
		return locationFromCombo;
	}

	private void classFromSelected(ClientLocation selectItem) {
		locationFromIDTextItem.setValue(String.valueOf(selectItem.getID()));
	}

	private void classToSelected(ClientLocation selectItem) {
		locationToIDTextItem.setValue(String.valueOf(selectItem.getID()));
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = form.validate();
		if ( clientFromLocation != null && clientToLocation != null) {
			if (clientFromLocation.getID() == clientToLocation.getID()) {
				result.addError(clientFromLocation,
						messages.notMove(messages.location()));
				return result;
			}
		}

		result.add(form1.validate());

		return result;

	}

	@Override
	protected boolean onOK() {

		if (clientToLocation != null && clientFromLocation != null) {
			if (clientToLocation.getID() == clientFromLocation.getID()) {
				return false;
			}
		}
		Accounter.createHomeService().mergeLocation(clientFromLocation,
				clientToLocation, this);
		com.google.gwt.user.client.History.back();
		return true;
	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Void result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		locationFromCombo.setFocus();
	}

}
