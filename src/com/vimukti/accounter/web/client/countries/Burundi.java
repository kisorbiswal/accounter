package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Burundi extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "BIF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Bubanza", "Bujumbura", "Bururi", "Cankuzo",
				"Cibitoke", "Gitega", "Karuzi", "Kayanza", "Kirundo",
				"Makamba", "Muramvya", "Muyinga", "Ngozi", "Rutana", "Ruyigi" };
	}

}
