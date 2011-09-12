/**
 * 
 */
package com.vimukti.accounter.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserInfo;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface IS2SServiceAsync {

	void createComapny(long companyID, String companyName, int companyType,
			ClientUser user, AsyncCallback<Void> callback);

	void deleteUserFromCompany(long companyID, String email,
			AsyncCallback<Void> callback);

	void isAdmin(long companyID, String emailID, AsyncCallback<Boolean> callback);

	public void deleteClientFromCompany(long serverCompanyId,
			String deletableEmail, AsyncCallback<Void> callback);

	void inviteUser(long companyId, ClientUserInfo userInfo,
			String senderEmailId, AsyncCallback<Boolean> callback);

	void deleteCompany(long serverCompanyID, AsyncCallback<Void> callback);

	void updateServerCompany(long serverCompanyID, String fullName,
			AsyncCallback<Void> callback);
}
