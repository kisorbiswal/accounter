package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;

public class WarehouseListView extends BaseListView<ClientWarehouse> {

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().warehouseList();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getWarehouses(this);

	}

	@Override
	protected void initGrid() {
		grid = new WarehouseListGrid(false);
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		return Accounter.constants().warehouseList();
	}

	@Override
	protected Action getAddNewAction() {
		return ActionFactory.getWareHouseViewAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return Accounter.constants().addNewWarehouse();
	}

	@Override
	public void updateInGrid(ClientWarehouse objectTobeModified) {
		// TODO Auto-generated method stub

	}
}
