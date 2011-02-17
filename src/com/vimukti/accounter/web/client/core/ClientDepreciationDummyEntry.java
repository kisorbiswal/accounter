package com.vimukti.accounter.web.client.core;

public class ClientDepreciationDummyEntry {

	private ClientAccount account;
	private String fixedAssetName;
	private double amountToBeDepreciated;
	private String assetAccount;

	public ClientAccount getAccount() {
		return account;
	}

	public void setAccount(ClientAccount account) {
		this.account = account;
	}

	public String getFixedAssetName() {
		return fixedAssetName;
	}

	public void setFixedAssetName(String fixedAssetName) {
		this.fixedAssetName = fixedAssetName;
	}

	public double getAmountToBeDepreciated() {
		return amountToBeDepreciated;
	}

	public void setAmountToBeDepreciated(double amountToBeDepreciated) {
		this.amountToBeDepreciated = amountToBeDepreciated;
	}

	public String getAssetAccount() {
		return assetAccount;
	}

	public void setAssetAccount(String assetAccount) {
		this.assetAccount = assetAccount;
	}

}
