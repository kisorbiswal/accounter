package com.vimukti.accounter.setup.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.vimukti.accounter.setup.client.core.AccountDetails;
import com.vimukti.accounter.setup.client.core.DatabaseConnection;

public interface ISetupService extends RemoteService {

	String testDBConnection(DatabaseConnection conn) throws Exception;

	Boolean saveDBConnection(DatabaseConnection conn) throws Exception;

	String getRandomServerID();

	boolean verifyLicense(String serverID, String license);

	boolean saveAccountDetails(AccountDetails accountDetails) throws Exception;

}
