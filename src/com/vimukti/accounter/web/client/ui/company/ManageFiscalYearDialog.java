package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

public class ManageFiscalYearDialog extends BaseDialog {

	private HTML manageFiscalyearLabel;
	private HTML descriptionLabel;
	private FiscalYearListGrid listOfperiods;
	private VerticalPanel buttonVlayout;
	private AccounterButton newFiscalYearButton;
	private AccounterButton closeFiscalYearButton;
	private AccounterButton openFiscalYearButoon;
	private AccounterButton editFiscalYear;
	private AccounterButton deleteFiscalYear;
	private AccounterButton changeStartDate;
	private HorizontalPanel listHpanel;
	private VerticalPanel mainVlayout;

	private HorizontalPanel buttonPanel;

	public ManageFiscalYearDialog(String title, String desc) {
		super(title, desc);
		createControls();
		setSize("550px", "250px");
		center();

	}

	public void createControls() {
		manageFiscalyearLabel = new HTML();
		manageFiscalyearLabel.setHTML(Accounter.constants().manageFiscalYear());
		descriptionLabel = new HTML();
		descriptionLabel.setHTML(Accounter.constants()
				.toCloseBooksOrFascalYear());
		listOfperiods = new FiscalYearListGrid(this, false);
		listOfperiods.setTitle(Accounter.constants().listOfPeriods());
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
			public boolean onOK() {
				return true;
			}

			@Override
			public void onCancel() {
				hide();
			}
		});
		initDataToFiscalYearList();
	}

	private void initDataToFiscalYearList() {
		listOfperiods.removeAllRecords();
		for (ClientFiscalYear clientFiscalYear : Accounter.getCompany()
				.getFiscalYears()) {
			listOfperiods.addData(clientFiscalYear);
		}
		// listOfperiods.sortList();
	}

	private VerticalPanel getButtonLayout() {
		buttonVlayout = new VerticalPanel();
		buttonVlayout.setStyleName(Accounter.constants().fiscalYearButtons());
		buttonVlayout.setSpacing(5);
		newFiscalYearButton = new AccounterButton();
		newFiscalYearButton.setWidth("140px");
		newFiscalYearButton.setText(Accounter.constants().newFiscalYear());
		newFiscalYearButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new CreateFiscalYearDialog(Accounter.constants()
						.createFascalYear(), "", listOfperiods);
			}
		});
		closeFiscalYearButton = new AccounterButton();
		closeFiscalYearButton.setWidth("140px");
		closeFiscalYearButton.setEnabled(false);
		closeFiscalYearButton.setText(Accounter.constants().closeFiscalYear());
		closeFiscalYearButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showCloseFiscalYearPopupPanel();
			}
		});
		openFiscalYearButoon = new AccounterButton();
		openFiscalYearButoon.setWidth("140px");
		openFiscalYearButoon.setEnabled(false);
		openFiscalYearButoon.setText(Accounter.constants().openFiscalYear());
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
		editFiscalYear = new AccounterButton();
		editFiscalYear.setWidth("140px");
		editFiscalYear.setEnabled(false);
		editFiscalYear.setText(Accounter.constants().editFiscalYear());
		editFiscalYear.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				editFiscalYear();
			}
		});
		deleteFiscalYear = new AccounterButton();
		deleteFiscalYear.setWidth("140px");
		deleteFiscalYear.setText(Accounter.constants().deleteFiscalYear());
		deleteFiscalYear.setWidth("140px");
		// deleteFiscalYear.setEnabled(false);
		deleteFiscalYear.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deleteFiscalYear();
			}
		});
		changeStartDate = new AccounterButton();
		changeStartDate.setWidth("140px");
		changeStartDate.setText(Accounter.constants().changeStartDate());
		changeStartDate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new ChangeFiscalYearStartDateDialog(Accounter.constants()
						.changeStartDate(), "", listOfperiods);
			}
		});

		buttonVlayout.setWidth("160px");
		buttonVlayout.add(newFiscalYearButton);
		buttonVlayout.add(closeFiscalYearButton);
		buttonVlayout.add(openFiscalYearButoon);
		buttonVlayout.add(editFiscalYear);
		buttonVlayout.add(deleteFiscalYear);
		buttonVlayout.add(changeStartDate);

		newFiscalYearButton.enabledButton();
		// closeFiscalYearButton.enabledButton();
		// openFiscalYearButoon.enabledButton();
		// editFiscalYear.enabledButton();
		deleteFiscalYear.enabledButton();
		changeStartDate.enabledButton();

		//
		// if (newFiscalYearButton.isEnabled()) {
		// newFiscalYearButton.getElement().getParentElement().setClassName(
		// "ibutton");
		// ThemesUtil.addDivToButton(newFiscalYearButton, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// } /*
		// * else {
		// * newFiscalYearButton.getElement().getParentElement().setClassName(
		// * "cancel-button"); ThemesUtil.addDivToButton(newFiscalYearButton,
		// * FinanceApplication .getThemeImages().button_right_gray_image(),
		// * "ibutton-right-image"); }
		// */
		// if (closeFiscalYearButton.isEnabled()) {
		// closeFiscalYearButton.getElement().getParentElement().setClassName(
		// "ibutton");
		// ThemesUtil.addDivToButton(closeFiscalYearButton, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// } /*
		// * else {
		// * closeFiscalYearButton.getElement().getParentElement().setClassName(
		// * "cancel-button"); ThemesUtil.addDivToButton(closeFiscalYearButton,
		// * FinanceApplication .getThemeImages().button_right_gray_image(),
		// * "ibutton-right-image"); }
		// */
		// if (openFiscalYearButoon.isEnabled()) {
		// openFiscalYearButoon.getElement().getParentElement().setClassName(
		// "ibutton");
		// ThemesUtil.addDivToButton(openFiscalYearButoon, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// } /*
		// * else {
		// * openFiscalYearButoon.getElement().getParentElement().setClassName(
		// * "cancel-button"); ThemesUtil.addDivToButton(openFiscalYearButoon,
		// * FinanceApplication .getThemeImages().button_right_gray_image(),
		// * "ibutton-right-image"); }
		// */
		// if (editFiscalYear.isEnabled()) {
		// editFiscalYear.getElement().getParentElement().setClassName(
		// "ibutton");
		// ThemesUtil.addDivToButton(editFiscalYear, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// } /*
		// * else { editFiscalYear.getElement().getParentElement().setClassName(
		// * "cancel-button"); ThemesUtil.addDivToButton(editFiscalYear,
		// * FinanceApplication .getThemeImages().button_right_gray_image(),
		// * "ibutton-right-image"); }
		// */
		// if (deleteFiscalYear.isEnabled()) {
		// deleteFiscalYear.getElement().getParentElement().setClassName(
		// "ibutton");
		// ThemesUtil.addDivToButton(deleteFiscalYear, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// } /*
		// * else {
		// deleteFiscalYear.getElement().getParentElement().setClassName(
		// * "cancel-button"); ThemesUtil.addDivToButton(deleteFiscalYear,
		// * FinanceApplication .getThemeImages().button_right_gray_image(),
		// * "ibutton-right-image"); }
		// */
		// if (changeStartDate.isEnabled()) {
		// changeStartDate.getElement().getParentElement().setClassName(
		// "ibutton");
		// ThemesUtil.addDivToButton(changeStartDate, FinanceApplication
		// .getThemeImages().button_right_blue_image(),
		// "ibutton-right-image");
		// } /*
		// * else {
		// changeStartDate.getElement().getParentElement().setClassName(
		// * "cancel-button"); ThemesUtil.addDivToButton(changeStartDate,
		// * FinanceApplication .getThemeImages().button_right_gray_image(),
		// * "ibutton-right-image"); }
		// */
		return buttonVlayout;

	}

	private void showCloseFiscalYearPopupPanel() {
		Accounter.showWarning(Accounter.constants().warnOfFascalYear(),
				AccounterType.WARNING, new ErrorDialogHandler() {

					@Override
					public boolean onYesClick() {
						final ClientFiscalYear clientFiscalYear = listOfperiods
								.getSelection();
						clientFiscalYear
								.setStatus(ClientFiscalYear.STATUS_CLOSE);
						ViewManager.getInstance().alterObject(clientFiscalYear,
								ManageFiscalYearDialog.this);
						return true;
					}

					@Override
					public boolean onNoClick() {

						return true;
					}

					@Override
					public boolean onCancel() {

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
			Accounter.showInformation(Accounter.constants()
					.selectAnyOfTheFiscalYearFromList());
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
			new CreateFiscalYearDialog(Accounter.constants().editFiscalYear(),
					"", listOfperiods);
			// }

			// else {
			//
			// Accounter.showInformation(FinanceApplication
			// .constants().fiscalYearsCanBeEdited());
			// }

		} else if (listOfperiods.getSelection().getStatus() == ClientFiscalYear.STATUS_CLOSE)
			Accounter.showInformation(Accounter.constants()
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
		editFiscalYear.setEnabled(false);
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
				editFiscalYear.setEnabled(true);
				break;

			}
			if (listOfperiods.getSelectedRecordIndex() == listOfperiods
					.getRowCount() - 1
					&& listOfperiods.getSelection().getStatus() == ClientFiscalYear.STATUS_CLOSE) {
				openFiscalYearButoon.setEnabled(true);
				closeFiscalYearButton.setEnabled(false);
				editFiscalYear.setEnabled(true);
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

}
