package com.vimukti.accounter.migration;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.InventoryAssembly;
import com.vimukti.accounter.core.InventoryAssemblyItem;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Quantity;

public class InventoryAssemblyMigrator implements IMigrator<InventoryAssembly> {
	@Override
	public JSONObject migrate(InventoryAssembly obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		Set<InventoryAssemblyItem> components = obj.getComponents();
		JSONArray array = new JSONArray();
		for (InventoryAssemblyItem item : components) {
			JSONObject jsonitem = new JSONObject();
			Item inventoryItem = item.getInventoryItem();
			if (inventoryItem != null) {
				jsonitem.put("item", context.get("Item", inventoryItem.getID()));
			}
			jsonitem.put("description", item.getDiscription());
			{
				Quantity quantity = item.getQuantity();
				JSONObject objectQty = new JSONObject();
				objectQty.put("value", quantity.getValue());
				objectQty.put("unitFactor", quantity.getUnit().getFactor());
				objectQty.put("unit", quantity.getUnit());
				jsonitem.put("quantity", objectQty);
			}
			array.put(jsonitem);
		}
		jsonObject.put("assemblyItems", array);
		return jsonObject;
	}
}
