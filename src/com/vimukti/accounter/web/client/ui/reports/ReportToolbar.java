package com.vimukti.accounter.web.client.ui.reports;

import java.util.Date;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;

public abstract class ReportToolbar extends HorizontalPanel {
	protected ClientFinanceDate startDate;
	protected ClientFinanceDate endDate;
	private DynamicForm form;
	protected ReportToolBarItemSelectionHandler itemSelectionHandler;
	public static final int TYPE_ACCRUAL = 0;
	public static final int TYPE_CASH = 1;
	private String selectedDateRange = "";
	protected AbstractReportView<?> reportview;

	public boolean isToolBarComponentChanged;

	public ReportToolbar() {
		createControls();
	}

	private void createControls() {
		setSize("100%", "10");
		// setBackgroundColor("#dedede");

		form = new DynamicForm();
		form.setSize("100%", "100%");
		form.setNumCols(8);

		startDate = FinanceApplication.getStartDate();
		endDate = Utility.getLastandOpenedFiscalYearEndDate();
		itemSelectionHandler = new ReportToolBarItemSelectionHandler() {

			public void onItemSelectionChanged(int type,
					ClientFinanceDate startDate, ClientFinanceDate endDate) {

			}
		};
		add(form);
	}

	public ReportToolBarItemSelectionHandler getItemSelectionHandler() {
		return itemSelectionHandler;
	}

	public void setItemSelectionHandler(
			ReportToolBarItemSelectionHandler itemSelectionHandler) {
		this.itemSelectionHandler = itemSelectionHandler;
	}

	public void addItems(FormItem... items) {
		form.setItems(items);
	}

	public ClientFinanceDate getStartDate() {
		return startDate != null && startDate.getTime() == 0 ? new ClientFinanceDate()
				: startDate;
	}

	public void setStartDate(ClientFinanceDate startDate) {
		this.startDate = startDate;
	}

