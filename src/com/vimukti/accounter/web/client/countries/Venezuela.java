package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Venezuela extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Amazonas", "Anzoategui", "Apure",
				"Aragua", "Barinas", "Bolivar", "Carabobo", "Cojedes",
				"Delta Amacuro", "Distrito Capital", "Falcon", "Guarico",
				"Lara", "Merida", "Miranda", "Monagas", "Nueva Esparta",
				"Portuguesa", "Sucre", "Tachira", "Trujillo", "Vargas",
				"Yaracuy", "Zulia" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "VEB";
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
