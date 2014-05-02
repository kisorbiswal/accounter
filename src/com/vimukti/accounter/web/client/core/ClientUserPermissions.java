package com.vimukti.accounter.web.client.core;

import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

public class ClientUserPermissions implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// public static int TYPE_YES = 1;
	// public static int TYPE_NO = 3;
	// public static int TYPE_READ_ONLY = 2;

	int typeOfBankReconcilation;

	int typeOfInvoicesBills;

	int typeOfSaveasDrafts;

	int typeOfPayBillsPayments;

	int typeOfCompanySettingsLockDates;

	int typeOfViewReports;

	int typeOfMangeAccounts;

	int typeOfInventoryWarehouse;

	ClientUser user;

	private int version;

	public boolean isOnlySeeInvoiceandBills;

	public int getTypeOfBankReconcilation() {
		return typeOfBankReconcilation;
	}

	public void setTypeOfBankReconcilation(int typeOfBankReconcilation) {
		this.typeOfBankReconcilation = typeOfBankReconcilation;
	}

	public int getTypeOfInvoicesBills() {
		return typeOfInvoicesBills;
	}

	public void setTypeOfInvoicesBills(int typeOfInvoicesBills) {
		this.typeOfInvoicesBills = typeOfInvoicesBills;
	}

	public int getTypeOfPayBillsPayments() {
		return typeOfPayBillsPayments;
	}

	public void setTypeOfPayBillsPayments(int typeOfPayBillsPayments) {
		this.typeOfPayBillsPayments = typeOfPayBillsPayments;
	}

	public int getTypeOfCompanySettingsLockDates() {
		return typeOfCompanySettingsLockDates;
	}

	public void setTypeOfCompanySettingsLockDates(
			int typeOfCompanySettingsLockDates) {
		this.typeOfCompanySettingsLockDates = typeOfCompanySettingsLockDates;
	}

	public int getTypeOfViewReports() {
		return typeOfViewReports;
	}

	public void setTypeOfViewReports(int typeOfViewReports) {
		this.typeOfViewReports = typeOfViewReports;
	}

	public int getTypeOfManageAccounts() {
		return typeOfMangeAccounts;
	}

	public void setTypeOfManageAccounts(int typeOfMangeAccounts) {
		this.typeOfMangeAccounts = typeOfMangeAccounts;
	}

	public int getTypeOfInventoryWarehouse() {
		return typeOfInventoryWarehouse;
	}

	public void setTypeOfInventoryWarehouse(int typeOfInventoryWarehouse) {
		this.typeOfInventoryWarehouse = typeOfInventoryWarehouse;
	}

	public ClientUser getUser() {
		return user;
	}

	public void setUser(ClientUser user) {
		this.user = user;
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
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setID(long id) {
		// TODO Auto-generated method stub

	}

	public ClientUserPermissions clone() {
		ClientUserPermissions clientUserPreferencesClone = (ClientUserPermissions) this
				.clone();
		clientUserPreferencesClone.user = this.user.clone();

		return clientUserPreferencesClone;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	public int getTypeOfSaveasDrafts() {
		return typeOfSaveasDrafts;
	}

	public void setTypeOfSaveasDrafts(int typeOfSaveasDrafts) {
		this.typeOfSaveasDrafts = typeOfSaveasDrafts;
	}

	public boolean isOnlySeeInvoiceandBills() {
		if ((typeOfInvoicesBills == RolePermissions.TYPE_YES && typeOfPayBillsPayments == RolePermissions.TYPE_YES)
				&& (typeOfBankReconcilation == RolePermissions.TYPE_NO
						&& typeOfCompanySettingsLockDates == RolePermissions.TYPE_NO
						&& typeOfInventoryWarehouse == RolePermissions.TYPE_NO
						&& typeOfMangeAccounts == RolePermissions.TYPE_NO
						&& typeOfCompanySettingsLockDates == RolePermissions.TYPE_NO
						&& typeOfViewReports == RolePermissions.TYPE_NO && typeOfSaveasDrafts == RolePermissions.TYPE_NO)) {
			this.isOnlySeeInvoiceandBills = true;
		}
		return isOnlySeeInvoiceandBills;
	}
}
