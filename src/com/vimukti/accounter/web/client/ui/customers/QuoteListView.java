package com.vimukti.accounter.web.client.ui.customers;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.CustomersActionFactory;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.grids.QuoteListGrid;

public class QuoteListView extends BaseListView<ClientEstimate> {

	private CustomersMessages customerConstants = GWT
			.create(CustomersMessages.class);

	protected List<ClientEstimate> estimates;

	private SelectItem viewSelect;

	private List<ClientEstimate> listOfEstimates;

	private static String OPEN = FinanceApplication.getCustomersMessages()
			.open();
	private static String REJECTED = FinanceApplication.getCustomersMessages()
			.rejected();
	private static String ACCEPTED = FinanceApplication.getCustomersMessages()
			.accepted();
	private static String EXPIRED = FinanceApplication.getCustomersMessages()
			.expired();
	private static String ALL = FinanceApplication.getCustomersMessages().all();
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
		return CustomersActionFactory.getNewQuoteAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return customerConstants.addaNewQuote();
	}

	@Override
	protected String getListViewHeading() {
		return FinanceApplication.getCustomersMessages().quotesList();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		FinanceApplication.createHomeService().getEstimates(this);

	}

	@Override
	public void onSuccess(List<ClientEstimate> result) {
		super.onSuccess(result);
		listOfEstimates = result;
		filterList(viewSelect.getValue().toString());
		grid.setViewType(viewSelect.getValue().toString());
	}

	@Override
	public void updateInGrid(ClientEstimate objectTobeModified) {

	}

	@Override
	protected void initGrid() {
		grid = new QuoteListGrid();
		grid.init();

	}

	protected SelectItem getSelectItem() {
		viewSelect = new SelectItem(FinanceApplication.getCustomersMessages()
				.currentView());
		viewSelect.setValueMap(OPEN, REJECTED, ACCEPTED, EXPIRED, ALL
		// , DELETED
				);

		if (UIUtils.isMSIEBrowser())
			viewSelect.setWidth("150px");

		viewSelect.setDefaultValue(OPEN);
		viewSelect.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (viewSelect.getValue() != null) {
					grid.setViewType(viewSelect.getValue().toString());
					filterList(viewSelect.getValue().toString());
				}

			}
		});

		return viewSelect;
	}

	@SuppressWarnings("unchecked")
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
				ClientFinanceDate expiryDate = new ClientFinanceDate(estimate
						.getExpirationDate());
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
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

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
