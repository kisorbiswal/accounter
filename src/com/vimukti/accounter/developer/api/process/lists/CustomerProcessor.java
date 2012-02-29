package com.vimukti.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class CustomerProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		initObjectsList(req, resp);

		List<? extends IAccounterCore> resultList = null;
		try {
			resultList = service.getPayeeList(ClientPayee.TYPE_CUSTOMER,
					isActive, start, length);
		} catch (AccounterException e) {
			sendFail(e.getMessage());
			return;
		}
		sendResult(resultList);
	}

}
