package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Cameroon extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "XAF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Adamaoua", "Centre", "Est", "Littoral", "Nord",
				"Nord Extrème", "Nordouest", "Ouest", "Sud", "Sudouest" };
	}

}
