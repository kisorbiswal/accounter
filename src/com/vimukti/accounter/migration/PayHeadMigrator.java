package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.PayHead;
import com.vimukti.accounter.web.client.core.ClientPayHead;

public class PayHeadMigrator implements IMigrator<PayHead> {

	@Override
	public JSONObject migrate(PayHead obj, MigratorContext context)
			throws JSONException {
		JSONObject payHead = new JSONObject();
		payHead.put("name", obj.getName());
		payHead.put(
				"payHeadType",
				context.getPickListContext().get("PayHeadType",
						ClientPayHead.getPayHeadType(obj.getType())));
		payHead.put("isAffectNetSalary", obj.isAffectNetSalary());
		payHead.put("expenseAccount", obj.getAccount());
		payHead.put("calculationType", obj.getCalculationType());
		payHead.put("paySlipName", obj.getNameToAppearInPaySlip());
		payHead.put("isDeduction", obj.isDeduction());
		payHead.put("isEarning", obj.isEarning());
		payHead.put("assetAccount",
				context.get("Account", obj.getAssetAccount().getID()));
		payHead.put("statutoryLiabilityAccount",
				context.get("Account", obj.getLiabilityAccount().getID()));
		// TODO PayHead.obj has
		// isFromTimeSheet,formulaItems,lastComputedValuer,otherPayHead,
		// earningDeductionOn
		// ,calculationPeriod,attendanceLeaveWithPay,productionType,
		// perDayCalculationBasis,userDefinedCalendar,computeOn,computationSlabs
		// not in PayHead.java
		return payHead;
	}

}