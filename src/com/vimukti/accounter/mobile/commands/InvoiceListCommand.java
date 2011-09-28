package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class InvoiceListCommand extends AbstractTransactionCommand {

	private static final String VIEW_BY = "viewBy";
	private static final String FROM_DATE = "fromDate";
	private static final String TO_DATE = "toDate";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(VIEW_BY, true, true));
		list.add(new Requirement(FROM_DATE, false, true));
		list.add(new Requirement(TO_DATE, false, true));
	}

	@Override
	public Result run(Context context) {
		Result result = null;
		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	private Result createOptionalResult(Context context) {

		context.setAttribute(INPUT_ATTR, "optional");
		Object selection = context.getSelection(FROM_DATE);
		ResultList list = new ResultList("invoicesList");
		Result result = fromDateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = toDateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		Date fromDate = (Date) get(FROM_DATE).getValue();
		Date toDate = (Date) get(TO_DATE).getValue();

		result = invoicesList(fromDate, toDate, context);
		return null;
	}

	private Result invoicesList(Date fromDate, Date toDate, Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private Result toDateRequirement(Context context, ResultList list,
			Object selection) {
		Requirement dateReq = get(TO_DATE);
		Date transDate = (Date) dateReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(TO_DATE)) {
			Date date = context.getSelection(TO_DATE);
			if (date == null) {
				date = context.getDate();
			}
			transDate = date;
			dateReq.setValue(transDate);
		}
		if (selection == transDate) {
			context.setAttribute(INPUT_ATTR, TO_DATE);
			return date(context, "Enter to Date", transDate);
		}

		Record transDateRecord = new Record(transDate);
		transDateRecord.add("Name", TO_DATE);
		transDateRecord.add("Value", transDate.toString());
		list.add(transDateRecord);
		return null;
	}

	private Result fromDateRequirement(Context context, ResultList list,
			Object selection) {
		Requirement dateReq = get(FROM_DATE);
		Date transDate = (Date) dateReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(FROM_DATE)) {
			Date date = context.getSelection(FROM_DATE);
			if (date == null) {
				date = context.getDate();
			}
			transDate = date;
			dateReq.setValue(transDate);
		}
		if (selection == transDate) {
			context.setAttribute(INPUT_ATTR, FROM_DATE);
			return date(context, "Enter From Date", transDate);
		}

		Record transDateRecord = new Record(transDate);
		transDateRecord.add("Name", FROM_DATE);
		transDateRecord.add("Value", transDate.toString());
		list.add(transDateRecord);
		return null;
	}

}
