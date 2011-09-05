package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.ICountryPreferences;
import com.vimukti.accounter.web.client.util.OrganizationType;

public class Italy implements ICountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Abruzzen", "Apulien", "Basilicata",
				"Calabria", "Campania", "Emilia-Romagna",
				"Friuli-Venezia Giulia", "Lazio", "Ligurien", "Lombardei",
				"Marken", "Molise", "Piemonte", "Sardinien", "Sizilien",
				"Toscana", "Trentino-Alto Adige", "Umbria", "Valle d'Aosta",
				"Veneto" };
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
