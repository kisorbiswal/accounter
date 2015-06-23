package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.PayrollUnit;

public class PayrollUnitMigrator implements IMigrator<PayrollUnit> {
	@Override
	public JSONObject migrate(PayrollUnit payrollunit, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(payrollunit, jsonObject,
				context);
		jsonObject.put("symbol", payrollunit.getSymbol());
		jsonObject.put("formalName", payrollunit.getFormalname());
		jsonObject.put("numberOfDecimalPlaces",
				payrollunit.getNoofDecimalPlaces());
		return jsonObject;
	}
}