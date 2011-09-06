package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class UnitedArabEmirates extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Abu Dhabi", "'Ajman", "al-Fujayrah",
				"aš-Šariqah", "Dubai", "Ra's al-H_aymah", "Umm al-Qaywayn" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {

		return "AED";
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
