/**
 * 
 */
package com.vimukti.accounter.web.client.ui.fixedassets;

import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.FixedAssetsActionFactory;
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
		return FixedAssetsActionFactory.getNewFixedAssetAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return fixedAssetConstants.addNewAsset();
	}

	@Override
	protected String getListViewHeading() {
		return FinanceApplication.getFixedAssetConstants().pendingItemsList();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initGrid() {
		grid = new PendingItemsListGrid(false);
		grid.init();
		grid.setRecords(getAssetsByType(ClientFixedAsset.STATUS_PENDING,
				FinanceApplication.getCompany().getFixedAssets()));
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

	/* updates the grid after editing the selected record(asset) */
	@SuppressWarnings("unchecked")
	@Override
	public void updateGrid(IAccounterCore core) {
		grid.setRecords(getAssetsByType(ClientFixedAsset.STATUS_PENDING,
				FinanceApplication.getCompany().getFixedAssets()));
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
		return FinanceApplication.getActionsConstants().pendingItemsList();
	}

}
