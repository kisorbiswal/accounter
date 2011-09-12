package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccountable;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.ProductCombo;
import com.vimukti.accounter.web.client.ui.combo.ServiceCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VATItemCombo;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.DescriptionEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.ImageEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionDiscountColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionItemNameColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionQuantityColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionTotalColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionUnitPriceColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatCodeColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatColumn;

public abstract class VendorTransactionTable extends
		EditTable<ClientTransactionItem> {

	ServiceCombo serviceItemCombo;
	ProductCombo productItemCombo;
	TAXCodeCombo taxCodeCombo;
	VATItemCombo vatItemCombo;
	AccounterConstants accounterConstants = Accounter.constants();
	private Double totallinetotal = 0.0;
	private Double totalVat = 0.0;
	private Double grandTotal;
	private double taxableTotal;
	private int accountingType;
	protected boolean isPurchseOrderTransaction;
	protected int maxDecimalPoint;

	public VendorTransactionTable() {
		initTable();
	}

	private void initTable() {
		this.addColumn(new ImageEditColumn<ClientTransactionItem>() {

			@Override
			public ImageResource getResource(ClientTransactionItem row) {
				return VendorTransactionTable.this.getImage(row);
			}

			@Override
			public int getWidth() {
				return 20;
			}

		});

		this.addColumn(new TransactionItemNameColumn() {

			@Override
			public ListFilter<ClientAccount> getAccountsFilter() {
				return new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {

						if (account.getType() != ClientAccount.TYPE_CASH
								&& account.getType() != ClientAccount.TYPE_BANK
								&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
								&& account.getType() != ClientAccount.TYPE_INCOME
								&& account.getType() != ClientAccount.TYPE_OTHER_INCOME
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
								&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
								&& account.getType() != ClientAccount.TYPE_EQUITY
								&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY) {
							return true;
						} else {
							return false;
						}
					}
				};

			}

			@Override
			protected void setValue(ClientTransactionItem row,
					IAccountable newValue) {
				super.setValue(row, newValue);
				update(row);
			}
		});

		this.addColumn(new DescriptionEditColumn());

		this.addColumn(new TransactionQuantityColumn());

		this.addColumn(new TransactionUnitPriceColumn());

		this.addColumn(new TransactionDiscountColumn());

		this.addColumn(new TransactionTotalColumn());

		if (getCompany().getPreferences().isRegisteredForVAT()) {

			this.addColumn(new TransactionVatCodeColumn());

			this.addColumn(new TransactionVatColumn());
		}

		this.addColumn(new DeleteColumn<ClientTransactionItem>());
	}

	@Override
	public void update(ClientTransactionItem row) {
		updateTotals();
		super.update(row);
	}

	private ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	private ImageResource getImage(ClientTransactionItem row) {
		switch (row.getType()) {
		case ClientTransactionItem.TYPE_ITEM:
			return Accounter.getFinanceImages().itemsIcon();
		case ClientTransactionItem.TYPE_SERVICE:
			return Accounter.getFinanceImages().salesTaxIcon();
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

			ClientItem item = getCompany().getItem(rec.getItem());
			if (item != null && item.isTaxable()) {
				taxableTotal += lineTotalAmt;
			}
			totalVat += rec.getVATfraction();
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
			add(item);
		}
	}

	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		int validationcount = 1;
		for (ClientTransactionItem item : this.getRecords()) {
			int row = this.getRecords().indexOf(item);
			if (item.getType() != ClientTransactionItem.TYPE_COMMENT) {
				switch (validationcount++) {
				case 1:
					if (item.getAccountable() == null) {
						result.addError(
								row + "," + 1,
								Accounter.messages().pleaseEnter(
										UIUtils.getTransactionTypeName(item
												.getType())));
						result.addError(
								row + "," + 1,
								Accounter.messages().pleaseEnter(
										Utility.getTransactionName(item
												.getType())));
					}
					// ,
					// UIUtils.getTransactionTypeName(item.getType()));
				case 2:
					if (getCompany().getPreferences().isRegisteredForVAT()) {
						if (item.getTaxCode() == 0) {
							result.addError(
									row + "," + 6,
									Accounter.messages().pleaseEnter(
											Accounter.constants().vatCode()));
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
}
