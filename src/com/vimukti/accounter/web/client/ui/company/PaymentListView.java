package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.SelectPaymentTypeDialog;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.PaymentsListGrid;

/**
 * 
 * @author Mandeep Singh
 * 
 */
public class PaymentListView extends TransactionsListView<PaymentsList>
		implements IPrintableView {
	private int checkType;
	// private static String DELETED = "Deleted";

	private static final int STATUS_NOT_ISSUED = 0;
	private static final int STATUS_ISSUED = 2;

	public static final int TYPE_CUSTOMER_CHECKS = 1;
	public static final int TYPE_VENDOR_CHECKS = 2;
	private int type = 0;

	public PaymentListView() {
		super(messages.notIssued());
	}

	public PaymentListView(int checkType) {
		super(messages.notIssued());
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
			return messages.addaNewPayment();
		} else {
			return messages.writeCheck();
		}
	}

	@Override
	protected String getListViewHeading() {
		if (checkType == 0) {
			return messages.paymentsList();
		} else if (checkType == TYPE_CUSTOMER_CHECKS) {
			return messages.payeeChecks(Global.get().Customer());
		} else {
			return messages.payeeChecks(Global.get().Vendor());
		}
	}

	@Override
	public void onSuccess(PaginationList<PaymentsList> result) {
		grid.removeAllRecords();
		if (result.isEmpty()) {
			updateRecordsCount(result.getStart(), grid.getTableRowCount(),
					result.getTotalCount());
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		grid.setRecords(result);
		grid.sort(12, false);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());
	}

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

	@Override
	protected java.util.List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.notIssued());
		listOfTypes.add(messages.issued());
		listOfTypes.add(messages.voided());
		listOfTypes.add(messages.all());
		listOfTypes.add(messages.drafts());
		return listOfTypes;
	}

	@Override
	protected void filterList(String text) {
		onPageChange(0, getPageSize());
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
		return messages.payments();
	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	protected void onPageChange(int start, int length) {

		if (viewType.equalsIgnoreCase(messages.notIssued())) {
			type = STATUS_NOT_ISSUED;
		} else if (viewType.equalsIgnoreCase(messages.issued())) {
			type = STATUS_ISSUED;
		} else if (viewType.equalsIgnoreCase(messages.voided())) {
			type = VIEW_VOIDED;
		} else if (viewType.equalsIgnoreCase(messages.all())) {
			type = TYPE_ALL;
		} else if (viewType.equalsIgnoreCase(messages.drafts())) {
			type = VIEW_DRAFT;
		}
		if (checkType == 0) {
			Accounter.createHomeService().getPaymentsList(
					getStartDate().getDate(), getEndDate().getDate(), start,
					length, type, this);
		} else if (checkType == TYPE_CUSTOMER_CHECKS) {
			Accounter.createHomeService().getPayeeChecks(true,
					getStartDate().getDate(), getEndDate().getDate(), start,
					length, type, this);
		} else {
			Accounter.createHomeService().getPayeeChecks(false,
					getStartDate().getDate(), getEndDate().getDate(), start,
					length, type, this);
		}
	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void exportToCsv() {
		if (checkType == 0) {
			Accounter.createExportCSVService().getPaymentsListExportCsv(
					getStartDate().getDate(), getEndDate().getDate(), type,
					getExportCSVCallback(messages.payments()));
		} else if (checkType == TYPE_CUSTOMER_CHECKS) {
			Accounter.createExportCSVService().getPayeeChecksExportCsv(true,
					getStartDate().getDate(), getEndDate().getDate(), type,
					getExportCSVCallback(messages.payments()));
		} else {
			Accounter.createExportCSVService().getPayeeChecksExportCsv(false,
					getStartDate().getDate(), getEndDate().getDate(), type,
					getExportCSVCallback(messages.payments()));
		}
	}
}
