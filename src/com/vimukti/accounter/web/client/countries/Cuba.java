package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Cuba extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "CUP";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Camagüey", "Ciego de Ávila", "Cienfuegos",
				"Ciudad de la Habana", "Granma", "Guantánamo", "Holguín",
				"Isla de la Juventud", "La Habana", "Las Tunas", "Matanzas",
				"Pinar del Río", "Sancti Spíritus", "Santiago de Cuba",
				"Villa Clara" };
	}

}
