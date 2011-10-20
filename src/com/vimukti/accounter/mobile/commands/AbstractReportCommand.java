package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;

public abstract class AbstractReportCommand<T> extends AbstractCommand {
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
	private static final String CUSTOMERS = "Customers";

	@Override
	protected abstract void addRequirements(List<Requirement> list);

	/**
	 * 3 requirements of many report...
	 * 
	 * @param list
	 */
	protected void add3ReportRequirements(List<Requirement> list) {
		list.add(new Requirement(DATE_RANGE, true, true));
		list.add(new Requirement(FROM_DATE, true, true));
		list.add(new Requirement(TO_DATE, true, true));
	}

	@Override
	public abstract String getId();

	@Override
	public Result run(Context context) {
		setOptionalFields();
		Result reportResult = clickOnRecord(context);
		if (reportResult != null) {
			return reportResult;
		}
		reportResult = createReqReportRecord(reportResult, context);
		if (reportResult != null) {
			return reportResult;
		}
		getReportResult(reportResult, context);
		return reportResult;
	}

	private Result clickOnRecord(Context context) {
		T selection = context.getSelection(RECORDS);
		if (selection != null) {
			return createOpenRecord(selection, context);
		}
		return null;
	}

	protected Result createOpenRecord(T selection, Context context) {
		Result result = context.makeResult();
		CommandList commandList = new CommandList();
		addCommandOnRecordClick(selection, commandList);
		result.add(commandList);
		return result;
	}

	protected abstract void addCommandOnRecordClick(T selection,
			CommandList commandList);

	@SuppressWarnings("unchecked")
	protected void getReportResult(Result plReportResult, Context context) {

		ResultList recordsList = new ResultList(RECORDS);
		Object last = context.getLast(RequirementType.BASEREPORT);
		int num = 0;
		if (last != null) {
			recordsList.add(createReportRecord((T) last));
			num++;
		}
		List<T> records = (List<T>) getRecords(context.getHibernateSession());
		for (T record : records) {
			if (record != last) {
				recordsList.add(createReportRecord(record));
				num++;
			}
			if (num == REPORTS_TO_SHOW) {
				break;
			}
		}
		int size = recordsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a record to view its details");
		}

