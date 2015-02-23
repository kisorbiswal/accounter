package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Employee;
import com.vimukti.accounter.core.PayEmployee;
import com.vimukti.accounter.core.TransactionPayEmployee;

public class PayEmployeeMigrator extends TransactionMigrator<PayEmployee> {
	@Override
	public JSONObject migrate(PayEmployee obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);
		jsonObj.put("payFrom",
				context.get("Account", obj.getPayAccount().getID()));
		{
			List<TransactionPayEmployee> transactionPayEmployee = obj
					.getTransactionPayEmployee();
			JSONArray array = new JSONArray();
			for (TransactionPayEmployee item : transactionPayEmployee) {
				JSONObject jsonObject = new JSONObject();
				jsonObj.put("payRun",
						context.get("PayRun", item.getPayRun().getID()));
				// TODO payment not found
				array.put(jsonObject);
			}
			jsonObj.put("payEmployeeItems", array);
		}
		Employee employee = obj.getEmployee();
		if (employee != null) {
			jsonObj.put(
					"type",
					context.getPickListContext().get("PayEmployeeType",
							"Employee"));
			jsonObj.put("employee",
					context.get("Employee", obj.getEmployee().getID()));
		} else {
			jsonObj.put(
					"type",
					context.getPickListContext().get("EmployeeGroup",
							"EmployeeGroup"));
			jsonObj.put("employeeGroup", context.get("EmployeeGroup", obj
					.getEmployeeGroup().getID()));
		}
		return jsonObj;
	}
}
