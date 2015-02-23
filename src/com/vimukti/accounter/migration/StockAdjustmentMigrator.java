package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.StockAdjustment;
import com.vimukti.accounter.core.TransactionItem;

public class StockAdjustmentMigrator extends
		TransactionMigrator<StockAdjustment> {
	@Override
	public JSONObject migrate(StockAdjustment obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		jsonObject.put("warehouse",
				context.get("Warehouse", obj.getWareHouse().getID()));
		jsonObject.put("adjustmentAccount",
				context.get("Account", obj.getAdjustmentAccount().getID()));
		{
			List<TransactionItem> transactionItems = obj.getTransactionItems();
			JSONArray array = new JSONArray();
			for (TransactionItem item : transactionItems) {
				JSONObject jsonobjItem = new JSONObject();
				jsonobjItem.put("item",
						context.get("InventoryItem", item.getItem().getID()));
				jsonobjItem.put("comment", item.getDescription());
				{
					Quantity quantity = item.getQuantity();
					JSONObject objectQty = new JSONObject();
					objectQty.put("value", quantity.getValue());
					objectQty.put("unitFactor", quantity.getUnit().getFactor());
					objectQty.put("unit", quantity.getUnit());
					jsonobjItem.put("adjustQty", objectQty);
				}
				jsonobjItem.put("adjustRate", item.getEffectiveAmount());
				array.put(jsonobjItem);
			}
			jsonObject.put("stockAdjustmentItems", array);
		}
		return jsonObject;
	}
}
