package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.reports.BaseReport;

public class ProfitLossReportCommand extends AbstractReportCommand {

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
	protected List<BaseReport> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Record createReportRecord(BaseReport record) {

		return null;
	}

}
