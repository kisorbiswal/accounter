package com.vimukti.accounter.migration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.core.Unit;

public class MeasurementMigrator implements IMigrator<Measurement> {

	@Override
	public JSONObject migrate(Measurement obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		Map<String, List<Long>> childrenMap = context.getChildrenMap();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		String key = "units-Unit";
		List<Long> list = childrenMap.get(key);
		if (list == null) {
			list = new ArrayList<Long>();
			childrenMap.put(key, list);
		}
		JSONArray units = new JSONArray();
		for (Unit unit : obj.getUnits()) {
			JSONObject unitObject = new JSONObject();
			unitObject.put("name", unit.getType());
			unitObject.put("factor", unit.getFactor());
			units.put(unitObject);
			list.add(unit.getID());
			if (unit.isDefault()) {
				unitObject.put("_localId", unit.getID());
				jsonObject.put("defaultUnit", unit.getID());
			}
		}
		jsonObject.put("identity", MigratorUtil.asIdentifier(obj.getName()));
		jsonObject.put("units", units);
		jsonObject.put("name", obj.getName());
		return jsonObject;
	}
}
