package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.CompanyPreferences;

public class DefaultCommonSettingsMigrator implements
		IMigrator<CompanyPreferences> {

	@Override
	public JSONObject migrate(CompanyPreferences obj, MigratorContext context)
			throws JSONException {
		JSONObject commonSettings = new JSONObject();
		commonSettings.put("autoApplycredits", false);
		commonSettings.put("useBillable", true);
		return commonSettings;
	}

}
