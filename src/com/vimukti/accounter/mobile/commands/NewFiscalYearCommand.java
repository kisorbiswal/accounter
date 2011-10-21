package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.FiscalYear;
import com.vimukti.accounter.mobile.ActionNames;
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
public class NewFiscalYearCommand extends AbstractTransactionCommand {

	private static final String START_DATE = "start date";
	private static final String END_DATE = "end date";
	private static final String STATUS = "status";

	private static final String INPUT_ATTR = "input";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("startDate", false, true));
		list.add(new Requirement("endDate", false, true));
		list.add(new Requirement("status", true, true));

	}

	@Override
	public Result run(Context context) {

		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = context.makeResult();

		// Preparing Result
		Result makeResult = context.makeResult();
		makeResult
				.add(getMessages().readyToCreate(getConstants().newFiscalYear()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);

		result = dateRequirement(context, list, null, "startDate",
				getMessages().enterDate(getConstants().startDate()),
				getConstants().startDate());
		if (result != null) {
			return result;
		}
		result = dateRequirement(context, list, null, "endDate", getMessages()
				.enterDate(getConstants().endDate()), getConstants().endDate());
		if (result != null) {
			return result;

		}
		makeResult.add(actions);
		result = createOptionalRequirement(context);
		if (result != null) {
			return result;
		}

		return createNewFiscalRear(context);
	}

	private Result createNewFiscalRear(Context context) {

		FiscalYear fiscalYear = new FiscalYear();

		Date startDate = (Date) get(START_DATE).getValue();
		Date endDate = (Date) get(END_DATE).getValue();
		Integer status = (Integer) get(STATUS).getValue();

		fiscalYear.setStartDate(new FinanceDate(startDate));
		fiscalYear.setEndDate(new FinanceDate(endDate));
		fiscalYear.setStatus(status);

		Session session = context.getHibernateSession();
		Transaction transaction = session.beginTransaction();
		session.saveOrUpdate(fiscalYear);
		transaction.commit();

		markDone();

		Result result = new Result();
		result.add(" Fiscal year is created successfully.");

		return result;
	}

	private Result createOptionalRequirement(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("values");

		Requirement startDateReq = get(START_DATE);
		Date sdate = (Date) startDateReq.getValue();
		if (sdate == selection) {
			context.setAttribute(INPUT_ATTR, START_DATE);
			return text(context, "Please Enter the starting date", "" + sdate);
		}

		ResultList list = new ResultList("values");

		Record startDateRecord = new Record(sdate);
		startDateRecord.add("Name", START_DATE);
		startDateRecord.add("Value", sdate.toString());

		list.add(startDateRecord);
		Requirement endDateReq = get(START_DATE);
		Date edate = (Date) endDateReq.getValue();
		if (edate == selection) {
			context.setAttribute(INPUT_ATTR, START_DATE);
			return text(context, "Please Enter the ending date", "" + edate);
		}

		Record endDateRecord = new Record(edate);
		endDateRecord.add("Name", END_DATE);
		endDateRecord.add("Value", edate.toString());

		list.add(endDateRecord);

		Result result = statusRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = context.makeResult();
		result.add(" Creating new fiscal year  following values.");
		result.add(list);
		ResultList actions = new ResultList("actions");
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to creat new fiscal year");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result statusRequirement(Context context, ResultList list,
			Object selection) {

		Requirement statusReq = get(STATUS);
		Integer status = (Integer) statusReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(STATUS)) {
			Integer st = context.getSelection(NUMBER);
			if (st != 0) {
				st = context.getInteger();
			}
			status = st;
			statusReq.setValue(status);
		}
		if (selection == status) {
			context.setAttribute(INPUT_ATTR, STATUS);
			return number(context, "Status", "" + status);
		}

		Record statusRecord = new Record(status);
		statusRecord.add("Name", STATUS);
		statusRecord.add("Value", status);
		list.add(statusRecord);
		return null;
	}

}
