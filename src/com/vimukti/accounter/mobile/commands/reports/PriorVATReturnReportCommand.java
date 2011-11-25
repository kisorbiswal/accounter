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

public class PriorVATReturnReportCommand extends
		NewAbstractReportCommand<VATSummary> {
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
		addFromToDateRequirements(list);
		super.addRequirements(list);
	}

	@Override
	protected Record createReportRecord(VATSummary record) {
		Record vatRecord = new Record(record);
		vatRecord.add("", record.getName());
		vatRecord.add(getEndDate() + "", record.getValue());
		return vatRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<VATSummary> getRecords() {
		ArrayList<VATSummary> vatSummaries = new ArrayList<VATSummary>();
		try {
			vatSummaries = new FinanceTool().getTaxManager()
					.getPriorReturnVATSummary(agency, getEndDate(),
							getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatSummaries;
	}

	@Override
	protected String addCommandOnRecordClick(VATSummary selection) {
		return null;
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
		return getMessages().reportSelected(getMessages().priorVATReturns());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().priorVATReturns());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().priorVATReturns());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().priorVATReturns());
	}
}