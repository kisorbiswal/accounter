package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddEditFiscalYearDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;

public class FiscalYearListDialog extends GroupDialog<ClientFiscalYear> {

	protected GroupDialogButtonsHandler groupDialogButtonHandler;
	protected List<ClientFiscalYear> savedFiscalYear;
	private AddEditFiscalYearDialog addEditFiscalYear;
	private Button closeButton, openButton, changeButton;

	public FiscalYearListDialog(String title, String descript) {
		super(title, descript);
		initialise();
		getListFiscalYear();
		center();
	}

	private void getListFiscalYear() {

		AccounterAsyncCallback<ArrayList<ClientFiscalYear>> callback = new AccounterAsyncCallback<ArrayList<ClientFiscalYear>>() {

			public void onException(AccounterException caught) {

			}

			public void onResultSuccess(ArrayList<ClientFiscalYear> result) {
				savedFiscalYear = result;
				fillFiscalYear(result);
			}

		};

		// FinanceApplication.createGETService().getCom
	}

	protected void fillFiscalYear(List<ClientFiscalYear> result) {

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
		// ListGridField period = new ListGridField("period",
		// Accounter.constants()
		// .period());
		// period.setWidth(90);
		// addField(period);
		// ListGridField status = new ListGridField("status",
		// Accounter.constants()
		// .status());
		// status.setWidth(130);
		// addField(status);
		getGrid().setType(AccounterCoreType.CREDIT_RATING);
		closeButton = new Button();
		openButton = new Button();
		changeButton = new Button();
		closeButton.setTitle(Accounter.constants().closeFiscalYear());
		addButton(closeButton);
		closeButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

			}
		});
		openButton.setTitle(Accounter.constants().openFiscalYear());
		addButton(openButton);
		openButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

			}
		});
		changeButton.setTitle(Accounter.constants().changeStartDate());
		addButton(changeButton);
		changeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				new ChangeStartDateDialog(Accounter.constants().title(),
						Accounter.constants().description()).show();
			}
		});
		// setHeight(250);

		groupDialogButtonHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {

			}

			public void onFirstButtonClick() {
				showAddEditFiscalYear(Accounter.constants().createFascalYear(),
						Accounter.constants().description(), null);
			}

			public void onSecondButtonClick() {
				showAddEditFiscalYear(Accounter.constants().editFiscalYear(),
						Accounter.constants().description(), null);
			}

			public void onThirdButtonClick() {

			}

		};
		addGroupButtonsHandler(groupDialogButtonHandler);
	}// initialise

	protected void showAddEditFiscalYear(String title, String Description,
			ClientFiscalYear fiscalYear) {
		addEditFiscalYear = new AddEditFiscalYearDialog(title, Description,
				fiscalYear);
		addEditFiscalYear.show();

	}

	@Override
	public Object getGridColumnValue(ClientFiscalYear obj, int index) {
		return null;
	}

	@Override
	public String[] setColumns() {
		return null;
	}

	@Override
	protected List<ClientFiscalYear> getRecords() {
		return null;
	}

	public void deleteCallBack() {
	}

	public void addCallBack() {
	}

	public void editCallBack() {
	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return true;
	}

}
