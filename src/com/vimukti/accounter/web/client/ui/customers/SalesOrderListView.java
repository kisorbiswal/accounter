package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.SalesOrdersList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.SalesOrderListGrid;

public class SalesOrderListView extends TransactionsListView<SalesOrdersList> {

	protected List<SalesOrdersList> salesList;

	// private SalesDetailesView salesDetailView;

	private List<SalesOrdersList> listOfSalesOrder;

	// private static String CANCELLED = "Cancelled";

	public SalesOrderListView() {
		super(messages.open());
		isDeleteDisable = true;
	}

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return ActionFactory.getSalesOrderAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return messages.addNewSalesOrder();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {
		return messages.salesOrderList();
	}

	@Override
	protected void initGrid() {
		grid = new SalesOrderListGrid(this);
		grid.init();
		// grid.setHeight("75%");
		// grid.setWidth(600+"px");

	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getSalesOrders(getStartDate().getDate(),
				getEndDate().getDate(), this);
	}

	@Override
	public void updateInGrid(SalesOrdersList objectTobeModified) {
		// NOTHING TO DO.
	}

	// @Override
	// protected void onAttach() {
	// salesDetailView = new SalesDetailesView();
	// // salesDetailView.setPreferences(getPreferences());
	// salesDetailView.init();
	// gridLayout.add(salesDetailView);
	// super.onAttach();
	// }
	//
	// @Override
	// protected void onDetach() {
	// gridLayout.remove(salesDetailView);
	// super.onDetach();
	// }

	// public void onClick(SalesOrdersList obj) {
	// AccounterAsyncCallback<IAccounterCore> callbackforsalesOrder = new
	// AccounterAsyncCallback<IAccounterCore>() {
	//
	// @Override
	// public void onResultSuccess(IAccounterCore result) {
	// if (result != null)
	// salesDetailView.setObjValues((ClientSalesOrder) result);
	// }
	//
	// @Override
	// public void onException(AccounterException caught) {
	//
	// }
	// };
	// rpcGetService.getObjectById(AccounterCoreType.SALESORDER,
	// obj.getTransactionId(), callbackforsalesOrder);
	//
	// }

	@Override
	public void onSuccess(PaginationList<SalesOrdersList> result) {
		super.onSuccess(result);
		listOfSalesOrder = result;
		filterList(viewSelect.getValue().toString());
		grid.setViewType(viewSelect.getValue().toString());
		grid.sort(10, false);
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.all());
		listOfTypes.add(messages.completed());
		listOfTypes.add(messages.cancelled());
		listOfTypes.add(messages.drafts());
		return listOfTypes;
	}

	@Override
	protected void filterList(String text) {
		grid.removeAllRecords();
		if (listOfSalesOrder != null) {
			for (SalesOrdersList salesOrder : listOfSalesOrder) {
				if (text.equals(messages.open())) {
					if (salesOrder.getStatus() == ClientTransaction.STATUS_OPEN
							|| salesOrder.getStatus() == ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED)
						grid.addData(salesOrder);
					// if (grid.getRecords().isEmpty()) {
					// salesDetailView.itemsGrid.clear();
					// salesDetailView.itemsGrid.addEmptyMessage(messages
					// .noRecordsToShow());
					// }
					continue;
				}
				if (text.equals(messages.completed())) {
					if (salesOrder.getStatus() == ClientTransaction.STATUS_COMPLETED)
						grid.addData(salesOrder);
					// if (grid.getRecords().isEmpty()) {
					// salesDetailView.itemsGrid.clear();
					// salesDetailView.itemsGrid.addEmptyMessage(messages
					// .noRecordsToShow());
					// }
					continue;
				}
				if (text.equals(messages.cancelled())) {
					if (salesOrder.getStatus() == ClientTransaction.STATUS_CANCELLED)
						grid.addData(salesOrder);
					// if (grid.getRecords().isEmpty()) {
					// salesDetailView.itemsGrid.clear();
					// salesDetailView.itemsGrid.addEmptyMessage(messages
					// .noRecordsToShow());
					// }
					continue;
				}
				if (text.equalsIgnoreCase(messages.drafts())) {
					if (salesOrder.getStatus() == ClientTransaction.STATUS_DRAFT)
						grid.addData(salesOrder);
					// if (grid.getRecords().isEmpty()) {
					// salesDetailView.itemsGrid.clear();
					// salesDetailView.itemsGrid.addEmptyMessage(messages
					// .noRecordsToShow());
					// }
					continue;
				}

			}
		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
		// if (salesDetailView.itemsGrid.getRecords().isEmpty()) {
		// salesDetailView.itemsGrid.addEmptyMessage(messages
		// .noRecordsToShow());
		// }
	}

	@Override
	public void fitToSize(int height, int width) {
		if (height > 0 && width - 350 > 0) {
			grid.setHeight("100%");
			grid.setWidth("100%");
		}
	}

	@Override
	public void onEdit() {
		// Nothing to do

	}

	@Override
	public void print() {
		// Nothing to do

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	public boolean shouldSaveInHistory() {
		return true;
	}

	// @Override
	// protected void onLoad() {
	// if (grid.getSelection() != null) {
	// AccounterAsyncCallback<IAccounterCore> callbackforsalesOrder = new
	// AccounterAsyncCallback<IAccounterCore>() {
	//
	// @Override
	// public void onResultSuccess(IAccounterCore result) {
	// if (result != null)
	// salesDetailView.setObjValues((ClientSalesOrder) result);
	// }
	//
	// @Override
	// public void onException(AccounterException caught) {
	// // TODO Auto-generated method stub
	//
	// }
	// };
	// rpcGetService.getObjectById(AccounterCoreType.SALESORDER,
	// ((SalesOrdersList) grid.getSelection()).getTransactionId(),
	// callbackforsalesOrder);
	// }
	// super.onLoad();
	// }

	@Override
	protected String getViewTitle() {
		return messages.salesOrders();
	}

}
