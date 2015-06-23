package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXReturn;
import com.vimukti.accounter.core.TAXReturnEntry;

public class FileTaxMigrator extends TransactionMigrator<TAXReturn> {

	@Override
	public JSONObject migrate(TAXReturn obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		TAXAgency taxAgency = obj.getTaxAgency();
		if (taxAgency != null) {
			jsonObject.put("taxAgency",
					context.get("TaxAgency", taxAgency.getID()));
		}
		jsonObject.put("fromDate", obj.getPeriodStartDate().getAsDateObject()
				.getTime());
		jsonObject
				.put("to", obj.getPeriodEndDate().getAsDateObject().getTime());
		List<TAXReturnEntry> taxReturnEntries = obj.getTaxReturnEntries();
		JSONArray array = new JSONArray();
		for (TAXReturnEntry taxReturnEntry : taxReturnEntries) {
			JSONObject transactionJson = new JSONObject();
			// TODO TaxRateCalculation Object is not there in account
			// FileTaxItem Completely depends on that.
			array.put(transactionJson);
		}
		jsonObject.put("taxItems", array);
		return jsonObject;
	}
}
