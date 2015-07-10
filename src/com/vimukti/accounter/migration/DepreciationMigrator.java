package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Depreciation;
import com.vimukti.accounter.core.FinanceDate;

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
		FinanceDate rollBackDepreciationDate = depreciation
				.getRollBackDepreciationDate();
		if (rollBackDepreciationDate != null) {
			jsonObject.put("RollBackDepreciationDate", rollBackDepreciationDate
					.getAsDateObject().getTime());
		}
		return jsonObject;
	}
}