package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Honduras extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Atlántida", "Choluteca", "Colón",
				"Comayagua", "Copán", "Cortés", "Distrito Central",
				"El Paraíso", "Francisco Morazán", "Gracias a Dios",
				"Intibucá", "Islas de la Bahía", "La Paz", "Lempira",
				"Ocotepeque", "Olancho", "Santa Bárbara", "Valle", "Yoro" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "HNL";
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
