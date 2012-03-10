package com.vimukti.accounter.web.client.ui.vendors;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.RecordDoubleClickHandler;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

public class VendorPurchaseListDialog extends BaseDialog {

	private ItemReceiptView itemReceiptView;
	private List<PurchaseOrdersList> purchaseOrderList;

	private DialogGrid grid;

	public VendorPurchaseListDialog(ItemReceiptView view,
			List<PurchaseOrdersList> purchaseOrderList) {
		itemReceiptView = view;
		this.purchaseOrderList = purchaseOrderList;
		setText(messages.purchaseOrderList());
		createControls();
		setPurchaseOrderList(purchaseOrderList);
		setWidth("600px");
		show();
		center();
	}

	private void createControls() {

		StyledPanel mainLayout = new StyledPanel("mainLayout");
		Label infoLabel = new Label(messages.selectPurchaseOrder()
				+ messages.selectDocument());

		mainLayout.add(infoLabel);

		grid = new DialogGrid(false);
		grid.addColumns(messages.date(), messages.no(), messages.type(),
				messages.payeeName(Global.get().Vendor()), messages.total());
		grid.setView(this);
		grid.init();
		grid.setColumnTypes(ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT);
		grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler<PurchaseOrdersList>() {

			@Override
			public void OnCellDoubleClick(PurchaseOrdersList record, int column) {

				if (record != null)
					getPurchaseOrder(record);

			}

		});

		// getGridData();
		setPurchaseOrderList(purchaseOrderList);

		mainLayout.add(grid);

		StyledPanel helpButtonLayout = new StyledPanel("helpButtonLayout");

		Button helpButton = new Button(messages.help());
		helpButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Accounter.showError(messages.sorryNoHelp());

			}

		});
		helpButtonLayout.add(helpButton);

		StyledPanel okButtonLayout = new StyledPanel("okButtonLayout");
		Button okButton = new Button(messages.ok());
//		okButton.setWidth("100px");
		okButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				PurchaseOrdersList record = (PurchaseOrdersList) grid
						.getSelection();
				if (record != null)
					getPurchaseOrder(record);

			}

		});
		okButtonLayout.add(okButton);
		Button cancelButton = new Button(messages.cancel());
//		cancelButton.setWidth("100px");
		cancelButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				removeFromParent();
			}

		});
		okButtonLayout.add(cancelButton);

		StyledPanel buttonLayout = new StyledPanel("buttonLayout");
		buttonLayout.add(okButtonLayout);

		mainLayout.add(buttonLayout);
		mainLayout.setSize("100%", "100%");

		add(mainLayout);
	}

	public void setPurchaseOrderList(List<PurchaseOrdersList> result) {
		if (result == null)
			return;
		this.purchaseOrderList = result;
		grid.removeAllRecords();
		for (PurchaseOrdersList rec : purchaseOrderList) {
			grid.addData(rec);
		}
	}

	// protected void setPurchaseOrder(PurchaseOrdersList record) {
	// String purchaseOrderId = record.getTransactionId();
	// getPurchaseOrder(purchaseOrderId);
	//
	// }

	private void getPurchaseOrder(PurchaseOrdersList record) {

		AccounterAsyncCallback<ClientPurchaseOrder> callback = new AccounterAsyncCallback<ClientPurchaseOrder>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(messages.failedToGetPurchaseOrder());

			}

			@Override
			public void onResultSuccess(ClientPurchaseOrder result) {
				if (itemReceiptView != null && result != null)
					itemReceiptView.selectedPurchaseOrder(result);
				removeFromParent();

			}
		};
		rpcGetService.getObjectById(AccounterCoreType.PURCHASEORDER,
				record.getTransactionId(), callback);

	}

	public Object getGridColumnValue(IsSerializable obj, int index) {
		PurchaseOrdersList purchaseOrder = (PurchaseOrdersList) obj;
		if (purchaseOrder != null) {
			switch (index) {
			case 0:
				return DateUtills.getDateAsString(purchaseOrder.getDate());
			case 1:
				return purchaseOrder.getNumber();
			case 2:
				return messages.purchaseOrder();// purchaseOrder.getType();
			case 3:
				// return
				// company.getVendor(purchaseOrder.getVendorName()).getName();
				return purchaseOrder.getVendorName();
			case 4:
				return amountAsString(purchaseOrder.getPurchasePrice());
			}
		}
		return null;

	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	// protected String getViewTitle() {
	// return FinanceApplication.constants().purchaseOrderList();
	// }

}
