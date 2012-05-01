package com.vimukti.accounter.web.client.ui.company;

import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXReturn;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

public class FileTaxListGrid extends BaseListGrid<ClientTAXReturn> {
	private ClientCurrency currency = getCompany().getPrimaryCurrency();

	public FileTaxListGrid() {
		super(false);
	}

	public FileTaxListGrid(FileTAXListView fileTAXListView) {
		super(false);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DATE, ListGrid.COLUMN_TYPE_DATE,
				ListGrid.COLUMN_TYPE_DATE, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "taxAgency", "periodStartDate", "periodEndDate",
				"taxFiledDate", "taxAmount", "totalPaymentMade" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "taxAgency-value", "periodStartDate-value",
				"periodEndDate-value", "taxFiledDate-value", "taxAmount-value",
				"totalPaymentMade-value" };
	}

	@Override
	protected void executeDelete(final ClientTAXReturn object) {
		String warning = messages.taxReturnDeleteWarning(
				DateUtills.getDateAsString(object.getPeriodStartDate()),
				DateUtills.getDateAsString(object.getPeriodEndDate()));
		Accounter.showWarning(warning, AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onYesClick() {
						FileTaxListGrid.this.deleteRecord(object);
						Accounter.deleteObject(FileTaxListGrid.this, object);
						return true;
					}

					@Override
					public boolean onNoClick() {
						return true;
					}

					@Override
					public boolean onCancelClick() {
						return false;
					}
				});
	}

	@Override
	protected void onClick(ClientTAXReturn obj, int row, int col) {
		ReportsRPC.openTransactionView(ClientTransaction.TYPE_TAX_RETURN,
				obj.getID());
	}

	@Override
	protected Object getColumnValue(ClientTAXReturn obj, int index) {
		switch (index) {

		case 0:
			return getCompany().getTaxAgency(obj.getTaxAgency()).getName();
		case 1:
			return new ClientFinanceDate(obj.getPeriodStartDate()).toString();
		case 2:
			return new ClientFinanceDate(obj.getPeriodEndDate());
		case 3:
			return new ClientFinanceDate(obj.getTransactionDate());
		case 4:
			return DataUtils.amountAsStringWithCurrency(
					obj.getTotalTAXAmount(), currency);
		case 5:
			return DataUtils.amountAsStringWithCurrency(
					(obj.getTotalTAXAmount() - obj.getBalance()), currency);
		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientTAXReturn obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.taxAgency(), messages.periodStartDate(),
				messages.periodEndDate(), messages.taxFiledDate(),
				messages.taxAmount(), messages.totalPaymentMade() };
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 1:
		case 2:
		case 3:
			return 120;
		case 4:
			return 80;
		case 5:
			return 130;
		}
		return -1;
	}
}
