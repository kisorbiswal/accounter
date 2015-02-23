package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TAXAdjustment;

public class TAXAdjustmentMigrator extends TransactionMigrator<TAXAdjustment> {
	@Override
	public JSONObject migrate(TAXAdjustment obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		jsonObject.put("taxAgency",
				context.get("TAXAgency", obj.getTaxAgency().getID()));
		jsonObject.put("taxItem",
				context.get("TaxItem", obj.getTaxItem().getID()));
		String type = "SalesType";
		if (obj.getType() == 1) {
			type = "PurchaseType";
		}
		jsonObject.put("taxItem",
				context.getPickListContext().get("TAXAccountType", type));

		jsonObject.put("adjustmentAccount",
				context.get("Account", obj.getAdjustmentAccount().getID()));
		String adjustmentType = "IncreaseTAXline";
		if (obj.getTransactionCategory() == 1) {
			adjustmentType = "DecreaseTAXline";
		}
		jsonObject.put(
				"taxItem",
				context.getPickListContext().get("TAXAdjustmentType",
						adjustmentType));

		jsonObject.put("amount", obj.getTransactionCategory());
		return jsonObject;
	}

}
