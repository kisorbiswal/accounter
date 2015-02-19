package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.PayHead;
import com.vimukti.accounter.web.client.core.ClientPayHead;

public class PayHeadMigrator {
	public static JSONObject migrate(PayHead payhead, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", payhead.getName());
		jsonObject.put(
				"payHeadType",
				context.getPickListContext().get("PayHeadType",
						ClientPayHead.getPayHeadType(payhead.getType())));
		jsonObject.put("isAffectNetSalary", payhead.isAffectNetSalary());
		jsonObject.put("expenseAccount", payhead.getAccount());
		jsonObject.put("calculationType", payhead.getCalculationType());
		jsonObject.put("paySlipName", payhead.getNameToAppearInPaySlip());
		jsonObject.put("isDeduction", payhead.isDeduction());
		jsonObject.put("isEarning", payhead.isEarning());
		jsonObject.put("assetAccount",
				context.get("Account", payhead.getAssetAccount().getID()));
		jsonObject.put("statutoryLiabilityAccount",
				context.get("Account", payhead.getLiabilityAccount().getID()));
		// TODO PayHead.obj has
		// isFromTimeSheet,formulaItems,lastComputedValuer,otherPayHead,
		// earningDeductionOn
		// ,calculationPeriod,attendanceLeaveWithPay,productionType,
		// perDayCalculationBasis,userDefinedCalendar,computeOn,computationSlabs
		// not in PayHead.java
		return jsonObject;
	}
}