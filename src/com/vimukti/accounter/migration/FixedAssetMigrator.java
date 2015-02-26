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
		jsonObject.put(
				"status",
				context.getPickListContext().get("FixedAssetStatus",
						getStatus(obj.getStatus())));
		jsonObject.put("name", obj.getName());
		jsonObject.put("description", obj.getDescription());
		jsonObject.put("assetNumber", obj.getAssetNumber());
		jsonObject.put("assetAccount",
				context.get("Account", obj.getAssetAccount().getID()));
		jsonObject.put("purchaseDate", obj.getPurchaseDate().getAsDateObject());
		jsonObject.put("purchasePrice", obj.getPurchasePrice());
		jsonObject.put("assetType", obj.getAssetType());
		jsonObject.put("depreciationRate", obj.getDepreciationRate());

		int depreciationMethod = obj.getDepreciationMethod();
		String depreciationString = "ReducingBalance";
		if (depreciationMethod == 1) {
			depreciationString = "StraightLine";
		}
		jsonObject.put(
				"depreciationMethod",
				context.getPickListContext().get("DepreciationMethod",
						depreciationString));

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

	private String getStatus(int i) {
		switch (i) {
		case 0:
			return "Pending";
		case 1:
			return "Register";
		case 2:
			return "Sell";
		case 3:
			return "Dispose";
		case 4:
			return "PartialDisposed";
		default:
			return "Pending";
		}
	}
	// FixedAssetStatus Pending{
	// name : 'Pending'
	// }
	// FixedAssetStatus Register{
	// name : 'Register'
	// }
	// FixedAssetStatus Sell{
	// name : 'Sell'
	// }
	// FixedAssetStatus Dispose{
	// name : 'Dispose'
	// }
	// FixedAssetStatus PartialDisposed{
	// name : 'Partial Disposed'
	// }
}