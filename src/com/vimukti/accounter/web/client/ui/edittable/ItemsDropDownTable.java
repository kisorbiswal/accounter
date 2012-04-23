package com.vimukti.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;

public class ItemsDropDownTable extends AbstractDropDownTable<ClientItem> {
	private ListFilter<ClientItem> filter;
	private int type;
	private boolean isForCustomer = true;
	private int transactionType = 0;

	public ItemsDropDownTable(ListFilter<ClientItem> filter) {
		super(getItems(filter), true);
		this.filter = filter;
	}

	private static List<ClientItem> getItems(ListFilter<ClientItem> filter) {
		ArrayList<ClientItem> filteredList = Utility.filteredList(filter,
				Accounter.getCompany().getActiveItems());
		return filteredList;
	}

	@Override
	public void initColumns() {
		TextColumn<ClientItem> textColumn = new TextColumn<ClientItem>() {

			@Override
			public String getValue(ClientItem object) {

				if ((transactionType == ClientTransaction.TYPE_PURCHASE_ORDER
						|| transactionType == ClientTransaction.TYPE_CASH_PURCHASE || transactionType == ClientTransaction.TYPE_ENTER_BILL)
						&& object.getVendorItemNumber() != null
						&& !object.getVendorItemNumber().isEmpty()) {
					return object.getDisplayName() + "("
							+ object.getVendorItemNumber() + ")";
				} else {
					return object.getDisplayName();
				}
			}
		};
		this.addColumn(textColumn);
	}

	@Override
	protected boolean filter(ClientItem t, String string) {
		return getDisplayValue(t).toLowerCase().startsWith(string);
	}

	@Override
	protected String getDisplayValue(ClientItem object) {
		if ((transactionType == ClientTransaction.TYPE_PURCHASE_ORDER
				|| transactionType == ClientTransaction.TYPE_CASH_PURCHASE || transactionType == ClientTransaction.TYPE_ENTER_BILL)
				&& object.getVendorItemNumber() != null
				&& !object.getVendorItemNumber().isEmpty()) {
			return object.getDisplayName() + "(" + object.getVendorItemNumber()
					+ ")";
		} else {
			return object.getDisplayName();
		}

	}

	@Override
	protected ClientItem getAddNewRow() {
		ClientCompany company = Accounter.getCompany();
		ClientItem clientItem = new ClientItem();
		boolean sellServices = company.getPreferences().isSellServices();
		boolean sellProducts = company.getPreferences().isSellProducts();
		if (sellServices && sellProducts) {
			clientItem.setName(messages.comboDefaultAddNew(messages.item()));
		} else if (sellServices) {
			clientItem.setName(messages.comboDefaultAddNew(messages
					.serviceItem()));
		} else if (sellProducts) {
			clientItem.setName(messages.comboDefaultAddNew(messages
					.productItem()));
		}
		return clientItem;
	}

	@Override
	public void addNewItem(String text) {
		NewItemAction action;
		action = new NewItemAction(isForCustomer());
		action.setCallback(new ActionCallback<ClientItem>() {

			@Override
			public void actionResult(ClientItem result) {
				if (result.isActive() && filter.filter(result)) {
					selectRow(result);
				}

			}
		});
		action.setType(getItemType());
		action.setItemText(text);
		action.run(null, true);
	}

	@Override
	public void addNewItem() {
		addNewItem("");
	}

	public int getItemType() {
		return type;
	}

	public void setItemType(int type) {
		this.type = type;
	}

	@Override
	protected Class<?> getType() {
		return ClientItem.class;
	}

	@Override
	public List<ClientItem> getTotalRowsData() {
		return getItems(filter);
	}

	public boolean isForCustomer() {
		return isForCustomer;
	}

	public void setForCustomer(boolean isForCustomer) {
		this.isForCustomer = isForCustomer;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}
}
