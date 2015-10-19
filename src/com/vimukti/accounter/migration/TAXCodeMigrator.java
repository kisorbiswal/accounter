package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXGroup;
import com.vimukti.accounter.core.TAXItemGroup;

public class TAXCodeMigrator implements IMigrator<TAXCode> {

	@Override
	public JSONObject migrate(TAXCode taxcode, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(taxcode, jsonObject, context);
		jsonObject.put("name", taxcode.getName());
		jsonObject.put("isTaxable", taxcode.isTaxable());
		boolean isSalesTaxGroupNotEmpty = false;
		boolean isPurchaseTaxGroupNotEmpty = false;
		// Sales Tax
		TAXItemGroup taxItemGrpForSales = taxcode.getTAXItemGrpForSales();
		if (taxItemGrpForSales != null) {
			if (taxItemGrpForSales instanceof TAXGroup) {
				TAXGroup group = (TAXGroup) taxItemGrpForSales;
				if (group.getTAXItems().isEmpty()) {
					jsonObject.put("isTaxable", false);
				} else {
					isSalesTaxGroupNotEmpty = true;
					jsonObject.put("taxItemOrGroupForSales",
							context.get("Tax", taxItemGrpForSales.getID()));
				}
			} else {
				jsonObject.put("taxItemOrGroupForSales",
						context.get("Tax", taxItemGrpForSales.getID()));
				isSalesTaxGroupNotEmpty = true;
			}
		}
		// Purchase Tax
		TAXItemGroup taxItemGrpForPurchases = taxcode
				.getTAXItemGrpForPurchases();
		if (taxItemGrpForPurchases != null) {
			if (taxItemGrpForPurchases instanceof TAXGroup) {
				TAXGroup group = (TAXGroup) taxItemGrpForPurchases;
				if (group.getTAXItems().isEmpty()) {
					jsonObject.put("isTaxable", false);
				} else {
					isPurchaseTaxGroupNotEmpty = true;
					jsonObject.put("taxItemOrGroupForPurchases",
							context.get("Tax", taxItemGrpForPurchases.getID()));
				}
			} else {
				jsonObject.put("taxItemOrGroupForPurchases",
						context.get("Tax", taxItemGrpForPurchases.getID()));
				isPurchaseTaxGroupNotEmpty = true;
			}
		}
		if (isSalesTaxGroupNotEmpty || isPurchaseTaxGroupNotEmpty) {
			jsonObject.put("isTaxable", taxcode.isTaxable());
		}
		jsonObject.put("description", taxcode.getDescription());
		jsonObject.put("isInactive", !taxcode.isActive());
		return jsonObject;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub
		
	}
}