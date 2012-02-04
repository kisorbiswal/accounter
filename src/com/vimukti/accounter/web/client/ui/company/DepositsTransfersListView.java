package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.DepositsTransfersList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.DepositsTransfersListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class DepositsTransfersListView extends
		TransactionsListView<DepositsTransfersList> {

	private int transactionType;

	public DepositsTransfersListView() {
		super(messages.all());
	}

	public DepositsTransfersListView(int transactionType) {
		super(messages.all());
		this.transactionType = transactionType;
	}

	@Override
	public void updateInGrid(DepositsTransfersList objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initGrid() {
		grid = new DepositsTransfersListGrid(false, transactionType);
		grid.init();
		grid.setEditEventType(ListGrid.EDIT_EVENT_DBCLICK);
		// FinanceApplication.createHomeService().getPaymentsList(this);
	}

	public void onSuccess(PaginationList<DepositsTransfersList> result) {
		grid.removeAllRecords();
		if (result.isEmpty()) {
			updateRecordsCount(result.getStart(), grid.getTableRowCount(),
					result.getTotalCount());
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		grid.setRecords(result);
		grid.sort(12, false);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.voided());
		listOfTypes.add(messages.all());
		listOfTypes.add(messages.drafts());
		return listOfTypes;
	}

	@Override
	protected void filterList(boolean isActive) {
		onPageChange(0, getPageSize());
	}

	@Override
	protected void filterList(String selectedValue) {
		onPageChange(0, getPageSize());
	}

	@Override
	protected String getListViewHeading() {
		if (transactionType == 0) {
			return messages.deposits();
		} else {
			return messages.transferFunds();
		}
	}

	@Override
	protected Action getAddNewAction() {
		if (transactionType == 0) {
			return ActionFactory.getDepositAction();
		} else {
			return ActionFactory.getMakeDepositAction();
		}
	}

	@Override
	protected String getAddNewLabelString() {
		if (transactionType == 0) {
			return messages.makeDeposit();
		} else {
			return messages.transferFund();
		}
	}

	@Override
	protected String getViewTitle() {
		if (transactionType == 0) {
			return messages.deposits();
		} else {
			return messages.transferFunds();
		}
	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	protected void onPageChange(int start, int length) {
		int type = 0;
		if (getViewType().equalsIgnoreCase(messages.voided())) {
			type = VIEW_VOIDED;
		} else if (getViewType().equalsIgnoreCase(messages.all())) {
			type = TYPE_ALL;
		} else if (getViewType().equalsIgnoreCase(messages.drafts())) {
			type = VIEW_DRAFT;
		}
		if (transactionType == 0) {
			Accounter.createHomeService().getDepositsList(
					getStartDate().getDate(), getEndDate().getDate(), start,
					length, type, this);
		} else {
			Accounter.createHomeService().getTransfersList(
					getStartDate().getDate(), getEndDate().getDate(), start,
					length, type, this);
		}
	}

}
