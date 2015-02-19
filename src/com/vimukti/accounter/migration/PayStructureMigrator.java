package com.vimukti.accounter.migration;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.PayStructure;
import com.vimukti.accounter.core.PayStructureItem;

public class PayStructureMigrator implements IMigrator<PayStructure> {
	@Override
	public JSONObject migrate(PayStructure obj, MigratorContext context)
			throws JSONException {
		JSONObject payStructure = new JSONObject();
		if (obj.getEmployee() != null) {
			payStructure.put("employee",
					context.get("Employee", obj.getEmployee().getID()));
		}
		if (obj.getEmployeeGroup() != null) {
			payStructure.put("employeeGroup", context.get("EmployeeGroup", obj
					.getEmployeeGroup().getID()));
		}

		// Setting oneMany PayStructureItems of PayStructure
		List<PayStructureItem> payStructureItems = obj.getItems();
		JSONArray payStructureItemJsons = new JSONArray();
		for (PayStructureItem payStructureItem : payStructureItems) {
			JSONObject inJson = new JSONObject();
			inJson.put("payHead", context.get("PayHead", payStructureItem
					.getPayHead().getID()));
			inJson.put("rate", payStructureItem.getRate());
			inJson.put("effectiveFrom", payStructureItem.getEffectiveFrom()
					.getAsDateObject());
			// calculationPeriod, payHeadType and computedOn are Computation
			// fields
		}
		payStructure.put("payStructureItems", payStructureItemJsons);
		
		return payStructure;
	}
}