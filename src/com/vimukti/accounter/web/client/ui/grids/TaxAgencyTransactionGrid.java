package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.combo.VATItemCombo;

public class TaxAgencyTransactionGrid extends
		AbstractTransactionGrid<ClientTransactionItem> {

	VATItemCombo taxItemCombo;
	private Double totallinetotal;
	private ClientTAXAgency selectedTaxAgency;

	public TaxAgencyTransactionGrid() {
		super(false, true);
	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientTransactionItem obj,
			int colIndex) {
		switch (colIndex) {
		case 0:
			return (CustomCombo<E>) taxItemCombo;
		}
		return null;
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 0:
			return ListGrid.COLUMN_TYPE_SELECT;
		case 1:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 2:
			return ListGrid.COLUMN_TYPE_TEXTBOX;
		case 3:
			return ListGrid.COLUMN_TYPE_IMAGE;
		}
		return 0;
	}

	@Override
	protected Object getColumnValue(ClientTransactionItem item, int index) {
		// ClientTaxCode taxCode = FinanceApplication.getCompany().getTaxCode(
		// item.getTaxCode());
		switch (index) {
		case 0:
			return (item.getTaxItem() != null) ? item.getTaxItem().getName()
					: "";
		case 1:
			return item.getDescription();
		case 2:
			return amountAsString(item.getLineTotal());
		case 3:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		default:
			break;
		}
		return "";
	}

	@Override
	protected boolean isEditable(ClientTransactionItem obj, int row, int index) {
		switch (index) {
		case 1:
			return false;
		case 4:
			return false;

		default:
			return true;
		}
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 3)
			return 15;
		return -1;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.constants().taxItem(),
				Accounter.constants().taxAgency(),
				Accounter.constants().amountToPay(), "" };
	}

	@Override
	public void init() {
		super.init();
		createControls();
		ClientTransaction transactionObject = transactionView
				.getTransactionObject();

		if (transactionObject != null) {
			removeAllRecords();
			setRecords(transactionObject.getTransactionItems());
			updateTotals();
			if (transactionObject.getID() != 0) {
				canDeleteRecord(false);
			}
		}
	}

	// public void setSelectedTaxAgency(ClientTaxAgency selectedTaxAgency) {
	// this.selectedTaxAgency = selectedTaxAgency;
	// }

	public void filteredTaxCodes() {

		List<ClientTAXItem> filteredTaxItems = new ArrayList<ClientTAXItem>();
		// if (selectedTaxAgency != null) {
		// for (ClientTaxItem taxItem : FinanceApplication.getCompany()
		// .getActiveTaxItems()) {
		// if (taxItem.getTaxAgency().equals(
		// selectedTaxAgency.getID()))
		// // if (taxItem.getID().equalsIgnoreCase(
		// // selectedTaxAgency.getID())) {
		// filteredTaxItems.add(taxItem);
		// // }
		// }
		// }
		// taxItemCombo.initCombo(filteredTaxItems);
	}

	private void createControls() {
		taxItemCombo = new VATItemCombo(Accounter.constants().taxCode());
		taxItemCombo.setGrid(this);
		addRecordDoubleClickHandler(new RecordDoubleClickHandler<ClientTransactionItem>() {

			@Override
			public void OnCellDoubleClick(ClientTransactionItem core, int column) {
				// if (column == 7
				// && !(FinanceApplication.getCompany()
				// .getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
				// && !isEdit)
				if (column == 0) {
					if (core.getTaxItem() != null
							&& !core.getTaxItem().equals("")) {
						// taxItemCombo.setComboItem(core.getTaxItem());
					} else
						taxItemCombo.setValue("");
				}
				return;
			}
		});

		// taxItemCombo
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientTaxItem>() {
		//
		// @Override
		// public void selectedComboBoxItem(ClientTaxItem selectItem) {
		// // selectedObject.setTaxCode(selectItem.getID());
		//
		// selectedObject.setTaxItem(selectItem);
		// setText(currentRow, currentCol, selectItem.getName());
		// updateRecord(selectedObject, currentRow, currentCol);
		// }
		// });
		// taxItemCombo.initCombo(FinanceApplication.getCompany()
		// .getActiveTaxItems());
		// this.addFooterValue(FinanceApplication.constants().total(),
		// 2);

	}

	@Override
	public void updateRecord(ClientTransactionItem item, int row, int col) {
		switch (col) {
		case 0:
			if (item.getTaxItem() != null) {
				// item.setDescription(FinanceApplication.getCompany()
				// .getTaxAgency(
				// FinanceApplication.getCompany().getTaxCode(
				// item.getTaxCode()).getTaxAgency())
				// .getName());
				item.setDescription(Accounter.getCompany()
						.getTaxAgency(item.getTaxItem().getTaxAgency())
						.getName());

				setText(currentRow, currentCol + 1, item.getDescription());
			}
		}
		// super.updateRecord(item, row, col);
	}

	@Override
	public void updateData(ClientTransactionItem obj) {
		super.updateData(obj);
	}

	public List<ClientTransactionItem> getallTransactionItems(
			ClientTransaction object) {

		return getRecords();
	}

	public Double getTotal() {
		return totallinetotal != null ? totallinetotal.doubleValue() : 0.0d;
	}

	public void setAllTransactionItems(List<ClientTransactionItem> items) {
		addRecords(items);
		updateTotals();
	}

	@Override
	protected void onClick(ClientTransactionItem obj, int row, int index) {
		switch (index) {
		case 3:
			deleteRecord(obj);
			break;

		default:
			break;
		}
	}

	@Override
	public void editComplete(ClientTransactionItem item, Object value, int col) {
		if (col == 2) {
			item.setLineTotal(Double.parseDouble(DataUtils
					.getReformatedAmount(value.toString()) + ""));
		}
		updateTotals();
		updateData(item);
	}

	public void updateTotals() {

		List<ClientTransactionItem> allrecords = (List<ClientTransactionItem>) (ArrayList) getRecords();
		totallinetotal = 0.0;
		for (ClientTransactionItem citem : allrecords) {
			Double lineTotalAmt = citem.getLineTotal();
			totallinetotal += lineTotalAmt;
		}

		// this.updateFooterValues(FinanceApplication.constants()
		// .totalcolan()
		// + amountAsString(totallinetotal), 2);
		transactionView.updateNonEditableItems();

	}

	@Override
	public void setTaxCode(long taxCode) {

	}

}
