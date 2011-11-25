package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.ECSalesListDetail;
import com.vimukti.accounter.web.server.FinanceTool;

public class ECSalesListDetailReportCommand extends
		NewAbstractReportCommand<ECSalesListDetail> {
	private Customer customer;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		super.addRequirements(list);
	}

	@Override
	protected Record createReportRecord(ECSalesListDetail record) {
		Record ecRecord = new Record(record);
		ecRecord.add("Type",
				Utility.getTransactionName(record.getTransactionType()));
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
	protected List<ECSalesListDetail> getRecords() {
		ArrayList<ECSalesListDetail> ecsalSalesListDetails = new ArrayList<ECSalesListDetail>();
		try {
			if (customer != null) {
				ecsalSalesListDetails = new FinanceTool().getReportManager()
						.getECSalesListDetailReport(customer.getName(),
								getStartDate(), getEndDate(), getCompany());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ecsalSalesListDetails;
	}

	@Override
	protected String addCommandOnRecordClick(ECSalesListDetail selection) {
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
		return getMessages().reportSelected(getMessages().ecSalesListDetails());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String customerName = null;
		String string = context.getString();
		if (string != null && string.isEmpty()) {
			String[] split = string.split(",");
			context.setString(split[0]);
			customerName = split[1];
		}
		if (customerName != null) {
			customer = CommandUtils.getCustomerByName(getCompany(),
					customerName);
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().ecSalesListDetails());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().ecSalesListDetails());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().ecSalesListDetails());
	}

}
