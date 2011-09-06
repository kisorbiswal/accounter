package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Latvia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Aizkraukles", "Aluksnes", "Balvu",
				"Bauskas", "Cesu", "Daugavpils", "Daugavpils pilseta",
				"Dobeles", "Gulbenes", "Jekabspils", "Jelgava", "Jelgavas",
				"Jurmala pilseta", "Kraslavas", "Kuldigas", "Liepaja",
				"Liepajas", "Limbažu", "Ludzas", "Madonas", "Ogres", "Preilu",
				"Rezekne", "Rezeknes", "Riga", "Rigas", "Saldus", "Talsu",
				"Tukuma", "Valkas", "Valmieras", "Ventspils",
				"Ventspils pilseta" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "LVL";
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
