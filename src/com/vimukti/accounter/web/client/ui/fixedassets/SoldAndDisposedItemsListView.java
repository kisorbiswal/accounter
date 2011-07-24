/**
 * 
 */
package com.vimukti.accounter.web.client.ui.fixedassets;

import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.FixedAssetsActionFactory;
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
		return FixedAssetsActionFactory.getNewFixedAssetAction();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getAddNewLabelString
	 * ()
	 */
	@Override
	protected String getAddNewLabelString() {
		return fixedAssetConstants.addNewAsset();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getListViewHeading
	 * ()
	 */
	@Override
	protected String getListViewHeading() {
		return Accounter.getFixedAssetConstants()
				.soldAndDisposedItems();
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.BaseListView#initGrid()
	 */
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return Accounter.getActionsConstants().soldDisposedItems();
	}

}