		plReportResult.add(message.toString());
		plReportResult.add(recordsList);
	}

	/**
	 * Creating report record to show user...
	 * 
	 * @param record
	 * @return
	 */
	protected abstract Record createReportRecord(T record);

	/**
	 * get report records based on session..
	 * 
	 * @param session
	 * @return
	 */
	protected abstract List<T> getRecords(Session session);

	/**
	 * creating date range based report requirement
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	protected Result dateRangeRequirement(Context context, ResultList list,
			Object selection) {
		Requirement dateRangeReq = get(DATE_RANGE);
		String range = (String) dateRangeReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(DATE_RANGE)) {
			String dateRange = context.getSelection(TEXT);
			if (dateRange == null) {
				dateRange = context.getString();
			}
			range = dateRange;
			dateRangeReq.setValue(range);
		}
		if (selection == range) {
			context.setAttribute(INPUT_ATTR, DATE_RANGE);
			return text(context, "Enter date range", range);
		}

		Record transDateRecord = new Record(range);
		transDateRecord.add("Name", "Date Range");
		transDateRecord.add("Value", range.toString());
		list.add(transDateRecord);
		return null;
	}

	/**
	 * Creating from date based on report requirement...
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	protected Result fromDateRequirement(Context context, ResultList list,
			Object selection) {
		Requirement dateReq = get(FROM_DATE);
		Date fromDate = (Date) dateReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(FROM_DATE)) {
			Date date = context.getSelection(DATE);
			// if (date == null) {
			// date = context.getDate();
			// }
			fromDate = date;
			dateReq.setValue(fromDate);
		}
		if (selection == fromDate) {
			// context.setAttribute(INPUT_ATTR, FROM_DATE);
			// return date(context, "Enter From Date", fromDate);
		}

		Record transDateRecord = new Record(fromDate);
		transDateRecord.add("Name", "From Date");
		transDateRecord.add("Value", fromDate.toString());
		list.add(transDateRecord);
		return null;
	}

	/**
	 * Creating To date based on report requirement...
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */

	protected Result toDateRequirement(Context context, ResultList list,
			Object selection) {
		Requirement dateReq = get(TO_DATE);
		Date toDate = (Date) dateReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(TO_DATE)) {
			Date date = context.getSelection(DATE);
			// if (date == null) {
			// date = context.getDate();
			// }
			toDate = date;
			dateReq.setValue(toDate);
		}
		if (selection == toDate) {
			// context.setAttribute(INPUT_ATTR, TO_DATE);
			// return date(context, "Enter To Date", toDate);
		}

		Record transDateRecord = new Record(toDate);
		transDateRecord.add("Name", "To Date");
		transDateRecord.add("Value", toDate.toString());
		list.add(transDateRecord);
		return null;
	}

	/**
	 * Creating VAT agency based on report requirement..
	 * 
	 * @param context
	 * @return
	 */

	protected Result vatAgencyRequirement(Context context) {
		Requirement vatAgencyReq = get(TAX_AGENCY);
		TAXAgency taxAgency = context.getSelection(VAT_AGENCIES);
		if (!vatAgencyReq.isDone()) {
			if (taxAgency != null) {
				vatAgencyReq.setValue(taxAgency);
			} else {
				return vatAgencies(context);
			}
		}
		if (taxAgency != null) {
			vatAgencyReq.setValue(taxAgency);
		}
		return null;
	}

	/**
	 * creating VAT agencies list based context to show user..
	 * 
	 * @param context
	 * @return
	 */
	protected Result vatAgencies(Context context) {
		Result result = context.makeResult();
		ResultList vatAgencysList = new ResultList(VAT_AGENCIES);

		Object last = context.getLast(RequirementType.TAXAGENCY);
		int num = 0;
		if (last != null) {
			vatAgencysList.add(createVatAgencyRecord((TAXAgency) last));
			num++;
		}
		List<TAXAgency> vatAgencys = getVatAgencies(context
				.getHibernateSession());
		for (TAXAgency vatAgency : vatAgencys) {
			if (vatAgency != last) {
				vatAgencysList.add(createVatAgencyRecord(vatAgency));
				num++;
			}
			if (num == VATAGENCIES_TO_SHOW) {
				break;
			}
		}
		int size = vatAgencysList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select a VAT Agency");
		}

		result.add(message.toString());
		result.add(vatAgencysList);
		result.add("Type for VAT Agency");
		return result;
	}

	/**
	 * creating VAT agency record to show user..
	 * 
	 * @param vatAgency
	 * @return
	 */
	private Record createVatAgencyRecord(TAXAgency vatAgency) {
		Record record = new Record(vatAgency);
		record.add("Name", vatAgency.getName());
		return record;
	}

	/**
	 * get VAT agencies based on session which is passed..
	 * 
	 * @param session
	 * @return
	 */
	private List<TAXAgency> getVatAgencies(Session session) {
		return null;
	}

	/**
	 * creating status lists for reports requirements
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	protected Result statusRequirement(Context context, ResultList list,
			Object selection) {
		Requirement statusReq = get(STATUS);
		String status = (String) statusReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(STATUS)) {
			String dateRange = context.getSelection(STATUS);
			if (dateRange == null) {
				dateRange = context.getString();
			}
			status = dateRange;
			statusReq.setValue(status);
		}
		if (selection == status) {
			context.setAttribute(INPUT_ATTR, STATUS);
			return text(context, "Enter status", status);
		}

		Record transDateRecord = new Record(status);
		transDateRecord.add("Name", "Status");
		transDateRecord.add("Value", status.toString());
		list.add(transDateRecord);
		return null;
	}

	/**
	 * creating expense type list for reports requirements
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	protected Result expenseTypeRequirement(Context context, ResultList list,
			Object selection) {
		Requirement expenseTypeReq = get(EXPENSE_TYPE);
		String expenseType = (String) expenseTypeReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(EXPENSE_TYPE)) {
			String dateRange = context.getSelection(EXPENSE_TYPE);
			if (dateRange == null) {
				dateRange = context.getString();
			}
			expenseType = dateRange;
			expenseTypeReq.setValue(expenseType);
		}
		if (selection == expenseType) {
			context.setAttribute(INPUT_ATTR, EXPENSE_TYPE);
			return text(context, "Enter Expense Type", expenseType);
		}

		Record transDateRecord = new Record(expenseType);
		transDateRecord.add("Name", "Expense Type");
		transDateRecord.add("Value", expenseType.toString());
		list.add(transDateRecord);
		return null;
	}

	/**
	 * creating required fields for reports
	 * 
	 * @param reportResult
	 * @param context
	 * @return
	 */
	protected Result createReqReportRecord(Result reportResult, Context context) {
		ResultList resultList = new ResultList("values");

		// Checking whether date range is there or not and returning result
		String selection = context.getSelection("values");

		Result result = dateRangeRequirement(context, resultList, selection);
		if (result != null) {
			return result;
		}
		// Checking whether from date is there or not and returning result
		result = fromDateRequirement(context, resultList, selection);
		if (result != null) {
			return result;
		}
		// Checking whether to date is there or not and returning result
		result = toDateRequirement(context, resultList, selection);
		if (result != null) {
			return result;
		}

		reportResult.add(resultList);
		return null;
	}

	protected Result customerRequirement(Context context) {
		Requirement customerReq = get(CUSTOMER);
		Customer customer = context.getSelection(CUSTOMERS);
		if (customer != null) {
			customerReq.setValue(customer);
		}
		if (!customerReq.isDone()) {
			return customers(context);
		}
		return null;
	}

	/**
	 * showing customers list based on context...
	 * 
	 * @param context
	 * @return
	 */
	protected Result customers(Context context) {
		Result result = context.makeResult();
		ResultList customersList = new ResultList(CUSTOMERS);

		Object last = context.getLast(RequirementType.CUSTOMER);
		int num = 0;
		if (last != null) {
			customersList.add(createCustomerRecord((Customer) last));
			num++;
		}
		List<Customer> customers = getCustomers(context.getHibernateSession());
		for (Customer customer : customers) {
			if (customer != last) {
				customersList.add(createCustomerRecord(customer));
				num++;
			}
			if (num == CUSTOMERS_TO_SHOW) {
				break;
			}
		}
		int size = customersList.size();
		if (size <= 0)
			result.add("There is no customers in your company to select.");
		result.add(customersList);
		result.add("Type for Customer");
		return result;
	}

	/**
	 * get customers from given session.
	 * 
	 * @param session
	 * @return customers list
	 */
	private List<Customer> getCustomers(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * customer record to show user when customer statement report selected
	 * */
	private Record createCustomerRecord(Customer customer) {
		Record record = new Record(customer);
		record.add("Name", customer.getName());
		record.add("Balance", customer.getBalance());
		return record;
	}

	/**
	 * start date of report..
	 * 
	 * @return
	 */
	protected Date getStartDate() {
		return null;

	}

	/**
	 * end date of report..
	 * 
	 * @return
	 */

	protected Date getEndDate() {
		return null;

	}

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

	protected void setOptionalFields() {
		setDefaultDateRange();
		setDefaultFromDate();
		setDefaultToDate();
	}

	protected void setDefaultDateRange() {
		get(DATE_RANGE).setDefaultValue(new String("ALL"));
	}

	protected void setDefaultFromDate() {
		get(FROM_DATE).setDefaultValue(new Date());
	}

	protected void setDefaultToDate() {
		get(TO_DATE).setDefaultValue(new Date());
	}

	protected void setDefaultTaxAgency() {
		get(TAX_AGENCY).setDefaultValue(new String());
	}

	protected void setDefaultStatus() {
		get(STATUS).setDefaultValue(new String("Open"));
	}

	protected void setDefaultExpenseType() {
		get(EXPENSE_TYPE).setDefaultValue(new String("Open"));
	}
}
