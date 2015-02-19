package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Vendor;

public class VendorMigrator {

	public static JSONObject migrate(Vendor vendor, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("taxId", vendor.getTaxId());
		jsonObject.put("vendorGroup",
				context.get("VendorGroup", vendor.getVendorGroup().getID()));
		jsonObject.put("vendorTDSCode",
				context.get("TaxtItem", vendor.getTAXItem().getID()));
		return jsonObject;
	}

}
