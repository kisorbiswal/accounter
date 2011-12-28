package com.vimukti.accounter.web.client.core;

public class ClientFixedAssetNote implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int version;
	private ClientFixedAsset fixedAsset;
	String note;

	public ClientFixedAssetNote() {
	}

	@Override
	public String getDisplayName() {
		return "ClientFixedAssetNote";
	}

	@Override
	public String getName() {
		return "ClientFixedAssetNote";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.FIXEDASSETNOTE;
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
		this.version = version;

	}

	public ClientFixedAsset getFixedAsset() {
		return fixedAsset;
	}

	public void setFixedAsset(ClientFixedAsset fixedAsset) {
		this.fixedAsset = fixedAsset;
	}

	@Override
	public void setID(long id) {
	}

	@Override
	public long getID() {
		return 0;
	}
}
