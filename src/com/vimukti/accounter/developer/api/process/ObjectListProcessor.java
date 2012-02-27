package com.vimukti.accounter.developer.api.process;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.web.client.IAccounterHomeViewService;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class ObjectListProcessor extends ApiProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String listName = req.getParameter("name");
		if (listName == null) {
			sendFail("listname should be present");
			return;
		}

		IAccounterHomeViewService service = getS2sSyncProxy(req,
				"/do/accounter/home/rpc/service",
				IAccounterHomeViewService.class);
		boolean isActive = false;
		int start = 0;
		int length = -1;
		try {
			String actPar = req.getParameter("active");
			isActive = actPar == null ? false : Boolean.parseBoolean(actPar);
			String startPar = req.getParameter("start");
			start = startPar == null ? 0 : Integer.parseInt(startPar);
			String lengthPar = req.getParameter("length");
			length = lengthPar == null ? -1 : Integer.parseInt(lengthPar);
		} catch (Exception e) {
			sendFail("Wrong parameter value(s)");
			return;
		}

		List<? extends IAccounterCore> resultList = null;
		if (listName.equals("customers")) {
			try {
				resultList = service.getPayeeList(ClientPayee.TYPE_CUSTOMER,
						isActive, start, length);
			} catch (AccounterException e) {
				sendFail(e.getMessage());
				return;
			}
		} else if (listName.equals("items")) {
			// TODO write query for this, set pagination values
			List<Item> items = null;
			List<ClientItem> clientItems = new ArrayList<ClientItem>();
			new ClientConvertUtil().toCollection(items, clientItems);
			resultList = clientItems;
		} else if (listName.equals("")) {

		}
		sendResult(resultList);
	}

}
