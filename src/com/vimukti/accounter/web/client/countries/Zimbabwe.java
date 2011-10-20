package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Zimbabwe extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "ZWD";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Bulawayo", "Harare", "Manicaland",
				"Mashonaland Central", "Mashonaland East", "Mashonaland West",
				"Masvingo", "Matabeleland North", "Matabeleland South",
				"Midlands" };
	}

}
