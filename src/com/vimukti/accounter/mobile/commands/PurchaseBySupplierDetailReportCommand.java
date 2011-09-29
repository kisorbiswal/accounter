package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;

public class PurchaseBySupplierDetailReportCommand extends
		AbstractReportCommand<SalesByCustomerDetail> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
	}

	@Override
	protected Record createReportRecord(SalesByCustomerDetail record) {
		Record transactionRecord = new Record(record);
		transactionRecord.add("Supplier Name", "");
		transactionRecord.add("Date", record.getDate());
		transactionRecord.add("Type", Utility.getTransactionName(record
				.getType()));
		transactionRecord.add("No.", record.getNumber());
		transactionRecord.add("Amount", record.getAmount());
		return transactionRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<SalesByCustomerDetail> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addCommandOnRecordClick(SalesByCustomerDetail selection,
			CommandList commandList) {
		commandList.add(Utility.getTransactionName(selection.getType()));
	}

}