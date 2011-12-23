package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.QuoteListGrid;

public class QuoteListView extends TransactionsListView<ClientEstimate> {

	protected List<ClientEstimate> estimates;

	private List<ClientEstimate> listOfEstimates;

	private int type;

	public static final int STATUS_OPEN = 0;
	public static final int STATUS_REJECTED = 1;
	public static final int STATUS_ACCECPTED = 2;

	public QuoteListView(int type) {
		super(Accounter.messages().open());
		this.type = type;
		// isDeleteDisable = true;
	}

	@Override
	protected Action getAddNewAction() {
		if (type == ClientEstimate.QUOTES
				&& Accounter.getUser().canDoInvoiceTransactions())
			return ActionFactory.getNewQuoteAction(type);
		else if (getPreferences().isDelayedchargesEnabled()) {
			if (type == ClientEstimate.CHARGES) {
				return ActionFactory.getNewQuoteAction(type);
			} else if (type == ClientEstimate.CREDITS) {
				return ActionFactory.getNewQuoteAction(type);
			}
		}
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (type == ClientEstimate.QUOTES
				&& Accounter.getUser().canDoInvoiceTransactions())
			return messages().addaNewQuote();
		else if (getPreferences().isDelayedchargesEnabled()) {
			if (type == ClientEstimate.CHARGES) {
				return messages().addNewCharge();
			} else if (type == ClientEstimate.CREDITS) {
				return messages().addNew(messages().credit());
			}
		}
		return "";
	}

	@Override
	protected String getListViewHeading() {
		if (type == ClientEstimate.CHARGES) {
			return messages().chargesList();
		} else if (type == ClientEstimate.CREDITS) {
			return messages().creditsList();
		}
		return messages().quotesList();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getEstimates(type,
				getStartDate().getDate(), getEndDate().getDate(), this);

	}

	@Override
	public void onSuccess(PaginationList<ClientEstimate> result) {
		super.onSuccess(result);
		listOfEstimates = result;
		filterList(viewSelect.getSelectedValue());
		grid.setViewType(viewSelect.getSelectedValue());
		grid.sort(10, false);
	}

	@Override
	public void updateInGrid(ClientEstimate objectTobeModified) {

	}

	@Override
	protected void initGrid() {
		grid = new QuoteListGrid(type);
		grid.init();

	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages().open());
		listOfTypes.add(messages().rejected());
		listOfTypes.add(messages().accepted());
		listOfTypes.add(messages().expired());
		listOfTypes.add(messages().all());
		listOfTypes.add(messages().close());
		listOfTypes.add(messages().applied());
		return listOfTypes;
	}

	@Override
	protected void filterList(String text) {

		grid.removeAllRecords();

		for (ClientEstimate estimate : listOfEstimates) {
			if (text.equals(messages().open())) {
				if (estimate.getStatus() == ClientEstimate.STATUS_OPEN)
					grid.addData(estimate);
				continue;
			}
			if (text.equals(messages().rejected())) {
				if (estimate.getStatus() == ClientEstimate.STATUS_REJECTED)
					grid.addData(estimate);
				continue;
			}
			if (text.equals(messages().accepted())) {
				if (estimate.getStatus() == ClientEstimate.STATUS_ACCECPTED)
					grid.addData(estimate);
				continue;
			}
			if (text.equals(messages().expired())) {
				ClientFinanceDate expiryDate = new ClientFinanceDate(
						estimate.getExpirationDate());
				if (expiryDate.before(new ClientFinanceDate()))
					grid.addData(estimate);
				continue;
			}
			if (text.equals(messages().applied())) {
				if (estimate.getStatus() == ClientEstimate.STATUS_APPLIED)
					grid.addData(estimate);
				continue;
			}
			if (text.equals(messages().close())) {
				if (estimate.getStatus() == ClientEstimate.STATUS_CLOSE)
					grid.addData(estimate);
				continue;
			}
			if (text.equals(messages().all())) {
				grid.addData(estimate);
			}
		}
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(messages().noRecordsToShow());

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		if (type == ClientEstimate.CREDITS) {
			return messages().credits();
		} else if (type == ClientEstimate.CHARGES) {
			return messages().Charges();
		}
		return messages().quotes();
	}
}
