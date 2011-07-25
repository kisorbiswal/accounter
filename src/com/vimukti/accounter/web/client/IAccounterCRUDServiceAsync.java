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

	void delete(AccounterCoreType type, String id,
			AsyncCallback<Boolean> callback);

	void updateCompanyPreferences(ClientCompanyPreferences preferences,
			AsyncCallback<Long> callback);

	void voidTransaction(AccounterCoreType accounterCoreType, String id,
			AsyncCallback<Boolean> callback);

	void deleteTransaction(AccounterCoreType accounterCoreType,
			String id, AsyncCallback<Boolean> callback);

	void canEdit(AccounterCoreType accounterCoreType, String id,
			AsyncCallback<Boolean> callback);

	void updateCompany(ClientCompany clientCompany, AsyncCallback<Long> callback);

}
