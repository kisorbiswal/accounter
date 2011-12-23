package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.RecurringsListGrid;

public class RecurringTransactionsListView extends
		TransactionsListView<ClientRecurringTransaction> {

	private final static String ALL = Accounter.messages().all();
	private final static String SCHEDULED = Accounter.messages().scheduled();
	private final static String REMAINDER = Accounter.messages().reminder();
	private final static String UNSCHEDULED = Accounter.messages()
			.unScheduled();

	List<String> listOfTypes;

	private String viewType;

	private List<ClientRecurringTransaction> recurringTransactions;

	public RecurringTransactionsListView() {
		super(Accounter.messages().all());
	}

	@Override
	public void updateInGrid(ClientRecurringTransaction objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(PaginationList<ClientRecurringTransaction> result) {
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
	protected SelectCombo getSelectItem() {
		viewSelect = new SelectCombo(Accounter.messages().currentView());
		viewSelect.setHelpInformation(true);
		listOfTypes = new ArrayList<String>();
		listOfTypes.add(ALL);
		listOfTypes.add(SCHEDULED);
		listOfTypes.add(REMAINDER);
		listOfTypes.add(UNSCHEDULED);
		viewSelect.initCombo(listOfTypes);

		if (viewType != null && !viewType.equals(""))
			viewSelect.setComboItem(viewType);
		else
			viewSelect.setComboItem(ALL);

		// if (UIUtils.isMSIEBrowser())
		// viewSelect.setWidth("105px");

		viewSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (viewSelect.getSelectedValue() != null) {
							grid.setViewType(viewSelect.getSelectedValue());
							filterList(viewSelect.getSelectedValue());
						}

					}
				});
		viewSelect.addStyleName("recurringListCombo");

		return viewSelect;
	}

	@Override
	protected void filterList(String text) {
		grid.removeAllRecords();
		for (ClientRecurringTransaction recTransaction : recurringTransactions) {
			if (text.equals(messages().all())) {
				grid.addData(recTransaction);
				continue;
			}

			if (text.equals(SCHEDULED)
					&& recTransaction.getType() == ClientRecurringTransaction.RECURRING_SCHEDULED) {
				grid.addData(recTransaction);
				continue;
			}

			if (text.equals(REMAINDER)
					&& recTransaction.getType() == ClientRecurringTransaction.RECURRING_REMINDER) {
				grid.addData(recTransaction);
				continue;
			}

			if (text.equals(UNSCHEDULED)
					&& recTransaction.getType() == ClientRecurringTransaction.RECURRING_UNSCHEDULED) {
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
		return Accounter.messages().recurringTransactionsList();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getRecurringsList(
				getStartDate().getDate(), getEndDate().getDate(), this);
	}

	@Override
	protected String getAddNewLabelString() {
		return "";
	}

	@Override
	protected String getViewTitle() {
		return Accounter.messages().recurringTransactions();
	}

	@Override
	protected Action getAddNewAction() {
		// TODO Auto-generated method stub
		return null;
	}

}
