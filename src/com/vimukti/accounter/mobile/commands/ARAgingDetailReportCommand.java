package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;

public class ARAgingDetailReportCommand extends
		AbstractReportCommand<AgedDebtors> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Record createReportRecord(AgedDebtors record) {
		Record agingRecord = new Record(record);
		agingRecord.add("Name", record.getName());
		agingRecord.add("Date", record.getDate());
		agingRecord.add("Type", Utility.getTransactionName(record.getType()));
		agingRecord.add("No.", record.getNumber());
		agingRecord.add("Aging", record.getAgeing());
		agingRecord.add("Amount", record.getTotal());
		return agingRecord;
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

	@Override
	protected void addCommandOnRecordClick(AgedDebtors selection,
			CommandList commandList) {
		commandList.add(Utility.getTransactionName(selection.getType()));
	}

}
