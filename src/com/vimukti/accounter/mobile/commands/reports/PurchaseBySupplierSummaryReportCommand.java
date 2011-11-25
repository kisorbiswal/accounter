package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.server.FinanceTool;

public class PurchaseBySupplierSummaryReportCommand extends
		NewAbstractReportCommand<SalesByCustomerDetail> {

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		super.addRequirements(list);

	}

	@Override
	protected Record createReportRecord(SalesByCustomerDetail record) {
		Record salesRecord = new Record(record);
		salesRecord.add("Supplier Name", record.getName());
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
			salesByCustomerDetails = new FinanceTool().getVendorManager()
					.getPurchasesByVendorSummary(getStartDate(), getEndDate(),
							getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesByCustomerDetails;
	}

	@Override
	protected String addCommandOnRecordClick(SalesByCustomerDetail selection) {
		return "Purchase By Supplier Detail," + selection.getName();
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
				getMessages().purchaseByVendorSummary(Global.get().Vendor()));
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().purchaseByVendorSummary(Global.get().Vendor()));
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(
				getMessages().purchaseByVendorSummary(Global.get().Vendor()));
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().purchaseByVendorSummary(Global.get().Vendor()));
	}

}