package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Argentina extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Buenos Aires", "Catamarca", "Chaco",
				"Chubut", "Córdoba", "Corrientes", "Distrito Federal",
				"Entre Ríos", "Formosa", "Jujuy", "La Pampa", "La Rioja",
				"Mendoza", "Misiones", "Neuquén", "Río Negro", "Salta",
				"San Juan", "San Luis", "Santa Cruz", "Santa Fé",
				"Santiago del Estero", "Tierra del Fuego", "Tucumán" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "ARP";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {

		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().january();
	}

}
