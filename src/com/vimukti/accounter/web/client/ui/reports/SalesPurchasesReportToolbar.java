package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.forms.DateItem;

/**
 * 
 * @author Sujana Bijjam
 */

public class SalesPurchasesReportToolbar extends ReportToolbar {
	private DateItem fromItem;
	private DateItem toItem;
	protected SelectCombo statusCombo, dateRangeCombo;
	protected List<String> statusList, dateRangeList;
	private com.vimukti.accounter.web.client.ui.core.AccounterButton updateButton;
	public static int OPEN = 1;
	public static int COMPLETED = 2;
	public static int CANCELLED = 3;
	public static int ALL = 4;
	private int status = OPEN;

	public SalesPurchasesReportToolbar() {
		createControls();
		// selectedDateRange = FinanceApplication.getReportsMessages().all();
	}

	private void createControls() {
		String[] statusArray = { Accounter.getReportsMessages().open(),
				Accounter.getReportsMessages().completed(),
				Accounter.getReportsMessages().cancelled(),
				Accounter.getReportsMessages().all() };

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

		statusCombo = new SelectCombo(Accounter.getVendorsMessages().status());
		statusCombo.setHelpInformation(true);
		statusList = new ArrayList<String>();
		for (int i = 0; i < statusArray.length; i++) {
			statusList.add(statusArray[i]);
		}
		statusCombo.initCombo(statusList);
		statusCombo.setComboItem(statusArray[0]);
		statusCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						if (statusCombo.getSelectedValue().equals(
								Accounter.getReportsMessages().open())) {
							status = OPEN;
						} else if (statusCombo.getSelectedValue().equals(
								Accounter.getReportsMessages().completed())) {
							status = COMPLETED;
						} else if (statusCombo.getSelectedValue().equals(
								Accounter.getReportsMessages().all())) {
							status = ALL;
						} else
							status = CANCELLED;
						ClientFinanceDate startDate = fromItem.getDate();
						ClientFinanceDate endDate = toItem.getDate();
						reportview
								.makeReportRequest(status, startDate, endDate);

					}
				});

		dateRangeCombo = new SelectCombo(Accounter.getReportsMessages()
				.dateRange());
		dateRangeCombo.setHelpInformation(true);
		dateRangeCombo.setValueMap(dateRangeArray);
		dateRangeCombo.setDefaultValue(dateRangeArray[0]);
		dateRangeList = new ArrayList<String>();
		for (int i = 0; i < statusArray.length; i++) {
			dateRangeList.add(statusArray[i]);
		}
		dateRangeCombo.initCombo(dateRangeList);
		dateRangeCombo.setComboItem(Accounter.getReportsMessages().all());
		dateRangeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						dateRangeChanged(dateRangeCombo.getSelectedValue());
					}

				});

		fromItem = new DateItem();
		fromItem.setHelpInformation(true);
		fromItem.setDatethanFireEvent(Accounter.getStartDate());
		fromItem.setTitle(Accounter.getReportsMessages().from());

		toItem = new DateItem();
		toItem.setHelpInformation(true);
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
				startDate = fromItem.getValue();
				endDate = toItem.getValue();
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
				dateRangeCombo.setDefaultValue(Accounter.getReportsMessages()
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
			dateRangeCombo.setWidth("200px");
			statusCombo.setWidth("200px");
		}
		addItems(statusCombo, dateRangeCombo, fromItem, toItem);
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
		reportview.makeReportRequest(status, startDate, endDate);

		// itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL, startDate,
		// endDate);
	}

	@Override
	public void setDateRanageOptions(String... dateRanages) {
		List<String> dateRangesList = new ArrayList<String>();
		for (int i = 0; i < dateRanages.length; i++) {
			dateRangesList.add(dateRanages[i]);
		}
		dateRangeCombo.initCombo(dateRangesList);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeCombo.setDefaultValue(defaultDateRange);
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
