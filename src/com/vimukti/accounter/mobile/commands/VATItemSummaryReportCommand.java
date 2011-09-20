package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.reports.VATItemSummary;

public class VATItemSummaryReportCommand extends
		AbstractReportCommand<VATItemSummary> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
	}

	@Override
	protected Record createReportRecord(VATItemSummary record) {
		Record vatItemRecord = new Record(record);
		vatItemRecord.add("Name", record.getName());
		vatItemRecord.add("Amout", record.getAmount());
		return vatItemRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<VATItemSummary> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

}