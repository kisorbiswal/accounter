package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
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
	private ClientWarehouse takenWarehouse;
	private LabelItem titleItem;

	public AccounterConstants settingsConstants = Accounter.constants();

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {

		leftSideForm = new DynamicForm();

		titleItem = new LabelItem();
		titleItem.setTitle(settingsConstants.wareHouse());

		wareHouseNameItem = new TextItem();
		wareHouseNameItem.setTitle(settingsConstants.wareName());

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

		this.setSize("100%", "100%");
		this.add(vPanel);

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
	public void deleteSuccess(IAccounterCore result){

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

}
