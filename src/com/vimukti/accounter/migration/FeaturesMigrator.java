package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.CompanyPreferences;

public class FeaturesMigrator implements IMigrator<CompanyPreferences> {

	@Override
	public JSONObject migrate(CompanyPreferences obj, MigratorContext context)
			throws JSONException {
		JSONObject features = new JSONObject();
		features.put("enableLocationTracking", obj.isLocationTrackingEnabled());
		features.put("enableClassTracking", obj.isClassTrackingEnabled());
		features.put("classTrackingType",
				obj.isClassPerDetailLine() ? "OnePerDetailLine"
						: "OnePerTransaction");
		features.put("enableShipping", obj.isDoProductShipMents());
		features.put("projectTracking", obj.isJobTrackingEnabled());
		// department and projectManagement are not found
		return features;
	}

}
