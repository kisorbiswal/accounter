package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.PaymentTerms;

public class PaymentTermsMigrator implements IMigrator<PaymentTerms> {

	@Override
	public JSONObject migrate(PaymentTerms obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		jsonObject.put("name", obj.getName());
		jsonObject.put("description", obj.getDescription());
		jsonObject.put("netDue", obj.getDue());
		jsonObject.put("dueDays", obj.getDueDays());
		jsonObject.put("discountPercentage", obj.getDiscountPercent() / 100);
		jsonObject.put("ifPaidWithIn", obj.getIfPaidWithIn());
		jsonObject.put("isDateDriven", obj.isDateDriven());
		return jsonObject;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}