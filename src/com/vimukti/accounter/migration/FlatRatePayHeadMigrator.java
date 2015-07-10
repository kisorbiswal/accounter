package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FlatRatePayHead;

public class FlatRatePayHeadMigrator implements IMigrator<FlatRatePayHead> {

	@Override
	public JSONObject migrate(FlatRatePayHead obj, MigratorContext context)
			throws JSONException {
		JSONObject payHead = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, payHead, context);
		payHead.put("name", obj.getName());
		payHead.put("payHeadType",
				PicklistUtilMigrator.getPayHeadType(obj.getType()));
		payHead.put("isAffectNetSalary", obj.isAffectNetSalary());
		Account account = obj.getAccount();
		if (account != null) {
			JSONObject accountJson = new JSONObject();
			accountJson.put("name", account.getName());
			payHead.put("expenseAccount", accountJson);
		}
		payHead.put("calculationType", PicklistUtilMigrator
				.getCalculationType(obj.getCalculationType()));
		payHead.put("paySlipName", obj.getNameToAppearInPaySlip());
		payHead.put("isDeduction", obj.isDeduction());
		payHead.put("isEarning", obj.isEarning());
		Account assetAccount = obj.getAssetAccount();
		if (assetAccount != null) {
			JSONObject accountJson = new JSONObject();
			accountJson.put("name", assetAccount.getName());
			payHead.put("assetAccount", accountJson);
		}
		Account liabilityAccount = obj.getLiabilityAccount();
		if (liabilityAccount != null) {
			JSONObject accountJson = new JSONObject();
			accountJson.put("name", liabilityAccount.getName());
			payHead.put("statutoryLiabilityAccount", accountJson);
		}
		payHead.put("calculationPeriod", PicklistUtilMigrator
				.getCalculationPeriod(obj.getCalculationPeriod()));
		payHead.put("perDayCalculationBasis", PicklistUtilMigrator
				.getPerdayCalculationBasis(obj.getCalculationPeriod()));
		return payHead;
	}
}