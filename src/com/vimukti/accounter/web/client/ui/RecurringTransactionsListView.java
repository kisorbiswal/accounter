package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.RecurringsListGrid;

public class RecurringTransactionsListView extends
		BaseListView<ClientRecurringTransaction> {

	private final static String ALL = Accounter.constants().all();
	private final static String SCHEDULES = Accounter.constants().schedule();
	private final static String REMAINDERS = Accounter.constants().remainder();
	private final static String TEMPLATES = Accounter.constants().noneJustTemplate();

	private String viewType;

	private List<ClientRecurringTransaction> recurringTransactions;

	public RecurringTransactionsListView() {
		super();
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
	protected SelectCombo getSelectItem() {
		viewSelect = new SelectCombo(Accounter.constants().currentView());
		viewSelect.setHelpInformation(true);
		listOfTypes = new ArrayList<String>();
		listOfTypes.add(ALL);
		listOfTypes.add(SCHEDULES);
		listOfTypes.add(REMAINDERS);
		listOfTypes.add(TEMPLATES);
		viewSelect.initCombo(listOfTypes);

		if (viewType != null && !viewType.equals(""))
			viewSelect.setComboItem(viewType);
		else
			viewSelect.setComboItem(ALL);

//		if (UIUtils.isMSIEBrowser())
//			viewSelect.setWidth("105px");

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

	private void filterList(String text) {

		grid.removeAllRecords();

		for (ClientRecurringTransaction recTransaction : recurringTransactions) {

			if (text.equals(ALL)) {
				grid.addData(recTransaction);
				continue;
			}

			if (text.equals(SCHEDULES)
					&& recTransaction.getType() == ClientRecurringTransaction.RECURRING_SCHEDULE) {
				grid.addData(recTransaction);
				continue;
			}

			if (text.equals(REMAINDERS)
					&& recTransaction.getType() == ClientRecurringTransaction.RECURRING_REMAINDER) {
				grid.addData(recTransaction);
				continue;
			}

			if (text.equals(TEMPLATES)
					&& recTransaction.getType() == ClientRecurringTransaction.RECURRING_NONE) {
				grid.addData(recTransaction);
				continue;
			}
		}

		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
		}
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
