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
public class CloseFiscalYearCommand extends AbstractTransactionCommand {

	private static final String FROM_DATE = "fromDate";
	private static final String TO_DATE = "toDate";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(FROM_DATE, false, true));
		list.add(new Requirement(TO_DATE, false, true));
	}

	@Override
	public Result run(Context context) {

		Result result = null;

		ResultList list = new ResultList("values");
		result = fromDateRequirement(context);
		if (result != null) {
			return result;
		}
		Date fromDate = (Date) get(FROM_DATE).getValue();
		list.add(createDateRecord(fromDate));
		result = toDateRequirement(context);
		if (result != null) {
			return result;
		}
		Date toDate = (Date) get(TO_DATE).getValue();
		list.add(createDateRecord(toDate));
		result.add(list);
		completeProces(context);
		return result;
	}

	private Record createDateRecord(Date fromDate) {
		// TODO Auto-generated method stub
		return null;
	}

	private void completeProces(Context context) {
		// TODO Auto-generated method stub

	}

	private Result toDateRequirement(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	private Result fromDateRequirement(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
