package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Colombia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Amazonas", "Antioquia", "Arauca",
				"Atlantico", "Bogota", "Bolivar", "Boyaca", "Caldas",
				"Caqueta", "Casanare", "Cauca", "Cesar", "Choco", "Cordoba",
				"Cundinamarca", "Guainia", "Guaviare", "Huila", "La Guajira",
				"Magdalena", "Meta", "Narino", "Norte de Santander",
				"Putumayo", "Quindio", "Risaralda", "San Andres y Providencia",
				"Santander", "Sucre", "Tolima", "Valle del Cauca", "Vaupes",
				"Vichada" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "COP";
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
