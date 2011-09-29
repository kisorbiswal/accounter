package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;

public class APAgingDetailReportCommand extends
		AbstractReportCommand<AgedDebtors> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<AgedDebtors> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Result createReqReportRecord(Result reportResult, Context context) {
		return null;
	}

	@Override
	protected Record createReportRecord(AgedDebtors recordReport) {
		Record record = new Record((AgedDebtors) recordReport);
		record.add("Name", recordReport.getName());
		record.add("Date", recordReport.getDate());
		record.add("Transansaction Type", ReportUtility
				.getTransactionName(recordReport.getType()));
		record.add("Number", recordReport.getNumber());
		record.add("Aging", recordReport.getAgeing());
		record.add("Total", recordReport.getTotal());
		return record;
	}

	@Override
	protected void addCommandOnRecordClick(AgedDebtors selection,
			CommandList commandList) {
		commandList.add(ReportUtility.getTransactionName(selection.getType()));
	}

}