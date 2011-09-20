package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;

public class CashFlowReportCommand extends AbstractReportCommand<TrialBalance> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Record createReportRecord(TrialBalance record) {
		Record trialRecord = new Record(record);
		trialRecord.add("", record.getAccountNumber() != null ? record
				.getAccountNumber()
				+ "-" + record.getAccountName() : "" + record.getAccountName());
		trialRecord
				.add(getStartDate() + "_" + getEndDate(), record.getAmount());
		return trialRecord;
	}

	@Override
	protected List<TrialBalance> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

}
