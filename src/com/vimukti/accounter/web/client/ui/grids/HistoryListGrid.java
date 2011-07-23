package com.vimukti.accounter.web.client.ui.grids;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFixedAssetHistory;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;
import com.vimukti.accounter.web.client.ui.fixedassets.NoteDialog;

public class HistoryListGrid extends BaseListGrid<ClientFixedAssetHistory> {

	@SuppressWarnings("unused")
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
					|| asset
							.getActionType()
							.equalsIgnoreCase(
									ClientFixedAssetHistory.ACTION_TYPE_DISPOSAL_REVERSED)) {
				return Accounter.getFixedAssetConstants()
						.viewDisposalJournal();
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
					new AsyncCallback<ClientJournalEntry>() {

						@Override
						public void onFailure(Throwable caught) {
							if (caught instanceof InvocationException) {
								Accounter
										.showMessage("Your session expired, Please login again to continue");
							} else {
								Accounter
										.showError(AccounterErrorType.FAILED_GET_JOURNALENTRIES);
							}
						}

						@Override
						public void onSuccess(ClientJournalEntry journalEntry) {
							if (journalEntry != null) {
								HistoryTokenUtils.setPresentToken(
										CompanyActionFactory
												.getNewJournalEntryAction(),
										journalEntry);
								CompanyActionFactory.getNewJournalEntryAction()
										.run(journalEntry, true);
							}

						}
					});
		}

	}

	@Override
	public boolean validateGrid() {
		return false;
	}

	@Override
	protected String[] getColumns() {
		return new String[] {
				Accounter.getFixedAssetConstants().changes(),
				Accounter.getFixedAssetConstants().date(),
				Accounter.getFixedAssetConstants().user(),
				Accounter.getFixedAssetConstants().details() };
	}

	@Override
	protected int[] setColTypes() {
		return null;
	}

	@Override
	protected void executeDelete(ClientFixedAssetHistory object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	public AccounterCoreType getType() {
		return AccounterCoreType.FIXEDASSETHISTORY;
	}

}
