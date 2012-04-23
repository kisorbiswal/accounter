package com.vimukti.accounter.developer.api.process.lists;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.ui.Accounter;

public class ItemsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		initObjectsList(req, resp);
		String itemType = req.getParameter("itemType");
		int type = 0;
		try {
			type = Integer.parseInt(itemType);
		} catch (Exception e) {
			sendFail("Item type is wrong formate");
			return;
		}

		List<ClientItem> resultList = new ArrayList<ClientItem>();
		List<ClientItem> itemsList = new ArrayList<ClientItem>();
		
		itemsList = getPurchaseItems();
		for (ClientItem clientItem : itemsList) {
			if (isActive) {
				if (clientItem.isActive() == true)
					resultList.add(clientItem);
			} else if (clientItem.isActive() == false) {
				resultList.add(clientItem);
			}
		}
		sendResult(resultList);
	}

	private List<ClientItem> getPurchaseItems() {
		return null;
	}

}
