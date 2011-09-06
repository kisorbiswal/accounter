package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class HongKong extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Hongkong", "Kowloon and New Kowl" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "HKD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().april();
	}

}
