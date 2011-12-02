package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.QuoteListGrid;

public class QuoteListView extends BaseListView<ClientEstimate> {

	protected List<ClientEstimate> estimates;

	private SelectCombo viewSelect;

	private List<ClientEstimate> listOfEstimates;

	private int type;

	private static String OPEN = messages.open();
	private static String REJECTED = messages.rejected();
	private static String ACCEPTED = messages.accepted();
	private static String EXPIRED = messages.expired();
	private static String ALL = messages.all();
	private static String CLOSE = messages.close();
	private static String APPLIED = messages.applied();

	public static final int STATUS_OPEN = 0;
	public static final int STATUS_REJECTED = 1;
	public static final int STATUS_ACCECPTED = 2;

	public QuoteListView(int type) {

		super();
		this.type = type;
		// isDeleteDisable = true;
	}

	@Override
	protected Action getAddNewAction() {
		if (type == ClientEstimate.QUOTES
				&& Accounter.getUser().canDoInvoiceTransactions())
			return ActionFactory.getNewQuoteAction(type, messages.newQuote());
		else if (getPreferences().isDelayedchargesEnabled()) {
			if (type == ClientEstimate.CHARGES) {
				return ActionFactory.getNewQuoteAction(type,
						messages.newCharge());
			} else if (type == ClientEstimate.CREDITS) {
				return ActionFactory.getNewQuoteAction(type,
						messages.newCredit());
			}
		}
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (type == ClientEstimate.QUOTES
				&& Accounter.getUser().canDoInvoiceTransactions())
			return messages.addaNewQuote();
		else if (getPreferences().isDelayedchargesEnabled()) {
			if (type == ClientEstimate.CHARGES) {
				return messages.addNewCharge();
			} else if (type == ClientEstimate.CREDITS) {
				return messages.addNew(messages.credit());
			}
		}
		return "";
	}

	@Override
	protected String getListViewHeading() {
		if (type == ClientEstimate.CHARGES) {
			return messages.chargesList();
		} else if (type == ClientEstimate.CREDITS) {
			return messages.creditsList();
		}
		return messages.quotesList();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getEstimates(type, this);

	}

	@Override
	public void onSuccess(ArrayList<ClientEstimate> result) {
		super.onSuccess(result);
		listOfEstimates = result;
		filterList(viewSelect.getSelectedValue());
		grid.setViewType(viewSelect.getSelectedValue());
	}

	@Override
	public void updateInGrid(ClientEstimate objectTobeModified) {

	}

	@Override
	protected void initGrid() {
		grid = new QuoteListGrid(type);
		grid.init();

	}

	protected SelectCombo getSelectItem() {
		viewSelect = new SelectCombo(messages.currentView());
		viewSelect.setHelpInformation(true);
		listOfTypes = new ArrayList<String>();
		listOfTypes.add(OPEN);
		listOfTypes.add(REJECTED);
		listOfTypes.add(ACCEPTED);
		listOfTypes.add(EXPIRED);
		listOfTypes.add(APPLIED);
		listOfTypes.add(CLOSE);
		listOfTypes.add(ALL);
		viewSelect.initCombo(listOfTypes);

		// if (UIUtils.isMSIEBrowser())
		// viewSelect.setWidth("150px");

		viewSelect.setComboItem(OPEN);
		viewSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (viewSelect.getSelectedValue() != null) {
							grid.setViewType(viewSelect.getSelectedValue()
									.toString());
							filterList(viewSelect.getSelectedValue().toString());
						}

					}
				});

		if (type == ClientEstimate.QUOTES) {
			return viewSelect;
		}
		return null;
	}

	private void filterList(String text) {

		grid.removeAllRecords();

		for (ClientEstimate estimate : listOfEstimates) {
			if (text.equals(OPEN)) {
				if (estimate.getStatus() == ClientEstimate.STATUS_OPEN)
					grid.addData(estimate);
				continue;
			}
			if (text.equals(REJECTED)) {
				if (estimate.getStatus() == ClientEstimate.STATUS_REJECTED)
					grid.addData(estimate);
				continue;
			}
			if (text.equals(ACCEPTED)) {
				if (estimate.getStatus() == ClientEstimate.STATUS_ACCECPTED)
					grid.addData(estimate);
				continue;
			}
			if (text.equals(EXPIRED)) {
				ClientFinanceDate expiryDate = new ClientFinanceDate(
						estimate.getExpirationDate());
				if (expiryDate.before(new ClientFinanceDate()))
					grid.addData(estimate);
				continue;
			}
			if (text.equals(APPLIED)) {
				if (estimate.getStatus() == ClientEstimate.STATUS_APPLIED)
					grid.addData(estimate);
				continue;
			}
			if (text.equals(CLOSE)) {
				if (estimate.getStatus() == ClientEstimate.STATUS_CLOSE)
					grid.addData(estimate);
				continue;
			}
			if (text.equals(ALL)) {
				grid.addData(estimate);
			}
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

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return messages.quotes();
	}
}
