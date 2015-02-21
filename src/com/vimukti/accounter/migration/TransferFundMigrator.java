package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TransferFund;

public class TransferFundMigrator extends TransactionMigrator<TransferFund> {
	@Override
	public JSONObject migrate(TransferFund obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		jsonObj.put("amount", obj.getTotal());
		jsonObj.put("transferFrom",
				context.get("Account", obj.getDepositFrom().getID()));
		jsonObj.put("transferTo",
				context.get("Account,", obj.getDepositIn().getID()));
		return jsonObj;
	}
}
