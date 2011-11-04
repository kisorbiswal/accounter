package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class WareHouseView extends BaseView<ClientWarehouse> {

	private DynamicForm leftSideForm, rightSideForm;
	private TextItem wareHouseNameItem, contactNameItem, contactNumberItem,
			mobileNumberItem, DDINumberItem, addressItem, streetItem, cityItem,
			stateItem, countryItem, postalCodeItem, warehouseCodeItem;
	private VerticalPanel vPanel;
	private CheckboxItem defaultWareHouse;
	private Label titleItem;

	public AccounterConstants settingsConstants = Accounter.constants();

	@Override
	public void init() {
		super.init();
		createControls();
	}

	@Override
	public void initData() {
		if (getData() == null) {
			setData(new ClientWarehouse());
		} else {
			initWarehouseData(getData());
		}
		super.initData();
	}

	private void initWarehouseData(ClientWarehouse warehouse) {

		warehouseCodeItem.setValue(warehouse.getWarehouseCode());
		wareHouseNameItem.setValue(warehouse.getName());

		ClientContact clientContact = warehouse.getContact();
		if (clientContact != null) {

			if (clientContact.getName() != null) {
				contactNameItem.setValue(clientContact.getName());
			}
			if (clientContact.getBusinessPhone() != null) {
				contactNumberItem.setValue(clientContact.getBusinessPhone());
			}

		}

		ClientAddress address = warehouse.getAddress();
		if (address != null) {

			if (address.getAddress1() != null) {
				addressItem.setValue(address.getAddress1());
			}
			if (address.getCity() != null) {
				cityItem.setValue(address.getCity());
			}
			if (address.getCountryOrRegion() != null) {
				countryItem.setValue(address.getCountryOrRegion());
			}
			if (address.getStateOrProvinence() != null) {
				stateItem.setValue(address.getStateOrProvinence());
			}
			if (address.getStreet() != null) {
				streetItem.setValue(address.getStreet());
			}
			if (address.getZipOrPostalCode() != null) {
				postalCodeItem.setValue(address.getZipOrPostalCode());
			}

		}
		if (warehouse.getMobileNumber() != null) {
			mobileNumberItem.setValue(warehouse.getMobileNumber());
		}
		if (warehouse.getDDINumber() != null) {
			DDINumberItem.setValue(warehouse.getDDINumber());
		}
		if (warehouse.isDefaultWarehouse()) {
			defaultWareHouse.setValue(true);
		}
		// defaultWareHouse.setValue(warehouse.isDefaultWarehouse());

	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		result.add(leftSideForm.validate());
		return result;
	}

	private void createControls() {

		HorizontalPanel mainHLay = new HorizontalPanel();
		mainHLay.addStyleName("fields-panel");
		mainHLay.setWidth("100%");

		mainHLay.add(getLeftSideForm());
		mainHLay.add(getRightSideForm());

		titleItem = new Label(settingsConstants.wareHouse());
		titleItem.setStyleName(Accounter.constants().labelTitle());

		vPanel = new VerticalPanel();
		vPanel.setWidth("100%");
		vPanel.add(titleItem);
		vPanel.add(mainHLay);

		this.setSize("100%", "100%");
		this.add(vPanel);

	}

	private DynamicForm getRightSideForm() {

		rightSideForm = new DynamicForm();
		rightSideForm.setWidth("100%");

		addressItem = new TextItem();
		addressItem.setTitle(settingsConstants.address());
		addressItem.setDisabled(isInViewMode());

		streetItem = new TextItem();
		streetItem.setTitle(settingsConstants.streetName());
		streetItem.setDisabled(isInViewMode());

		cityItem = new TextItem();
		cityItem.setTitle(settingsConstants.city());
		cityItem.setDisabled(isInViewMode());

		stateItem = new TextItem();
		stateItem.setTitle(settingsConstants.state());
		stateItem.setDisabled(isInViewMode());

		countryItem = new TextItem();
		countryItem.setTitle(settingsConstants.country());
		countryItem.setDisabled(isInViewMode());

		postalCodeItem = new TextItem();
		postalCodeItem.setTitle(settingsConstants.postalCode());
		postalCodeItem.setDisabled(isInViewMode());

		rightSideForm.setFields(addressItem, streetItem, cityItem, stateItem,
				countryItem, postalCodeItem);
		return rightSideForm;
	}

	private DynamicForm getLeftSideForm() {

		leftSideForm = new DynamicForm();
		leftSideForm.setWidth("100%");

		warehouseCodeItem = new TextItem();
		warehouseCodeItem.setTitle(settingsConstants.warehouseCode());
		warehouseCodeItem.setRequired(true);
		warehouseCodeItem.setDisabled(isInViewMode());

		wareHouseNameItem = new TextItem();
		wareHouseNameItem.setTitle(settingsConstants.wareName());
		wareHouseNameItem.setRequired(true);
		wareHouseNameItem.setDisabled(isInViewMode());

		contactNameItem = new TextItem();
		contactNameItem.setTitle(settingsConstants.contactName());
		contactNameItem.setDisabled(isInViewMode());

		contactNumberItem = new TextItem();
		contactNumberItem.setTitle(settingsConstants.contactNumber());
		contactNumberItem.setDisabled(isInViewMode());

		mobileNumberItem = new TextItem();
		mobileNumberItem.setTitle(settingsConstants.mobileNumber());
		mobileNumberItem.setDisabled(isInViewMode());

		DDINumberItem = new TextItem();
		DDINumberItem.setTitle(settingsConstants.ddiNumber());
		DDINumberItem.setDisabled(isInViewMode());

		defaultWareHouse = new CheckboxItem();
		defaultWareHouse.setTitle(settingsConstants.defaultWareHouse());
		defaultWareHouse.setDisabled(isInViewMode());

		leftSideForm.setFields(warehouseCodeItem, wareHouseNameItem,
				contactNameItem, contactNumberItem, mobileNumberItem,
				DDINumberItem, defaultWareHouse);

		return leftSideForm;
	}

	@Override
	public void onEdit() {

		AccounterAsyncCallback<Boolean> editCallback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onResultSuccess(Boolean result) {
				if (result) {
					enableFormItems();
				}
			}

			@Override
			public void onException(AccounterException exception) {
				Accounter.showError(exception.getMessage());
			}
		};
		this.rpcDoSerivce.canEdit(AccounterCoreType.WAREHOUSE, data.getID(),
				editCallback);

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		addressItem.setDisabled(isInViewMode());
		streetItem.setDisabled(isInViewMode());
		cityItem.setDisabled(isInViewMode());
		countryItem.setDisabled(isInViewMode());
		stateItem.setDisabled(isInViewMode());
		postalCodeItem.setDisabled(isInViewMode());
		warehouseCodeItem.setDisabled(isInViewMode());
		wareHouseNameItem.setDisabled(isInViewMode());
		contactNameItem.setDisabled(isInViewMode());
		contactNumberItem.setDisabled(isInViewMode());
		mobileNumberItem.setDisabled(isInViewMode());
		DDINumberItem.setDisabled(isInViewMode());
		defaultWareHouse.setDisabled(isInViewMode());

	}

	private ClientWarehouse getWarehouseObject() {

		data.setWarehouseCode(warehouseCodeItem.getValue());
		data.setName(wareHouseNameItem.getValue().toString());
		data.setMobileNumber(mobileNumberItem.getValue().toString());
		data.setDDINumber(DDINumberItem.getValue().toString());
		data.setDefaultWarehouse((Boolean) defaultWareHouse.getValue());

		ClientAddress address = new ClientAddress();
		address.setType(ClientAddress.TYPE_WAREHOUSE);
		address.setAddress1(addressItem.getValue());
		address.setCity(cityItem.getValue());
		address.setCountryOrRegion(countryItem.getValue());
		address.setStreet(streetItem.getValue());
		address.setStateOrProvinence(stateItem.getValue());
		address.setZipOrPostalCode(postalCodeItem.getValue());
		data.setAddress(address);

		ClientContact contact = new ClientContact();
		contact.setName(contactNameItem.getValue());
		contact.setBusinessPhone(contactNumberItem.getValue());

		data.setContact(contact);

		return data;
	}

	@Override
	public void saveAndUpdateView() {

		ClientWarehouse warehouse = getWarehouseObject();
		saveOrUpdate(warehouse);

		super.saveAndUpdateView();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().wareHouse();
	}

	@Override
	public List getForms() {
		// currently not using
		return null;
	}

	@Override
	public void printPreview() {

	}

	@Override
	public void print() {

	}

	@Override
	public void setFocus() {
		this.warehouseCodeItem.setFocus();

	}

}
