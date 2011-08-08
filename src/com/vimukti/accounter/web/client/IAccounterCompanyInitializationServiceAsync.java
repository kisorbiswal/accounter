/**
 * 
 */
package com.vimukti.accounter.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface IAccounterCompanyInitializationServiceAsync {

	void initalizeCompany(ClientCompanyPreferences preferences,
			AsyncCallback<Boolean> callback);
}
