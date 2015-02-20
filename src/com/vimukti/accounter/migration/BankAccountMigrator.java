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
		jsonObject.put("bankName", bankAccount.getBank().getName());
		jsonObject.put(
				"bankAccountType",
				context.getPickListContext().get(
						"BankAccountType",
						Utility.getBankAccountType(bankAccount
								.getBankAccountType())));
		jsonObject.put("bankAccountNumber", bankAccount.getBankAccountNumber());
		return jsonObject;
	}
}