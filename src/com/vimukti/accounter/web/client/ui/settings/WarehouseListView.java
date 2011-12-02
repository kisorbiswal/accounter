package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;

public class WarehouseListView extends BaseListView<ClientWarehouse> {

	// private ArrayList<ClientWarehouse> listOfWarehouses = new
	// ArrayList<ClientWarehouse>();

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected String getViewTitle() {
		return Accounter.messages().warehouseList();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getWarehouses(this);

	}

	@Override
	protected void initGrid() {
		viewSelect.setVisible(false);
		grid = new WarehouseListGrid(false);
		grid.init();
		// listOfWarehouses = getCompany().getWarehouses();
		// filterList(true);
	}

	// @Override
	// protected void filterList(boolean isActive) {
	// grid.removeAllRecords();
	// for (ClientWarehouse warehouse : listOfWarehouses) {
	// if (isActive) {
	// if (warehouse.isDefaultWarehouse() == true)
	// grid.addData(warehouse);
	// } else if (warehouse.isDefaultWarehouse() == false) {
	// grid.addData(warehouse);
	// }
	//
	// }
	// if (grid.getRecords().isEmpty())
	// grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
	// }

	@Override
	protected String getListViewHeading() {
		return Accounter.messages().warehouseList();
	}

	@Override
	protected Action getAddNewAction() {
		return ActionFactory.getWareHouseViewAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return Accounter.messages().addNewWarehouse();
	}

	@Override
	public void updateInGrid(ClientWarehouse objectTobeModified) {
		// TODO Auto-generated method stub

	}
}
