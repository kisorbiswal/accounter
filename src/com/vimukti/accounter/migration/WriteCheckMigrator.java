package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.WriteCheck;

public class WriteCheckMigrator extends TransactionMigrator<WriteCheck> {
	@Override
	public JSONObject migrate(WriteCheck obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		int payToType = obj.getPayToType();
		if (payToType == WriteCheck.TYPE_VENDOR) {
			jsonObj.put("payee", context.get("Vendor", obj.getVendor().getID()));
		}
		Account bankAccount = obj.getBankAccount();
		if (bankAccount != null) {
			JSONObject account = new JSONObject();
			account.put("name", bankAccount.getName());
			jsonObj.put("account", account);
		}
		jsonObj.put("inFavourOf", obj.getInFavourOf());
		jsonObj.put("amount", obj.getAmount());
		jsonObj.put("date", obj.getDate().getAsDateObject().getTime());
		jsonObj.put("toBePrinted", obj.isToBePrinted());
		try {
			jsonObj.put("chequeNumber", Long.parseLong(obj.getCheckNumber()));
		} catch (NumberFormatException nfe) {
		}
		jsonObj.put("notes", obj.getMemo());

		return jsonObj;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("payToType", WriteCheck.TYPE_VENDOR));
	}
}
