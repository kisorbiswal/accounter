package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.Vendor;

public class VendorTdsEnableMigrator implements IMigrator<Vendor> {
	boolean isResetTds;

	public VendorTdsEnableMigrator(boolean enableTds) {
		this.isResetTds = enableTds;
	}

	@Override
	public JSONObject migrate(Vendor obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", context.get("Vendor", obj.getID()));
		if (isResetTds) {
			jsonObject.put("tDSApplicable", obj.isTdsApplicable());
		} else {
			jsonObject.put("tDSApplicable", isResetTds);
		}
		TAXItem taxItem2 = obj.getTAXItem();
		if (taxItem2 != null) {
			jsonObject.put("vendorTDSCode",
					context.get("Tax", taxItem2.getID()));
		}
		return jsonObject;
	}
}
