package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

@SuppressWarnings("unchecked")
public class ChangeFiscalYearStartDateDialog extends BaseDialog {

	private HTML enterStartDateLabel;
	private DateField startDateItem;
	private DynamicForm dynamicForm;
	private VerticalPanel mainVlayout;
	private FiscalYearListGrid listofperiods;

	public ChangeFiscalYearStartDateDialog(String title, String desc,
			FiscalYearListGrid listOfperiods) {
		super(title, desc);
		this.listofperiods = listOfperiods;
		createControls();
		initData();
		center();

	}

	private void initData() {

		/*
		 * ClientFiscalYear fiscalYear = listofperiods.getSelection(); if
		 * (fiscalYear != null) {
		 * startDateItem.setDate(fiscalYear.getStartDate()); } else { Date
		 * startDate = null; for (ClientFiscalYear clientFiscalYear :
		 * listofperiods.getRecords()) { if (startDate == null) { startDate =
		 * clientFiscalYear.getStartDate(); } if
		 * (startDate.before(clientFiscalYear.getEndDate())) { startDate =
		 * clientFiscalYear.getEndDate(); } } startDateItem.setDate(startDate);
		 * }
		 */
		startDateItem.setDatethanFireEvent(new ClientFinanceDate(
				FinanceApplication.getCompany().getPreferences()
						.getStartOfFiscalYear()));
	}

	private void createControls() {
		enterStartDateLabel = new HTML();
		enterStartDateLabel.setHTML(FinanceApplication.getCompanyMessages()
				.pleaseEnterNewStartDate());
		startDateItem = new DateField(FinanceApplication.getCompanyMessages()
				.startDate());
		startDateItem.setHelpInformation(true);

		long startdate = FinanceApplication.getCompany().getPreferences()
				.getStartOfFiscalYear();
		startDateItem.setEnteredDate(new ClientFinanceDate(startdate));
		// startDateItem.setTitle("Start date");
		dynamicForm = new DynamicForm();
		dynamicForm.setFields(startDateItem);
		mainVlayout = new VerticalPanel();
		mainVlayout.add(enterStartDateLabel);
		mainVlayout.add(dynamicForm);
		setBodyLayout(mainVlayout);
		addInputDialogHandler(new InputDialogHandler() {

			@SuppressWarnings("unused")
			private ArrayList<ClientFiscalYear> listofNewFiscalYears = new ArrayList<ClientFiscalYear>();

			@Override
			public boolean onOkClick() {
				ClientFiscalYear firstFiscalYear = listofperiods
						.getRecordByIndex(0);
				ClientFiscalYear latestFiscalYear = listofperiods
						.getRecordByIndex(listofperiods.getRowCount() - 1);
				ClientFinanceDate changedFiscalStartDate = startDateItem
						.getDate();
				if (changedFiscalStartDate.before(firstFiscalYear
						.getStartDate())
						&& firstFiscalYear.getStatus() == ClientFiscalYear.STATUS_CLOSE) {
					Accounter
							.showError("New fiscal year start date must begin later than the end date of the most recent, closed fiscal year");
					return false;
				}
				// if (changedFiscalStartDate.before(firstFiscalYear
				// .getStartDate())
				// || changedFiscalStartDate.after(latestFiscalYear
				// .getStartDate())) {
				// createFiscalYear(changedFiscalStartDate);
				// return true;
				// } else
				// Accounter
				// .showError("Fiscal year for this year already created");
				// return false;

				createFiscalYear(changedFiscalStartDate);
				return true;
			}

			@Override
			public void onCancelClick() {
				hide();
			}
		});
	}

