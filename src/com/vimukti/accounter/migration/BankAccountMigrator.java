package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;
import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.Utility;

public class BankAccountMigrator implements IMigrator<BankAccount> {

	@Override
	public JSONObject migrate(BankAccount bankAccount, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(bankAccount, jsonObject, context);
		jsonObject.put("bankName", bankAccount.getBank().getName());
		jsonObject.put("bankAccountType",
				Utility.getBankAccountType(bankAccount.getBankAccountType()));
		jsonObject.put("bankAccountNumber", bankAccount.getBankAccountNumber());
		jsonObject.put("number", bankAccount.getNumber());
		jsonObject.put("name", bankAccount.getName());
		jsonObject.put("asOf", bankAccount.getAsOf());
		if (bankAccount.getParent() != null) {
			jsonObject.put("subAccountOf",
					context.get("Account", bankAccount.getParent().getID()));
		}

		jsonObject.put("type", PicklistUtilMigrator
				.getAccountTypeIdentity(bankAccount.getType()));
		jsonObject.put("currency", bankAccount.getCurrency().getFormalName());
		jsonObject.put("inactive", !bankAccount.getIsActive());
		jsonObject.put("description", bankAccount.getComment());
		jsonObject.put("openingBalance", bankAccount.getOpeningBalance());
		jsonObject.put("paypalEmail", bankAccount.getPaypalEmail());
		jsonObject.put("cashFlowCategory", Utility
				.getCashFlowCategoryName(bankAccount.getCashFlowCategory()));
		jsonObject.put("currencyFactor", bankAccount.getCurrencyFactor());
		jsonObject.put("isIncrease", bankAccount.isIncrease());
		return jsonObject;
	}
}