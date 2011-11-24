package com.vimukti.accounter.mobile.commands.reports;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.commands.NewAbstractCommand;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;

public abstract class NewAbstractReportCommand<T> extends NewAbstractCommand {
	protected static final int REPORTS_TO_SHOW = 5;
	protected static final int VATAGENCIES_TO_SHOW = 5;
	protected static final int CUSTOMERS_TO_SHOW = 5;
	protected static final String DATE_RANGE = "Date Range";
	protected static final String FROM_DATE = "From Date";
	protected static final String TO_DATE = "To Date";
	protected static final String TAX_AGENCY = "Tax Agency";
	protected static final String STATUS = "Status";
	protected static final String EXPENSE_TYPE = "Expense Type";
	protected static final String VENDOR_PREPAYMENT = "Supplier Prepayment";
	protected static final String VAT_AGENCIES = "Tax Agencies";
	protected static final String RECORDS = "records";
	protected static final String ENDING_DATE = "Ending Date";
	protected static final String CUSTOMER = "Customer";
	private String previousSelectedRange;

	/**
	 * Adds Date range,from date and to date requirements to existing
	 * requirements.
	 * 
	 * @param list
	 */
	protected void addDateRangeFromToDateRequirements(List<Requirement> list) {
		list.add(addDateRangeRequirement());
		list.add(new DateRequirement(FROM_DATE, getMessages().pleaseEnter(
				getMessages().startDate()), getMessages().startDate(), true,
				true) {
			@Override
			public void setValue(Object value) {
				super.setValue(value);
				resetandUpdateRecords();
			}
		});
		list.add(new DateRequirement(TO_DATE, getMessages().pleaseEnter(
				getMessages().endDate()), getMessages().endDate(), true, true) {
			@Override
			public void setValue(Object value) {
				super.setValue(value);
				resetandUpdateRecords();
			}
		});
	}

	protected void addFromToDateRequirements(List<Requirement> list) {
		list.add(new DateRequirement(FROM_DATE, getMessages().pleaseEnter(
				getMessages().startDate()), getMessages().startDate(), true,
				true) {
			@Override
			public void setValue(Object value) {
				super.setValue(value);
				resetandUpdateRecords();
			}
		});
		list.add(new DateRequirement(TO_DATE, getMessages().pleaseEnter(
				getMessages().endDate()), getMessages().endDate(), true, true) {
			@Override
			public void setValue(Object value) {
				super.setValue(value);
				resetandUpdateRecords();
			}
		});
	}

	protected void addDateRangeFromDateRequirements(List<Requirement> list) {
		list.add(addDateRangeRequirement());
		list.add(new DateRequirement(FROM_DATE, getMessages().pleaseEnter(
				getMessages().startDate()), getMessages().startDate(), true,
				true) {
			@Override
			public void setValue(Object value) {
				super.setValue(value);
				resetandUpdateRecords();
			}
		});
	}

