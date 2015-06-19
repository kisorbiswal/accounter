package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TAXCode;

public class TAXCodeMigrator implements IMigrator<TAXCode> {

	@Override
	public JSONObject migrate(TAXCode taxcode, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(taxcode, jsonObject);
		jsonObject.put("name", taxcode.getName());
		jsonObject.put("isTaxable", taxcode.isTaxable());
		jsonObject.put("taxItemOrGroupForSales",
				taxcode.getTAXItemGrpForSales());
		jsonObject.put("taxItemOrGroupPurchases",
				taxcode.getTAXItemGrpForPurchases());
		jsonObject.put("description", taxcode.getDescription());
		jsonObject.put("isInactive", !taxcode.isActive());
		return jsonObject;
	}
}