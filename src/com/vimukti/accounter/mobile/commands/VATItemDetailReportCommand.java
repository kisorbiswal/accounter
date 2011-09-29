package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.VATItemDetail;

public class VATItemDetailReportCommand extends
		AbstractReportCommand<VATItemDetail> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
	}

	@Override
	protected Record createReportRecord(VATItemDetail record) {
		Record ecRecord = new Record(record);
		ecRecord.add("Type", Utility.getTransactionName(record
				.getTransactionType()));
		ecRecord.add("Date", record.getDate());
		ecRecord.add("No", record.getTransactionNumber());
		ecRecord.add("Name", record.getName());
		ecRecord.add("Memo", record.getMemo());
		ecRecord.add("Amount", record.getAmount());
		ecRecord.add("Sales Price", record.getSalesPrice());
		return ecRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<VATItemDetail> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addCommandOnRecordClick(VATItemDetail selection,
			CommandList commandList) {
		commandList.add(Utility.getTransactionName(selection
				.getTransactionType()));
	}

}
