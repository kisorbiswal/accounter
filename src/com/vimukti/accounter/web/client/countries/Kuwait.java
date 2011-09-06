package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Kuwait extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "al-Ah?madi", "al-'Asamah",
				"al-Farwaniyah", "al-Jahra'", "Hawalli", "Mubarak al-Kabir" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "KWD";
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
