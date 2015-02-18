package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.User;

public class AccountMigrator {

	public static JSONObject migrate(Account account) throws JSONException {

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("number", account.getNumber());

		jsonObject.put("name", account.getName());

		jsonObject.put("asOf", account.getAsOf());

		jsonObject.put("type", account.getType());

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

		return jsonObject;
	}
}