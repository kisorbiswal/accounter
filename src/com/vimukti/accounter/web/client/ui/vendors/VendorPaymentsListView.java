package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.PaginationList;
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

	private int transactionType;
	private int viewType;

	private VendorPaymentsListView() {
		super(messages.notIssued());
	}

	public VendorPaymentsListView(int transactionType) {
		super(messages.notIssued());
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
		return messages.addANewVendorPayment(Global.get().Vendor());
	}

	@Override
	protected String getListViewHeading() {
		return messages.payeePaymentList(Global.get().Vendor());
	}

	@Override
	public void updateInGrid(PaymentsList objectTobeModified) {

	}

	@Override
	protected void initGrid() {
		grid = new VendorPaymentsListGrid(false, transactionType);
		grid.init();
		grid.setViewType(messages.notIssued());
	}

	@Override
	public void onSuccess(PaginationList<PaymentsList> result) {
		grid.removeAllRecords();
		grid.setRecords(result);
		grid.sort(12, false);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), result.size(),
				result.getTotalCount());
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}

	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.notIssued());
		listOfTypes.add(messages.issued());
		listOfTypes.add(messages.voided());
		listOfTypes.add(messages.all());
		return listOfTypes;
	}

	@Override
	protected void filterList(String text) {
		grid.removeAllRecords();
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
		return messages.payeePayments(Global.get().Vendor());
	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	protected void onPageChange(int start, int length) {
		checkViewType();
		Accounter.createHomeService().getVendorPaymentsList(
				getStartDate().getDate(), getEndDate().getDate(), start,
				length, viewType, this);
	}

	public void checkViewType() {
		if (viewSelect.getSelectedValue().equalsIgnoreCase("Not Issued")) {
			this.viewType = ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		} else if (viewSelect.getSelectedValue().equalsIgnoreCase("Issued")) {
			this.viewType = ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		} else if (viewSelect.getSelectedValue().equalsIgnoreCase("Voided")) {
			this.viewType = ClientTransaction.VIEW_VOIDED;
		} else if (viewSelect.getSelectedValue().equalsIgnoreCase("All")) {
			this.viewType = ClientTransaction.TYPE_ALL;
		}
	}
}
