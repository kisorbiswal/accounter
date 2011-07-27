package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class WareHouseView extends BaseView<ClientWarehouse> {

	private DynamicForm leftSideForm, rightSideForm;
	private TextItem wareHouseNameItem, contactNameItem, contactNumberItem,
			mobileNumberItem, DDINumberItem, addressItem, streetItem, cityItem,
			stateItem, countryItem, postalCodeItem;
	private VerticalPanel vPanel;
	private CheckboxItem defaultWareHouse;
	private HorizontalPanel leftRightPanel;
	private ClientContact contact;
	private ClientWarehouse takenWarehouse;
	private ClientCompany company = getCompany();
	private LabelItem titleItem;
	private boolean wait;

	public SettingsMessages settingsConstants;

	@Override
	public void init() {
		super.init();
		try {
			createControls();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void initConstants() {
		super.initConstants();
		settingsConstants = GWT.create(SettingsMessages.class);
	}

	private void createControls() {

		leftSideForm = new DynamicForm();

		titleItem = new LabelItem();
		titleItem.setTitle(settingsConstants.wareHouseTitle());

		wareHouseNameItem = new TextItem();
		wareHouseNameItem.setTitle(settingsConstants.wareName());

		contactNameItem = new TextItem();
		contactNameItem.setTitle(settingsConstants.contactName());

		contactNumberItem = new TextItem();
		contactNumberItem.setTitle(settingsConstants.contactNo());

		mobileNumberItem = new TextItem();
		mobileNumberItem.setTitle(settingsConstants.mobileNumber());

		DDINumberItem = new TextItem();
		DDINumberItem.setTitle(settingsConstants.DDINumber());

		defaultWareHouse = new CheckboxItem();
		defaultWareHouse.setTitle(settingsConstants.defaultWareHouse());

		leftSideForm.setFields(titleItem, wareHouseNameItem, contactNameItem,
				contactNumberItem, mobileNumberItem, DDINumberItem,
				defaultWareHouse);

		rightSideForm = new DynamicForm();

		addressItem = new TextItem();
		addressItem.setTitle(settingsConstants.address());

		streetItem = new TextItem();
		streetItem.setTitle(settingsConstants.streetName());

		cityItem = new TextItem();
		cityItem.setTitle(settingsConstants.city());

		stateItem = new TextItem();
		stateItem.setTitle(settingsConstants.state());

		countryItem = new TextItem();
		countryItem.setTitle(settingsConstants.country());

		postalCodeItem = new TextItem();
		postalCodeItem.setTitle(settingsConstants.postalCode());

		rightSideForm.setFields(addressItem, streetItem, cityItem, stateItem,
				countryItem, postalCodeItem);

		leftRightPanel = new HorizontalPanel();
		leftRightPanel.add(leftSideForm);
		leftRightPanel.add(rightSideForm);

		vPanel = new VerticalPanel();
		vPanel.add(leftRightPanel);
		vPanel.add(buttonLayout);

		mainPanel.add(vPanel);

	}

	private ClientWarehouse getWarehouseObject() {

		ClientWarehouse warehouse = takenWarehouse != null ? takenWarehouse
				: new ClientWarehouse();
		warehouse.setName(wareHouseNameItem.getValue().toString());
		warehouse.setMobileNumber(mobileNumberItem.getValue().toString());
		warehouse.setDDINumber(DDINumberItem.getValue().toString());
		warehouse.setDefaultWarehouse(getBooleanValue());

		return warehouse;
	}

	private boolean getBooleanValue() {
		if (defaultWareHouse.isHighLighted()) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void saveAndUpdateView() throws Exception {

		ClientWarehouse warehouse = getWarehouseObject();
		if (warehouse.getID() == 0) {
			createObject(warehouse);
		} else {
			alterObject(warehouse);
		}

		super.saveAndUpdateView();
	}

	@Override
	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		super.saveSuccess(object);
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	protected String getViewTitle() {
		return Accounter.getSettingsMessages().wareHouseTitle();
	}

	@Override
	public List getForms() {
		// currently not using
		return null;
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	public void print() {

	}

}
