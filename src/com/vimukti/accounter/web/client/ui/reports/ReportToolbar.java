package com.vimukti.accounter.web.client.ui.reports;

import java.util.Date;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Calendar;
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
		setSize("100%", "10px");
		// setBackgroundColor("#dedede");

		form = new DynamicForm();
		form.setSize("100%", "100%");
		form.setNumCols(8);

		startDate = Accounter.getStartDate();
		endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
		// getLastandOpenedFiscalYearEndDate();
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
		return startDate != null && startDate.getDate() == 0 ? new ClientFinanceDate()
				: startDate;
	}

	public void setStartDate(ClientFinanceDate startDate) {
		this.startDate = startDate;
	}

	public ClientFinanceDate getEndDate() {
		return (endDate != null && endDate.getDate() == 0) ? new ClientFinanceDate()
				: endDate;
	}

	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
	}

	public void dateRangeChanged(String dateRange) {
		try {
			ClientFinanceDate date = new ClientFinanceDate();
			if (!getSelectedDateRange().equals(Accounter.constants().all())
					&& dateRange.equals(Accounter.constants().all())) {
				setSelectedDateRange(Accounter.constants().all());
				// startDate = FinanceApplication.getStartDate();
				// endDate = Utility.getLastandOpenedFiscalYearEndDate();
				// if (endDate == null)
				// endDate = new ClientFinanceDate();
				startDate = new ClientFinanceDate(0);
				endDate = new ClientFinanceDate(0);

			} else if (!getSelectedDateRange().equals(
					Accounter.constants().today())
					&& dateRange.equals(Accounter.constants().today())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.constants().today());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().endThisWeek())
					&& dateRange.equals(Accounter.constants().endThisWeek())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate(new Date(
						(long) getWeekEndDate()));
				setSelectedDateRange(Accounter.constants().endThisWeek());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().endThisWeekToDate())
					&& dateRange.equals(Accounter.constants()
							.endThisWeekToDate())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.constants().endThisWeekToDate());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().endThisMonth())
					&& dateRange.equals(Accounter.constants().endThisMonth())) {
				int lastDay = getMonthLastDate(date.getMonth(), date.getYear());
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				endDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), lastDay);
				setSelectedDateRange(Accounter.constants().endThisMonth());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().endThisMonthToDate())
					&& dateRange.equals(Accounter.constants()
							.endThisMonthToDate())) {
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.constants().endThisMonthToDate());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().endThisFiscalQuarter())
					&& dateRange.equals(Accounter.constants()
							.endThisFiscalQuarter())) {
				// changes are needed for calculating Fiscal Quarter,
				// according
				// to user preferences.
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
				setSelectedDateRange(Accounter.constants()
						.endThisFiscalQuarter());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().endThisFiscalQuarterToDate())
					&& dateRange.equals(Accounter.constants()
							.endThisFiscalQuarterToDate())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;

				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.constants()
						.endThisFiscalQuarterToDate());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().endThisCalanderQuarter())
					&& dateRange.equals(Accounter.constants()
							.endThisCalanderQuarter())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
				setSelectedDateRange(Accounter.constants()
						.endThisCalanderQuarter());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().endThisCalanderQuarterToDate())
					&& dateRange.equals(Accounter.constants()
							.endThisCalanderQuarterToDate())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.constants()
						.endThisCalanderQuarterToDate());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().endThisFiscalYear())
					&& dateRange.equals(Accounter.constants()
							.endThisFiscalYear())) {

				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);
				setSelectedDateRange(Accounter.constants().endThisFiscalYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().endThisFiscalYearToDate())
					&& dateRange.equals(Accounter.constants()
							.endThisFiscalYearToDate())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.constants()
						.endThisFiscalYearToDate());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().endThisCalanderYear())
					&& dateRange.equals(Accounter.constants()
							.endThisCalanderYear())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);
				setSelectedDateRange(Accounter.constants()
						.endThisCalanderYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().endThisCalanderYearToDate())
					&& dateRange.equals(Accounter.constants()
							.endThisCalanderYearToDate())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.constants()
						.endThisCalanderYearToDate());
				changeDates(startDate, endDate);
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().endYesterday())
					&& dateRange.equals(Accounter.constants().endYesterday())) {
				// startDate = new ClientFinanceDate(date.getYear(),
				// date.getMonth(), date
				// .getDate() - 1);
				startDate = Accounter.getStartDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				int day = endDate.getDay();
				endDate.setDay(day - 1);
				setSelectedDateRange((Accounter.constants().endYesterday()));
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().endPreviousFiscalQuarter())
					&& dateRange.equals(Accounter.constants()
							.endPreviousFiscalQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.constants()
						.endPreviousFiscalQuarter());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().endLastCalendarQuarter())
					&& dateRange.equals(Accounter.constants()
							.endLastCalendarQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.constants()
						.endLastCalendarQuarter());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().previousFiscalYearSameDates())
					&& dateRange.equals(Accounter.constants()
							.previousFiscalYearSameDates())) {
				setSelectedDateRange(Accounter.constants()
						.previousFiscalYearSameDates());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().previousFiscalYearSameDates())
					&& dateRange.equals(Accounter.constants()
							.previousFiscalYearSameDates())) {

				startDate = new ClientFinanceDate(this.startDate.getYear() - 1,
						this.startDate.getMonth(), this.startDate.getDay());
				endDate = new ClientFinanceDate(this.endDate.getYear() - 1,
						this.endDate.getMonth(), this.endDate.getDay());
				// startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				// endDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
				setSelectedDateRange(Accounter.constants()
						.previousFiscalYearSameDates());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().lastCalenderYear())
					&& dateRange.equals(Accounter.constants()
							.lastCalenderYear())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);

				setSelectedDateRange(Accounter.constants().lastCalenderYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().previousCalenderYear())
					&& dateRange.equals(Accounter.constants()
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
				setSelectedDateRange(Accounter.constants()
						.previousCalenderYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().lastMonth())
					&& dateRange.equals(Accounter.constants().lastMonth())) {
				int day;
				if (date.getMonth() == 0) {
					day = getMonthLastDate(11, date.getYear() - 1);
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else {
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					startDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, 1);
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				}
				setSelectedDateRange(Accounter.constants().lastMonth());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().last3Months())
					&& dateRange.equals(Accounter.constants().last3Months())) {
				int day;
				if (date.getMonth() == 0) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
					day = getMonthLastDate(11, date.getYear() - 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else if (date.getMonth() == 1) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 10, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 2) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else {
					startDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 3, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				}
				setSelectedDateRange(Accounter.constants().last3Months());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().last6Months())
					&& dateRange.equals(Accounter.constants().last6Months())) {
				int day;
				if (date.getMonth() == 0) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 6, 1);
					day = getMonthLastDate(11, date.getYear() - 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else if (date.getMonth() == 1) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 7, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 2) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 8, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 3) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 4) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 10, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 5) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else {
					startDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 6, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				}
				setSelectedDateRange(Accounter.constants().last6Months());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().lastYear())
					&& dateRange.equals(Accounter.constants().lastYear())) {
				startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
				setSelectedDateRange(Accounter.constants().lastYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().present())
					&& dateRange.equals(Accounter.constants().present())) {
				startDate = new ClientFinanceDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.constants().present());

			} else if (!getSelectedDateRange().equals(
					Accounter.constants().untilEndOfYear())
					&& dateRange.equals(Accounter.constants().untilEndOfYear())) {
				startDate = new ClientFinanceDate();
				endDate = new ClientFinanceDate(startDate.getYear(), 11, 31);
				setSelectedDateRange(Accounter.constants().untilEndOfYear());

			} else if (!getSelectedDateRange().equals(
					Accounter.constants().thisWeek())
					&& dateRange.equals(Accounter.constants().thisWeek())) {
				startDate = getWeekStartDate();
				endDate.setDay(startDate.getDay() + 6);
				endDate.setMonth(startDate.getMonth());
				endDate.setYear(startDate.getYear());
				setSelectedDateRange(Accounter.constants().thisWeek());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().thisMonth())
					&& dateRange.equals(Accounter.constants().thisMonth())) {
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				Calendar endCal = Calendar.getInstance();
				endCal.setTime(new ClientFinanceDate().getDateAsObject());
				endCal.set(Calendar.DAY_OF_MONTH,
						endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
				endDate = new ClientFinanceDate(endCal.getTime());
				setSelectedDateRange(Accounter.constants().thisMonth());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().lastWeek())
					&& dateRange.equals(Accounter.constants().lastWeek())) {

				endDate = getWeekStartDate();
				endDate.setDay(endDate.getDay() - 1);
				startDate = new ClientFinanceDate(endDate.getDate());
				startDate.setDay(startDate.getDay() - 6);

				setSelectedDateRange(Accounter.constants().lastWeek());

			} else if (!getSelectedDateRange().equals(
					Accounter.constants().thisFinancialYear())
					&& dateRange.equals(Accounter.constants()
							.thisFinancialYear())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				setSelectedDateRange(Accounter.constants().thisFinancialYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().lastFinancialYear())
					&& dateRange.equals(Accounter.constants()
							.lastFinancialYear())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				startDate.setYear(startDate.getYear() - 1);
				Calendar endCal = Calendar.getInstance();
				endCal.setTime(Accounter.getCompany()
						.getCurrentFiscalYearEndDate().getDateAsObject());
				endCal.set(Calendar.DAY_OF_MONTH,
						endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
				endDate = new ClientFinanceDate(endCal.getTime());
				endDate.setYear(endDate.getYear() - 1);
				// startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				// endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
				setSelectedDateRange(Accounter.constants().lastFinancialYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().thisFinancialQuarter())
					&& dateRange.equals(Accounter.constants()
							.thisFinancialQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.constants()
						.thisFinancialQuarter());
				getCurrentFiscalYearQuarter();
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().lastFinancialQuarter())
					&& dateRange.equals(Accounter.constants()
							.lastFinancialQuarter())) {
				getCurrentFiscalYearQuarter();
				Calendar startDateCal = Calendar.getInstance();
				startDateCal.setTime(startDate.getDateAsObject());
				startDateCal.set(Calendar.MONTH,
						startDateCal.get(Calendar.MONTH) - 3);
				Calendar endDateCal = Calendar.getInstance();
				endDateCal.setTime(endDate.getDateAsObject());
				endDateCal.set(Calendar.MONTH,
						endDateCal.get(Calendar.MONTH) - 3);
				startDate = new ClientFinanceDate(startDateCal.getTime());
				endDate = new ClientFinanceDate(endDateCal.getTime());
				setSelectedDateRange(Accounter.constants()
						.lastFinancialQuarter());
				// getCurrentQuarter();
				// startDate.setYear(startDate.getYear() - 1);
				// endDate.setYear(endDate.getYear() - 1);

			} else if (!getSelectedDateRange().equals(
					Accounter.constants().financialYearToDate())
					&& dateRange.equals(Accounter.constants()
							.financialYearToDate())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.constants()
						.financialYearToDate());
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().thisVATQuarter())
					&& dateRange.equals(Accounter.constants().thisVATQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				setSelectedDateRange(Accounter.constants().thisVATQuarter());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().thisVATQuarterToDate())
					&& dateRange.equals(Accounter.constants()
							.thisVATQuarterToDate())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.constants()
						.thisVATQuarterToDate());
				getCurrentQuarter();
				endDate = new ClientFinanceDate();

			} else if (!getSelectedDateRange().equals(
					Accounter.constants().lastVATQuarter())
					&& dateRange.equals(Accounter.constants().lastVATQuarter())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.constants().lastVATQuarter());
				getPreviousQuarter();

			} else if (!getSelectedDateRange().equals(
					Accounter.constants().lastVATQuarterToDate())
					&& dateRange.equals(Accounter.constants()
							.lastVATQuarterToDate())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.constants()
						.lastVATQuarterToDate());
				getPreviousQuarter();
				endDate = new ClientFinanceDate();
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().nextVATQuarter())
					&& dateRange.equals(Accounter.constants().nextVATQuarter())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.constants().nextVATQuarter());
				getNextQuarter();
			} else if (!getSelectedDateRange().equals(
					Accounter.constants().custom())
					&& dateRange.equals(Accounter.constants().custom())) {
				startDate = this.getStartDate();
				endDate = this.getEndDate();
				setSelectedDateRange(Accounter.constants().custom());
			}
			setStartDate(startDate);
			setEndDate(endDate);
			changeDates(startDate, endDate);
		} catch (Exception e) {
		}
	}

	public void getCurrentFiscalYearQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();
		ClientFinanceDate start = Accounter.getCompany()
				.getCurrentFiscalYearStartDate();
		ClientFinanceDate end = Accounter.getCompany()
				.getCurrentFiscalYearEndDate();

		int currentQuarter = 0;
		ClientFinanceDate quarterStart = Accounter.getCompany()
				.getCurrentFiscalYearStartDate();
		ClientFinanceDate quarterEnd;

		Calendar quarterEndCal = Calendar.getInstance();
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());

		if (date.after(quarterStart) && date.before(quarterEnd)) {
			currentQuarter = 1;
		}
		quarterStart.setMonth(quarterStart.getMonth() + 3);
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());
		if (date.after(quarterStart) && date.before(quarterEnd)) {
			currentQuarter = 2;
		}
		quarterStart.setMonth(quarterStart.getMonth() + 3);
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());
		if (date.after(quarterStart) && date.before(quarterEnd)) {
			currentQuarter = 3;
		}
		quarterStart.setMonth(quarterStart.getMonth() + 3);
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());
		if (date.after(quarterStart) && date.before(quarterEnd)) {
			currentQuarter = 4;
		}
		switch (currentQuarter) {
		case 1:
			startDate = start;
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(start.getDateAsObject());
			endCal.set(Calendar.MONTH, endCal.get(Calendar.MONTH) + 3);
			endCal.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal.getTime());
			break;

		case 2:
			startDate = start;
			startDate.setMonth(start.getMonth() + 3);
			Calendar endCal2 = Calendar.getInstance();
			endCal2.setTime(startDate.getDateAsObject());
			endCal2.set(Calendar.MONTH, endCal2.get(Calendar.MONTH) + 3);
			endCal2.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal2.getTime());
			break;

		case 3:
			startDate = start;
			startDate.setMonth(start.getMonth() + 6);
			Calendar endCal3 = Calendar.getInstance();
			endCal3.setTime(startDate.getDateAsObject());
			endCal3.set(Calendar.MONTH, endCal3.get(Calendar.MONTH) + 3);
			endCal3.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal3.getTime());
			break;
		default:
			startDate = start;
			startDate.setMonth(start.getMonth() + 9);
			endDate = end;
			break;
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
		int day = date.getDay() % 6;
		ClientFinanceDate newDate = new ClientFinanceDate();
		if (day != 1) {
			newDate.setDay(date.getDay() - day);
		} else {
			newDate.setDay(date.getDay());
		}

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
		var date = new ClientFinanceDate();
		var day = date.getDay();
		var remainingDays = 6 - day;
		var newDate = new ClientFinanceDate();
		newDate.setDate(date.getDate() + remainingDays);
		var tmp = newDate.getTime();
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
