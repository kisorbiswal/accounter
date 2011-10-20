package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Cambodia extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "KHR";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Banteay Mean Chey", "Bat Dâmbâng",
				"Kâmpóng Cham", "Kâmpóng Chhnang", "Kâmpóng Spoeu",
				"Kâmpóng Thum", "Kâmpôt", "Kândal", "Kaôh Kông", "Krâchéh",
				"Krong Kaeb", "Krong Pailin", "Krong Preah Sihanouk",
				"Môndôl Kiri", "Otdar Mean Chey", "Phnum Pénh", "Pousat",
				"Preah Vihéar", "Prey Veaeng", "Rôtanak Kiri", "Siem Reab",
				"Stueng Traeng", "Svay Rieng", "Takaev" };
	}

}
