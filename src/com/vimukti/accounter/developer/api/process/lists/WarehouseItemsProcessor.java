package com.vimukti.accounter.developer.api.process.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WarehouseItemsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String warehouse = req.getParameter("warehouse");
		if (warehouse == null) {
			sendFail("Warehouse missing");
			return;
		}
		long warehouseNo = Long.parseLong(warehouse);
		List<?> resultList = service.getItemStatuses(warehouseNo);
		sendResult(resultList);
	}

}
