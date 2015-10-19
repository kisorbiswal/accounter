package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
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
		jsonObject.put("telephoneNumber",
				String.valueOf(obj.getTelephoneNumber()));
		jsonObject.put("faxNo", String.valueOf(obj.getFaxNo()));
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
		Address taxOfficeAdd = obj.getTaxOfficeAddress();
		if (taxOfficeAdd != null) {
			JSONObject taxOfficeAddess = new JSONObject();
			taxOfficeAddess.put("street", taxOfficeAdd.getStreet());
			taxOfficeAddess.put("city", taxOfficeAdd.getCity());
			taxOfficeAddess.put("stateOrProvince",
					taxOfficeAdd.getStateOrProvinence());
			taxOfficeAddess.put("zipOrPostalCode",
					taxOfficeAdd.getZipOrPostalCode());
			taxOfficeAddess.put("country", taxOfficeAdd.getCountryOrRegion());
			jsonObject.put("taxOfficeAddress", taxOfficeAddess);
		}
		// obj.getStatus() return 'Government' or 'Other'
		jsonObject.put("status", obj.getStatus());

		return jsonObject;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub
		
	}
}