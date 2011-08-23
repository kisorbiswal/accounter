package com.vimukti.accounter.web.client;

import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public interface IGlobal {

	public AccounterConstants constants();
	
	public AccounterMessages messages();

	public ClientCompanyPreferences preferences();

	public String Customer();

	public String customer();

	public String Account();

	public String account();

	public String Vendor();

	public String vendor();

}
