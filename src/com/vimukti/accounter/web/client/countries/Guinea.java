package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Guinea extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "GNS";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Basse Guinée", "Conakry", "Guinée Forestière",
				"Haute Guinée", "Moyenne Guinée" };
	}

}
