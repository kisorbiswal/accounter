/**
 * 
 */
package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * @author vimukti16
 * 
 */
@SuppressWarnings("serial")
public class FixedAssetNote extends CreatableObject implements
		IAccounterServerCore {

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
			throws AccounterException {

		return true;
	}

}
