package com.vimukti.accounter.web.client.ui.vendors;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.externalization.FinanceConstants;
import com.vimukti.accounter.web.client.ui.AbstractBaseDialog;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.RecordDoubleClickHandler;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

@SuppressWarnings("unchecked")
public class VendorPurchaseListDialog extends AbstractBaseDialog {

	private ItemReceiptView itemReceiptView;
	private List<PurchaseOrdersList> purchaseOrderList;
	private VendorsMessages vendorConstants = GWT.create(VendorsMessages.class);
	private FinanceConstants financeConstants = GWT
			.create(FinanceConstants.class);
	@SuppressWarnings("unused")
	private PurchaseOrdersList selectedPurchaseOrder;
	private DialogGrid grid;

	public VendorPurchaseListDialog(ItemReceiptView view,
			List<PurchaseOrdersList> purchaseOrderList) {
		super(view);
		itemReceiptView = view;
		this.purchaseOrderList = purchaseOrderList;
		setText(Accounter.getVendorsMessages().purchaseOrderList());
		createControls();
		setPurchaseOrderList(purchaseOrderList);
		setWidth("600");
		show();
		center();
	}

	private void createControls() {

		VerticalPanel mainLayout = new VerticalPanel();
		mainLayout.setSize("100%", "100%");
		mainLayout.setSpacing(3);
		Label infoLabel = new Label(Accounter.getVendorsMessages()
				.selectPurchaseOrder()
				+ Accounter.getVendorsMessages().selectDocument());

		mainLayout.add(infoLabel);

		grid = new DialogGrid(false);
		grid.addColumns(vendorConstants.Date(), vendorConstants.no(),
				vendorConstants.type(), UIUtils.getVendorString(
						Accounter.getVendorsMessages().supplierName(),
						Accounter.getVendorsMessages().vendorName()),
				vendorConstants.total());
		grid.setView(this);
		grid.init();
		grid.setColumnTypes(ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT);
		grid
				.addRecordDoubleClickHandler(new RecordDoubleClickHandler<PurchaseOrdersList>() {

					@Override
					public void OnCellDoubleClick(PurchaseOrdersList record,
							int column) {

						if (record != null)
							getPurchaseOrder(record);

					}

				});

		// getGridData();
		setPurchaseOrderList(purchaseOrderList);

		mainLayout.add(grid);

		HorizontalPanel helpButtonLayout = new HorizontalPanel();

		AccounterButton helpButton = new AccounterButton(financeConstants
				.help());
		helpButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Accounter.showError(Accounter.getVendorsMessages()
						.sorryNoHelp());

			}

		});
		helpButtonLayout.add(helpButton);
		helpButton.enabledButton();

		HorizontalPanel okButtonLayout = new HorizontalPanel();
		okButtonLayout.setSpacing(3);
		AccounterButton okButton = new AccounterButton(financeConstants.ok());
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
		okButton.enabledButton();
		AccounterButton cancelButton = new AccounterButton(financeConstants
				.cancel());
		cancelButton.setWidth("100px");
		cancelButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				removeFromParent();
			}

		});
		okButtonLayout.add(cancelButton);

		cancelButton.enabledButton();

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

		AsyncCallback<ClientPurchaseOrder> callback = new AsyncCallback<ClientPurchaseOrder>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter
							.showMessage("Your session expired, Please login again to continue");
				} else {
					Accounter.showError(Accounter.getVendorsMessages()
							.failedToGetPurchaseOrder());
				}
				return;

			}

			@Override
			public void onSuccess(ClientPurchaseOrder result) {
				if (itemReceiptView != null && result != null)
					itemReceiptView.selectedPurchaseOrder(result);
				removeFromParent();

			}
		};
		rpcGetService.getObjectById(AccounterCoreType.PURCHASEORDER, record
				.getTransactionId(), callback);

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
				return Accounter.getVendorsMessages().purchaseOrder();// purchaseOrder.getType();
			case 3:
				// return
				// company.getVendor(purchaseOrder.getVendorName()).getName();
				return purchaseOrder.getVendorName();
			case 4:
				return DataUtils.getAmountAsString(purchaseOrder
						.getPurchasePrice());
			}
		}
		return null;

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		

	}

	// protected String getViewTitle() {
	// return FinanceApplication.getVendorsMessages().purchaseOrderList();
	// }

}
