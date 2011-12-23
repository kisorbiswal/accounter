package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;

public class VendorsListGrid extends BaseListGrid<PayeeList> {
	Map<Integer, Integer> colsMap = new HashMap<Integer, Integer>();
	private VendorSelectionListener vendorSelectionListener;
	ClientVendor selectedVendor;
	ArrayList<PayeeList> listOfVendors;

	public VendorsListGrid() {
		super(false, true);
		addDataToGrid();
	}

	@Override
	protected Object getColumnValue(PayeeList payee, int col) {

		switch (col) {

		case 0:
			return payee.getPayeeName();
		case 1:
			return DataUtils.amountAsStringWithCurrency(payee.getBalance(),
					payee.getCurrecny());

		default:
			break;
		}

		return null;
	}

	@Override
	protected String[] getColumns() {

		return new String[] { "Name", "Balance" };

	}

	@Override
	protected void onClick(PayeeList obj, int row, int col) {

		AccounterAsyncCallback<ClientVendor> callback = new AccounterAsyncCallback<ClientVendor>() {

			@Override
			public void onException(AccounterException caught) {
			}

			@Override
			public void onResultSuccess(ClientVendor result) {
				if (result != null) {
					setSelectedVendor(result);
					vendorSelectionListener.vendorSelected();
				}
			}

		};
		Accounter.createGETService().getObjectById(AccounterCoreType.VENDOR,
				obj.id, callback);

	}

	@Override
	public void addEmptyMessage(String msg) {
		super.addEmptyMessage(msg);
	}

	private void addDataToGrid() {
		// Accounter.createHomeService().getPayeeList(ClientPayee.TYPE_VENDOR,
		// new AsyncCallback<PaginationList<PayeeList>>() {
		//
		// @Override
		// public void onSuccess(PaginationList<PayeeList> result) {
		// if (result.size() == 0) {
		// addEmptyMessage(messages.youDontHaveAny(Global
		// .get().Vendors()));
		// } else {
		// for (PayeeList payeeList : result) {
		// listOfVendors = result;
		// if (payeeList.isActive())
		// addData(payeeList);
		// }
		// }
		// }
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

	}

	public void filterList(boolean isActive) {
		removeAllRecords();
		if (listOfVendors != null) {
			for (PayeeList vendor : listOfVendors) {
				if (isActive) {
					if (vendor.isActive() == true)
						addData(vendor);
				} else if (vendor.isActive() == false) {
					addData(vendor);
				}
			}
		}
		if (getRecords().size() == 0)
			addEmptyMessage(messages.youDontHaveAny(Global.get().Vendors()));
	}

	@Override
	protected void onValueChange(PayeeList obj, int col, Object value) {
		Accounter.showInformation("on value change called");
	}

	@Override
	public void onDoubleClick(PayeeList obj) {

	}

	@Override
	protected void executeDelete(final PayeeList recordToBeDeleted) {

	}

	public void setVendorSelectionListener(
			VendorSelectionListener vendorSelectionListener) {
		this.vendorSelectionListener = vendorSelectionListener;

	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	protected void updateTotal(PayeeList vendor, boolean add) {

		if (add) {
			if (vendor.isActive())
				total += vendor.getBalance();
			else
				total += vendor.getBalance();
		} else
			total -= vendor.getBalance();
	}

	@Override
	public Double getTotal() {
		return total;
	}

	@Override
	public void setTotal() {
		this.total = 0.0D;
	}

	@Override
	protected int getCellWidth(int index) {

		return 100;
	}

	@Override
	public void addRecords(List<PayeeList> list) {
		super.addRecords(list);

	}

	@Override
	protected int sort(PayeeList obj1, PayeeList obj2, int index) {
		switch (index) {
		case 1:
			return obj1.getPayeeName().toLowerCase()
					.compareTo(obj2.getPayeeName().toLowerCase());

		default:
			break;
		}

		return 0;
	}

	@Override
	public AccounterCoreType getType() {
		return AccounterCoreType.VENDOR;
	}

	@Override
	public void addData(PayeeList obj) {
		super.addData(obj);

	}

	@Override
	public void headerCellClicked(int colIndex) {
		super.headerCellClicked(colIndex);

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		int errorCode = caught.getErrorCode();
		if (errorCode == AccounterException.ERROR_OBJECT_IN_USE) {
			Accounter.showError(AccounterExceptions.accounterMessages
					.payeeInUse(Global.get().Vendor()));
			return;
		}
		super.deleteFailed(caught);
	}

	public void setSelectedVendor(ClientVendor selectedVendor) {
		this.selectedVendor = selectedVendor;
	}

	public ClientVendor getSelectedVendor() {
		return selectedVendor;
	}

}
