package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEmployeeDetails;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AbstractActionFactory;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.customers.CustomersMessages;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;

public class EmployeeListGrid extends BaseListGrid<ClientEmployeeDetails> {

	public EmployeeListGrid() {
		super(false, true);
	}

	@Override
	protected void executeDelete(ClientEmployeeDetails recordToBeDeleted) {
		AsyncCallback<ClientCustomer> callback = new AsyncCallback<ClientCustomer>() {

			public void onFailure(Throwable caught) {
			}

			public void onSuccess(ClientCustomer result) {
				if (result != null) {
					ViewManager.getInstance().deleteObject(result,
							AccounterCoreType.EMPLOYEE, EmployeeListGrid.this);

				}
			}

		};
		FinanceApplication.createGETService().getObjectById(
				AccounterCoreType.EMPLOYEE, recordToBeDeleted.getStringID(),
				callback);

	}

	@Override
	protected int[] setColTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onClick(ClientEmployeeDetails obj, int row, int col) {
		switch (col) {
		case 8:
			showWarnDialog(obj);
			break;
		default:
			break;
		}
	}

	@Override
	protected Object getColumnValue(ClientEmployeeDetails employeeDetails,
			int index) {
		switch (index) {
		case 0:
			return employeeDetails.isActive();
		case 1:
			return employeeDetails.getName();
		case 2:
			return employeeDetails != null ? employeeDetails.getAddress1() : "";
		case 3:
			return employeeDetails != null ? employeeDetails.getCity() : "";
		case 4:
			return employeeDetails != null ? employeeDetails.getState() : "";

		case 5:
			return employeeDetails != null ? employeeDetails.getPostalCode()
					: "";

		case 6:
			return employeeDetails.getHomePhone();

		case 7:
			return employeeDetails.getHomeMailID();

		case 8:
			return FinanceApplication.getFinanceMenuImages().delete();
		default:
			break;
		}

		return null;
	}

	@Override
	public void onDoubleClick(ClientEmployeeDetails obj) {
		CompanyActionFactory.getEmployeeAction().run(obj, true);
	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String[] getColumns() {
		salesPersonConstants = GWT.create(CustomersMessages.class);
		return new String[] { salesPersonConstants.active(),
				salesPersonConstants.salesPerson(),
				salesPersonConstants.address(), salesPersonConstants.city(),
				salesPersonConstants.state(), salesPersonConstants.zipCode(),
				salesPersonConstants.phone(),
				AbstractActionFactory.actionsConstants.homeEmail(), " " };
	}

	public Double getTotal() {
		return total;
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 8) {
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		}
		if (index == 0) {
			return 40;
		}
		if (index == 2 || index == 3 || index == 4 || index == 5 || index == 6
				|| index == 7) {
			return 100;
		}
		return -1;
	}

	@Override
	protected int sort(ClientEmployeeDetails obj1, ClientEmployeeDetails obj2,
			int index) {
		switch (index) {
		case 1:
			return obj1.getName().compareTo(obj2.getName());
		case 2:

			String address1 = obj1 != null ? obj1.getAddress1() : "";
			String address2 = obj2 != null ? obj2.getAddress1() : "";
			return address1.compareTo(address2);

		case 3:

			String city1 = obj1 != null ? obj1.getCity() : "";
			String city2 = obj2 != null ? obj2.getCity() : "";
			return city1.compareTo(city2);

		case 4:
			String state1 = obj1 != null ? obj2.getState() : "";
			String state2 = obj2 != null ? obj2.getState() : "";
			return state1.compareTo(state2);

		case 5:
			String zip1 = obj1 != null ? obj2.getPostalCode() : "";
			String zip2 = obj2 != null ? obj2.getPostalCode() : "";
			return zip1.compareTo(zip2);
		case 6:
			String phone1 = obj1.getHomePhone();
			String phone2 = obj2.getHomePhone();
			return phone1.compareTo(phone2);
		case 7:
			String fax1 = obj1.getHomeMailID();
			String fax2 = obj2.getHomeMailID();
			return fax1.compareTo(fax2);
			// case 8:
			// Double bal1 = obj1.getBalance();
			// Double bal2 = obj2.getBalance();
			// return bal1.compareTo(bal2);

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
		return AccounterCoreType.EMPLOYEE;
	}

	@Override
	public void addData(ClientEmployeeDetails obj) {
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
