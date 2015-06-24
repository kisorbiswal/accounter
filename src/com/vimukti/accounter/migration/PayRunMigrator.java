package com.vimukti.accounter.migration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.AttendanceManagementItem;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.PayRun;
import com.vimukti.accounter.core.UserDefinedPayheadItem;
import com.vimukti.accounter.core.Utility;

public class PayRunMigrator implements IMigrator<PayRun> {
	@Override
	public JSONObject migrate(PayRun obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		jsonObject.put("date", obj.getDate().getAsDateObject().getTime());
		jsonObject.put("number", obj.getNumber());
		Currency currency = obj.getCurrency();
		if (currency != null) {
			JSONObject currencyJSON = new JSONObject();
			currencyJSON.put("identity", currency.getFormalName());
			jsonObject.put("currency", currencyJSON);
		}
		jsonObject.put("currencyFactor", obj.getCurrencyFactor());
		jsonObject.put("notes", obj.getMemo());
		jsonObject.put("transactionType",
				Utility.getTransactionName(obj.getType()));
		jsonObject.put("fromDate", obj.getPayPeriodStartDate());
		jsonObject.put("toDate", obj.getPayPeriodEndDate());
		if (obj.getEmployee() == null) {
			jsonObject.put("type", "EmployeeGroup");
			jsonObject.put("employeeGroup", context.get("EmployeeGroup", obj
					.getEmployeeGroup().getID()));

		} else {
			jsonObject.put("type", "Employee");
			jsonObject.put("employee",
					context.get("Employee", obj.getEmployee().getID()));
		}
		jsonObject.put("workingDays", obj.getNoOfWorkingDays());

		JSONArray amiItems = new JSONArray();
		for (AttendanceManagementItem item : obj.getAttendanceItems()) {
			JSONObject ami = new JSONObject();
			ami.put("Employee",
					context.get("Employee", item.getEmployee().getID()));
			JSONArray attItems = new JSONArray();
			for (UserDefinedPayheadItem payHeadItem : item
					.getUserDefinedPayheads()) {
				JSONObject attendanceManagementItems = new JSONObject();
				// TODO
				// type
				// payStrurctureItem
				// number
				attendanceManagementItems.put("amount", payHeadItem.getValue());
				attItems.put(attendanceManagementItems);
			}
			ami.put("attendanceItems", attItems);
			amiItems.put(ami);
		}
		jsonObject.put("attendanceManagementItems", amiItems);
		JSONArray payRunItems = new JSONArray();
		obj.getAccountTransactionEntriesList();
		for (AttendanceManagementItem item : obj.getAttendanceItems()) {
			JSONObject payRunItem = new JSONObject();
			payRunItem.put("Employee",
					context.get("Employee", item.getEmployee().getID()));
			payRunItem.put("absentDays", item.getAbscentDays());
			// TODO EmployeePayRunItems need to set
			payRunItems.put(payRunItem);
		}
		jsonObject.put("payRunItems", payRunItems);
		return jsonObject;
	}
}
