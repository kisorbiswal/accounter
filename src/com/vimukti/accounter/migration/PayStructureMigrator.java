package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.PayHead;
import com.vimukti.accounter.core.PayStructure;
import com.vimukti.accounter.core.PayStructureItem;

public class PayStructureMigrator implements IMigrator<PayStructure> {
	@Override
	public JSONObject migrate(PayStructure obj, MigratorContext context)
			throws JSONException {
		JSONObject payStructure = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, payStructure, context);

		if (obj.getEmployee() != null) {
			payStructure.put("type", "Employee");
			payStructure.put("employee",
					context.get("Employee", obj.getEmployee().getID()));
		} else {
			payStructure.put("type", "EmployeeGroup");
			payStructure.put("employeeGroup", context.get("EmployeeGroup", obj
					.getEmployeeGroup().getID()));
		}

		// Setting oneMany PayStructureItems of PayStructure
		List<PayStructureItem> payStructureItems = obj.getItems();
		JSONArray payStructureItemJsons = new JSONArray();
		for (PayStructureItem payStructureItem : payStructureItems) {
			JSONObject inJson = new JSONObject();
			PayHead payHead = payStructureItem.getPayHead();
			if (payHead != null) {
				inJson.put("payHead", context.get("PayHead", payHead.getID()));
			}
			inJson.put("rate", payStructureItem.getRate());
			inJson.put("effectiveFrom", payStructureItem.getEffectiveFrom()
					.getAsDateObject().getTime());
			payStructureItemJsons.put(inJson);
		}
		payStructure.put("payStructureItems", payStructureItemJsons);

		return payStructure;
	}
}