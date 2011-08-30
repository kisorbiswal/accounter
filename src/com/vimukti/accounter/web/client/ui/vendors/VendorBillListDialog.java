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
import com.vimukti.accounter.web.client.core.ClientItemReceipt;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.RecordDoubleClickHandler;

public class VendorBillListDialog extends BaseDialog {

	private VendorBillView view;
	// private CustomersMessages customerConstants = GWT
	// .create(CustomersMessages.class);
	private AccounterConstants financeConstants = GWT
			.create(AccounterConstants.class);
	private DialogGrid grid;
	private List<PurchaseOrdersAndItemReceiptsList> list;
	public ClientVendor preVendor;

	public VendorBillListDialog(VendorBillView view,
			List<PurchaseOrdersAndItemReceiptsList> list) {
		this.view = view;
		this.list = list;
		// setTitle("");
		setText(Accounter.constants().purchaseOrderList());
		initRPCService();
		createControls();
		setWidth("600px");
		setQuoteList(list);
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
				.no(), Accounter.constants().type(), messages.vendorName(Global
				.get().Vendor()), Accounter.constants().total(), Accounter
				.constants().remainingTotal());
		grid.setCellsWidth(60, 20, 90, -1, 60, 95);
		grid.setView(this);
		grid.init();

		grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler<PurchaseOrdersAndItemReceiptsList>() {

			@Override
			public void OnCellDoubleClick(
					PurchaseOrdersAndItemReceiptsList core, int column) {

				setRecord(core);
				// try {
				// ClientEstimate record = (ClientEstimate) core;
				//
				// String estimateId = record.getID();
				// selectedEstimate = getEstimate(estimateId);
				//
				// if (invoiceView != null && selectedEstimate != null)
				// invoiceView.selectedQuote(selectedEstimate);
				//
				// removeFromParent();
				//
				// } catch (Exception e) {
				// Accounter.showError("Error Loading Quote...");
				// }

			}
		});

		// getGridData();
		// setQuoteList(estimates);

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

				PurchaseOrdersAndItemReceiptsList record = (PurchaseOrdersAndItemReceiptsList) grid
						.getSelection();
				setRecord(record);

				// try {
				// ClientEstimate selectedEstimate = (ClientEstimate) grid
				// .getSelection();
				// if (invoiceView != null && selectedEstimate != null)
				// invoiceView.selectedQuote(selectedEstimate);
				// removeFromParent();
				//
				// } catch (Exception e) {
				// Accounter.showError("Error Loading Quote...");
				// }

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

	protected void setRecord(PurchaseOrdersAndItemReceiptsList record) {

		if (record != null) {
			if (record.getType() == ClientTransaction.TYPE_PURCHASE_ORDER) {
				getPurchaseOrder(record);

			} else {
				getItemReceipt(record);
			}
		}

		removeFromParent();

	}

	private void getItemReceipt(PurchaseOrdersAndItemReceiptsList record) {
		AccounterAsyncCallback<ClientItemReceipt> callback = new AccounterAsyncCallback<ClientItemReceipt>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(Accounter.constants()
						.errorLoadingItemReceipt());
			}

			@Override
			public void onResultSuccess(ClientItemReceipt result) {
				if (result != null)
					view.selectedItemReceipt(result);

			}

		};
		rpcGetService.getObjectById(AccounterCoreType.ITEMRECEIPT,
				record.getTransactionId(), callback);

	}

	private void getPurchaseOrder(PurchaseOrdersAndItemReceiptsList record) {
		AccounterAsyncCallback<ClientPurchaseOrder> callback = new AccounterAsyncCallback<ClientPurchaseOrder>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(Accounter.constants()
						.errorLoadingPurchaseOrder());
			}

			@Override
			public void onResultSuccess(ClientPurchaseOrder result) {
				if (result != null)
					view.selectedPurchaseOrder(result);

			}

		};
		rpcGetService.getObjectById(AccounterCoreType.PURCHASEORDER,
				record.getTransactionId(), callback);

	}

	public void setQuoteList(List<PurchaseOrdersAndItemReceiptsList> result) {
		if (result == null)
			return;
		this.list = result;
		grid.removeAllRecords();
		if (list.size() > 0) {
			for (PurchaseOrdersAndItemReceiptsList rec : list) {
				grid.addData(rec);
			}
		} else
			grid.addEmptyMessage(Accounter.constants().noRecordsToShow());
	}

	public Object getGridColumnValue(IsSerializable obj, int index) {
		PurchaseOrdersAndItemReceiptsList record = (PurchaseOrdersAndItemReceiptsList) obj;
		if (record != null) {
			switch (index) {
			case 0:
				return UIUtils.dateFormat(record.getDate());
			case 1:
				return record.getTransactionNumber();
			case 2:
				return Utility.getTransactionName(record.getType());
			case 3:
				return record.getVendorName();
			case 4:
				return amountAsString(record.getTotal());
			case 5:
				return amountAsString(record.getRemainingTotal());
			}
		}
		return null;

	}

	public void setFocus() {
		// cancelBtn.setFocus(true);
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public void processupdateView(IAccounterCore core, int command) {
	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return true;
	}
}