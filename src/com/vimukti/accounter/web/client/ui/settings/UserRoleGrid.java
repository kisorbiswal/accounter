package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.EditMode;
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
			if (view.getMode() != EditMode.CREATE) {
				if (obj.getRoleName().equals(view.getData().getUserRole())) {
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
			/*
			 * case 4: return
			 * getPermissionTypeForExpences(obj.getTypeOfExpences());
			 */
		case 4:
			return getPermissionType(obj.getTypeOfSystemSettings());
		case 5:
			return getPermissionType(obj.getTypeOfViewReports());
		case 6:
			return getPermissionType(obj.getTypeOfLockDates());
			// return getPermissionType(obj.getTypeOfPublishReports());
		case 7:
			if (obj.isCanDoUserManagement())
				return Accounter.constants().yes();
			else
				return Accounter.constants().no();
			// return getPermissionType(obj.getTypeOfLockDates());
		}
		return null;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	protected String[] getSelectValues(RolePermissions obj, int index) {
		return null;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	protected boolean isEditable(RolePermissions obj, int row, int index) {
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

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public void onDoubleClick(RolePermissions obj) {
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	protected void onValueChange(RolePermissions obj, int index, Object value) {
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	protected int sort(RolePermissions obj1, RolePermissions obj2, int index) {
		return 0;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 15;
		case 1:
			return 100;
		case 2:
			return 100;
		case 3:
			return 100;
		case 4:
			return 100;
		case 5:
			return 100;
		case 6:
			return 100;
		case 7:
			return 100;
		case 8:
			return 100;

		}
		return -1;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "", Accounter.constants().name(),
				RolePermissions.BANK_RECONCILATION, RolePermissions.INVOICES,
				/* RolePermissions.EMPLOYEE_EXPENCES, */
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
			return Accounter.constants().yes();
		case 3:
			return Accounter.constants().no();
		case 2:
			return Accounter.constants().readOnly();
		default:
			return "";
		}
	}

	public String getPermissionTypeForExpences(int type) {
		switch (type) {
		/*
		 * case 4: return Accounter.constants().draftOnly();
		 */
		case 3:
			return Accounter.constants().no();
		case 5:
			return Accounter.constants().approve();
		default:
			return "";
		}
	}
}
