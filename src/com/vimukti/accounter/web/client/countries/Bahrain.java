package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.ICountryPreferences;
import com.vimukti.accounter.web.client.util.OrganizationType;

public class Bahrain implements ICountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "al-Garbiyah", "al-H?idd",
				"al-Muh?arraq", "al-Wusta", "ar-Rifa'a", "aš-Šamaliyah",
				"'Isa", "Jidh?afs", "Madinat H?amad", "Manama", "Sitrah" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultTimeZone(String state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganizationType[] getOrganizationTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		// TODO Auto-generated method stub
		return null;
	}

}
