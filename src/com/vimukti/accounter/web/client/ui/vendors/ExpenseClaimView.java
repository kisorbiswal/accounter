package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class ExpenseClaimView extends BaseView<BillsList> {

	ExpenseClaimGrid grid;
	List<String> expenseLists = new ArrayList<String>();

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {

		VerticalPanel panel = new VerticalPanel();
		HTML addNew = new HTML("<a>Add Employee Expense</a>");
		addNew.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				VendorsActionFactory.EmployeeExpenseAction().run(null, false);
			}
		});
		initGrid();

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setWidth("100%");
		buttonPanel.setStyleName("button-expense");
		buttonPanel.setHorizontalAlignment(ALIGN_RIGHT);
		Button submitApproval = new Button("Submit For Approval");

		Button deleteButton = new Button("Delete");
		buttonPanel.add(submitApproval);
		buttonPanel.add(deleteButton);
		panel.add(addNew);
		buttonLayout.setVisible(false);
		panel.add(grid);
		panel.add(buttonPanel);
		mainPanel.add(panel);
	}

	private void initGrid() {
		grid = new ExpenseClaimGrid(false);
		grid.isEnable = false;
		grid.init();
		grid.setView(this);
		grid.setSize("100%", "100%");
	}

	@Override
	protected void initRPCService() {
		super.initRPCService();
		FinanceApplication.createHomeService().getEmployeeExpensesByStatus(0,
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
