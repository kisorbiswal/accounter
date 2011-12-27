package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.BillsListGrid;

public class ExpensesListView extends TransactionsListView<BillsList> {

	public ExpensesListView() {
		super(Accounter.messages().all());
	}

	public ExpensesListView(String viewType) {
		super(viewType);
	}

	public static ExpensesListView getInstance() {
		return new ExpensesListView();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService()
				.getBillsAndItemReceiptList(true, 0, getStartDate().getDate(),
						getEndDate().getDate(), 0, 0, 0, this);
	}

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			if (viewType == messages().cashExpenses()) {
				return ActionFactory.CashExpenseAction();
			} else if (viewType.equals(messages().creditCardExpenses())) {
				return ActionFactory.CreditCardExpenseAction();
			}
			return ActionFactory.getRecordExpensesAction();
		} else {
			return null;
		}
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			if (viewType == null || viewType.equals(messages().all())
					|| viewType.equals(messages().voided())) {
				return messages().addNewExpense();
			}
			if (viewType.equals(messages().cash())) {
				return messages().newcashExpenses();
			} else if (viewType.equals(messages().creditCard())) {
				return messages().newCreditCardExpenses();
			}
		}
		return "";
	}

	@Override
	protected String getListViewHeading() {
		if (viewType == null || viewType.equals(messages().all())) {
			return messages().expensesList();
		}
		if (viewType.equals(messages().cashExpenses())) {
			return messages().cashExpensesList();
		} else if (viewType.equals(messages().creditCardExpenses())) {
			return messages().creditCardExpensesList();
		}
		return messages().expensesList();
	}

	@Override
	protected void initGrid() {
		grid = new BillsListGrid(false);
		grid.init();
		grid.setViewType(messages().all());
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		// listOfTypes.add(FinanceApplication.constants().open());
		// listOfTypes.add(FinanceApplication.constants().overDue());
		listOfTypes.add(messages().cash());
		listOfTypes.add(messages().creditCard());

		// This should be added when user select to track employee expenses.
		if (Global.get().preferences().isHaveEpmloyees()
				&& Global.get().preferences().isTrackEmployeeExpenses()) {
			listOfTypes.add(messages().employee());
		}
		listOfTypes.add(messages().voided());
		listOfTypes.add(messages().all());
		return listOfTypes;
	}

	@Override
	protected void filterList(String text) {
		grid.removeAllRecords();
		if (text.equalsIgnoreCase(messages().employee())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_EMPLOYEE_EXPENSE)
					records.add(record);
			}
			grid.setRecords(records);
		} else if (text.equalsIgnoreCase(messages().cash())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_CASH_EXPENSE)
					records.add(record);
			}
			grid.setRecords(records);
		} else if (text.equalsIgnoreCase(messages().creditCard())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_CREDIT_CARD_EXPENSE)
					records.add(record);
			}
			grid.setRecords(records);
		} else if (text.equalsIgnoreCase(messages().employee())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_EMPLOYEE_EXPENSE)
					records.add(record);
			}
			grid.setRecords(records);
		} else if (text.equalsIgnoreCase(messages().Voided())) {
			List<BillsList> voidedRecs = new ArrayList<BillsList>();
			List<BillsList> allRecs = initialRecords;
			for (BillsList rec : allRecs) {
				if (rec.isVoided() && !rec.isDeleted()) {
					voidedRecs.add(rec);
				}
			}
			grid.setRecords(voidedRecs);

		} else if (text.equalsIgnoreCase(messages().all())) {
			grid.setRecords(initialRecords);
		}

		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(messages().noRecordsToShow());

	}

	@Override
	public void onSuccess(PaginationList<BillsList> result) {

		super.onSuccess(result);
		filterList(viewSelect.getSelectedValue().toString());
		grid.setViewType(viewSelect.getSelectedValue().toString());
		grid.sort(10, false);

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
		// NOTHING TO DO
	}

	@Override
	public void updateInGrid(BillsList objectTobeModified) {
		// NOTHING TO DO
	}

	@Override
	protected String getViewTitle() {
		return messages().expensesList();
	}

}
