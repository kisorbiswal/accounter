package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.FixedAsset;

public class FixedAssetMigrator implements IMigrator<FixedAsset> {
	@Override
	public JSONObject migrate(FixedAsset obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		// Fixed Asset status
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		jsonObject.put("status", PicklistUtilMigrator
				.getFixedAssetStatusIdentifier(obj.getStatus()));
		jsonObject.put("name", obj.getName());
		// description
		jsonObject.put("description", obj.getDescription());
		// assetAccount
		Account assetAccount = obj.getAssetAccount();
		if (assetAccount != null) {
			jsonObject.put("assetAccount",
					context.get("Account", assetAccount.getID()));
		}
		// purchasePrice
		jsonObject.put("purchaseDate", obj.getPurchaseDate().getAsDateObject()
				.getTime());
		jsonObject.put("purchasePrice", obj.getPurchasePrice());
		// assetType
		jsonObject.put("assetType", obj.getAssetType());
		// depreciationRate
		jsonObject.put("depreciationRate", obj.getDepreciationRate() / 100);
		// depreciationMethod
		jsonObject.put("depreciationMethod", PicklistUtilMigrator
				.depreciationMethodIdentity(obj.getDepreciationMethod()));
		// depreciationExpenseAccount
		Account depreciationExpenseAccount = obj
				.getDepreciationExpenseAccount();
		if (depreciationExpenseAccount != null) {
			jsonObject.put("depreciationExpenseAccount",
					context.get("Account", depreciationExpenseAccount.getID()));
		}
		// accumulatedDepreciationAccount
		Account accumulatedDepreciationAccount = obj
				.getAccumulatedDepreciationAccount();
		if (accumulatedDepreciationAccount != null) {
			jsonObject.put(
					"accumulatedDepreciationAccount",
					context.get("Account",
							accumulatedDepreciationAccount.getID()));
		}
		// accumulatedDepreciationAmount
		jsonObject.put("accumulatedDepreciationAmount",
				obj.getAccumulatedDepreciationAmount());
		// soldOrDisposedDate
		FinanceDate soldOrDisposedDate = obj.getSoldOrDisposedDate();
		if (soldOrDisposedDate != null) {
			jsonObject.put("soldOrDisposedDate", soldOrDisposedDate
					.getAsDateObject().getTime());
		}
		// accountForSale
		Account accountForSale = obj.getAccountForSale();
		if (accountForSale != null) {
			jsonObject.put("accountForSale",
					context.get("Account", accountForSale.getID()));
		}
		// salesPrice
		jsonObject.put("salesPrice", obj.getSalePrice());
		// noDepreciation is not found
		// depreciationTillDate
		FinanceDate depreciationTillDate = obj.getDepreciationTillDate();
		if (depreciationTillDate != null) {
			jsonObject.put("depreciationTillDate", depreciationTillDate
					.getAsDateObject().getTime());
		}
		// notes
		jsonObject.put("notes", obj.getNotes());
		// lossOrDisposalAccount
		Account lossOrGainOnDisposalAccount = obj
				.getLossOrGainOnDisposalAccount();
		if (lossOrGainOnDisposalAccount != null) {
			jsonObject
					.put("lossOrDisposalAccount",
							context.get("Account",
									lossOrGainOnDisposalAccount.getID()));
		}
		// totalCapitalGain
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

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub
		
	}
}