package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Rwanda extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Butare", "Byumba", "Cyangugu", "Gikongoro",
				"Gisenyi", "Gitarama", "Kibungo", "Kibuye", "Ruhengeri",
				"Ville de Kigali" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "RWF";
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
