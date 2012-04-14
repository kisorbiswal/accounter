package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

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
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

}
