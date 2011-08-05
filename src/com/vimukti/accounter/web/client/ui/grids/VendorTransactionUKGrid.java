/**
 * 
 */
package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VATItemCombo;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

/**
 * @author Murali.A
 * 
 */
public class VendorTransactionUKGrid extends VendorTransactionUSGrid {

	boolean isAddNewRequired = true;

	public VendorTransactionUKGrid() {
	}

	public VendorTransactionUKGrid(boolean isAddNewRequired) {
		super(isAddNewRequired);
		this.isAddNewRequired = isAddNewRequired;

	}

	@Override
	public void init(ViewManager manager) {
		super.isEnable = false;
		super.init();
		createControls();
		super.initTransactionData();

		ClientTransaction transactionObject = transactionView
				.getTransactionObject();

		if (transactionObject != null) {
			transactionView.setAmountIncludeChkValue(transactionObject
					.isAmountsIncludeVAT());
			setAllTransactions(transactionObject.getTransactionItems());
			if (transactionObject.getID() != 0) {
				// ITS Edit Mode
				canDeleteRecord(false);
			}
		}
	}

	protected void createControls() {
		vatItemCombo = new VATItemCombo(Accounter.constants().vatItem(),
				isAddNewRequired);
		List<ClientTAXItem> vendorVATItems = new ArrayList<ClientTAXItem>();
		for (ClientTAXItem vatItem : Accounter.getCompany().getActiveTaxItems()) {
			if (!vatItem.isSalesType())
				vendorVATItems.add(vatItem);

		}
		vatItemCombo.initCombo(vendorVATItems);
		vatItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {

					@Override
					public void selectedComboBoxItem(ClientTAXItem selectItem) {
						if (selectItem != null) {
							if (!isPreviuslyUsed(selectItem)) {
								Accounter
										.showError(Accounter
												.constants()
												.vatitemslctdalreadyusedinVATEnterdiffVATItem());
							}
							selectedObject.setVatItem(selectItem.getID());
							setText(currentRow, currentCol,
									selectItem.getName());
						}
					}
				});
		// taxCodeCombo.setGrid(this);

