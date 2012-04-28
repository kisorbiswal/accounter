package com.vimukti.accounter.core;


/**
 * A Payroll Unit is similar to Unit of Measure used in the Inventory module. In
 * Payroll, on the basis of Payroll Units, Pay components are calculated for a
 * given period. You can create Simple as well as Compound Payroll Units
 * measured on Attendance / Production Types such as Time, Work or Quantity.
 * 
 * @author Prasanna Kumar G
 * 
 */
public class PayrollUnit extends CreatableObject {

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
}
