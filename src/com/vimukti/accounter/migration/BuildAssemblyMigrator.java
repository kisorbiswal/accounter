package com.vimukti.accounter.migration;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.core.BuildAssembly;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.InventoryAssembly;
import com.vimukti.accounter.core.InventoryAssemblyItem;
import com.vimukti.accounter.core.Job;
import com.vimukti.accounter.core.Location;

public class BuildAssemblyMigrator extends TransactionMigrator<BuildAssembly> {

	@Override
	public JSONObject migrate(BuildAssembly obj, MigratorContext context)
			throws JSONException {
		JSONObject buildAssembly = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, buildAssembly, context);
		InventoryAssembly inventoryAssembly = obj.getInventoryAssembly();
		buildAssembly.put("inventoryAssembly",
				context.get("Item", inventoryAssembly.getID()));
		buildAssembly.put("date", obj.getDate().getAsDateObject().getTime());
		buildAssembly.put("number", obj.getNumber());
		AccounterClass accounterClass = obj.getAccounterClass();
		if (accounterClass != null) {
			buildAssembly.put("accountClass",
					context.get("AccountClass", accounterClass.getID()));
		}
		Location location = obj.getLocation();
		if (location != null) {
			buildAssembly.put("location",
					context.get("Location", location.getID()));
		}
		JSONArray array = new JSONArray();
		Set<InventoryAssemblyItem> transactionItems = inventoryAssembly
				.getComponents();
		for (InventoryAssemblyItem transactionItem : transactionItems) {
			JSONObject object = new JSONObject();
			object.put(
					"item",
					context.get("InventoryAssemblyItem",
							transactionItem.getID()));
			object.put("description", transactionItem.getDiscription());
			array.put(object);
		}
		buildAssembly.put("assemblyItems", array);
		Currency currency = obj.getCurrency();
		if (currency != null) {
			JSONObject currencyJSON = new JSONObject();
			currencyJSON.put("identity", currency.getFormalName());
			buildAssembly.put("currency", currencyJSON);
		}
		buildAssembly.put("currencyFactor", obj.getCurrencyFactor());
		buildAssembly.put("notes", obj.getMemo());
		buildAssembly.put("transactionType", PicklistUtilMigrator
				.getTransactionTypeIdentifier(obj.getType(), 0));
		Job job = obj.getJob();
		if (job != null) {
			buildAssembly.put("project", context.get("Job", job.getID()));
		}
		buildAssembly.put("quantitytoBuild", obj.getQuantityToBuild());
		return buildAssembly;
	}
}