	private Requirement addDateRangeRequirement() {
		return new StringListRequirement(DATE_RANGE, getMessages().pleaseEnter(
				getMessages().dateRange()), getMessages().dateRange(), true,
				true, new ChangeListner<String>() {

					@Override
					public void onSelection(String value) {
						dateRangeChanged(value);
						resetandUpdateRecords();
					}
				}) {

			@Override
			protected String getSetMessage() {
				return "";
			}

			@Override
			protected String getSelectString() {
				return null;
			}

			@Override
			protected List<String> getLists(Context context) {
				String[] dateRangeArray = { getMessages().all(),
						getMessages().thisWeek(), getMessages().thisMonth(),
						getMessages().lastWeek(), getMessages().lastMonth(),
						getMessages().thisFinancialYear(),
						getMessages().lastFinancialYear(),
						getMessages().thisFinancialQuarter(),
						getMessages().lastFinancialQuarter(),
						getMessages().financialYearToDate(),
						getMessages().custom() };
				return Arrays.asList(dateRangeArray);
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		};
	}

	protected void dateRangeChanged(String dateRange) {
		if (previousSelectedRange == null) {
			previousSelectedRange = "";
		}
		List<ClientFinanceDate> minimumAndMaximumDates = CommandUtils
				.getMinimumAndMaximumTransactionDate(getCompanyId());
		if (minimumAndMaximumDates.isEmpty()) {
			return;
		}
		ClientFinanceDate startDate = get(FROM_DATE).getValue();
		ClientFinanceDate endDate = get(TO_DATE).getValue();
		Map<String, Object> dateRangeChanged = CommandUtils.dateRangeChanged(
				getMessages(), dateRange, previousSelectedRange,
				getPreferences(), startDate, endDate,
				minimumAndMaximumDates.get(0));
		startDate = (ClientFinanceDate) dateRangeChanged.get("startDate");
		get(FROM_DATE).setValue(startDate);
		endDate = (ClientFinanceDate) dateRangeChanged.get("endDate");
		get(TO_DATE).setValue(endDate);
		// boolean isDateChanges = (Boolean)
		// dateRangeChanged.get("isDateChanges");
		previousSelectedRange = (String) dateRangeChanged
				.get("selectedDateRange");
	}

	protected void resetandUpdateRecords() {
	}

	@Override
	public String getId() {
		return null;
	}

	/**
	 * get report records based on session..
	 * 
	 * @param session
	 * @return
	 */
	protected abstract List<T> getRecords();

	/**
	 * to get last date of the month.
	 **/
	public FinanceDate getLastMonth(FinanceDate date) {
		int month = date.getMonth() - 1;
		int year = date.getYear();

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
		return new FinanceDate(date.getYear(), date.getMonth() - 1, lastDay);
		// return lastDay;
	}

	protected int getType(TransactionDetailByAccount record) {
		if (record.getTransactionType() == 11) {
			return (record.getMemo() != null && record.getMemo()
					.equalsIgnoreCase(VENDOR_PREPAYMENT)) ? ClientTransaction.TYPE_VENDOR_PAYMENT
					: ClientTransaction.TYPE_PAY_BILL;
		}

		return record.getTransactionType();
	}

	protected abstract String addCommandOnRecordClick(T selection);

	protected abstract Record createReportRecord(T record);

	protected FinanceDate getStartDate() {
		return get(FROM_DATE) == null ? new FinanceDate() : new FinanceDate(
				(ClientFinanceDate) get(FROM_DATE).getValue());
	}

	protected FinanceDate getEndDate() {
		return get(TO_DATE) == null ? new FinanceDate() : new FinanceDate(
				(ClientFinanceDate) get(TO_DATE).getValue());
	}

	@Override
	protected void setDefaultValues(Context context) {
		if (get(FROM_DATE) != null) {
			get(FROM_DATE).setValue(
					CommandUtils.getCurrentFiscalYearStartDate(context
							.getPreferences()));
		}
		if (get(TO_DATE) != null) {
			get(TO_DATE).setValue(new ClientFinanceDate());
		}
		if (get(DATE_RANGE) != null) {
			get(DATE_RANGE).setValue(getMessages().all());
		}
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(getShowListRequirement());
	}

	private Requirement getShowListRequirement() {
		return new ShowListRequirement<T>(RECORDS, getSelectRecordString(),
				REPORTS_TO_SHOW) {

			@Override
			protected String onSelection(T value) {
				return NewAbstractReportCommand.this
						.addCommandOnRecordClick(value);
			}

			@Override
			protected String getShowMessage() {
				return NewAbstractReportCommand.this.getShowMessage();
			}

			@Override
			protected String getEmptyString() {
				return NewAbstractReportCommand.this.getEmptyString();
			}

			@Override
			protected Record createRecord(T value) {
				return createReportRecord(value);
			}

			@Override
			protected void setCreateCommand(CommandList list) {

			}

			@Override
			protected boolean filter(T e, String name) {
				return true;
			}

			@Override
			protected List<T> getLists(Context context) {
				return getRecords();
			}
		};
	}

	protected abstract String getEmptyString();

	protected abstract String getShowMessage();

	protected abstract String getSelectRecordString();
}
