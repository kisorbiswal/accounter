package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.BillsListGrid;

public class ExpensesListView extends BaseListView<BillsList> {

	private SelectCombo currentView;
	public String viewType;

	public ExpensesListView() {
		super();
	}

	public ExpensesListView(String viewType) {
		super();
		this.viewType = viewType;
	}

	public static ExpensesListView getInstance() {
		return new ExpensesListView();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getBillsAndItemReceiptList(true, this);
	}

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return ActionFactory.getRecordExpensesAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return Accounter.constants().addNewExpense();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {
		return Accounter.constants().expensesList();
	}

	@Override
	protected void initGrid() {
		grid = new BillsListGrid(false);
		grid.init();
		grid.setViewType(Accounter.constants().all());
	}

	@Override
	protected SelectCombo getSelectItem() {
		currentView = new SelectCombo(Accounter.constants().currentView());
		currentView.setHelpInformation(true);
		listOfTypes = new ArrayList<String>();
		// listOfTypes.add(FinanceApplication.constants().open());
		// listOfTypes.add(FinanceApplication.constants().overDue());
		listOfTypes.add(Accounter.constants().cash());
		listOfTypes.add(Accounter.constants().creditCard());

		// This should be added when user select to track employee expenses.
		if (ClientCompanyPreferences.get().isHaveEpmloyees()
				&& ClientCompanyPreferences.get().isTrackEmployeeExpenses()) {
			listOfTypes.add(Accounter.constants().employee());
		}
		listOfTypes.add(Accounter.constants().voided());
		listOfTypes.add(Accounter.constants().all());
		currentView.initCombo(listOfTypes);

		// if (UIUtils.isMSIEBrowser())
		// currentView.setWidth("150px");
		if (this.viewType == null || this.viewType.equals(""))
			currentView.setComboItem(Accounter.constants().all());
		else
			currentView.setComboItem(this.viewType);
		currentView
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (currentView.getSelectedValue() != null) {
							grid.setViewType(currentView.getSelectedValue());
							filterList(currentView.getSelectedValue());
						}
					}
				});
		return currentView;
	}

	protected void filterList(String text) {
		grid.removeAllRecords();
		if (text.equalsIgnoreCase(Accounter.constants().employee())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_EMPLOYEE_EXPENSE)
					records.add(record);
			}
			grid.setRecords(records);
		} else if (text.equalsIgnoreCase(Accounter.constants().cash())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_CASH_EXPENSE)
					records.add(record);
			}
			grid.setRecords(records);
		} else if (text.equalsIgnoreCase(Accounter.constants().creditCard())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_CREDIT_CARD_EXPENSE)
					records.add(record);
			}
			grid.setRecords(records);
		} else if (text.equalsIgnoreCase(Accounter.constants().employee())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_EMPLOYEE_EXPENSE)
					records.add(record);
			}
			grid.setRecords(records);
		} else if (text.equalsIgnoreCase(Accounter.getFinanceConstants()
				.Voided())) {
			List<BillsList> voidedRecs = new ArrayList<BillsList>();
			List<BillsList> allRecs = initialRecords;
			for (BillsList rec : allRecs) {
				if (rec.isVoided() && !rec.isDeleted()) {
					voidedRecs.add(rec);
				}
			}
			grid.setRecords(voidedRecs);

		} else if (text.equalsIgnoreCase(Accounter.getFinanceConstants().all())) {
			grid.setRecords(initialRecords);
		}

		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);

	}

	@Override
	public void onSuccess(ArrayList<BillsList> result) {
		super.onSuccess(result);
		filterList(currentView.getSelectedValue().toString());
		grid.setViewType(currentView.getSelectedValue().toString());
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
		return Accounter.constants().expensesList();
	}

}
