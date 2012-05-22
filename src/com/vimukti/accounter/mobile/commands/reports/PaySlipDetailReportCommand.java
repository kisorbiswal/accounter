package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.PayStructureDestination;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.EmployeeAndEmployeeGroupRequirement;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.web.client.core.reports.PaySlipDetail;
import com.vimukti.accounter.web.server.FinanceTool;

public class PaySlipDetailReportCommand extends
		NewAbstractReportCommand<PaySlipDetail> {

	private static String EMPLOYEE = "employee";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		context.getSelection(EMPLOYEE);
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new EmployeeAndEmployeeGroupRequirement(EMPLOYEE,
				getMessages().pleaseSelect(getMessages().employeeOrGroup()),
				getMessages().employeeOrGroup(), null));
		addDateRangeFromDateRequirements(list);
		list.add(new ReportResultRequirement<PaySlipDetail>() {

			@Override
			protected String onSelection(PaySlipDetail selection, String name) {
				return addCommandOnRecordClick(selection);
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<PaySlipDetail> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add(getMessages().noRecordsToShow());
					return;
				}

				ResultList resultList = new ResultList("paySlipDetail");
				addSelection("paySlipDetail");
				double earnings = 0.0D;
				double deductions = 0.0D;
				for (PaySlipDetail record : records) {
					if (record.getType() == 2) {
						earnings += record.getAmount();
					}
					if (record.getType() == 3) {
						deductions += record.getAmount();
					}
					resultList.add(createReportRecord(record));
				}
				makeResult.add(resultList);
				makeResult.add(getMessages().earnings()
						+ getAmountWithCurrency(earnings));
				makeResult.add(getMessages().deductions()
						+ getAmountWithCurrency(deductions));
			}
		});
	}

	protected Record createReportRecord(PaySlipDetail record) {
		Record payDetailRecord = new Record(record);
		payDetailRecord.add(getMessages().name(), record.getName());
		if (record.getType() == 2) {
			payDetailRecord.add(getMessages().earnings(), record.getAmount());
			payDetailRecord.add(getMessages().deductions(), 0);
		}
		if (record.getType() == 3) {
			payDetailRecord.add(getMessages().earnings(), 0);
			payDetailRecord.add(getMessages().deductions(), record.getAmount());
		}
		return null;
	}

	protected List<PaySlipDetail> getRecords() {
		List<PaySlipDetail> paySlipDetails = new ArrayList<PaySlipDetail>();
		PayStructureDestination value = get(EMPLOYEE).getValue();
		paySlipDetails = new FinanceTool().getPayrollManager()
				.getPaySlipDetail(value.getID(), getStartDate(), getEndDate(),
						getCompanyId());
		return paySlipDetails;
	}

	protected String addCommandOnRecordClick(PaySlipDetail selection) {
		return null;
	}

}
