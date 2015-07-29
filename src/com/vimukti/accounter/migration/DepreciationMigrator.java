package com.vimukti.accounter.migration;

import java.util.Date;

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
				.put("fixedAsset", context.get("FixedAsset", depreciation
						.getFixedAsset().getID()));
		jsonObject.put("depreciationFor", PicklistUtilMigrator
				.depreciationForIdentity(depreciation.getDepreciationFor()));
		// FinanceDate rollBackDepreciationDate = depreciation
		// .getRollBackDepreciationDate().getAsDateObject();
		// TODO above value all ways null because it is not saving in database
		jsonObject.put("RollBackDepreciationDate", new Date());
		return jsonObject;
	}
}