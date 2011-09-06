package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class DominicanRepublic extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Azua", "Baoruco", "Barahona",
				"Dajabón", "Duarte", "Elías Piña", "El Seibo", "Espaillat",
				"Hato Mayor", "Independencia", "La Altagracia", "La Romana",
				"La Vega", "María Trinidad Sánchez", "Monseñor Nouel",
				"Monte Cristi", "Monte Plata", "Pedernales", "Peravia",
				"Puerto Plata", "Salcedo", "Samaná", "Sánchez Ramírez",
				"San Cristóbal", "San José de Ocoa", "San Juan",
				"San Pedro de Macorís", "Santiago", "Santiago Rodríguez",
				"Santo Domingo", "Valverde" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "DOP";
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
