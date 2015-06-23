package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TDSDeductorMasters;

public class TDSDeductorMastersMigrator implements
		IMigrator<TDSDeductorMasters> {

	@Override
	public JSONObject migrate(TDSDeductorMasters obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		jsonObject.put("deductorName", obj.getDeductorName());
		jsonObject.put("branch", obj.getBranch());
		jsonObject.put("flatNo", obj.getFlatNo());
		jsonObject.put("buildingName", obj.getBuildingName());
		jsonObject.put("area", obj.getArea());
		jsonObject.put("city", obj.getCity());
		jsonObject.put("state", obj.getState());
		jsonObject.put("pinCode", obj.getPinCode());
		jsonObject.put("addressdChanged", obj.isAddressdChanged());
		jsonObject.put("telephoneNumber", obj.getTelephoneNumber());
		jsonObject.put("faxNo", obj.getFaxNo());
		jsonObject.put("emailID", obj.getEmailID());
		jsonObject.put("deductorType", PicklistUtilMigrator
				.getDeductorTypeIndentity(obj.getDeductorType()));
		jsonObject.put("govtState", obj.getGovtState());
		jsonObject.put("paoCode", obj.getPaoCode());
		jsonObject.put("paoRegistration", obj.getPaoRegistration());
		jsonObject.put("ddoCode", obj.getDdoCode());
		jsonObject.put("ddoRegistration", obj.getDdoRegistration());
		jsonObject.put("ministryDeptName", PicklistUtilMigrator
				.getMinistryDeptNameIdentity(obj.getMinistryDeptName()));
		jsonObject.put("ministryDeptOtherName", obj.getMinistryDeptOtherName());
		jsonObject.put("tanNumber", obj.getTanNumber());
		jsonObject.put("panNumber", obj.getPanNumber());
		jsonObject.put("stdCode", obj.getStdCode());
		jsonObject.put("isAddressSameForResopsiblePerson",
				obj.isAddressSameForResopsiblePerson());
		jsonObject.put("taxOfficeAddress", obj.getTaxOfficeAddress());
		// obj.getStatus() return 'Government' or 'Other'
		jsonObject.put("status", obj.getStatus());

		return jsonObject;
	}
}