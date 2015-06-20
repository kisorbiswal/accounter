package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
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
		payHead.put("isFromTimeSheet", false);
		// TODO PayHead.obj has
		// isFromTimeSheet,formulaItems,lastComputedValuer,otherPayHead,
		// earningDeductionOn
		// ,calculationPeriod,attendanceLeaveWithPay,productionType,
		// perDayCalculationBasis,userDefinedCalendar,computeOn,computationSlabs
		// not in PayHead.java
		return payHead;
	}

}