package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.TAXCode;

public class CompanyDefatultTaxAgencyMigrator implements
		IMigrator<CompanyPreferences> {
	@Override
	public JSONObject migrate(CompanyPreferences obj, MigratorContext context)
			throws JSONException {
		JSONObject commonSettings = new JSONObject();
		commonSettings.put("id", context.get(CompanyMigrator.COMMON_SETTINGS,
				CompanyMigrator.COMMON_SETTINGS_OLD_ID));
		TAXCode defaultTaxCode = obj.getDefaultTaxCode();
		if (defaultTaxCode != null) {
			commonSettings.put("defaultTaxCode",
					context.get("TaxCode", defaultTaxCode.getID()));
		}
		return commonSettings;
	}

}
