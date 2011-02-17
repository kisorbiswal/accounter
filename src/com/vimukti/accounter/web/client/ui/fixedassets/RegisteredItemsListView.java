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
import com.vimukti.accounter.web.client.ui.grids.RegisteredItemsListGrid;

/**
 * @author Murali.A
 * 
 */
public class RegisteredItemsListView extends BaseListView<ClientFixedAsset> {

	public RegisteredItemsListView() {
		this.isViewSelectRequired = false;
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getAddNewAction()
	 */
	@Override
	protected Action getAddNewAction() {
		return FixedAssetsActionFactory.getNewFixedAssetAction();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getAddNewLabelString
	 */
	@Override
	protected String getAddNewLabelString() {
		return fixedAssetConstants.addNewAsset();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getListViewHeading
	 */
	@Override
	protected String getListViewHeading() {
		return FinanceApplication.getFixedAssetConstants()
				.registeredItemsList();
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.BaseListView#initGrid()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void initGrid() {
		grid = new RegisteredItemsListGrid(false);
		grid.init();
		grid.setRecords(getAssetsByType(ClientFixedAsset.STATUS_REGISTERED,
				FinanceApplication.getCompany().getFixedAssets()));
		disableFilter();
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
	public void updateInGrid(ClientFixedAsset objectTobeModified) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateGrid(IAccounterCore core) {
		grid.setRecords(getAssetsByType(ClientFixedAsset.STATUS_REGISTERED,
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

}
