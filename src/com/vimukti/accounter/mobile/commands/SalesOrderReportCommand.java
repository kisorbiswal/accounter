package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.reports.BaseReport;

public class SalesOrderReportCommand extends AbstractReportCommand {

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
		list.add(new Requirement("status", true, true));
	}

	@Override
	protected Record createReportRecord(BaseReport record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
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

		// Checking whether status is there or not and returning result
		Requirement statusReq = get("status");
		String status = (String) statusReq.getValue();
		String selectionstatus = context.getSelection("values");
		if (status == selectionstatus)
			return statusRequirement(context, resultList, selectionstatus);
		return super.createReqReportRecord(reportResult, context);
	}
}