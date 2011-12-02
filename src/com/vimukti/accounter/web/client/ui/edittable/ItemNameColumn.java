package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;

public abstract class ItemNameColumn extends
		ComboColumn<ClientTransactionItem, ClientItem> {

	ItemsDropDownTable itemsList = new ItemsDropDownTable(getItemsFilter());
	private boolean isSales;
	private ICurrencyProvider currencyProvider;

	public ItemNameColumn(boolean isSales, ICurrencyProvider currencyProvider) {
		this.isSales = isSales;
		this.currencyProvider = currencyProvider;
	}

	@Override
	protected ClientItem getValue(ClientTransactionItem row) {
		return (ClientItem) row.getAccountable();
	}

	public abstract ListFilter<ClientItem> getItemsFilter();

	@Override
	@SuppressWarnings({ "unchecked" })
	public AbstractDropDownTable getDisplayTable(ClientTransactionItem row) {
		return itemsList;
	}

	@Override
	public int getWidth() {
		return 150;
	}

	@Override
	protected void setValue(ClientTransactionItem row, ClientItem newValue) {

		if (newValue != null) {
			if (isSales()) {
				row.setUnitPrice(newValue.getSalesPrice()
						/ currencyProvider.getCurrencyFactor());
			} else {
				row.setUnitPrice(newValue.getPurchasePrice()
						/ currencyProvider.getCurrencyFactor());
			}
			row.setAccountable(newValue);
			row.setDescription(getDiscription(newValue));
			// row.setUnitPrice(newValue.getSalesPrice());
			row.setTaxable(newValue.isTaxable());

			if (row.getQuantity() == null) {
				ClientQuantity quantity = new ClientQuantity();
				quantity.setValue(1.0);
				row.setQuantity(quantity);
			}
			if (row.getDiscount() == null) {
				row.setDiscount(new Double(0));
			}

			double lt = row.getQuantity().getValue() * row.getUnitPrice();
			double disc = row.getDiscount();
			row.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
					* disc / 100)) : lt);

			if (getPreferences().isTrackTax()
					&& getPreferences().isTaxPerDetailLine()) {
				ClientTAXCode taxCode = Accounter.getCompany().getTAXCode(
						newValue.getTaxCode());
				if (taxCode != null) {
					if (isSales() && taxCode.getTAXItemGrpForSales() != 0) {
						row.setTaxCode(taxCode.getID());
					} else if (!isSales()
							&& taxCode.getTAXItemGrpForPurchases() != 0) {
						row.setTaxCode(taxCode.getID());
					}
				}
			}
		}
	}

	private boolean isSales() {
		return isSales;
	}

	protected abstract String getDiscription(ClientItem item);

	@Override
	protected String getColumnName() {
		return Accounter.messages().name();
	}

	public void setItemForCustomer(boolean isForCustomer) {
		itemsList.setForCustomer(isForCustomer);
	}

	public void setSales(boolean isSales) {
		this.isSales = isSales;
	}
}