	public ClientFinanceDate getEndDate() {
return (endDate != null && endDate.getTime() == 0) ? new ClientFinanceDate()
				: endDate;
	}

	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
	}

	public void dateRangeChanged(String dateRange) {
		try {
			ClientFinanceDate date = new ClientFinanceDate();
			if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().all())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.all())) {
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.all());
				// startDate = FinanceApplication.getStartDate();
				// endDate = Utility.getLastandOpenedFiscalYearEndDate();
				// if (endDate == null)
				// endDate = new ClientFinanceDate();
				startDate = new ClientFinanceDate(0);
				endDate = new ClientFinanceDate(0);

			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().today())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.today())) {
				startDate = new ClientFinanceDate();
				endDate = Utility.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.today());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().endThisWeek())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.endThisWeek())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate(new Date(
						(long) getWeekEndDate()));
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.endThisWeek());
			} else if (!getSelectedDateRange()
					.equals(
							FinanceApplication.getReportsMessages()
									.endThisWeekToDate())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.endThisWeekToDate())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.endThisWeekToDate());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().endThisMonth())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.endThisMonth())) {
				int lastDay = getMonthLastDate(date.getMonth(), date.getYear());
				startDate = new ClientFinanceDate(date.getYear(), date
						.getMonth(), 1);
				endDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), lastDay);
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.endThisMonth());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages()
							.endThisMonthToDate())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.endThisMonthToDate())) {
				startDate = new ClientFinanceDate(date.getYear(), date
						.getMonth(), 1);
				endDate = Utility.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.endThisMonthToDate());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages()
							.endThisFiscalQuarter())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.endThisFiscalQuarter())) {
				// TODO changes are needed for calculating Fiscal Quarter,
				// according
				// to user preferences.
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.endThisFiscalQuarter());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages()
							.endThisFiscalQuarterToDate())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.endThisFiscalQuarterToDate())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				@SuppressWarnings("unused")
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = Utility.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.endThisFiscalQuarterToDate());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages()
							.endThisCalanderQuarter())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.endThisCalanderQuarter())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.endThisCalanderQuarter());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages()
							.endThisCalanderQuarterToDate())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.endThisCalanderQuarterToDate())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = Utility.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.endThisCalanderQuarterToDate());
			} else if (!getSelectedDateRange()
					.equals(
							FinanceApplication.getReportsMessages()
									.endThisFiscalYear())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.endThisFiscalYear())) {

				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.endThisFiscalYear());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages()
							.endThisFiscalYearToDate())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.endThisFiscalYearToDate())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = Utility.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.endThisFiscalYearToDate());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages()
							.endThisCalanderYear())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.endThisCalanderYear())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.endThisCalanderYear());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages()
							.endThisCalanderYearToDate())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.endThisCalanderYearToDate())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = Utility.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.endThisCalanderYearToDate());
				changeDates(startDate, endDate);
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().endYesterday())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.endYesterday())) {
				// startDate = new ClientFinanceDate(date.getYear(),
				// date.getMonth(), date
				// .getDate() - 1);
				startDate = FinanceApplication.getStartDate();
				endDate = Utility.getLastandOpenedFiscalYearEndDate();
				int day = endDate.getDay();
				endDate.setDate(day - 1);
				setSelectedDateRange((FinanceApplication.getReportsMessages()
						.endYesterday()));
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages()
							.endPreviousFiscalQuarter())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.endPreviousFiscalQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Utility.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.endPreviousFiscalQuarter());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages()
							.endLastCalenderQuarter())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.endLastCalenderQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Utility.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.endLastCalenderQuarter());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages()
							.previousFiscalYearSameDates())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.previousFiscalYearSameDates())) {
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.previousFiscalYearSameDates());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages()
							.previousFiscalYearSameDates())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.previousFiscalYearSameDates())) {

				startDate = new ClientFinanceDate(this.startDate.getYear() - 1,
						this.startDate.getMonth(), this.startDate.getDate());
				endDate = new ClientFinanceDate(this.endDate.getYear() - 1,
						this.endDate.getMonth(), this.endDate.getDate());
				// startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				// endDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.previousFiscalYearSameDates());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().lastCalenderYear())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.lastCalenderYear())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);

				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.lastCalenderYear());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages()
							.previousCalenderYear())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.previousCalenderYear())) {
				startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
				// startDate = new ClientFinanceDate(this.startDate.getYear() -
				// 1,
				// this.startDate
				// .getMonth(), this.startDate.getDate());
				// startDate = new ClientFinanceDate(this.endDate.getYear() - 1,
				// this.endDate
				// .getMonth(), this.endDate.getDate());
				// startDate.setYear(this.startDate.getYear() - 1);
				// endDate.setYear(this.endDate.getYear() - 1);
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.previousCalenderYear());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().lastMonth())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.lastMonth())) {
				int day;
				if (date.getMonth() == 0) {
					day = getMonthLastDate(11, date.getYear() - 1);
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else {
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					startDate = new ClientFinanceDate(date.getYear(), date
							.getMonth() - 1, 1);
					endDate = new ClientFinanceDate(date.getYear(), date
							.getMonth() - 1, day);
				}
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.lastMonth());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().last3Months())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.last3Months())) {
				int day;
				if (date.getMonth() == 0) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
					day = getMonthLastDate(11, date.getYear() - 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else if (date.getMonth() == 1) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 10, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(), date
							.getMonth() - 1, day);
				} else if (date.getMonth() == 2) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(), date
							.getMonth() - 1, day);
				} else {
					startDate = new ClientFinanceDate(date.getYear(), date
							.getMonth() - 3, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(), date
							.getMonth() - 1, day);
				}
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.last3Months());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().last6Months())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.last6Months())) {
				int day;
				if (date.getMonth() == 0) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 6, 1);
					day = getMonthLastDate(11, date.getYear() - 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else if (date.getMonth() == 1) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 7, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(), date
							.getMonth() - 1, day);
				} else if (date.getMonth() == 2) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 8, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(), date
							.getMonth() - 1, day);
				} else if (date.getMonth() == 3) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(), date
							.getMonth() - 1, day);
				} else if (date.getMonth() == 4) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 10, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(), date
							.getMonth() - 1, day);
				} else if (date.getMonth() == 5) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(), date
							.getMonth() - 1, day);
				} else {
					startDate = new ClientFinanceDate(date.getYear(), date
							.getMonth() - 6, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(), date
							.getMonth() - 1, day);
				}
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.last6Months());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().lastYear())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.lastYear())) {
				startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.lastYear());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().present())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.present())) {
				startDate = new ClientFinanceDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.present());

			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().untilEndOfYear())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.untilEndOfYear())) {
				startDate = new ClientFinanceDate();
				endDate = new ClientFinanceDate(startDate.getYear(), 11, 31);
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.untilEndOfYear());

			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().thisWeek())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.thisWeek())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.thisWeek());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().thisMonth())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.thisMonth())) {
				startDate = new ClientFinanceDate(date.getYear(), date
						.getMonth(), 1);
				endDate = new ClientFinanceDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.thisMonth());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().lastWeek())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.lastWeek())) {

				endDate = getWeekStartDate();
				endDate.setDate(endDate.getDate() - 1);
				startDate = new ClientFinanceDate(endDate.getTime());
				startDate.setDate(startDate.getDate() - 6);

				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.lastWeek());

			} else if (!getSelectedDateRange()
					.equals(
							FinanceApplication.getReportsMessages()
									.thisFinancialYear())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.thisFinancialYear())) {
				startDate = Utility.getCurrentFiscalYearStartDate();
				endDate = Utility.getCurrentFiscalYearEndDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.thisFinancialYear());
			} else if (!getSelectedDateRange()
					.equals(
							FinanceApplication.getReportsMessages()
									.lastFinancialYear())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.lastFinancialYear())) {
				startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.lastFinancialYear());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages()
							.thisFinancialQuarter())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.thisFinancialQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Utility.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.thisFinancialQuarter());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages()
							.lastFinancialQuarter())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.lastFinancialQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Utility.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.lastFinancialQuarter());
				getCurrentQuarter();
				startDate.setYear(startDate.getYear() - 1);
				endDate.setYear(endDate.getYear() - 1);

			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages()
							.financialYearToDate())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.financialYearToDate())) {
				startDate = Utility.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.financialYearToDate());
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().thisVATQuarter())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.thisVATQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Utility.getCurrentFiscalYearEndDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.thisVATQuarter());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages()
							.thisVATQuarterToDate())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.thisVATQuarterToDate())) {
				startDate = Utility.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.thisVATQuarterToDate());
				getCurrentQuarter();
				endDate = new ClientFinanceDate();

			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().lastVATQuarter())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.lastVATQuarter())) {
				startDate = Utility.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.lastVATQuarter());
				getPreviousQuarter();

			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages()
							.lastVATQuarterToDate())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.lastVATQuarterToDate())) {
				startDate = Utility.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.lastVATQuarterToDate());
				getPreviousQuarter();
				endDate = new ClientFinanceDate();
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().nextVATQuarter())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.nextVATQuarter())) {
				startDate = Utility.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.nextVATQuarter());
				getNextQuarter();
			} else if (!getSelectedDateRange().equals(
					FinanceApplication.getReportsMessages().custom())
					&& dateRange.equals(FinanceApplication.getReportsMessages()
							.custom())) {
				startDate = this.getStartDate();
				endDate = this.getEndDate();
				setSelectedDateRange(FinanceApplication.getReportsMessages()
						.custom());
			}
			setStartDate(startDate);
			setEndDate(endDate);
			changeDates(startDate, endDate);
		} catch (Exception e) {
		}
	}

	public void getCurrentQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear(), 0, 1);
			endDate = new ClientFinanceDate(date.getYear(), 2, 31);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear(), 9, 1);
			endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			break;
		}
	}

	public void getPreviousQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
			endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 0, 1);
			endDate = new ClientFinanceDate(date.getYear(), 2, 31);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;
		}
	}

	public void getNextQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 9, 1);
			endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear() + 1, 0, 1);
			endDate = new ClientFinanceDate(date.getYear() + 1, 2, 31);
			break;
		}
	}

	public int getMonthLastDate(int month, int year) {
		int lastDay;
		switch (month) {
		case 0:
		case 2:
		case 4:
		case 6:
		case 7:
		case 9:
		case 11:
			lastDay = 31;
			break;
		case 1:
			if (year % 4 == 0 && year % 100 == 0)
				lastDay = 29;
			else
				lastDay = 28;
			break;

		default:
			lastDay = 30;
			break;
		}
		return lastDay;
	}

	public ClientFinanceDate getWeekStartDate() {
		ClientFinanceDate date = new ClientFinanceDate();
		int day = date.getDay();
		ClientFinanceDate newDate = new ClientFinanceDate();
		newDate.setDate(Math.abs(date.getDate() - day));
		return newDate;
	}

	// public native double getWeekStartDate()/*-{
	// var date= new ClientFinanceDate();
	// var day=date.getDay();
	// var newDate=new ClientFinanceDate();
	// newDate.setDate(date.getDate()-day);
	// var tmp=newDate.getTime();
	// return tmp;
	// }-*/;

	public native double getWeekEndDate()/*-{
		var date= new ClientFinanceDate();
		var day=date.getDay();
		var remainingDays= 6-day;
		var newDate=new ClientFinanceDate();
		newDate.setDate(date.getDate()+remainingDays);
		var tmp=newDate.getTime();
		return tmp;
	}-*/;

	public abstract void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public abstract void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public abstract void setDefaultDateRange(String defaultDateRange);

	public void setDateRanageOptions(String... dateRanages) {

	}

	public AbstractReportView<?> getView() {
		return reportview;
	}

	public void setView(AbstractReportView<?> view) {
		this.reportview = view;
	}

	/**
	 * @param selectedDateRange
	 *            the selectedDateRange to set
	 */
	public void setSelectedDateRange(String selectedDateRange) {
		this.selectedDateRange = selectedDateRange;
	}

	/**
	 * @return the selectedDateRange
	 */
	public String getSelectedDateRange() {
		return selectedDateRange;
	}
}
