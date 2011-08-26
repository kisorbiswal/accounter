package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class FixedAssetHistory implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	public static final String ACTION_TYPE_NONE = "NONE";
	public static final String ACTION_TYPE_STATUS = "Status";
	public static final String ACTION_TYPE_ROLLBACK = "Rollback";
	public static final String ACTION_TYPE_CREATED = "Created";
	public static final String ACTION_TYPE_DELETED = "Deleted";
	public static final String ACTION_TYPE_REGISTERED = "Registered";
	public static final String ACTION_TYPE_SOLD = "Sold";
	public static final String ACTION_TYPE_DISPOSED = "Disposed";
	public static final String ACTION_TYPE_DISPOSAL_REVERSED = "Disposal reversed";
	public static final String ACTION_TYPE_DEPRECIATED = "Depreciation";
	public static final String ACTION_TYPE_NOTE = "Note";

	String actionType;
	FinanceDate actionDate;
	String details;

	String user;
	JournalEntry postedJournalEntry;

	private int version;

	public FixedAssetHistory() {

	}

	public long getID() {
		return this.id;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public FinanceDate getActionDate() {
		return actionDate;
	}

	public void setActionDate(FinanceDate actionDate) {
		this.actionDate = actionDate;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public JournalEntry getPostedJournalEntry() {
		return postedJournalEntry;
	}

	public void setPostedJournalEntry(JournalEntry postedJournalEntry) {
		this.postedJournalEntry = postedJournalEntry;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		// NOTHING TO DO
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// NOTHING TO DO
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		// NOTHING TO DO
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
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
