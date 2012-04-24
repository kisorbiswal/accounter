package com.vimukti.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.reports.BaseReport;

public class PriorreturnVatSummaryProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		init(req, resp);
		Long taxAgency = (Long) req.getAttribute("TaxAgency");
		List<? extends BaseReport> result = service.getPriorReturnVATSummary(
				taxAgency, endDate);

		sendResult(result);
	}
}