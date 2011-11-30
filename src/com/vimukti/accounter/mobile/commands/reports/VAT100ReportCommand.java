package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.TaxAgencyRequirement;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.server.FinanceTool;

public class VAT100ReportCommand extends NewAbstractReportCommand<VATSummary> {
	private TAXAgency agency;

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new TaxAgencyRequirement(TAX_AGENCY, getMessages()
				.pleaseEnter(getMessages().taxAgencie()), getMessages()
				.taxAgencie(), false, true, new ChangeListner<TAXAgency>() {

			@Override
			public void onSelection(TAXAgency value) {
				agency = value;
			}
		}) {

			@Override
			protected String getSetMessage() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected List<TAXAgency> getLists(Context context) {
				return new ArrayList<TAXAgency>(getCompany().getTaxAgencies());
			}

			@Override
			protected String getEmptyString() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected boolean filter(TAXAgency e, String name) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		addDateRangeFromToDateRequirements(list);
	}

	protected Record createReportRecord(VATSummary record) {
		Record vatItemRecord = new Record(record);
		vatItemRecord.add(getMessages().name(), record.getVatReturnEntryName());
		vatItemRecord.add(getStartDate() + "_" + getEndDate(),
				record.getValue());
		return vatItemRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	protected List<VATSummary> getRecords() {
		ArrayList<VATSummary> vatSummaries = new ArrayList<VATSummary>();
		try {
			if (agency != null) {
				vatSummaries = new FinanceTool().getReportManager()
						.getVAT100Report(agency, getStartDate(), getEndDate(),
								getCompanyId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatSummaries;
	}

	protected String addCommandOnRecordClick(VATSummary selection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(getMessages().vat100());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().vat100());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().vat100());
	}
}