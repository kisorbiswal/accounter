package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFixedAssetHistory;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.fixedassets.NoteDialog;

public class HistoryListGrid extends BaseListGrid<ClientFixedAssetHistory> {

	private NoteDialog noteDialog;

	public HistoryListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int getColumnType(int col) {
		if (col == 3) {
			String opration = ((ClientFixedAssetHistory) selectedObject)
					.getActionType();
			if (opration
					.equalsIgnoreCase(ClientFixedAssetHistory.ACTION_TYPE_DISPOSED)
					|| opration
							.equalsIgnoreCase(ClientFixedAssetHistory.ACTION_TYPE_DISPOSAL_REVERSED)) {
				return ListGrid.COLUMN_TYPE_LINK;
			}
		}

		return ListGrid.COLUMN_TYPE_TEXT;
	}

	@Override
	protected Object getColumnValue(ClientFixedAssetHistory asset, int index) {
		switch (index) {
		case 0:
			return asset.getActionType();
		case 1:
			return UIUtils.getDateByCompanyType(new ClientFinanceDate(asset
					.getActionDate()));
		case 2:
			return asset.getUser() != null ? asset.getUser() : "";
		case 3:
			if (asset.getActionType().equalsIgnoreCase(
					ClientFixedAssetHistory.ACTION_TYPE_DISPOSED)
					|| asset.getActionType()
							.equalsIgnoreCase(
									ClientFixedAssetHistory.ACTION_TYPE_DISPOSAL_REVERSED)) {
				return Accounter.constants().viewDisposalJournal();
			} else {
				return asset.getDetails() != null ? asset.getDetails() : "";
			}
		}
		return "";
	}

	@Override
	public void onDoubleClick(ClientFixedAssetHistory obj) {

	}

	@Override
	protected void onClick(ClientFixedAssetHistory asset, int row, int col) {
		if (col == 3
				&& asset.getActionType().equalsIgnoreCase(
						ClientFixedAssetHistory.ACTION_TYPE_DISPOSED)
				|| asset.getActionType().equalsIgnoreCase(
						ClientFixedAssetHistory.ACTION_TYPE_DISPOSAL_REVERSED)) {
			Accounter.createGETService().getObjectById(
					AccounterCoreType.JOURNALENTRY,
					asset.getPostedJournalEntry(),
					new AccounterAsyncCallback<ClientJournalEntry>() {

						@Override
						public void onException(AccounterException caught) {
							Accounter
									.showError(Accounter.constants().failedGetJournalEntries());
						}

						@Override
						public void onResultSuccess(ClientJournalEntry journalEntry) {
							if (journalEntry != null) {
								ActionFactory.getNewJournalEntryAction().run(
										journalEntry, true);
							}

						}
					});
		}

	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.constants().changes(),
				Accounter.constants().date(), Accounter.constants().user(),
				Accounter.constants().details() };
	}

	@Override
	protected int[] setColTypes() {
		return null;
	}

	@Override
	protected void executeDelete(ClientFixedAssetHistory object) {
		// NOTHING TO DO
	}


	public AccounterCoreType getType() {
		return AccounterCoreType.FIXEDASSETHISTORY;
	}

}
