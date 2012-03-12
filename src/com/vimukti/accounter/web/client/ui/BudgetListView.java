package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientBudgetItem;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
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

	// @Override
	// public void onEdit() {
	// ActionFactory.getNewBudgetAction().run((ClientBudget) data, false);
	//
	// }

	// BudgetListGrid gridView = new BudgetListGrid();

	List<ClientBudget> listOfBudgets = new ArrayList<ClientBudget>();

	public BudgetListView() {
		this.getElement().setId("BudgetListView");
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		super.deleteFailed(caught);
		AccounterException accounterException = (AccounterException) caught;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	protected void initGrid() {
		grid = new BudgetListGrid();
		grid.init();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getBudgetList(this);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected String getListViewHeading() {
		return messages.budgetList();
	}

	@Override
	protected Action getAddNewAction() {
		ActionFactory.getNewBudgetAction().run(listOfBudgets);
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		return messages.addNewBudget();
	}

	@Override
	protected String getViewTitle() {
		return messages.payees(messages.budgets());
	}

	@Override
	public void updateInGrid(ClientBudget objectTobeModified) {
		// TODO Auto-generated method stub

	}

	// @Override
	// protected SelectCombo getSelectItem() {
	// currentView = new SelectCombo(messages.currentBudget());
	// currentView.setHelpInformation(true);
	//
	// // listOfBudgets = getCompany().getBudget();
	// currentView.initCombo(listOfTypes);
	//
	// if (UIUtils.isMSIEBrowser())
	// currentView.setWidth("150px");
	//
	// currentView
	// .addSelectionChangeHandler(new
	// IAccounterComboSelectionChangeHandler<String>() {
	//
	// @Override
	// public void selectedComboBoxItem(String selectItem) {
	// if (currentView.getSelectedValue() != null) {
	// grid.setViewType(currentView.getSelectedValue());
	// initGridData(currentView.getSelectedValue(),
	// listOfBudgets.get(currentView
	// .getSelectedIndex()));
	// }
	// }
	// });
	// return currentView;
	// }

	@Override
	protected void changeBudgetGrid(int numberSelected) {
		grid.removeAllRecords();
		grid.setTotal();

		if (listOfBudgets.size() > 0) {
			ClientBudget budget = listOfBudgets.get(numberSelected);
			setData(budget);
			List<ClientBudgetItem> budgetItems = new ArrayList<ClientBudgetItem>();
			budgetItems = budget.getBudgetItem();
			for (ClientBudgetItem budgetItem : budgetItems) {

				budgetItem.setAccountsName(budgetItem.getAccount().getName());

				if (budgetItem.getTotalAmount() > 0) {
					grid.addData(budgetItem);
				}
			}
		}
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(messages.noRecordsToShow());

		getTotalLayout(grid);
	}

	@Override
	protected void filterList(boolean isActive) {
		grid.removeAllRecords();
		grid.setTotal();

		if (listOfBudgets.size() > 0) {
			ClientBudget budget = listOfBudgets.get(0);
			setData(budget);
			List<ClientBudgetItem> budgetItems = new ArrayList<ClientBudgetItem>();
			budgetItems = budget.getBudgetItem();

			for (ClientBudgetItem budgetItem : budgetItems) {

				budgetItem.setAccountsName(budgetItem.getAccount().getName());

				if (budgetItem.getTotalAmount() > 0) {
					grid.addData(budgetItem);
				}
			}
		}
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(messages.noRecordsToShow());

		getTotalLayout(grid);
	}

	@Override
	public void onSuccess(PaginationList<ClientBudget> result) {
		super.onSuccess(result);
		this.listOfBudgets = result;
		super.onSuccess(result);
		grid.sort(10, false);

	}

	// @Override
	// public boolean canEdit() {
	// return true;
	// }
	//
	// @Override
	// public boolean isDirty() {
	// return false;
	// }

}
