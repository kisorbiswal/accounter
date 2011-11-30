package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;
import com.vimukti.accounter.web.server.FinanceTool;

public class SalesByItemDetailReportCommand extends
		NewAbstractReportCommand<SalesByCustomerDetail> {
	private String itemName;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
	}

	protected Record createReportRecord(SalesByCustomerDetail record) {
		Record salesRecord = new Record(record);
		salesRecord.add(getMessages().item(), "");
		salesRecord.add(getMessages().date(), record.getDate());
		salesRecord.add(getMessages().type(),
				ReportUtility.getTransactionName(record.getType()));
		salesRecord.add(getMessages().number(), record.getNumber());
		salesRecord.add(getMessages().quantity(), record.getQuantity());
		salesRecord.add(getMessages().unitPrice(), record.getUnitPrice());
		salesRecord.add(getMessages().discount(), record.getDiscount());
		salesRecord.add(getMessages().amount(), record.getAmount());
		return salesRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	protected List<SalesByCustomerDetail> getRecords() {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetails = new ArrayList<SalesByCustomerDetail>();
		try {
			if (itemName == null) {
				salesByCustomerDetails = new FinanceTool().getSalesManager()
						.getSalesByItemDetail(getStartDate(), getEndDate(),
								getCompanyId());
			} else if (itemName != null && !itemName.isEmpty()) {
				salesByCustomerDetails = new FinanceTool().getSalesManager()
						.getSalesByItemDetail(itemName, getStartDate(),
								getEndDate(), getCompanyId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesByCustomerDetails;
	}

	protected String addCommandOnRecordClick(SalesByCustomerDetail selection) {
		return "update transaction " + selection.getTransactionId();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (string != null && !string.isEmpty()) {
			String[] split = string.split(",");
			context.setString(split[0]);
			itemName = split[1];
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().salesByItemSummary());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().salesByItemSummary());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().salesByItemSummary());
	}

}