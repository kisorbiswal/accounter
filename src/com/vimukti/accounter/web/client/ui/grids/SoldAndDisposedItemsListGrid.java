/**
 * 
 */
package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.ClientFixedAssetNote;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.FixedAssetsActionFactory;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.fixedassets.NoteDialog;

/**
 * @author Murali.A
 * 
 */
public class SoldAndDisposedItemsListGrid extends
		BaseListGrid<ClientFixedAsset> {

	private NoteDialog noteDialog;

	/**
	 * @param isMultiSelectionEnable
	 */
	public SoldAndDisposedItemsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	/**
	 * @param isMultiSelectionEnable
	 * @param showFooter
	 */
	public SoldAndDisposedItemsListGrid(boolean isMultiSelectionEnable,
			boolean showFooter) {
		super(isMultiSelectionEnable, showFooter);
		// TODO Auto-generated constructor stub
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.grids.BaseListGrid#setColTypes()
	 */
	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 8)
			return 20;
		return super.getCellWidth(index);
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
			return asset.getName();
		case 1:
			return asset.getAssetNumber();
		case 2:
			return Accounter.getCompany().getAccount(
					asset.getAssetAccount()) != null ? Accounter
					.getCompany().getAccount(asset.getAssetAccount()).getName()
					: "";
		case 3:
			long date;
			if (asset.getSoldOrDisposedDate() != 0) {
				return UIUtils.getDateByCompanyType(new ClientFinanceDate(asset
						.getSoldOrDisposedDate()));
			} else
				date = 0;
			return date != 0 ? UIUtils
					.getDateByCompanyType(new ClientFinanceDate(date)) : "";

		case 4:
			return DataUtils.getAmountAsString(asset.getSalePrice());
		case 5:
			return DataUtils.getAmountAsString(asset.getLossOrGain());

		case 6:
			return Accounter.getFixedAssetConstants().showHistory();
		case 7:
			return Accounter.getFixedAssetConstants().addNote();
		case 8:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		}
		return "";
	}

	// public ClientSellingOrDisposingFixedAsset getSellingDisposedItem(
	// String assetId) {
	//
	// return FinanceApplication.getCompany().getSellingDisposedItem(assetId);
	//
	// }

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.grids.ListGrid#onDoubleClick(java
	 * .lang.Object)
	 */
	@Override
	public void onDoubleClick(ClientFixedAsset obj) {
		HistoryTokenUtils.setPresentToken(FixedAssetsActionFactory
				.getNewFixedAssetAction(), obj);
		FixedAssetsActionFactory.getNewFixedAssetAction().run(obj, true);
	}

	@Override
	protected void onClick(ClientFixedAsset obj, int row, int col) {
		@SuppressWarnings("unused")
		List<ClientFixedAsset> records = getRecords();
		switch (col) {
		case 6:
			openHistoryView(obj);
			break;
		case 7:
			openNoteDialog(obj);
			break;
		case 8:
			showWarnDialog(obj);
		}
	}

	private void openHistoryView(ClientFixedAsset obj) {
		Action action = FixedAssetsActionFactory.getHistoryListAction();
		action.catagory = Accounter.getFixedAssetConstants()
				.fixedAssetsPendingItemsList();
		HistoryTokenUtils.setPresentToken(action, obj);
		action.run(obj, true);
	}

	private void openNoteDialog(final ClientFixedAsset asset) {
		noteDialog = new NoteDialog(Accounter.getFixedAssetConstants()
				.addNote(), "");
		noteDialog.addInputDialogHandler(new InputDialogHandler() {

			@Override
			public boolean onOkClick() {
				String note = noteDialog.noteArea.getValue() != null ? noteDialog.noteArea
						.getValue().toString()
						: "";
				// setAttribute("note", note, currentRow);
				if (note.length() != 0)
					executeUpdate(asset, note);
				return true;
			}

			@Override
			public void onCancelClick() {

			}
		});
	}

	private void executeUpdate(ClientFixedAsset asset, String value) {
		List<ClientFixedAssetNote> noteList = asset.getFixedAssetNotes();
		ClientFixedAssetNote note = new ClientFixedAssetNote();
		note.setNote(value);
		noteList.add(note);
		asset.setFixedAssetNotes(noteList);
		ViewManager.getInstance().alterObject(asset, this);
	}

	protected void executeDelete(ClientFixedAsset asset) {
		ViewManager.getInstance().deleteObject(asset,
				AccounterCoreType.FIXEDASSET, this);
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.grids.ListGrid#validateGrid()
	 */
	@Override
	public boolean validateGrid() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.grids.CustomTable#getColumns()
	 */
	@Override
	protected String[] getColumns() {
		return new String[] {
				Accounter.getFixedAssetConstants().item(),
				Accounter.getFixedAssetConstants().assetNumber(),
				Accounter.getFixedAssetConstants().account(),
				Accounter.getFixedAssetConstants().disposalDate(),
				Accounter.getFixedAssetConstants().disposalPrice(),
				Accounter.getFixedAssetConstants().GainsOrLosses(),
				Accounter.getFixedAssetConstants().showHistory(),
				Accounter.getFixedAssetConstants().addNote(), "" };
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
			ClientFinanceDate date1 = new ClientFinanceDate(obj1
					.getSoldOrDisposedDate());
			ClientFinanceDate date2 = new ClientFinanceDate(obj2
					.getSoldOrDisposedDate());
			if (date1 != null && date2 != null)
				return date1.compareTo(date2);
			break;

		case 4:
			Double price1 = obj1.getSalePrice();
			Double price2 = obj2.getSalePrice();
			return price1.compareTo(price2);

		case 5:
			Double gain1 = obj1.getLossOrGain();
			Double gain2 = obj2.getLossOrGain();
			return gain1.compareTo(gain2);
		}

		return 0;

	}

	private String getAccount(ClientFixedAsset obj) {

		return Accounter.getCompany()
				.getAccount(obj.getAssetAccount()) != null ? Accounter
				.getCompany().getAccount(obj.getAssetAccount()).getName() : "";
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	public AccounterCoreType getType() {
		return null;
	}

}
