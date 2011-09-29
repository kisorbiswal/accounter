package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;

public class SalesByItemDetailReportCommand extends
		AbstractReportCommand<SalesByCustomerDetail> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
	}

	@Override
	protected Record createReportRecord(SalesByCustomerDetail record) {
		Record salesRecord = new Record(record);
		salesRecord.add("Item", "");
		salesRecord.add("Date", record.getDate());
		salesRecord.add("Type", ReportUtility.getTransactionName(record
				.getType()));
		salesRecord.add("No.", record.getNumber());
		salesRecord.add("Quantity", record.getQuantity());
		salesRecord.add("Unit price", record.getUnitPrice());
		salesRecord.add("Discount", record.getDiscount());
		salesRecord.add("Amount", record.getAmount());
		return salesRecord;
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