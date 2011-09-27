package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Ecuador extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Azuay", "Bolivar", "Canar", "Carchi",
				"Chimborazo", "Cotopaxi", "El Oro", "Esmeraldas", "Galapagos",
				"Guayas", "Imbabura", "Loja", "Los Rios", "Manabi",
				"Morona Santiago", "Napo", "Orellana", "Pastaza", "Pichincha",
				"Sucumbios", "Tungurahua", "Zamora Chinchipe" };

		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "ECS";
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
