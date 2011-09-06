package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Italy extends AbstractCountryPreferences {

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
		return "EUR";
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
