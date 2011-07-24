package com.vimukti.accounter.web.client.core.Lists;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class DepreciableFixedAssetsList implements IsSerializable, Serializable {

	Map<String, List<DepreciableFixedAssetsEntry>> accountViceFixedAssets;
	List<String> fixedAssetIDs;

	public DepreciableFixedAssetsList() {
	}

	// public Map<String, Double> getAccountViceFixedAssets() {
	// return accountViceFixedAssets;
	// }
	//
	// public void setAccountViceFixedAssets(
	// Map<String, Double> accountViceFixedAssets) {
	// this.accountViceFixedAssets = accountViceFixedAssets;
	// }

	public Map<String, List<DepreciableFixedAssetsEntry>> getAccountViceFixedAssets() {
		return accountViceFixedAssets;
	}

	public void setAccountViceFixedAssets(
			Map<String, List<DepreciableFixedAssetsEntry>> accountViceFixedAssets) {
		this.accountViceFixedAssets = accountViceFixedAssets;
	}

	public List<String> getFixedAssetIDs() {
		return fixedAssetIDs;
	}

	public void setFixedAssetIDs(List<String> fixedAssetIDs) {
		this.fixedAssetIDs = fixedAssetIDs;
	}

}
