/**
 * 
 */
package com.vimukti.accounter.web.client.ui.fixedassets;

import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.PendingItemsListGrid;

/**
 * @author Murali.A
 * 
 */
public class PendingItemsListView extends BaseListView<ClientFixedAsset> {

	public PendingItemsListView() {
		this.isViewSelectRequired = false;
	}

	@Override
	protected Action getAddNewAction() {
		return ActionFactory.getNewFixedAssetAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return Accounter.constants().addNewAsset();
	}

	@Override
	protected String getListViewHeading() {
		return Accounter.constants().pendingItemsList();
	}

	
	@Override
	protected void initGrid() {
		grid = new PendingItemsListGrid(false);
		grid.init();
		grid.setRecords(getAssetsByType(ClientFixedAsset.STATUS_PENDING,
				getCompany().getFixedAssets()));
		disableFilter();
	}

	@Override
	public void initListCallback() {
		// FinanceApplication.createGETService().getObjects(
		// AccounterCoreType.FIXEDASSET, this);

	}

	@Override
	public void updateInGrid(ClientFixedAsset objectTobeModified) {
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {
		// Nothing to do

	}

	@Override
	public void print() {
		// NOTHING to do

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().pendingItemsList();
	}

}
