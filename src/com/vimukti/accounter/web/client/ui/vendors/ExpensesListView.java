package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
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
		FinanceApplication.createHomeService().getBillsAndItemReceiptList(true,
				this);
	}

	@Override
	protected Action getAddNewAction() {
		if (FinanceApplication.getUser().canDoInvoiceTransactions())
			return VendorsActionFactory.getRecordExpensesAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (FinanceApplication.getUser().canDoInvoiceTransactions())
			return FinanceApplication.getVendorsMessages().addNewExpense();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {
		return FinanceApplication.getVendorsMessages().expensesList();
	}

	@Override
	protected void initGrid() {
		grid = new BillsListGrid(false);
		grid.init();
		grid.setViewType(FinanceApplication.getVendorsMessages().all());
	}

	@Override
	protected SelectCombo getSelectItem() {
		currentView = new SelectCombo(FinanceApplication.getVendorsMessages()
				.currentView());
		currentView.setHelpInformation(true);
		listOfTypes = new ArrayList<String>();
		// listOfTypes.add(FinanceApplication.getVendorsMessages().open());
		// listOfTypes.add(FinanceApplication.getVendorsMessages().overDue());
		listOfTypes.add(FinanceApplication.getVendorsMessages().cash());
		listOfTypes.add(FinanceApplication.getVendorsMessages().creditCard());
		listOfTypes.add(FinanceApplication.getVendorsMessages().employee());
		listOfTypes.add(FinanceApplication.getVendorsMessages().Voided());
		listOfTypes.add(FinanceApplication.getVendorsMessages().all());
		currentView.initCombo(listOfTypes);

		if (UIUtils.isMSIEBrowser())
			currentView.setWidth("150px");
		if (this.viewType == null || this.viewType.equals(""))
			currentView.setComboItem(FinanceApplication.getCustomersMessages()
					.all());
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
		if (text.equalsIgnoreCase(FinanceApplication.getVendorsMessages()
				.employee())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_EMPLOYEE_EXPENSE)
					records.add(record);
			}
			grid.setRecords(records);
		} else if (text.equalsIgnoreCase(FinanceApplication
				.getVendorsMessages().cash())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_CASH_EXPENSE)
					records.add(record);
			}
			grid.setRecords(records);
		} else if (text.equalsIgnoreCase(FinanceApplication
				.getVendorsMessages().creditCard())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_CREDIT_CARD_EXPENSE)
					records.add(record);
			}
			grid.setRecords(records);
		} else if (text.equalsIgnoreCase(FinanceApplication
				.getVendorsMessages().employee())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_EMPLOYEE_EXPENSE)
					records.add(record);
			}
			grid.setRecords(records);
		} else if (text.equalsIgnoreCase("Voided")) {
			List<BillsList> voidedRecs = new ArrayList<BillsList>();
			List<BillsList> allRecs = initialRecords;
			for (BillsList rec : allRecs) {
				if (rec.isVoided() && !rec.isDeleted()) {
					voidedRecs.add(rec);
				}
			}
			grid.setRecords(voidedRecs);

		} else if (text.equalsIgnoreCase("All")) {
			grid.setRecords(initialRecords);
		}

		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);

	}

	@Override
	public void onSuccess(List<BillsList> result) {
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
		// TODO Auto-generated method stub

	}

	@Override
	public void updateInGrid(BillsList objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return FinanceApplication.getVendorsMessages().expensesList();
	}

}
