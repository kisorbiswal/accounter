package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.ICountryPreferences;
import com.vimukti.accounter.web.client.util.OrganizationType;

public class Botswana implements ICountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Bobonong", "Boteti", "Chobe",
				"Francistown", "Gaborone", "Ghanzi", "Jwaneng",
				"Kgalagadi North", "Kgalagadi South", "Kgatleng", "Kweneng",
				"Lobatse", "Mahalapye", "Ngamiland", "Ngwaketse", "North East",
				"Okavango", "Orapa", "Selibe Phikwe", "Serowe-Palapye",
				"South East", "Sowa", "Tutume" };
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
