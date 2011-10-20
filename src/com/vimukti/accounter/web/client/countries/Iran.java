package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Iran extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "IRR";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Ardabil", "Azarbayejan-e Gharbi",
				"Azarbayejan-e Sharqi", "Bushehr", "Chahar Mahal-e Bakhtiari",
				"Esfahan", "Fars", "Gilan", "Golestan", "Hamadan", "Hormozgan",
				"Ilam", "Kerman", "Kermanshah", "Khorasan-e Razavi",
				"Khorasan Janubi", "Khorasan Shamali", "Khuzestan",
				"Kohgiluyeh-e Boyerahmad", "Kordestan", "Lorestan", "Markazi",
				"Mazandaran", "Qazvin", "Qom", "Semnan",
				"Sistan-e Baluchestan", "Teheran", "Yazd", "Zanjan" };
	}

}
