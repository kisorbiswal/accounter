package com.vimukti.accounter.web.client.ui.reports;

import java.util.Date;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
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

		startDate = Accounter.getStartDate();
		endDate = Accounter.getCompany().getLastandOpenedFiscalYearEndDate();
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
					Accounter.getReportsMessages().all())
					&& dateRange.equals(Accounter.getReportsMessages().all())) {
				setSelectedDateRange(Accounter.getReportsMessages().all());
				// startDate = FinanceApplication.getStartDate();
				// endDate = Utility.getLastandOpenedFiscalYearEndDate();
				// if (endDate == null)
				// endDate = new ClientFinanceDate();
				startDate = new ClientFinanceDate(0);
				endDate = new ClientFinanceDate(0);

			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().today())
					&& dateRange.equals(Accounter.getReportsMessages().today())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany()
						.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.getReportsMessages().today());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().endThisWeek())
					&& dateRange.equals(Accounter.getReportsMessages()
							.endThisWeek())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate(new Date(
						(long) getWeekEndDate()));
				setSelectedDateRange(Accounter.getReportsMessages()
						.endThisWeek());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().endThisWeekToDate())
					&& dateRange.equals(Accounter.getReportsMessages()
							.endThisWeekToDate())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.getReportsMessages()
						.endThisWeekToDate());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().endThisMonth())
					&& dateRange.equals(Accounter.getReportsMessages()
							.endThisMonth())) {
				int lastDay = getMonthLastDate(date.getMonth(), date.getYear());
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				endDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), lastDay);
				setSelectedDateRange(Accounter.getReportsMessages()
						.endThisMonth());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().endThisMonthToDate())
					&& dateRange.equals(Accounter.getReportsMessages()
							.endThisMonthToDate())) {
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				endDate = Accounter.getCompany()
						.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.getReportsMessages()
						.endThisMonthToDate());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().endThisFiscalQuarter())
					&& dateRange.equals(Accounter.getReportsMessages()
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
				setSelectedDateRange(Accounter.getReportsMessages()
						.endThisFiscalQuarter());
			} else if (!getSelectedDateRange()
					.equals(Accounter.getReportsMessages()
							.endThisFiscalQuarterToDate())
					&& dateRange.equals(Accounter.getReportsMessages()
							.endThisFiscalQuarterToDate())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				@SuppressWarnings("unused")
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = Accounter.getCompany()
						.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.getReportsMessages()
						.endThisFiscalQuarterToDate());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().endThisCalanderQuarter())
					&& dateRange.equals(Accounter.getReportsMessages()
							.endThisCalanderQuarter())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
				setSelectedDateRange(Accounter.getReportsMessages()
						.endThisCalanderQuarter());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages()
							.endThisCalanderQuarterToDate())
					&& dateRange.equals(Accounter.getReportsMessages()
							.endThisCalanderQuarterToDate())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = Accounter.getCompany()
						.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.getReportsMessages()
						.endThisCalanderQuarterToDate());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().endThisFiscalYear())
					&& dateRange.equals(Accounter.getReportsMessages()
							.endThisFiscalYear())) {

				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);
				setSelectedDateRange(Accounter.getReportsMessages()
						.endThisFiscalYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().endThisFiscalYearToDate())
					&& dateRange.equals(Accounter.getReportsMessages()
							.endThisFiscalYearToDate())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = Accounter.getCompany()
						.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.getReportsMessages()
						.endThisFiscalYearToDate());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().endThisCalanderYear())
					&& dateRange.equals(Accounter.getReportsMessages()
							.endThisCalanderYear())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);
				setSelectedDateRange(Accounter.getReportsMessages()
						.endThisCalanderYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().endThisCalanderYearToDate())
					&& dateRange.equals(Accounter.getReportsMessages()
							.endThisCalanderYearToDate())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = Accounter.getCompany()
						.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.getReportsMessages()
						.endThisCalanderYearToDate());
				changeDates(startDate, endDate);
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().endYesterday())
					&& dateRange.equals(Accounter.getReportsMessages()
							.endYesterday())) {
				// startDate = new ClientFinanceDate(date.getYear(),
				// date.getMonth(), date
				// .getDate() - 1);
				startDate = Accounter.getStartDate();
				endDate = Accounter.getCompany()
						.getLastandOpenedFiscalYearEndDate();
				int day = endDate.getDay();
				endDate.setDate(day - 1);
				setSelectedDateRange((Accounter.getReportsMessages()
						.endYesterday()));
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().endPreviousFiscalQuarter())
					&& dateRange.equals(Accounter.getReportsMessages()
							.endPreviousFiscalQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany()
						.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.getReportsMessages()
						.endPreviousFiscalQuarter());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().endLastCalenderQuarter())
					&& dateRange.equals(Accounter.getReportsMessages()
							.endLastCalenderQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany()
						.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.getReportsMessages()
						.endLastCalenderQuarter());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages()
							.previousFiscalYearSameDates())
					&& dateRange.equals(Accounter.getReportsMessages()
							.previousFiscalYearSameDates())) {
				setSelectedDateRange(Accounter.getReportsMessages()
						.previousFiscalYearSameDates());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages()
							.previousFiscalYearSameDates())
					&& dateRange.equals(Accounter.getReportsMessages()
							.previousFiscalYearSameDates())) {

				startDate = new ClientFinanceDate(this.startDate.getYear() - 1,
						this.startDate.getMonth(), this.startDate.getDate());
				endDate = new ClientFinanceDate(this.endDate.getYear() - 1,
						this.endDate.getMonth(), this.endDate.getDate());
				// startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				// endDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
				setSelectedDateRange(Accounter.getReportsMessages()
						.previousFiscalYearSameDates());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().lastCalenderYear())
					&& dateRange.equals(Accounter.getReportsMessages()
							.lastCalenderYear())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);

				setSelectedDateRange(Accounter.getReportsMessages()
						.lastCalenderYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().previousCalenderYear())
					&& dateRange.equals(Accounter.getReportsMessages()
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
				setSelectedDateRange(Accounter.getReportsMessages()
						.previousCalenderYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().lastMonth())
					&& dateRange.equals(Accounter.getReportsMessages()
							.lastMonth())) {
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
				setSelectedDateRange(Accounter.getReportsMessages().lastMonth());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().last3Months())
					&& dateRange.equals(Accounter.getReportsMessages()
							.last3Months())) {
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
				setSelectedDateRange(Accounter.getReportsMessages()
						.last3Months());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().last6Months())
					&& dateRange.equals(Accounter.getReportsMessages()
							.last6Months())) {
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
				setSelectedDateRange(Accounter.getReportsMessages()
						.last6Months());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().lastYear())
					&& dateRange.equals(Accounter.getReportsMessages()
							.lastYear())) {
				startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
				setSelectedDateRange(Accounter.getReportsMessages().lastYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().present())
					&& dateRange.equals(Accounter.getReportsMessages()
							.present())) {
				startDate = new ClientFinanceDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.getReportsMessages().present());

			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().untilEndOfYear())
					&& dateRange.equals(Accounter.getReportsMessages()
							.untilEndOfYear())) {
				startDate = new ClientFinanceDate();
				endDate = new ClientFinanceDate(startDate.getYear(), 11, 31);
				setSelectedDateRange(Accounter.getReportsMessages()
						.untilEndOfYear());

			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().thisWeek())
					&& dateRange.equals(Accounter.getReportsMessages()
							.thisWeek())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.getReportsMessages().thisWeek());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().thisMonth())
					&& dateRange.equals(Accounter.getReportsMessages()
							.thisMonth())) {
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.getReportsMessages().thisMonth());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().lastWeek())
					&& dateRange.equals(Accounter.getReportsMessages()
							.lastWeek())) {

				endDate = getWeekStartDate();
				endDate.setDate(endDate.getDate() - 1);
				startDate = new ClientFinanceDate(endDate.getTime());
				startDate.setDate(startDate.getDate() - 6);

				setSelectedDateRange(Accounter.getReportsMessages().lastWeek());

			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().thisFinancialYear())
					&& dateRange.equals(Accounter.getReportsMessages()
							.thisFinancialYear())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				setSelectedDateRange(Accounter.getReportsMessages()
						.thisFinancialYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().lastFinancialYear())
					&& dateRange.equals(Accounter.getReportsMessages()
							.lastFinancialYear())) {
				startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
				setSelectedDateRange(Accounter.getReportsMessages()
						.lastFinancialYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().thisFinancialQuarter())
					&& dateRange.equals(Accounter.getReportsMessages()
							.thisFinancialQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany()
						.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.getReportsMessages()
						.thisFinancialQuarter());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().lastFinancialQuarter())
					&& dateRange.equals(Accounter.getReportsMessages()
							.lastFinancialQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany()
						.getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.getReportsMessages()
						.lastFinancialQuarter());
				getCurrentQuarter();
				startDate.setYear(startDate.getYear() - 1);
				endDate.setYear(endDate.getYear() - 1);

			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().financialYearToDate())
					&& dateRange.equals(Accounter.getReportsMessages()
							.financialYearToDate())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.getReportsMessages()
						.financialYearToDate());
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().thisVATQuarter())
					&& dateRange.equals(Accounter.getReportsMessages()
							.thisVATQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				setSelectedDateRange(Accounter.getReportsMessages()
						.thisVATQuarter());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().thisVATQuarterToDate())
					&& dateRange.equals(Accounter.getReportsMessages()
							.thisVATQuarterToDate())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.getReportsMessages()
						.thisVATQuarterToDate());
				getCurrentQuarter();
				endDate = new ClientFinanceDate();

			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().lastVATQuarter())
					&& dateRange.equals(Accounter.getReportsMessages()
							.lastVATQuarter())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.getReportsMessages()
						.lastVATQuarter());
				getPreviousQuarter();

			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().lastVATQuarterToDate())
					&& dateRange.equals(Accounter.getReportsMessages()
							.lastVATQuarterToDate())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.getReportsMessages()
						.lastVATQuarterToDate());
				getPreviousQuarter();
				endDate = new ClientFinanceDate();
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().nextVATQuarter())
					&& dateRange.equals(Accounter.getReportsMessages()
							.nextVATQuarter())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.getReportsMessages()
						.nextVATQuarter());
				getNextQuarter();
			} else if (!getSelectedDateRange().equals(
					Accounter.getReportsMessages().custom())
					&& dateRange
							.equals(Accounter.getReportsMessages().custom())) {
				startDate = this.getStartDate();
				endDate = this.getEndDate();
				setSelectedDateRange(Accounter.getReportsMessages().custom());
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
