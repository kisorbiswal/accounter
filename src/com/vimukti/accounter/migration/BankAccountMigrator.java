package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.BankAccount;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Utility;

public class BankAccountMigrator implements IMigrator<BankAccount> {

	@Override
	public JSONObject migrate(BankAccount bankAccount, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(bankAccount, jsonObject,
				context);
		jsonObject.put("bankName", bankAccount.getBank().getName());
		String bankAccountType = getBankAccountTypeIdentity(bankAccount
				.getBankAccountType());
		if (bankAccountType != null) {
			jsonObject.put("bankAccountType", bankAccountType);
		}
		jsonObject.put("bankAccountNumber", bankAccount.getBankAccountNumber());
		jsonObject.put("number", bankAccount.getNumber());
		jsonObject.put("name", bankAccount.getName());
		FinanceDate asOf = bankAccount.getAsOf();
		if (asOf != null) {
			jsonObject.put("asOf", asOf.getAsDateObject().getTime());
		}
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

	String getBankAccountTypeIdentity(int type) {
		switch (type) {
		case 0:
			return "CurrentAccount";
		case 1:
			return "Checking";
		case 2:
			return "Savings";
		case 3:
			return "MoneyMarket";
		}
		return null;
	}
}