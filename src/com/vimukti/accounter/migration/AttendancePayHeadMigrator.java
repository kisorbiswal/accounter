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
		payHead.put("expenseAccount", obj.getAccount());
		payHead.put("calculationType", obj.getCalculationType());
		payHead.put("paySlipName", obj.getNameToAppearInPaySlip());
		payHead.put("isDeduction", obj.isDeduction());
		payHead.put("isEarning", obj.isEarning());
		Account assetAccount = obj.getAssetAccount();
		if (assetAccount != null) {
			payHead.put("assetAccount",
					context.get("Account", assetAccount.getID()));
		}
		Account liabilityAccount = obj.getLiabilityAccount();
		if (liabilityAccount != null) {
			payHead.put("statutoryLiabilityAccount",
					context.get("Account", liabilityAccount.getID()));
		}
		payHead.put("calculationPeriod", PicklistUtilMigrator
				.getCalculationPeriod(obj.getCalculationPeriod()));
		payHead.put("productionType", context.get("AttendanceOrProductionType",
				obj.getProductionType().getID()));
		payHead.put("perDayCalculationBasis", PicklistUtilMigrator
				.getPerdayCalculationBasis(obj.getCalculationPeriod()));
		payHead.put("earningDeductionOn",
				PicklistUtilMigrator.getAttendanceType(obj.getAttendanceType()));
		// TODO PayHead.obj has
		// isFromTimeSheet,formulaItems,lastComputedValue,otherPayHead,
		// ,attendanceLeaveWithPay,
		// userDefinedCalendar,computationSlabs
		// not in PayHead.java
		return payHead;
	}
}