package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.web.client.core.reports.VATSummary;

public class VAT100ReportCommand extends AbstractReportCommand<VATSummary> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
		list.add(new Requirement("vatAgency", true, true));
	}

	@Override
	protected Record createReportRecord(VATSummary record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<VATSummary> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Result createReqReportRecord(Result reportResult, Context context) {

		// Checking whether vat agency is there or not and returning result
		Requirement vatAgencyReq = get("vatAgency");
		String vatAgency = (String) vatAgencyReq.getValue();
		String selectiontoVatAgency = context.getSelection("values");
		if (vatAgency == selectiontoVatAgency)
			return vatAgencyRequirement(context);

		return super.createReqReportRecord(reportResult, context);
	}

}