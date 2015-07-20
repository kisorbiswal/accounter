package com.vimukti.accounter.migration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.BuildAssembly;

public class BuildAssemblyMigrator extends TransactionMigrator<BuildAssembly> {

	@Override
	public JSONObject migrate(BuildAssembly obj, MigratorContext context)
			throws JSONException {
		JSONObject buildAssembly = super.migrate(obj, context);
		CommonFieldsMigrator.migrateCommonFields(obj, buildAssembly, context);
		buildAssembly.put("inventoryAssembly",
				context.get("Item", obj.getInventoryAssembly().getID()));
		JSONArray items = buildAssembly.getJSONArray("transactionItems");
		if (items != null) {
			buildAssembly.remove("transactionItems");
			buildAssembly.put("assemblyItems", items);
		}
		buildAssembly.put("quantitytoBuild", obj.getQuantityToBuild());
		return buildAssembly;
	}
}
