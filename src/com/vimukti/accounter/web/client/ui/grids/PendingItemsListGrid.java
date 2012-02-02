/**
 * 
 */
package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.fixedassets.NoteDialog;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

/**
 * @author Murali.A
 * 
 */
public class PendingItemsListGrid extends BaseListGrid<ClientFixedAsset> {

	private NoteDialog noteDialog;

	/**
	 * @param isMultiSelectionEnable
	 */
	public PendingItemsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.grids.BaseListGrid#setColTypes()
	 */
	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.grids.CustomTable#getColumns()
	 */
	@Override
	protected String[] getColumns() {
		return new String[] { messages.item(), messages.assetNumber(),
				messages.Account(), messages.purchaseDate(),
				messages.purchasePrice(), messages.showHistory(),
				messages.addNote(), "" };
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.grids.ListGrid#getColumnValue(java
	 * .lang.Object, int)
	 */
	@Override
	protected Object getColumnValue(ClientFixedAsset asset, int index) {
		switch (index) {
		case 0:
			return asset.getName() != null ? asset.getName() : "";
		case 1:
			return asset.getAssetNumber();
		case 2:
			return getCompany().getAccount(asset.getAssetAccount()) != null ? Accounter
					.getCompany().getAccount(asset.getAssetAccount()).getName()
					: "";
		case 3:
			return asset.getPurchaseDate() != 0 ? UIUtils
					.getDateByCompanyType(new ClientFinanceDate(asset
							.getPurchaseDate())) : "";
		case 4:
			return DataUtils
					.amountAsStringWithCurrency(asset.getPurchasePrice(),
							getCompany().getPrimaryCurrency());
		case 5:
			return messages.showHistory();
		case 6:
			return messages.addNote();
		case 7:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		default:
			return "";
		}
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 7)
			return 20;
		if (index == 2) {
			return 200;
		}
		return super.getCellWidth(index);
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.grids.ListGrid#onDoubleClick(java
	 * .lang.Object)
	 */
	@Override
	public void onDoubleClick(ClientFixedAsset obj) {

	}

	@Override
	protected void onDoubleClick(ClientFixedAsset obj, int row, int index) {
		if (!(Accounter.getUser().getUserRole().equals(RolePermissions.ADMIN) || Accounter
				.getUser().getUserRole()
				.equals(RolePermissions.FINANCIAL_ADVISER)))
			return;
		switch (index) {
		case 5:
			openHistoryView(obj);
			break;
		case 6:
			openNoteDialog(obj);
			break;
		default:
			ActionFactory.getNewFixedAssetAction().run(obj, false);
			break;
		}

	}

	@Override
	protected void onClick(ClientFixedAsset obj, int row, int col) {
		switch (col) {
		case 7:
			showWarnDialog(obj);
			break;
		default:
			break;
		}
	}

	private void openHistoryView(ClientFixedAsset obj) {
		Action action = ActionFactory.getHistoryListAction();
		action.catagory = messages.fixedAssetsPendingItemsList();
		action.run(obj, true);
	}

	private void openNoteDialog(final ClientFixedAsset asset) {
		noteDialog = new NoteDialog(messages.addNote(), "");
		if (noteDialog.executeUpdate(asset)) {
			updateData(asset);
		}
	}

	@Override
	protected void executeDelete(ClientFixedAsset asset) {
		deleteObject(asset);
	}

	@Override
	protected int sort(ClientFixedAsset obj1, ClientFixedAsset obj2, int index) {

		switch (index) {
		case 0:
			return obj1.getName().compareTo(obj2.getName());

		case 1:
			String number1 = obj1.getAssetNumber();
			String number2 = obj2.getAssetNumber();
			return number1.compareTo(number2);

		case 2:
			return getAccount(obj1).compareTo(getAccount(obj2));

		case 3:
			ClientFinanceDate date1 = new ClientFinanceDate(
					obj1.getPurchaseDate());
			ClientFinanceDate date2 = new ClientFinanceDate(
					obj2.getPurchaseDate());
			if (date1 != null && date2 != null)
				return date1.compareTo(date2);
			break;

		case 4:
			Double price1 = obj1.getPurchasePrice();
			Double price2 = obj2.getPurchasePrice();
			return price1.compareTo(price2);
		}

		return 0;

	}

	private String getAccount(ClientFixedAsset obj) {

		return getCompany().getAccount(obj.getAssetAccount()) != null ? Accounter
				.getCompany().getAccount(obj.getAssetAccount()).getName()
				: "";
	}

	@Override
	public AccounterCoreType getType() {
		return AccounterCoreType.FIXEDASSET;
	}

}
