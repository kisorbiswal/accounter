package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.server.FinanceTool;

public class PurchaseBySupplierDetailReportCommand extends
		NewAbstractReportCommand<SalesByCustomerDetail> {
	private String supplierName;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		super.addRequirements(list);
	}

	@Override
	protected Record createReportRecord(SalesByCustomerDetail record) {
		Record transactionRecord = new Record(record);
		transactionRecord.add(getMessages().payeeName(Global.get().vendor()),
				"");
		transactionRecord.add(getMessages().date(), record.getDate());
		transactionRecord.add(getMessages().type(),
				Utility.getTransactionName(record.getType()));
		transactionRecord.add(getMessages().number(), record.getNumber());
		transactionRecord.add(getMessages().amount(), record.getAmount());
		return transactionRecord;
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
			if (supplierName == null) {
				salesByCustomerDetails = new FinanceTool().getVendorManager()
						.getPurchasesByVendorDetail(getStartDate(),
								getEndDate(), getCompanyId());
			} else if (supplierName != null && supplierName.isEmpty()) {
				salesByCustomerDetails = new FinanceTool().getVendorManager()
						.getPurchasesByVendorDetail(supplierName,
								getStartDate(), getEndDate(), getCompanyId());
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
				getMessages().purchaseByVendorDetail(Global.get().Vendor()));
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String supplierName = null;
		String string = context.getString();
		if (string != null && string.isEmpty()) {
			String[] split = string.split(",");
			context.setString(split[0]);
			supplierName = split[1];
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().purchaseByVendorDetail(Global.get().Vendor()));
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(
				getMessages().purchaseByVendorDetail(Global.get().Vendor()));
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().purchaseByVendorDetail(Global.get().Vendor()));
	}

}