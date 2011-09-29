package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.reports.VATDetail;

public class VATDetailReportCommand extends AbstractReportCommand<VATDetail> {

	private String currentsectionName;
	private double accountbalance;

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
	}

	@Override
	protected Record createReportRecord(VATDetail record) {
		Record salesRecord = new Record(record);

		salesRecord.add("Type", record.getTransactionName());
		salesRecord.add("Date", record.getTransactionDate());
		salesRecord.add("No.", record.getTransactionNumber());
		salesRecord.add("VAT Rate", record.isPercentage() ? record.getVatRate()
				+ "%" : record.getVatRate());
		salesRecord.add("Net amount", record.getNetAmount());
		salesRecord.add("Amount", record.getTotal());
		if (!currentsectionName.equals(record.getBoxName())) {
			currentsectionName = record.getBoxName();
			accountbalance = 0.0D;
		}
		salesRecord.add("Balance", accountbalance += record.getTotal());

		return salesRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<VATDetail> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addCommandOnRecordClick(VATDetail selection,
			CommandList commandList) {
		commandList.add(selection.getTransactionName());
	}

}