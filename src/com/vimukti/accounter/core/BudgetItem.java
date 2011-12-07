package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class BudgetItem implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private int version;

	private Company company;

	private Account account;

	String accountName = "";

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

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public double getFebrauaryAmount() {
		return febrauaryAmount;
	}

	public void setFebrauaryAmount(double febrauaryAmount) {
		this.febrauaryAmount = febrauaryAmount;
	}

	public double getSeptemberAmount() {
		return septemberAmount;
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

	public Account getBudgetAccount() {

		return account;
	}

	public void setBudgetAccount(Account account) {
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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.budgetitem());

		if (this.account != null)
			w.put(messages.account(), this.account.getName()).gap();

		w.put(messages.january() + " " + messages.amount(),
				Double.toString(this.januaryAmount));
		w.put(messages.february() + " " + messages.amount(),
				Double.toString(this.febrauaryAmount));
		w.put(messages.march() + " " + messages.amount(),
				Double.toString(this.marchAmount));
		w.put(messages.april() + " " + messages.amount(),
				Double.toString(this.aprilAmount));
		w.put(messages.may() + " " + messages.amount(),
				Double.toString(this.mayAmount));
		w.put(messages.june() + " " + messages.amount(),
				Double.toString(this.juneAmount));
		w.put(messages.july() + " " + messages.amount(),
				Double.toString(this.julyAmount));
		w.put(messages.august() + " " + messages.amount(),
				Double.toString(this.augustAmount));
		w.put(messages.september() + " " + messages.amount(),
				Double.toString(this.septemberAmount));
		w.put(messages.october() + " " + messages.amount(),
				Double.toString(this.octoberAmount));
		w.put(messages.november() + " " + messages.amount(),
				Double.toString(this.novemberAmount));
		w.put(messages.december() + " " + messages.amount(),
				Double.toString(this.decemberAmount));
	}

}
