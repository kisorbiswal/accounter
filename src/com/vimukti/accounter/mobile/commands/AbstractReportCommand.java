package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public abstract class AbstractReportCommand<T> extends AbstractCommand {
	protected static final int REPORTS_TO_SHOW = 5;
	protected static final int VATAGENCIES_TO_SHOW = 5;
	private static final int CUSTOMERS_TO_SHOW = 5;

	@Override
	protected abstract void addRequirements(List<Requirement> list);

	protected void add3ReportRequirements(List<Requirement> list) {
		list.add(new Requirement("dateRange", true, true));
		list.add(new Requirement("fromDate", true, true));
		list.add(new Requirement("toDate", true, true));
	}

	@Override
	public abstract String getId();

	@Override
	public Result run(Context context) {
		Result reportResult = context.makeResult();
		reportResult = createReqReportRecord(reportResult, context);
		getReportResult(reportResult, context);
		return reportResult;
	}

	@SuppressWarnings("unchecked")
	protected void getReportResult(Result plReportResult, Context context) {

		ResultList recordsList = new ResultList("values");
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

	protected abstract Record createReportRecord(T record);

	protected abstract List<T> getRecords(Session session);

	protected Result dateRangeRequirement(Context context, ResultList list,
			Object selection) {
		Requirement dateRangeReq = get("dateRange");
		String range = (String) dateRangeReq.getValue();
		String attribute = (String) context.getAttribute("input");
		if (attribute.equals("dateRange")) {
			String dateRange = context.getSelection("dateRanges");
			if (dateRange == null) {
				dateRange = context.getString();
			}
			range = dateRange;
			dateRangeReq.setValue(range);
		}
		if (selection == range) {
			context.setAttribute("input", "fromDate");
			return text(context, "Enter date range", range);
		}

		Record transDateRecord = new Record(range);
		transDateRecord.add("Name", "Date Range");
		transDateRecord.add("Value", range.toString());
		list.add(transDateRecord);
		return null;
	}

	protected Result fromDateRequirement(Context context, ResultList list,
			Object selection) {
		Requirement dateReq = get("fromDate");
		Date fromDate = (Date) dateReq.getValue();
		String attribute = (String) context.getAttribute("input");
		if (attribute.equals("fromDate")) {
			Date date = context.getSelection(DATE);
			if (date == null) {
				date = context.getDate();
			}
			fromDate = date;
			dateReq.setValue(fromDate);
		}
		if (selection == fromDate) {
			context.setAttribute("input", "fromDate");
			return date(context, "Enter From Date", fromDate);
		}

		Record transDateRecord = new Record(fromDate);
		transDateRecord.add("Name", "From Date");
		transDateRecord.add("Value", fromDate.toString());
		list.add(transDateRecord);
		return null;
	}

	protected Result toDateRequirement(Context context, ResultList list,
			Object selection) {
		Requirement dateReq = get("toDate");
		Date toDate = (Date) dateReq.getValue();
		String attribute = (String) context.getAttribute("input");
		if (attribute.equals("toDate")) {
			Date date = context.getSelection(DATE);
			if (date == null) {
				date = context.getDate();
			}
			toDate = date;
			dateReq.setValue(toDate);
		}
		if (selection == toDate) {
			context.setAttribute("input", "toDate");
			return date(context, "Enter To Date", toDate);
		}

		Record transDateRecord = new Record(toDate);
		transDateRecord.add("Name", "To Date");
		transDateRecord.add("Value", toDate.toString());
		list.add(transDateRecord);
		return null;
	}

	protected Result vatAgencyRequirement(Context context) {
		Requirement vatAgencyReq = get("vatAgency");
		TAXAgency taxAgency = context.getSelection("vatAgencies");
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

	protected Result vatAgencies(Context context) {
		Result result = context.makeResult();
		ResultList vatAgencysList = new ResultList("vatAgencies");

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

	private Record createVatAgencyRecord(TAXAgency vatAgency) {
		Record record = new Record(vatAgency);
		record.add("Name", vatAgency.getName());
		record.add("Payment Term", vatAgency.getPaymentTerm());
		record.add("VAT Return", vatAgency.getVATReturn());
		record.add("Sales Liability Account",
				vatAgency.getSalesLiabilityAccount());
		record.add("Purchase Liability Account",
				vatAgency.getPurchaseLiabilityAccount());
		return record;
	}

	private List<TAXAgency> getVatAgencies(Session session) {
		return null;
	}

	protected Result statusRequirement(Context context, ResultList list,
			Object selection) {
		Requirement statusReq = get("status");
		String status = (String) statusReq.getValue();
		String attribute = (String) context.getAttribute("input");
		if (attribute.equals("status")) {
			String dateRange = context.getSelection("status");
			if (dateRange == null) {
				dateRange = context.getString();
			}
			status = dateRange;
			statusReq.setValue(status);
		}
		if (selection == status) {
			context.setAttribute("input", "status");
			return text(context, "Enter status", status);
		}

		Record transDateRecord = new Record(status);
		transDateRecord.add("Name", "Status");
		transDateRecord.add("Value", status.toString());
		list.add(transDateRecord);
		return null;
	}

	protected Result expenseTypeRequirement(Context context, ResultList list,
			Object selection) {
		Requirement expenseTypeReq = get("expenseType");
		String expenseType = (String) expenseTypeReq.getValue();
		String attribute = (String) context.getAttribute("input");
		if (attribute.equals("expenseType")) {
			String dateRange = context.getSelection("expenseType");
			if (dateRange == null) {
				dateRange = context.getString();
			}
			expenseType = dateRange;
			expenseTypeReq.setValue(expenseType);
		}
		if (selection == expenseType) {
			context.setAttribute("input", "expenseType");
			return text(context, "Enter Expense Type", expenseType);
		}

		Record transDateRecord = new Record(expenseType);
		transDateRecord.add("Name", "Expense Type");
		transDateRecord.add("Value", expenseType.toString());
		list.add(transDateRecord);
		return null;
	}

	protected Result createReqReportRecord(Result reportResult, Context context) {
		ResultList resultList = new ResultList("values");

		// Checking whether date range is there or not and returning result
		Requirement reportReq = get("dateRange");
		String dateRange = (String) reportReq.getValue();
		String selectionDateRange = context.getSelection("values");

		if (dateRange == selectionDateRange)
			return dateRangeRequirement(context, resultList, selectionDateRange);

		// Checking whether from date is there or not and returning result
		Requirement fromDateReq = get("fromDate");
		Date fromDate = (Date) fromDateReq.getValue();
		Date selectionfromDate = context.getSelection("values");

		if (fromDate == selectionfromDate)
			return fromDateRequirement(context, resultList, selectionfromDate);

		// Checking whether to date is there or not and returning result
		Requirement toDateReq = get("toDate");
		Date toDate = (Date) toDateReq.getValue();
		Date selectiontoDate = context.getSelection("values");
		if (toDate == selectiontoDate)
			return toDateRequirement(context, resultList, selectiontoDate);

		return reportResult;
	}

	protected Result customerRequirement(Context context) {
		Requirement customerReq = get("customer");
		Customer customer = context.getSelection("customers");
		if (customer != null) {
			customerReq.setValue(customer);
		}
		if (!customerReq.isDone()) {
			return customers(context);
		}
		return null;
	}

	protected Result customers(Context context) {
		Result result = context.makeResult();
		ResultList customersList = new ResultList("customers");

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

	private List<Customer> getCustomers(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	private Record createCustomerRecord(Customer customer) {
		Record record = new Record(customer);
		record.add("Name", customer.getName());
		record.add("Balance", customer.getBalance());
		return record;
	}

}
