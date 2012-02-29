/**
 * 
 */
package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.ClientFixedAssetNote;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.fixedassets.NoteDialog;

/**
 * @author Murali.A
 * 
 */
public class RegisteredItemsListGrid extends BaseListGrid<ClientFixedAsset> {

	private NoteDialog noteDialog;
	private final ClientCurrency currency = getCompany().getPrimaryCurrency();

	/**
	 * @param isMultiSelectionEnable
	 */
	public RegisteredItemsListGrid(boolean isMultiSelectionEnable) {
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
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 8)
			return 20;
		else if (index == 7)
			return 60;
		else if (index == 6 || index == 5)
			return 90;
		else if (index == 4 || index == 3)
			return 100;
		else if (index == 0)
			return 120;
		else if (index == 1)
			return 85;

		return super.getCellWidth(index);
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.grids.ListGrid#getColumnValue(java
	 * .lang.Object, int)
	 */
	@Override
	protected Object getColumnValue(ClientFixedAsset item, int index) {
		switch (index) {
		case 0:
			return item.getName();
		case 1:
			return item.getAssetNumber();
		case 2:
			return getCompany().getAccount(item.getAssetAccount()) != null ? Accounter
					.getCompany().getAccount(item.getAssetAccount()).getName()
					: "";
		case 3:
			return item.getPurchaseDate() != 0 ? UIUtils
					.getDateByCompanyType(new ClientFinanceDate(item
							.getPurchaseDate())) : "";
		case 4:
			return DataUtils.amountAsStringWithCurrency(
					item.getPurchasePrice(), currency);
		case 5:
			return DataUtils.amountAsStringWithCurrency(item.getBookValue(),
					currency);
		case 6:
			return messages.showHistory();
		case 7:
			return messages.addNote();
		case 8:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		}
		return "";
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
	protected void onDoubleClick(ClientFixedAsset obj, int row, int col) {
		if (!Utility.isUserHavePermissions(IAccounterCore.FIXED_ASSET)) {
			return;
		}
		switch (col) {
		case 6:
			openHistoryView(obj);
			break;
		case 7:
			openNoteDialog(obj);
			break;
		default:
			ActionFactory.getNewFixedAssetAction().run(obj, false);
			break;
		}
	}

	@Override
	protected void onClick(ClientFixedAsset obj, int row, int col) {
		if (!Utility.isUserHavePermissions(IAccounterCore.FIXED_ASSET)) {
			return;
		}
		switch (col) {
		case 8:
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

		ClientFixedAssetNote note = new ClientFixedAssetNote();
		note.setNote(value);
		asset.getFixedAssetNotes().add(note);
		Accounter.createOrUpdate(this, asset);
	}

	@Override
	protected void executeDelete(ClientFixedAsset asset) {
		deleteObject(asset);
	}

	/*
	 * @see com.vimukti.accounter.web.client.ui.grids.CustomTable#getColumns()
	 */
	@Override
	protected String[] getColumns() {
		return new String[] { messages.item(), messages.assetNumber(),
				messages.Account(), messages.purchaseDate(),
				messages.purchasePrice(), messages.bookValue(),
				messages.showHistory(), messages.addNote(), "" };
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

		case 5:
			Double bookValue1 = obj1.getBookValue();
			Double bookValue2 = obj2.getBookValue();
			return bookValue1.compareTo(bookValue2);
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
		return null;
	}

}
