package com.vimukti.accounter.web.client.core.Lists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FixedAssetLinkedAccountMap implements IsSerializable,Serializable {

	List<LinkAccount> assetLinkedAccounts = new ArrayList<LinkAccount>();

	public List<LinkAccount> getFixedAssetLinkedAccounts() {
		return assetLinkedAccounts;
	}

	public void setFixedAssetLinkedAccounts(
			List<LinkAccount> fixedAssetLinkedAccounts) {
		this.assetLinkedAccounts = fixedAssetLinkedAccounts;
	}

	public Set<String> keySet() {
		Set<String> keySet = new HashSet<String>();
		for (LinkAccount linkAccount : assetLinkedAccounts) {
			keySet.add(linkAccount.assetAccount);
		}
		return keySet;
	}

	public String get(String account) {
		for (LinkAccount linkAccount : assetLinkedAccounts) {
			if (linkAccount.assetAccount.equals(account)) {
				return linkAccount.linkedAccount;
			}
		}
		return null;
	}


}
