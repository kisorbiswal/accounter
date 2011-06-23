package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author Uday Kumar
 * 
 */
public class ExpenseClaimView extends BaseView<BillsList> {

	ExpenseClaimGrid grid;
	List<String> expenseLists = new ArrayList<String>();
	public boolean isProcessingAdded;

	public ExpenseClaimView() {
		init();
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {

		VerticalPanel panel = new VerticalPanel();
		HTML addNew = new HTML("<a>Add New Employee Expense</a>");
		addNew.setStyleName("add-new-expense");
		addNew.getElement().getStyle().setMarginBottom(10, Unit.PX);
		addNew.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				VendorsActionFactory.EmployeeExpenseAction().run(null, false);
			}
		});
		initGrid();

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setStyleName("button-expense");
		AccounterButton submitApproval = new AccounterButton(
				"Submit For Approval");
		submitApproval.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isProcessingAdded = false;
				setAction(VendorsActionFactory.getExpenseClaimsAction());
				updateSelectedRecords(ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_SUBMITED_FOR_APPROVAL);
			}
		});

		AccounterButton deleteButton = new AccounterButton("Delete");
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isProcessingAdded = false;
				setAction(VendorsActionFactory.getExpenseClaimsAction());
				updateSelectedRecords(ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_DELETE);
			}
		});
		buttonPanel.add(submitApproval);
		buttonPanel.add(deleteButton);
		panel.add(addNew);
		buttonLayout.setVisible(false);
		panel.add(grid);
		panel.add(buttonPanel);
		panel.setCellHorizontalAlignment(buttonPanel, ALIGN_RIGHT);
		mainPanel.add(panel);
		panel.setWidth("100%");
		submitApproval.enabledButton();
//		submitApproval.enabledButton(AccounterButton.SUBMIT_BUTTON, "approve-image",
//				"ibutton1");

		deleteButton.enabledButton();
		mainPanel.removeStyleName("main-class-pannel");
		buttonLayout.getElement().getParentElement().removeClassName(
				"bottom-view");
		bottomShadow.getElement().getParentElement().removeClassName(
				"bottom-shadow");

	}

	private void initGrid() {
		grid = new ExpenseClaimGrid(true);
		grid.isEnable = false;
		grid.init();
		grid.setView(this);
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
							updateTransactionItems(result);
							setAction(VendorsActionFactory
									.getExpenseClaimsAction());
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
		String userName = null;
		if (!FinanceApplication.getUser().isAdminUser()) {
			userName = FinanceApplication.getUser().getName();
		} else {
			userName = null;
		}
		FinanceApplication.createHomeService().getEmployeeExpensesByStatus(
				userName, ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_SAVE,
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
		// action = VendorsActionFactory.getExpenseClaimListAction();
		super.setAction(action);
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
