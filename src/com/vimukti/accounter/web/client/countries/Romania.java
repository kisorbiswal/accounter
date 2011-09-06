package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Romania extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Alba", "Arad", "Arges", "Bacau",
				"Bihor", "Bistrita-Nasaud", "Botosani", "Braila", "Brasov",
				"Bukarest", "Buzau", "Calarasi", "Caras-Severin", "Cluj",
				"Constanta", "Covasna", "Dâmbovita", "Dolj", "Galati",
				"Giurgiu", "Gorj", "Harghita", "Hunedoara", "Ialomita", "Iasi",
				"Ilfov", "Maramures", "Mehedinti", "Mures", "Neamt", "Olt",
				"Prahova", "Salaj", "Satu Mare", "Sibiu", "Suceava",
				"Teleorman", "Timis", "Tulcea", "Vâlcea", "Vaslui", "Vrancea" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "ROL";
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
