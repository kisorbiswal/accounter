package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;
import java.util.Map;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.TDSAcknowledgmentsReport;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.TDSAcknowledgmentServerReportView;

public class TDSAcknowledgmentsReportView extends
		AbstractReportView<TDSAcknowledgmentsReport> {

	public TDSAcknowledgmentsReportView() {
		this.serverReport = new TDSAcknowledgmentServerReportView(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getTDSAcknowledgments(start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(TDSAcknowledgmentsReport record) {
		// TODO Auto-generated method stub

	}

	public int sort(TDSAcknowledgmentsReport obj1,
			TDSAcknowledgmentsReport obj2, int col) {
		switch (col) {
		case 0:
			return obj1.getAckNo().toLowerCase()
					.compareTo(obj2.getAckNo().toLowerCase());
		case 1:
			return UIUtils.compareInt(obj1.getFormType(), obj2.getFormType());
		case 2:
			return UIUtils.compareInt(obj1.getFinancialYearStart(),
					obj2.getFinancialYearStart());
		case 3:
			return UIUtils.compareInt(obj1.getQuater(), obj2.getQuater());
		case 4:
			return new ClientFinanceDate(obj1.getDate())
					.compareTo(new ClientFinanceDate(obj2.getDate()));
		}
		return 0;
	}

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		String selectedDateRange = toolbar.getSelectedDateRange();
		ClientFinanceDate startDate = toolbar.getStartDate();
		ClientFinanceDate endDate = toolbar.getEndDate();
		map.put("selectedDateRange", selectedDateRange);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;
	}

}
