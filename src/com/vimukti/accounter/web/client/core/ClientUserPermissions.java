package com.vimukti.accounter.web.client.core;

public class ClientUserPermissions implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1371160964393449868L;

	// public static String BANK_RECONCILATION = "Bank Reconcilation";
	// public static String INVOICES_AND_EXPENSES = "Invoices And Expenses";
	// public static String EDIT_SYSTEM_SETTINGS = "Edit System Settings";
	// public static String VIEW_REPORTS = "View Reports";
	// public static String PUBLISH_REPORTS = "Publish Reports";
	// public static String LOCK_DATES = "Lock Dates";
	//
	// public static int TYPE_YES = 1;
	// public static int TYPE_NO = 3;
	// public static int TYPE_READ_ONLY = 2;

	int typeOfBankReconcilation;

	int typeOfInvoicesAndExpenses;

	int typeOfSystemSettings;

	int typeOfViewReports;

	int typeOfPublishReports;

	int typeOfLockDates;

	ClientUser user;

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

	public ClientUser getUser() {
		return user;
	}

	public void setUser(ClientUser user) {
		this.user = user;
	}

	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStringID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStringID(String stringID) {
		// TODO Auto-generated method stub

	}

}
