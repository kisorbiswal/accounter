package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class InvoiceListCommand extends AbstractTransactionCommand {

	private static final String CURRENT_VIEW = "Current View";
	private static final String FROM_DATE = "From";
	private static final String TO_DATE = "To";
	private static final String DATE = "Date";

	private static final String OPEN = "Open";
	private static final String ALL = "All";
	private static final String VOIDED = "Voided";
	private static final String OVER_DUE = "OverDue";
	private static final int ITEMS_TO_VIEW = 4;
	private static final int DATE_ITEMS_TO_VIEW = 11;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(CURRENT_VIEW, true, true));
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(FROM_DATE, true, true));
		list.add(new Requirement(TO_DATE, true, true));
	}

	@Override
	public Result run(Context context) {
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result result = null;
		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	public void setDefaultValues() {

		get(CURRENT_VIEW).setDefaultValue(OPEN);
		get(DATE).setDefaultValue(ALL);
		get(FROM_DATE).setDefaultValue(new Date());
		get(TO_DATE).setDefaultValue(new Date());

	}

	private Result createOptionalResult(Context context) {

		List<String> viewType = new ArrayList<String>();
		viewType.add("Open");
		viewType.add("Over-Due");
		viewType.add("Voided");
		viewType.add("All");

		List<String> dateType = new ArrayList<String>();
		dateType.add("All");
		dateType.add("This week");
		dateType.add("This month");
		dateType.add("Last week");
		dateType.add("Last month");
		dateType.add("This financial year");
		dateType.add("Last financial year");
		dateType.add("This financial quarter");
		dateType.add("Last financial quarter");
		dateType.add("Financial year to date");
		dateType.add("Custom");

		ResultList resultList = new ResultList("invoicesList");
		Object selection = context.getSelection(ACTIONS);
		ActionNames actionNames;
		if (selection != null) {
			actionNames = (ActionNames) selection;
			switch (actionNames) {
			case FINISH:
				markDone();
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");
		Result result = stringListOptionalRequirement(context, resultList,
				selection, CURRENT_VIEW, "Current View", viewType,
				"Select View type", ITEMS_TO_VIEW);
		if (result != null) {
			return result;
		}
		result = stringListOptionalRequirement(context, resultList, selection,
				CURRENT_VIEW, "Date", dateType, "Select Date type",
				DATE_ITEMS_TO_VIEW);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, resultList, "From",
				"Enter From Date", selection);
		if (result != null) {
			return result;
		}

		result = dateOptionalRequirement(context, resultList, "To",
				"Enter To Date", selection);
		if (result != null) {
			return result;
		}

		return createInvoiceList(context, "", null, null, null);

	}

	private Result createInvoiceList(Context context, String viewType,
			String date, Date startDate, Date endDate) {
		Result result = context.makeResult();
		ResultList resultList = new ResultList("invoicesList");
		List<InvoicesList> invoices = getInvoices(context.getCompany().getID(),
				viewType, startDate, endDate);
		for (InvoicesList invoice : invoices) {
			resultList.add(createInvoiceListRecord(invoice));
		}

		StringBuilder message = new StringBuilder();
		if (resultList.size() > 0) {
			message.append("Select an Invoice List");
		}

		result.add(message.toString());
		result.add(resultList);

		CommandList commandList = new CommandList();
		commandList.add("Add Invoice");
		result.add(commandList);
		return result;
	}

	private List<InvoicesList> getInvoices(long companyId, String viewType,
			Date startDate, Date endDate) {

		try {
			List<InvoicesList> invoices = new FinanceTool()
					.getInventoryManager().getInvoiceList(companyId,
							startDate.getTime(), endDate.getTime());

			List<InvoicesList> list = new ArrayList<InvoicesList>(
					invoices.size());
			for (InvoicesList invoice : invoices) {
				if (viewType.equals(OPEN)) {
					if (invoice.getBalance() != null
							&& DecimalUtil.isGreaterThan(invoice.getBalance(),
									0)
							&& invoice.getDueDate() != null
							&& (invoice.getStatus() != ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED)
							&& !invoice.isVoided()) {
						list.add(invoice);
					}

				} else if (viewType.equals(OVER_DUE)) {
					if (invoice.getBalance() != null
							&& DecimalUtil.isGreaterThan(invoice.getBalance(),
									0)
							&& invoice.getDueDate() != null
							&& (invoice.getDueDate().compareTo(
									new ClientFinanceDate()) < 0)
							&& !invoice.isVoided()) {
						list.add(invoice);
					}
				} else if (viewType.equals(VOIDED)) {
					if (invoice.isVoided()) {
						list.add(invoice);
					}
				} else if (viewType.equals(ALL)) {
					list.add(invoice);
				}
			}

			return list;
		} catch (DAOException e) {
		}
		return null;
	}

	private Record createInvoiceListRecord(InvoicesList inv) {

		Record record = new Record(inv);

		record.add("Type", inv.getType());
		record.add("Number", inv.getNumber());
		record.add("Date", inv.getDate());
		record.add("CustomerName", inv.getCustomerName());
		record.add("DueDate", inv.getDueDate());
		record.add("NetPrice", inv.getNetAmount());
		record.add("TotalPrice", inv.getTotalPrice());
		record.add("Balance", inv.getBalance());
		return record;
	}

}
