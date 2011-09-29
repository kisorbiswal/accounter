package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;

public class TransactionDetailbyTaxItemReportCommand extends
		AbstractReportCommand<TransactionDetailByTaxItem> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
	}

	@Override
	protected Record createReportRecord(TransactionDetailByTaxItem record) {

		Record tdRecord = new Record(record);

		tdRecord.add("", ReportUtility.getTransactionName(record
				.getTransactionType()));
		tdRecord.add("Tax Rate", record.getRate());
		tdRecord.add("Date", record.getDate());
		tdRecord.add("No", record.getNumber());
		tdRecord.add("Name", record.getTaxItemName());
		tdRecord.add("Memo", record.getMemo());
		tdRecord.add("Sales Tax", record.getSalesTaxAmount());
		tdRecord.add("Taxable Amount", record.getTaxableAmount());

		return tdRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<TransactionDetailByTaxItem> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addCommandOnRecordClick(
			TransactionDetailByTaxItem selection, CommandList commandList) {
		commandList.add(ReportUtility.getTransactionName(selection
				.getTransactionType()));
	}

}
