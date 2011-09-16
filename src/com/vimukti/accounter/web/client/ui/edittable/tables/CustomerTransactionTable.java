package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccountable;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
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

/**
 * This class is used for every Customer Transactions
 * 
 * @author vimukti04
 * 
 */
public abstract class CustomerTransactionTable extends
		EditTable<ClientTransactionItem> {
	private Double totallinetotal = 0.0d;
	private double taxableTotal;
	private double totalVat;
	private double grandTotal;
	private double totalValue;
	private ClientCompany company;
	private Object taxCode;
	private long ztaxCodeid;
	private ClientPriceLevel priceLevel;

	public CustomerTransactionTable() {
		initColumns();
		company = Accounter.getCompany();
	}

	protected void initColumns() {
		this.addColumn(new ImageEditColumn<ClientTransactionItem>() {

			@Override
			public ImageResource getResource(ClientTransactionItem row) {
				return CustomerTransactionTable.this.getImage(row);
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
						if (getCompany().getPreferences().isRegisteredForVAT()) {
							if (Arrays.asList(ClientAccount.TYPE_BANK,
									ClientAccount.TYPE_CREDIT_CARD,
									ClientAccount.TYPE_OTHER_CURRENT_ASSET,
									ClientAccount.TYPE_FIXED_ASSET).contains(
									account.getType())) {
								return true;
							}
						} else {
							if (Arrays.asList(ClientAccount.TYPE_CREDIT_CARD,
									ClientAccount.TYPE_OTHER_CURRENT_ASSET,
									ClientAccount.TYPE_BANK,
									ClientAccount.TYPE_FIXED_ASSET).contains(
									account.getType())) {

								return true;
							}
						}
						return false;
					}
				};
			}

			@Override
			public ListFilter<ClientItem> getItemsFilter() {
				return new ListFilter<ClientItem>() {

					@Override
					public boolean filter(ClientItem e) {
						return e.isISellThisItem();
					}
				};
			}

			@Override
			protected void setValue(ClientTransactionItem row,
					IAccountable newValue) {
				super.setValue(row, newValue);
				applyPriceLevel(row);
			}
		});

		this.addColumn(new DescriptionEditColumn());

		this.addColumn(new TransactionQuantityColumn());

		this.addColumn(new TransactionUnitPriceColumn());

		this.addColumn(new TransactionDiscountColumn());

		this.addColumn(new TransactionTotalColumn());

		// if (getCompany().getPreferences().isChargeSalesTax()) {

		if (getCompany().getPreferences().isRegisteredForVAT()) {

			this.addColumn(new TransactionVatCodeColumn());

			this.addColumn(new TransactionVatColumn());
		}

		// }

		this.addColumn(new DeleteColumn<ClientTransactionItem>());
	}

	protected ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	protected ImageResource getImage(ClientTransactionItem row) {
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

	protected void applyPriceLevel(ClientTransactionItem item) {
		priceLevelSelected(priceLevel);
		selectedRecord = item;
		if (priceLevel != null) {
			setUnitPriceForSelectedItem(getCompany().getItem(item.getItem()));
		} else {
			update(item);
		}
	}

	public void setPricingLevel(ClientPriceLevel priceLevel) {
		this.priceLevel = priceLevel;
	}

	public void refreshVatValue(ClientTransactionItem obj) {
		ClientTransactionItem record = (ClientTransactionItem) obj;

		long taxCode = obj.getTaxCode();
		if (taxCode == 0)
			return;
		record.setVATfraction(getVATAmount(taxCode, record));

		updateTotals();
		update(record);
	}

	public double getVATAmount(long TAXCodeID, ClientTransactionItem record) {

		double vatRate = 0.0;
		if (TAXCodeID != 0) {
			// Checking the selected object is VATItem or VATGroup.
			// If it is VATItem,the we should get 'VATRate',otherwise 'GroupRate
			try {
				ClientTAXItemGroup item = company.getTAXItemGroup(company
						.getTAXCode(TAXCodeID).getTAXItemGrpForSales());
				if (item == null) {
					vatRate = 0.0;
				} else if (item instanceof ClientTAXItem) {
					// The selected one is VATItem,so get 'VATRate' from
					// 'VATItem'
					vatRate = ((ClientTAXItem) item).getTaxRate();
				} else {
					// The selected one is VATGroup,so get 'GroupRate' from
					// 'VATGroup'
					vatRate = ((ClientTAXGroup) item).getGroupRate();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Double vat = 0.0;
		if (isShowPriceWithVat()) {
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
	public void update(ClientTransactionItem row) {
		super.update(row);
		updateTotals();
	}

	public void updateTotals() {

		List<ClientTransactionItem> allrecords = getAllRows();
		int totaldiscount = 0;
		totallinetotal = 0.0;
		taxableTotal = 0.0;
		totalVat = 0.0;
		int accountType = Accounter.getCompany().getAccountingType();

		for (ClientTransactionItem citem : allrecords) {
			ClientTransactionItem record = (ClientTransactionItem) citem;

			totaldiscount += citem.getDiscount();

			Double lineTotalAmt = citem.getLineTotal();
			totallinetotal += lineTotalAmt;

			if (citem != null && citem.isTaxable()) {
				// ClientTAXItem taxItem = getCompany().getTAXItem(
				// citem.getTaxCode());
				// if (taxItem != null) {
				// totalVat += taxItem.getTaxRate() / 100 * lineTotalAmt;
				// }
				taxableTotal += lineTotalAmt;
			}

			record.setVATfraction(getVATAmount(citem.getTaxCode(), record));
			totalVat += record.getVATfraction();
			super.update(citem);
			// totalVat += citem.getVATfraction();
		}

		if (getCompany().getPreferences().isChargeSalesTax()) {
			grandTotal = totalVat + totallinetotal;
		} else {
			grandTotal = totallinetotal;
			totalValue = grandTotal;
		}
		if (getCompany().getPreferences().isRegisteredForVAT()) {
			// if (transactionView.vatinclusiveCheck != null
			// && (Boolean) transactionView.vatinclusiveCheck.getValue()) {
			// grandTotal = totallinetotal - totalVat;
			// setTotalValue(totallinetotal);
			//
			// } else {
			grandTotal = totallinetotal;
			totalValue = grandTotal + totalVat;
			// }
		} else {
			grandTotal = totallinetotal;
			totalValue = grandTotal;
		}

		updateNonEditableItems();
	}

	public abstract void updateNonEditableItems();

	public Double getTaxableLineTotal() {
		return taxableTotal;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public double getTotalValue() {
		return totalValue;
	}

	public Double getTotal() {
		return totallinetotal != null ? totallinetotal.doubleValue() : 0.0d;
	}

	public void refreshVatValue() {
		List<ClientTransactionItem> allrecords = getAllRows();
		for (ClientTransactionItem record : allrecords) {
			// if (record.getTaxCode() != null)
			refreshVatValue(record);
		}
		updateTotals();
	}

	public void setTaxCode(long taxCode) {
		this.taxCode = taxCode;
		refreshVatValue();
	}

	public void setCustomerTaxCode(ClientTransactionItem selectedObject) {
		List<ClientTAXCode> taxCodes = company.getActiveTaxCodes();
		for (ClientTAXCode taxCode : taxCodes) {
			if (taxCode.getName().equals("S")) {
				ztaxCodeid = taxCode.getID();
			}
		}
		Object transactionObj = null;
		if (transactionObj == null) {
			if (getCustomer() != null)
				selectedObject
						.setTaxCode(selectedObject.getTaxCode() != 0 ? selectedObject
								.getTaxCode()
								: getCustomer().getTAXCode() > 0 ? getCustomer()
										.getTAXCode() : ztaxCodeid);
			else
				selectedObject.setTaxCode(ztaxCodeid);
		}
		updateTotals();
		update(selectedObject);
	}

	protected abstract ClientCustomer getCustomer();

	public void priceLevelSelected(ClientPriceLevel priceLevel) {
		this.priceLevel = priceLevel;
	}

	private ClientTransactionItem selectedRecord;

	public void updatePriceLevel() {
		for (ClientTransactionItem item : getAllRows()) {
			if (item.getType() == ClientTransactionItem.TYPE_ITEM) {
				selectedRecord = item;
				if (priceLevel != null)
					setUnitPriceForSelectedItem(company.getItem(item.getItem()));

			}
		}

		updateTotals();
	}

	protected void setUnitPriceForSelectedItem(ClientItem selectedItem) {

		ClientTransactionItem record = selectedRecord;

		if (record == null)
			return;
		Double calculatedUnitPrice = Utility
				.getCalculatedItemUnitPriceForPriceLevel(selectedItem,
						priceLevel, false);

		if (calculatedUnitPrice == null)
			return;

		record.setItem(selectedItem.getID());

		record.setUnitPrice(calculatedUnitPrice);

		record.setDescription(selectedItem.getName());

		if (company.getAccountingType() == 1) {
			// if (selectedItem.getVatCode() != null) {
			// record.setTaxCode(selectedItem.getVatCode());
			// refreshVatValue(record);
			// }
		} else {
			record.setTaxable(selectedItem.isTaxable());

		}
		double lt = record.getQuantity().getValue() * record.getUnitPrice();
		double disc = record.getDiscount();
		record.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
				* disc / 100)) : lt);
		update(record);

	}

	public void removeAllRecords() {
		clear();
	}

	public void setRecords(List<ClientTransactionItem> transactionItems) {
		setAllRows(transactionItems);
	}

	public List<ClientTransactionItem> getRecords() {
		return getAllRows();
	}

	public void setAllTransactionItems(
			List<ClientTransactionItem> transactionItems) {
		setAllRows(transactionItems);
	}

	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		// Validations
		// 1. checking for the name of the transaction item
		// 2. checking for the vat code if the company is of type UK
		// TODO::: check whether this validation is working or not
		for (ClientTransactionItem item : this.getRecords()) {
			if (item.getType() == ClientTransactionItem.TYPE_COMMENT) {
				continue;
			}
			if (item.getAccountable() == null) {
				result.addError(
						"GridItem-" + item.getAccount(),
						Accounter.messages().pleaseEnter(
								UIUtils.getTransactionTypeName(item.getType())));
			}
			if (getCompany().getPreferences().isRegisteredForVAT()) {
				if (item.getTaxCode() == 0) {
					result.addError(
							"GridItemUK-" + item.getAccount(),
							Accounter.messages().pleaseEnter(
									Accounter.constants().vatCode()));
				}

			}
		}
		return result;
	}

	@Override
	public void delete(ClientTransactionItem row) {
		super.delete(row);
		updateTotals();
	}

	@Override
	public void setAllRows(List<ClientTransactionItem> rows) {
		for (ClientTransactionItem item : rows) {
			item.setID(0);
		}
		super.setAllRows(rows);
	}
}
