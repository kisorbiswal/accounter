package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerCreditMemo;

public class CreditMemoMigrator extends TransactionMigrator<CustomerCreditMemo> {
	@Override
	public JSONObject migrate(CustomerCreditMemo obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		jsonObj.put("payee", obj.getCustomer());
		return jsonObj;
	}
}
