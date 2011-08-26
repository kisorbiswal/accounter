package com.vimukti.accounter.web.client.core;

public class ClientFixedAssetNote implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String note;
	private long id;

	private int version;

	public ClientFixedAssetNote() {
	}

	@Override
	public String getClientClassSimpleName() {
		return null;
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
		return AccounterCoreType.FIXEDASSETNOTE;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public ClientFixedAssetNote clone() {
		ClientFixedAssetNote fixedAssetNote = (ClientFixedAssetNote) this
				.clone();
		return fixedAssetNote;

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
