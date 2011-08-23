package com.vimukti.accounter.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.IAccounterCore;

/**
 * 
 * @author Fernandez
 * 
 */
public interface IAccounterCRUDServiceAsync {

	void create(IAccounterCore coreObject, AsyncCallback<Long> callback);

	void update(IAccounterCore coreObject, AsyncCallback<Long> callback);

	void delete(AccounterCoreType type, long id, AsyncCallback<Boolean> callback);

	void updateCompanyPreferences(ClientCompanyPreferences preferences,
			AsyncCallback<Boolean> callback);

	void voidTransaction(AccounterCoreType accounterCoreType, long id,
			AsyncCallback<Boolean> callback);

	void deleteTransaction(AccounterCoreType accounterCoreType, long id,
			AsyncCallback<Boolean> callback);

	void canEdit(AccounterCoreType accounterCoreType, long id,
			AsyncCallback<Boolean> callback);

	void updateCompany(ClientCompany clientCompany, AsyncCallback<Long> callback);

	void inviteUser(IAccounterCore coreObject, AsyncCallback<Long> callback);

	void updateUser(IAccounterCore coreObject, AsyncCallback<Long> callback);

	void deleteUser(IAccounterCore deletableUser, String senderEmail,
			AsyncCallback<Boolean> callback);

}
