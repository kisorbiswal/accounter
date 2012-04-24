package com.vimukti.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.IAccounterCore;

public class AccountsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		initTransactionList(req, resp);

		String accountType = req.getParameter("accountType");
		int type = 0;
		if (accountType == null) {
			type = 0;
		} else {
			try {
				type = Integer.parseInt(accountType);
			} catch (Exception e) {
				sendFail("Wrong accountType");
			}
		}

		List<? extends IAccounterCore> resultList = service.getAccounts(type,
				isActive, start, length);

		sendResult(resultList);
	}

}
