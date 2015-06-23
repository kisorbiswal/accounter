package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Warehouse;

public class WarehouseMigrator implements IMigrator<Warehouse> {

	@Override
	public JSONObject migrate(Warehouse obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		jsonObject.put("warehouseCode", obj.getWarehouseCode());
		jsonObject.put("warehouseName", obj.getName());
		Contact contact = obj.getContact();
		if (contact != null) {
			jsonObject.put("contactName", contact.getName());
			jsonObject.put("contactNumber", contact.getBusinessPhone());
		}
		jsonObject.put("mobileNumber", obj.getMobileNumber());
		jsonObject.put("dDInumber", obj.getDDINumber());
		jsonObject.put("defaultWarehouse", obj.isDefaultWarehouse());
		// Setting Address reference
		Address address = obj.getAddress();
		if (address != null) {
			JSONObject jsonAddress = new JSONObject();
			jsonAddress.put("street", address.getStreet());
			jsonAddress.put("city", address.getCity());
			jsonAddress.put("stateOrProvince", address.getStateOrProvinence());
			jsonAddress.put("zipOrPostalCode", address.getZipOrPostalCode());
			jsonAddress.put("country", address.getCountryOrRegion());
			jsonObject.put("address", jsonAddress);
		}
		return jsonObject;
	}
}