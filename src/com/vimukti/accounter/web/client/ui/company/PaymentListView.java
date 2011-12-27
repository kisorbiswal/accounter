package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.SelectPaymentTypeDialog;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.PaymentsListGrid;

/**
 * 
 * @author Mandeep Singh
 * 
 */
public class PaymentListView extends TransactionsListView<PaymentsList> {
	List<PaymentsList> allPayments;
	private List<PaymentsList> listOfPayments;
	private int checkType;
	private int viewType;
	// private static String DELETED = "Deleted";

	private static final int STATUS_NOT_ISSUED = 0;
	private static final int STATUS_PARTIALLY_PAID = 1;
	private static final int STATUS_ISSUED = 2;
	private static final int STATUS_VOID = 4;

	public static final int TYPE_CUSTOMER_CHECKS = 1;
	public static final int TYPE_VENDOR_CHECKS = 2;

	public PaymentListView() {
		super(Accounter.messages().notIssued());
	}

	public PaymentListView(int checkType) {
		super(Accounter.messages().notIssued());
		this.checkType = checkType;
	}

	public static PaymentListView getInstance() {
		return new PaymentListView();
	}

	@Override
	protected Action getAddNewAction() {
		if (checkType == 0) {
			new SelectPaymentTypeDialog().show();
		} else {
			return ActionFactory.getWriteChecksAction();
		}
		return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (checkType == 0) {
			return messages().addaNewPayment();
		} else {
			return messages().writeCheck();
		}
	}

	@Override
	protected String getListViewHeading() {
		if (checkType == 0) {
			return messages().paymentsList();
		} else if (checkType == TYPE_CUSTOMER_CHECKS) {
			return messages().payeeChecks(Global.get().Customer());
		} else {
			return messages().payeeChecks(Global.get().Vendor());
		}
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		if (checkType == 0) {
//			Accounter.createHomeService().getPaymentsList(
//					getStartDate().getDate(), getEndDate().getDate(), this);
		} else if (checkType == TYPE_CUSTOMER_CHECKS) {
//			Accounter.createHomeService().getPayeeChecks(true,
//					getStartDate().getDate(), getEndDate().getDate(), this);
		} else {
//			Accounter.createHomeService().getPayeeChecks(false,
//					getStartDate().getDate(), getEndDate().getDate(), this);
		}
	}

//	@Override
//	public void onSuccess(ArrayList<PaymentsList> result) {
//		super.onSuccess(result);
//		listOfPayments = result;
//		filterList(viewSelect.getSelectedValue());
//		grid.setViewType(viewSelect.getSelectedValue());
//		grid.sort(10, false);
//	}

	@Override
	public void updateInGrid(PaymentsList objectTobeModified) {
		// NOTHING TO DO.
	}

	@Override
	protected void initGrid() {
		grid = new PaymentsListGrid(false, checkType);
		grid.init();
		grid.setEditEventType(ListGrid.EDIT_EVENT_DBCLICK);
		// FinanceApplication.createHomeService().getPaymentsList(this);
	}

	protected java.util.List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages().notIssued());
		listOfTypes.add(messages().issued());
		listOfTypes.add(messages().Voided());
		listOfTypes.add(messages().all());
		return listOfTypes;
	}

	protected void filterList(String text) {

		grid.removeAllRecords();
		if (viewSelect.getSelectedValue().equalsIgnoreCase("Not Issued")) {
			viewType = STATUS_NOT_ISSUED;
		} else if (viewSelect.getSelectedValue().equalsIgnoreCase("Issued")) {
			viewType = STATUS_ISSUED;
		} else if (viewSelect.getSelectedValue().equalsIgnoreCase("Voided")) {
			viewType = ClientTransaction.VIEW_VOIDED;
		} else if (viewSelect.getSelectedValue().equalsIgnoreCase("All")) {
			viewType = ClientTransaction.TYPE_ALL;
		}
		onPageChange(0, getPageSize());

//		for (PaymentsList payment : listOfPayments) {
//			if (text.equals(messages().notIssued())) {
//				if ((payment.getStatus() == STATUS_NOT_ISSUED || payment
//						.getStatus() == STATUS_PARTIALLY_PAID)
//						&& (!payment.isVoided()))
//					grid.addData(payment);
//				continue;
//			}
//			if (text.equals(messages().issued())) {
//				if (payment.getStatus() == STATUS_ISSUED
//						&& (!payment.isVoided()))
//					grid.addData(payment);
//
//				continue;
//			}
//			if (text.equals(messages().Voided())) {
//				if (payment.isVoided()
//				// && payment.getStatus()!=ClientTransaction.STATUS_DELETED
//				)
//					grid.addData(payment);
//				continue;
//			}
//			// if (text.equals(DELETED)) {
//			// if(payment.getStatus()==ClientTransaction.STATUS_DELETED)
//			// grid.addData(payment);
//			// continue;
//			// }
//			if (text.equals(messages().all())) {
//				grid.addData(payment);
//			}
//		}
//		if (grid.getRecords().isEmpty())
//			grid.addEmptyMessage(messages().noRecordsToShow());
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
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return messages().payments();
	}

}
