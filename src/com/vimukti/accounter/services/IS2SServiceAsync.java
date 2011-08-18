/**
 * 
 */
package com.vimukti.accounter.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface IS2SServiceAsync {
	public void createComapny(long companyID, String companyName,
			int companyType, ClientUser user, AsyncCallback<Void> callback)
			throws AccounterException;

	public boolean isAdmin(long companyID, String emailID,
			AsyncCallback<Boolean> callback);

	public void deleteUserFromCompany(String email, AsyncCallback<Void> callback);
}
