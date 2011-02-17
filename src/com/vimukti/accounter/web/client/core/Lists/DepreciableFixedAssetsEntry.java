package com.vimukti.accounter.web.client.core.Lists;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class DepreciableFixedAssetsEntry implements IsSerializable,
		Serializable {

	String stringID;
	String fixedAssetName;
	double amountToBeDepreciated;

	public String getStringID() {
		return stringID;
	}

	public void setStringID(String stringID) {
		this.stringID = stringID;
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

}
