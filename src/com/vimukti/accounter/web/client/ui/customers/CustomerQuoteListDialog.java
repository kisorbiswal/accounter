package com.vimukti.accounter.web.client.ui.customers;

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
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.externalization.FinanceConstants;
import com.vimukti.accounter.web.client.ui.AbstractBaseDialog;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.RecordDoubleClickHandler;

@SuppressWarnings("unchecked")
public class CustomerQuoteListDialog extends AbstractBaseDialog {
	public DialogGrid grid;
	private InvoiceView invoiceView;
	private List<EstimatesAndSalesOrdersList> estimatesAndSalesOrder;
	private CustomersMessages customerConstants = GWT
			.create(CustomersMessages.class);
	private FinanceConstants financeConstants = GWT
			.create(FinanceConstants.class);

	public ClientCustomer preCustomer;

	public CustomerQuoteListDialog(InvoiceView parentView,
			List<EstimatesAndSalesOrdersList> estimatesAndSalesOrder) {
		super(parentView);
		invoiceView = parentView;
		this.estimatesAndSalesOrder = estimatesAndSalesOrder;
		setText(FinanceApplication.getCustomersMessages()
				.quoteAndSalesOrderList());
		createControl();
		setWidth("600");
		show();
		center();

	}

	private void createControl() {

		VerticalPanel mainLayout = new VerticalPanel();
		mainLayout.setSize("100%", "100%");
		mainLayout.setSpacing(3);
		Label infoLabel = new Label(FinanceApplication.getCustomersMessages()
				.selectQuoteOrSalesOrder());

		mainLayout.add(infoLabel);

		grid = new DialogGrid(false);
		grid.addColumns(customerConstants.date(), customerConstants.no(),
				customerConstants.type(), customerConstants.customeRName(),
				customerConstants.total());
		grid.setView(this);
		grid.setCellsWidth(70, 30, 80, -1, 60);
		grid.init();
		grid.setColumnTypes(ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT);
		grid
				.addRecordDoubleClickHandler(new RecordDoubleClickHandler<EstimatesAndSalesOrdersList>() {

					@Override
					public void OnCellDoubleClick(
							EstimatesAndSalesOrdersList core, int column) {
						setRecord(core);

					}
				});

		// getGridData();
		setQuoteList(estimatesAndSalesOrder);

		mainLayout.add(grid);

		HorizontalPanel helpButtonLayout = new HorizontalPanel();

		AccounterButton helpButton = new AccounterButton(financeConstants
				.help());
		helpButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Accounter.showError(FinanceApplication.getCustomersMessages()
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

				EstimatesAndSalesOrdersList record = (EstimatesAndSalesOrdersList) grid
						.getSelection();
				setRecord(record);

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

	protected void setRecord(EstimatesAndSalesOrdersList record) {

		if (record != null) {
			if (record.getType() == ClientTransaction.TYPE_ESTIMATE) {
				getEstimate(record);

			} else {
				getSalesOrder(record);
			}
		}

		removeFromParent();

	}

	private void getSalesOrder(EstimatesAndSalesOrdersList record) {
		AsyncCallback<ClientSalesOrder> callback = new AsyncCallback<ClientSalesOrder>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter
							.showMessage("Your session expired, Please login again to continue");
				} else {
					Accounter.showError(FinanceApplication
							.getCustomersMessages().errorLoadingSalesOrder());
				}

			}

			@Override
			public void onSuccess(ClientSalesOrder result) {
				if (invoiceView != null && result != null)
					invoiceView.selectedSalesOrder(result);
				removeFromParent();

			}

		};
		rpcGetService.getObjectById(AccounterCoreType.SALESORDER, record
				.getTransactionId(), callback);
	}

	private void getEstimate(EstimatesAndSalesOrdersList record) {
		AsyncCallback<ClientEstimate> callback = new AsyncCallback<ClientEstimate>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter
							.showMessage("Your session expired, Please login again to continue");
				} else {
					Accounter.showError(FinanceApplication
							.getCustomersMessages().errorLoadingQuote());
				}

			}

			@Override
			public void onSuccess(ClientEstimate result) {
				if (invoiceView != null && result != null)
					invoiceView.selectedQuote(result);
				removeFromParent();

			}

		};
		rpcGetService.getObjectById(AccounterCoreType.ESTIMATE, record
				.getTransactionId(), callback);

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
					return FinanceApplication.getCustomersMessages().quote();
				} else {
					return FinanceApplication.getCustomersMessages()
							.salesOrder();
				}
			case 3:
				return estimate.getCustomerName();
			case 4:
				return DataUtils.getAmountAsString(estimate.getTotal());
			}
		}
		return null;

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}
	// setTitle(customerConstants.createForm());
}
