package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.Accounter;
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
		Accounter.createHomeService().getBillsAndItemReceiptList(true, this);
	}

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return VendorsActionFactory.getRecordExpensesAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return Accounter.getVendorsMessages().addNewExpense();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {
		return Accounter.getVendorsMessages().expensesList();
	}

	@Override
	protected void initGrid() {
		grid = new BillsListGrid(false);
		grid.init();
		grid.setViewType(Accounter.getVendorsMessages().all());
	}

	@Override
	protected SelectCombo getSelectItem() {
		currentView = new SelectCombo(Accounter.getVendorsMessages()
				.currentView());
		currentView.setHelpInformation(true);
		listOfTypes = new ArrayList<String>();
		// listOfTypes.add(FinanceApplication.getVendorsMessages().open());
		// listOfTypes.add(FinanceApplication.getVendorsMessages().overDue());
		listOfTypes.add(Accounter.getVendorsMessages().cash());
		listOfTypes.add(Accounter.getVendorsMessages().creditCard());
		listOfTypes.add(Accounter.getVendorsMessages().employee());
		listOfTypes.add(Accounter.getVendorsMessages().Voided());
		listOfTypes.add(Accounter.getVendorsMessages().all());
		currentView.initCombo(listOfTypes);

		if (UIUtils.isMSIEBrowser())
			currentView.setWidth("150px");
		if (this.viewType == null || this.viewType.equals(""))
			currentView.setComboItem(Accounter.getCustomersMessages().all());
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
		if (text.equalsIgnoreCase(Accounter.getVendorsMessages().employee())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_EMPLOYEE_EXPENSE)
					records.add(record);
			}
			grid.setRecords(records);
		} else if (text.equalsIgnoreCase(Accounter.getVendorsMessages().cash())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_CASH_EXPENSE)
					records.add(record);
			}
			grid.setRecords(records);
		} else if (text.equalsIgnoreCase(Accounter.getVendorsMessages()
				.creditCard())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_CREDIT_CARD_EXPENSE)
					records.add(record);
			}
			grid.setRecords(records);
		} else if (text.equalsIgnoreCase(Accounter.getVendorsMessages()
				.employee())) {
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

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public void printPreview() {
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public void updateInGrid(BillsList objectTobeModified) {
	}

	@Override
	protected String getViewTitle() {
		return Accounter.getVendorsMessages().expensesList();
	}

}
