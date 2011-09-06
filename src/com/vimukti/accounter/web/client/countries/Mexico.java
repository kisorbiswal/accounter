package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Mexico extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Aguascalientes", "Baja California",
				"Baja California Sur", "Campeche", "Chiapas", "Chihuahua",
				"Coahuila", "Colima", "Distrito Federal", "Durango",
				"Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "México",
				"Michoacán", "Morelos", "Nayarit", "Nuevo León", "Oaxaca",
				"Puebla", "Querétaro", "Quintana Roo", "San Luis Potosí",
				"Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala",
				"Veracruz", "Yucatán", "Zacatecas" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MXP";
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
