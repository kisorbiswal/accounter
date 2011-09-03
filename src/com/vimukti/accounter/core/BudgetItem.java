package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class BudgetItem implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private int version;

	private Account account;

	private double januaryAmount = 0.0D;
	private double febrauaryAmount = 0.0D;
	private double marchAmount = 0.0D;
	private double aprilAmount = 0.0D;
	private double mayAmount = 0.0D;
	private double juneAmount = 0.0D;
	private double julyAmount = 0.0D;
	private double augustAmount = 0.0D;
	private double septemberAmount = 0.0D;
	private double octoberAmount = 0.0D;
	private double novemberAmount = 0.0D;
	private double decemberAmount = 0.0D;
	private double totalAmount = 0.0D;

	@Override
	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;

	}

	public double getJanuaryAmount() {
		return januaryAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setJanuaryAmount(double amount) {
		this.januaryAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getFebruaryAmount() {
		return febrauaryAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setFebruaryAmount(double amount) {
		this.febrauaryAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getMarchAmount() {
		return marchAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setMarchAmount(double amount) {
		this.marchAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getAprilAmount() {
		return aprilAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setAprilAmount(double amount) {
		this.aprilAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getMayAmount() {
		return mayAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setMayAmount(double amount) {
		this.mayAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getJuneAmount() {
		return juneAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setJuneAmount(double amount) {
		this.juneAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getJulyAmount() {
		return julyAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setJulyAmount(double amount) {
		this.julyAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getAugustAmount() {
		return augustAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setAugustAmount(double amount) {
		this.augustAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getSpetemberAmount() {
		return septemberAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setSeptemberAmount(double amount) {
		this.septemberAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getOctoberAmount() {
		return octoberAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setOctoberAmount(double amount) {
		this.octoberAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getNovemberAmount() {
		return novemberAmount;
	}

	/**
	 * 
	 * @param amount
	 */
	public void setNovemberAmount(double amount) {
		this.novemberAmount = amount;
	}

	/**
	 * 
	 * @return
	 */
	public double getDecemberAmount() {
		return decemberAmount;
	}

	public void setDecemberAmount(double amount) {
		this.decemberAmount = amount;
	}

	public Account getAccountsName() {

		return account;
	}

	public void setAccountsName(Account account) {
		this.account = account;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double amount) {
		this.totalAmount = amount;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

}
