package com.vimukti.accounter.web.client.ui.vendors;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author Uday Kumar
 * 
 */
public class PreviousClaimsView extends BaseView<BillsList> {

	PreviousClaimGrid grid;
	public boolean isProcessingAdded;

	public PreviousClaimsView() {
		init();
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {

		VerticalPanel panel = new VerticalPanel();

		Label previous = new Label(Accounter.constants()
				.previousClaims());

		initGrid();

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setStyleName("button-expense");
		AccounterButton notShowInList = new AccounterButton(Accounter.constants().dontShowinList());
		notShowInList.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isProcessingAdded = false;
				setAction(ActionFactory.getPreviousClaimAction());
				updateSelectedRecords(ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_NOT_TO_SHOW);
			}
		});
		buttonPanel.add(notShowInList);
		buttonLayout.setVisible(false);

		panel.add(previous);
		panel.add(grid);
		panel.add(buttonPanel);
		panel.setWidth("100%");
		panel.setCellHorizontalAlignment(buttonPanel, ALIGN_RIGHT);
		mainPanel.add(panel);
		notShowInList.enabledButton("ibutton1");

	}

	private void initGrid() {

		grid = new PreviousClaimGrid(true);
		grid.isEnable = false;
		grid.init();
		grid.setView(this);
		grid.setHeight("600px");
		grid.setSize("100%", "100%");

	}

	protected void updateSelectedRecords(final int expenceStatus) {
		List<BillsList> selectedRecords = grid.getSelectedRecords();
		if (selectedRecords.size() > 0) {
			for (BillsList record : selectedRecords) {
				Accounter.createGETService().getObjectById(
						AccounterCoreType.CASHPURCHASE,
						record.getTransactionId(),

						new AccounterAsyncCallback<ClientCashPurchase>() {

							@Override
							public void onException(AccounterException caught) {

							}

							@Override
							public void onSuccess(ClientCashPurchase result) {
								result.setExpenseStatus(expenceStatus);
								updateTransactionItems(result);
								setAction(ActionFactory
										.getExpenseClaimsAction(1));
								alterObject(result);
							}
						});
			}
		} else {
			Accounter.showInformation(Accounter.constants().noRecordsSelected());
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
				ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED,
				new AccounterAsyncCallback<List<BillsList>>() {

					@Override
					public void onSuccess(List<BillsList> result) {
						for (BillsList list : result)
							grid.addData(list);

					}

					@Override
					public void onException(AccounterException caught) {

					}
				});

		Accounter.createHomeService().getEmployeeExpensesByStatus(userName,
				ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_DECLINED,
				new AccounterAsyncCallback<List<BillsList>>() {

					@Override
					public void onSuccess(List<BillsList> result) {
						for (BillsList list : result)
							grid.addData(list);
					}

					@Override
					public void onException(AccounterException caught) {

					}
				});

		Accounter
				.createHomeService()
				.getEmployeeExpensesByStatus(
						userName,
						ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_SUBMITED_FOR_APPROVAL,
						new AccounterAsyncCallback<List<BillsList>>() {

							@Override
							public void onSuccess(List<BillsList> result) {
								for (BillsList list : result)
									grid.addData(list);
							}

							@Override
							public void onException(AccounterException caught) {

							}
						});
	}

	@Override
	public List<DynamicForm> getForms() {
		// NOTHING TO DO.
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
		// NOTHING TO DO.
	}

	@Override
	public void deleteFailed(Throwable caught) {
		// Nothing to do
	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().previousClaims();
	}

}
