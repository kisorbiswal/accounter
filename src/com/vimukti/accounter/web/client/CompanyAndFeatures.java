package com.vimukti.accounter.web.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.IAccounterCore;

public class CompanyAndFeatures implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ClientCompany clientCompany;

	private ArrayList<String> features = new ArrayList<String>();
	/**
	 * @return the clientCompany
	 */
	public ClientCompany getClientCompany() {
		return clientCompany;
	}

	/**
	 * @param clientCompany
	 *            the clientCompany to set
	 */
	public void setClientCompany(ClientCompany clientCompany) {
		this.clientCompany = clientCompany;
	}

	/**
	 * @return the features
	 */
	public ArrayList<String> getFeatures() {
		return features;
	}

	/**
	 * @param features
	 *            the features to set
	 */
	public void setFeatures(ArrayList<String> features) {
		this.features = features;
	}

}
