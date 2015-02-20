package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TDSResponsiblePerson;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

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
		jsonObject.put("telephoneNo", obj.getTelephoneNumber());
		jsonObject.put("faxNo", obj.getFaxNo());
		jsonObject.put("emailAddress", obj.getEmailAddress());
		jsonObject.put("financialYear", obj.getFinancialYear());
		jsonObject.put("assesmentYear", obj.getAssesmentYear());
		jsonObject.put(
				"retutnType",
				context.getPickListContext().get("RetutnType",
						getReturnTypeString(obj.getReturnType())));
		jsonObject.put("existingTDSassesses", obj.isExistingTDSassesse());
		jsonObject.put("panNumber", obj.getPanNumber());
		jsonObject.put("panRegistrationNumber", obj.getPanRegistrationNumber());
		jsonObject.put("mobilenumber", obj.getMobileNumber());
		jsonObject.put("stdCode", obj.getStdCode());
		return jsonObject;
	}

	public String getReturnTypeString(int returnType) {
		AccounterMessages messages = Global.get().messages();
		if (returnType == 1) {
			return messages.electronic();
		}
		return messages.digital();
	}
}
