package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class UserPermissions implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7933490266078955247L;

	long id;

	int typeOfBankReconcilation;

	int typeOfInvoices;

	int typeOfExpences;

	int typeOfSystemSettings;

	int typeOfViewReports;

	int typeOfPublishReports;

	int typeOfLockDates;

	User user;

	private int version;

	public int getTypeOfBankReconcilation() {
		return typeOfBankReconcilation;
	}

	public void setTypeOfBankReconcilation(int typeOfBankReconcilation) {
		this.typeOfBankReconcilation = typeOfBankReconcilation;
	}

	public int getTypeOfInvoices() {
		return typeOfInvoices;
	}

	public void setTypeOfInvoices(int typeOfInvoices) {
		this.typeOfInvoices = typeOfInvoices;
	}

	public int getTypeOfExpences() {
		return typeOfExpences;
	}

	public void setTypeOfExpences(int typeOfExpences) {
		this.typeOfExpences = typeOfExpences;
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
			throws AccounterException {

		return false;
	}

	@Override
	public long getID() {

		return 0;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version=version;
	}

}
