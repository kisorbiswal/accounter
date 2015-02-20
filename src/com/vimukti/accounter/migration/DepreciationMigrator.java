package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Depreciation;

public class DepreciationMigrator implements IMigrator<Depreciation> {
	public JSONObject migrate(Depreciation depreciation, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		int status = depreciation.getStatus();
		String statausValue="Approve";
		if(status==1){
			statausValue="Rollback";
		}
		jsonObject.put("status", statausValue);
		jsonObject.put("depreciateFrom", depreciation.getDepreciateFrom());
		jsonObject.put("depreciateTo", depreciation.getDepreciateTo());
		jsonObject.put("fixedAsset", depreciation.getFixedAsset());
		// List<FixedAsset> fixedAssets = depreciation.getFixedAssets();
		// JSONArray array = new JSONArray();
		// for (FixedAsset fixedAsset : fixedAssets) {
		// JSONObject jsonObject1 = new JSONObject();
		// jsonObject1.put("fixedAsset",
		// context.get("FixedAsset", fixedAsset.getID()));
		// array.put(jsonObject1);
		// }
		// jsonObject.put("fixedAssets", array);
		//TODO
		jsonObject.put("depreciationFor", depreciation.getDepreciationFor());
		jsonObject.put("RollBackDepreciationDate",
				depreciation.getRollBackDepreciationDate());
		return jsonObject;
	}
}