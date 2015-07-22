package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TDSResponsiblePerson;

public class TDSResponsiblePersonMigrator implements
		IMigrator<TDSResponsiblePerson> {

	@Override
	public JSONObject migrate(TDSResponsiblePerson obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("responsibleName", obj.getResponsibleName());
		jsonObject.put("designation", obj.getDesignation());
		jsonObject.put("branch", obj.getBranch());
		jsonObject.put("flatNo", obj.getFlatNo());
		jsonObject.put("buildingName", obj.getBuildingName());
		jsonObject.put("street", obj.getStreet());
		jsonObject.put("area", obj.getArea());
		jsonObject.put("city", obj.getCity());
		jsonObject.put("stateName", obj.getStateName());
		jsonObject.put("pinCode", obj.getPinCode());
		jsonObject.put("addressChanged", obj.isAddressChanged());
		jsonObject.put("telephoneNo", String.valueOf(obj.getTelephoneNumber()));
		jsonObject.put("faxNo", String.valueOf(obj.getFaxNo()));
		jsonObject.put("emailAddress", obj.getEmailAddress());
		jsonObject.put("financialYear", obj.getFinancialYear());
		jsonObject.put("assesmentYear", obj.getAssesmentYear());
		jsonObject.put("retutnType", getReturnTypeString(obj.getReturnType()));
		jsonObject.put("existingTDSassesses", obj.isExistingTDSassesse());
		jsonObject.put("panNumber", obj.getPanNumber());
		jsonObject.put("panRegistrationNumber",
				String.valueOf(obj.getPanRegistrationNumber()));
		jsonObject.put("mobilenumber", String.valueOf(obj.getMobileNumber()));
		jsonObject.put("stdCode", obj.getStdCode());
		return jsonObject;
	}

	public String getReturnTypeString(int returnType) {
		if (returnType == 1) {
			return "Electronic";
		} else {
			return "Digital";
		}
	}
}
