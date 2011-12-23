package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.VendorPaymentsListGrid;

/**
 * @modified by Ravi kiran.G
 * 
 */

public class VendorPaymentsListView extends TransactionsListView<PaymentsList> {

	protected List<PaymentsList> allPayments;
	private int transactionType;

	private VendorPaymentsListView() {
		super(Accounter.messages().notIssued());
	}

	public VendorPaymentsListView(int transactionType) {
		super(Accounter.messages().notIssued());
		this.transactionType = transactionType;
	}

	public static VendorPaymentsListView getInstance() {
		return new VendorPaymentsListView();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Action getAddNewAction() {
		return ActionFactory.getNewVendorPaymentAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return messages().addANewVendorPayment(Global.get().Vendor());
	}

	@Override
	protected String getListViewHeading() {
		return messages().payeePaymentList(Global.get().Vendor());
	}

	@Override
	public void initListCallback() {
		onPageChange(0, getPageSize());

	}

	@Override
	public void updateInGrid(PaymentsList objectTobeModified) {

	}

	@Override
	protected void initGrid() {
		grid = new VendorPaymentsListGrid(false, transactionType);
		grid.init();
		grid.setViewType(messages().notIssued());
	}

	// @Override
	// public void onSuccess(ArrayList<PaymentsList> result) {
	// super.onSuccess(result);
	// grid.setViewType(messages().all());
	// filterList(messages().all());
	// grid.sort(10, false);
	// }
	@Override
	public void onSuccess(PaginationList<PaymentsList> result) {
		super.onSuccess(result);
		grid.setViewType(messages().all());
		filterList(messages().all());
		grid.sort(10, false);
		updateRecordsCount(result.getStart(), grid.getRowCount(),
				result.getTotalCount());
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

	@SuppressWarnings("unchecked")
	@Override
	protected void filterList(String text) {
		grid.removeAllRecords();
		if (viewSelect.getSelectedValue().equalsIgnoreCase("Not Issued")) {
			List<PaymentsList> notIssuedRecs = new ArrayList<PaymentsList>();
			List<PaymentsList> allRecs = initialRecords;
			for (PaymentsList rec : allRecs) {
				if (Utility.getStatus(rec.getType(), rec.getStatus())
						.equalsIgnoreCase("Not Issued") && !rec.isVoided()) {
					notIssuedRecs.add(rec);
				}
			}
			grid.setRecords(notIssuedRecs);
		} else if (viewSelect.getSelectedValue().equalsIgnoreCase("Issued")) {
			List<PaymentsList> issued = new ArrayList<PaymentsList>();
			List<PaymentsList> allRecs = initialRecords;
			for (PaymentsList rec : allRecs) {
				if (Utility.getStatus(rec.getType(), rec.getStatus())
						.equalsIgnoreCase("Issued") && !rec.isVoided()) {
					issued.add(rec);
				}
			}
			grid.setRecords(issued);
		} else if (viewSelect.getSelectedValue().equalsIgnoreCase("Voided")) {
			List<PaymentsList> voidedRecs = new ArrayList<PaymentsList>();
			List<PaymentsList> allRecs = initialRecords;
			for (PaymentsList rec : allRecs) {
				if (rec.isVoided()
						&& rec.getStatus() != ClientTransaction.STATUS_DELETED) {
					voidedRecs.add(rec);
				}
			}
			grid.setRecords(voidedRecs);
		}
		// else if (currentView.getValue().toString().equalsIgnoreCase(
		// "Deleted")) {
		// List<PaymentsList> deletedRecs = new
		// ArrayList<PaymentsList>();
		// List<PaymentsList> allRecs = initialRecords;
		// for (PaymentsList rec : allRecs) {
		// if (rec.getStatus() == ClientTransaction.STATUS_DELETED) {
		// deletedRecs.add(rec);
		// }
		// }
		// grid.setRecords(deletedRecs);
		//
		// }
		if (viewSelect.getSelectedValue().equalsIgnoreCase("All")) {
			grid.setRecords(initialRecords);
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
		return messages().payeePayments(Global.get().Vendor());
	}

	@Override
	protected int getPageSize() {
		return 10;
	}

	@Override
	protected void onPageChange(int start, int length) {
		Accounter.createHomeService().getVendorPaymentsList(
				getStartDate().getDate(), getEndDate().getDate(), start,
				length, this);
	}
}
