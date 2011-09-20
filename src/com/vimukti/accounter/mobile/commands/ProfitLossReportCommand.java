package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Utility_R;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;

public class ProfitLossReportCommand extends
		AbstractReportCommand<TrialBalance> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
	}

	@Override
	protected Record createReportRecord(TrialBalance record) {
		Record trialRecord = new Record(record);
		if (getCompany().getPreferences().getUseAccountNumbers() == true) {
			trialRecord.add("Category Number", record.getAccountNumber());
		} else {
			trialRecord.add("Category Number", "");
		}
		trialRecord.add("", record.getAccountName());
		trialRecord
				.add(getStartDate() + "_" + getEndDate(), record.getAmount());
		trialRecord.add("", record.getAmount());
		trialRecord.add(Utility_R.getCurrentFiscalYearStartDate() + "_"
				+ getLastMonth(new FinanceDate()), record.getTotalAmount());
		trialRecord.add("", record.getTotalAmount());
		return trialRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<TrialBalance> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

}
