package com.vimukti.accounter.migration;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.StockTransfer;
import com.vimukti.accounter.core.StockTransferItem;

public class WarehouseTransferMigrator implements IMigrator<StockTransfer> {

	@Override
	public JSONObject migrate(StockTransfer obj, MigratorContext context)
			throws JSONException {
		JSONObject stockTransfer = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, stockTransfer, context);
		stockTransfer.put("fromWareHouse",
				context.get("Warehouse", obj.getFromWarehouse().getID()));
		stockTransfer.put("to",
				context.get("Warehouse", obj.getToWarehouse().getID()));
		stockTransfer.put("comment", obj.getMemo());

		JSONArray itemsArray = new JSONArray();
		Set<StockTransferItem> stockTransferItems = obj.getStockTransferItems();
		for (StockTransferItem stockTransferItem : stockTransferItems) {
			JSONObject stockItem = new JSONObject();
			stockItem.put("item", context.get("InventoryItem",
					stockTransferItem.getItem().getID()));
			{
				Quantity quantity = stockTransferItem.getQuantity();
				JSONObject jsonQuantity = new JSONObject();
				jsonQuantity.put("value", quantity.getValue());
				jsonQuantity.put("unit",
						context.get("Unit", quantity.getUnit().getID()));
				stockItem.put("transferQuantity", jsonQuantity);
			}
			itemsArray.put(stockItem);
		}
		stockTransfer.put("warehouseTransferItems", itemsArray);
		return stockTransfer;
	}
}
