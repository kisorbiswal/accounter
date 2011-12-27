package com.vimukti.accounter.web.client.core;

import com.vimukti.accounter.web.client.ui.Accounter;

public class ClientFixedAssetHistory implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	public static final String ACTION_TYPE_DISPOSED = Accounter.messages().disposed();
	public static final String ACTION_TYPE_DISPOSAL_REVERSED = Accounter.messages().disposalreversed();


	String actionType;
	long actionDate;
	String details;
	String user;
	long postedJournalEntry;

	private int version;

	public ClientFixedAssetHistory() {
	}


	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.FIXEDASSETHISTORY;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public long getPostedJournalEntry() {
		return postedJournalEntry;
	}

	public void setPostedJournalEntry(long postedJournalEntry) {
		this.postedJournalEntry = postedJournalEntry;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public long getActionDate() {
		return actionDate;
	}

	public void setActionDate(long actionDate) {
		this.actionDate = actionDate;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public ClientFixedAssetHistory clone() {
		ClientFixedAssetHistory fixedAssetHistory = (ClientFixedAssetHistory) this
				.clone();
		return fixedAssetHistory;

	}

	@Override
	public int getVersion() {
		
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version=version;
	}

}
