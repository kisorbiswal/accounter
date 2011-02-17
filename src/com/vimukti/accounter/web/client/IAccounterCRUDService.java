/**
 * 
 */
package com.vimukti.accounter.web.client;

import com.bizantra.client.data.InvaliedSessionException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.IAccounterCore;

/**
 * @author Fernandez
 * 
 */
public interface IAccounterCRUDService extends RemoteService {

	String create(IAccounterCore coreObject) throws InvalidOperationException,
			InvaliedSessionException;

	Boolean update(IAccounterCore coreObject) throws InvalidOperationException,
			InvaliedSessionException;

	Boolean delete(AccounterCoreType type, String stringID)
			throws InvalidOperationException;

	Boolean updateCompanyPreferences(ClientCompanyPreferences preferences)
			throws InvalidOperationException;

	Boolean voidTransaction(AccounterCoreType accounterCoreType, String stringID)
			throws InvalidOperationException, InvaliedSessionException;

	Boolean deleteTransaction(AccounterCoreType accounterCoreType,
			String stringID) throws InvalidOperationException,
			InvaliedSessionException;

	Boolean canEdit(AccounterCoreType accounterCoreType, String stringID)
			throws InvalidOperationException;

	Boolean updateCompany(ClientCompany clientCompany)
			throws InvalidOperationException;

}
