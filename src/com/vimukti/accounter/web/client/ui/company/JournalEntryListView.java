package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.grids.JournalEntriesListGrid;

/**
 * 
 * @author Mandeep Singh
 * 
 */
public class JournalEntryListView extends BaseListView<ClientJournalEntry> {
	SelectItem viewSelect;
	DynamicForm form;
	List<ClientJournalEntry> allEntries;
	private SelectCombo currentView;

	public JournalEntryListView() {
		isDeleteDisable = true;
	}

	@Override
	protected Action getAddNewAction() {

		return ActionFactory.getNewJournalEntryAction();
	}

	@Override
	protected String getAddNewLabelString() {

		return Accounter.messages().addNewJournalEntry();
	}

	@Override
	protected String getListViewHeading() {

		return Accounter.messages().journalEntryList();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		rpcUtilService.getJournalEntries(this);
	}

	@Override
	protected void initGrid() {
		grid = new JournalEntriesListGrid(false);
		grid.init();
	}

	@Override
	public void onSuccess(ArrayList<ClientJournalEntry> result) {
		super.onSuccess(result);
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(messages.noRecordsToShow());
		// grid.setViewType(FinanceApplication.constants().all());
		// filterList(FinanceApplication.constants().all());
	}

	@Override
	public void updateInGrid(ClientJournalEntry objectTobeModified) {
		// NOTHING TO DO.
	}

	@Override
	protected SelectCombo getSelectItem() {
		currentView = new SelectCombo(null);
		currentView.setHelpInformation(true);
		listOfTypes = new ArrayList<String>();
		// listOfTypes.add(FinanceApplication.constants().nonVoided());
		// listOfTypes.add(FinanceApplication.constants().Voided());
		// listOfTypes.add(FinanceApplication.constants().cashBasis());
		// listOfTypes.add(FinanceApplication.constants()
		// .voidedCashBasis());
		listOfTypes.add(Accounter.messages().all());
		currentView.initCombo(listOfTypes);
		// currentView.setComboItem(FinanceApplication.constants().all());
		currentView.setComboItem(Accounter.messages().all());
		currentView
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						if (currentView.getSelectedValue() != null) {
							currentView.setSelected(currentView
									.getSelectedValue());
							grid.setViewType(currentView.getSelectedValue());
							filterList(currentView.getSelectedValue());
						}
					}
				});
		return currentView;
	}

	private void filterList(String text) {
		/*
		 * if (currentView.getSelectedValue().equalsIgnoreCase("Non Voided")) {
		 * List<ClientJournalEntry> nonVoidedRecs = new
		 * ArrayList<ClientJournalEntry>(); List<ClientJournalEntry> allRecs =
		 * initialRecords; for (ClientJournalEntry rec : allRecs) { if
		 * (!rec.isVoid()) { nonVoidedRecs.add(rec); } }
		 * grid.setRecords(nonVoidedRecs);
		 * 
		 * } else if (currentView.getSelectedValue().equalsIgnoreCase("Voided"))
		 * { List<ClientJournalEntry> voidedRecs = new
		 * ArrayList<ClientJournalEntry>(); List<ClientJournalEntry> allRecs =
		 * initialRecords; for (ClientJournalEntry rec : allRecs) { if
		 * (rec.isVoid() && rec.getStatus() != ClientTransaction.STATUS_DELETED)
		 * { voidedRecs.add(rec); } } grid.setRecords(voidedRecs);
		 * 
		 * } else if (currentView.getSelectedValue()
		 * .equalsIgnoreCase("Cash Basis")) { List<ClientJournalEntry>
		 * cashBasisRecs = new ArrayList<ClientJournalEntry>();
		 * List<ClientJournalEntry> allRecs = initialRecords; for
		 * (ClientJournalEntry rec : allRecs) { if (rec.getType() ==
		 * ClientJournalEntry.TYPE_CASH_BASIS_JOURNAL_ENTRY) {
		 * cashBasisRecs.add(rec); } } grid.setRecords(cashBasisRecs);
		 * 
		 * } else if (currentView.getSelectedValue().equalsIgnoreCase(
		 * "Voided Cash Basis")) { List<ClientJournalEntry> voidedCashBasisRecs
		 * = new ArrayList<ClientJournalEntry>(); List<ClientJournalEntry>
		 * allRecs = initialRecords; for (ClientJournalEntry rec : allRecs) { if
		 * (rec.getType() == ClientJournalEntry.TYPE_CASH_BASIS_JOURNAL_ENTRY &&
		 * rec.isVoid() && rec.getStatus() != ClientTransaction.STATUS_DELETED)
		 * { voidedCashBasisRecs.add(rec); } }
		 * grid.setRecords(voidedCashBasisRecs);
		 * 
		 * }
		 */
		// else if (currentView.getValue().toString().equalsIgnoreCase(
		// "Deleted")) {
		// List<ClientJournalEntry> deletedRecs = new
		// ArrayList<ClientJournalEntry>();
		// List<ClientJournalEntry> allRecs = initialRecords;
		// for (ClientJournalEntry rec : allRecs) {
		// if (rec.getStatus()==ClientTransaction.STATUS_DELETED) {
		// deletedRecs.add(rec);
		// }
		// }
		// grid.setRecords(deletedRecs);
		//
		// }
		if (currentView.getSelectedValue().equalsIgnoreCase("All")) {
			grid.setRecords(initialRecords);
		}
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(messages.noRecordsToShow());

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

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
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return Accounter.messages().journalEntries();
	}

}
