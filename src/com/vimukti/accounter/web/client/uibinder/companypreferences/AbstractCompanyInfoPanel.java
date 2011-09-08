package com.vimukti.accounter.web.client.uibinder.companypreferences;

import com.google.gwt.user.client.ui.Composite;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class AbstractCompanyInfoPanel extends Composite {
	protected static ClientCompanyPreferences companyPreferences = Global.get()
			.preferences();
	protected static ClientCompany company = Accounter.getCompany();
	protected AccounterConstants constants = Accounter.constants();
	protected AccounterMessages messages = Accounter.messages();

	public abstract void onSave();

	public abstract void validate();
}
