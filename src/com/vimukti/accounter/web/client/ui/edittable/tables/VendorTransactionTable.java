package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;

public abstract class VendorTransactionTable extends
		EditTable<ClientTransactionItem> {

	// ServiceCombo serviceItemCombo;
	// ProductCombo productItemCombo;
	// TAXCodeCombo taxCodeCombo;
	// VATItemCombo vatItemCombo;
	AccounterConstants accounterConstants = Accounter.constants();
	private Double totallinetotal = 0.0;
	private Double totalVat = 0.0;
	private Double grandTotal = 0.0;
	private double taxableTotal;
	private int accountingType;
	// protected boolean isPurchseOrderTransaction;
	// protected int maxDecimalPoint;
	protected boolean needDiscount = true;

	public VendorTransactionTable() {
		initColumns();
	}

	public VendorTransactionTable(boolean needDiscount) {
		this.needDiscount = needDiscount;
		initColumns();
	}

	protected abstract void initColumns();

	@Override
	public void update(ClientTransactionItem row) {
		super.update(row);
		updateTotals();
	}

	protected ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	private ImageResource getImage(ClientTransactionItem row) {
		switch (row.getType()) {
		case ClientTransactionItem.TYPE_ITEM:
			return Accounter.getFinanceImages().itemsIcon();
		case ClientTransactionItem.TYPE_ACCOUNT:
			return Accounter.getFinanceImages().AccountsIcon();
		case ClientTransactionItem.TYPE_COMMENT:
			return Accounter.getFinanceImages().CommentsIcon();
		case ClientTransactionItem.TYPE_SALESTAX:
			return Accounter.getFinanceImages().salesTaxIcon();
		default:
			break;
		}
		return Accounter.getFinanceImages().errorImage();
	}

	public void removeAllRecords() {
		clear();
	}

	public void setRecords(List<ClientTransactionItem> transactionItems) {
		setAllRows(transactionItems);
	}

	public void updateTotals() {
		List<ClientTransactionItem> allrecords = getRecords();
		int totaldiscount = 0;
		totallinetotal = 0.0;
		taxableTotal = 0.0;
		totalVat = 0.0;
		for (ClientTransactionItem rec : allrecords) {

			int type = rec.getType();

			if (type == 0)
				continue;

			totaldiscount += rec.getDiscount();

			Double lineTotalAmt = rec.getLineTotal();
			totallinetotal += lineTotalAmt;

			// ClientItem item = getCompany().getItem(rec.getItem());
			if (rec != null && rec.isTaxable()) {
				// ClientTAXItem taxItem = getCompany().getTAXItem(
				// rec.getTaxCode());
				// if (taxItem != null) {
				// totalVat += taxItem.getTaxRate() / 100 * lineTotalAmt;
				// }
				taxableTotal += lineTotalAmt;
			}
			rec.setVATfraction(getVATAmount(rec.getTaxCode(), rec));
			totalVat += rec.getVATfraction();
			super.update(rec);
		}
		// if (isPurchseOrderTransaction) {
		// this.addFooterValue(amountAsString(totallinetotal), 7);
		// } else {
		// this.addFooterValue(amountAsString(totallinetotal), 5);
		// }

		if (getCompany().getPreferences().isChargeSalesTax())
			grandTotal = totalVat + totallinetotal;
		else {
			// if (transactionView.vatinclusiveCheck != null
			// && (Boolean) transactionView.vatinclusiveCheck.getValue()) {
			// grandTotal = totallinetotal - totalVat;
			//
			// } else {
			grandTotal = totallinetotal;
			totallinetotal = grandTotal + totalVat;
			// }
		}

		updateNonEditableItems();

		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_UK
		// && !isPurchseOrderTransaction) {
		// this.addFooterValue(amountAsString(totalVat), 7);
		// }
	}

	protected abstract void updateNonEditableItems();

	public List<ClientTransactionItem> getRecords() {
		return getAllRows();
	}

	public void setTaxCode(long taxCode) {
		for (ClientTransactionItem item : getRecords()) {
			if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT
					&& item.getTaxCode() == 0) {
				// Only set this for account and if we have not specified
				// already
				// This works only once
				item.setTaxCode(taxCode);
				update(item);
			}
		}
		updateTotals();
	}

	protected abstract ClientTransaction getTransactionObject();

	protected abstract ClientVendor getSelectedVendor();

	public double getTotal() {
		return totallinetotal != null ? totallinetotal.doubleValue() : 0.0d;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void addRecords(List<ClientTransactionItem> transactionItems) {
		for (ClientTransactionItem item : transactionItems) {
			item.setID(0);
			item.taxRateCalculationEntriesList.clear();
			add(item);
		}
	}

	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		int validationcount = 1;
		for (ClientTransactionItem item : this.getRecords()) {
			int row = this.getRecords().indexOf(item);
			if (item.isEmpty()) {
				continue;
			}
			switch (validationcount++) {
			case 1:
				if (item.getAccountable() == null) {
					// result.addError(row + "," + 1, Accounter.messages()
					// .pleaseSelectCustomer(
					// UIUtils.getTransactionTypeName(item
					// .getType())));
					result.addError(
							row + "," + 1,
							Accounter.messages().pleaseSelectCustomer(
									Utility.getItemType(item.getType())));
				}
				// ,
				// UIUtils.getTransactionTypeName(item.getType()));
			case 2:
				if (getCompany().getPreferences().isRegisteredForVAT()) {
					if (item.getTaxCode() == 0) {
						result.addError(row + "," + 6, Accounter.messages()
								.pleaseEnter(Accounter.constants().vatCode()));
					}
					// .vatCode());
					validationcount = 1;
				} else
					validationcount = 1;
				break;
			default:
				break;
			}

		}
		if (DecimalUtil.isLessThan(totallinetotal, 0.0)) {
			result.addError(this, Accounter.constants()
					.invalidTransactionAmount());
			// Accounter.showError(AccounterErrorType.InvalidTransactionAmount);
			// return false;
		}
		return result;
	}

	public void setAllTransactionItems(
			List<ClientTransactionItem> transactionItems) {
		setAllRows(transactionItems);
	}

	public double getVATAmount(long TAXCodeID, ClientTransactionItem record) {

		double vatRate = 0.0;
		try {
			if (TAXCodeID != 0) {
				// Checking the selected object is VATItem or VATGroup.
				// If it is VATItem,the we should get 'VATRate',otherwise
				// 'GroupRate
				vatRate = UIUtils.getVATItemRate(Accounter.getCompany()
						.getTAXCode(TAXCodeID), false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Double vat = 0.0;
		if (isShowPriceWithVat()) {
			// TODO raj
			vat = ((ClientTransactionItem) record).getLineTotal()
					- (100 * (((ClientTransactionItem) record).getLineTotal() / (100 + vatRate)));
		} else {
			vat = ((ClientTransactionItem) record).getLineTotal() * vatRate
					/ 100;
		}
		vat = UIUtils.getRoundValue(vat);
		return vat.doubleValue();
	}

	public abstract boolean isShowPriceWithVat();

	@Override
	public void delete(ClientTransactionItem row) {
		super.delete(row);
		updateTotals();
	}

	@Override
	public void setAllRows(List<ClientTransactionItem> rows) {
		for (ClientTransactionItem item : rows) {
			item.setID(0);
			item.taxRateCalculationEntriesList.clear();
		}
		super.setAllRows(rows);
	}
}
