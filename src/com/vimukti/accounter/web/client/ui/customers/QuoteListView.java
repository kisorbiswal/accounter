package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.QuoteListGrid;

public class QuoteListView extends BaseListView<ClientEstimate> {

	private AccounterConstants customerConstants = Accounter.constants();

	protected List<ClientEstimate> estimates;

	private SelectCombo viewSelect;

	private List<ClientEstimate> listOfEstimates;

	private static String OPEN = Accounter.constants().open();
	private static String REJECTED = Accounter.constants().rejected();
	private static String ACCEPTED = Accounter.constants().accepted();
	private static String EXPIRED = Accounter.constants().expired();
	private static String ALL = Accounter.constants().all();
	// private static String DELETED = "Deleted";

	public static final int STATUS_OPEN = 0;
	public static final int STATUS_REJECTED = 1;
	public static final int STATUS_ACCECPTED = 2;

	public QuoteListView() {

		super();
		// isDeleteDisable = true;
	}

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return ActionFactory.getNewQuoteAction(ClientEstimate.QUOTES);
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return customerConstants.addaNewQuote();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {
		return Accounter.constants().quotesList();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getEstimates(this);

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
		grid = new QuoteListGrid();
		grid.init();

	}

	protected SelectCombo getSelectItem() {
		viewSelect = new SelectCombo(Accounter.constants().currentView());
		viewSelect.setHelpInformation(true);
		listOfTypes = new ArrayList<String>();
		listOfTypes.add(OPEN);
		listOfTypes.add(REJECTED);
		listOfTypes.add(ACCEPTED);
		listOfTypes.add(EXPIRED);
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

		return viewSelect;
	}

	private void filterList(String text) {

		grid.removeAllRecords();

		for (ClientEstimate estimate : listOfEstimates) {
			if (text.equals(OPEN)) {
				if (estimate.getStatus() == STATUS_OPEN)
					grid.addData(estimate);
				continue;
			}
			if (text.equals(REJECTED)) {
				if (estimate.getStatus() == STATUS_REJECTED)
					grid.addData(estimate);
				continue;
			}
			if (text.equals(ACCEPTED)) {
				if (estimate.getStatus() == STATUS_ACCECPTED)
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
			// if (text.equals(DELETED)) {
			// if (estimate.getStatus() == ClientTransaction.STATUS_DELETED)
			// grid.addData(estimate);
			// continue;
			// }
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
		return Accounter.constants().quotes();
	}
}
