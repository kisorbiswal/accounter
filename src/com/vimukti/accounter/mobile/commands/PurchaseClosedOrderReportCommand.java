package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class PurchaseClosedOrderReportCommand extends
		AbstractReportCommand<OpenAndClosedOrders> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		add3ReportRequirements(list);
		list.add(new Requirement("status", true, true));
	}

	@Override
	protected Result createReqReportRecord(Result reportResult, Context context) {
		ResultList resultList = new ResultList("values");

		// Checking whether status is there or not and returning result
		Requirement statusReq = get("status");
		String status = (String) statusReq.getValue();
		String selectionstatus = context.getSelection("values");
		if (status == selectionstatus)
			return statusRequirement(context, resultList, selectionstatus);
		return super.createReqReportRecord(reportResult, context);
	}

	@Override
	protected Record createReportRecord(OpenAndClosedOrders record) {
		Record openRecord = new Record(record);
		if (record.getTransactionDate() != null)
			openRecord.add("Order Date", UIUtils.getDateByCompanyType(record
					.getTransactionDate()));
		else
			openRecord.add("", "");
		openRecord.add("Supplier", record.getVendorOrCustomerName());
		openRecord.add("Description", record.getDescription());
		openRecord.add("Quantity", ((Double) record.getQuantity()).toString());
		openRecord.add("Value", record.getAmount());

		return openRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<OpenAndClosedOrders> getRecords(Session session) {
		// TODO Auto-generated method stub
		return null;
	}

}
