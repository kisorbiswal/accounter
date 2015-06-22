package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.Unit;

public class InventoryItemMigrator implements IMigrator<Item> {
	@Override
	public JSONObject migrate(Item item, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(item, jsonObject);
		jsonObject.put("name", item.getName());
		jsonObject.put("isSubItemOf", item.isSubItemOf());
		jsonObject.put("subItemOf",
				context.get("Item", item.getParentItem().getID()));
		jsonObject.put("iSellThisService", item.isISellThisItem());
		jsonObject.put("salesDescription", item.getSalesDescription());
		jsonObject.put("salesPrice", item.getSalesPrice());
		jsonObject.put("incomeAccount",
				context.get("Account", item.getIncomeAccount().getID()));
		jsonObject.put("isTaxable", item.isTaxable());
		jsonObject.put("isCommissionItem", item.isCommissionItem());
		jsonObject.put("standardCost", item.getStandardCost());
		jsonObject.put("itemGroup",
				context.get("ItemGroup", item.getItemGroup().getID()));
		jsonObject.put("inActive", !item.isActive());
		jsonObject.put("iBuyThisService", item.isIBuyThisItem());
		jsonObject.put("purchaseDescription", item.getPurchaseDescription());
		jsonObject.put("purchasePrice", item.getPurchasePrice());
		jsonObject.put("expenseAccount",
				context.get("Account", item.getExpenseAccount().getID()));
		jsonObject.put("preferredVendor",
				context.get("Vendor", item.getPreferredVendor().getID()));
		jsonObject.put("vendorServiceNumber", item.getVendorItemNumber());
		jsonObject.put(
				"itemType",
				context.getPickListContext().get(
						"ItemType",
						PicklistUtilMigrator.getItemTypeIdentifier(item
								.getType())));
		Quantity quantity = item.getOnhandQty();
		if (quantity != null) {
			JSONObject quantityJSON = new JSONObject();
			quantityJSON.put("value", quantity.getValue());
			Unit unit = quantity.getUnit();
			if (unit != null) {
				quantityJSON.put("unit", context.get("Unit", unit.getID()));
			}
			jsonObject.put("quantityItem", quantityJSON);
		}
		jsonObject.put("assetAccount", item.getAssestsAccount());
		jsonObject.put("reOrderPoint", item.getReorderPoint());
		jsonObject.put("costOfGoodsSold", item.getExpenseAccount());
		jsonObject.put("warehouse", item.getWarehouse());
		jsonObject.put("measurement",
				context.get("Measurement", item.getMeasurement().getID()));
		jsonObject.put("averageCost", item.getAverageCost());
		return jsonObject;
	}

	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("itemType", Item.TYPE_INVENTORY_PART));
	}
}
