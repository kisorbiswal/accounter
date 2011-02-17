package com.vimukti.accounter.web.client.core.Lists;

import java.io.Serializable;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class KeyFinancialIndicators implements IsSerializable, Serializable {

	Map<String, Map<Integer, Double>> indicators;

	public Map<String, Map<Integer, Double>> getIndicators() {
		return indicators;
	}

	public void setIndicators(Map<String, Map<Integer, Double>> indicators) {
		this.indicators = indicators;
	}

}
