/**
 * 
 */
package com.vimukti.accounter.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCompany;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface IAccounterCompanyInitializationServiceAsync {

	void createCompany(int accountType, AsyncCallback<ClientCompany> callback);

}
