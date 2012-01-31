package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.PurchaseOrderListGrid;

public class PurchaseOrderListView extends
		TransactionsListView<PurchaseOrdersList> {

	// private PurchaseDetailesView purchaseDetailView;
	private List<PurchaseOrdersList> listOfPurchaseOrders;

	// private static String CANCELLED = "Cancelled";

	public PurchaseOrderListView() {
		super(messages.open());
		isDeleteDisable = true;
	}

	@Override
	protected Action getAddNewAction() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return ActionFactory.getPurchaseOrderAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (Accounter.getUser().canDoInvoiceTransactions())
			return messages.addNewPurchaseOrder();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {
		return messages.purchaseOrderList();
	}

	@Override
	protected void initGrid() {
		grid = new PurchaseOrderListGrid(this);
		grid.init();
	}

	@Override
	public void updateInGrid(PurchaseOrdersList objectTobeModified) {
		// TODO currently not using anywhere

	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getPurchaseOrders(
				getStartDate().getDate(), getEndDate().getDate(), this);

	}

	@Override
	public void fitToSize(int height, int width) {
		if (height > 0 && width - 350 > 0) {
			grid.setHeight("100%");
			grid.setWidth("100%");
		}

	}

	// @Override
	// protected void onAttach() {
	// purchaseDetailView = new PurchaseDetailesView();
	// gridLayout.add(purchaseDetailView);
	// gridLayout.setCellWidth(purchaseDetailView, "30%");
	// super.onAttach();
	// }
	//
	// @Override
	// protected void onDetach() {
	// gridLayout.remove(purchaseDetailView);
	// super.onDetach();
	// }

	@Override
	public void onSuccess(PaginationList<PurchaseOrdersList> result) {
		super.onSuccess(result);
		listOfPurchaseOrders = result;
		filterList(viewSelect.getValue().toString());
		grid.setViewType(viewSelect.getValue().toString());
	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.open());
		listOfTypes.add(messages.completed());
		listOfTypes.add(messages.cancelled());
		listOfTypes.add(messages.drafts());
		return listOfTypes;
	}

	@Override
	protected void filterList(String text) {
		grid.removeAllRecords();
		if (listOfPurchaseOrders != null) {
			for (PurchaseOrdersList purchaseOrder : listOfPurchaseOrders) {
				if (text.equals(messages.open())) {
					if (purchaseOrder.getStatus() == ClientTransaction.STATUS_OPEN
							|| purchaseOrder.getStatus() == ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED)
						grid.addData(purchaseOrder);
					// if (grid.getRecords().isEmpty()) {
					// purchaseDetailView.itemsGrid.clear();
					// purchaseDetailView.itemsGrid.addEmptyMessage(messages
					// .noRecordsToShow());
					// }
					continue;
				}
				if (text.equals(messages.completed())) {
					if (purchaseOrder.getStatus() == ClientTransaction.STATUS_COMPLETED)
						grid.addData(purchaseOrder);
					// if (grid.getRecords().isEmpty()) {
					// if (purchaseDetailView.itemsGrid != null) {
					// purchaseDetailView.itemsGrid.clear();
					// purchaseDetailView.itemsGrid
					// .addEmptyMessage(messages.noRecordsToShow());
					// }
					// }
					continue;
				}
				if (text.equals(messages.cancelled())) {
					if (purchaseOrder.getStatus() == ClientTransaction.STATUS_CANCELLED)
						grid.addData(purchaseOrder);
					// if (grid.getRecords().isEmpty()) {
					// if (purchaseDetailView.itemsGrid != null) {
					// purchaseDetailView.itemsGrid.clear();
					// purchaseDetailView.itemsGrid
					// .addEmptyMessage(messages.noRecordsToShow());
					// }
					// }
					continue;
				}
				if (text.equalsIgnoreCase(messages.drafts())) {
					if (purchaseOrder.getStatus() == VIEW_DRAFT)
						grid.addData(purchaseOrder);
					// if (grid.getRecords().isEmpty()) {
					// if (purchaseDetailView.itemsGrid != null) {
					// purchaseDetailView.itemsGrid.clear();
					// purchaseDetailView.itemsGrid
					// .addEmptyMessage(messages.noRecordsToShow());
					// }
					// }
					continue;
				}
			}
		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
		// if (purchaseDetailView.itemsGrid != null)
		// if (purchaseDetailView.itemsGrid.getRecords().isEmpty()) {
		// purchaseDetailView.itemsGrid.addEmptyMessage(messages
		// .noRecordsToShow());
		// }
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {
		// its not using any where return null;
	}

	@Override
	public boolean shouldSaveInHistory() {
		return true;
	}

	// @Override
	// protected void onLoad() {
	// if (grid.getSelection() != null) {
	// AccounterAsyncCallback<IAccounterCore> callbackforpurchaseOrder = new
	// AccounterAsyncCallback<IAccounterCore>() {
	//
	// @Override
	// public void onResultSuccess(IAccounterCore result) {
	// if (result != null)
	// purchaseDetailView
	// .setObjValues((ClientPurchaseOrder) result);
	// }
	//
	// @Override
	// public void onException(AccounterException caught) {
	//
	// }
	// };
	// rpcGetService.getObjectById(AccounterCoreType.PURCHASEORDER,
	// ((PurchaseOrdersList) grid.getSelection())
	// .getTransactionId(), callbackforpurchaseOrder);
	// }
	// super.onLoad();
	// }

	@Override
	protected String getViewTitle() {
		return messages.purchaseOrders();
	}
}
