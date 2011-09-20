package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;

public class CustomerStatementReportCommand extends
		AbstractReportCommand<PayeeStatementsList> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
		list.add(new Requirement("customerName", true, true));
	}

	@Override
	protected Result createReqReportRecord(Result reportResult, Context context) {
		// Checking whether customer is there or not and returning result
		Requirement customerReq = get("customer");
		String customer = (String) customerReq.getValue();
		String selectioncustomer = context.getSelection("values");
		if (customer == selectioncustomer)
			return customerRequirement(context);

		return super.createReqReportRecord(reportResult, context);
	}

	@Override
	protected Record createReportRecord(PayeeStatementsList record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<PayeeStatementsList> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

}