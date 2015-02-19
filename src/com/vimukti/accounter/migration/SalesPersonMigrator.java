package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.SalesPerson;

public class SalesPersonMigrator implements IMigrator<SalesPerson> {

	@Override
	public JSONObject migrate(SalesPerson salesPerson, MigratorContext context)throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", salesPerson.getName());
		jsonObject.put("fileAs", salesPerson.getFileAs());
		jsonObject.put("jobTitle",salesPerson.getJobTitle());
		jsonObject.put("gender",salesPerson.getGender());
		jsonObject.put("dateOfBirth",salesPerson.getDateOfBirth());
		jsonObject.put("dateOfHire",salesPerson.getDateOfHire());
		jsonObject.put("dateOfLastReview",salesPerson.getFinanceDateOfLastReview());
		jsonObject.put("inActive",salesPerson.isActive());
		jsonObject.put("dateOfRelease",salesPerson.getDateOfRelease());
		jsonObject.put("address",salesPerson.getAddress());
		jsonObject.put("phoneNo",salesPerson.getPhoneNo());
		jsonObject.put("email",salesPerson.getEmail());
		jsonObject.put("webPageAddress",salesPerson.getWebPageAddress());
		return jsonObject;
	}
}