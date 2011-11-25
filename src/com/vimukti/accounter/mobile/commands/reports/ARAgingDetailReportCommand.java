package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.server.FinanceTool;

public class ARAgingDetailReportCommand extends
		NewAbstractReportCommand<AgedDebtors> {
	private String customerName;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addFromToDateRequirements(list);
		super.addRequirements(list);
	}

	@Override
	protected Record createReportRecord(AgedDebtors record) {
		Record agingRecord = new Record(record);
		agingRecord.add("Name", record.getName());
		agingRecord.add("Date", record.getDate());
		agingRecord.add("Type", Utility.getTransactionName(record.getType()));
		agingRecord.add("No.", record.getNumber());
		agingRecord.add("Aging", record.getAgeing());
		agingRecord.add("Amount", record.getTotal());
		return agingRecord;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected List<AgedDebtors> getRecords() {
		ArrayList<AgedDebtors> debitors = new ArrayList<AgedDebtors>();
		ArrayList<AgedDebtors> agedCreditorsListForCustomer = new ArrayList<AgedDebtors>();
		try {
			if (customerName == null) {
				debitors = new FinanceTool().getReportManager().getAgedDebtors(
						getStartDate(), getEndDate(), getCompanyId());
			} else if (customerName != null) {
				debitors = new FinanceTool().getReportManager().getAgedDebtors(
						getStartDate(), getEndDate(), getCompanyId());
				for (AgedDebtors agdDebitor : debitors) {
					if (customerName.equals(agdDebitor.getName()))
						agedCreditorsListForCustomer.add(agdDebitor);
				}
			}
			if (agedCreditorsListForCustomer != null
					&& !agedCreditorsListForCustomer.isEmpty()) {
				return agedCreditorsListForCustomer;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return debitors;
	}

	@Override
	protected String addCommandOnRecordClick(AgedDebtors selection) {
		return "update transaction " + selection.getTransactionId();
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAnyReports();
	}

	@Override
	protected String getShowMessage() {
		return null;
	}

	@Override
	protected String getSelectRecordString() {
		return getMessages().reportSelected(getMessages().arAgeingDetails());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (string != null && !string.isEmpty()) {
			String[] split = string.split(",");
			if (split.length > 1) {
				context.setString(split[0]);
				customerName = split[1];
			}
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().arAgeingDetails());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().arAgeingDetails());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().arAgeingDetails());
	}

}
