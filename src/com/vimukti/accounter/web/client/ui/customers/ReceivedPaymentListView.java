package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.ReceivedPaymentListGrid;

/**
 * 
 * @modified Fernandez
 * 
 */
public class ReceivedPaymentListView extends
		TransactionsListView<ReceivePaymentsList> {

	private List<ReceivePaymentsList> listOfRecievePayments;
	private int transactionType;
	private int viewType;

	public ReceivedPaymentListView() {
		super(Accounter.messages().paid());
	}

	public ReceivedPaymentListView(int type) {
		super(Accounter.messages().paid());
		this.transactionType = type;
	}

	@Override
	protected Action getAddNewAction() {
		if (transactionType == ClientTransaction.TYPE_RECEIVE_PAYMENT) {
			return ActionFactory.getReceivePaymentAction();
		} else if (transactionType == ClientTransaction.TYPE_CUSTOMER_PREPAYMENT) {
			return ActionFactory.getNewCustomerPaymentAction();
		}
		return ActionFactory.getPaymentDialogAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return messages().addaNewPayment();

	}

	@Override
	protected String getListViewHeading() {
		if (transactionType == ClientTransaction.TYPE_CUSTOMER_PREPAYMENT) {
			return messages().payeePayments(Global.get().Customer());
		}
		return messages().getReceivedPaymentListViewHeading();
	}

	@Override
	public void initListCallback() {
		onPageChange(0, getPageSize());
	}

	@Override
	public void onSuccess(PaginationList<ReceivePaymentsList> result) {
		super.onSuccess(result);
		listOfRecievePayments = result;
		grid.removeAllRecords();
		grid.setRecords(result);
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(messages().noRecordsToShow());

		grid.sort(12, false);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());
	}

	@Override
	public void updateInGrid(ReceivePaymentsList objectTobeModified) {
	}

	@Override
	protected void initGrid() {
		grid = new ReceivedPaymentListGrid(false, transactionType);
		grid.init();

	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages().all());
		listOfTypes.add(messages().paid());
		// listOfTypes.add(OPEN);
		// listOfTypes.add(FULLY_APPLIED);
		listOfTypes.add(messages().voided());
		return listOfTypes;
	}

	protected void filterList(String text) {
		grid.removeAllRecords();
		if (text.equals(messages().paid())) {
			viewType = ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		} else if (text.equals(messages().voided())) {
			viewType = ClientTransaction.VIEW_VOIDED;
		} else if (text.equals(messages().all())) {
			viewType = ClientTransaction.VIEW_ALL;
		}
		onPageChange(0, getPageSize());
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
		return messages().recievePayments();
	}

	@Override
	protected int getPageSize() {
		return 25;
	}

	protected void onPageChange(int start, int length) {
		Accounter.createHomeService().getReceivePaymentsList(
				getStartDate().getDate(), getEndDate().getDate(),
				transactionType, start, length, viewType, this);
	};
}
