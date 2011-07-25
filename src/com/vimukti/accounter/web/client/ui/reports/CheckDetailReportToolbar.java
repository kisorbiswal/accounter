package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.forms.ComboBoxItem;
import com.vimukti.accounter.web.client.ui.forms.DateItem;

public class CheckDetailReportToolbar extends ReportToolbar {
	private DateItem fromItem;
	private DateItem toItem;
	protected ComboBoxItem checkDetailCombo;
	private ComboBoxItem dateRangeItem;
	private AccounterButton updateButton;

	public CheckDetailReportToolbar() {
		createControls();
		// selectedDateRange = FinanceApplication.getReportsMessages().all();
	}

	private void createControls() {
		String[] statusArray = { Accounter.getVendorsMessages().cash(),
				Accounter.getVendorsMessages().check(),
				Accounter.getVendorsMessages().creditCard(),
				Accounter.getVendorsMessages().directDebit(),
				Accounter.getVendorsMessages().masterCard(),
				Accounter.getVendorsMessages().onlineBanking(),
				Accounter.getVendorsMessages().standingOrder(),
				Accounter.getVendorsMessages().switchMaestro() };

		String[] dateRangeArray = { Accounter.getReportsMessages().all(),
				Accounter.getReportsMessages().thisWeek(),
				Accounter.getReportsMessages().thisMonth(),
				Accounter.getReportsMessages().lastWeek(),
				Accounter.getReportsMessages().lastMonth(),
				Accounter.getReportsMessages().thisFinancialYear(),
				Accounter.getReportsMessages().lastFinancialYear(),
				Accounter.getReportsMessages().thisFinancialQuarter(),
				Accounter.getReportsMessages().lastFinancialQuarter(),
				Accounter.getReportsMessages().financialYearToDate(),
				Accounter.getReportsMessages().custom() };

		checkDetailCombo = new ComboBoxItem();
		checkDetailCombo.setTitle(Accounter.getVendorsMessages()
				.Paymentmethod());
		checkDetailCombo.setValueMap(statusArray);
		checkDetailCombo.setDefaultValue(statusArray[0]);
		checkDetailCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				ClientFinanceDate startDate = fromItem.getDate();
				ClientFinanceDate endDate = toItem.getDate();
				reportview.makeReportRequest(
						(Long) checkDetailCombo.getValue(), startDate, endDate);

			}
		});

		dateRangeItem = new ComboBoxItem();
		dateRangeItem.setTitle(Accounter.getReportsMessages().dateRange());
		dateRangeItem.setValueMap(dateRangeArray);
		dateRangeItem.setDefaultValue(dateRangeArray[0]);

		dateRangeItem.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {

				dateRangeChanged(dateRangeItem.getValue().toString());

			}
		});

		fromItem = new DateItem();
		fromItem.setDatethanFireEvent(Accounter.getStartDate());
		fromItem.setTitle(Accounter.getReportsMessages().from());

		toItem = new DateItem();
		ClientFinanceDate date = Accounter.getCompany()
				.getLastandOpenedFiscalYearEndDate();

		if (date != null)
			toItem.setDatethanFireEvent(date);
		else
			toItem.setDatethanFireEvent(new ClientFinanceDate());

		toItem.setTitle(Accounter.getReportsMessages().to());
		toItem.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				startDate = (ClientFinanceDate) fromItem.getValue();
				endDate = (ClientFinanceDate) toItem.getValue();
			}
		});
		updateButton = new AccounterButton(Accounter.getReportsMessages()
				.update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				setStartDate(fromItem.getDate());
				setEndDate(toItem.getDate());

				changeDates(fromItem.getDate(), toItem.getDate());
				dateRangeItem.setDefaultValue(Accounter.getReportsMessages()
						.custom());
				setSelectedDateRange(Accounter.getReportsMessages().custom());

			}
		});

		// fromItem.setDisabled(true);
		// toItem.setDisabled(true);
		// updateButton.setEnabled(false);

		AccounterButton printButton = new AccounterButton(Accounter
				.getReportsMessages().print());
		// printButton.setTop(2);
		// printButton.setWidth(40);
		printButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

			}

		});

		if (UIUtils.isMSIEBrowser()) {
			dateRangeItem.setWidth("170px");
			checkDetailCombo.setWidth("90px");
		}
		addItems(checkDetailCombo, dateRangeItem, fromItem, toItem);
		add(updateButton);
		updateButton.enabledButton();
		this.setCellVerticalAlignment(updateButton,
				HasVerticalAlignment.ALIGN_MIDDLE);
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		reportview.makeReportRequest((Long) checkDetailCombo.getValue(),
				startDate, endDate);

		// itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL, startDate,
		// endDate);
	}

	@Override
	public void setDateRanageOptions(String... dateRanages) {
		dateRangeItem.setValueMap(dateRanages);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeItem.setDefaultValue(defaultDateRange);
		dateRangeChanged(defaultDateRange);
	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		if (startDate != null && endDate != null) {
			fromItem.setEnteredDate(startDate);
			toItem.setEnteredDate(endDate);
			setStartDate(startDate);
			setEndDate(endDate);
		}
	}
}
