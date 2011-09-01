package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.core.ClientStockTransfer;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;

public class WarehouseListView extends BaseListView<ClientStockTransfer> {

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result){

	}


	@Override
	protected String getViewTitle() {
		return Accounter.constants().warehouseList();
	}

	@Override
	public void fitToSize(int height, int width) {

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

	@Override
	public void saveSuccess(IAccounterCore object) {
		if (object != null) {
			super.saveSuccess(object);
			ActionFactory.getInvoiceBrandingAction().run(null, false);
		} else
			saveFailed(new AccounterException(Accounter.constants().failed()));
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
		return Accounter.constants().wareHouse();
	}

	@Override
	public void updateInGrid(ClientStockTransfer objectTobeModified) {
		// currently not using

	}
}
