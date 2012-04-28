package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * A Payroll Unit is similar to Unit of Measure used in the Inventory module. In
 * Payroll, on the basis of Payroll Units, Pay components are calculated for a
 * given period. You can create Simple as well as Compound Payroll Units
 * measured on Attendance / Production Types such as Time, Work or Quantity.
 * 
 * @author Prasanna Kumar G
 * 
 */
public class PayrollUnit extends CreatableObject implements
		IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Symbol of the Payroll Unit
	 */
	private String symbol;

	/**
	 * Formal Name of the Pay Roll Unit
	 */
	private String formalname;

	/**
	 * Number of Decimal Places for the Unit if you want to use the Unit in
	 * fractions
	 */
	private int noofDecimalPlaces;

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol
	 *            the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the formalname
	 */
	public String getFormalname() {
		return formalname;
	}

	/**
	 * @param formalname
	 *            the formalname to set
	 */
	public void setFormalname(String formalname) {
		this.formalname = formalname;
	}

	/**
	 * @return the noofDecimalPlaces
	 */
	public int getNoofDecimalPlaces() {
		return noofDecimalPlaces;
	}

	/**
	 * @param noofDecimalPlaces
	 *            the noofDecimalPlaces to set
	 */
	public void setNoofDecimalPlaces(int noofDecimalPlaces) {
		this.noofDecimalPlaces = noofDecimalPlaces;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public void selfValidate() throws AccounterException {
		// TODO Auto-generated method stub

	}
}
