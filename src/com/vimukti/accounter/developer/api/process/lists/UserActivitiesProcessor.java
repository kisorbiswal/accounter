package com.vimukti.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserActivitiesProcessor extends ListProcessor {
	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		initTransactionList(req, resp);
		String customise = req.getParameter("customise");
		int value = 0;
		if (customise == null) {
			value = 0;
		} else {
			try {
				value = Integer.parseInt(customise);
			} catch (Exception e) {
				sendFail("Wrong customise value");
			}
		}

		List<?> resultList = service.getUsersActivityLog(from, to, start,
				length, value);

		sendResult(resultList);
	}
}
