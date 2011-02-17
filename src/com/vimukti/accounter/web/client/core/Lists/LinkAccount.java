package com.vimukti.accounter.web.client.core.Lists;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LinkAccount implements IsSerializable,Serializable{

	String assetAccount;

	String linkedAccount;

	public String getAssetAccount() {
		return assetAccount;
	}

	public void setAssetAccount(String assetAccount) {
		this.assetAccount = assetAccount;
	}

	public String getLinkedAccount() {
		return linkedAccount;
	}

	public void setLinkedAccount(String linkedAccount) {
		this.linkedAccount = linkedAccount;
	}



}
