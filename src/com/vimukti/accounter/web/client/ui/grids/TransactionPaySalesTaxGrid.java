package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientTransactionPaySalesTax;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.PaySalesTaxView;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;

public class TransactionPaySalesTaxGrid extends
		AbstractTransactionGrid<ClientTransactionPaySalesTax> {
	AccounterConstants companyConstants = Accounter.constants();
	PaySalesTaxView paySalesTaxView;
	private boolean canEdit;
	private int[] columns = { ListGrid.COLUMN_TYPE_TEXT,
			ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
			ListGrid.COLUMN_TYPE_TEXTBOX };

	public TransactionPaySalesTaxGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	public TransactionPaySalesTaxGrid(boolean isMultiSelectionEnable,
			boolean showFooter) {
		super(isMultiSelectionEnable, showFooter);
	}

	public void setCurrentView(PaySalesTaxView view) {
		paySalesTaxView = view;

	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 300;
		case 1:
			return 150;
		}
		return -1;
	}

	@Override
	protected int getColumnType(int col) {
		return columns[col];
	}

	@Override
	protected String[] getColumns() {
		if (!paySalesTaxView.isEditMode())

			return new String[] { companyConstants.taxAgency(),
					companyConstants.taxItem(), companyConstants.taxDue(),
					companyConstants.amountToPay() };
		else

			return new String[] { companyConstants.taxAgency(),
					companyConstants.taxItem(), companyConstants.amountToPay() };
	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientTransactionPaySalesTax obj,
			int colIndex) {
		return null;
	}

	@Override
	protected Object getColumnValue(ClientTransactionPaySalesTax paySalesTax,
			int index) {
		if (!paySalesTaxView.isEditMode()) {
			switch (index) {
			case 0:
				return Accounter.getCompany()
						.getTaxAgency(paySalesTax.getTaxAgency()).getName() != null ? Accounter
						.getCompany().getTaxAgency(paySalesTax.getTaxAgency())
						.getName()
						: "";
			case 1:
				return paySalesTax.getTaxItem() != 0 ? ((Accounter.getCompany()
						.getTaxItem(paySalesTax.getTaxItem()).getName()) != null ? (Accounter
						.getCompany().getTaxItem(paySalesTax.getTaxItem())
						.getName()) : "")
						: " ";
			case 2:
				return DataUtils.getAmountAsString(paySalesTax.getTaxDue());
			case 3:
				return DataUtils
						.getAmountAsString(paySalesTax.getAmountToPay());
			default:
				break;
			}
		} else {
			switch (index) {
			case 0:
				return Accounter.getCompany()
						.getTaxAgency(paySalesTax.getTaxAgency()).getName() != null ? Accounter
						.getCompany().getTaxAgency(paySalesTax.getTaxAgency())
						.getName()
						: "";
			case 1:
				return Accounter.getCompany().getTaxItem(
						paySalesTax.getTaxItem()) != null ? Accounter
						.getCompany().getTaxItem(paySalesTax.getTaxItem())
						.getName() : "";

			case 2:
				return DataUtils
						.getAmountAsString(getAmountInForeignCurrency(paySalesTax
								.getAmountToPay()));
			default:
				break;
			}
		}
		return null;
	}

	@Override
	protected boolean isEditable(ClientTransactionPaySalesTax obj, int row,
			int index) {
		if (canEdit && index == 3)
			return isSelected(obj);
		return false;
	}

	@Override
	public void onHeaderCheckBoxClick(boolean isChecked) {
		if (isChecked) {
			selectAllRows();
		} else {
			resetValues();
		}
		super.onHeaderCheckBoxClick(isChecked);
	}

	public void resetValues() {
		for (ClientTransactionPaySalesTax obj : this.getRecords()) {
			if (!isSelected(obj)) {
				resetValue(obj);
			}
		}
	}

	public void resetValue(ClientTransactionPaySalesTax obj) {
		obj.setAmountToPay(0.0d);
		updateData(obj);
		updateFotterTotal();
	}

	@Override
	public void setCanEdit(boolean enabled) {
		this.canEdit = enabled;
		super.setCanEdit(enabled);
	}

	public void selectAllRows() {
		for (ClientTransactionPaySalesTax obj : this.getRecords()) {
			if (isSelected(obj)) {
				((CheckBox) this.body.getWidget(indexOf(obj), 0))
						.setValue(true);
				updateValue(obj);

			}
		}

	}

	public void updateValue(ClientTransactionPaySalesTax obj) {
		obj.setAmountToPay(obj.getTaxDue());
		updateRecord(obj, indexOf(obj), 3);
		updateFotterTotal();
	}

	public boolean isSelected(ClientTransactionPaySalesTax transactionList) {
		return ((CheckBox) getWidget(indexOf(transactionList), 0)).getValue();
	}

	@Override
	protected void onSelectionChanged(ClientTransactionPaySalesTax obj,
			int row, boolean isChecked) {
		if (isChecked) {
			updateValue(obj);
		} else {
			resetValue(obj);
		}
		super.onSelectionChanged(obj, row, isChecked);
	}

	public void editComplete(ClientTransactionPaySalesTax paySalesTax,
			Object value, int col) {
		if (col == 3) {
			paySalesTax
					.setAmountToPay((getAmountInBaseCurrency(((Double) value)
							.doubleValue())));
			updateRecord(paySalesTax, indexOf(paySalesTax), 3);
			updateFotterTotal();

		}
	}

	public void updateFotterTotal() {
		List<ClientTransactionPaySalesTax> records = getRecords();
		double footertotal = 0.0;
		for (ClientTransactionPaySalesTax record : records) {
			footertotal += record.getAmountToPay();
		}
		// this.updateFooterValues(FinanceApplication.constants().total()
		// + DataUtils.getAmountAsString(footertotal), 3);
		paySalesTaxView.totalAmount = footertotal;
		paySalesTaxView.refreshAmounts();

	}

	@Override
	public List<ClientTransactionItem> getallTransactions(
			ClientTransaction object) throws InvalidEntryException {

		return null;
	}

	@Override
	public void setTaxCode(long taxCode) {
		// currently not using anywhere in the project.

	}
}
