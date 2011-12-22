package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class Mexico extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Aguascalientes", "Baja California",
				"Baja California Sur", "Campeche", "Chiapas", "Chihuahua",
				"Coahuila", "Colima", "Distrito Federal", "Durango",
				"Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Mexico",
				"Michoacan", "Morelos", "Nayarit", "Nuevo Leon", "Oaxaca",
				"Puebla", "Queretaro", "Quintana Roo", "San Luis Potosi",
				"Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala",
				"Veracruz", "Yucatan", "Zacatecas" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MXP";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.january();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		// America/Mexico_City Central Time - most locations
		// America/Cancun Central Time - Quintana Roo
		// America/Merida Central Time - Campeche, Yucatan
		// America/Monterrey Mexican Central Time - Coahuila, Durango, Nuevo
		// Leon, Tamaulipas away from US border
		// America/Matamoros US Central Time - Coahuila, Durango, Nuevo Leon,
		// Tamaulipas near US border
		// America/Mazatlan Mountain Time - S Baja, Nayarit, Sinaloa
		// America/Chihuahua Mexican Mountain Time - Chihuahua away from US
		// border
		// America/Ojinaga US Mountain Time - Chihuahua near US border
		// America/Hermosillo Mountain Standard Time - Sonora
		// America/Tijuana US Pacific Time - Baja California near US border
		// America/Santa_Isabel Mexican Pacific Time - Baja California away from
		// US border
		// America/Bahia_Banderas Mexican Central Time - Bahia de Banderas
		return null;
	}

}
