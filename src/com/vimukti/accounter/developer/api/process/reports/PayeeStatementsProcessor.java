package com.vimukti.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.reports.BaseReport;

public class PayeeStatementsProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		init(req, resp);
		boolean isVendor = (Boolean) req.getAttribute("IsVendor");
		int id = (Integer) req.getAttribute("Id");
		List<? extends BaseReport> result = service.getStatements(isVendor, id,
				0, startDate, endDate);

		sendResult(result);
	}
}
