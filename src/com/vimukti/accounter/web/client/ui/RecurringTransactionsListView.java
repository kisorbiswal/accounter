package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.RecurringsListGrid;

public class RecurringTransactionsListView extends
		TransactionsListView<ClientRecurringTransaction> {

	private List<ClientRecurringTransaction> recurringTransactions;

	public RecurringTransactionsListView() {
		super(Accounter.messages().all());
	}

	@Override
	public void updateInGrid(ClientRecurringTransaction objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(ArrayList<ClientRecurringTransaction> result) {
		super.onSuccess(result);
		recurringTransactions = result;
		filterList(viewSelect.getValue().toString());
		grid.setViewType(viewSelect.getValue().toString());
	}

	@Override
	protected void initGrid() {
		grid = new RecurringsListGrid(false);
		grid.init();
		// filterList(false);
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages().all());
		listOfTypes.add(messages().schedule());
		listOfTypes.add(messages().remainder());
		listOfTypes.add(messages().noneJustTemplate());
		return listOfTypes;
	}

	@Override
	protected void filterList(String text) {
		grid.removeAllRecords();
		for (ClientRecurringTransaction recTransaction : recurringTransactions) {
			if (text.equals(messages().all())) {
				grid.addData(recTransaction);
				continue;
			}

			if (text.equals(messages().schedule())
					&& recTransaction.getType() == ClientRecurringTransaction.RECURRING_SCHEDULE) {
				grid.addData(recTransaction);
				continue;
			}

			if (text.equals(messages().remainder())
					&& recTransaction.getType() == ClientRecurringTransaction.RECURRING_REMAINDER) {
				grid.addData(recTransaction);
				continue;
			}

			if (text.equals(messages().noneJustTemplate())
					&& recTransaction.getType() == ClientRecurringTransaction.RECURRING_NONE) {
				grid.addData(recTransaction);
				continue;
			}
		}

		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages().noRecordsToShow());
		}
	}

	@Override
	protected String getListViewHeading() {
		return "Recurring Transactions List";
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getRecurringsList(
				getStartDate().getDate(), getEndDate().getDate(), this);
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
