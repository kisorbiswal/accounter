package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.TransferFund;

public class TransferFundMigrator extends TransactionMigrator<TransferFund> {
	@Override
	public JSONObject migrate(TransferFund obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		jsonObj.put("amount", obj.getTotal());
		// DepositFrom
		Account depositFrom = obj.getDepositFrom();
		if (depositFrom != null) {
			JSONObject account = new JSONObject();
			account.put("name", depositFrom.getName());
			jsonObj.put("transferFrom", account);
		}
		// DepositTo
		Account depositIn = obj.getDepositIn();
		if (depositIn != null) {
			JSONObject account = new JSONObject();
			account.put("name", depositIn.getName());
			jsonObj.put("transferTo", account);
		}
		return jsonObj;
	}
}
