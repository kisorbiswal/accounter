package com.vimukti.accounter.web.client.core.Lists;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;

/**
 * 
 * @author Amrit Mishra
 * 
 */
public class BudgetList implements IAccounterCore {

	private static final long serialVersionUID = 1L;

	String accounts;
	double januaryMonth = 0D;
	double februaryMonth = 0D;
	double marchMonth = 0D;
	double aprilMonth = 0D;
	double mayMonth = 0D;
	double juneMonth = 0D;
	double julyMonth = 0D;
	double augustMonth = 0D;
	double septemberMonth = 0D;
	double octoberMonth = 0D;
	double novemberMonth = 0D;
	double decemeberMonth = 0D;

	double totalBalance = 0D;

	public String getAccountsName() {
		return accounts;
	}

	public void setAccountName(String accounts) {
		this.accounts = accounts;
	}

	public double getJanuaryMonthAmount() {
		return januaryMonth;
	}

	public void setJanuaryMonthAmount(double amount) {
		this.januaryMonth = amount;
	}

	public double getFebruaryMonthAmount() {
		return februaryMonth;
	}

	public void setFebruaryMonthAmount(double amount) {
		this.februaryMonth = amount;
	}

	public double getMarchMonthAmount() {
		return marchMonth;
	}

	public void setMarchMonthAmount(double amount) {
		this.marchMonth = amount;
	}

	public double getAprilMonthAmount() {
		return aprilMonth;
	}

	public void setAprilMonthAmount(double amount) {
		this.aprilMonth = amount;
	}

	public double getMayMonthAmount() {
		return mayMonth;
	}

	public void setMayMonthAmount(double amount) {
		this.mayMonth = amount;
	}

	public double getJuneMonthAmount() {
		return juneMonth;
	}

	public void setJuneMonthAmount(double amount) {
		this.juneMonth = amount;
	}

	public double getJulyMonthAmount() {
		return julyMonth;
	}

	public void setJulyMonthAmount(double amount) {
		this.julyMonth = amount;
	}

	public double getAugustMonthAmount() {
		return augustMonth;
	}

	public void setAugustMonthAmount(double amount) {
		this.augustMonth = amount;
	}

	public double getSeptemberMonthAmount() {
		return septemberMonth;
	}

	public void setSeptemberMonthAmount(double amount) {
		this.septemberMonth = amount;
	}

	public double getOctoberMonthAmount() {
		return octoberMonth;
	}

	public void setOctoberMonthAmount(double amount) {
		this.octoberMonth = amount;
	}

	public double getNovemberMonthAmount() {
		return novemberMonth;
	}

	public void setNovemberMonthAmount(double amount) {
		this.novemberMonth = amount;
	}

	public double getDecemberMonthAmount() {
		return decemeberMonth;
	}

	public void setDecemberMonthAmount(double amount) {
		this.decemeberMonth = amount;
	}

	@Override
	public String getClientClassSimpleName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return null;
	}

	public BudgetList clone() {
		BudgetList budgetListClone = (BudgetList) this.clone();
		return budgetListClone;
	}

	@Override
	public void setID(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

}
