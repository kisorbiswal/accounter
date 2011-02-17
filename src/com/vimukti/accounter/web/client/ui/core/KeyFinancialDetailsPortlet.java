package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.Portlet;

public class KeyFinancialDetailsPortlet extends Portlet {

	public KeyFinancialDetailsPortlet() {
		super(FinanceApplication.getCompanyMessages().keyFinancialIndicators());
	}

	@Override
	public void linkClicked() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshClicked() {
		// TODO Auto-generated method stub

	}

}
