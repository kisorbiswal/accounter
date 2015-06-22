package com.vimukti.accounter.migration;

import com.vimukti.accounter.core.AccounterClass;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Job;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.Location;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Utility;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

public class JournalEntryMigrator implements IMigrator<JournalEntry> {

	@Override
	public JSONObject migrate(JournalEntry entry, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(entry, jsonObject);
		jsonObject.put("date", entry.getDate().getAsDateObject());
		jsonObject.put("number", entry.getNumber());
		AccounterClass accounterClass = entry.getAccounterClass();
		if (accounterClass != null) {
			jsonObject.put("accountClass",
					context.get("AccountClass", accounterClass.getID()));
		}
		Location location = entry.getLocation();
		if (location != null) {
			jsonObject.put("location",
					context.get("Location", location.getID()));
		}
		Currency currency = entry.getCurrency();
		if (currency != null) {
			jsonObject.put("currency", currency.getName());
		}
		jsonObject.put("currencyFactor", entry.getCurrencyFactor());
		jsonObject.put("notes", entry.getMemo());
		jsonObject
				.put("isAutomaticTransaction", entry.isAutomaticTransaction());
		jsonObject.put(
				"transactionType",
				context.getPickListContext().get("TransactionType",
						Utility.getTransactionName(entry.getType())));
		jsonObject.put(
				"status",
				context.getPickListContext().get("TransactionStatus",
						Utility.getStatus(entry.getType(), entry.getStatus())));
		Job job = entry.getJob();
		if (job != null) {
			jsonObject.put("project", context.get("Job", job.getID()));
		}
		CommonFieldsMigrator.migrateCommonFields(entry, jsonObject);
		jsonObject.put("payee", context.get("BusinessRelationship", entry
				.getInvolvedPayee().getID()));
		for (TransactionItem item : entry.getTransactionItems()) {
			JSONObject transactionItem = new JSONObject();
			transactionItem.put("account",
					context.get("Account", item.getAccount().getID()));
			transactionItem.put("memo", item.getDescription());
			double total = item.getLineTotal();
			if (total > 0) {
				transactionItem.put("type", "Debit");
				transactionItem.put("debitAmount", total);
			} else {
				transactionItem.put("type", "Credit");
				transactionItem.put("creditAmount", total);
			}
		}
		return jsonObject;
	}

	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("type", Transaction.TYPE_JOURNAL_ENTRY));
		criteria.add(Restrictions.eq("involvedAccount", null));
		criteria.add(Restrictions.eq("involvedPayee", null));
	}
}
