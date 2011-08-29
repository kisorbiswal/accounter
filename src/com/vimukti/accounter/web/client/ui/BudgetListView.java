package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Lists.BudgetList;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.BudgetListGrid;

/**
 * 
 * @author Amrit Mishra
 * 
 */
public class BudgetListView extends BaseListView<BudgetList> {

	AccounterConstants customerConstants;
	private List<BudgetList> listOfBudgets;

	@Override
	protected void initGrid() {
		grid = new BudgetListGrid();
		grid.init();

	}

	@Override
	public void init() {
		customerConstants = Accounter.constants();
		super.init();

	}

	@Override
	protected String getListViewHeading() {
		// return Accounter.messages().customerList(Global.get().Customer());
		return Accounter.constants().budgetList();
	}

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return ActionFactory.getNewBudgetAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		return Accounter.constants().addNewBudget();
	}

	@Override
	protected String getViewTitle() {
		return Accounter.messages().customers(Global.get().Customer());

	}

	@Override
	public void updateInGrid(BudgetList objectTobeModified) {
		// TODO Auto-generated method stub

	}

}
