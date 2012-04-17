package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * @author Prasanna Kumar G
 * 
 */
public class EmployeePayHeadComponent extends CreatableObject implements IAccounterServerCore{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PayHead payHead;

	private double rate;
	
	private int noOfLeaves;

	/**
	 * @return the payHead
	 */
	public PayHead getPayHead() {
		return payHead;
	}

	/**
	 * @param payHead
	 *            the payHead to set
	 */
	public void setPayHead(PayHead payHead) {
		this.payHead = payHead;
	}

	/**
	 * @return the rate
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	public boolean isEarning() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isDeduction() {
		// TODO Auto-generated method stub
		return false;
	}

	public double calculatePayment() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNoOfLeaves() {
		return noOfLeaves;
	}

	public void setNoOfLeaves(int noOfLeaves) {
		this.noOfLeaves = noOfLeaves;
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

}
