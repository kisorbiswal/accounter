package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.ICountryPreferences;
import com.vimukti.accounter.web.client.util.OrganizationType;

public class BahamasThe implements ICountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Abaco", "Acklins Island", "Andros",
				"Berry Islands", "Biminis", "Cat Island", "Crooked Island",
				"Eleuthera", "Exuma and Cays", "Grand Bahama",
				"Inagua Islands", "Long Island", "Mayaguana", "New Providence",
				"Ragged Island", "Rum Cay", "San Salvador" };
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
