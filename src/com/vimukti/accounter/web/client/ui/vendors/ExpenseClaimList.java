package com.vimukti.accounter.web.client.ui.vendors;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author Uday Kumar
 * 
 */
public class ExpenseClaimList extends BaseView {

	ExpenseClaimGrid grid;
	public boolean isProcessingAdded;

	public ExpenseClaimList() {
	}

	@Override
	public void init(ViewManager manager) {
		super.init(manager);
		createControls();
	}

	private void createControls() {

		VerticalPanel panel = new VerticalPanel();
		HTML addNew = new HTML(Accounter.constants().addNewEmployeeExpense());
		addNew.setStyleName("add-new-expense");
		addNew.getElement().getStyle().setMarginBottom(10, Unit.PX);
		addNew.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				HistoryTokenUtils.setPresentToken(
						ActionFactory.EmployeeExpenseAction(), null);
				ActionFactory.EmployeeExpenseAction().run(null, false);
			}
		});
		initGrid();

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setStyleName("button-expense");
		AccounterButton submitApproval = new AccounterButton(Accounter
				.constants().submitForApproval());
		submitApproval.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				isProcessingAdded = false;
				setAction(ActionFactory.getExpenseClaimsAction(0));

				List<BillsList> records = grid.getSelectedRecords();
				if (records.size() > 0) {
					updateSelectedRecords(
							records,
							ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_SUBMITED_FOR_APPROVAL);
				} else {
					Accounter.showInformation(Accounter.constants()
							.noRecordsToShow());
				}

			}
		});

		AccounterButton deleteButton = new AccounterButton(Accounter
				.constants().delete());
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isProcessingAdded = false;
				setAction(ActionFactory.getExpenseClaimsAction(0));
				List<BillsList> records = grid.getSelectedRecords();
				if (records.size() > 0) {
					updateSelectedRecords(records,
							ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_DELETE);
				} else {
					Accounter.showInformation(Accounter.constants()
							.noRecordsToShow());
				}
			}
		});
		buttonPanel.add(submitApproval);
		buttonPanel.add(deleteButton);
		panel.add(addNew);
		panel.add(grid);
		panel.add(buttonPanel);
		panel.setCellHorizontalAlignment(buttonPanel, ALIGN_RIGHT);
		panel.setWidth("100%");
		this.add(panel);
		submitApproval.setWidth("160px");
		submitApproval.enabledButton(AccounterButton.SUBMIT_BUTTON,
				"submit-approve-image", "ibutton1");
		deleteButton.setWidth("90px");
		deleteButton.enabledButton(AccounterButton.DELETE_BUTTON,
				"close-image", "ibutton1");
		this.removeStyleName("main-class-pannel");
	}

	private void initGrid() {
		grid = new ExpenseClaimGrid(true);
		grid.isEnable = false;
		grid.init();
		grid.setView(this);
		grid.setSize("100%", "100%");
	}

	protected void updateSelectedRecords(List<BillsList> records,
			final int expenceStatus) {
		for (BillsList record : records) {
			Accounter.createGETService().getObjectById(
					AccounterCoreType.CASHPURCHASE, record.getTransactionId(),

					new AccounterAsyncCallback<ClientCashPurchase>() {

						@Override
						public void onException(AccounterException caught) {

						}

						@Override
						public void onSuccess(ClientCashPurchase result) {
							result.setExpenseStatus(expenceStatus);
							updateTransactionItems(result);
							setAction(ActionFactory.getExpenseClaimsAction(0));
							saveOrUpdate(result);
						}
					});
		}

	}

	void updateTransactionItems(ClientCashPurchase result) {
		for (ClientTransactionItem item : result.getTransactionItems()) {
			item.setID(0);
		}
	}

	@Override
	protected void initRPCService() {
		super.initRPCService();
		String userName = null;
		if (!Accounter.getUser().isAdminUser()) {
			userName = Accounter.getUser().getName();
		} else {
			userName = null;
		}
		Accounter.createHomeService().getEmployeeExpensesByStatus(userName,
				ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_SAVE,
				new AccounterAsyncCallback<List<BillsList>>() {

					@Override
					public void onSuccess(List<BillsList> result) {
						if (result.size() > 0) {
							for (BillsList list : result)
								grid.addData(list);
						} else {
							grid.addEmptyMessage(Accounter.constants()
									.noRecordsToShow());
						}

					}

					@Override
					public void onException(AccounterException caught) {

					}
				});
	}

	@Override
	public void setAction(Action action) {
		// action = ActionFactory.getExpenseClaimListAction();
		super.setAction(action);
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public List<DynamicForm> getForms() {
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
		// NOTHING TO DO
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

	@Override
	protected String getViewTitle() {
		return Accounter.constants().expenseClaims();
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
	}
}
