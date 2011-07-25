package com.vimukti.accounter.web.client.core.Lists;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LinkAccount implements IsSerializable,Serializable{

	long assetAccount;

	String linkedAccount;

	public long getAssetAccount() {
		return assetAccount;
	}

	public void setAssetAccount(long assetAccount) {
		this.assetAccount = assetAccount;
	}

	public String getLinkedAccount() {
		return linkedAccount;
	}

	public void setLinkedAccount(String linkedAccount) {
		this.linkedAccount = linkedAccount;
	}



}
