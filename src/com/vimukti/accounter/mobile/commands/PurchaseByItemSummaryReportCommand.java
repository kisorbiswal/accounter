package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;

public class PurchaseByItemSummaryReportCommand extends
		AbstractReportCommand<SalesByCustomerDetail> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
	}

	@Override
	protected Record createReportRecord(SalesByCustomerDetail record) {

		Record salesRecord = new Record(record);
		salesRecord.add("Item", record.getItemName());
		salesRecord.add("Quantity", record.getQuantity());
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
		commandList.add("Purchase By Item Detail");
	}

}