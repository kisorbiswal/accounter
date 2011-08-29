package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class AbstractCompanyInfoPanel extends VerticalPanel {
	protected static ClientCompanyPreferences companyPreferences;
	protected static ClientCompany company;
	protected static AbstractBaseView<ClientCompanyPreferences> view;
	protected AccounterConstants constants = Accounter.constants();
	protected AccounterMessages messages = Accounter.messages();

	public AbstractCompanyInfoPanel(ClientCompanyPreferences preferences,
			ClientCompany clientCompany,
			AbstractBaseView<ClientCompanyPreferences> baseView) {
		companyPreferences = preferences;
		view = baseView;
		company = clientCompany;
	}

	public AbstractCompanyInfoPanel() {
		this(companyPreferences, company, view);
	}

	public abstract void onLoad();

	public abstract void onSave();
}