		taxCodeCombo = new TAXCodeCombo(Accounter.constants().vatCode(),
				isAddNewRequired, false);
		taxCodeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {

					@Override
					public void selectedComboBoxItem(ClientTAXCode selectItem) {
						if (selectItem != null) {
							selectedObject.setTaxCode(selectItem.getID());
							if (selectedObject.getType() == TYPE_SERVICE
									|| selectedObject.getType() == TYPE_ACCOUNT)
								editComplete(selectedObject,
										selectedObject.getLineTotal(), 6);
							else
								editComplete(selectedObject,
										selectedObject.getUnitPrice(), 4);
						}
					}
				});
		taxCodeCombo.setGrid(this);
		super.createControls();

	}

	public boolean isPreviuslyUsed(ClientTAXItem selectedVATItem) {
		for (ClientTransactionItem rec : getRecords()) {
			if (rec.getTaxCode() != 0) {
				long vatItem = Accounter.getCompany()
						.getTAXCode(rec.getTaxCode())
						.getTAXItemGrpForPurchases();
				if (selectedVATItem.getID() == vatItem) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public List<ClientTransactionItem> getallTransactions(
			ClientTransaction object) throws InvalidEntryException {
		return getRecords();
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0 || index == 8)
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		else if (index == 3 || index == 4)
			return 90;
		if (index == 6)
			return 70;
		if (index == 7)
			return 60;
		// if (index == 2)
		// return 100;
		if (index == 5)
			return 100;
		return -1;
	}

	@Override
	protected int getColumnType(int col) {
		switch (col) {
		case 1:
			return ListGrid.COLUMN_TYPE_SELECT;
		case 2:
			return ListGrid.COLUMN_TYPE_TEXTBOX;
		case 3:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 4:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 5:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 6:
			return ListGrid.COLUMN_TYPE_SELECT;
		case 7:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		default:
			return ListGrid.COLUMN_TYPE_IMAGE;
		}
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "", Accounter.constants().name(),
				Accounter.constants().description(),
				Accounter.constants().quantity(),
				Accounter.constants().unitPrice(),
				Accounter.constants().total(),
				Accounter.constants().newVATCode(),
				Accounter.constants().vat(), " " };
	}

	@Override
	protected boolean isEditable(ClientTransactionItem obj, int row, int col) {
		if (obj == null)
			return false;
		if (!Accounter.getCompany().getPreferences().getDoYouPaySalesTax()) {
			if (col == 6 || col == 7)
				return false;
		}
		switch (obj.getType()) {
		case ClientTransactionItem.TYPE_COMMENT:
			switch (col) {
			case 2:
				return true;
			default:
				return false;
			}
		case ClientTransactionItem.TYPE_ITEM:
			switch (col) {
			case 5:
				return false;
			case 7:
				return false;
			default:
				return true;
			}
		case ClientTransactionItem.TYPE_SALESTAX:
			switch (col) {
			case 3:
				return false;
			case 4:
				return true;
			case 5:
				return false;
			case 7:
				return false;
			default:
				if (selectedObject.getType() == TYPE_SALESTAX && col == 6) {
					return false;
				}
				return true;
			}
		case ClientTransactionItem.TYPE_ACCOUNT:
			switch (col) {
			// case 3:
			// return false;
			// case 4:
			// return false;
			case 7:
				return false;
			default:
				return true;
			}
		case ClientTransactionItem.TYPE_SERVICE:
			switch (col) {
			case 3:
				return true;
			case 4:
				return true;

			default:
				return true;
			}
		}

		return true;

	}

	@Override
	protected Object getColumnValue(ClientTransactionItem item, int col) {

		if (item.getType() == ClientTransactionItem.TYPE_COMMENT) {

			if (!Arrays.asList(0, 2, 8).contains(col))
				return "..";
		}
		// if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
		//
		// if (Arrays.asList(3).contains(col))
		// return "";
		// }
		if (item.getType() == ClientTransactionItem.TYPE_SALESTAX) {
			if (!Arrays.asList(0, 1, 4, 6, 8).contains(col))
				return "";
		}
		// if (item.getType() == ClientTransactionItem.TYPE_SERVICE) {
		// if (!Arrays.asList(0, 1, 2, 5, 6, 7, 8).contains(col))
		// return "";
		// }

		switch (col) {
		case 0:
			return getImageByType(item.getType());
		case 1:
			return getNameValue(item);
		case 2:
			return item.getDescription();
		case 3:
			if (item.getType() != ClientTransactionItem.TYPE_ACCOUNT)
				return item.getQuantity();
			else {
				return (item.getQuantity() != null || item.getLineTotal() == 0) ? item
						.getQuantity() : "";
			}
		case 4:
			if (item.getType() != ClientTransactionItem.TYPE_ACCOUNT)
				return DataUtils.getAmountAsString(item.getUnitPrice());
			else {
				return (item.getUnitPrice() != 0 || item.getLineTotal() == 0) ? DataUtils
						.getAmountAsString(item.getUnitPrice()) : "";
			}
		case 5:
			return DataUtils.getAmountAsString(item.getLineTotal());
		case 6:
			return getTAXCodeName(item.getTaxCode());
		case 7:
			return DataUtils.getAmountAsString(item.getVATfraction());
		case 8:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		default:
			return "";
		}
	}

	private String getDecimalsUsingMaxDecimals(double quantity,
			int maxDecimalPoint) {
		String qty = String.valueOf(quantity);
		// if (maxDecimalPoint != 0) {
		// qty = !qty.contains(".") ? qty + ".0" : qty;
		// } else {
		// return qty;
		// }
		// int max = qty.substring(qty.indexOf(".") + 1).length();
		// if (maxDecimalPoint > max) {
		// for (int i = max; maxDecimalPoint != i; i++) {
		// qty = qty + "0";
		// }
		// }
		return qty;
	}

}
