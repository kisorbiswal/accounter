package com.vimukti.accounter.web.client.ui.grids;

import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class ChartOfAccountsListGrid extends BaseListGrid<ClientAccount> {

	public ChartOfAccountsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int getColumnType(int col) {
		return super.getColumnType(col);
	}

	@Override
	protected Object getColumnValue(ClientAccount obj, int col) {

		if (getPreferences().getUseAccountNumbers() == true) {
			switch (col) {
			case 0:
				return obj.getIsActive();
			case 1:
				return obj.getNumber() != null ? obj.getNumber() : "";
			case 2:
				return obj.getName() != null ? obj.getName() : "";
			case 3:
				return Utility.getAccountTypeString(obj.getType());
			case 4:
				return amountAsString(!DecimalUtil.isEquals(obj
						.getTotalBalance(), 0.0) ? obj.getTotalBalance() : 0.0);
			case 5:
				return Accounter.getFinanceMenuImages().accounterRegisterIcon();
				// return "/images/find.png";
			case 6:
				return Accounter.getFinanceMenuImages().delete();
				// return "/images/delete.png";
			default:
				break;
			}
			return null;
		} else {
			switch (col) {
			case 0:
				return obj.getIsActive();
			case 1:
				return obj.getName() != null ? obj.getName() : "";
			case 2:
				return Utility.getAccountTypeString(obj.getType());
			case 3:
				return amountAsString(!DecimalUtil.isEquals(obj
						.getTotalBalance(), 0.0) ? obj.getTotalBalance() : 0.0);
			case 4:
				return Accounter.getFinanceMenuImages().accounterRegisterIcon();
				// return "/images/find.png";
			case 5:
				return Accounter.getFinanceMenuImages().delete();
				// return "/images/delete.png";
			default:
				break;
			}
			return null;
		}

	}

	@Override
	protected String[] getColumns() {

		if (getPreferences().getUseAccountNumbers() == true) {
			bankingContants = Accounter.constants();
			return new String[] { bankingContants.active(),
					bankingContants.no(), bankingContants.name(),
					bankingContants.type(), bankingContants.balance(),
					bankingContants.register(), "" };
		} else {
			bankingContants = Accounter.constants();
			return new String[] { bankingContants.active(),
					bankingContants.name(), bankingContants.type(),
					bankingContants.balance(), bankingContants.register(), "" };
		}

	}

	@Override
	protected void onClick(ClientAccount obj, int row, int col) {
		if (!Accounter.getUser().canDoBanking()
				&& obj.getType() == ClientAccount.TYPE_BANK)
			return;
		else if (!Accounter.getUser().canDoInvoiceTransactions())
			return;
		if (col == getColumns().length - 1)
			showWarnDialog(obj);
		if (col == 5) {
			ActionFactory.getAccountRegisterAction().run(obj, true);
		}
	}

	@Override
	public void onDoubleClick(ClientAccount account) {
		if (!Accounter.getUser().canDoBanking()
				&& account.getType() == ClientAccount.TYPE_BANK) {
			return;
		} else if (!Accounter.getUser().canDoInvoiceTransactions()) {
			return;
		}
		if (account.getType() == ClientAccount.TYPE_BANK) {
			ActionFactory.getNewBankAccountAction().run(
					(ClientBankAccount) account, false);
		} else {
			ActionFactory.getNewAccountAction().run(account, false);
		}

	}

	@Override
	protected int[] setColTypes() {
		if (getPreferences().getUseAccountNumbers() == true) {
			return new int[] { ListGrid.COLUMN_TYPE_CHECK,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
					ListGrid.COLUMN_TYPE_IMAGE, ListGrid.COLUMN_TYPE_IMAGE };
		} else {
			return new int[] { ListGrid.COLUMN_TYPE_CHECK,
					ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
					ListGrid.COLUMN_TYPE_IMAGE, ListGrid.COLUMN_TYPE_IMAGE };
		}

	}

	@Override
	protected int getCellWidth(int index) {
		if (getPreferences().getUseAccountNumbers() == true) {
			if (index == 6) {
				if (UIUtils.isMSIEBrowser())
					return 25;
				else
					return 15;
			}
			if (index == 0 || index == 1) {
				return 50;
			} else if (index == 4)
				return 200;
			else if (index == 5)
				return 55;
			if (index == 3)
				return 200;
			// return super.getCellWidth(index);
			return -1;
		} else {
			if (index == 5) {
				if (UIUtils.isMSIEBrowser())
					return 25;
				else
					return 15;
			}
			if (index == 0) {
				return 50;
			} else if (index == 3)
				return 200;
			else if (index == 4)
				return 55;
			else if (index == 1) {
				if (UIUtils.isMSIEBrowser()) {
					return 960 - 700;
				} else {
					return 960 - 690;
				}
			}
			if (index == 2)
				return 200;
			if (index == 6) {
				if (UIUtils.isMSIEBrowser())
					return 25;
				else
					return 15;
			}
			// return super.getCellWidth(index);
			return -1;
		}

	}

	@Override
	protected void executeDelete(ClientAccount obj) {
		deleteObject(obj);
	}

	@Override
	protected int sort(ClientAccount obj1, ClientAccount obj2, int index) {

		switch (index) {
		case 1:
			return obj1.getNumber().compareTo(obj2.getNumber());

		case 2:
			return obj1.getName().toLowerCase().compareTo(
					obj2.getName().toLowerCase());
		case 3:
			String type1 = Utility.getAccountTypeString(obj1.getType())
					.toLowerCase();
			String type2 = Utility.getAccountTypeString(obj2.getType())
					.toLowerCase();
			return type1.compareTo(type2);

		case 4:
			Double bal1 = obj1.getTotalBalance();
			Double bal2 = obj2.getTotalBalance();
			return bal1.compareTo(bal2);

		default:
			break;
		}

		return 0;
	}

	@Override
	public void addData(ClientAccount obj) {
		super.addData(obj);
		((CheckBox) this.getWidget(currentRow, 0)).setEnabled(false);
	}

	public AccounterCoreType getType() {
		return AccounterCoreType.ACCOUNT;
	}

	@Override
	public void headerCellClicked(int colIndex) {
		super.headerCellClicked(colIndex);
		for (int i = 0; i < this.getRowCount(); i++) {
			((CheckBox) this.getWidget(i, 0)).setEnabled(false);
		}
	}
}
