package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TAXAdjustment;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXItem;

public class TAXAdjustmentMigrator extends TransactionMigrator<TAXAdjustment> {
	@Override
	public JSONObject migrate(TAXAdjustment obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		TAXAgency taxAgency = obj.getTaxAgency();
		if (taxAgency != null) {
			jsonObject.put("taxAgency",
					context.get("TAXAgency", taxAgency.getID()));
			jsonObject
					.put("payee", context.get("TAXAgency", taxAgency.getID()));
		}
		TAXItem taxItem = obj.getTaxItem();
		if (taxItem != null) {
			jsonObject.put("taxItem", context.get("TaxItem", taxItem.getID()));
		}
		String type = "SalesType";
		if (obj.getType() == 1) {
			type = "PurchaseType";
		}
		jsonObject.put("type",
				context.getPickListContext().get("TAXAccountType", type));

		jsonObject.put("adjustmentAccount",
				context.get("Account", obj.getAdjustmentAccount().getID()));
		String adjustmentType = "IncreaseTAXline";
		if (obj.getTransactionCategory() == 1) {
			adjustmentType = "DecreaseTAXline";
		}
		jsonObject.put(
				"adjustmentType",
				context.getPickListContext().get("TAXAdjustmentType",
						adjustmentType));

		jsonObject.put("amount", obj.getTransactionCategory());
		return jsonObject;
	}

}
