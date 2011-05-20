package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class UserRoleGrid extends ListGrid<RolePermissions> {

	private InviteUserView view;

	public UserRoleGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int getColumnType(int index) {
		if (index == 0) {
			return ListGrid.COLUMN_TYPE_CHECK;
		}
		return ListGrid.COLUMN_TYPE_TEXT;
	}

	@Override
	protected Object getColumnValue(RolePermissions obj, int index) {
		switch (index) {
		case 0:
			if (view.isEditMode()) {
				if (obj.getRoleName().equals(view.takenUser.getUserRole())) {
					// if (!view.canDoUserManagement(obj))
					// view.userManagementBox.setEnabled(false);
					return true;
				} else
					return false;
			} else {
				if (obj.getRoleName().equals(RolePermissions.BASIC_EMPLOYEE))
					return true;
				else
					return false;
			}
		case 1:
			return obj.getRoleName();
		case 2:
			return getPermissionType(obj.getTypeOfBankReconcilation());
		case 3:
			return getPermissionType(obj.getTypeOfInvoices());
		case 4:
			return getPermissionTypeForExpences(obj.getTypeOfExpences());
		case 5:
			return getPermissionType(obj.getTypeOfSystemSettings());
		case 6:
			return getPermissionType(obj.getTypeOfViewReports());
		case 7:
			return getPermissionType(obj.getTypeOfLockDates());
			// return getPermissionType(obj.getTypeOfPublishReports());
		case 8:
			if (obj.isCanDoUserManagement())
				return "Yes";
			else
				return "No";
			// return getPermissionType(obj.getTypeOfLockDates());
		}
		return null;
	}

	@Override
	protected String[] getSelectValues(RolePermissions obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isEditable(RolePermissions obj, int row, int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onClick(RolePermissions obj, int row, int index) {
		((CheckBox) this.getWidget(row, 0)).setValue(true);
		disableOtherCheckBoxes();
		// view.checkBoxClicked(obj);

		// if (index == 0) {
		// boolean isSelected = ((CheckBox) this.getWidget(row, index))
		// .getValue();
		// if (isSelected) {
		// disableOtherCheckBoxes();
		// view.checkBoxClicked(obj);
		// } else {
		// // ((CheckBox) this.getWidget(row, index)).setValue(true);
		// }
		// }
	}

	private void disableOtherCheckBoxes() {
		for (int i = 0; i < this.getRowCount(); i++) {
			Widget wdget = this.getWidget(i, 0);
			if (i != currentRow && wdget != null && wdget instanceof CheckBox) {
				CheckBox box = (CheckBox) this.getWidget(i, 0);
				box.setValue(false);
			}
		}
	}

	@Override
	public void onDoubleClick(RolePermissions obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onValueChange(RolePermissions obj, int index, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int sort(RolePermissions obj1, RolePermissions obj2, int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 15;
		}
		return -1;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "", "", RolePermissions.BANK_RECONCILATION,
				RolePermissions.INVOICES, RolePermissions.EMPLOYEE_EXPENCES,
				RolePermissions.EDIT_SYSTEM_SETTINGS,
				RolePermissions.VIEW_REPORTS, RolePermissions.LOCK_DATES,
				RolePermissions.MANAGE_USERS };
	}

	public void setView(InviteUserView view) {
		this.view = view;
	}

	public InviteUserView getView() {
		return view;
	}

	public String getPermissionType(int type) {
		switch (type) {
		case 1:
			return "Yes";
		case 3:
			return "No";
		case 2:
			return "Read Only";
		default:
			return "";
		}
	}

	public String getPermissionTypeForExpences(int type) {
		switch (type) {
		case 4:
			return "Draft Only";
		case 3:
			return "No";
		case 5:
			return "Approve";
		default:
			return "";
		}
	}
}
