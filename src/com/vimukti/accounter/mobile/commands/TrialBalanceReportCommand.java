package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.reports.BaseReport;

public class TrialBalanceReportCommand extends AbstractReportCommand {

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("dateRange", true, true));
		list.add(new Requirement("date", true, true));
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Record createReportRecord(BaseReport record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<BaseReport> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Result createReqReportRecord(Result reportResult, Context context) {
		ResultList resultList = new ResultList("values");

		// Checking whether date range is there or not and returning result
		Requirement reportReq = get("dateRange");
		String dateRange = (String) reportReq.getValue();
		String selectionDateRange = context.getSelection("values");

		if (dateRange == selectionDateRange)
			return dateRangeRequirement(context, resultList, selectionDateRange);

		// Checking whether to date is there or not and returning result
		Requirement toDateReq = get("toDate");
		Date toDate = (Date) toDateReq.getValue();
		Date selectiontoDate = context.getSelection("values");
		if (toDate == selectiontoDate)
			return toDateRequirement(context, resultList, selectiontoDate);

		return reportResult;
	}

}
