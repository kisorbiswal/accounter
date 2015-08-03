package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.CompanyPreferences;

public class DefaultCustomerAndSalesSettingsMigrator implements
		IMigrator<CompanyPreferences> {

	@Override
	public JSONObject migrate(CompanyPreferences obj, MigratorContext context)
			throws JSONException {
		JSONObject customerAndSalesSetting = new JSONObject();

		customerAndSalesSetting
				.put("itemType", CustomerAndSalesSettingsMigrator
						.itemTypeByIdentity(true, true));
		customerAndSalesSetting.put("inventoryTracking", true);
		customerAndSalesSetting.put("haveMultipleWarehouses", true);
		customerAndSalesSetting.put("enableInventoryUnits",
				obj.isUnitsEnabled());
		customerAndSalesSetting.put("inventoryScheme",
				CustomerAndSalesSettingsMigrator.getInventorySchemeString(obj
						.getActiveInventoryScheme()));
		customerAndSalesSetting.put("useDelayedCharges", true);
		customerAndSalesSetting.put("enablePriceLevel", true);
		return customerAndSalesSetting;
	}

}
