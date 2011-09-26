package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Box;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class FileVATCommand extends AbstractVATCommand {

	private static final String FROM_DATE = null;
	private static final String TO_DATE = null;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(TAX_AGENCY, false, true));
		list.add(new Requirement(FROM_DATE, false, true));
		list.add(new Requirement(TO_DATE, false, true));
	}

	@Override
	public Result run(Context context) {
		Result result = null;

		result = taxAgencyRequirement(context);
		if (result != null) {
			return result;
		}

		result = dateRequirement(context, FROM_DATE);
		if (result != null) {
			return result;
		}

		result = dateRequirement(context, TO_DATE);
		if (result != null) {
			return result;
		}

		result = createOptionalRequirement(context);
		if (result != null) {
			return result;
		}

		return null;
	}

	private Result createOptionalRequirement(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");

		Requirement taxAgencyReq = get(TAX_AGENCY);
		TAXAgency taxAgency = (TAXAgency) taxAgencyReq.getValue();
		if (taxAgency == selection) {
			context.setAttribute(INPUT_ATTR, TAX_AGENCY);
			return getTaxAgencyResult(context);
		}

		Requirement fromDateReq = get(FROM_DATE);
		Date fromDate = (Date) fromDateReq.getValue();
		if (fromDate == selection) {
			context.setAttribute(INPUT_ATTR, FROM_DATE);
			return date(context, "Enter From Date", fromDate);
		}

		Requirement toDateReq = get(TO_DATE);
		Date toDate = (Date) toDateReq.getValue();
		if (toDate == selection) {
			context.setAttribute(INPUT_ATTR, TO_DATE);
			return date(context, "Enter To Date", toDate);
		}

		ResultList list = new ResultList("values");

		Record taxAgencyRecord = new Record(taxAgency);
		taxAgencyRecord.add(INPUT_ATTR, TAX_AGENCY);
		taxAgencyRecord.add("Value", taxAgency);
		list.add(taxAgencyRecord);

		Record fromDateRecord = new Record(fromDate);
		fromDateRecord.add(INPUT_ATTR, FROM_DATE);
		fromDateRecord.add("Value", fromDate);
		list.add(fromDateRecord);

		Record toDateRecord = new Record(toDate);
		toDateRecord.add(INPUT_ATTR, TO_DATE);
		toDateRecord.add("Value", toDate);
		list.add(toDateRecord);

		Result result = context.makeResult();
		result.add("File VAT is ready to create with following values.");
		result.add(list);
		result.add("VAT Line:-");
		ResultList boxes = new ResultList("vatline");
		for (Box box : getBoxes()) {
			Record itemRec = createBoxRecord(box);
			boxes.add(itemRec);
		}
		result.add(boxes);

		ResultList actions = new ResultList(ACTIONS);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to File vat.");
		actions.add(finish);
		result.add(actions);

		return null;
	}

	private Record createBoxRecord(Box box) {
		Record record = new Record(box);
		record.add("Vat Line", box.getName());
		record.add("Amount", box.getAmount());
		return record;
	}

	private List<Box> getBoxes() {
		// TODO Auto-generated method stub
		return null;
	}

	private Result dateRequirement(Context context, String dateString) {
		Requirement fromDateReq = get(dateString);
		Date date = context.getSelection(DATE);
		if (date != null) {
			fromDateReq.setValue(date);
		}
		if (!fromDateReq.isDone()) {
			return date(context, "Enter " + dateString, null);
		}
		return null;
	}

}
