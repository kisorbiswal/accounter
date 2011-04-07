package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.core.Accounter.AccounterType;

@SuppressWarnings("unchecked")
public class ManageFiscalYearDialog extends BaseDialog {

	private HTML manageFiscalyearLabel;
	private HTML descriptionLabel;
	private FiscalYearListGrid listOfperiods;
	private VerticalPanel buttonVlayout;
	private Button newFiscalYearButton;
	private Button closeFiscalYearButton;
	private Button openFiscalYearButoon;
	private Button editFiscalYear;
	private Button deleteFiscalYear;
	private Button changeStartDate;
	private HorizontalPanel listHpanel;
	private VerticalPanel mainVlayout;
	@SuppressWarnings("unused")
	private HorizontalPanel buttonPanel;

	public ManageFiscalYearDialog(String title, String desc) {
		super(title, desc);
		createControls();
		setSize("550px", "250px");
		center();

	}

	public void createControls() {
		manageFiscalyearLabel = new HTML();
		manageFiscalyearLabel.setHTML(FinanceApplication.getCompanyMessages()
				.manageFiscalYear());
		descriptionLabel = new HTML();
		descriptionLabel.setHTML(FinanceApplication.getCompanyMessages()
				.toCloseBooksOrFascalYear());
		listOfperiods = new FiscalYearListGrid(this, false);
		listOfperiods.setTitle(FinanceApplication.getCompanyMessages()
				.listOfPeriods());
		listOfperiods.setWidth("350px");
		listOfperiods.setHeight("250px");
		listHpanel = new HorizontalPanel();
		listHpanel.setSpacing(5);
		listHpanel.setWidth("100%");
		listHpanel.add(listOfperiods);
		listHpanel.add(getButtonLayout());
		mainVlayout = new VerticalPanel();
		mainVlayout.setSpacing(5);
		// mainVlayout.add(manageFiscalyearLabel);
		mainVlayout.add(descriptionLabel);
		mainVlayout.add(listHpanel);
		setBodyLayout(mainVlayout);
		addInputDialogHandler(new InputDialogHandler() {

			@Override
			public boolean onOkClick() {
				return true;
			}

			@Override
			public void onCancelClick() {
				hide();
			}
		});
		initDataToFiscalYearList();
	}

	private void initDataToFiscalYearList() {
		listOfperiods.removeAllRecords();
		for (ClientFiscalYear clientFiscalYear : FinanceApplication
				.getCompany().getFiscalYears()) {
			listOfperiods.addData(clientFiscalYear);
		}
		// listOfperiods.sortList();
	}

