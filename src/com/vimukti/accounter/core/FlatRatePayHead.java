package com.vimukti.accounter.core;

/**
 * Flat Rate is a Calculation Type is used where the value of the Pay head is a
 * fixed amount for a period. Pro-rata will not happen in this type of the
 * component (Pay Head). Examples of Flat Rate Calculation Type:
 * 
 * @author Prasanna Kumar G
 * 
 */
public class FlatRatePayHead extends PayHead {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int calculationPeriod;

	public FlatRatePayHead() {
		super(CALCULATION_TYPE_FLAT_RATE);
	}

	/**
	 * @return the calculationPeriod
	 */
	public int getCalculationPeriod() {
		return calculationPeriod;
	}

	/**
	 * @param calculationPeriod
	 *            the calculationPeriod to set
	 */
	public void setCalculationPeriod(int calculationPeriod) {
		this.calculationPeriod = calculationPeriod;
	}

	@Override
	public double calculatePayment(PayStructureItem payStructureItem,
			double deductions, double earnings) {
		return payStructureItem.getRate();
	}
}
