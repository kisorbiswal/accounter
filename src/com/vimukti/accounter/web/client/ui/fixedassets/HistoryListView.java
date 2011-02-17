/**
 * 
 */
package com.vimukti.accounter.web.client.ui.fixedassets;

import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.ClientFixedAssetHistory;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.HistoryListGrid;

/**
 * @author Murali.A
 * 
 */
public class HistoryListView extends BaseListView<ClientFixedAssetHistory> {

	private ClientFixedAsset asset;

	public HistoryListView(ClientFixedAsset asset) {
		this.asset = asset;
		this.isViewSelectRequired = false;
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getAddNewAction()
	 */
	@Override
	protected Action getAddNewAction() {
		return null;
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getAddNewLabelString
	 * ()
	 */
	@Override
	protected String getAddNewLabelString() {
		return "";
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getListViewHeading
	 * ()
	 */
	@Override
	protected String getListViewHeading() {
		return FinanceApplication.getFixedAssetConstants().history();
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.BaseListView#initGrid()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void initGrid() {
		grid = new HistoryListGrid(false);
		grid.init();
		grid.setRecords(asset.getFixedAssetsHistory());

	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#initListCallback()
	 */
	@Override
	public void initListCallback() {

	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.IAccounterList#updateInGrid(
	 * java.lang.Object)
	 */
	@Override
	public void updateInGrid(ClientFixedAssetHistory objectTobeModified) {

	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

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

}
