package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientBudgetItem;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.BudgetListGrid;

/**
 * 
 * @author Amrit Mishra
 * 
 */
public class BudgetListView extends BaseListView<ClientBudget> {

	AccounterConstants customerConstants;

	SelectCombo currentView;

	BudgetListGrid gridView = new BudgetListGrid();

	List<ClientBudget> listOfBudgets = new ArrayList<ClientBudget>();

	@Override
	protected void initGrid() {
		grid = new BudgetListGrid();
		grid.init();
		currentView.setSelectedItem(1);
		initGridData(currentView.getSelectedValue(),
				listOfBudgets.get(currentView.getSelectedIndex()));
		Window.scrollTo(0, 0);
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
		return Accounter.messages()
				.customers(Global.get().constants().budget());

	}

	@Override
	public void updateInGrid(ClientBudget objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected SelectCombo getSelectItem() {
		currentView = new SelectCombo(Accounter.constants().currentBudget());
		currentView.setHelpInformation(true);

		listOfTypes = new ArrayList<String>();
		for (ClientBudget budgets : listOfBudgets) {
			listOfTypes.add(budgets.getBudgetName());
		}

		currentView.initCombo(listOfTypes);

		if (UIUtils.isMSIEBrowser())
			currentView.setWidth("150px");

		currentView
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (currentView.getSelectedValue() != null) {
							grid.setViewType(currentView.getSelectedValue());
							filterList(currentView.getSelectedValue(),
									listOfBudgets.get(currentView
											.getSelectedIndex()));
						}
					}
				});
		return currentView;
	}

	private void initGridData(String selectedValue, ClientBudget clientBudget) {
		grid.removeAllRecords();
		List<ClientBudgetItem> allBudgetItems = clientBudget.getBudgetItem();
		for (ClientBudgetItem item : allBudgetItems) {
			item.setAccountsName(item.getAccount().getName());
			grid.addData(item);
		}

	}

	protected void filterList(String selectedValue, ClientBudget clientBudget) {

		grid.removeAllRecords();
		List<ClientBudgetItem> allBudgetItems = clientBudget.getBudgetItem();
		for (ClientBudgetItem item : allBudgetItems) {
			item.setAccountsName(item.getAccount().getName());
			grid.addData(item);
		}

	}
}
