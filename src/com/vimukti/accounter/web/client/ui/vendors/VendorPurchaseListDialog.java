package com.vimukti.accounter.web.client.ui.vendors;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.RecordDoubleClickHandler;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class VendorPurchaseListDialog extends BaseDialog {

	private ItemReceiptView itemReceiptView;
	private List<PurchaseOrdersList> purchaseOrderList;
	private AccounterConstants financeConstants = GWT
			.create(AccounterConstants.class);

	private DialogGrid grid;

	public VendorPurchaseListDialog(ItemReceiptView view,
			List<PurchaseOrdersList> purchaseOrderList) {
		itemReceiptView = view;
		this.purchaseOrderList = purchaseOrderList;
		setText(Accounter.constants().purchaseOrderList());
		createControls();
		setPurchaseOrderList(purchaseOrderList);
		setWidth("600px");
		show();
		center();
	}

	private void createControls() {

		VerticalPanel mainLayout = new VerticalPanel();
		mainLayout.setSize("100%", "100%");
		mainLayout.setSpacing(3);
		Label infoLabel = new Label(Accounter.constants().selectPurchaseOrder()
				+ Accounter.constants().selectDocument());

		mainLayout.add(infoLabel);

		grid = new DialogGrid(false);
		grid.addColumns(Accounter.constants().date(), Accounter.constants()
				.no(), Accounter.constants().type(), messages
				.vendorName(Global.get().Vendor()), Accounter.constants()
				.total());
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

		HorizontalPanel helpButtonLayout = new HorizontalPanel();

		Button helpButton = new Button(financeConstants.help());
		helpButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Accounter.showError(Accounter.constants().sorryNoHelp());

			}

		});
		helpButtonLayout.add(helpButton);

		HorizontalPanel okButtonLayout = new HorizontalPanel();
		okButtonLayout.setSpacing(3);
		Button okButton = new Button(financeConstants.ok());
		okButton.setWidth("100px");
		okButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				PurchaseOrdersList record = (PurchaseOrdersList) grid
						.getSelection();
				if (record != null)
					getPurchaseOrder(record);

			}

		});
		okButtonLayout.add(okButton);
		Button cancelButton = new Button(financeConstants.cancel());
		cancelButton.setWidth("100px");
		cancelButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				removeFromParent();
			}

		});
		okButtonLayout.add(cancelButton);

		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.setWidth("100%");
		// buttonLayout.add(helpButtonLayout);
		buttonLayout.add(okButtonLayout);
		buttonLayout.setCellHorizontalAlignment(okButtonLayout,
				HasHorizontalAlignment.ALIGN_RIGHT);

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
				Accounter.showError(Accounter.constants()
						.failedToGetPurchaseOrder());

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
				return UIUtils.dateFormat(purchaseOrder.getDate());
			case 1:
				return purchaseOrder.getNumber();
			case 2:
				return Accounter.constants().purchaseOrder();// purchaseOrder.getType();
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

	// protected String getViewTitle() {
	// return FinanceApplication.constants().purchaseOrderList();
	// }

}
