package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.ICountryPreferences;
import com.vimukti.accounter.web.client.util.OrganizationType;

public class Angola implements ICountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Bengo", "Benguela", "Bié", "Cabinda",
				"Cuando-Cubango", "Cuanza-Norte", "Cuanza-Sul", "Cunene",
				"Huambo", "Huíla", "Luanda", "Lunda Norte", "Lunda Sul",
				"Malanje", "Moxico", "Namibe", "Uíge", "Zaire" };
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
