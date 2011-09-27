package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Paraguay extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Alto Paraguay", "Alto Parana",
				"Amambay", "Asuncion", "Boqueron", "Caaguazu", "Caazapa",
				"Canendiyu", "Central", "Concepcion", "Cordillera", "Guaira",
				"Itapua", "Misiones", "Neembucu", "Paraguari",
				"Presidente Hayes", "San Pedro" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "PYG";
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
