package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.core.client.GWT;
import com.vimukti.accounter.core.StockTransfer;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;

public class WarehouseTransferListView extends BaseListView<StockTransfer> {

	private AccounterConstants customerConstants;

	@Override
	public void init() {
		customerConstants = Accounter.constants();
		super.init();
	}

	@Override
	public void updateInGrid(StockTransfer objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initGrid() {
		grid = new WarehouseTransferListGrid(false);
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Action getAddNewAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().warehouseTransferList();
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		if (object != null) {
			super.saveSuccess(object);
			SettingsActionFactory.getInvoiceBrandingAction().run(null, false);
		} else
			saveFailed(new Exception(Accounter.constants().failed()));
	}

}
