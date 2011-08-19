package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.RecurringsListGrid;

public class RecurringTransactionsListView extends BaseListView<ClientRecurringTransaction>{

	
	public RecurringTransactionsListView() {
		super();
	}
	
	@Override
	public void updateInGrid(ClientRecurringTransaction objectTobeModified) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initGrid() {
		grid = new RecurringsListGrid(false);
		grid.init();
		//filterList(false);
	}

	@Override
	protected String getListViewHeading() {		
		return "Recurring Transactions List";
	}

	
	@Override
	public void initListCallback() {		
		super.initListCallback();
		
		Accounter.createHomeService().getRecurringsList(this);
	}
	
	@Override
	protected Action getAddNewAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		// TODO Auto-generated method stub
		return "Recurring Label string";
	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return "RecurringTransactions";
	}

}
