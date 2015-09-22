package com.vimukti.accounter.migration;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Utility;

public class AccountMigrator implements IMigrator<Account> {

	String[] names = new String[] { "Account Receivable", "Account Payable",
			"Central Sales Tax Payable", "Tax Filed", "Opening Balances",
			"Exchange Loss or Gain", "Salaries Payable", "Inventory Assets",
			"TDS Tax Payable", "Service Tax Payable", "Cost Of Goods Sold" };

	@Override
	public JSONObject migrate(Account account, MigratorContext context)
			throws JSONException {
		List<String> asList = Arrays.asList(names);
		if (account.isDefault() || asList.contains(account.getName())
				|| account.getName().equals("Debtors")
				|| account.getName().equals("Creditors")) {
			return null;
		}
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(account, jsonObject, context);
		jsonObject.put("name", account.getName());
		FinanceDate asOf = account.getAsOf();
		if (asOf != null) {
			jsonObject.put("asOf", asOf.getAsDateObject().getTime());
		} else {
			jsonObject.put("asOf", account.getCreatedDate().getTime());
		}
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
		jsonObject.put("number", context.getNextAccountNumber());
		jsonObject.put("inactive", !account.getIsActive());
		jsonObject.put("description", account.getComment());
		jsonObject.put("openingBalance", account.getOpeningBalance());
		jsonObject.put("payPalEmail", account.getPaypalEmail());
		jsonObject.put("cashFlowCategory",
				Utility.getCashFlowCategoryName(account.getCashFlowCategory()));
		jsonObject.put("currencyFactor", account.getCurrencyFactor());
		jsonObject.put("isIncrease", account.isIncrease());
		return jsonObject;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.ne("type", 2));
	}
}