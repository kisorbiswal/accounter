package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.PurchaseOrderListGrid;

public class PurchaseOrderListView extends BaseListView<PurchaseOrdersList> {

	private PurchaseDetailesView purchaseDetailView;
	private List<PurchaseOrdersList> listOfPurchaseOrders;

	private static String OPEN = Accounter.constants().open();
	private static String COMPLETED = Accounter.constants().completed();
	private static String CANCELLED = Accounter.constants().cancelled();

	// private static String CANCELLED = "Cancelled";

	public PurchaseOrderListView() {
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
			return Accounter.constants().addNewPurchaseOrder();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {
		return Accounter.constants().purchaseOrderList();
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
		Accounter.createHomeService().getPurchaseOrders(this);

	}

	@Override
	public void fitToSize(int height, int width) {
		if (height > 0 && width - 350 > 0) {
			grid.setHeight("100%");
			grid.setWidth("100%");
		}

	}

	@Override
	protected void onAttach() {
		purchaseDetailView = new PurchaseDetailesView();
		gridLayout.add(purchaseDetailView);
		gridLayout.setCellWidth(purchaseDetailView, "30%");
		super.onAttach();
	}

	@Override
	protected void onDetach() {
		gridLayout.remove(purchaseDetailView);
		super.onDetach();
	}

	public void onClick(PurchaseOrdersList obj) {
		AccounterAsyncCallback<IAccounterCore> callbackforsalesOrder = new AccounterAsyncCallback<IAccounterCore>() {

			@Override
			public void onResultSuccess(IAccounterCore result) {
				if (result != null)
					purchaseDetailView
							.setObjValues((ClientPurchaseOrder) result);
			}

			@Override
			public void onException(AccounterException caught) {

			}
		};
		rpcGetService.getObjectById(AccounterCoreType.PURCHASEORDER,
				obj.getTransactionId(), Accounter.getCompany().getID(),
				callbackforsalesOrder);

	}

	@Override
	public void onSuccess(ArrayList<PurchaseOrdersList> result) {
		super.onSuccess(result);
		listOfPurchaseOrders = result;
		filterList(viewSelect.getValue().toString());
		grid.setViewType(viewSelect.getValue().toString());
	}

	protected SelectCombo getSelectItem() {
		listOfTypes = new ArrayList<String>();
		viewSelect = new SelectCombo(Accounter.constants().currentView());
		listOfTypes.add(OPEN);
		listOfTypes.add(COMPLETED);
		listOfTypes.add(CANCELLED);
		viewSelect.initCombo(listOfTypes);

		if (UIUtils.isMSIEBrowser())
			viewSelect.setWidth("120px");
		viewSelect.setComboItem(OPEN);
		viewSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (viewSelect.getSelectedValue() != null) {
							grid.setViewType(viewSelect.getSelectedValue()
									.toString());
							filterList(viewSelect.getSelectedValue().toString());

						}

					}
				});

		return viewSelect;
	}

	private void filterList(String text) {
		grid.removeAllRecords();
		if (listOfPurchaseOrders != null) {
			for (PurchaseOrdersList purchaseOrder : listOfPurchaseOrders) {
				if (text.equals(OPEN)) {
					if (purchaseOrder.getStatus() == ClientTransaction.STATUS_OPEN
							|| purchaseOrder.getStatus() == ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED)
						grid.addData(purchaseOrder);
					if (grid.getRecords().isEmpty()) {
						purchaseDetailView.itemsGrid.clear();
						purchaseDetailView.itemsGrid
								.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
					}
					continue;
				}
				if (text.equals(COMPLETED)) {
					if (purchaseOrder.getStatus() == ClientTransaction.STATUS_COMPLETED)
						grid.addData(purchaseOrder);
					if (grid.getRecords().isEmpty()) {
						if (purchaseDetailView.itemsGrid != null) {
							purchaseDetailView.itemsGrid.clear();
							purchaseDetailView.itemsGrid
									.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
						}
					}
					continue;
				}
				if (text.equals(CANCELLED)) {
					if (purchaseOrder.getStatus() == ClientTransaction.STATUS_CANCELLED)
						grid.addData(purchaseOrder);
					if (grid.getRecords().isEmpty()) {
						if (purchaseDetailView.itemsGrid != null) {
							purchaseDetailView.itemsGrid.clear();
							purchaseDetailView.itemsGrid
									.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
						}
					}
					continue;
				}
			}
		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
		}
		if (purchaseDetailView.itemsGrid != null)
			if (purchaseDetailView.itemsGrid.getRecords().isEmpty()) {
				purchaseDetailView.itemsGrid
						.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);
			}
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

	@Override
	protected void onLoad() {
		if (grid.getSelection() != null) {
			AccounterAsyncCallback<IAccounterCore> callbackforpurchaseOrder = new AccounterAsyncCallback<IAccounterCore>() {

				@Override
				public void onResultSuccess(IAccounterCore result) {
					if (result != null)
						purchaseDetailView
								.setObjValues((ClientPurchaseOrder) result);
				}

				@Override
				public void onException(AccounterException caught) {

				}
			};
			rpcGetService.getObjectById(AccounterCoreType.PURCHASEORDER,
					((PurchaseOrdersList) grid.getSelection())
							.getTransactionId(),
					Accounter.getCompany().getID(), callbackforpurchaseOrder);
		}
		super.onLoad();
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().purchaseOrders();
	}
}
