package com.vimukti.accounter.migration;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.ReceiveVAT;
import com.vimukti.accounter.core.ReceiveVATEntries;
import com.vimukti.accounter.core.TAXAgency;

public class TaxRefundMigrator extends TransactionMigrator<ReceiveVAT> {

	@Override
	public JSONObject migrate(ReceiveVAT obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		jsonObject.put("depositIn",
				context.get("Account", obj.getDepositIn().getID()));
		jsonObject.put("filterbyTAXreturnendDate", obj
				.getReturnsDueOnOrBefore().getAsDateObject().getTime());
		TAXAgency taxAgency = obj.getTaxAgency();
		if (taxAgency != null) {
			jsonObject.put("involvedPayee",
					context.get("TaxAgency", taxAgency.getID()));
		}
		JSONArray array = new JSONArray();
		Set<ReceiveVATEntries> receiveVATEntriesList = obj
				.getReceiveVATEntriesList();
		for (ReceiveVATEntries vatEntries : receiveVATEntriesList) {
			JSONObject itemJson = new JSONObject();
			TAXAgency taxAgency2 = vatEntries.getTAXAgency();
			if (taxAgency2 != null) {
				itemJson.put("taxAgency",
						context.get("TaxAgency", taxAgency2.getID()));
			}
			itemJson.put("fileTax",
					context.get("FileTax", vatEntries.getTransaction().getID()));
			itemJson.put("taxDue", vatEntries.getBalance());
			itemJson.put("amountToReceive", vatEntries.getAmount());
			array.put(itemJson);
		}
		jsonObject.put("taxRefundItems", array);
		return jsonObject;
	}
}
