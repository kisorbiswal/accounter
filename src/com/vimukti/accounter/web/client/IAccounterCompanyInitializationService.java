/**
 * 
 */
package com.vimukti.accounter.web.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.vimukti.accounter.web.client.core.AccountsTemplate;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.TemplateAccount;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface IAccounterCompanyInitializationService extends RemoteService {

	boolean initalizeCompany(ClientCompanyPreferences preferences,
			List<TemplateAccount> accountsTemplates) throws AccounterException;

	public List<AccountsTemplate> getAccountsTemplate()
			throws AccounterException;

	public ClientCompany getCompany() throws AccounterException;
}
