package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.SalesPerson;

public class SalesPersonMigrator implements IMigrator<SalesPerson> {

	@Override
	public JSONObject migrate(SalesPerson salesPerson, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(salesPerson, jsonObject,
				context);
		jsonObject.put("name", salesPerson.getFirstName());
		jsonObject.put("fileAs", salesPerson.getFileAs());
		jsonObject.put("jobTitle", salesPerson.getJobTitle());
		jsonObject.put("gender", salesPerson.getGender());
		FinanceDate dateOfBirth = salesPerson.getDateOfBirth();
		if (dateOfBirth != null) {
			jsonObject.put("dateOfBirth", dateOfBirth.getAsDateObject()
					.getTime());
		}
		FinanceDate dateOfHire = salesPerson.getDateOfHire();
		if (dateOfHire != null) {
			jsonObject
					.put("dateOfHire", dateOfHire.getAsDateObject().getTime());
		}
		FinanceDate financeDateOfLastReview = salesPerson
				.getFinanceDateOfLastReview();
		if (financeDateOfLastReview != null) {
			jsonObject.put("dateOfLastReview", financeDateOfLastReview
					.getAsDateObject().getTime());
		}
		jsonObject.put("inActive", !salesPerson.isActive());
		FinanceDate dateOfRelease = salesPerson.getDateOfRelease();
		if (dateOfRelease != null) {
			jsonObject.put("dateOfRelease", dateOfRelease.getAsDateObject()
					.getTime());
		}
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
			jsonObject.put("address", jSONAddress);
		}
		return jsonObject;
	}
}