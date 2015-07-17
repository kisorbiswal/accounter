package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Bank;
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
		Bank bank = bankAccount.getBank();
		if (bank != null) {
			jsonObject.put("bankName", bank.getName());
		}
		jsonObject.put("bankAccountType",
				getBankAccountTypeIdentity(bankAccount.getBankAccountType()));
		String bankAccountNumber = bankAccount.getBankAccountNumber();
		if (bankAccountNumber != null) {
			jsonObject.put("bankAccountNumber", bankAccountNumber);
		}
		jsonObject.put("number", context.getNextAccountNumber());
		jsonObject.put("name", bankAccount.getName());
		FinanceDate asOf = bankAccount.getAsOf();
		if (asOf != null) {
			jsonObject.put("asOf", asOf.getAsDateObject().getTime());
		} else {
			jsonObject.put("asOf", bankAccount.getCreatedDate().getTime());
		}
		if (bankAccount.getParent() != null) {
			jsonObject.put("subAccountOf",
					context.get("Account", bankAccount.getParent().getID()));
		}
		// Type
		JSONObject accountTypeJSON = new JSONObject();
		accountTypeJSON.put("identity", PicklistUtilMigrator
				.getAccountTypeIdentity(bankAccount.getType()));
		jsonObject.put("type", accountTypeJSON);
		// Currency
		JSONObject currencyJSON = new JSONObject();
		currencyJSON.put("identity", bankAccount.getCurrency().getFormalName());
		jsonObject.put("currency", currencyJSON);

		jsonObject.put("currencyFactor", bankAccount.getCurrencyFactor());
		jsonObject.put("inactive", !bankAccount.getIsActive());
		jsonObject.put("description", bankAccount.getComment());
		jsonObject.put("openingBalance", bankAccount.getOpeningBalance());
		jsonObject.put("payPalEmail", bankAccount.getPaypalEmail());
		jsonObject.put("cashFlowCategory", Utility
				.getCashFlowCategoryName(bankAccount.getCashFlowCategory()));
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

	@Override
	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("type", 2));
	}
}