package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientItemStatus;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;

public class WareHouseItemsListView extends BaseListView<ClientItemStatus> {

	private long wareHouse;

	public WareHouseItemsListView(long wareHouse) {
		this.wareHouse = wareHouse;
	}

	@Override
	public void updateInGrid(ClientItemStatus objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initGrid() {
		viewSelect.setVisible(false);
		grid = new WareHouseItemsListGrid(false);
		grid.init();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getItemStatuses(wareHouse,
				new AccounterAsyncCallback<ArrayList<ClientItemStatus>>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(
							ArrayList<ClientItemStatus> result) {
						grid.removeLoadingImage();
						if (result != null && !result.isEmpty()) {
							grid.setRecords(result);
						} else {
							grid.addEmptyMessage(messages().noRecordsToShow());
						}
						grid.sort(10, false);
					}
				});
	}

	@Override
	protected String getListViewHeading() {
		return messages().itemsDash(
				getCompany().getWarehouse(wareHouse).getName());
	}

	@Override
	protected Action getAddNewAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		return "";
	}

	@Override
	protected String getViewTitle() {
		return messages().wareHouseItems();
	}

}
