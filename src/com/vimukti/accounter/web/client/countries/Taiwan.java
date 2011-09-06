package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Taiwan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Changhwa", "Chiayi Hsien",
				"Chiayi Shih", "Hsinchu Hsien", "Hsinchu Shih", "Hualien",
				"Ilan", "Kaohsiung Hsien", "Kaohsiung Shih", "Keelung Shih",
				"Kinmen", "Miaoli", "Nantou", "Penghu", "Pingtung",
				"Taichung Hsien", "Taichung Shih", "Tainan Hsien",
				"Tainan Shih", "Taipei Hsien", "Taitung", "Taoyuan", "Yunlin" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "TWD";
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
