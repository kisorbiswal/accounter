package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
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
		super.initListCallback();
		Accounter.createHomeService().getReceivePaymentsList(
				getStartDate().getDate(), getEndDate().getDate(),
				transactionType, this);
	}

	@Override
	public void onSuccess(ArrayList<ReceivePaymentsList> result) {
		super.onSuccess(result);
		listOfRecievePayments = result;
		filterList(viewSelect.getSelectedValue());
		grid.setViewType(viewSelect.getSelectedValue());
		grid.sort(10, false);
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
		for (ReceivePaymentsList recievePayment : listOfRecievePayments) {
			// if (text.equals(OPEN)) {
			// if ((recievePayment.getStatus() == STATUS_UNAPPLIED ||
			// recievePayment
			// .getStatus() == STATUS_PARTIALLY_APPLIED)
			// && (!recievePayment.isVoided()))
			// grid.addData(recievePayment);
			//
			// continue;
			// }
			// if (text.equals(FULLY_APPLIED)) {
			// if (recievePayment.getStatus() == STATUS_APPLIED
			// && !recievePayment.isVoided())
			// grid.addData(recievePayment);
			//
			// continue;
			// }
			if (text.equals(messages().paid())) {
				if (!recievePayment.isVoided()) {
					grid.addData(recievePayment);
				}
			} else if (text.equals(messages().voided())) {
				if (recievePayment.isVoided() && !recievePayment.isDeleted()) {
					grid.addData(recievePayment);
				}
				continue;
			} else if (text.equals(messages().all())) {
				grid.addData(recievePayment);
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
		return messages().recievePayments();
	}

}
