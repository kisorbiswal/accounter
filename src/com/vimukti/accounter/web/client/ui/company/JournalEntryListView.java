package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;
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
	CompanyMessages companyConstants = GWT.create(CompanyMessages.class);
	private SelectItem currentView;

	public JournalEntryListView() {
		isDeleteDisable = true;
	}

	@Override
	protected Action getAddNewAction() {

		return CompanyActionFactory.getNewJournalEntryAction();
	}

	@Override
	protected String getAddNewLabelString() {

		return companyConstants.addNewJournalEntry();
	}

	@Override
	protected String getListViewHeading() {

		return companyConstants.journalEntryList();
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
	public void onSuccess(List<ClientJournalEntry> result) {
		super.onSuccess(result);
		grid.setViewType(FinanceApplication.getVendorsMessages().all());
	}

	@Override
	public void updateInGrid(ClientJournalEntry objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected SelectItem getSelectItem() {
		currentView = new SelectItem();
		currentView.setValueMap(FinanceApplication.getVendorsMessages()
				.nonVoided(), FinanceApplication.getVendorsMessages().Voided(),
				FinanceApplication.getVendorsMessages().cashBasis(),
				FinanceApplication.getVendorsMessages().voidedCashBasis(),
				FinanceApplication.getVendorsMessages().all()
		// , "Deleted"
				);
		currentView
				.setSelected(FinanceApplication.getCustomersMessages().all());
		currentView.addChangeHandler(new ChangeHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onChange(ChangeEvent event) {
				if (currentView.getValue() != null) {
					grid.setViewType(currentView.getValue().toString());
					filterList(currentView.getValue().toString());
				}
			}
		});
		return currentView;
	}

	private void filterList(String text) {
		if (currentView.getValue().toString().equalsIgnoreCase("Non Voided")) {
			List<ClientJournalEntry> nonVoidedRecs = new ArrayList<ClientJournalEntry>();
			List<ClientJournalEntry> allRecs = initialRecords;
			for (ClientJournalEntry rec : allRecs) {
				if (!rec.isVoid()) {
					nonVoidedRecs.add(rec);
				}
			}
			grid.setRecords(nonVoidedRecs);

		} else if (currentView.getValue().toString().equalsIgnoreCase("Voided")) {
			List<ClientJournalEntry> voidedRecs = new ArrayList<ClientJournalEntry>();
			List<ClientJournalEntry> allRecs = initialRecords;
			for (ClientJournalEntry rec : allRecs) {
				if (rec.isVoid()
						&& rec.getStatus() != ClientTransaction.STATUS_DELETED) {
					voidedRecs.add(rec);
				}
			}
			grid.setRecords(voidedRecs);

		} else if (currentView.getValue().toString().equalsIgnoreCase(
				"Cash Basis")) {
			List<ClientJournalEntry> cashBasisRecs = new ArrayList<ClientJournalEntry>();
			List<ClientJournalEntry> allRecs = initialRecords;
			for (ClientJournalEntry rec : allRecs) {
				if (rec.getType() == ClientJournalEntry.TYPE_CASH_BASIS_JOURNAL_ENTRY) {
					cashBasisRecs.add(rec);
				}
			}
			grid.setRecords(cashBasisRecs);

		} else if (currentView.getValue().toString().equalsIgnoreCase(
				"Voided Cash Basis")) {
			List<ClientJournalEntry> voidedCashBasisRecs = new ArrayList<ClientJournalEntry>();
			List<ClientJournalEntry> allRecs = initialRecords;
			for (ClientJournalEntry rec : allRecs) {
				if (rec.getType() == ClientJournalEntry.TYPE_CASH_BASIS_JOURNAL_ENTRY
						&& rec.isVoid()
						&& rec.getStatus() != ClientTransaction.STATUS_DELETED) {
					voidedCashBasisRecs.add(rec);
				}
			}
			grid.setRecords(voidedCashBasisRecs);

		}
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
		if (currentView.getValue().toString().equalsIgnoreCase("All")) {
			grid.setRecords(initialRecords);
		}
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);

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
		// TODO Auto-generated method stub

	}

}
