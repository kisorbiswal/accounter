package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Qatar extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "al-Guwayriyah", "al-H_awr",
				"al-Jumayliyah", "al-Wakrah", "ar-Rayyan", "as-Samal", "Doha",
				"Jariyan al-Batnah", "Musay'id", "Umm Salal" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {

		return "QAR";
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
