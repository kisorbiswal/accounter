package com.vimukti.accounter.developer.api.process.lists;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.ClientFixedAsset;

public class FixedAssetItemsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String string = req.getParameter("item_type");
		int type = 0;
		if (string != null) {
			try {
				type = Integer.parseInt(string);
			} catch (Exception e) {
				sendFail("Wrong paymentType");
			}
		}
		ArrayList<ClientFixedAsset> list = service.getFixedAssetList(type);
		sendResult(list);
	}

}
