package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;

public class ARAgingDetailReportCommand extends
		AbstractReportCommand<AgedDebtors> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Record createReportRecord(AgedDebtors record) {
		// TODO Auto-generated method stub
		return null;
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

}
