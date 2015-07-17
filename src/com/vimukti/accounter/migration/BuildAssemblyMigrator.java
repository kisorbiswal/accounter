package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.BuildAssembly;
import com.vimukti.accounter.core.TransactionItem;

public class BuildAssemblyMigrator extends TransactionMigrator<BuildAssembly> {

	@Override
	public JSONObject migrate(BuildAssembly obj, MigratorContext context)
			throws JSONException {
		JSONObject buildAssembly = super.migrate(obj, context);
		CommonFieldsMigrator.migrateCommonFields(obj, buildAssembly, context);
		buildAssembly.put("inventoryAssembly", context.get("InventoryAssembly",
				obj.getInventoryAssembly().getID()));
		JSONArray array = new JSONArray();
		List<TransactionItem> transactionItems = obj.getTransactionItems();
		for (TransactionItem transactionItem : transactionItems) {
			JSONObject object = new JSONObject();
			object.put("item",
					context.get("Item", transactionItem.getItem().getID()));
			object.put("description", transactionItem.getDescription());
			array.put(object);
		}
		buildAssembly.put("assemblyItems", array);
		buildAssembly.put("quantitytoBuild", obj.getQuantityToBuild());
		return buildAssembly;
	}
}
