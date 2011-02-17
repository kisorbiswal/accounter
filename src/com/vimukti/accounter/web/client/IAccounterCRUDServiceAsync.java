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

	void create(IAccounterCore coreObject, AsyncCallback<String> callback);

	void update(IAccounterCore coreObject, AsyncCallback<Boolean> callback);

	void delete(AccounterCoreType type, String stringID,
			AsyncCallback<Boolean> callback);

	void updateCompanyPreferences(ClientCompanyPreferences preferences,
			AsyncCallback<Boolean> callback);

	void voidTransaction(AccounterCoreType accounterCoreType, String stringID,
			AsyncCallback<Boolean> callback);

	void deleteTransaction(AccounterCoreType accounterCoreType,
			String stringID, AsyncCallback<Boolean> callback);

	void canEdit(AccounterCoreType accounterCoreType, String stringID,
			AsyncCallback<Boolean> callback);

	void updateCompany(ClientCompany clientCompany,
			AsyncCallback<Boolean> callback);

}
