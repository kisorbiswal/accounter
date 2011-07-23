package com.vimukti.accounter.core;

import java.util.List;

import com.vimukti.accounter.web.client.InvalidOperationException;

/**
 * This class an intermediate class which stores all the static values of the
 * VAT return boxes for both UK and Ireland.
 */

public class Box implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3730671126732195673L;
	long id;
	long id;

	int boxNumber;

	String name;
	double amount;

	List<TAXRateCalculation> taxRateCalculations;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setID(long id){
		this.id = id;
	}

	/**
	 * @return the vatRateCalculations
	 */
	public List<TAXRateCalculation> getTaxRateCalculations() {
		return taxRateCalculations;
	}

	/**
	 * @param vatRateCalculations
	 *            the vatRateCalculations to set
	 */
	public void setTaxRateCalculations(
			List<TAXRateCalculation> taxRateCalculations) {
		this.taxRateCalculations = taxRateCalculations;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the boxNumber
	 */
	public int getBoxNumber() {
		return boxNumber;
	}

	/**
	 * @param boxNumber
	 *            the boxNumber to set
	 */
	public void setBoxNumber(int boxNumber) {
		this.boxNumber = boxNumber;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public long getID(){
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setID(long id){
		this.id=id;
	}

	@Override
	public void setImported(boolean isImported) {

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isPositiveBox() {
		return (this.boxNumber == 1);

	}
}
