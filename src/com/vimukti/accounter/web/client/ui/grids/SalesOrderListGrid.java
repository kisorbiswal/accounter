package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.SalesOrdersList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.customers.SalesOrderListView;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class SalesOrderListGrid extends BaseListGrid<SalesOrdersList> {

	private SalesOrderListView view;

	public SalesOrderListGrid(SalesOrderListView salesOrderListView) {
		super(false);
		this.view = salesOrderListView;
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	protected Object getColumnValue(SalesOrdersList obj, int index) {
		switch (index) {
		case 0:
			return UIUtils.getDateByCompanyType(obj.getDate());
		case 1:
			return obj.getNumber();
		case 2:
			return obj.getCustomerName();
		case 3:
			return amountAsString(obj.getTotal());
			// case 7:
			// return amountAsString(obj.getBalance());

		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(SalesOrdersList obj) {
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(obj.getType(), obj
					.getTransactionId());
	}

	@Override
	protected void onClick(SalesOrdersList obj, int row, int col) {
		// NOTHING TO DO.
		if (!Accounter.getUser().canDoInvoiceTransactions())
			return;
		view.onClick(obj);
		super.onClick(obj, row, col);
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0)
			return 75;
		if (index == 1)
			return 100;
		if (index == 3)
			return 150;

		return -1;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.constants().date(),
				Accounter.constants().orderNumber(),
				Accounter.messages().customerName(Global.get().Customer()),
				Accounter.constants().totalPrice() };
	}

	@Override
	protected void executeDelete(SalesOrdersList object) {
		// NOTHING TO DO.
	}


	public AccounterCoreType getType() {
		return null;
	}

	@Override
	protected int sort(SalesOrdersList obj1, SalesOrdersList obj2, int index) {
		switch (index) {
		case 0:
			ClientFinanceDate date1 = obj1.getDate();
			ClientFinanceDate date2 = obj2.getDate();
			if (date1 != null && date2 != null)
				return date1.compareTo(date2);
		case 1:
			String num1 = obj1.getNumber();
			String num2 = obj2.getNumber();
			return num1.compareTo(num2);
		case 2:
			String name1 = obj1.getCustomerName();
			String name2 = obj2.getCustomerName();
			return name1.toLowerCase().compareTo(name2.toLowerCase());
		case 3:
			Double price1 = obj1.getTotal();
			Double price2 = obj2.getTotal();
			return price1.compareTo(price2);
		default:
			break;
		}
		return 0;
	}
}
