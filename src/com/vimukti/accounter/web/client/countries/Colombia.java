package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Colombia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Amazonas", "Antioquia", "Arauca",
				"Atlántico", "Bogotá", "Bolívar", "Boyacá", "Caldas",
				"Caquetá", "Casanare", "Cauca", "César", "Chocó", "Córdoba",
				"Cundinamarca", "Guainía", "Guaviare", "Huila", "La Guajira",
				"Magdalena", "Meta", "Nariño", "Norte de Santander",
				"Putumayo", "Quindió", "Risaralda", "San Andrés y Providencia",
				"Santander", "Sucre", "Tolima", "Valle del Cauca", "Vaupés",
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
