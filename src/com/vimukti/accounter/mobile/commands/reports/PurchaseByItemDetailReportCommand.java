package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;
import com.vimukti.accounter.web.server.FinanceTool;

public class PurchaseByItemDetailReportCommand extends
		NewAbstractReportCommand<SalesByCustomerDetail> {
	private String itemName;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		super.addRequirements(list);
	}

	@Override
	protected Record createReportRecord(SalesByCustomerDetail record) {
		Record salesRecord = new Record(record);
		salesRecord.add("Item", "");
		salesRecord.add("Date", record.getDate());
		salesRecord.add("Type",
				ReportUtility.getTransactionName(record.getType()));
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
	protected List<SalesByCustomerDetail> getRecords() {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetails = new ArrayList<SalesByCustomerDetail>();
		try {
			if (itemName == null) {
				salesByCustomerDetails = new FinanceTool().getPurchageManager()
						.getPurchasesByItemDetail(getStartDate(), getEndDate(),
								getCompanyId());
			} else if (itemName != null && itemName.isEmpty()) {
				salesByCustomerDetails = new FinanceTool().getPurchageManager()
						.getPurchasesByItemDetail(itemName, getStartDate(),
								getEndDate(), getCompanyId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesByCustomerDetails;
	}

	@Override
	protected String addCommandOnRecordClick(SalesByCustomerDetail selection) {
		return "update transaction " + selection.getTransactionId();
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAnyReports();
	}

	@Override
	protected String getShowMessage() {
		return "";
	}

	@Override
	protected String getSelectRecordString() {
		return getMessages().reportSelected(
				getMessages().purchaseByItemDetail());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (string != null && string.isEmpty()) {
			String[] split = string.split(",");
			context.setString(split[0]);
			itemName = split[1];
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().purchaseByItemDetail());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages()
				.reportDetails(getMessages().purchaseByItemDetail());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().purchaseByItemDetail());
	}

}