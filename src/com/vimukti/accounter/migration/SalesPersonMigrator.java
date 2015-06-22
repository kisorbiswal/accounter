package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.SalesPerson;

public class SalesPersonMigrator implements IMigrator<SalesPerson> {

	@Override
	public JSONObject migrate(SalesPerson salesPerson, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(salesPerson, jsonObject);
		jsonObject.put("name", salesPerson.getName());
		jsonObject.put("fileAs", salesPerson.getFileAs());
		jsonObject.put("jobTitle", salesPerson.getJobTitle());
		jsonObject.put(
				"gender",
				context.getPickListContext().get("Gender",
						salesPerson.getGender()));
		jsonObject.put("dateOfBirth", salesPerson.getDateOfBirth()
				.getAsDateObject());
		jsonObject.put("dateOfHire", salesPerson.getDateOfHire()
				.getAsDateObject());
		jsonObject.put("dateOfLastReview", salesPerson
				.getFinanceDateOfLastReview().getAsDateObject());
		jsonObject.put("inActive", !salesPerson.isActive());
		jsonObject.put("dateOfRelease", salesPerson.getDateOfRelease()
				.getAsDateObject());
		jsonObject.put("phoneNo", salesPerson.getPhoneNo());
		jsonObject.put("email", salesPerson.getEmail());
		jsonObject.put("webPageAddress", salesPerson.getWebPageAddress());

		JSONObject jSONAddress = new JSONObject();
		Address address = salesPerson.getAddress();
		if (address != null) {
			jSONAddress.put("street", address.getStreet());
			jSONAddress.put("city", address.getCity());
			jSONAddress.put("stateOrProvince", address.getStateOrProvinence());
			jSONAddress.put("zipOrPostalCode", address.getZipOrPostalCode());
			jSONAddress.put("country", address.getCountryOrRegion());
			jsonObject.put("Addres", jSONAddress);
		}

		return jsonObject;
	}
}