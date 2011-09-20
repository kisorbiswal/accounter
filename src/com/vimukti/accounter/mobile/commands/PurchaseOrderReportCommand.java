package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;

public class PurchaseOrderReportCommand extends
		AbstractReportCommand<OpenAndClosedOrders> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
		list.add(new Requirement("status", true, true));
	}

	@Override
	protected Record createReportRecord(OpenAndClosedOrders record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<OpenAndClosedOrders> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

}