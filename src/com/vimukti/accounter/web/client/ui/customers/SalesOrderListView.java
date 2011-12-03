package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.SalesOrdersList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.SalesOrderListGrid;

public class SalesOrderListView extends BaseListView<SalesOrdersList> {

	protected List<SalesOrdersList> salesList;

	private SalesDetailesView salesDetailView;

	private List<SalesOrdersList> listOfSalesOrder;

	private static String OPEN = Accounter.messages().open();
	private static String COMPLETED = Accounter.messages().completed();
	private static String CANCELLED = Accounter.messages().cancelled();
	private List<String> listOfTypes;

	// private static String CANCELLED = "Cancelled";

	public SalesOrderListView() {
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
			return Accounter.messages().addNewSalesOrder();
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {
		return Accounter.messages().salesOrderList();
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
		Accounter.createHomeService().getSalesOrders(this);
	}

	@Override
	public void updateInGrid(SalesOrdersList objectTobeModified) {
		// NOTHING TO DO.
	}

	@Override
	protected void onAttach() {
		salesDetailView = new SalesDetailesView();
		// salesDetailView.setPreferences(getPreferences());
		salesDetailView.init();
		gridLayout.add(salesDetailView);
		super.onAttach();
	}

	@Override
	protected void onDetach() {
		gridLayout.remove(salesDetailView);
		super.onDetach();
	}

	public void onClick(SalesOrdersList obj) {
		AccounterAsyncCallback<IAccounterCore> callbackforsalesOrder = new AccounterAsyncCallback<IAccounterCore>() {

			@Override
			public void onResultSuccess(IAccounterCore result) {
				if (result != null)
					salesDetailView.setObjValues((ClientSalesOrder) result);
			}

			@Override
			public void onException(AccounterException caught) {

			}
		};
		rpcGetService.getObjectById(AccounterCoreType.SALESORDER,
				obj.getTransactionId(), callbackforsalesOrder);

	}

	@Override
	public void onSuccess(ArrayList<SalesOrdersList> result) {
		super.onSuccess(result);
		listOfSalesOrder = result;
		filterList(viewSelect.getValue().toString());
		grid.setViewType(viewSelect.getValue().toString());
	}

	protected SelectCombo getSelectItem() {
		viewSelect = new SelectCombo(Accounter.messages().currentView());
		listOfTypes = new ArrayList<String>();
		listOfTypes.add(OPEN);
		listOfTypes.add(COMPLETED);
		listOfTypes.add(CANCELLED);
		viewSelect.initCombo(listOfTypes);
		viewSelect.setComboItem(OPEN);
		// if (UIUtils.isMSIEBrowser())
		// viewSelect.setWidth("120px");

		viewSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (viewSelect.getSelectedValue() != null) {
							grid.setViewType(viewSelect.getSelectedValue());
							filterList(viewSelect.getSelectedValue());
							salesDetailView.refreshView();
						}

					}
				});

		return viewSelect;
	}

	private void filterList(String text) {
		grid.removeAllRecords();
		if (listOfSalesOrder != null) {
			for (SalesOrdersList salesOrder : listOfSalesOrder) {
				if (text.equals(OPEN)) {
					if (salesOrder.getStatus() == ClientTransaction.STATUS_OPEN
							|| salesOrder.getStatus() == ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED)
						grid.addData(salesOrder);
					if (grid.getRecords().isEmpty()) {
						salesDetailView.itemsGrid.clear();
						salesDetailView.itemsGrid.addEmptyMessage(messages
								.noRecordsToShow());
					}
					continue;
				}
				if (text.equals(COMPLETED)) {
					if (salesOrder.getStatus() == ClientTransaction.STATUS_COMPLETED)
						grid.addData(salesOrder);
					if (grid.getRecords().isEmpty()) {
						salesDetailView.itemsGrid.clear();
						salesDetailView.itemsGrid.addEmptyMessage(messages
								.noRecordsToShow());
					}
					continue;
				}
				if (text.equals(CANCELLED)) {
					if (salesOrder.getStatus() == ClientTransaction.STATUS_CANCELLED)
						grid.addData(salesOrder);
					if (grid.getRecords().isEmpty()) {
						salesDetailView.itemsGrid.clear();
						salesDetailView.itemsGrid.addEmptyMessage(messages
								.noRecordsToShow());
					}
					continue;
				}

			}
		}
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
		if (salesDetailView.itemsGrid.getRecords().isEmpty()) {
			salesDetailView.itemsGrid.addEmptyMessage(messages
					.noRecordsToShow());
		}
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

	@Override
	protected void onLoad() {
		if (grid.getSelection() != null) {
			AccounterAsyncCallback<IAccounterCore> callbackforsalesOrder = new AccounterAsyncCallback<IAccounterCore>() {

				@Override
				public void onResultSuccess(IAccounterCore result) {
					if (result != null)
						salesDetailView.setObjValues((ClientSalesOrder) result);
				}

				@Override
				public void onException(AccounterException caught) {
					// TODO Auto-generated method stub

				}
			};
			rpcGetService.getObjectById(AccounterCoreType.SALESORDER,
					((SalesOrdersList) grid.getSelection()).getTransactionId(),
					callbackforsalesOrder);
		}
		super.onLoad();
	}

	@Override
	protected String getViewTitle() {
		return Accounter.messages().salesOrders();
	}

}
