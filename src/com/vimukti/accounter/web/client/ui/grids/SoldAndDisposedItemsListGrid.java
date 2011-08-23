/**
 * 
 */
package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.ClientFixedAssetNote;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
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
			return getCompany().getAccount(asset.getAssetAccount()) != null ? Accounter
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
			return amountAsString(asset.getSalePrice());
		case 5:
			return amountAsString(asset.getLossOrGain());

		case 6:
			return Accounter.constants().showHistory();
		case 7:
			return Accounter.constants().addNote();
		case 8:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		}
		return "";
	}

	// public ClientSellingOrDisposingFixedAsset getSellingDisposedItem(
	// String assetID) {
	//
	// return FinanceApplication.getCompany().getSellingDisposedItem(assetID);
	//
	// }

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.grids.ListGrid#onDoubleClick(java
	 * .lang.Object)
	 */
	@Override
	public void onDoubleClick(ClientFixedAsset obj) {
		ActionFactory.getNewFixedAssetAction().run(obj, true);
	}

	@Override
	protected void onClick(ClientFixedAsset obj, int row, int col) {

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
		Action action = ActionFactory.getHistoryListAction();
		action.catagory = Accounter.constants().fixedAssetsPendingItemsList();
		action.run(obj, true);
	}

	private void openNoteDialog(final ClientFixedAsset asset) {
		noteDialog = new NoteDialog(Accounter.constants().addNote(), "");
		noteDialog.addInputDialogHandler(new InputDialogHandler() {

			@Override
			public boolean onOK() {
				String note = noteDialog.noteArea.getValue() != null ? noteDialog.noteArea
						.getValue().toString() : "";
				// setAttribute("note", note, currentRow);
				if (note.length() != 0)
					executeUpdate(asset, note);
				return true;
			}

			@Override
			public void onCancel() {

			}
		});
	}

	private void executeUpdate(ClientFixedAsset asset, String value) {
		List<ClientFixedAssetNote> noteList = asset.getFixedAssetNotes();
		ClientFixedAssetNote note = new ClientFixedAssetNote();
		note.setNote(value);
		noteList.add(note);
		asset.setFixedAssetNotes(noteList);
		Accounter.createOrUpdate(this, asset);
	}

	protected void executeDelete(ClientFixedAsset asset) {
		deleteObject(asset);
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.grids.CustomTable#getColumns()
	 */
	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.constants().item(),
				Accounter.constants().assetNumber(),
				Accounter.messages().account(Global.get().account()),
				Accounter.constants().disposalDate(),
				Accounter.constants().disposalPrice(),
				Accounter.constants().gainsOrLosses(),
				Accounter.constants().showHistory(),
				Accounter.constants().addNote(), "" };
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
					obj1.getSoldOrDisposedDate());
			ClientFinanceDate date2 = new ClientFinanceDate(
					obj2.getSoldOrDisposedDate());
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

		return getCompany().getAccount(obj.getAssetAccount()) != null ? Accounter
				.getCompany().getAccount(obj.getAssetAccount()).getName()
				: "";
	}

	// its not using any where

	public AccounterCoreType getType() {
		return null;
	}

}
