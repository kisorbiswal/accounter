package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class Budget implements IAccounterServerCore {

	private static final long serialVersionUID = 1L;

	private long id;

	private String budgetName;

	private Customer customer;

	private Account account;

	private double januaryAmount;
	private double febrauaryAmount;
	private double marchAmount;
	private double aprilAmount;
	private double mayAmount;
	private double juneAmount;
	private double julyAmount;
	private double augustAmount;
	private double septemberAmount;
	private double octoberAmount;
	private double novemberAmount;
	private double decemberAmount;

	private int version;

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setID(long id) {
		this.id = id;
	}

	public String getBudgetName() {
		return budgetName;
	}

	public void setBudgetName(String budgetname) {
		this.budgetName = budgetname;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * 
	 * @return
	 */
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

	/**
	 * 
	 * @param amount
	 */
	public void setDecemberAmount(double amount) {
		this.decemberAmount = amount;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}
}
