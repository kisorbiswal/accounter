package com.vimukti.accounter.web.client.ui.settings;

public class RolePermissions {

	public static String READ_ONLY = "Read Only";
	public static String INVOICE_ONLY = "Invoice Only";
	public static String STANDARD = "Standard";
	public static String FINANCIAL_ADVISER = "Financial Adviser";

	public static String BANK_RECONCILATION = "Bank Reconcilation";
	public static String INVOICES_AND_EXPENSES = "Invoices And Expenses";
	public static String EDIT_SYSTEM_SETTINGS = "Edit System Settings";
	public static String VIEW_REPORTS = "View Reports";
	public static String PUBLISH_REPORTS = "Publish Reports";
	public static String LOCK_DATES = "Lock Dates";

	public static int TYPE_YES = 1;
	public static int TYPE_NO = 3;
	public static int TYPE_READ_ONLY = 2;

	String roleName;

	int typeOfBankReconcilation;

	int typeOfInvoicesAndExpenses;

	int typeOfSystemSettings;

	int typeOfViewReports;

	int typeOfPublishReports;

	int typeOfLockDates;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

}
