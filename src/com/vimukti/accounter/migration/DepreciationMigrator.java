package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Depreciation;

public class DepreciationMigrator implements IMigrator<Depreciation> {
	public JSONObject migrate(Depreciation depreciation, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(depreciation, jsonObject,
				context);
		jsonObject.put("status", PicklistUtilMigrator
				.depreciationStatusIdentity(depreciation.getStatus()));
		jsonObject.put("depreciateFrom", depreciation.getDepreciateFrom()
				.getAsDateObject().getTime());
		jsonObject.put("depreciateTo", depreciation.getDepreciateTo()
				.getAsDateObject().getTime());
		jsonObject
				.put("fixedAsset", context.get("FixexAsset", depreciation
						.getFixedAsset().getID()));
		// List<FixedAsset> fixedAssets = depreciation.getFixedAssets();
		// JSONArray array = new JSONArray();
		// for (FixedAsset fixedAsset : fixedAssets) {
		// JSONObject jsonObject1 = new JSONObject();
		// jsonObject1.put("fixedAsset",
		// context.get("FixedAsset", fixedAsset.getID()));
		// array.put(jsonObject1);
		// }
		// jsonObject.put("fixedAssets", array);
		// TODO
		jsonObject.put("depreciationFor", PicklistUtilMigrator
				.depreciationForIdentity(depreciation.getDepreciationFor()));
		jsonObject.put("RollBackDepreciationDate", depreciation
				.getRollBackDepreciationDate().getAsDateObject().getTime());
		return jsonObject;
	}
}