package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.web.client.core.Lists.DummyDebitor;

public class APAgingSummaryReportCommand extends
		AbstractReportCommand<DummyDebitor> {

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
	protected Result createReqReportRecord(Result reportResult, Context context) {
		return null;
	}

	@Override
	protected Record createReportRecord(DummyDebitor record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<DummyDebitor> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}
}