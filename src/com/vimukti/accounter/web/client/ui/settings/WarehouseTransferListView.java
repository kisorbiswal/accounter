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
						grid.addEmptyMessage(messages().noRecordsToShow());
					}

					@Override
					public void onResultSuccess(
							ArrayList<ClientStockTransfer> result) {
						grid.removeLoadingImage();
						if (result != null && !result.isEmpty()) {
							grid.addRecords(result);
						} else {
							grid.addEmptyMessage(messages().noRecordsToShow());
						}
						grid.sort(10, false);
					}
				});
	}

	@Override
	protected void initGrid() {
		viewSelect.setVisible(false);
		grid = new WarehouseTransferListGrid(false);
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		return Accounter.messages().warehouseTransferList();
	}

	@Override
	protected Action getAddNewAction() {
		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages().readOnly()))
			return ActionFactory.getWareHouseTransferAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages().readOnly()))
			return Accounter.messages().addNewWarehouseTransfer();
		else
			return "";
	}

	@Override
	protected String getViewTitle() {
		return Accounter.messages().warehouseTransferList();
	}

	@Override
	public void printPreview() {

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}
}
