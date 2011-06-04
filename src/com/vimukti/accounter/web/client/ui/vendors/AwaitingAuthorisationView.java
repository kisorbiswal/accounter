package com.vimukti.accounter.web.client.ui.vendors;

import java.util.List;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class AwaitingAuthorisationView extends BaseView<BillsList> {
	AwaitingAuthorisationgrid grid;
	public boolean isProcessingAdded;

	public AwaitingAuthorisationView() {
		init();
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		VerticalPanel panel = new VerticalPanel();
		panel.setSize("100%", "100%");

		initGrid();

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.getElement().getStyle().setMarginTop(15, Unit.PX);
		AccounterButton approve = new AccounterButton("Approve");
		approve.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MainFinanceWindow.getViewManager().restoreErrorBox();
				isProcessingAdded = false;
				setAction(VendorsActionFactory.getAwaitingAuthorisationAction());
				boolean isErrorOccured = checkPayFromAccount();
				if (!isErrorOccured)
					updateSelectedRecords(ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED);
				else
					MainFinanceWindow
							.getViewManager()
							.showError(
									"Please Select Pay From Account for the records showm in red.");
			}
		});
		AccounterButton decline = new AccounterButton("Decline");
		decline.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MainFinanceWindow.getViewManager().restoreErrorBox();
				isProcessingAdded = false;
				setAction(VendorsActionFactory.getAwaitingAuthorisationAction());
				updateSelectedRecords(ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_DECLINED);
			}
		});
		AccounterButton delete = new AccounterButton("Delete");
		delete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MainFinanceWindow.getViewManager().restoreErrorBox();
				isProcessingAdded = false;
				setAction(VendorsActionFactory.getAwaitingAuthorisationAction());
				updateSelectedRecords(ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_DELETE);
			}
		});
		buttonPanel.add(approve);
		buttonPanel.add(decline);
		buttonPanel.add(delete);
		approve.getElement().getStyle().setMarginLeft(25, Unit.PX);

		approve.enabledButton("ibutton1");
		decline.enabledButton("ibutton1");
		delete.enabledButton();

		buttonLayout.setVisible(false);
		panel.add(grid);
		panel.add(buttonPanel);
		panel.setCellHorizontalAlignment(buttonPanel, ALIGN_RIGHT);
		mainPanel.add(panel);
		mainPanel.removeStyleName("main-class-pannel");
		buttonLayout.getElement().getParentElement().removeClassName(
				"bottom-view");
		bottomShadow.getElement().getParentElement().removeClassName(
				"bottom-shadow");

	}

	protected boolean checkPayFromAccount() {
		boolean isErrorOccured = false;
		List<BillsList> selectedRecords = grid.getSelectedRecords();
		for (BillsList record : selectedRecords) {
			if (record.getPayFrom() == null) {
				isErrorOccured = true;
			}
		}
		return isErrorOccured;
	}

	private void initGrid() {
		grid = new AwaitingAuthorisationgrid(true);
		grid.init();
		grid.setSize("100%", "100%");
	}

	protected void updateSelectedRecords(final int expenceStatus) {
		List<BillsList> selectedRecords = grid.getSelectedRecords();

		for (BillsList record : selectedRecords) {
			FinanceApplication.createGETService().getObjectById(
					AccounterCoreType.CASHPURCHASE, record.getTransactionId(),

					new AsyncCallback<ClientCashPurchase>() {

						@Override
						public void onFailure(Throwable caught) {

						}

						@Override
						public void onSuccess(ClientCashPurchase result) {
							result.setExpenseStatus(expenceStatus);
							setAction(VendorsActionFactory
									.getExpenseClaimsAction());
							updateTransactionItems(result);
							alterObject(result);
						}
					});
		}
	}

	void updateTransactionItems(ClientCashPurchase result) {
		for (ClientTransactionItem item : result.getTransactionItems()) {
			item.setStringID("");
		}
	}

	@Override
	protected void initRPCService() {
		super.initRPCService();
		FinanceApplication
				.createHomeService()
				.getEmployeeExpensesByStatus(
						ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_SUBMITED_FOR_APPROVAL,
						new AsyncCallback<List<BillsList>>() {

							@Override
							public void onSuccess(List<BillsList> result) {
								for (BillsList list : result)
									grid.addData(list);
							}

							@Override
							public void onFailure(Throwable caught) {

							}
						});
	}

	@Override
	public void setAction(Action action) {
		super.setAction(action);
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		try {
			if (this.callback != null) {
				this.callback.onSuccess(object);
			}
			if (saveAndClose)
				MainFinanceWindow.getViewManager().closeView(this.getAction(),
						object);
			else {
				if (getAction() instanceof ExpenseClaimsAction)
					((ExpenseClaimsAction) getAction()).run(null, true, 2);
				else
					getAction().run(null, true);
			}
		} catch (Exception e) {
			Accounter.showInformation(((JavaScriptException) e)
					.getDescription());
		}

	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
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
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

}
