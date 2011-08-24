package com.vimukti.accounter.web.client.ui.customers;

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
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.RecordDoubleClickHandler;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class CustomerQuoteListDialog extends BaseDialog {
	public DialogGrid grid;
	private InvoiceView invoiceView;
	private List<EstimatesAndSalesOrdersList> estimatesAndSalesOrder;
	private AccounterConstants customerConstants = Accounter.constants();
	private AccounterConstants financeConstants = GWT
			.create(AccounterConstants.class);

	public ClientCustomer preCustomer;

	public CustomerQuoteListDialog(InvoiceView parentView,
			List<EstimatesAndSalesOrdersList> estimatesAndSalesOrder) {
		super(Accounter.constants().quotesList(), "");
		invoiceView = parentView;
		this.estimatesAndSalesOrder = estimatesAndSalesOrder;
		if (getPreferences().isSalesOrderEnabled()
				&& getPreferences().isDoyouwantEstimates()) {
			setText(Accounter.constants().quoteAndSalesOrderList());
		} else if (getPreferences().isDoyouwantEstimates()) {
			setText(Accounter.constants().quoteList());

		} else if (getPreferences().isSalesOrderEnabled()) {
			setText(Accounter.constants().salesOrderList());
		}
		createControl();
		setWidth("600px");
		show();
		center();

	}

	private void createControl() {

		VerticalPanel mainLayout = new VerticalPanel();
		mainLayout.setSize("100%", "100%");
		mainLayout.setSpacing(3);
		Label infoLabel = null;
		if (getPreferences().isSalesOrderEnabled()
				&& getPreferences().isDoyouwantEstimates()) {
			infoLabel = new Label(Accounter.constants()
					.selectQuoteOrSalesOrder());
		} else if (getPreferences().isDoyouwantEstimates()) {
			infoLabel = new Label(Accounter.constants().selectQuote());
		} else if (getPreferences().isSalesOrderEnabled()) {
			infoLabel = new Label(Accounter.constants().selectSalesOrder());
		}
		mainLayout.add(infoLabel);

		grid = new DialogGrid(false);
		grid.addColumns(customerConstants.date(), customerConstants.no(),
				customerConstants.type(),
				Accounter.messages().customerName(Global.get().Customer()),
				customerConstants.total(), customerConstants.remainingTotal());
		grid.setView(this);
		grid.setCellsWidth(70, 30, 80, -1, 60, 95);
		grid.init();
		grid.setColumnTypes(ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT);
		grid.addRecordDoubleClickHandler(new RecordDoubleClickHandler<EstimatesAndSalesOrdersList>() {

			@Override
			public void OnCellDoubleClick(EstimatesAndSalesOrdersList core,
					int column) {
				setRecord(core);

			}
		});

		// getGridData();
		setQuoteList(estimatesAndSalesOrder);

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

				EstimatesAndSalesOrdersList record = (EstimatesAndSalesOrdersList) grid
						.getSelection();
				setRecord(record);

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

		// mainLayout.add(buttonLayout);
		mainLayout.setSize("100%", "100%");

		// add(mainLayout);
		setBodyLayout(mainLayout);
	}

	protected void setRecord(EstimatesAndSalesOrdersList record) {

		if (record != null) {
			if (record.getType() == ClientTransaction.TYPE_ESTIMATE) {
				getEstimate(record);

			} else {
				getSalesOrder(record);
			}
		}

	}

	private void getSalesOrder(EstimatesAndSalesOrdersList record) {
		AccounterAsyncCallback<ClientSalesOrder> callback = new AccounterAsyncCallback<ClientSalesOrder>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(Accounter.constants()
						.errorLoadingSalesOrder());

			}

			@Override
			public void onResultSuccess(ClientSalesOrder result) {
				if (invoiceView != null && result != null)
					invoiceView.selectedSalesOrder(result);
				removeFromParent();

			}

		};
		rpcGetService.getObjectById(AccounterCoreType.SALESORDER,
				record.getTransactionId(), callback);
	}

	private void getEstimate(EstimatesAndSalesOrdersList record) {
		AccounterAsyncCallback<ClientEstimate> callback = new AccounterAsyncCallback<ClientEstimate>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(Accounter.constants().errorLoadingQuote());

			}

			@Override
			public void onResultSuccess(ClientEstimate result) {
				//super.onSuccess(result);
				if (invoiceView != null && result != null)
					invoiceView.selectedQuote(result);
				removeFromParent();

			}

		};
		rpcGetService.getObjectById(AccounterCoreType.ESTIMATE,
				record.getTransactionId(), callback);

	}

	public void setQuoteList(List<EstimatesAndSalesOrdersList> result) {
		if (result == null)
			return;
		this.estimatesAndSalesOrder = result;
		grid.removeAllRecords();
		for (EstimatesAndSalesOrdersList rec : estimatesAndSalesOrder) {
			grid.addData(rec);
		}
	}

	public Object getGridColumnValue(IsSerializable obj, int index) {
		EstimatesAndSalesOrdersList estimate = (EstimatesAndSalesOrdersList) obj;
		if (estimate != null) {
			switch (index) {
			case 0:
				return UIUtils.getDateByCompanyType(estimate.getDate());
			case 1:
				return estimate.getTransactionNumber();
			case 2:
				if (estimate.getType() == ClientTransaction.TYPE_ESTIMATE) {
					return Accounter.constants().quote();
				} else {
					return Accounter.constants().salesOrder();
				}
			case 3:
				return estimate.getCustomerName();
			case 4:
				return amountAsString(estimate.getTotal());
			case 5:
				return amountAsString(estimate.getRemainingTotal());
			}
		}
		return null;

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	// setTitle(customerConstants.createForm());

	@Override
	protected boolean onOK() {
		EstimatesAndSalesOrdersList record = (EstimatesAndSalesOrdersList) grid
				.getSelection();
		setRecord(record);
		return true;
	}
}
