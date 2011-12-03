package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

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
	private Company company;
	int boxNumber;

	String name;
	double amount;

	List<TAXRateCalculation> taxRateCalculations = new ArrayList<TAXRateCalculation>();
	private int version;

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
	public long getID() {
		return id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isPositiveBox() {
		return (this.boxNumber == 1);

	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();
		
		w.put(messages.type(), messages.box()).gap();
		w.put(messages.no(), this.boxNumber);
		w.put(messages.name(), this.name).gap().gap();
		w.put(messages.total(), this.amount);
		
		w.put(messages.details(), this.taxRateCalculations);
	}
}
