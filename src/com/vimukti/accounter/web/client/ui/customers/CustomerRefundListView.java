package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.CustomerRefundsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.CustomerRefundListGrid;

/**
 * 
 * @author Fernandez
 * @param <T>
 * 
 */
public class CustomerRefundListView extends
		TransactionsListView<CustomerRefundsList> {
	protected List<CustomerRefundsList> transactions;
	private List<CustomerRefundsList> listOfCustomerRefund;
	private int transactionType;

	// private static String DELETED="Deleted";

	private static final int STATUS_NOT_ISSUED = 0;
	private static final int STATUS_PARTIALLY_PAID = 1;
	private static final int STATUS_ISSUED = 2;

	public CustomerRefundListView() {
		super(Accounter.messages().issued());
	}

	public CustomerRefundListView(int transactionType) {
		super(Accounter.messages().issued());
		this.transactionType = transactionType;
	}

	@Override
	protected Action getAddNewAction() {
		return ActionFactory.getCustomerRefundAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return messages().addaNew(
				messages().customerRefund(Global.get().Customer()));
	}

	@Override
	protected String getListViewHeading() {
		return messages().getCustomersRefundListViewHeading(
				Global.get().Customer());
	}

	@Override
	protected void initGrid() {
		grid = new CustomerRefundListGrid(false);
		grid.init();

	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getCustomerRefundsList(
				getStartDate().getDate(), getEndDate().getDate(), this);
	}

	@Override
	public void onSuccess(PaginationList<CustomerRefundsList> result) {
		super.onSuccess(result);
		listOfCustomerRefund = result;
		filterList(viewSelect != null ? viewSelect.getSelectedValue()
				: messages().notIssued());
		grid.setViewType(viewSelect != null ? viewSelect.getSelectedValue()
				: messages().notIssued());
		grid.sort(10, false);
	}

	@Override
	public void updateInGrid(CustomerRefundsList objectTobeModified) {
		// its not using any where

	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages().notIssued());
		listOfTypes.add(messages().issued());
		listOfTypes.add(messages().voided());
		listOfTypes.add(messages().all());
		return listOfTypes;
	}

	@Override
	protected void filterList(String text) {
		grid.removeAllRecords();
		for (CustomerRefundsList customerRefund : listOfCustomerRefund) {
			if (text.equals(messages().notIssued())) {
				if ((customerRefund.getStatus() == STATUS_NOT_ISSUED || customerRefund
						.getStatus() == STATUS_PARTIALLY_PAID)
						&& (!customerRefund.isVoided())) {
					grid.addData(customerRefund);
				}
				continue;
			}
			if (text.equals(messages().issued())) {
				if (customerRefund.getStatus() == STATUS_ISSUED
						&& (!customerRefund.isVoided())) {
					grid.addData(customerRefund);
				}
				continue;
			}
			if (text.equals(messages().voided())) {
				if (customerRefund.isVoided() && !customerRefund.isDeleted()) {
					grid.addData(customerRefund);
				}
				continue;
			}
			// if (text.equals(DELETED)) {
			// if (customerRefund.isDeleted())
			// grid.addData(customerRefund);
			// continue;
			// }
			if (text.equals(messages().all())) {
				grid.addData(customerRefund);
			}
		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages().noRecordsToShow());
		}
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
		// its not using any where

	}

	@Override
	protected String getViewTitle() {
		return messages().customerRefunds(Global.get().Customer());
	}
}