	private VerticalPanel getButtonLayout() {
		buttonVlayout = new VerticalPanel();
		buttonVlayout.setStyleName(FinanceApplication.getCompanyMessages()
				.fiscalYearButtons());
		buttonVlayout.setSpacing(5);
		newFiscalYearButton = new Button();
		newFiscalYearButton.setWidth("150px");
		newFiscalYearButton.setText(FinanceApplication.getCompanyMessages()
				.newFiscalYear());
		newFiscalYearButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new CreateFiscalYearDialog(FinanceApplication
						.getCompanyMessages().createFascalYear(), "",
						listOfperiods);
			}
		});
		closeFiscalYearButton = new Button();
		closeFiscalYearButton.setWidth("150px");
		closeFiscalYearButton.setEnabled(false);
		closeFiscalYearButton.setText(FinanceApplication.getCompanyMessages()
				.closeFiscalYear());
		closeFiscalYearButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showCloseFiscalYearPopupPanel();
			}
		});
		openFiscalYearButoon = new Button();
		openFiscalYearButoon.setWidth("150px");
		openFiscalYearButoon.setEnabled(false);
		openFiscalYearButoon.setText(FinanceApplication.getCompanyMessages()
				.openFiscalYear());
		openFiscalYearButoon.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final ClientFiscalYear clientFiscalYear = listOfperiods
						.getSelection();
				clientFiscalYear.setStatus(ClientFiscalYear.STATUS_OPEN);
				ViewManager.getInstance().alterObject(clientFiscalYear,
						ManageFiscalYearDialog.this);
			}
		});
		editFiscalYear = new Button();
		editFiscalYear.setWidth("150px");
		editFiscalYear.setText(FinanceApplication.getCompanyMessages()
				.editFiscalYear());
		editFiscalYear.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				editFiscalYear();
			}
		});
		deleteFiscalYear = new Button();
		deleteFiscalYear.setWidth("150px");
		deleteFiscalYear.setText(FinanceApplication.getCompanyMessages()
				.deleteFiscalYear());
		deleteFiscalYear.setWidth("150px");
		// deleteFiscalYear.setEnabled(false);
		deleteFiscalYear.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deleteFiscalYear();
			}
		});
		changeStartDate = new Button();
		changeStartDate.setWidth("150px");
		changeStartDate.setText(FinanceApplication.getCompanyMessages()
				.changeStartDate());
		changeStartDate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new ChangeFiscalYearStartDateDialog(FinanceApplication
						.getCompanyMessages().changeStartDate(), "",
						listOfperiods);
			}
		});
		buttonVlayout.add(newFiscalYearButton);
		buttonVlayout.add(closeFiscalYearButton);
		buttonVlayout.add(openFiscalYearButoon);
		buttonVlayout.add(editFiscalYear);
		buttonVlayout.add(deleteFiscalYear);
		buttonVlayout.add(changeStartDate);

		newFiscalYearButton.getElement().getParentElement().setClassName(
				"ibutton");
		ThemesUtil.addDivToButton(newFiscalYearButton, FinanceApplication
				.getThemeImages().button_right_blue_image(),
				"ibutton-right-image");
		closeFiscalYearButton.getElement().getParentElement().setClassName(
				"ibutton");
		ThemesUtil.addDivToButton(closeFiscalYearButton, FinanceApplication
				.getThemeImages().button_right_blue_image(),
				"ibutton-right-image");
		openFiscalYearButoon.getElement().getParentElement().setClassName(
				"ibutton");
		ThemesUtil.addDivToButton(openFiscalYearButoon, FinanceApplication
				.getThemeImages().button_right_blue_image(),
				"ibutton-right-image");
		editFiscalYear.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(editFiscalYear, FinanceApplication
				.getThemeImages().button_right_blue_image(),
				"ibutton-right-image");
		deleteFiscalYear.getElement().getParentElement()
				.setClassName("ibutton");
		ThemesUtil.addDivToButton(deleteFiscalYear, FinanceApplication
				.getThemeImages().button_right_blue_image(),
				"ibutton-right-image");
		changeStartDate.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(changeStartDate, FinanceApplication
				.getThemeImages().button_right_blue_image(),
				"ibutton-right-image");
		return buttonVlayout;

	}

	private void showCloseFiscalYearPopupPanel() {
		Accounter.showWarning(FinanceApplication.getCompanyMessages()
				.warnOfFascalYear(), AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onYesClick() throws InvalidEntryException {
						final ClientFiscalYear clientFiscalYear = listOfperiods
								.getSelection();
						clientFiscalYear
								.setStatus(ClientFiscalYear.STATUS_CLOSE);
						ViewManager.getInstance().alterObject(clientFiscalYear,
								ManageFiscalYearDialog.this);
						return true;
					}

					@Override
					public boolean onNoClick() throws InvalidEntryException {

						return true;
					}

					@Override
					public boolean onCancelClick() throws InvalidEntryException {

						return true;
					}
				});
	}

	private void deleteFiscalYear() {
		if (listOfperiods.getSelection() != null) {
			// if (listOfperiods.getRecordByIndex(listOfperiods.getRowCount() -
			// 1) == listOfperiods
			// .getSelection()) {
			ViewManager.getInstance().deleteObject(
					listOfperiods.getSelection(), AccounterCoreType.FISCALYEAR,
					this);
			// }
		} else {
			Accounter.showInformation(FinanceApplication.getCompanyMessages()
					.SelectAnyOfTheFiscalYearFromList());
		}
	}

	private void editFiscalYear() {
		if (listOfperiods.getSelection() != null
				&& listOfperiods.getSelection().getStatus() != ClientFiscalYear.STATUS_CLOSE) {

			// if (listOfperiods.getRecordByIndex(listOfperiods.getRowCount() -
			// 1) == listOfperiods
			// .getSelection()
			// || listOfperiods.getRecordByIndex(0) == listOfperiods
			// .getSelection()) {
			new CreateFiscalYearDialog(FinanceApplication.getCompanyMessages()
					.editFiscalYear(), "", listOfperiods);
			// }

			// else {
			//
			// Accounter.showInformation(FinanceApplication
			// .getCompanyMessages().fiscalYearsCanBeEdited());
			// }

		} else if (listOfperiods.getSelection().getStatus() == ClientFiscalYear.STATUS_CLOSE)
			Accounter.showInformation(FinanceApplication.getCompanyMessages()
					.youCannotEdiClosedFiscalYear());
	}

	public void showButtonsVisibility() {
		// for (int i = listOfperiods.getRowCount() - 1; i >= 0; i--) {
		// if (listOfperiods.getRecordByIndex(i).getStatus() ==
		// ClientFiscalYear.STATUS_CLOSE) {
		// if (listOfperiods.getSelection() == listOfperiods
		// .getRecordByIndex(i)) {
		//
		// closeFiscalYearButton.setEnabled(false);
		// openFiscalYearButoon.setEnabled(true);
		// break;
		// } else {
		// openFiscalYearButoon.setEnabled(false);
		// closeFiscalYearButton.setEnabled(false);
		// // break;
		// }
		//
		// }
		//
		// }
		closeFiscalYearButton.setEnabled(false);
		openFiscalYearButoon.setEnabled(false);
		for (int i = 0; i <= listOfperiods.getRowCount() - 1; i++) {
			if (listOfperiods.getRecordByIndex(i).getStatus() == ClientFiscalYear.STATUS_OPEN) {

				if (listOfperiods.getSelection() == listOfperiods
						.getRecordByIndex(i)) {

					openFiscalYearButoon.setEnabled(false);
					closeFiscalYearButton.setEnabled(true);

				} else if (i != 0
						&& listOfperiods.getSelection() == listOfperiods
								.getRecordByIndex(i - 1)) {
					openFiscalYearButoon.setEnabled(true);
					closeFiscalYearButton.setEnabled(false);

				}
				break;

			}
			if (listOfperiods.getSelectedRecordIndex() == listOfperiods
					.getRowCount() - 1
					&& listOfperiods.getSelection().getStatus() == ClientFiscalYear.STATUS_CLOSE) {
				openFiscalYearButoon.setEnabled(true);
				closeFiscalYearButton.setEnabled(false);
			}

		}
		// if (listOfperiods.getSelection() == listOfperiods
		// .getRecordByIndex(listOfperiods.getRowCount() - 1)
		// && listOfperiods.getRecords().size() > 1) {
		// // if (listOfperiods.getSelection().getStatus() ==
		// // ClientFiscalYear.STATUS_CLOSE) {
		// // openFiscalYearButoon.setEnabled(true);
		// // closeFiscalYearButton.setEnabled(false);
		// // } else {
		// // openFiscalYearButoon.setEnabled(false);
		// // closeFiscalYearButton.setEnabled(true);
		// // }
		//
		// deleteFiscalYear.setEnabled(true);
		// } else {
		// deleteFiscalYear.setEnabled(false);
		// }
		if (listOfperiods.getSelection() == listOfperiods.getRecordByIndex(0)) {

		}
	}

	@Override
	public void deleteFailed(Throwable caught) {
		Accounter.showError(caught.getMessage());
	}

	@Override
	public void deleteSuccess(Boolean result) {
		listOfperiods.deleteRecord(listOfperiods.getSelection());
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		// ClientFiscalYear fiscalY = (ClientFiscalYear) object;
		initDataToFiscalYearList();
	}

	@Override
	public void saveFailed(Throwable exception) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}
}
