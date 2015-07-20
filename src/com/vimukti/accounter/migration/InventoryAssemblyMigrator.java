package com.vimukti.accounter.migration;

import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.InventoryAssembly;
import com.vimukti.accounter.core.InventoryAssemblyItem;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.ItemGroup;
import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.Warehouse;

public class InventoryAssemblyMigrator implements IMigrator<InventoryAssembly> {
	@Override
	public JSONObject migrate(InventoryAssembly item, MigratorContext context)
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
			jsonObject.put("incomeAccount",
					context.get("Account", incomeAccount.getID()));
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
			jsonObject.put("expenseAccount",
					context.get("Account", expenseAccount.getID()));
		}
		Vendor preferredVendor = item.getPreferredVendor();
		if (preferredVendor != null) {
			jsonObject.put("preferredVendor",
					context.get("Vendor", preferredVendor.getID()));
		}

		jsonObject.put("vendorServiceNumber", item.getVendorItemNumber());
		jsonObject.put("itemType",
				PicklistUtilMigrator.getItemTypeIdentifier(item.getType()));
		Account assestsAccount = item.getAssestsAccount();
		if (assestsAccount != null) {
			jsonObject.put("assetAccount",
					context.get("Account", assestsAccount.getID()));
		}

		Quantity reorderPoint = item.getReorderPoint();
		if (reorderPoint != null) {
			double value = reorderPoint.getValue();
			Unit unit = reorderPoint.getUnit();
			if (unit != null) {
				jsonObject.put("reOrderPoint", unit.getFactor() * value);
			} else {
				jsonObject.put("reOrderPoint", value);
			}
		}
		Account costOfGoodsSold = item.getExpenseAccount();
		if (costOfGoodsSold != null) {
			jsonObject.put("costOfGoodsSold",
					context.get("Account", costOfGoodsSold.getID()));
		}
		Warehouse warehouse = item.getWarehouse();
		if (warehouse != null) {
			jsonObject.put("warehouse",
					context.get("Warehouse", warehouse.getID()));
		}
		Measurement measurement = item.getMeasurement();
		if (measurement != null) {
			jsonObject.put("measurement",
					context.get("Measurement", measurement.getID()));
		}

		jsonObject.put("averageCost", item.getAverageCost());

		Set<InventoryAssemblyItem> components = item.getComponents();
		JSONArray array = new JSONArray();
		for (InventoryAssemblyItem obj : components) {
			JSONObject jsonitem = new JSONObject();
			Item inventoryItem = obj.getInventoryItem();
			if (inventoryItem != null) {
				jsonitem.put("item", context.get("Item", inventoryItem.getID()));
			}
			jsonitem.put("description", obj.getDiscription());
			array.put(jsonitem);
		}
		jsonObject.put("assemblyItems", array);
		return jsonObject;
	}

	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.eq("type", Item.TYPE_INVENTORY_ASSEMBLY));
	}
}
