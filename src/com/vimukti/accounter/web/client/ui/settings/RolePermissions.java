package com.vimukti.accounter.web.client.ui.settings;

public class RolePermissions {

	public static String READ_ONLY = "Read Only";
	public static String INVOICE_ONLY = "Invoice Only";
	public static String BASIC_EMPLOYEE = "Basic Employee";
	public static String FINANCIAL_ADVISER = "Financial Adviser";
	public static String FINANCE_ADMIN = "Finance Admin";
	public static String ADMIN = "Admin";

	public static String BANK_RECONCILATION = "Bank Reconcilation";
	public static String INVOICES = "Invoices";
	public static String EMPLOYEE_EXPENCES = "Employee Expences";
	public static String EDIT_SYSTEM_SETTINGS = "Edit System Settings";
	public static String VIEW_REPORTS = "View Reports";
	public static String PUBLISH_REPORTS = "Publish Reports";
	public static String LOCK_DATES = "Lock Dates";
	public static String MANAGE_USERS = "Manage Users";

	public static int TYPE_YES = 1;
	public static int TYPE_NO = 3;
	public static int TYPE_READ_ONLY = 2;
	public static int TYPE_DRAFT_ONLY = 4;
	public static int TYPE_APPROVE = 5;

	String roleName;

	int typeOfBankReconcilation;

	int typeOfInvoices;
	
	int typeOfExpences;

	int typeOfSystemSettings;

	int typeOfViewReports;

	int typeOfPublishReports;

	int typeOfLockDates;
	
	boolean canDoUserManagement;

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

	public boolean isCanDoUserManagement() {
		return canDoUserManagement;
	}

	public void setCanDoUserManagement(boolean canDoUserManagement) {
		this.canDoUserManagement = canDoUserManagement;
	}

}
