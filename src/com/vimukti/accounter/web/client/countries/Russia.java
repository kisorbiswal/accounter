package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Russia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Adygeja", "Aga", "Alanija", "Altaj",
				"Amur", "Arhangelsk", "Astrahan", "Baskortostan", "Belgorod",
				"Brjansk", "Burjatija", "Cecenija", "Celjabinsk", "Cita",
				"Cukotka", "Cuvasija", "Dagestan", "Evenkija", "Gorno-Altaj",
				"Habarovsk", "Hakasija", "Hanty-Mansija", "Ingusetija",
				"Irkutsk", "Ivanovo", "Jamalo-Nenets", "Jaroslavl", "Jevrej",
				"Kabardino-Balkarija", "Kaliningrad", "Kalmykija", "Kaluga",
				"Kamcatka", "Karacaj-Cerkessija", "Karelija", "Kemerovo",
				"Kirov", "Komi", "Komi-Permjak", "Korjakija", "Kostroma",
				"Krasnodar", "Krasnojarsk", "Kurgan", "Kursk", "Leningrad",
				"Lipeck", "Magadan", "Marij El", "Mordovija", "Moskau",
				"Moskovskaja Oblast", "Murmansk", "Nenets", "Niznij Novgorod",
				"Novgorod", "Novosibirsk", "Omsk", "Orenburg", "Orjol",
				"Penza", "Perm", "Primorje", "Pskov", "Rjazan", "Rostov",
				"Saha", "Sahalin", "Samara", "Sankt Petersburg", "Saratov",
				"Smolensk", "Stavropol", "Sverdlovsk", "Tajmyr", "Tambov",
				"Tatarstan", "Tjumen", "Tomsk", "Tula", "Tver", "Tyva",
				"Udmurtija", "Uljanovsk", "Ust-Orda", "Vladimir", "Volgograd",
				"Vologda", "Voronez" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "RUR";
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
