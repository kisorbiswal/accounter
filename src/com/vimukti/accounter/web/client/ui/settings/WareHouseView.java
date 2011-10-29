package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
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
	private ClientWarehouse takenWarehouse;
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
		contactNameItem.setValue(clientContact.getName());
		contactNumberItem.setValue(clientContact.getBusinessPhone());

		ClientAddress address = warehouse.getAddress();
		addressItem.setValue(address.getAddress1());
		cityItem.setValue(address.getCity());
		countryItem.setValue(address.getCountryOrRegion());
		stateItem.setValue(address.getStateOrProvinence());
		streetItem.setValue(address.getStreet());
		postalCodeItem.setValue(address.getZipOrPostalCode());

		mobileNumberItem.setValue(warehouse.getMobileNumber());
		DDINumberItem.setValue(warehouse.getDDINumber());
		defaultWareHouse.setValue(warehouse.isDefaultWarehouse());

	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		result.add(leftSideForm.validate());
		return result;
	}

	private void createControls() {

		HorizontalPanel mainHLay = new HorizontalPanel();
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
		return rightSideForm;
	}

	private DynamicForm getLeftSideForm() {

		leftSideForm = new DynamicForm();
		leftSideForm.setWidth("100%");

		warehouseCodeItem = new TextItem();
		warehouseCodeItem.setTitle(settingsConstants.warehouseCode());
		warehouseCodeItem.setRequired(true);

		wareHouseNameItem = new TextItem();
		wareHouseNameItem.setTitle(settingsConstants.wareName());
		wareHouseNameItem.setRequired(true);

		contactNameItem = new TextItem();
		contactNameItem.setTitle(settingsConstants.contactName());

		contactNumberItem = new TextItem();
		contactNumberItem.setTitle(settingsConstants.contactNumber());

		mobileNumberItem = new TextItem();
		mobileNumberItem.setTitle(settingsConstants.mobileNumber());

		DDINumberItem = new TextItem();
		DDINumberItem.setTitle(settingsConstants.ddiNumber());

		defaultWareHouse = new CheckboxItem();
		defaultWareHouse.setTitle(settingsConstants.defaultWareHouse());

		leftSideForm.setFields(warehouseCodeItem, wareHouseNameItem,
				contactNameItem, contactNumberItem, mobileNumberItem,
				DDINumberItem, defaultWareHouse);

		return leftSideForm;
	}

	private ClientWarehouse getWarehouseObject() {

		ClientWarehouse warehouse = takenWarehouse != null ? takenWarehouse
				: new ClientWarehouse();
		warehouse.setWarehouseCode(warehouseCodeItem.getValue());
		warehouse.setName(wareHouseNameItem.getValue().toString());
		warehouse.setMobileNumber(mobileNumberItem.getValue().toString());
		warehouse.setDDINumber(DDINumberItem.getValue().toString());
		warehouse.setDefaultWarehouse(getBooleanValue());

		ClientAddress address = new ClientAddress();
		address.setType(ClientAddress.TYPE_WAREHOUSE);
		address.setAddress1(addressItem.getValue());
		address.setCity(cityItem.getValue());
		address.setCountryOrRegion(countryItem.getValue());
		address.setStreet(streetItem.getValue());
		address.setStateOrProvinence(stateItem.getValue());
		address.setZipOrPostalCode(postalCodeItem.getValue());
		warehouse.setAddress(address);

		ClientContact contact = new ClientContact();
		contact.setName(contactNameItem.getValue());
		contact.setBusinessPhone(contactNumberItem.getValue());

		warehouse.setContact(contact);

		return warehouse;
	}

	private boolean getBooleanValue() {
		if (defaultWareHouse.isHighLighted()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void saveAndUpdateView() {

		ClientWarehouse warehouse = getWarehouseObject();
		saveOrUpdate(warehouse);

		super.saveAndUpdateView();
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		super.saveSuccess(object);
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
	public void onEdit() {
		setMode(EditMode.EDIT);
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
