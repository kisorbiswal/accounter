package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientStockTransfer;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;

public class WarehouseTransferListView extends
		BaseListView<ClientStockTransfer> {

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void updateInGrid(ClientStockTransfer objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		reloadRecords();
	}

	public void reloadRecords() {
		Accounter.createHomeService().getWarehouseTransfersList(
				new AccounterAsyncCallback<ArrayList<ClientStockTransfer>>() {

					@Override
					public void onException(AccounterException exception) {
						grid.addEmptyMessage(constants.noRecordsToShow());
					}

					@Override
					public void onResultSuccess(
							ArrayList<ClientStockTransfer> result) {
						grid.removeLoadingImage();
						if (result != null && !result.isEmpty()) {
							grid.addRecords(result);
						} else {
							grid.addEmptyMessage(constants.noRecordsToShow());
						}
					}
				});
	}

	@Override
	protected void initGrid() {
		grid = new WarehouseTransferListGrid(false);
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		return Accounter.constants().warehouseTransferList();
	}

	@Override
	protected Action getAddNewAction() {
		return ActionFactory.getWareHouseTransferAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return Accounter.constants().addNewWarehouseTransfer();
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().warehouseTransferList();
	}

	@Override
	public void printPreview() {

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}
}
