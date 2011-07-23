/**
 * 
 */
package com.vimukti.accounter.web.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.data.InvalidSessionException;

/**
 * @author Fernandez
 * 
 */
public interface IAccounterCRUDService extends RemoteService {

	String create(IAccounterCore coreObject) throws InvalidOperationException,
			InvalidSessionException;

	Boolean update(IAccounterCore coreObject) throws InvalidOperationException,
			InvalidSessionException;

	Boolean delete(AccounterCoreType type, String id)
			throws InvalidOperationException;

	Boolean updateCompanyPreferences(ClientCompanyPreferences preferences)
			throws InvalidOperationException;

	Boolean voidTransaction(AccounterCoreType accounterCoreType, String id)
			throws InvalidOperationException, InvalidSessionException;

	Boolean deleteTransaction(AccounterCoreType accounterCoreType,
			String id) throws InvalidOperationException,
			InvalidSessionException;

	Boolean canEdit(AccounterCoreType accounterCoreType, String id)
			throws InvalidOperationException;

	Boolean updateCompany(ClientCompany clientCompany)
			throws InvalidOperationException;

}
