/**
 * 
 */
package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.web.client.InvalidOperationException;

/**
 * @author vimukti16
 * 
 */
@SuppressWarnings("serial")
public class FixedAssetNote extends CreatableObject implements
		IAccounterServerCore, Lifecycle {

	private String note;

	public FixedAssetNote() {
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {

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
}
