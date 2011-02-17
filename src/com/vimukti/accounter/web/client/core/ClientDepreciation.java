package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientDepreciation implements IAccounterCore {

	public static final int METHOD_STRAIGHT_LINE = 1;
	public static final int METHOD_REDUCING_BALANCE = 2;

	public static final int APPROVE = 1;
	public static final int ROLLBACK = 2;

	public static final int DEPRECIATION_FOR_ALL_FIXEDASSET = 1;
	public static final int DEPRECIATION_FOR_SINGLE_FIXEDASSET = 2;

	long id;

	private String stringID;

	int status;

	long depreciateFrom;

	long depreciateTo;

	// List<ClientFixedAsset> fixedAssets;
	ClientFixedAsset fixedAsset;

	int depreciationFor;

	// FixedAssetLinkedAccountMap linkedAccounts;

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
		return AccounterCoreType.DEPRECIATION;
	}

	@Override
	public String getStringID() {
		return stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;
	}

	public ClientFixedAsset getFixedAsset() {
		return fixedAsset;
	}

	public void setFixedAsset(ClientFixedAsset fixedAsset) {
		this.fixedAsset = fixedAsset;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getDepreciateFrom() {
		return depreciateFrom;
	}

	public void setDepreciateFrom(long depreciateFrom) {
		this.depreciateFrom = depreciateFrom;
	}

	public long getDepreciateTo() {
		return depreciateTo;
	}

	public void setDepreciateTo(long depreciateTo) {
		this.depreciateTo = depreciateTo;
	}

	public int getDepreciationFor() {
		return depreciationFor;
	}

	public void setDepreciationFor(int depreciationFor) {
		this.depreciationFor = depreciationFor;
	}

}
