/**
 * 
 */
package com.vimukti.accounter.web.client.ui.fixedassets;

import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
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
		return ActionFactory.getNewFixedAssetAction();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getAddNewLabelString
	 */
	@Override
	protected String getAddNewLabelString() {
		return Accounter.constants().addNewAsset();
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.core.BaseListView#getListViewHeading
	 */
	@Override
	protected String getListViewHeading() {
		return Accounter.constants().registeredItemsList();
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.core.BaseListView#initGrid()
	 */
	
	@Override
	protected void initGrid() {
		grid = new RegisteredItemsListGrid(false);
		grid.init();
		grid.setRecords(getAssetsByType(ClientFixedAsset.STATUS_REGISTERED,
				getCompany().getFixedAssets()));
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
		return Accounter.constants().registeredItemsList();
	}

}
