package com.vimukti.accounter.web.client;

import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;

public interface IGlobal {

	public AccounterConstants constants();

	public ClientCompanyPreferences preferences();

	public String Customer();

	public String customer();

	public String Account();

	public String account();

	public String Supplier();

	public String supplier();

}
