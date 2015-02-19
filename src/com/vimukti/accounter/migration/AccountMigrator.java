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
			jsonObject.put("number", account.getNumber());
			jsonObject.put("name", account.getName());
			jsonObject.put("asOf", account.getAsOf());
			jsonObject.put("id", account.getID());
			if (account.getParent() != null) {
				jsonObject.put("parent", account.getParent().getID());
			}

			jsonObject.put(
					"type",
					context.getPickListContext().get("AccountType",
							Utility.getAccountTypeString(account.getType())));
			jsonObject.put("currency", account.getCurrency());
			jsonObject.put("inactive", account.getIsActive());
			jsonObject.put("description", account.getComment());
			jsonObject.put("creditLimit", account.getCreditLimit());
			jsonObject.put("cardLimit", account.getCardOrLoanNumber());
			jsonObject.put("currentBalance", account.getCurrentBalance());
			jsonObject.put("totalBalance", account.getTotalBalance());
			jsonObject.put("paypalEmail", account.getPaypalEmail());
			jsonObject.put("lastCheckNum", account.getLastCheckNum());
			jsonObject.put("cashFlowCategory", account.getCashFlowCategory());
			jsonObject.put("openingBalance", account.getOpeningBalance());
			jsonObject.put("currencyFactor", account.getCurrencyFactor());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
}