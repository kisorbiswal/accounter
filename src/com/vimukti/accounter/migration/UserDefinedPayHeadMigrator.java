package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.UserDefinedPayHead;
import com.vimukti.accounter.web.client.core.ClientPayHead;

public class UserDefinedPayHeadMigrator implements
		IMigrator<UserDefinedPayHead> {

	@Override
	public JSONObject migrate(UserDefinedPayHead obj, MigratorContext context)
			throws JSONException {
		JSONObject payHead = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, payHead, context);
		payHead.put("name", obj.getName());
		payHead.put("payHeadType", ClientPayHead.getPayHeadType(obj.getType()));
		payHead.put("isAffectNetSalary", obj.isAffectNetSalary());
		Account account = obj.getAccount();
		if (account != null) {
			JSONObject accountJson = new JSONObject();
			accountJson.put("name", account.getName());
			payHead.put("expenseAccount", accountJson);
		}
		payHead.put("calculationType", obj.getCalculationType());
		payHead.put("paySlipName", obj.getNameToAppearInPaySlip());
		payHead.put("isDeduction", obj.isDeduction());
		payHead.put("isEarning", obj.isEarning());
		Account assetAccount = obj.getAssetAccount();
		if (assetAccount != null) {
			JSONObject assetJson = new JSONObject();
			assetJson.put("name", assetAccount.getName());
			payHead.put("assetAccount", assetJson);
		}
		Account liabilityAccount = obj.getLiabilityAccount();
		if (liabilityAccount != null) {
			JSONObject liabilityJson = new JSONObject();
			liabilityJson.put("name", liabilityAccount.getName());
			payHead.put("statutoryLiabilityAccount", liabilityJson);
		}
		return payHead;
	}
}
