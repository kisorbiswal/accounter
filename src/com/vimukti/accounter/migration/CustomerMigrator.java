package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Customer;

public class CustomerMigrator implements IMigrator<Customer> {

	@Override
	public JSONObject migrate(Customer obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("priceLevel",
				context.get("PriceLevel", obj.getPriceLevel().getID()));
		jsonObject.put("salesPerson",
				context.get("SalesPerson", obj.getSalesPerson().getID()));
		jsonObject.put("customerGroup",
				context.get("CustomerGroup", obj.getCustomerGroup().getID()));
		jsonObject.put("cSTNumber", obj.getCSTno());
		jsonObject.put("taxPayerIdentificationNo", obj.getTINNumber());
		return jsonObject;
	}

}
