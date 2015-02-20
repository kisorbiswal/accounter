package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TDSDeductorMasters;
import com.vimukti.accounter.web.client.core.Utility;

public class TDSDeductorMastersMigrator implements
		IMigrator<TDSDeductorMasters> {

	@Override
	public JSONObject migrate(TDSDeductorMasters obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();

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
		jsonObject.put(
				"deductorType",
				context.getPickListContext().get("DeductorType",
						obj.getDeductorType()));
		jsonObject.put("govtState", obj.getGovtState());
		jsonObject.put("paoCode", obj.getPaoCode());
		jsonObject.put("paoRegistration", obj.getPaoRegistration());
		jsonObject.put("ddoCode", obj.getDdoCode());
		jsonObject.put("ddoRegistration", obj.getDdoRegistration());
		jsonObject.put("ministryDeptName", obj.getMinistryDeptName());
		jsonObject.put("ministryDeptOtherName", obj.getMinistryDeptOtherName());
		jsonObject.put("tanNumber", obj.getTanNumber());
		jsonObject.put("panNumber", obj.getPanNumber());
		jsonObject.put("stdCode", obj.getStdCode());
		jsonObject.put("isAddressSameForResopsiblePerson",
				obj.isAddressSameForResopsiblePerson());
		jsonObject.put("taxOfficeAddress", obj.getTaxOfficeAddress());

		return jsonObject;
	}
}