package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.DraftsListGrid;

public class DraftsListView extends BaseListView<ClientTransaction> {

	@Override
	public void updateInGrid(ClientTransaction objectTobeModified) {
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		super.deleteFailed(caught);
		AccounterException accounterException = caught;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

	}

	@Override
	protected void initGrid() {
		grid = new DraftsListGrid(false);
		grid.init();
	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	protected void onPageChange(int start, int length) {

	}

	@Override
	protected String getListViewHeading() {
		return messages.draft() + "s";
	}

	@Override
	protected Action getAddNewAction() {
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		return null;
	}

	@Override
	public void onSuccess(PaginationList<ClientTransaction> result) {
		grid.removeAllRecords();
		if (result.isEmpty()) {
			updateRecordsCount(result.getStart(), grid.getTableRowCount(),
					result.getTotalCount());
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		grid.sort(10, false);
		grid.setRecords(result);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());
	}

	@Override
	protected String getViewTitle() {
		return messages.draftsList();
	}

}
