package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXItemGroup;

public class TAXCodeMigrator implements IMigrator<TAXCode> {

	@Override
	public JSONObject migrate(TAXCode taxcode, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(taxcode, jsonObject, context);
		jsonObject.put("name", taxcode.getName());
		jsonObject.put("isTaxable", taxcode.isTaxable());
		TAXItemGroup taxItemGrpForSales = taxcode.getTAXItemGrpForSales();
		if (taxItemGrpForSales != null) {
			jsonObject.put("taxItemOrGroupForSales", taxcode
					.getTAXItemGrpForSales().getID());
		}
		TAXItemGroup taxItemGrpForPurchases = taxcode
				.getTAXItemGrpForPurchases();
		if (taxItemGrpForPurchases != null) {
			jsonObject.put("taxItemOrGroupForPurchases",
					taxItemGrpForPurchases.getID());
		}
		jsonObject.put("description", taxcode.getDescription());
		jsonObject.put("isInactive", !taxcode.isActive());
		return jsonObject;
	}
}