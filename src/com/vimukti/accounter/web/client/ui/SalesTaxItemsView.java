package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.ManageSalesTaxItemListGrid;

public class SalesTaxItemsView extends BaseListView<ClientTAXItem> {
	private List<ClientTAXItem> listOfTaxItems;

	@Override
	protected Action getAddNewAction() {

		if (Accounter.getUser().canDoInvoiceTransactions())
			return ActionFactory.getNewVatItemAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return Accounter.constants().addaNewTaxItem();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {

		return Accounter.constants().taxItemsList();
	}

	@Override
	protected void initGrid() {
		grid = new ManageSalesTaxItemListGrid(false);
		grid.addStyleName("listgrid-tl");
		grid.init();
		listOfTaxItems = getCompany().getTaxItems();
		filterList(true);

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	public void initListCallback() {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateInGrid(ClientTAXItem objectTobeModified) {
		// NOTHING TO DO.
	}

	@Override
	protected void filterList(boolean isActive) {
		grid.removeAllRecords();
		for (ClientTAXItem item : listOfTaxItems) {
			if (isActive) {
				if (item.isActive() == true)
					grid.addData(item);
			} else if (item.isActive() == false) {
				grid.addData(item);
			}

		}

	}


	@Override
	protected String getViewTitle() {
		String constant;
		if (Accounter.getUser().canDoInvoiceTransactions())
			constant = Accounter.constants().manageSalesItems();
		else
			constant = Accounter.constants().salesTaxItems();
		return constant;
	}

}
