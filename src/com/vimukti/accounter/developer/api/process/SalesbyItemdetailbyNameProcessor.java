package com.vimukti.accounter.developer.api.process;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.developer.api.process.reports.ReportProcessor;
import com.vimukti.accounter.web.client.core.reports.BaseReport;

public class SalesbyItemdetailbyNameProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		init(req, resp);
		String itemName = (String) req.getAttribute("Name");
		List<? extends BaseReport> result = service.getAgedCreditors(itemName,
				clientFinanceStartDate, clientFinanceEndDate);

		sendResult(result);
	}
}