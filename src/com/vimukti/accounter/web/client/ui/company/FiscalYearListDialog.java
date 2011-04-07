package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.ui.AddEditFiscalYearDialog;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;

public class FiscalYearListDialog extends GroupDialog<ClientFiscalYear> {

	CompanyMessages companyConstants = GWT.create(CompanyMessages.class);
	protected GroupDialogButtonsHandler groupDialogButtonHandler;
	protected List<ClientFiscalYear> savedFiscalYear;
	private AddEditFiscalYearDialog addEditFiscalYear;
	private Button closeButton, openButton, changeButton;

	public FiscalYearListDialog(String title, String descript) {
		super(title, descript);
		// TODO Auto-generated constructor stub
		initialise();
		getListFiscalYear();
		center();
	}

	private void getListFiscalYear() {
		// TODO Auto-generated method stub

		@SuppressWarnings("unused")
		AsyncCallback<List<ClientFiscalYear>> callback = new AsyncCallback<List<ClientFiscalYear>>() {

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			public void onSuccess(List<ClientFiscalYear> result) {
				// TODO Auto-generated method stub
				savedFiscalYear = result;
				fillFiscalYear(result);
			}

		};

		// FinanceApplication.createGETService().getCom
	}

	protected void fillFiscalYear(List<ClientFiscalYear> result) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unused")
		String period, status;
		// if (result != null) {
		// // ListGridRecord[] records = new ListGridRecord[result.size()];
		// for (int i = 0; i < result.size(); i++) {
		// records[i] = new ListGridRecord();
		// period = result.get(i).getStartDate().toString();
		// period = period + result.get(i).getEndDate().toString();
		// records[i].setAttribute("period", period);
		// if (result.get(i).getStatus() == 1)
		// status = "Open";
		// else
		// status = "Close";
		// records[i].setAttribute("status", status);
		//
		// }
		// addRecords(records);
		//
		// } else
		// Accounter.showError("No Fiscal Years found.");

	}

	public void initialise() {
		// setHeight(450);
		// setWidth(450);
		// ListGridField period = new ListGridField("period", companyConstants
		// .period());
		// period.setWidth(90);
		// addField(period);
		// ListGridField status = new ListGridField("status", companyConstants
		// .status());
		// status.setWidth(130);
		// addField(status);
		getGrid().setType(AccounterCoreType.CREDIT_RATING);
		closeButton = new Button();
		openButton = new Button();
		changeButton = new Button();
		closeButton.setTitle(companyConstants.closeFiscalYear());
		addButton(closeButton);
		closeButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});
		openButton.setTitle(companyConstants.openFiscalYear());
		addButton(openButton);
		openButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

			}
		});
		changeButton.setTitle(companyConstants.changeStartDate());
		addButton(changeButton);
		changeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				new ChangeStartDateDialog(FinanceApplication
						.getCompanyMessages().title(), FinanceApplication
						.getCompanyMessages().description()).show();
			}
		});
		// setHeight(250);

		groupDialogButtonHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {
				// TODO Auto-generated method stub

			}

			public void onFirstButtonClick() {
				// TODO Auto-generated method stub
				showAddEditFiscalYear(FinanceApplication.getCompanyMessages()
						.createFascalYear(), FinanceApplication
						.getCompanyMessages().description(), null);
			}

			public void onSecondButtonClick() {
				// TODO Auto-generated method stub
				showAddEditFiscalYear(FinanceApplication.getCompanyMessages()
						.editFiscalYear(), FinanceApplication
						.getCompanyMessages().description(), null);
			}

			public void onThirdButtonClick() {
				// TODO Auto-generated method stub

			}

		};
		addGroupButtonsHandler(groupDialogButtonHandler);
	}// initialise

	protected void showAddEditFiscalYear(String title, String Description,
			ClientFiscalYear fiscalYear) {
		// TODO Auto-generated method stub
		addEditFiscalYear = new AddEditFiscalYearDialog(title, Description,
				fiscalYear);
		addEditFiscalYear.show();

	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] setColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<ClientFiscalYear> getRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteCallBack() {
	}

	public void addCallBack() {
	}

	public void editCallBack() {
	}

}
