package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public class PortletToolBar extends ReportToolbar {

	public static int THIS_MONTH = 0;
	public static int LAST_MONTH = 1;
	public static int THIS_FINANCIAL_YEAR = 2;
	public static int LAST_FINANCIAL_YEAR = 3;
	public static int THIS_QUARTER = 4;
	public static int LAST_QUARTER = 5;

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		// TODO Auto-generated method stub

	}

}
