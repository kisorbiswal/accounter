package com.vimukti.accounter.web.client.ui.grids;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

public class VendorListGrid extends BaseListGrid<PayeeList> {
	Map<Integer, Integer> colsMap = new HashMap<Integer, Integer>();

	public VendorListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable, true);
	}

	@Override
	protected String[] getColumns() {

		String[] colArray = new String[11];
		for (int index = 0; index < colArray.length; index++) {
			switch (index) {
			case 0:
				colArray[index] = Accounter.getVATMessages().active();
				break;
			case 1:
				if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
					colArray[index] = Accounter.getVendorsMessages()
							.vendorName();
				if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
					colArray[index] = Accounter.getVendorsMessages()
							.supplieRName();
				break;
			case 2:
				colArray[index] = Accounter.getCompanyMessages()
						.currentMonth();
				colsMap.put(2, getCurrentMonth());
				break;
			case 3:
				colArray[index] = getMonthAsString(getCurrentMonth() - 1);
				colsMap.put(3, getCurrentMonth() - 1);
				break;
			case 4:
				colArray[index] = getMonthAsString(getCurrentMonth() - 2);
				colsMap.put(4, getCurrentMonth() - 2);
				break;
			case 5:
				colArray[index] = getMonthAsString(getCurrentMonth() - 3);
				colsMap.put(5, getCurrentMonth() - 3);
				break;
			case 6:
				colArray[index] = getMonthAsString(getCurrentMonth() - 4);
				colsMap.put(6, getCurrentMonth() - 4);
				break;
			case 7:
				colArray[index] = getMonthAsString(getCurrentMonth() - 5);
				colsMap.put(6, getCurrentMonth() - 5);
				break;
			case 8:
				colArray[index] = Accounter.getCompanyMessages()
						.yearToDate();
				break;
			case 9:
				colArray[index] = Accounter.getVendorsMessages()
						.balance();
				break;
			case 10:
				colArray[index] = "";
				break;
			default:
				break;
			}
		}
		return colArray;

	}

	@SuppressWarnings("deprecation")
	private int getCurrentMonth() {
		return new ClientFinanceDate().getMonth();
	}

	private String getMonthAsString(int month) {
		switch (month) {
		case 0:
			return "JAN";
		case 1:
			return "FEB";
		case 2:
			return "MAR";
		case 3:
			return "APR";
		case 4:
			return "MAY";
		case 5:
			return "JUN";
		case 6:
			return "JUL";
		case 7:
		case -5:
			return "AUG";
		case 8:
		case -4:
			return "SEPT";
		case 9:
		case -3:
			return "OCT";
		case 10:
		case -2:
			return "NOV";
		case 11:
		case -1:
			return "DEC";

		}
		return "";

	}

	@Override
	protected Object getColumnValue(PayeeList payee, int col) {
		switch (col) {
		case 0:
			return payee.isActive();
		case 1:
			return payee.getPayeeName();
		case 2:
			return DataUtils.getAmountAsString(payee.getCurrentMonth());
		case 3:
			return DataUtils.getAmountAsString(payee.getPreviousMonth());
		case 4:
			return DataUtils.getAmountAsString(payee.getPreviousSecondMonth());

		case 5:
			return DataUtils.getAmountAsString(payee.getPreviousThirdMonth());

		case 6:
			return DataUtils.getAmountAsString(payee.getPreviousFourthMonth());

		case 7:
			return DataUtils.getAmountAsString(payee.getPreviousFifthMonth());

		case 8:
			return DataUtils.getAmountAsString(payee.getYearToDate());
		case 9:
			return DataUtils.getAmountAsString(payee.getBalance());
		case 10:
			updateTotal(payee, true);
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		default:
			break;
		}

		return null;
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 10) {
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		}
		if (index == 0) {
			return 40;
		}
		return super.getCellWidth(index);
	}

	@Override
	protected void onClick(PayeeList payee, int row, int col) {
		List<PayeeList> suppliers = getRecords();
		PayeeList supplier = suppliers.get(row);
		switch (col) {
		case 10:
			showWarnDialog(payee);
			break;
		default:
			break;
		}
		super.onClick(payee, row, col);
	}

	@Override
	public void onDoubleClick(PayeeList payee) {
		AsyncCallback<ClientPayee> callback = new AsyncCallback<ClientPayee>() {

			public void onFailure(Throwable caught) {
			}

			public void onSuccess(ClientPayee result) {
				if (result != null) {
					if (result instanceof ClientVendor) {
						HistoryTokenUtils.setPresentToken(VendorsActionFactory
								.getNewVendorAction(), result);
						UIUtils.runAction(result, VendorsActionFactory
								.getNewVendorAction());
						// } else if (result instanceof ClientTaxAgency) {
						// UIUtils.runAction(result, CompanyActionFactory
						// .getNewTaxAgencyAction());
					} else if (result instanceof ClientTAXAgency) {
						HistoryTokenUtils.setPresentToken(CompanyActionFactory
								.getNewTAXAgencyAction(), result);
						UIUtils.runAction(result, CompanyActionFactory
								.getNewTAXAgencyAction());
					}

				}
			}

		};
		if (payee.getType() == ClientPayee.TYPE_VENDOR)
			Accounter.createGETService().getObjectById(
					AccounterCoreType.VENDOR, payee.stringID, callback);
		else if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US
				&& payee.getType() == ClientPayee.TYPE_TAX_AGENCY)
			Accounter.createGETService().getObjectById(
					AccounterCoreType.TAXAGENCY, payee.stringID, callback);
		else if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK
				&& payee.getType() == ClientPayee.TYPE_TAX_AGENCY)
			Accounter.createGETService().getObjectById(
					AccounterCoreType.TAXAGENCY, payee.stringID, callback);
	}

	// public ClientAddress getBillToAddress(ClientPayee payee) {
	// Set<ClientAddress> address = payee.getAddress();
	// for (ClientAddress a : address) {
	// if (a.getType() == ClientAddress.TYPE_BILL_TO) {
	// return a;
	// }
	// }
	// return null;
	// }

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	protected void executeDelete(final PayeeList recordToBeDeleted) {

		AsyncCallback<ClientPayee> callback = new AsyncCallback<ClientPayee>() {

			public void onFailure(Throwable caught) {
			}

			public void onSuccess(ClientPayee result) {
				if (result != null) {
					if (result instanceof ClientVendor) {
						ViewManager.getInstance().deleteObject(result,
								AccounterCoreType.VENDOR, VendorListGrid.this);
						// } else if (result instanceof ClientTaxAgency) {
						// ViewManager.getInstance().deleteObject(result,
						// AccounterCoreType.TAXAGENCY,
						// VendorListGrid.this);
					} else if (result instanceof ClientTAXAgency) {
						ViewManager.getInstance().deleteObject(result,
								AccounterCoreType.TAXAGENCY,
								VendorListGrid.this);
					}

				}
			}

		};
		if (recordToBeDeleted.getType() == ClientPayee.TYPE_VENDOR)
			Accounter.createGETService().getObjectById(
					AccounterCoreType.VENDOR, recordToBeDeleted.stringID,
					callback);
		else if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US
				&& recordToBeDeleted.getType() == ClientPayee.TYPE_TAX_AGENCY)
			Accounter.createGETService().getObjectById(
					AccounterCoreType.TAXAGENCY, recordToBeDeleted.stringID,
					callback);
		else if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK
				&& recordToBeDeleted.getType() == ClientPayee.TYPE_TAX_AGENCY)
			Accounter.createGETService().getObjectById(
					AccounterCoreType.TAXAGENCY, recordToBeDeleted.stringID,
					callback);

		// rpcDoSerivce = FinanceApplication.createCRUDService();

	}

	@Override
	protected void onValueChange(PayeeList obj, int col, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validateGrid() {
		// TODO Auto-generated method stub
		return true;
	}

	protected void updateTotal(PayeeList supplier, boolean add) {

		if (add) {
			if (supplier.isActive())
				total += supplier.getBalance();
			else
				total += supplier.getBalance();
		} else
			total -= supplier.getBalance();

	}

	public Double getTotal() {
		return total;
	}

	public void setTotal() {
		this.total = 0.0D;

	}

	@Override
	protected int sort(PayeeList obj1, PayeeList obj2, int index) {
		switch (index) {
		case 1:
			return obj1.getPayeeName().toLowerCase().compareTo(
					obj2.getPayeeName().toLowerCase());
			//
			// case 3:
			//
			// String city1 = getBillToAddress(obj1) != null ? getBillToAddress(
			// obj1).getCity() : "";
			// String city2 = getBillToAddress(obj2) != null ? getBillToAddress(
			// obj2).getCity() : "";
			// return city1.compareTo(city2);
			//
			// case 4:
			// String state1 = getBillToAddress(obj1) != null ?
			// getBillToAddress(
			// obj1).getStateOrProvinence() : "";
			// String state2 = getBillToAddress(obj2) != null ?
			// getBillToAddress(
			// obj2).getStateOrProvinence() : "";
			// return state1.compareTo(state2);
			//
			// case 5:
			// String zip1 = getBillToAddress(obj1) != null ? getBillToAddress(
			// obj1).getZipOrPostalCode() : "";
			// String zip2 = getBillToAddress(obj2) != null ? getBillToAddress(
			// obj2).getZipOrPostalCode() : "";
			// return zip1.compareTo(zip2);
		case 2:
			Double currentMonth1 = obj1.getCurrentMonth();
			Double currentMonth2 = obj2.getCurrentMonth();
			return currentMonth1.compareTo(currentMonth2);
		case 3:
			Double previousMonth1 = obj1.getPreviousMonth();
			Double previousMonth2 = obj2.getPreviousMonth();
			return previousMonth1.compareTo(previousMonth2);
		case 4:
			Double previousSecondMonth1 = obj1.getPreviousSecondMonth();
			Double previousSecondMonth2 = obj2.getPreviousSecondMonth();
			return previousSecondMonth1.compareTo(previousSecondMonth2);
		case 5:
			Double previousThirdMonth1 = obj1.getPreviousThirdMonth();
			Double previousThirdMonth2 = obj2.getPreviousThirdMonth();
			return previousThirdMonth1.compareTo(previousThirdMonth2);
		case 6:
			Double previousFourthMonth1 = obj1.getPreviousFourthMonth();
			Double previousFourthMonth2 = obj2.getPreviousFourthMonth();
			return previousFourthMonth1.compareTo(previousFourthMonth2);
		case 7:
			Double previousFifthMonth1 = obj1.getPreviousFifthMonth();
			Double previousFifthMonth2 = obj2.getPreviousFifthMonth();
			return previousFifthMonth1.compareTo(previousFifthMonth2);

		case 8:
			Double yearToDate1 = obj1.getYearToDate();
			Double yearToDate2 = obj2.getYearToDate();
			return yearToDate1.compareTo(yearToDate2);
		case 9:
			Double bal1 = obj1.getBalance();
			Double bal2 = obj2.getBalance();
			return bal1.compareTo(bal2);

		default:
			break;
		}

		return 0;
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	public AccounterCoreType getType() {
		return AccounterCoreType.VENDOR;
	}

	@Override
	public void addData(PayeeList obj) {
		super.addData(obj);
		((CheckBox) this.getWidget(currentRow, 0)).setEnabled(false);
	}

	@Override
	public void headerCellClicked(int colIndex) {
		super.headerCellClicked(colIndex);
		for (int i = 0; i < this.getRowCount(); i++) {
			((CheckBox) this.getWidget(i, 0)).setEnabled(false);
		}
	}
}
