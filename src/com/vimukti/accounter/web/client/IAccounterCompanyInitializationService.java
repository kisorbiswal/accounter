/**
 * 
 */
package com.vimukti.accounter.web.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface IAccounterCompanyInitializationService extends RemoteService {

	ClientCompany createCompany(int accountType) throws AccounterException;

}
