package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.ItemGroup;
import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.Warehouse;

public class InventoryItemMigrator implements IMigrator<Item> {
	@Override
	public JSONObject migrate(Item item, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(item, jsonObject, context);
		jsonObject.put("name", item.getName());
		jsonObject.put("isSubItemOf", item.isSubItemOf());
		Item parentItem = item.getParentItem();
		if (parentItem != null) {
			jsonObject
					.put("subItemOf", context.get("Item", parentItem.getID()));
		}
		jsonObject.put("iSellThisService", item.isISellThisItem());
		jsonObject.put("salesDescription", item.getSalesDescription());
		jsonObject.put("salesPrice", item.getSalesPrice());
		Account incomeAccount = item.getIncomeAccount();
		if (incomeAccount != null) {
			JSONObject account = new JSONObject();
			account.put("name", incomeAccount.getName());
			jsonObject.put("incomeAccount", account);
		}
		jsonObject.put("isTaxable", item.isTaxable());
		jsonObject.put("isCommissionItem", item.isCommissionItem());
		jsonObject.put("standardCost", item.getStandardCost());
		ItemGroup itemGroup = item.getItemGroup();
		if (itemGroup != null) {
			jsonObject.put("itemGroup",
					context.get("ItemGroup", itemGroup.getID()));
		}
		jsonObject.put("inActive", !item.isActive());
		jsonObject.put("iBuyThisService", item.isIBuyThisItem());
		jsonObject.put("purchaseDescription", item.getPurchaseDescription());
		jsonObject.put("purchasePrice", item.getPurchasePrice());
		Account expenseAccount = item.getExpenseAccount();
		if (expenseAccount != null) {
			JSONObject account = new JSONObject();
			account.put("name", expenseAccount.getName());
			jsonObject.put("expenseAccount", account);
		}
		Vendor preferredVendor = item.getPreferredVendor();
		if (preferredVendor != null) {
			jsonObject.put("preferredVendor",
					context.get("Vendor", preferredVendor.getID()));
		}
		jsonObject.put("vendorServiceNumber", item.getVendorItemNumber());
		jsonObject.put("itemType",
				PicklistUtilMigrator.getItemTypeIdentifier(item.getType()));
		Quantity quantity = item.getOnhandQty();
		if (quantity != null) {
			JSONObject quantityJSON = new JSONObject();
			quantityJSON.put("value", quantity.getValue());
			Unit unit = quantity.getUnit();
			if (unit != null) {
				quantityJSON.put("unit", context.get("Unit", unit.getID()));
			}
			jsonObject.put("onHandQuantity", quantityJSON);
		}
		// assertAccount
		Account assestsAccount = item.getAssestsAccount();
		if (assestsAccount != null) {
			JSONObject acccount = new JSONObject();
			acccount.put("name", assestsAccount.getName());
			jsonObject.put("assetAccount", acccount);
		}
		Quantity reorderPoint = item.getReorderPoint();
		if (reorderPoint != null) {
			JSONObject quantityJSON = new JSONObject();
			quantityJSON.put("value", reorderPoint.getValue());
			Unit unit = reorderPoint.getUnit();
			if (unit != null) {
				quantityJSON.put("unit", context.get("Unit", unit.getID()));
			}
			jsonObject.put("quantityJSON", quantityJSON);
		}
		Account costOfGoodsSold = item.getExpenseAccount();
		if (costOfGoodsSold != null) {
			JSONObject account = new JSONObject();
			account.put("name", costOfGoodsSold.getName());
			jsonObject.put("costOfGoodsSold", account);
		}
		Warehouse warehouse = item.getWarehouse();
		if (warehouse != null) {
			jsonObject.put("warehouse", warehouse.getID());
		}
		Measurement measurement = item.getMeasurement();
		if (measurement != null) {
			jsonObject.put("measurement",
					context.get("Measurement", measurement.getID()));
		}

		jsonObject.put("averageCost", item.getAverageCost());
		return jsonObject;
	}

	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("type", Item.TYPE_INVENTORY_PART));
	}
}
