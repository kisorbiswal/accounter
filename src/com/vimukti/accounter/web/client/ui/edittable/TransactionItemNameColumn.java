package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccountable;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ItemView;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public abstract class TransactionItemNameColumn extends
		ComboColumn<ClientTransactionItem, IAccountable> {

	AccountDropDownTable accountsList = new AccountDropDownTable(
			getAccountsFilter());

	ItensDropDownTable itemsList = new ItensDropDownTable(getItemsFilter());

	@Override
	protected IAccountable getValue(ClientTransactionItem row) {
		return row.getAccountable();
	}

	public abstract ListFilter<ClientItem> getItemsFilter();

	public abstract ListFilter<ClientAccount> getAccountsFilter();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AbstractDropDownTable getDisplayTable(ClientTransactionItem row) {
		switch (row.getType()) {
		case ClientTransactionItem.TYPE_ACCOUNT:
			return accountsList;
		case ClientTransactionItem.TYPE_ITEM:
			itemsList.setItemType(ItemView.NON_INVENTORY_PART);
			return itemsList;
		case ClientTransactionItem.TYPE_SERVICE:
			itemsList.setItemType(ItemView.TYPE_SERVICE);
			return itemsList;
		default:
			break;
		}
		return null;
	}

	@Override
	public int getWidth() {
		return 150;
	}

	@Override
	protected void setValue(ClientTransactionItem row, IAccountable newValue) {
		row.setAccountable(newValue);
		if (newValue != null) {
			if (newValue instanceof ClientItem) {
				ClientItem selectItem = (ClientItem) newValue;
				row.setUnitPrice(selectItem.getSalesPrice());
				row.setTaxable(selectItem.isTaxable());
				double lt = row.getQuantity().getValue() * row.getUnitPrice();
				double disc = row.getDiscount();
				row.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
						* disc / 100)) : lt);

				if (Accounter.getCompany().getPreferences()
						.isRegisteredForVAT()) {
					row.setTaxCode(selectItem.getTaxCode() != 0 ? selectItem
							.getTaxCode() : 0);
				}
			} else {
				row.setTaxable(true);
			}
		}
	}

	@Override
	protected String getColumnName() {
		return Accounter.constants().name();
	}

	public void setItemForCustomer(boolean isForCustomer) {
		itemsList.setForCustomer(isForCustomer);
	}
}