	protected void createFiscalYear(ClientFinanceDate changedFiscalStartDate) {
		final long convertedStartDate = changedFiscalStartDate.getTime();
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				Accounter.showInformation("Fiscalyears Creation Failed");
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					ClientCompanyPreferences preferences = FinanceApplication
							.getCompany().getPreferences();
					preferences.setStartOfFiscalYear(convertedStartDate);
					// preferences.setPreventPostingBeforeDate(convertedStartDate);
					addFiscalYearsToList();
				} else {
					onFailure(new Exception());
				}
			}
		};
		rpcUtilService.changeFiscalYearsStartDateTo(convertedStartDate,
				callback);
	}

	@SuppressWarnings("unused")
	private List<ClientFiscalYear> getModifiedFiscalYears() {
		// AccounterValidator.createNecessaryFiscalYears(fiscalYear1, asOfDate,
		// view)
		return null;
	}

	@SuppressWarnings( { "deprecation", "unused" })
	private void createNessasaryFiscalYears() {
		ArrayList<ClientFiscalYear> listofNewFiscalYears = new ArrayList<ClientFiscalYear>();
		ClientFinanceDate changedStartDate = startDateItem.getDate();
		ClientFinanceDate firstStartDate = null;
		ClientFinanceDate latestEndDate = null;
		// ClientFiscalYear firstFiscalYear=null;
		// ClientFiscalYear latestFiscalYear=null;
		for (ClientFiscalYear clientFiscalYear : listofperiods.getRecords()) {
			if (firstStartDate == null && latestEndDate == null) {
				firstStartDate = clientFiscalYear.getStartDate();
				latestEndDate = clientFiscalYear.getEndDate();
			}
			if (firstStartDate.after(clientFiscalYear.getStartDate())) {
				firstStartDate = clientFiscalYear.getStartDate();
			}
			if (latestEndDate.before(clientFiscalYear.getEndDate())) {
				latestEndDate = clientFiscalYear.getEndDate();
			}

		}
		if (changedStartDate.before(firstStartDate)) {
			while (changedStartDate.before(firstStartDate)) {
				ClientFinanceDate tempStartDate = changedStartDate;
				ClientFinanceDate tempEndDate = new ClientFinanceDate(
						tempStartDate.getTime());
				tempEndDate.setYear(tempEndDate.getYear() + 1);
				tempEndDate.setDate(tempEndDate.getDate() - 1);
				if (tempEndDate.after(firstStartDate)) {
					tempEndDate = new ClientFinanceDate(firstStartDate
							.getTime());
				}
				ClientFiscalYear newFiscalYear = new ClientFiscalYear();
				newFiscalYear.setStartDate(tempStartDate.getTime());
				newFiscalYear.setEndDate(tempEndDate.getTime());
				newFiscalYear.setStatus(ClientFiscalYear.STATUS_OPEN);
				listofNewFiscalYears.add(newFiscalYear);
				changedStartDate = tempEndDate;
				changedStartDate.setDate(changedStartDate.getDate() + 1);

			}
		} else {
			while (changedStartDate.after(latestEndDate)) {
				ClientFinanceDate tempStartDate = latestEndDate;
				ClientFinanceDate tempEndDate = new ClientFinanceDate(
						tempStartDate.getTime());
				tempEndDate.setYear(tempEndDate.getYear() + 1);
				tempEndDate.setDate(tempEndDate.getDate() - 1);
				if (tempEndDate.after(changedStartDate)) {
					tempEndDate = new ClientFinanceDate(changedStartDate
							.getTime());
				}
				ClientFiscalYear newFiscalYear = new ClientFiscalYear();
				newFiscalYear.setStartDate(tempStartDate.getTime());
				newFiscalYear.setEndDate(tempEndDate.getTime());
				newFiscalYear.setStatus(ClientFiscalYear.STATUS_OPEN);
				listofNewFiscalYears.add(newFiscalYear);
				changedStartDate = tempEndDate;
				changedStartDate.setDate(changedStartDate.getDate() + 1);
			}
		}

		for (final ClientFiscalYear latestFiscalYear : listofNewFiscalYears) {
			ViewManager.getInstance().createObject(latestFiscalYear, this);
		}
		// listofperiods.sortList();
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {

		listofperiods.addData((ClientFiscalYear) object);
		// listofperiods.sortList();

		listofperiods.removeAllRecords();

		for (ClientFiscalYear clientFiscalYear : FinanceApplication
				.getCompany().getFiscalYears()) {
			listofperiods.addData(clientFiscalYear);
		}
	}

	@Override
	public void saveFailed(Throwable exception) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	public void addFiscalYearsToList() {
		listofperiods.removeAllRecords();

		for (ClientFiscalYear clientFiscalYear : FinanceApplication
				.getCompany().getFiscalYears()) {
			listofperiods.addData(clientFiscalYear);
		}
	}

}
