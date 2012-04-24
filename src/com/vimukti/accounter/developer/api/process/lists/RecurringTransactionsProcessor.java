package com.vimukti.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RecurringTransactionsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		initTransactionList(req, resp);
		List<?> resultList = service.getRecurringsList(from.getDate(),
				to.getDate());
		sendResult(resultList);
	}

}
