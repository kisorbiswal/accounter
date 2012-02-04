package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientInventoryAssemblyItem;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.ComboColumn;
import com.vimukti.accounter.web.client.ui.edittable.ItemsDropDownTable;

public class InventoryItemNameColumn extends
		ComboColumn<ClientInventoryAssemblyItem, ClientItem> {

	ItemsDropDownTable itemsList = new ItemsDropDownTable(getItemsFilter());
	private boolean isSales;
	private final ICurrencyProvider currencyProvider;
	private ClientPriceLevel priceLevel;

	public InventoryItemNameColumn(boolean isSales,
			ICurrencyProvider currencyProvider) {
		this.isSales = isSales;
		this.currencyProvider = currencyProvider;
	}

	@Override
	protected ClientItem getValue(ClientInventoryAssemblyItem row) {
		return (ClientItem) row.getInventoryItem();
	}

	public ListFilter<ClientItem> getItemsFilter() {
		return new ListFilter<ClientItem>() {

			@Override
			public boolean filter(ClientItem e) {
				return (e.getType() == ClientItem.TYPE_INVENTORY_PART);
			}
		};
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AbstractDropDownTable getDisplayTable(ClientInventoryAssemblyItem row) {
		itemsList = new ItemsDropDownTable(getItemsFilter()) {
			@Override
			protected ClientItem getAddNewRow() {
				ClientCompany company = Accounter.getCompany();
				ClientItem clientItem = new ClientItem();
				boolean sellServices = company.getPreferences()
						.isSellServices();
				boolean sellProducts = company.getPreferences()
						.isSellProducts();
				if (sellServices && sellProducts) {
					clientItem.setName(messages.comboDefaultAddNew(messages
							.inventoryItem()));
				}
				return clientItem;
			}

			@Override
			public void addNewItem(String text) {
				NewItemAction action;
				action = ActionFactory.getNewItemAction(isForCustomer());
				action.setCallback(new ActionCallback<ClientItem>() {

					@Override
					public void actionResult(ClientItem result) {
						if (result.isActive()
								&& getItemsFilter().filter(result)) {
							selectRow(result);
						}

					}
				});
				action.setType(ClientItem.TYPE_INVENTORY_PART);
				action.setItemText(text);
				action.run(null, false);
			}
		};
		return itemsList;
	}

	@Override
	public int getWidth() {
		return 150;
	}

	@Override
	protected void setValue(ClientInventoryAssemblyItem row, ClientItem newValue) {
		if (newValue != null) {
			Double unitPrice = 0.0;
			if (isSales()) {
				if (row.getUnitPrice() == null || !isSameItems(row, newValue)) {
					unitPrice = newValue.getSalesPrice()
							/ currencyProvider.getCurrencyFactor();
				} else {
					unitPrice = row.getUnitPrice();
				}
			} else {
				if (row.getUnitPrice() == null || !isSameItems(row, newValue)) {
					unitPrice = newValue.getPurchasePrice()
							/ currencyProvider.getCurrencyFactor();
				} else {
					unitPrice = row.getUnitPrice();
				}
			}
			if (unitPrice.equals((0.0 / 0.0))) {
				unitPrice = 0.0;
			}
			if (getPreferences().isPricingLevelsEnabled()) {
				if (priceLevel != null && !priceLevel.isDefault()) {
					row.setUnitPrice(getPriceLevel(unitPrice));
				} else {
					row.setUnitPrice(unitPrice);
				}
			} else {
				row.setUnitPrice(unitPrice);
			}
		}
		row.setInventoryItem(newValue);
		onValueChange(row);
		row.setDescription(getDiscription(newValue));
		if (row.getQuantity() == null) {
			ClientQuantity quantity = new ClientQuantity();
			quantity.setValue(1.0);
			row.setQuantity(quantity);
		}
		getTable().update(row);
	}

	private boolean isSameItems(ClientInventoryAssemblyItem row,
			ClientItem newValue) {
		if (row.getInventoryItem().getID() == newValue.getID()) {
			return true;
		}
		return false;
	}

	private boolean isSales() {
		return isSales;
	}

	protected String getDiscription(ClientItem newValue) {
		return newValue.getPurchaseDescription();
	}

	@Override
	protected String getColumnName() {
		return messages.name();
	}

	public void setItemForCustomer(boolean isForCustomer) {
		itemsList.setForCustomer(isForCustomer);
	}

	public void setSales(boolean isSales) {
		this.isSales = isSales;
	}

	protected Double getPriceLevel(Double unitPrice) {
		double amount = 0.0;
		if (unitPrice == 0) {
			return unitPrice;
		} else {
			if (priceLevel.isPriceLevelDecreaseByThisPercentage()) {
				amount = unitPrice
						- (priceLevel.getPercentage() * (unitPrice / 100));
			} else {
				amount = unitPrice
						+ (priceLevel.getPercentage() * (unitPrice / 100));
			}

		}
		return amount;
	}

	public void setPriceLevel(ClientPriceLevel priceLevel) {
		this.priceLevel = priceLevel;
	}

	@Override
	public boolean isPrimaryColumn() {
		return true;
	}
}