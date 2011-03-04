package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.grids.PurchaseOrderListGrid;

public class PurchaseOrderListView extends BaseListView<PurchaseOrdersList> {

	VendorsMessages vendorConstants = GWT.create(VendorsMessages.class);
	private PurchaseDetailesView purchaseDetailView;
	private List<PurchaseOrdersList> listOfPurchaseOrders;

	private static String OPEN = FinanceApplication.getVendorsMessages().open();
	private static String COMPLETED = FinanceApplication.getVendorsMessages()
			.completed();
	private static String CANCELLED = FinanceApplication.getVendorsMessages()
			.cancelled();

	// private static String CANCELLED = "Cancelled";

	public PurchaseOrderListView() {
		isDeleteDisable = true;
	}

	@Override
	protected Action getAddNewAction() {
		return VendorsActionFactory.getPurchaseOrderAction();
	}

	@Override
	protected String getAddNewLabelString() {
		// return FinanceApplication.getVendorsMessages().newPurchaseOrder();
		return "";
	}

	@Override
	protected String getListViewHeading() {
		return FinanceApplication.getVendorsMessages().purchaseOrderList();
	}

	@Override
	protected void initGrid() {
		grid = new PurchaseOrderListGrid(this);
		grid.init();

	}

	@Override
	public void updateInGrid(PurchaseOrdersList objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		FinanceApplication.createHomeService().getPurchaseOrders(this);

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
		super.onAttach();
	}

	@Override
	protected void onDetach() {
		gridLayout.remove(purchaseDetailView);
		super.onDetach();
	}

	public void onClick(PurchaseOrdersList obj) {
		AsyncCallback<IAccounterCore> callbackforsalesOrder = new AsyncCallback<IAccounterCore>() {

			@Override
			public void onSuccess(IAccounterCore result) {
				if (result != null)
					purchaseDetailView
							.setObjValues((ClientPurchaseOrder) result);
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		};
		rpcGetService.getObjectById(AccounterCoreType.PURCHASEORDER, obj
				.getTransactionId(), callbackforsalesOrder);

	}

	@Override
	public void onSuccess(List<PurchaseOrdersList> result) {
		super.onSuccess(result);
		listOfPurchaseOrders = result;
		filterList(OPEN);
		grid.setViewType(OPEN);
	}

	protected SelectCombo getSelectItem() {
		listOfTypes = new ArrayList<String>();
		viewSelect = new SelectCombo(FinanceApplication.getVendorsMessages()
				.currentView());
		listOfTypes.add(OPEN);
		listOfTypes.add(COMPLETED);
		listOfTypes.add(CANCELLED);
		viewSelect.initCombo(listOfTypes);
		viewSelect.setDefaultValue(OPEN);
		if (UIUtils.isMSIEBrowser())
			viewSelect.setWidth("120px");
		viewSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (viewSelect.getSelectedValue() != null) {
							grid.setViewType(viewSelect.getSelectedValue());
							filterList(viewSelect.getSelectedValue());
						}

					}
				});

		return viewSelect;
	}

	@SuppressWarnings("unchecked")
	private void filterList(String text) {
		grid.removeAllRecords();
		if (listOfPurchaseOrders != null) {
			for (PurchaseOrdersList purchaseOrder : listOfPurchaseOrders) {
				if (text.equals(OPEN)) {
					if (purchaseOrder.getStatus() == ClientTransaction.STATUS_OPEN
							|| purchaseOrder.getStatus() == ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED)
						grid.addData(purchaseOrder);
					continue;
				}
				if (text.equals(COMPLETED)) {
					if (purchaseOrder.getStatus() == ClientTransaction.STATUS_COMPLETED)
						grid.addData(purchaseOrder);
					continue;
				}
				if (text.equals(CANCELLED)) {
					if (purchaseOrder.getStatus() == ClientTransaction.STATUS_CANCELLED)
						grid.addData(purchaseOrder);
					continue;
				}
			}
		}
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		super.processupdateView(core, command);
		filterList(OPEN);
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
		// TODO Auto-generated method stub

	}

	@Override
	public boolean shouldSaveInHistory() {
		return true;
	}

	@Override
	protected void onLoad() {
		if (grid.getSelection() != null) {
			AsyncCallback<IAccounterCore> callbackforpurchaseOrder = new AsyncCallback<IAccounterCore>() {

				@Override
				public void onSuccess(IAccounterCore result) {
					if (result != null)
						purchaseDetailView
								.setObjValues((ClientPurchaseOrder) result);
				}

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}
			};
			rpcGetService.getObjectById(AccounterCoreType.PURCHASEORDER,
					((PurchaseOrdersList) grid.getSelection())
							.getTransactionId(), callbackforpurchaseOrder);
		}
		super.onLoad();
	}
}
