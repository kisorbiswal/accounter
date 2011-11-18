/**
 * 
 */
package com.vimukti.accounter.web.client.ui.fixedassets;

import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.SoldAndDisposedItemsListGrid;

/**
 * @author Murali.A
 * 
 */
public class SoldAndDisposedItemsListView extends
		BaseListView<ClientFixedAsset> {

	public SoldAndDisposedItemsListView() {
		this.isViewSelectRequired = false;
	}

	@Override
	protected Action getAddNewAction() {
		return ActionFactory.getNewFixedAssetAction();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getAddNewLabelString
	 * ()
	 */
	@Override
	protected String getAddNewLabelString() {
		return Accounter.messages().addNewAsset();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getListViewHeading
	 * ()
	 */
	@Override
	protected String getListViewHeading() {
		return Accounter.messages().soldAndDisposedItems();
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.BaseListView#initGrid()
	 */
	
	@Override
	protected void initGrid() {
		grid = new SoldAndDisposedItemsListGrid(false);
		grid.init();
		grid.setRecords(getAssetsByType(
				ClientFixedAsset.STATUS_SOLD_OR_DISPOSED, Accounter
						.getCompany().getFixedAssets()));
		disableFilter();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#initListCallback()
	 */
	@Override
	public void initListCallback() {
		// TODO Auto-generated method stub

	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.IAccounterList#updateInGrid(
	 * java.lang.Object)
	 */
	
	@Override
	public void updateInGrid(ClientFixedAsset objectTobeModified) {
		grid.setRecords(getAssetsByType(ClientFixedAsset.STATUS_PENDING,
				getCompany().getFixedAssets()));
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return Accounter.messages().soldDisposedItems();
	}

}
