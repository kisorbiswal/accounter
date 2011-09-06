package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class ElSalvador extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Ahuachapán", "Cabañas",
				"Chalatenango", "Cuscatlán", "La Libertad", "La Paz",
				"La Unión", "Morazán", "San Miguel", "San Salvador",
				"Santa Ana", "San Vicente", "Sonsonate", "Usulután" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SVC";
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
