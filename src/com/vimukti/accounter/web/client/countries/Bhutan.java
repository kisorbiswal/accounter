package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Bhutan extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "BTN";
		//INR also
	}

	@Override
	public String[] getStates() {
		return new String[]{ "Bumthang", "Chhukha", "Dagana", "Gasa", "Haa", "Lhuntse",
					"Mongar", "Paro", "Pemagatshel", "Punakha",
					"Samdrup Jongkhar", "Samtse", "Sarpang", "Thimphu",
					"Trashigang", "Trashiyangtse", "Trongsa", "Tsirang",
					"Wangdue Phodrang", "Zhemgang" };
	}

}
