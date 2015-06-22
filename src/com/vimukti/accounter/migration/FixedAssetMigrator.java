package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FixedAsset;

public class FixedAssetMigrator implements IMigrator<FixedAsset> {
	@Override
	public JSONObject migrate(FixedAsset obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject);
		jsonObject.put("status", PicklistUtilMigrator
				.getFixedAssetStatusIdentifier(obj.getStatus()));
		jsonObject.put("name", obj.getName());
		jsonObject.put("description", obj.getDescription());
		jsonObject.put("assetNumber", obj.getAssetNumber());
		jsonObject.put("assetAccount",
				context.get("Account", obj.getAssetAccount().getID()));
		jsonObject.put("purchaseDate", obj.getPurchaseDate().getAsDateObject());
		jsonObject.put("purchasePrice", obj.getPurchasePrice());
		jsonObject.put("assetType", obj.getAssetType());
		jsonObject.put("depreciationRate", obj.getDepreciationRate());
		jsonObject.put(
				"depreciationMethod",
				context.getPickListContext().get(
						"DepreciationMethod",
						PicklistUtilMigrator.depreciationMethodIdentity(obj
								.getDepreciationMethod())));

		Account accumulatedDepreciationAccount = obj
				.getAccumulatedDepreciationAccount();
		if (accumulatedDepreciationAccount != null) {
			jsonObject.put(
					"accumulatedDepreciationAccount",
					context.get("Account",
							accumulatedDepreciationAccount.getID()));
		}
		jsonObject.put("accumulatedDepreciationAmount",
				obj.getAccumulatedDepreciationAmount());
		jsonObject.put("soldOrDisposedDate", obj.getSoldOrDisposedDate()
				.getAsDateObject());
		Account accountForSale = obj.getAccountForSale();
		if (accountForSale != null) {
			jsonObject.put("accountForSale",
					context.get("Account", accountForSale.getID()));
		}
		jsonObject.put("salePrice", obj.getSalePrice());
		// noDepreciation is not found
		jsonObject.put("depreciationTillDate", obj.getDepreciationTillDate()
				.getAsDateObject());
		jsonObject.put("notes", obj.getNotes());
		Account lossOrGainOnDisposalAccount = obj
				.getLossOrGainOnDisposalAccount();
		if (lossOrGainOnDisposalAccount != null) {
			jsonObject
					.put("lossOrGainOnDisposalAccount",
							context.get("Account",
									lossOrGainOnDisposalAccount.getID()));
		}
		Account totalCapitalGain = obj.getTotalCapitalGain();
		if (totalCapitalGain != null) {
			jsonObject.put("tcGainAccount",
					context.get("Account", totalCapitalGain.getID()));
		}
		// depreciationToBePostedAmount is not found
		// rollBackDepreciationAmount is not found
		// journalEntry is not found
		return jsonObject;
	}
}