package com.vimukti.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ItemView;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class ItensDropDownTable extends AbstractDropDownTable<ClientItem> {

	private ListFilter<ClientItem> filter;

	public ItensDropDownTable(ListFilter<ClientItem> filter) {
		super(getProducts(filter));
		this.filter = filter;
	}

	private static List<ClientItem> getProducts(ListFilter<ClientItem> filter) {
		ArrayList<ClientItem> filteredList = Utility.filteredList(filter,
				Accounter.getCompany().getItems());
		return filteredList;
	}

	@Override
	public void initColumns() {
		TextColumn<ClientItem> textColumn = new TextColumn<ClientItem>() {

			@Override
			public String getValue(ClientItem object) {
				return object.getDisplayName();
			}
		};
		this.addColumn(textColumn);
	}

	@Override
	protected boolean filter(ClientItem t, String string) {
		return t.getDisplayName().toLowerCase().startsWith(string);
	}

	@Override
	protected String getDisplayValue(ClientItem value) {
		return value.getDisplayName();
	}

	@Override
	protected ClientItem getAddNewRow() {
		ClientItem clientItem = new ClientItem();
		clientItem.setName(Accounter.constants().addNewItem());
		return clientItem;
	}

	@Override
	public void addNewItem() {
		NewItemAction action;
		action = ActionFactory.getNewItemAction(false);
		action.setCallback(new ActionCallback<ClientItem>() {

			@Override
			public void actionResult(ClientItem result) {
				if (result.isActive()) {
					selectRow(result);
				}

			}
		});
		action.setType(ItemView.TYPE_SERVICE);
		action.run(null, true);
	}

	@Override
	protected Class<?> getType() {
		return ClientItem.class;
	}

	@Override
	public List<ClientItem> getTotalRowsData() {
		return getProducts(filter);
	}
}
