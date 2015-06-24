package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Utility;

public class AccountMigrator implements IMigrator<Account> {

	@Override
	public JSONObject migrate(Account account, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(account, jsonObject, context);
		jsonObject.put("number", account.getNumber());
		jsonObject.put("name", account.getName());
		FinanceDate asOf = account.getAsOf();
		if (asOf != null)
			jsonObject.put("asOf", account.getAsOf().getAsDateObject()
					.getTime());
		if (account.getParent() != null) {
			jsonObject.put("subAccountOf",
					context.get("Account", account.getParent().getID()));
		}
		JSONObject accountTypeJSON = new JSONObject();
		accountTypeJSON.put("identity",
				PicklistUtilMigrator.getAccountTypeIdentity(account.getType()));
		jsonObject.put("type", accountTypeJSON);
		JSONObject currencyJson = new JSONObject();
		currencyJson.put("identity", account.getCurrency().getFormalName());
		jsonObject.put("currency", currencyJson);
		jsonObject.put("inactive", !account.getIsActive());
		jsonObject.put("description", account.getComment());
		jsonObject.put("openingBalance", account.getOpeningBalance());
		jsonObject.put("paypalEmail", account.getPaypalEmail());
		jsonObject.put("cashFlowCategory",
				Utility.getCashFlowCategoryName(account.getCashFlowCategory()));
		jsonObject.put("currencyFactor", account.getCurrencyFactor());
		jsonObject.put("isIncrease", account.isIncrease());
		return jsonObject;
	}
}