package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.ManageSalesTaxItemListGrid;
import com.vimukti.accounter.web.client.ui.vat.VatActionFactory;

public class SalesTaxItemsView extends BaseListView<ClientTAXItem> {
	private List<ClientTAXItem> listOfTaxItems;

	@Override
	protected Action getAddNewAction() {

		if (FinanceApplication.getUser().canDoInvoiceTransactions())
			return VatActionFactory.getNewVatItemAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (FinanceApplication.getUser().canDoInvoiceTransactions())
			return FinanceApplication.getVATMessages().addaNewTaxItem();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {

		return FinanceApplication.getVATMessages().taxItemsList();
	}

	@Override
	protected void initGrid() {
		grid = new ManageSalesTaxItemListGrid(false);
		grid.addStyleName("listgrid-tl");
		grid.init();
		listOfTaxItems = FinanceApplication.getCompany().getTaxItems();
		filterList(true);

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListCallback() {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateInGrid(ClientTAXItem objectTobeModified) {
		// TODO Auto-generated method stub

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
	public void processupdateView(IAccounterCore core, int command) {

		super.processupdateView(core, command);
		filterList(true);
	}

}
