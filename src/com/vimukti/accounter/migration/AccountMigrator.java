package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Utility;

public class AccountMigrator implements IMigrator<Account> {

	@Override
	public JSONObject migrate(Account account, MigratorContext context) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
			CommonFieldsMigrator.migrateCommonFields(account, jsonObject);
			jsonObject.put("number", account.getNumber());
			jsonObject.put("name", account.getName());
			jsonObject.put("asOf", account.getAsOf());
			if (account.getParent() != null) {
				jsonObject.put("subAccountOf",
						context.get("Account", account.getParent().getID()));
			}

			jsonObject.put(
					"type",
					context.getPickListContext().get(
							"AccountType",
							PicklistUtilMigrator.getAccountTypeIdentity(account
									.getType())));
			jsonObject.put("currency", account.getCurrency().getFormalName());
			jsonObject.put("inactive", !account.getIsActive());
			jsonObject.put("description", account.getComment());
			jsonObject.put("openingBalance", account.getOpeningBalance());
			jsonObject.put("paypalEmail", account.getPaypalEmail());
			jsonObject.put(
					"cashFlowCategory",
					context.getPickListContext().get(
							"CashFlowCategory",
							Utility.getCashFlowCategoryName(account
									.getCashFlowCategory())));
			jsonObject.put("currencyFactor", account.getCurrencyFactor());
			jsonObject.put("isIncrease", account.isIncrease());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
}