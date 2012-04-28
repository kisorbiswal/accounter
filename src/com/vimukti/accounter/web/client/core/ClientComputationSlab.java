package com.vimukti.accounter.web.client.core;

public class ClientComputationSlab {
	/** Slab Types */

	public static final int TYPE_PERCENTAGE = 1;
	public static final int TYPE_VALUE = 2;

	private ClientFinanceDate effectiveFrom;

	private double fromAmount;

	private double toAmount;

	private int slabType;

	private double value;

	public ClientFinanceDate getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(ClientFinanceDate effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public double getFromAmount() {
		return fromAmount;
	}

	public void setFromAmount(double fromAmount) {
		this.fromAmount = fromAmount;
	}

	public double getToAmount() {
		return toAmount;
	}

	public void setToAmount(double toAmount) {
		this.toAmount = toAmount;
	}

	public int getSlabType() {
		return slabType;
	}

	public void setSlabType(int slabType) {
		this.slabType = slabType;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
