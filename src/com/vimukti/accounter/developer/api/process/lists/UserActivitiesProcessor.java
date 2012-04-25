package com.vimukti.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserActivitiesProcessor extends ListProcessor {
	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		initTransactionList(req, resp);
		int value = readInt(req, "customise", 0);

		List<?> resultList = service.getUsersActivityLog(from, to, start,
				length, value);

		sendResult(resultList);
	}
}
