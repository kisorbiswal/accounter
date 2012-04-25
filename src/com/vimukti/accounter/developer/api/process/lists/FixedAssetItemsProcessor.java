package com.vimukti.accounter.developer.api.process.lists;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.ClientFixedAsset;

public class FixedAssetItemsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		init(req, resp);
		int type = readInt(req, "item_type");
		ArrayList<ClientFixedAsset> list = service.getFixedAssetList(type);
		sendResult(list);
	}

}
