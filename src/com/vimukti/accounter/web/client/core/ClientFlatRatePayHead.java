package com.vimukti.accounter.web.client.core;

public class ClientFlatRatePayHead extends ClientPayHead {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int calculationPeriod;

	public int getCalculationPeriod() {
		return calculationPeriod;
	}

	public void setCalculationPeriod(int calculationPeriod) {
		this.calculationPeriod = calculationPeriod;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.FLATRATE_PAY_HEAD;
	}
}
