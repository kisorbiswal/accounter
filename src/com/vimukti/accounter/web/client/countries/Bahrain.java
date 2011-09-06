package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Bahrain extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "al-Garbiyah", "al-H?idd",
				"al-Muh?arraq", "al-Wusta", "ar-Rifa'a", "aš-Šamaliyah",
				"'Isa", "Jidh?afs", "Madinat H?amad", "Manama", "Sitrah" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "BHD";
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
