package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Slovakia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Banskobystricky", "Bratislavsky",
				"Kosicky", "Nitriansky", "Presovsky", "Trenciansky",
				"Trnavsky", "Zilinsky" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SKK";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().january();
	}

}
