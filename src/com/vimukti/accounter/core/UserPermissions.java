package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.InvalidOperationException;

public class UserPermissions implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7933490266078955247L;

	long id;

	int typeOfBankReconcilation;

	int typeOfInvoicesAndExpenses;

	int typeOfSystemSettings;

	int typeOfViewReports;

	int typeOfPublishReports;

	int typeOfLockDates;

	User user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTypeOfBankReconcilation() {
		return typeOfBankReconcilation;
	}

	public void setTypeOfBankReconcilation(int typeOfBankReconcilation) {
		this.typeOfBankReconcilation = typeOfBankReconcilation;
	}

	public int getTypeOfInvoicesAndExpenses() {
		return typeOfInvoicesAndExpenses;
	}

	public void setTypeOfInvoicesAndExpenses(int typeOfInvoicesAndExpenses) {
		this.typeOfInvoicesAndExpenses = typeOfInvoicesAndExpenses;
	}

	public int getTypeOfSystemSettings() {
		return typeOfSystemSettings;
	}

	public void setTypeOfSystemSettings(int typeOfSystemSettings) {
		this.typeOfSystemSettings = typeOfSystemSettings;
	}

	public int getTypeOfViewReports() {
		return typeOfViewReports;
	}

	public void setTypeOfViewReports(int typeOfViewReports) {
		this.typeOfViewReports = typeOfViewReports;
	}

	public int getTypeOfPublishReports() {
		return typeOfPublishReports;
	}

	public void setTypeOfPublishReports(int typeOfPublishReports) {
		this.typeOfPublishReports = typeOfPublishReports;
	}

	public int getTypeOfLockDates() {
		return typeOfLockDates;
	}

	public void setTypeOfLockDates(int typeOfLockDates) {
		this.typeOfLockDates = typeOfLockDates;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getStringID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setImported(boolean isImported) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStringID(String stringID) {
		// TODO Auto-generated method stub

	}

}
