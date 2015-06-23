package com.vimukti.accounter.migration;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Reconciliation;
import com.vimukti.accounter.core.ReconciliationItem;

public class ReconciliationMigrator implements IMigrator<Reconciliation> {
	@Override
	public JSONObject migrate(Reconciliation reconciliation,
			MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", reconciliation.getName());
		jsonObject.put("account",
				context.get("Account", reconciliation.getAccount().getID()));
		jsonObject.put("startDate", reconciliation.getStartDate()
				.getAsDateObject().getTime());
		jsonObject
				.put("endDate", reconciliation.getEndDate().getAsDateObject().getTime());
		jsonObject.put("reconciliationDate", reconciliation
				.getReconcilationDate().getAsDateObject().getTime());
		jsonObject.put("closingBalance", reconciliation.getClosingBalance());
		Set<ReconciliationItem> items = reconciliation.getItems();
		JSONArray array = new JSONArray();
		for (ReconciliationItem item : items) {
			JSONObject jsonObject1 = new JSONObject();
			jsonObject1.put("transactionNumber", item.getTransactionNo());
			jsonObject1.put("transactionDate", item.getTransactionDate()
					.getAsDateObject().getTime());
			jsonObject1.put("memo", item.getTransctionMemo());
			jsonObject1.put("amount", item.getAmount());
			array.put(jsonObject1);
			// TODO accountTransaction type is AccountTransaction in obj. not
			// available in java file
		}
		jsonObject.put("reconciliationItems", array);
		// TODO clearedAmount,difference are not available in java file
		return jsonObject;
	}
}