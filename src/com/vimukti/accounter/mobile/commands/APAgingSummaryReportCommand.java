package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.CommandList;
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
		Record agingRecord = new Record(record);
		agingRecord.add("Creditor", record.getDebitorName());
		agingRecord.add("0-30 days", record.getDebitdays_in30()
				+ record.getDebitdays_incurrent());
		agingRecord.add("31-60 days", record.getDebitdays_in60());
		agingRecord.add("61-90 days", record.getDebitdays_in90());
		agingRecord.add("Older", record.getDebitdays_inolder());
		agingRecord.add("Total balance", record.getDebitdays_in30()
				+ record.getDebitdays_in60() + record.getDebitdays_in90()
				+ record.getDebitdays_inolder()
				+ record.getDebitdays_incurrent());
		return agingRecord;
	}

	@Override
	protected List<DummyDebitor> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addCommandOnRecordClick(DummyDebitor selection,
			CommandList commandList) {
		commandList.add("A/P Aging Detail");
	}

}