package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AttendancePayHead;
import com.vimukti.accounter.web.client.core.ClientPayHead;

public class AttendancePayHeadMigrator implements IMigrator<AttendancePayHead> {

	@Override
	public JSONObject migrate(AttendancePayHead obj, MigratorContext context)
			throws JSONException {
		JSONObject payHead = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, payHead, context);
		payHead.put("name", obj.getName());
		payHead.put("payHeadType", ClientPayHead.getPayHeadType(obj.getType()));
		payHead.put("isAffectNetSalary", obj.isAffectNetSalary());
		Account expenseAccount = obj.getAccount();
		if (expenseAccount != null) {
			JSONObject account = new JSONObject();
			account.put("name", expenseAccount.getName());
			payHead.put("expenseAccount", account);
		}
		payHead.put("calculationType", PicklistUtilMigrator
				.getCalculationType(obj.getCalculationType()));
		payHead.put("paySlipName", obj.getNameToAppearInPaySlip());
		payHead.put("isDeduction", obj.isDeduction());
		payHead.put("isEarning", obj.isEarning());
		Account assetAccount = obj.getAssetAccount();
		if (assetAccount != null) {
			JSONObject account = new JSONObject();
			account.put("name", assetAccount.getName());
			payHead.put("assetAccount", account);
		}
		Account liabilityAccount = obj.getLiabilityAccount();
		if (liabilityAccount != null) {
			JSONObject account = new JSONObject();
			account.put("name", liabilityAccount.getName());
			payHead.put("statutoryLiabilityAccount", account);
		}
		payHead.put("calculationPeriod", PicklistUtilMigrator
				.getCalculationPeriod(obj.getCalculationPeriod()));
		if (PicklistUtilMigrator.getCalculationType(obj.getCalculationType())
				.equals("OnProduction")) {
			payHead.put("productionType", context.get(
					"AttendanceOrProductionType", obj.getProductionType()
							.getID()));
		} else {
			payHead.put("userDefinedCalendar", context.get(
					"AttendanceOrProductionType", obj.getProductionType()
							.getID()));
		}
		payHead.put("perDayCalculationBasis", PicklistUtilMigrator
				.getPerdayCalculationBasis(obj.getCalculationPeriod()));
		payHead.put("earningDeductionOn",
				PicklistUtilMigrator.getAttendanceType(obj.getAttendanceType()));
		payHead.put("otherPayHead", obj.getPayhead());
		// TODO PayHead.obj has
		// isFromTimeSheet,lastComputedValue,
		// attendanceLeaveWithPay
		return payHead;
	}
}