package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.BaseView;
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
		Button approve = new Button("Approve");
		approve.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isProcessingAdded = false;
				updateSelectedRecords(ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED);
			}
		});
		Button decline = new Button("Decline");
		decline.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isProcessingAdded = false;
				updateSelectedRecords(ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_DECLINED);
			}
		});
		Button delete = new Button("Delete");
		delete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isProcessingAdded = false;
				updateSelectedRecords(ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_DELETE);
			}
		});
		buttonPanel.add(approve);
		buttonPanel.add(decline);
		buttonPanel.add(delete);
		approve.getElement().getStyle().setMarginLeft(25, Unit.PX);
		approve.getElement().getParentElement().setClassName("ibutton1");
		ThemesUtil.addDivToButton(approve, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "ibutton-right-image");

		decline.getElement().getParentElement().setClassName("ibutton1");
		ThemesUtil.addDivToButton(decline, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "ibutton-right-image");

		delete.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(delete, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "ibutton-right-image");
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
							alterObject(result);
						}
					});
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
