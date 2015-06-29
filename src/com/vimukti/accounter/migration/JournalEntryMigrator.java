package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionItem;

public class JournalEntryMigrator extends TransactionMigrator<JournalEntry> {

	@Override
	public JSONObject migrate(JournalEntry entry, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(entry, context);
		// Journal Entry items
		JSONArray jeItems = new JSONArray();
		for (TransactionItem item : entry.getTransactionItems()) {
			JSONObject jeItem = new JSONObject();
			// Journal Entry Item Account
			{
				JSONObject jeItemAccount = new JSONObject();
				jeItemAccount.put("name", item.getAccount().getName());
				jeItem.put("account", jeItemAccount);
			}
			jeItem.put("memo", item.getDescription());
			double total = item.getLineTotal();
			if (total > 0) {
				jeItem.put("type", "Debit");
				jeItem.put("debitAmount", total);
			} else {
				jeItem.put("type", "Credit");
				jeItem.put("creditAmount", total);
			}
			jeItems.put(jeItem);
		}
		jsonObject.put("journalEntryItems", jeItems);
		return jsonObject;
	}

	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("type", Transaction.TYPE_JOURNAL_ENTRY));
		criteria.add(Restrictions.eq("involvedAccount", null));
		criteria.add(Restrictions.eq("involvedPayee", null));
	}
}
