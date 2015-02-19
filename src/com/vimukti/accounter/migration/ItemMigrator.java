package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Item;

public class ItemMigrator implements IMigrator<Item> {

	@Override
	public JSONObject migrate(Item item, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();

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
		jsonObject.put("itemType", item.getType());
		jsonObject.put("onHandQuantity", item.getOnhandQty());
		return jsonObject;
	}

}