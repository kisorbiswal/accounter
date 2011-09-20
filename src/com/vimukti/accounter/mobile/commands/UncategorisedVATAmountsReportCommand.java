package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.UncategorisedAmountsReport;

public class UncategorisedVATAmountsReportCommand extends
		AbstractReportCommand<UncategorisedAmountsReport> {

	private double balance;

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);

	}

	@Override
	protected Record createReportRecord(UncategorisedAmountsReport record) {
		Record uncategoryRecord = new Record(record);

		uncategoryRecord.add("Type", Utility.getTransactionName(record
				.getTransactionType()));
		if (record.getDate() != null)
			uncategoryRecord.add("Date", record.getDate());
		else
			uncategoryRecord.add("", "");
		uncategoryRecord.add("No.", record.getTransactionNumber());
		uncategoryRecord.add("Source Name", record.getSourceName());
		balance += record.getAmount();
		uncategoryRecord.add("Amount", record.getAmount());
		uncategoryRecord.add("Balance", balance);

		return uncategoryRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<UncategorisedAmountsReport> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

}