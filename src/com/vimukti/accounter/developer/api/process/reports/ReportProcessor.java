package com.vimukti.accounter.developer.api.process.reports;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.developer.api.core.ApiProcessor;
import com.vimukti.accounter.web.client.IAccounterReportService;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public abstract class ReportProcessor extends ApiProcessor {
	protected ClientFinanceDate startDate;
	protected ClientFinanceDate endDate;
	protected IAccounterReportService service;

	protected void init(HttpServletRequest req, HttpServletResponse resp) {
		String methodName = req.getParameter("reporttype");
		if (methodName == null) {
			sendFail("reporttype should be present");
			return;
		}
		service = getS2sSyncProxy(req, "/do/accounter/report/rpc/service",
				IAccounterReportService.class);
		startDate = getClientFinanceDate(req
				.getParameter("StartDate"));
		endDate = getClientFinanceDate(req.getParameter("EndDate"));
		if (endDate == null || startDate == null) {
			sendFail("Wrong date formate");
			return;
		}
	}
}
