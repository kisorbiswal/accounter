package com.vimukti.accounter.web.client.ui.grids;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTDSTransactionItem;
import com.vimukti.accounter.web.client.ui.vat.TDSChalanDetailsView;


public class TDSTransactionItemGrid extends
		BaseListGrid<ClientTDSTransactionItem> {

	private TDSChalanDetailsView tdsChalanDetails;
	boolean isSelected = false;
	boolean clicked = false;

	public TDSTransactionItemGrid() {
		super(false, true);
	}

	public TDSTransactionItemGrid(TDSChalanDetailsView tdsChalanDetailsView) {
		super(false, true);
		tdsChalanDetails = tdsChalanDetailsView;
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_TEXTBOX, ListGrid.COLUMN_TYPE_TEXTBOX,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_DATE };
	}

	@Override
	protected void executeDelete(ClientTDSTransactionItem object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(ClientTDSTransactionItem obj, int index) {

		switch (index) {
		case 0:
			return obj.isBoxSelected();
		case 1:
			return getCompany().getVendor(obj.getVendorID()).getName();
		case 2:
			return obj.getTotalAmount();
		case 3:
			return obj.getTaxAmount();
		case 4:
			return obj.getSurchargeAmount();
		case 5:
			return obj.getEduCess();
		case 6:
			return obj.getTdsTotal();
		case 7:
			ClientFinanceDate date = new ClientFinanceDate(
					obj.getTransactionDate());
			return date;
		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientTDSTransactionItem obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onClick(ClientTDSTransactionItem obj, int row, int col) {
		if (col == 6)
			return;

		if (col == 0) {
			((CheckBox) this.getWidget(row, col))
					.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							if (isSelected == false) {
								isSelected = true;

							} else {
								isSelected = false;
							}
							clicked = true;
						}
					});

			sendData(obj, clicked);
		}
		super.onClick(obj, row, col);
	}

	private void sendData(ClientTDSTransactionItem obj, boolean clicked2) {
		if (clicked2) {
			clicked = false;
			if (isSelected) {
				obj.setBoxSelected(true);
				tdsChalanDetails.setSurchargeValuesToField(
						obj.getSurchargeAmount(), true);
				tdsChalanDetails
						.setEduCessValuesToField(obj.getEduCess(), true);
				tdsChalanDetails.setTaxAmountValuesToField(obj.getTaxAmount(),
						true);
			} else {
				obj.setBoxSelected(false);
				tdsChalanDetails.setSurchargeValuesToField(
						obj.getSurchargeAmount(), false);
				tdsChalanDetails.setEduCessValuesToField(obj.getEduCess(),
						false);
				tdsChalanDetails.setTaxAmountValuesToField(obj.getTaxAmount(),
						false);
			}
		}
	}

	@Override
	public void editComplete(ClientTDSTransactionItem item, Object value,
			int col) {
		switch (col) {
		case 4:

			item.setSurchargeAmount(Double.parseDouble(value.toString()));

			item.setTdsTotal(item.getTaxAmount() + item.getSurchargeAmount()
					+ item.getEduCess());
			if (isSelected) {
				tdsChalanDetails.setSurchargeValuesToField(value, true);
			}
			this.refreshAllRecords();
			break;
		case 5:
			item.setEduCess(Double.parseDouble(value.toString()));
			item.setTdsTotal(item.getTaxAmount() + item.getSurchargeAmount()
					+ item.getEduCess());
			if (isSelected) {
				tdsChalanDetails.setEduCessValuesToField(value, true);
			}
			this.refreshAllRecords();
			break;
		}

	}

	@Override
	protected String[] getColumns() {
		String[] colArray = new String[8];
		for (int index = 0; index < colArray.length; index++) {
			switch (index) {
			case 0:
				colArray[index] = messages.applied();
				break;
			case 1:
				colArray[index] = Global.get().Vendor();
				break;
			case 2:
				colArray[index] = "Amount paid/Credit";
				break;
			case 3:
				colArray[index] = "TDS Amount";
				break;
			case 4:
				colArray[index] = "Surchage Amount";
				break;
			case 5:
				colArray[index] = "Education Cess";
				break;
			case 6:
				colArray[index] = "Total Tax";
				break;
			case 7:
				colArray[index] = "Date of Payment";
				break;
			default:
				break;
			}
		}
		return colArray;

	}

	@Override
	protected boolean isEditable(ClientTDSTransactionItem obj, int row, int col) {

		switch (col) {
		case 0:
			return false;
		case 1:
			return false;
		case 2:
			return false;
		case 3:
			return false;
		case 4:
			return true;
		case 5:
			return true;
		case 6:
			return true;
		case 7:
			return false;
		default:
			return false;
		}

	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0) {
			return 30;
		} else {
			return 100;
		}

	}

}
