package com.vimukti.accounter.migration;

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
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		Unit defaultUnit = null;
		for (Unit unit : obj.getUnits()) {
			if (unit.isDefault()) {
				defaultUnit = unit;
				break;
			}
		}

		JSONArray units = new JSONArray();
		for (Unit unit : obj.getUnits()) {
			JSONObject unitObject = new JSONObject();
			unitObject.put("name", unit.getType());
			unitObject.put("factor", unit.getFactor());
			units.put(unitObject);
		}

		jsonObject.put("identity", obj.getName());
		jsonObject.put("units", units);
		// jsonObject.put("defaultUnit", defaultUnit.getID());
		jsonObject.put("name", obj.getName());
		return jsonObject;
	}
}