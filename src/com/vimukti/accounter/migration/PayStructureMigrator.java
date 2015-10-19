package com.vimukti.accounter.migration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
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
		Map<String, List<Long>> childrenMap = context.getChildrenMap();
		String key = "payStructureItems-PayStructureItem";
		List<Long> list = childrenMap.get(key);
		if (list == null) {
			list = new ArrayList<Long>();
			childrenMap.put(key, list);
		}
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
			list.add(payStructureItem.getID());
		}
		payStructure.put("payStructureItems", payStructureItemJsons);

		return payStructure;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}