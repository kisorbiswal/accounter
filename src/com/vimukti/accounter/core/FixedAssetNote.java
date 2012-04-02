/**
 * 
 */
package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

/**
 * @author vimukti16
 * 
 */
public class FixedAssetNote extends CreatableObject implements
		IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String note;

	private FixedAsset fixedAsset;

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
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {

		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.fixedAssetNote()).gap();
	}

	public FixedAsset getFixedAsset() {
		return fixedAsset;
	}

	public void setFixedAsset(FixedAsset fixedAsset) {
		this.fixedAsset = fixedAsset;
	}

}
