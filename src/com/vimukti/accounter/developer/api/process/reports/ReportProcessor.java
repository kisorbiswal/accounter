package com.vimukti.accounter.developer.api.process.reports;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.developer.api.process.ApiProcessor;
import com.vimukti.accounter.web.client.IAccounterReportService;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public abstract class ReportProcessor extends ApiProcessor {
	protected ClientFinanceDate clientFinanceEndDate;
	protected ClientFinanceDate clientFinanceStartDate;
	protected IAccounterReportService service;

	protected void init(HttpServletRequest req, HttpServletResponse resp) {
		String methodName = req.getParameter("reporttype");
		if (methodName == null) {
			sendFail("reporttype should be present");
			return;
		}
		service = getS2sSyncProxy(req, "/do/accounter/report/rpc/service",
				IAccounterReportService.class);
		clientFinanceStartDate = getClientFinanceDate(req
				.getParameter("StartDate"));
		clientFinanceEndDate = getClientFinanceDate(req.getParameter("EndDate"));
		if (clientFinanceEndDate == null || clientFinanceStartDate == null) {
			sendFail("Wrong date formate");
			return;
		}
	}
}
