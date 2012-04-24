package com.vimukti.accounter.developer.api.process.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccountRegistersProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		init(req, resp);

		int start = 0;
		int length = 0;
		try {
			String startPar = req.getParameter("start");
			start = startPar == null ? 0 : Integer.parseInt(startPar);
			String lengthPar = req.getParameter("length");
			length = lengthPar == null ? -1 : Integer.parseInt(lengthPar);
		} catch (Exception e) {
			sendFail("Wrong parameter value(s)");
			return;
		}

		String takenAccount = req.getParameter("account");
		if (takenAccount == null) {
			sendFail("account parameter missing");
			return;
		}
		long takenAcountNo = 0;
		try {
			takenAcountNo = Long.parseLong(takenAccount);
		} catch (Exception e) {
			sendFail("Wrong account value");
			return;
		}

		List<?> resultList = service.getAccountRegister(startDate, endDate,
				takenAcountNo, start, length);
		sendResult(resultList);
	}

}
