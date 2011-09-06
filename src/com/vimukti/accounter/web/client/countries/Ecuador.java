package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Ecuador extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Azuay", "Bolívar", "Cañar", "Carchi",
				"Chimborazo", "Cotopaxi", "El Oro", "Esmeraldas", "Galápagos",
				"Guayas", "Imbabura", "Loja", "Los Ríos", "Manabí",
				"Morona Santiago", "Napo", "Orellana", "Pastaza", "Pichincha",
				"Sucumbíos", "Tungurahua", "Zamora Chinchipe" };

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
